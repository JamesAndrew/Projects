package racetrack;

import java.util.ArrayList;
import java.util.Random;

public class QLearning 
{
    // tunable parameters //
    // threshold to stop training the race track
    private final double epsilon = 0.01;
    // greedy parameter for action selection
    private final double greedy = 0.4;  
    // discount factor - low values decrement additive rewards
    private final double gamma = 0.95;
    // learning factor - lower values take longer to converge but give better results
    private final double alpha = 0.8;
    // temporary - for producing sample run output
    private final double printRate = 50;
    
    // other parameters //
    // racetrack currently being worked with 
    private final Racetrack track;
    
    /**
     * Constructor
     * @param in_track : race track to assign [state, action] values to
     */
    public QLearning(Racetrack in_track)
    {
        track = in_track;
    }
    
    /**
     * Begins the Q-Learning procedure as described in figure 21.8 on page 844
     * of Russell and Norvig.
     */
    public void learnTrack()
    {
        // delta flag variable used to check the difference between a Q-Value before and after update.
        // Once all q-value update deltas are less than epsilon, this flag stays as true and the suite exits
        boolean delta;
        Random random = new Random();
        int suiteIteration = 0;
        
        // repeat until delta threshold is met
        do
        {
            System.out.format("Current generation: %d%n", suiteIteration);                                         //            
            
            int runIteration = 0;
            delta = true;
            System.out.format("  = Delta halting condition reset. =%n");                                           //
            int[] currentState; // [0]: row position, [1]: col position, [2]: row velocity, [3]: col velocity
            
            // pick a semi-arbitrary state
            if (suiteIteration == 0) 
            {
                currentState = assignInitialState();
            }
            else
            {
                System.out.format("  Picking initial state randomly.%n");                                           //
                currentState = randomValidState();
            }
            System.out.format("  Initial State -- Location: [%d,%d], Velocity: [%d,%d]%n",                          //
                currentState[0], currentState[1], currentState[2], currentState[3]);                                //
            
            // repeat agent exploration until a 'F' finish cell state is reached
            while (true)
            {
                System.out.format("  === Current agent step t: %d ===%n", runIteration);                            //         
                track.printTrackWithAgentLocation(currentState[0], currentState[1]);                                //
                
                // choose an action using an epsilon-greedy policy
                int[] action;
                double greedCase = random.nextDouble();
                
                // pick the greediest action 'greedy' percent of the time
                if (greedCase <= greedy)
                {
                    System.out.format("    Finding best action for cell (%d,%d) with velocity "                    //
                        + "(%d,%d)%n", currentState[0], currentState[1], currentState[2], currentState[3]);        //
                    
                    action = getBestAction(currentState[0], currentState[1], currentState[2], currentState[3]);
                    
                    System.out.format("    Best acceleration action found to be [%d,%d]%n", action[0], action[1]); //
                }
                // else pick a random action
                else
                {
                    System.out.format("    Finding random action for cell (%d,%d) with velocity "                  //
                        + "(%d,%d)%n", currentState[0], currentState[1], currentState[2], currentState[3]);        //
                    action = getRandomAction();
                    System.out.format("    Random acceleration action assigned to [%d,%d]%n", action[0], action[1]);//
                }

                System.out.format("    Evaluating Q(s,a) <- Q(s,a) + alpha(R(s) + gamma*maxQ(s',a') - Q(s,a))...%n");//
                // evaluate max_a'[Q(s',a')] //
                // Get next state s'
                int[] nextState = getNextState(currentState, action);
                System.out.format("      s' -- Location: [%d,%d], Velocity: [%d,%d]%n",                             //
                    nextState[0], nextState[1], nextState[2], nextState[3]);                                        //
                
                // get the maximal action a' from next state s'
                int[] nextStateMaxAction = getBestAction(nextState[0], nextState[1], nextState[2], nextState[3]);
                System.out.format("      max a': [%d,%d]%n", nextStateMaxAction[0], nextStateMaxAction[1]);         //
                
                // get the q value for the next state's best action: Q(s',a')
                Cell nextCell = track.getTrack()[nextState[0]][nextState[1]];
                QValues nextCellQValues = nextCell.getQValues(nextState[2], nextState[3]);
                double maxNextQ = nextCellQValues.getQValue(nextStateMaxAction[0], nextStateMaxAction[1]);
                System.out.format("      maxQ(s',a'): %.3f%n", maxNextQ);                                           //

                // run update formula: Q(s,a) <- Q(s,a) + alpha(R(s) + gamma*maxQ(s',a') - Q(s,a))
                Cell currentCell = track.getTrack()[currentState[0]][currentState[1]];
                double currentQ = 
                    currentCell.getQValues(currentState[2], currentState[3]).getQValue(action[0], action[1]);
                double r = currentCell.getReward();
                double qBefore = currentCell.getQValue(currentState[2], currentState[3], action[0], action[1]);
                System.out.format("      Q(s,a) before update: %.3f%n",                                             //
                    currentCell.getQValue(currentState[2], currentState[3], action[0], action[1]));                 //
                double qValue = currentQ + alpha*(r + gamma*maxNextQ - currentQ);
                
                // update the Q value
                currentCell.setQValue(currentState[2], currentState[3], action[0], action[1], qValue);
                double qAfter = currentCell.getQValue(currentState[2], currentState[3], action[0], action[1]);
                System.out.format("      Q(s,a) after update: %.3f%n",                                              //
                    currentCell.getQValue(currentState[2], currentState[3], action[0], action[1]));                 //
                
                // check q-value deltas and flip flag if difference is over the threshold
                if (Math.abs(qBefore - qAfter) > epsilon || runIteration < 10)
                {
                    System.out.format("    Delta halting condition marked as false.%n");                            //
                    delta = false;
                }
                
                // set state to be next state
                currentState[0] = nextState[0];                 // update agent row location
                currentState[1] = nextState[1];                 // update agent column location
                currentState[2] = nextState[2];                 // update agent row velocity
                currentState[3] = nextState[3];                 // update agent column velocity
                System.out.format("    Moved agent to next state -- Location: [%d,%d], Velocity: [%d,%d]%n",        //
                    currentState[0], currentState[1], currentState[2], currentState[3]);                            //
                track.printTrackWithAgentLocation(currentState[0], currentState[1]);                                //
                
                // exit agent exploration if 'F' state is reached otherwise keep exploring
                Cell agentState = track.getTrack()[currentState[0]][currentState[1]];
                if (agentState.getType() == 'F') 
                {
                    System.out.format("    Reached end state. Ending current agent run.%n%n");                      //
                    break;
                }
                else runIteration++;
            }
            
            suiteIteration++;
        }
        while (!delta);
        
        System.out.format("Exploration halting state reached. Running agent as a race from the Start location");
    }
    
