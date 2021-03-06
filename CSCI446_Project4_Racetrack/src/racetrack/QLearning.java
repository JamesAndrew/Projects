package racetrack;

import java.util.ArrayList;
import java.util.Random;

public class QLearning 
{
    // tunable parameters //
    // threshold to stop training the race track
    private final double epsilon;      
    // greedy parameter for action selection
    private final double greedy = 0.3;  
    // discount factor - low values decrement additive rewards
    private final Double gamma; 
    // learning factor - lower values take longer to converge but give better results
    private final Double alpha; 
    
    // other parameters //
    // racetrack currently being worked with 
    private final Racetrack track;
    // loop iteration trackers
    private int suiteIteration;
    private int raceIteration;
    // delta flag variable used to check the difference between a Q-Value before and after update.
    // Once all q-value update deltas are less than epsilon, this flag stays as true and the suite exits
    private double delta;
    // the first 'minInitialExplorations', the agent explores from random locations on the track
    private final int minInitialExplorations = 6;
    
    /**
     * Constructor
     * @param in_track : race track to assign [state, action] values to
     */
    public QLearning(Racetrack in_track, Double in_gamma, Double in_alpha, double in_epsilon)
    {
        track = in_track;
        epsilon = in_epsilon;
        if (in_gamma == null) gamma = 0.6;
        else gamma = in_gamma;
        if (in_alpha == null) alpha = 0.8;
        else alpha = in_alpha;
    }
    
    /**
     * Begins the Q-Learning procedure as described in figure 21.8 on page 844
     * of Russell and Norvig.
     */
    public void learnTrack()
    {
        System.out.format("= Training Q-Learning on track %s. =%n", track.getName());
        
        Random random = new Random();
        suiteIteration = 0;
        
        // repeat until delta threshold is met
        do
        {
            raceIteration = 0;
            delta = 0;
            
            int[] currentState; // [0]: row position, [1]: col position, [2]: row velocity, [3]: col velocity

            // pick a semi-arbitrary state
            if (suiteIteration == 0) 
            {
                currentState = assignInitialState();
            }
            else if (suiteIteration < minInitialExplorations)
            {
                currentState = randomValidState();
            }
            else
            {
                currentState = startCellState();
            }

            // repeat agent exploration until a 'F' finish cell state is reached
            while (true)
            {
                // choose an action using an epsilon-greedy policy
                int[] action;
                double greedCase = random.nextDouble();

                // pick the greediest action 'greedy' percent of the time
                if (greedCase <= greedy)
                {
                    action = getBestAction(currentState[0], currentState[1], currentState[2], currentState[3]);
                }
                // else pick a random action
                else
                {
                    action = getRandomAction();
                }

                // evaluate max_a'[Q(s',a')] //
                // Get next state s'
                int[] nextState = getNextState(currentState, action);

                // get the maximal action a' from next state s'
                int[] nextStateMaxAction = getBestAction(nextState[0], nextState[1], nextState[2], nextState[3]);

                // get the q value for the next state's best action: Q(s',a')
                Cell nextCell = track.getTrack()[nextState[0]][nextState[1]];
                QValues nextCellQValues = nextCell.getQValues(nextState[2], nextState[3]);
                double maxNextQ = nextCellQValues.getQValue(nextStateMaxAction[0], nextStateMaxAction[1]);

                // run update formula: Q(s,a) <- Q(s,a) + alpha(R(s) + gamma*maxQ(s',a') - Q(s,a))
                Cell currentCell = track.getTrack()[currentState[0]][currentState[1]];
                double currentQ = 
                    currentCell.getQValues(currentState[2], currentState[3]).getQValue(action[0], action[1]);
                double r = currentCell.getReward();
                double qBefore = currentCell.getQValue(currentState[2], currentState[3], action[0], action[1]);
                double qValue = currentQ + alpha*(r + gamma*maxNextQ - currentQ);

                // update the Q value
                currentCell.setQValue(currentState[2], currentState[3], action[0], action[1], qValue);
                double qAfter = currentCell.getQValue(currentState[2], currentState[3], action[0], action[1]);

                // check q-value difference and update delta if |Q(s,a) - Q'(s',a')| > delta
                double difference = Math.abs(qBefore - qAfter);
                if (difference > delta) delta = difference;

                // set state to be next state
                currentState[0] = nextState[0];                 // update agent row location
                currentState[1] = nextState[1];                 // update agent column location
                currentState[2] = nextState[2];                 // update agent row velocity
                currentState[3] = nextState[3];                 // update agent column velocity

                // exit agent exploration if 'F' state is reached otherwise keep exploring
                Cell agentState = track.getTrack()[currentState[0]][currentState[1]];
                if (agentState.getType() == 'F') 
                {
                    break;
                }
                else raceIteration++;
            }
            suiteIteration++;
            
            if (suiteIteration % 100000 == 0) System.out.println("Current training iteration: " + suiteIteration);     //
            
            // force delta condition to not pass the first 'minInitialExplorations' runs
            if (suiteIteration < minInitialExplorations) delta = 9999;
            // handle non-convergence case with a max-tries condition
            if (suiteIteration > 9000000)
            {
                System.out.println("Did not converge.");
                break;
            }
            
        }
        while (delta >= epsilon*(1-gamma)*gamma);
        
        // update statistics
        QLearningStatistics.putTrainingIterations(suiteIteration);
        QLearningStatistics.putDiscountConvergence(gamma, suiteIteration);
        QLearningStatistics.putLearningConvergence(alpha, suiteIteration);
        
        System.out.format("Exploration halting state reached. Running agent as a racer from the Start location%n");
        // run same procedure but always starting at a start 'S' cell
        raceTrackFromStart();
    }
    
