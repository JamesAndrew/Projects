
package racetrack;

import java.util.ArrayList;
import java.util.Collections;

/**
 * 
 */
public class ValueIteration 
{
    // tunable parameters
    private final double gamma   = 1.0;                 // discount factor
    private final double epsilon = 0.0001;            // halting condition
    
    // track currently being worked with 
    private final Racetrack track;
    
    
    public ValueIteration(Racetrack in_track)
    {
        track = in_track;
    }
    
    /**
     * Runs the value iteration algorithm on page 653 of Russell & Norvig
     */
    public void trainUtilities()
    {
        System.out.format("Running Value Iteration.%nTunable Parameters:%n"
            + "  Discount factor: %.3f%n"
            + "  Epsilon halting condition: %.6f%n"
            + "  Bellman equation: U_{i+1}(s) <- R(s) + gamma*MaxAction[sum_{s'} P(s'|s,a)*U(s')]%n",
            gamma, epsilon);
        
        int tempI = 0;             // temp
        boolean halt;              // checks halting state
        
        // loop until delta is less than than threshold value:
        // delta < episilon(1-gamma)/gamma
        do
        {
            System.out.format("%nCurrent cycle: %d%n", tempI);                                                              //
            halt = true;
            
            System.out.format("  Looping over all possible states:%n");                                                     //
            // loop over all possible states //
            // for each track cell...
            for (int row = 0; row < track.getTrack().length; row++)
            {
                for (int col = 0; col < track.getTrack()[row].length; col++)
                {
                    // if the track is not a wall...
                    if (track.getTrack()[row][col].getType() != '#')
                    {
                        Cell currentCell = track.getTrack()[row][col];
                                                                                                  
                        // for each posssible velocity state at the cell
                        for (int rowVelIndex = 0; rowVelIndex < currentCell.getUtilities().length; rowVelIndex++)
                        {
                            for (int colVelIndex = 0 ; colVelIndex < currentCell.getUtilities()[rowVelIndex].length; colVelIndex++)
                            {
                                // the utility of this cell at this velocity is calculated using the bellman equation
                                // store in temp until all utilities are updated for the generation loop
                                int rowVel = rowVelIndex - 5;
                                int colVel = colVelIndex - 5;
                                
                                if ((rowVel == -5 || rowVel == 5) && (colVel == -5 || colVel == 5))                         //
                                {                                                                                           //
                                    System.out.format("    Calculating utility of cell (%d,%d) with velocity [%d,%d] "      //
                                            + "using Bellman Equation%n",
                                        row, col, rowVel, colVel);                                                          //
                                }                                                                                           //
                                
                                double utilityValue = bellmanEquation(currentCell, rowVel, colVel);
                                
                                if ((rowVel == -5 || rowVel == 5) && (colVel == -5 || colVel == 5))                         //
                                {                                                                                           //
                                    System.out.format("      Utility value: %.4f%n", utilityValue);                         //
                                }
                                
                                currentCell.setTempUtility(rowVelIndex, colVelIndex, utilityValue);
                            }
                        }
                        System.out.println();                                                                               //
                    }
                }
            }
            
            // update all states using the temp stored values and check for halt state //
            // for each track cell...
            for (int row = 0; row < track.getTrack().length; row++)
            {
                for (int col = 0; col < track.getTrack()[row].length; col++)
                {
                    // if the track is not a wall...
                    if (track.getTrack()[row][col].getType() != '#')
                    {
                        Cell currentCell = track.getTrack()[row][col];
                        
                        // for each posssible velocity state at the cell
                        for (int rowVel = 0; rowVel < currentCell.getUtilities().length; rowVel++)
                        {
                            for (int colVel = 0 ; colVel < currentCell.getUtilities()[rowVel].length; colVel++)
                            {
                                double newUtility = currentCell.getTempUtilities()[rowVel][colVel];
                                
                                // don't halt if a large enough utility update occured
                                if (Math.abs(currentCell.getUtilities()[rowVel][colVel] - newUtility) > epsilon) halt = false;
                                currentCell.setUtility(rowVel, colVel, newUtility);
                            }
                        }
                    }
                }
            }
            tempI++;        // temp used for testing
            System.out.format("  No update changes greater than delta?: %b%n", halt);
        } 
        while (!halt);
    }
    