    /**
     * Queries the Cell at index [trackRow,trackCol] with state [rowVelocity,colVelocity]
     * and calculates the greedy best action to take. 
     * The greedy best action is the largest Q values for the 9 possible actions 
     * that can occur from accelerating -1/0/+1 in each direction.
     * If there is a tie for best actions, a random one of the tied actions is chosen
     * 
     * @param trackRow
     * @param trackCol
     * @param rowVel
     * @param colVel
     * @return the acceleration vectors of the best action to take
     *   index [0] is row acceleration. index [1] is column acceleration
     */
    private int[] getBestAction(int trackRow, int trackCol, int rowVel, int colVel)
    {
        Cell cell = track.getTrack()[trackRow][trackCol];
        QValues values = cell.getQValues(rowVel, colVel);
        double bestActionQValue = -1000.0;
        ArrayList<int[]> bestActions = new ArrayList();
        
        // iterate through all possible actions one can take from the current state
        // (accelerate +1/-1/0 in x and y directions) and store the action that 
        // results in the largest Q(s',a') value
        
        // a_(-1,-1) case 1: accelerate up and left
        if (rowVel == -5 || colVel == -5) { /* do nothing, this action cannot occur */ }
        else
        {
            double qValue = values.getQValue(-1, -1);
            if (qValue == bestActionQValue) 
            {
                int[] action = new int[]{-1, -1};
                bestActions.add(action);
            }
            else if (qValue > bestActionQValue)
            {
                bestActions.clear();
                int[] action = new int[]{-1, -1};
                bestActions.add(action);
                bestActionQValue = qValue;
            }
        }

        // a_(-1,0) case 2: accelerate up only
        if (rowVel == -5) { /* do nothing, this action cannot occur */ }
        else
        {
            double qValue = values.getQValue(-1, 0);
            if (qValue == bestActionQValue) 
            {
                int[] action = new int[]{-1, 0};
                bestActions.add(action);
            }
            else if (qValue > bestActionQValue)
            {
                bestActions.clear();
                int[] action = new int[]{-1, 0};
                bestActions.add(action);
                bestActionQValue = qValue;
            }
        }

        // a_(-1,1) case 3: accelerate up and right
        if (colVel == -5) { /* do nothing, this action cannot occur */ }
        else
        {
            double qValue = values.getQValue(-1, 1);
            if (qValue == bestActionQValue) 
            {
                int[] action = new int[]{-1, 1};
                bestActions.add(action);
            }
            else if (qValue > bestActionQValue)
            {
                bestActions.clear();
                int[] action = new int[]{-1, 1};
                bestActions.add(action);
                bestActionQValue = qValue;
            }
        }
        
        // a_(0,-1) case 4 accelerate left only
        if (colVel == -5) { /* do nothing, this action cannot occur */ }
        else
        {
            double qValue = values.getQValue(0, -1);
            if (qValue == bestActionQValue) 
            {
                int[] action = new int[]{0, -1};
                bestActions.add(action);
            }
            else if (qValue > bestActionQValue)
            {
                bestActions.clear();
                int[] action = new int[]{0, -1};
                bestActions.add(action);
                bestActionQValue = qValue;
            }
        }

        // a_(0,0) case 5: no acceleration
        double tempQValue = values.getQValue(0, 0);
        if (tempQValue == bestActionQValue) 
        {
            int[] action = new int[]{0, 0};
            bestActions.add(action);
        }
        else if (tempQValue > bestActionQValue)
        {
            bestActions.clear();
            int[] action = new int[]{0, 0};
            bestActions.add(action);
            bestActionQValue = tempQValue;
        }


        // a_(0,1) case 6: accelerate right only
        if (colVel == 5) { /* do nothing, this action cannot occur */ }
        else
        {
            double qValue = values.getQValue(0, 1);
            if (qValue == bestActionQValue) 
            {
                int[] action = new int[]{0, 1};
                bestActions.add(action);
            }
            else if (qValue > bestActionQValue)
            {
                bestActions.clear();
                int[] action = new int[]{0, 1};
                bestActions.add(action);
                bestActionQValue = qValue;
            }
        }

        // a_(1,-1) case 7: down up and left
        if (rowVel == 5) { /* do nothing, this action cannot occur */ }
        else
        {
            double qValue = values.getQValue(1, -1);
            if (qValue == bestActionQValue) 
            {
                int[] action = new int[]{1, -1};
                bestActions.add(action);
            }
            else if (qValue > bestActionQValue)
            {
                bestActions.clear();
                int[] action = new int[]{1, -1};
                bestActions.add(action);
                bestActionQValue = qValue;
            }
        }
        
        // a_(1,0) case 8: accelerate down only
        if (rowVel == 5) { /* do nothing, this action cannot occur */ }
        else
        {
            double qValue = values.getQValue(1, 0);
            if (qValue == bestActionQValue) 
            {
                int[] action = new int[]{1, 0};
                bestActions.add(action);
            }
            else if (qValue > bestActionQValue)
            {
                bestActions.clear();
                int[] action = new int[]{1, 0};
                bestActions.add(action);
                bestActionQValue = qValue;
            }
        }

        // a_(1,1) case 9: accelerate down and right
        if (rowVel == 5 || colVel == 5) { /* do nothing, this action cannot occur */ }
        else
        {
            double qValue = values.getQValue(1, 1);
            if (qValue == bestActionQValue) 
            {
                int[] action = new int[]{1, 1};
                bestActions.add(action);
            }
            else if (qValue > bestActionQValue)
            {
                bestActions.clear();
                int[] action = new int[]{1, 1};
                bestActions.add(action);
                bestActionQValue = qValue;
            }
        }
        
        if (bestActionQValue == -1000.0 || bestActions.isEmpty()) 
        {
            throw new RuntimeException("bestAction never got assigned a real value.");
        }
        
        if (bestActions.size() == 1)
        {
            return bestActions.get(0);
        }
        else
        {
            Random random = new Random();
            int listSize = bestActions.size();
            int randomIndex = random.nextInt(listSize);
            return bestActions.get(randomIndex);
        }
    }
    
