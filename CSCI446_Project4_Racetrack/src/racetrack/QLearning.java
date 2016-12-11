
package racetrack;

import java.util.Collections;
import java.util.Random;

public class QLearning 
{
    // tunable parameters //
    // threshold to stop training the race track
    private final double epsilon = 0.00001;
    // greedy parameter for action selection
    private final double greedy = 0.6;
    // discount factor - low values decrement additive rewards
    private final double gamma = 0.95;
    // learning factor - lower values take longer to converge but give better results
    private final double alpha = 0.5;
    
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
        Random random = new Random();
        int iteration = 0;
        // indexes - [0]: row location, [1]: col location, [2]: row velocity, [3]: col velocity
        int[] currentState = new int[4];
        
        // repeat until delta threshold is met
        do
        {
            System.out.format("Current generation: %d%n", iteration);                                               //            
            
            // pick a semi-arbitrary state
            if (iteration == 0) currentState = assignInitialState();
            else
            {
                currentState = randomValidState();
            }
            System.out.format("  State of [cell-index][velocity] randomly chosen: [%d,%d][%d,%d]%n",                //
                    currentState[0], currentState[1], currentState[2], currentState[3]);                            //
            
            // choose an action using an epsilon-greedy policy
            double greedCase = random.nextDouble();
            // pick the greediest action 'greedy'% of the time
            if (greedCase <= greedy)
            {
                int[] action = getBestAction(currentState[0], currentState[1], currentState[2], currentState[3]);
            }
            else
            {
                
            }
            
            iteration++;
        }
        while (iteration < 3);
    }
    
    /**
     * Queries the Cell at index [trackRow,trackCol] for state [rowVelocity,colVelocity]
     * and calculates the best action to take. Best action is action resulting in highest Q value
     * @param trackRow
     * @param trackCol
     * @param rowVel
     * @param colVel
     * @return the acceleration vectors of the best action to take
     */
    private int[] getBestAction(int trackRow, int trackCol, int rowVel, int colVel)
    {
        Cell cell = track.getTrack()[trackRow][trackCol];
        int bestAction = -1;
        double bestActionQValue = Double.MIN_VALUE;
        
        // iterate through all possible actions one can take from the current state
        // (accelerate +1/-1/0 in x and y directions) and store the action that 
        // results in the largest Q(s',a') value
        
        // a_(-1,-1) case 1: accelerate up and left
        if (rowVel == -5 || colVel == -5) { /* do nothing, this action cannot occur */ }
        else
        {
            double qValue = cell.getQValue(-1, -1);
            if (qValue > bestActionQValue)
            {
                bestAction = 1;
                bestActionQValue = qValue;
            }
        }

        // a_(-1,0) case 2: accelerate up only
        if (rowVel == -5) { /* do nothing, this action cannot occur */ }
        else
        {
            double qValue = cell.getQValue(-1, 0);
            if (qValue > bestActionQValue)
            {
                bestAction = 2;
                bestActionQValue = qValue;
            }
        }

        // a_(-1,1) case 3: accelerate up and right
        if (colVel == -5) { /* do nothing, this action cannot occur */ }
        else
        {
            double qValue = cell.getQValue(-1, 1);
            if (qValue > bestActionQValue)
            {
                bestAction = 3;
                bestActionQValue = qValue;
            }
        }
        
        // a_(0,-1) case 4 accelerate left only
        if (colVel == -5) { /* do nothing, this action cannot occur */ }
        else
        {
            double qValue = cell.getQValue(0, -1);
            if (qValue > bestActionQValue)
            {
                bestAction = 4;
                bestActionQValue = qValue;
            }
        }

        // a_(0,0) case 5: no acceleration
        double tempQValue = cell.getQValue(0, 0);
        if (tempQValue > bestActionQValue)
        {
            bestAction = 5;
            bestActionQValue = tempQValue;
        }

        // a_(0,1) case 6: accelerate right only
        if (colVel == 5) { /* do nothing, this action cannot occur */ }
        else
        {
            double qValue = cell.getQValue(0, 1);
            if (qValue > bestActionQValue)
            {
                bestAction = 6;
                bestActionQValue = qValue;
            }
        }

        // a_(1,-1) case 7: down up and left
        if (rowVel == 5) { /* do nothing, this action cannot occur */ }
        else
        {
            double qValue = cell.getQValue(1, -1);
            if (qValue > bestActionQValue)
            {
                bestAction = 7;
                bestActionQValue = qValue;
            }
        }
        
        // a_(1,0) case 8: accelerate down only
        if (rowVel == 5) { /* do nothing, this action cannot occur */ }
        else
        {
            double qValue = cell.getQValue(1, 0);
            if (qValue > bestActionQValue)
            {
                bestAction = 8;
                bestActionQValue = qValue;
            }
        }

        // a_(1,1) case 9: accelerate down and right
        if (rowVel == 5 || colVel == 5) { /* do nothing, this action cannot occur */ }
        else
        {
            double qValue = cell.getQValue(1, 1);
            if (qValue > bestActionQValue)
            {
                bestAction = 9;
                bestActionQValue = qValue;
            }
        }
        
        if (bestActionQValue == Double.MIN_VALUE) 
        {
            throw new RuntimeException("bestActionQValue never got assigned a real value.");
        }
        
        // return the accel vectors associated with best qvalue
        switch (bestAction)
        {
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                break;
            case 5:
                break;
            case 6:
                break;
            case 7:
                break;
            case 8:
                break;
            case 9:
                break;
            default:
                throw new RuntimeException("a best action case was never assigned");
        }
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
        int randRow = random.nextInt(track.getTrack().length);
        int randCol = random.nextInt(track.getTrack()[randRow].length);
        
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
            throw new RuntimeException("randomValidState()row and column indices"
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