    /**
     * Calculates a cell's next utility value for a given velocity 
     *     U_{i+1}(s) <- R(s) + gamma*MaxAction[sum_{s'} P(s'|s,a)*U(s')]
     * This equation is from the Russell and Norvig book on page 653
     * 
     * @param cell : the cell to calculate next utility for
     * @param rowVel : the cell's row-velocity state, a number between [-5,5]
     * @param colVel : the cell's col-velocity state, a number between [-5,5]
     * @return U_{i+1}(s) : the updated utility for the cell at state [xVel,yVel]
     */
    private double bellmanEquation(Cell cell, int rowVel, int colVel)
    {
        double R = cell.getReward();
        // holds the values of the summation over s' for each action that can be taken from this state
        ArrayList<Double> actionSums = new ArrayList();
        
        // 'F' states always have 1.0 utility
        if (cell.getType() == 'F') 
        { 
            if ((rowVel == -5 || rowVel == 5) && (colVel == -5 || colVel == 5))                                                 //
                System.out.format("      Max action: Stay in square. (at finish 'F' state)%n");                                 //
            return 1.0; 
        }
        else
        {
            // for each action that can be taken from current state, add the sum value for that action //
            // (accelerate +1/-1/0 in x and y directions) //
            // a_(-1,-1) case : accelerate up and left
            if (rowVel == -5 || colVel == -5) { /* do nothing, this action cannot occur */ }
            else
            {
                double upLeftAction = bellmanEquationSubroutine(cell, rowVel, colVel, -1, -1);
                actionSums.add(upLeftAction);
                if ((rowVel == -5 || rowVel == 5) && (colVel == -5 || colVel == 5))
                    System.out.format("      Accelerate up-left action value: %.3f%n", upLeftAction);                               //
            }

            // a_(-1,0) case : accelerate up only
            if (rowVel == -5) { /* do nothing, this action cannot occur */ }
            else
            {
                double upAction = bellmanEquationSubroutine(cell, rowVel, colVel, -1, 0);
                actionSums.add(upAction);
                if ((rowVel == -5 || rowVel == 5) && (colVel == -5 || colVel == 5))
                    System.out.format("      Accelerate up action value: %.3f%n", upAction);                                        //
            }
            
            // a_(-1,1) case : accelerate up and right
            if (rowVel == -5 || colVel == 5) { /* do nothing, this action cannot occur */ }
            else
            {
                double upRightAction = bellmanEquationSubroutine(cell, rowVel, colVel, -1, 1);
                actionSums.add(upRightAction);
                if ((rowVel == -5 || rowVel == 5) && (colVel == -5 || colVel == 5))
                    System.out.format("      Accelerate up-right action value: %.3f%n", upRightAction);                             //
            }

            // a_(0,-1) case : accelerate left only
            if (colVel == -5) { /* do nothing, this action cannot occur */ }
            else
            {
                double leftAction = bellmanEquationSubroutine(cell, rowVel, colVel, 0, -1);
                actionSums.add(leftAction);
                if ((rowVel == -5 || rowVel == 5) && (colVel == -5 || colVel == 5))
                    System.out.format("      Accelerate left action value: %.3f%n", leftAction);                                //
            }

            // a_(0,0) case : no acceleration
            double noAction = bellmanEquationSubroutine(cell, rowVel, colVel, 0, 0);
            actionSums.add(noAction);
            if ((rowVel == -5 || rowVel == 5) && (colVel == -5 || colVel == 5))
                System.out.format("      No accelerate action value: %.3f%n", noAction);                                        //

            // a_(0,1) case : accelerate right only
            if (colVel == 5) { /* do nothing, this action cannot occur */ }
            else
            {
                double rightAction = bellmanEquationSubroutine(cell, rowVel, colVel, 0, 1);
                actionSums.add(rightAction);
                if ((rowVel == -5 || rowVel == 5) && (colVel == -5 || colVel == 5))
                    System.out.format("      Accelerate right action value: %.3f%n", rightAction);                              //
            }

            // a_(1,-1) case : accelerate down and left
            if (rowVel == 5 || colVel == -5) { /* do nothing, this action cannot occur */ }
            else
            {
                double downLeftAction = bellmanEquationSubroutine(cell, rowVel, colVel, 1, -1);
                actionSums.add(downLeftAction);
                if ((rowVel == -5 || rowVel == 5) && (colVel == -5 || colVel == 5)) 
                    System.out.format("      Accelerate down-left action value: %.3f%n", downLeftAction);                       //
            }
            
            // a_(1,0) case : accelerate down only
            if (rowVel == 5) { /* do nothing, this action cannot occur */ }
            else
            {
                double downAction = bellmanEquationSubroutine(cell, rowVel, colVel, 1, 0);
                actionSums.add(downAction);
                if ((rowVel == -5 || rowVel == 5) && (colVel == -5 || colVel == 5))
                    System.out.format("      Accelerate down action value: %.3f%n", downAction);                                //
            }

            // a_(1,1) case : accelerate down and right
            if (rowVel == 5 || colVel == 5) { /* do nothing, this action cannot occur */ }
            else
            {
                double downRightAction = bellmanEquationSubroutine(cell, rowVel, colVel, 1, 1);
                actionSums.add(downRightAction);
                if ((rowVel == -5 || rowVel == 5) && (colVel == -5 || colVel == 5))
                    System.out.format("      Accelerate down-right action value: %.3f%n", downRightAction);                     //
            }

            // get the maximal action value
            double maxAction = Collections.max(actionSums);
            if ((rowVel == -5 || rowVel == 5) && (colVel == -5 || colVel == 5))
                System.out.format("      Max action value: %.3f%n", maxAction);                                                 //
            
            // finish the bellman equation
            double result = R + (gamma * maxAction);
            return result;
        }
    }
    