    /**
     * @return a randomized acceleration vector with values between -1 and 1
     */
    private int[] getRandomAction()
    {
        Random random = new Random();
        int rowAccel = random.nextInt(3) - 1;
        int colAccel = random.nextInt(3) - 1;
        
        int[] action = new int[]{ rowAccel, colAccel };
        return action;
    }
    
    /**
     * Use the current state and an acceleration action to get the next state
     * 
     * @param currentState : [0]: row location, [1]: col location, [2]: row velocity, [3]: col velocity
     * @param action : An array with velocity vectors: [0] row velocity, [1] column velocity
     * @return An array describing the next state as follows: 
     *    [0]: row location, [1]: col location, [2]: row velocity, [3]: col velocity
     */
    private int[] getNextState(int[] currentState, int[] action)
    {
        int currentRow = currentState[0];
        int currentCol = currentState[1];
        int rowVel = currentState[2]; 
        int colVel = currentState[3]; 
        
        int nextRow;
        int nextCol;
        int newRowVel = rowVel + action[0];
        int newColVel = colVel + action[1];
        
        // next cell-state is [row+newXVel, col+newYVel] //
        // if the finish line can be crossed, set nextCell to be the finish
        if (finishOccurs(currentRow, currentCol, newRowVel, newColVel))
        {
            int[] finishLoc = finishLocation(currentRow, currentCol, newRowVel, newColVel);
            
            nextRow = finishLoc[0];
            nextCol = finishLoc[1];
            newRowVel = 0;
            newColVel = 0;
        }
        // if collision is known to happen, set nextCell to be cell right before wall was hit with 0 velocity
        else if (collisionOccurs(currentRow, currentCol, newRowVel, newColVel))
        {
            int[] crashLoc = collisionLocation(currentRow, currentCol, newRowVel, newColVel);
            
            nextRow = crashLoc[0];
            nextCol = crashLoc[1];
            newRowVel = 0;
            newColVel = 0;
        }
        // if the next cell is normal to move to (no walls, no finish), update next cell normally
        else
        {
            nextRow = currentRow + newRowVel;
            nextCol = currentCol + newColVel;
        }
        
        return new int[]{ nextRow, nextCol, newRowVel, newColVel };
    }
    
