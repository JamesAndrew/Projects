
package racetrack;

import java.util.ArrayList;
import java.util.Collections;

/**
 * 
 */
public class ValueIteration 
{
    // tunable parameters
    private final double gamma   = 0.5;                 // discount factor
    private final double epsilon = 0.000001;            // halting condition
    
    // track currently being worked with 
    private Racetrack track;
    
    
    public ValueIteration(Racetrack in_track)
    {
        track = in_track;
    }
    
    /**
     * Runs the value iteration algorithm on page 653 of Russell & Norvig
     */
    public void trainUtilities()
    {
        int tempI = 0;             // temp
        
        // loop until delta is less than than threshold value:
        // delta < episilon(1-gamma)/gamma
        do
        {
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
                        for (int rowVel = 0; rowVel < currentCell.getUtilities().length; rowVel++)
                        {
                            for (int colVel = 0 ; colVel < currentCell.getUtilities()[rowVel].length; colVel++)
                            {
                                // the utility of this cell at this velocity is calculated using the bellman equation
                                currentCell.getUtilities()[rowVel][colVel] = bellmanEquation(currentCell, rowVel, colVel);
                            }
                        }
                    }
                }
            }
            tempI++;        // temp used for testing
        } 
        while (tempI < 1);
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
        
        // for each action that can be taken from current state, add the sum value for that action //
        // (accelerate +1/-1/0 in x and y directions) //
        // a_(-1,-1) case : accelerate up and left
        if (rowVel == -5 || colVel == -5) { /* do nothing, this action cannot occur */ }
        else
        {
            double upLeftAction = bellmanEquationSubroutine(cell, rowVel, colVel, -1, -1);
            actionSums.add(upLeftAction);
        }
        
        // a_(-1,0) case : accelerate up only
        if (rowVel == -5) { /* do nothing, this action cannot occur */ }
        else
        {
            double upAction = bellmanEquationSubroutine(cell, rowVel, colVel, -1, 0);
            actionSums.add(upAction);
        }
        
        // a_(0,-1) case : accelerate left only
        if (colVel == -5) { /* do nothing, this action cannot occur */ }
        else
        {
            double leftAction = bellmanEquationSubroutine(cell, rowVel, colVel, 0, -1);
            actionSums.add(leftAction);
        }
        
        // a_(0,0) case : no acceleration
        double upAction = bellmanEquationSubroutine(cell, rowVel, colVel, 0, 0);
        actionSums.add(upAction);
        
        // a_(0,1) case : accelerate right only
        if (colVel == 5) { /* do nothing, this action cannot occur */ }
        else
        {
            double rightAction = bellmanEquationSubroutine(cell, rowVel, colVel, 0, 1);
            actionSums.add(rightAction);
        }
        
        // a_(1,0) case : accelerate down only
        if (rowVel == 5) { /* do nothing, this action cannot occur */ }
        else
        {
            double downAction = bellmanEquationSubroutine(cell, rowVel, colVel, 1, 0);
            actionSums.add(downAction);
        }
        
        // a_(1,1) case : accelerate down and right
        if (rowVel == 5 || colVel == 5) { /* do nothing, this action cannot occur */ }
        else
        {
            double downRightAction = bellmanEquationSubroutine(cell, rowVel, colVel, 1, 1);
            actionSums.add(downRightAction);
        }
        
        // get the maximal action value
        double maxAction = Collections.max(actionSums);
        
        // finish the bellman equation
        double result = R + (gamma * maxAction);
        return result;
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
        int newRowVel = rowVel + rowAccel;
        int newColVel = colVel + colAccel;
        // sum P(s'|s,a)*U(s') over each possible s' state
        double sum = 0.0;
        // acceleration worked as intended, next cell-state is [row+xVel', col+yVel'] with 80% chance
        try
        {
            // s'
            Cell nextCell = track.getTrack()[cell.getRow()+(newRowVel)][cell.getCol()+(newColVel)];
            // P(s'|s,a)U(s') //
            // if nextCell is a wall, next state is current cell with [0,0] velocity
            if (nextCell.getType() == '#')
            {
                nextCell = cell;
                newRowVel = 0;
                newColVel = 0;
            }
            sum += 0.8*nextCell.getUtilities()[newRowVel][newColVel];
        }
        // catch if nextCell is out of the track bounds, car will be placed back on the cell it came from
        catch (ArrayIndexOutOfBoundsException e)
        {
            Cell nextCell = cell;
            newRowVel = 0;
            newColVel = 0;
            sum += 0.8*nextCell.getUtilities()[newRowVel][newColVel];
        }

        // acceleration didn't occur, next cell-state is [row+xVel, col+yVel] with 20% chance
        try
        {
            // s'
            Cell nextCell = track.getTrack()[cell.getRow()+(rowVel)][cell.getCol()+(colVel)];
            if (nextCell.getType() == '#')
            {
                nextCell = cell;
                newRowVel = 0;
                newColVel = 0;
            }
            sum += 0.2*nextCell.getUtilities()[newRowVel][newColVel];
        }
        // catch if nextCell is out of the track bounds, car will be placed back on the cell it came from
        catch (ArrayIndexOutOfBoundsException e)
        {
            Cell nextCell = cell;
            newRowVel = 0;
            newColVel = 0;
            sum += 0.2*nextCell.getUtilities()[newRowVel][newColVel];
        }
        
        return sum;
    }
}
