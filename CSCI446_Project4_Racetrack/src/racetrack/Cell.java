
package racetrack;

/**
 * The racetrack is made of a 2d array of Cell objects.
 * The cell's primary purpose is to knows its following properties
 *   - The [row,col] location (0 indexed with [0,0] at bottom left)
 *   - The cell's utility value
 *   - The cell's reward value
 *   - The cell's type: Either a start (S), finish(F), open track (.), or wall (#)
 *   - (todo: various statistics tracking such as number of times visited)
 */
public class Cell 
{
    // 0 indexed with [0,0] at bottom left
    private final int row;
    // 0 indexed with [0,0] at bottom left
    private final int col;
    // start (S), finish(F), open track (.), or wall (#)
    private final char type;
    // utility is initially 0 for all cells. Other initializations will change depending on the learning alg. used
    private double utility = 0;
    // synonymous with cost or penalty. All non-finish cells have -1 reward
    private double reward = -1;     
    
    /**
     * Constructor.
     * Called by Experiment.convertToRacetrack()
     */
    public Cell(int in_row, int in_col, char in_type)
    {
        row = in_row;
        col = in_col;
        type = in_type;
    }

    public int getRow() 
    {
        return row;
    }

    public int getCol() 
    {
        return col;
    }

    public char getType() 
    {
        return type;
    }

    public double getUtility() 
    {
        return utility;
    }

    public void setUtility(double value)
    {
        utility = value;
    }
    
    public double getReward() 
    {
        return reward;
    }
    
    public void setReward(double value)
    {
        reward = value;
    }
}