    /**
     * Calculates if the finish line is crossed when a race car at position 
     * [row,col] moves with velocity [rowVel,colVel].
     * Note: this assumes the car always travels the full row distance first, then full column distance
     * 
     * @param row : current row location of car
     * @param col : current column location of car
     * @param rowVel : car's current row velocity
     * @param colVel : car's current column velocity
     * @return true if finish line is crossed
     */
    private boolean finishOccurs(int row, int col, int rowVel, int colVel)
    {
        if (rowVel < 0)
        {
            for (int i = row; i >= row + rowVel; i--)
            {
                if (track.getTrack()[i][col].getType() == '#') return false;
                else if (track.getTrack()[i][col].getType() == 'F') return true;
            }
        }
        else if (rowVel > 0)
        {
            for (int i = row; i <= row + rowVel; i++)
            {
                if (track.getTrack()[i][col].getType() == '#') return false;
                else if (track.getTrack()[i][col].getType() == 'F') return true;
            }
        }
        
        row = row + rowVel;
        if (colVel < 0)
        {
            for (int j = col; j >= col + colVel; j--)
            {
                if (track.getTrack()[row][j].getType() == '#') return false;
                else if (track.getTrack()[row][j].getType() == 'F') return true;
            }
        }
        else if (colVel > 0)
        {
            for (int j = col ; j <= col + colVel; j++)
            {
                if (track.getTrack()[row][j].getType() == '#') return false;
                else if (track.getTrack()[row][j].getType() == 'F') return true;
            }
        }
        
        return false;
    }
    