    /**
     * Calculate the action sum, sum_{s'} P(s'|s,a)*U(s'), for a given acceleration action
     * 
     * @param rowAccel : the row-direction acceleration. Either -1, 0, or 1
     * @param colAccel : the col-direction acceleration. Either -1, 0, or 1
     * @return sum : the sum over each possible next state
     */
    private double bellmanEquationSubroutine(Cell cell, int rowVel, int colVel, int rowAccel, int colAccel)
    {
        Cell nextCell;
        int newRowVel = rowVel + rowAccel;
        int newColVel = colVel + colAccel;
        // sum P(s'|s,a)*U(s') over each possible s' state
        double sum = 0.0;
        // acceleration worked as intended, next cell-state is [row+newXVel, col+newYVel] with 80% chance //
        // if the finish line can be crossed, set nextCell to be the finish
        if (finishOccurs(cell.getRow(), cell.getCol(), newRowVel, newColVel))
        {
            int[] finishLoc = finishLocation(cell.getRow(), cell.getCol(), newRowVel, newColVel);
            nextCell = track.getTrack()[finishLoc[0]][finishLoc[1]];
            newRowVel = 0;
            newColVel = 0;
        }
        // if collision is known to happen, set nextCell to be cell right before wall was hit with 0 velocity
        else if (collisionOccurs(cell.getRow(), cell.getCol(), newRowVel, newColVel))
        {
            int[] crashLoc = collisionLocation(cell.getRow(), cell.getCol(), newRowVel, newColVel);
            nextCell = track.getTrack()[crashLoc[0]][crashLoc[1]];
            newRowVel = 0;
            newColVel = 0;
        }
        // if the next cell is normal to move to (no walls, no finish), update next cell normally
        else
        {
            nextCell = track.getTrack()[cell.getRow() + newRowVel][cell.getCol() + newColVel];
        }
        // P(s'|s,a)U(s') //
        sum += 0.8*nextCell.getUtilities()[newRowVel+5][newColVel+5];
            
        // acceleration didn't occur, row and col velocities don't change.
        // next cell-state is [row+xVel, col+yVel] with 20% chance
        newRowVel = rowVel;
        newColVel = colVel;
        if (finishOccurs(cell.getRow(), cell.getCol(), newRowVel, newColVel))
        {
            int[] finishLoc = finishLocation(cell.getRow(), cell.getCol(), newRowVel, newColVel);
            nextCell = track.getTrack()[finishLoc[0]][finishLoc[1]];
            newRowVel = 0;
            newColVel = 0;
        }
        // if collision is known to happen, set nextCell to be cell right before wall was hit with 0 velocity
        else if (collisionOccurs(cell.getRow(), cell.getCol(), newRowVel, newColVel))
        {
            int[] crashLoc = collisionLocation(cell.getRow(), cell.getCol(), newRowVel, newColVel);
            nextCell = track.getTrack()[crashLoc[0]][crashLoc[1]];
            newRowVel = 0;
            newColVel = 0;
        }
        // if the next cell is normal to move to (no walls, no finish), update next cell normally
        else
        {
            nextCell = track.getTrack()[cell.getRow() + newRowVel][cell.getCol() + newColVel];
        }
        // P(s'|s,a)U(s')
        sum += 0.2*nextCell.getUtilities()[newRowVel+5][newColVel+5];
        
        return sum;
    }
    
    /**
     * Calculates if a collision will occur when a race car at position 
     * [row,col] moves with velocity [rowVel,colVel].
     * Note: this assumes the car always travels the full row distance first, then full column distance
     * 
     * @param row : current row location of car
     * @param col : current column locatin of car
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
}