    /**
     * Same as 'learnTrack()' but always starts at an 'S' cell and finishes once
     * an 'F' cell is reached
     */
    public void raceTrackFromStart()
    {
        System.out.format("Beginning Race...%n%n");
        
        Random random = new Random();
        int[] currentState; // [0]: row position, [1]: col position, [2]: row velocity, [3]: col velocity

        // start state at 'S' cell
        ArrayList<int[]> startCells = track.getRacetrackStartIndices();
        int randIndex = random.nextInt(startCells.size());
        int[] startIndex = startCells.get(randIndex);
        int rowVel = 0;
        int colVel = 0;
        currentState = new int[]{ startIndex[0], startIndex[1], rowVel, colVel };
        
        // repeat agent exploration until a 'F' finish cell state is reached
        int numSteps = 0;
        while (true)
        {
            // get an action to take for current state
            int[] action;
            action = getBestAction(currentState[0], currentState[1], currentState[2], currentState[3]);
            
            // evaluate max_a'[Q(s',a')] //
            // Get next state s'
            int[] nextState = getNextState(currentState, action);

            // get the maximal action a' from next state s'
            int[] nextStateMaxAction = getBestAction(nextState[0], nextState[1], nextState[2], nextState[3]);

            // get the q value for the next state's best action: Q(s',a')
            Cell nextCell = track.getTrack()[nextState[0]][nextState[1]];
            QValues nextCellQValues = nextCell.getQValues(nextState[2], nextState[3]);
            double maxNextQ = nextCellQValues.getQValue(nextStateMaxAction[0], nextStateMaxAction[1]);

            // run update formula: Q(s,a) <- Q(s,a) + alpha(R(s) + gamma*maxQ(s',a') - Q(s,a))
            Cell currentCell = track.getTrack()[currentState[0]][currentState[1]];
            double currentQ = 
                currentCell.getQValues(currentState[2], currentState[3]).getQValue(action[0], action[1]);
            double r = currentCell.getReward();
            double qValue = currentQ + alpha*(r + gamma*maxNextQ - currentQ);

            // update the Q value
            currentCell.setQValue(currentState[2], currentState[3], action[0], action[1], qValue);

            // set state to be next state
            currentState[0] = nextState[0];                 // update agent row location
            currentState[1] = nextState[1];                 // update agent column location
            currentState[2] = nextState[2];                 // update agent row velocity
            currentState[3] = nextState[3];                 // update agent column velocity

            // exit agent exploration if 'F' state is reached otherwise keep exploring
            Cell agentState = track.getTrack()[currentState[0]][currentState[1]];
            if (agentState.getType() == 'F') 
            {
                break;
            }
            
            numSteps++;
        }
        
        // update statistics
        QLearningStatistics.putRaceResults(numSteps);
        QLearningStatistics.putDiscountRaceResults(gamma, numSteps);
        QLearningStatistics.putLearningRaceResults(alpha, numSteps);
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
        Random random = new Random();
        double oilSlickChance = random.nextDouble();
        
        int currentRow = currentState[0];
        int currentCol = currentState[1];
        int rowVel = currentState[2]; 
        int colVel = currentState[3]; 
        
        int nextRow;
        int nextCol;
        // 20% of the time, no acceleration happens
        int newRowVel; 
        int newColVel;
        if (oilSlickChance <= 0.2)
        {
            newRowVel = rowVel;
            newColVel = colVel;
        }
        else
        {
            newRowVel = rowVel + action[0];
            newColVel = colVel + action[1];
        }
        
        // if any velocities are over +5 or under -5, set to just be +/-5
        if (newRowVel > 5) newRowVel = 5;
        if (newRowVel < -5) newRowVel = -5;
        if (newColVel > 5) newColVel = 5;
        if (newColVel < -5) newColVel = -5;
        
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
            case "simple2":
                state[0] = 2;
                state[1] = 1;
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
     * @return the [row, col, rowVel, colVel] values for once of the racetrack's 'S' cells
     */
    private int[] startCellState()
    {
        Random random = new Random();
        int[] state = new int[4];
        ArrayList<int[]> startIndices = track.getRacetrackStartIndices();
        int[] startIndex = startIndices.get(random.nextInt(startIndices.size()));
        state[0] = startIndex[0];
        state[1] = startIndex[1];
        
        // randomized initial velocities
        state[2] = 0;
        state[3] = 0;
        
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
                rowIndex = randRow - i;
                colIndex = randCol + i;
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
                    + " didn't assign to a cell with a '.' character."
                    + " Row index: "+rowIndex+", Col index: "+colIndex+".");
        else
        {
            int rowVel = random.nextInt(3) - 1;
            int colVel = random.nextInt(3) - 1;
            int[] returnedIndex = new int[]{ rowIndex, colIndex, rowVel, colVel };
            return returnedIndex;
        }
    }
}