    /**
     * Nearly identical to 'finishOccurs' method, but returns the row and column that
     * the finish line was reached at.
     * Note: this method assumes that the finish line is known to be reachable without
     * running in to any walls
     * 
     * @param row
     * @param col
     * @param rowVel
     * @param colVel
     * @return an int[] of [row,col] when finish line crossing occurred
     */
    private int[] finishLocation(int row, int col, int rowVel, int colVel)
    {
        if (rowVel < 0)
        {
            for (int i = row; i >= row + rowVel; i--)
            {
                if (track.getTrack()[i][col].getType() == 'F') return new int[]{i, col};
            }
        }
        else if (rowVel > 0)
        {
            for (int i = row; i <= row + rowVel; i++)
            {
                if (track.getTrack()[i][col].getType() == 'F') return new int[]{i, col};
            }
        }
        
        row = row + rowVel;
        if (colVel < 0)
        {
            for (int j = col; j >= col + colVel; j--)
            {
                if (track.getTrack()[row][j].getType() == 'F') return new int[]{row, j};
            }
        }
        else if (colVel > 0)
        {
            for (int j = col ; j <= col + colVel; j++)
            {
                if (track.getTrack()[row][j].getType() == 'F') return new int[]{row, j};
            }
        }
        
        // throw exception if none of these situations worked
        throw new RuntimeException("A finish location was never reached for "
                + "row,col [" + row + "," + col + "] with velocity (" + rowVel + "," + colVel + ").");
    }
    
    /**
     * Calculates if a collision will occur when a race car at position 
     * [row,col] moves with velocity [rowVel,colVel].
     * Note: this assumes the car always travels the full row distance first, then full column distance
     * 
     * @param row : current row location of car
     * @param col : current column location of car
     * @param rowVel : car's current row velocity
     * @param colVel : car's current column velocity
     * @return true if a collusion will occur
     */
    private boolean collisionOccurs(int row, int col, int rowVel, int colVel)
    {
        if (rowVel < 0)
        {
            for (int i = row; i >= row + rowVel; i--)
            {
                if (track.getTrack()[i][col].getType() == '#') return true;
            }
        }
        else if (rowVel > 0)
        {
            for (int i = row; i <= row + rowVel; i++)
            {
                if (track.getTrack()[i][col].getType() == '#') return true;
            }
        }
        
        row = row + rowVel;
        if (colVel < 0)
        {
            for (int j = col; j >= col + colVel; j--)
            {
                if (track.getTrack()[row][j].getType() == '#') return true;
            }
        }
        else if (colVel > 0)
        {
            for (int j = col ; j <= col + colVel; j++)
            {
                if (track.getTrack()[row][j].getType() == '#') return true;
            }
        }
        
        return false;
    }
    
    /**
     * Nearly identical to 'collisionOccurs' method, but returns the row and column that
     * the collision happened at.
     * Note: this method assumes that a collision is already known to happen for the
     * given location and velocity
     * 
     * @param row
     * @param col
     * @param rowVel
     * @param colVel
     * @return an int array of the [row,col] where the collision happened
     */
    private int[] collisionLocation(int row, int col, int rowVel, int colVel)
    {
        if (rowVel < 0)
        {
            for (int i = row; i >= row + rowVel; i--)
            {
                if (track.getTrack()[i-1][col].getType() == '#') return new int[]{i,col};
            }
        }
        else if (rowVel > 0)
        {
            for (int i = row; i <= row + rowVel; i++)
            {
                if (track.getTrack()[i+1][col].getType() == '#') return new int[]{i,col};
            }
        }
        
        row = row + rowVel;
        if (colVel < 0)
        {
            for (int j = col; j >= col + colVel; j--)
            {
                if (track.getTrack()[row][j-1].getType() == '#') return new int[]{row,j};
            }
        }
        else if (colVel > 0)
        {
            for (int j = col ; j <= col + colVel; j++)
            {
                if (track.getTrack()[row][j+1].getType() == '#') return new int[]{row,j};
            }
        }
        
        throw new RuntimeException("A crash location was never reached for "
                + "row,col [" + row + "," + col + "] with velocity (" + rowVel + "," + colVel + ").");
    }
    
    /**
     * @return the [row, col, rowVel, colVel] values close to the current map's finish line
     */
    private int[] assignInitialState()
    {
        Random random = new Random();
        int[] state = new int[4];
        switch (track.getName())
        {
            case "R":
                state[0] = 24;
                state[1] = 26;
                break;
            case "L":
                state[0] = 3;
                state[1] = 34;
                break;
            case "O":
                state[0] = 14;
                state[1] = 2;
                break;
            case "simple":
                state[0] = 2;
                state[1] = 7;
                break;
            default:
                throw new RuntimeException("The current track does not have a "
                    + "name that matches with the switch statement");
        }
        
        // randomized initial velocities
        state[2] = random.nextInt(3) - 1;
        state[3] = random.nextInt(3) - 1;
        
        return state;
    }
    
    /**
     * @return A random point on the racetrack that is a '.' character
     */
    private int[] randomValidState()
    {
        Random random = new Random();
        int rowIndex = 0;
        int colIndex = 0;
        
        // generate a random row and column index within the racetrack bounds
        int randRow = 1 + random.nextInt(track.getTrack().length - 2);
        int randCol = 1 + random.nextInt(track.getTrack()[randRow].length - 2);
        
        // find the closest '.' cell to index [randRow,randCol]
        boolean found = false;
        int i = 0;
        while (!found)
        {
            // west
            if (randCol-i >= 0 && track.getTrack()[randRow][randCol-i].getType() == '.')
            {
                found = true;
                rowIndex = randRow;
                colIndex = randCol - i;
            } 
            // north-west
            else if (randRow-i >= 0 && randCol-i >= 0 
                    && track.getTrack()[randRow-i][randCol-i].getType() == '.')
            {
                found = true;
                rowIndex = randRow - i;
                colIndex = randCol - i;
            } 
            // north
            else if (randRow-i >= 0 && track.getTrack()[randRow-i][randCol].getType() == '.')
            {
                found = true;
                rowIndex = randRow - i;
                colIndex = randCol;
            } 
            // north-east
            else if (randRow-i >= 0 && randCol+i < track.getTrack()[0].length 
                    && track.getTrack()[randRow-i][randCol+i].getType() == '.')
            {
                found = true;
                rowIndex = randRow - 1;
                colIndex = randCol + 1;
            } 
            // east
            else if (randCol+i < track.getTrack()[0].length 
                    && track.getTrack()[randRow][randCol+i].getType() == '.')
            {
                found = true;
                rowIndex = randRow;
                colIndex = randCol + i;
            } 
            // south-east
            else if (randRow+i < track.getTrack().length && randCol+i < track.getTrack()[0].length 
                    && track.getTrack()[randRow+i][randCol+i].getType() == '.')
            {
                found = true;
                rowIndex = randRow + i;
                colIndex = randCol + i;
            } 
            // south
            else if (randRow+i < track.getTrack().length 
                    && track.getTrack()[randRow+i][randCol].getType() == '.')
            {
                found = true;
                rowIndex = randRow + i;
                colIndex = randCol;
            } 
            // south-west
            else if (randRow+i < track.getTrack().length && randCol-i >= 0 
                    && track.getTrack()[randRow+i][randCol-i].getType() == '.')
            {
                found = true;
                rowIndex = randRow + i;
                colIndex = randCol - i;
            } 
            else i++;
        }
        
        if (track.getTrack()[rowIndex][colIndex].getType() != '.')
            throw new RuntimeException("randomValidState() row and column indices"
                    + " didn't assign to a cell with a '.' character.");
        else
        {
            int rowVel = random.nextInt(3) - 1;
            int colVel = random.nextInt(3) - 1;
            int[] returnedIndex = new int[]{ rowIndex, colIndex, rowVel, colVel };
            return returnedIndex;
        }
    }
}
