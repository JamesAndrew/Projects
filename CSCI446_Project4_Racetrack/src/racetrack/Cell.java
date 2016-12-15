
package racetrack;

/**
 * The racetrack is made of a 2d array of Cell objects.
 * The cell's primary purpose is to knows its following properties
   - The [row,col] location (0 indexed with [0,0] at bottom left)
   - The cell's utilities value
   - The cell's reward value
   - The cell's type: Either a start (S), finish(F), open track (.), or wall (#)
   - (todo: various statistics tracking such as number of times visited)
 */
public class Cell 
{
    // 0 indexed with [0,0] at top right
    private final int row;
    // 0 indexed with [0,0] at top right
    private final int col;
    // start (S), finish(F), open track (.), or wall (#)
    private final char type;
    // utilities is initially 0 for all cells and velocity states except the finish state cell with has a max-utility of 1
    // utilities is a mapping for each possible state of the cell mapped to the associated utilities.
    // the state of the cell is the [x,y] velocity value which match to the [row,col] of the array
    private double[][] utilities = new double[11][11];
    // because all utilities are updated simutaneous during value iteration, store the new utility
    // in a temporary array until all utilities have been updated for the current generation
    private final double[][] tempUtilities = new double[11][11];
    // Q-Value for each state this cell can have.
    private final QValues[][] qValues = new QValues[11][11];
    // Synonymous with cost or penalty. All non-finish cells have -1 reward
    private final double reward;   
    
    /**
     * Constructor.
     * Called by Experiment.convertToRacetrack()
     */
    public Cell(int in_row, int in_col, char in_type)
    {
        row = in_row;
        col = in_col;
        type = in_type;
        
        // utilities and qvalues is initially 0 for all cells and velocity states 
        // except the finish state cell with a value of 1
        if (in_type == '.' || in_type == 'S') 
        {
            fillUtilityValues(0.0);
            fillQValueValues(0.0);
            reward = -1;
        }
        else if (in_type == '#')
        {
            fillUtilityValues(-100.0);
            fillQValueValues(0.0);
            reward = -1;
        }
        else if (in_type == 'F') 
        {
            fillUtilityValues(1.0);
            fillQValueValues(1.0);
            reward = 0;
        }
        else throw new RuntimeException("Initializtion of cell utility didn't have a valid character");
    }

    /**
     * Fills each entry in the 'utilities' array with value 'value'
     * @param value : the utilities to fill each 'utilities' entry with
     */
    private void fillUtilityValues(double value)
    {
        for (int i = 0; i < utilities.length; i++)
        {
            for (int j = 0; j < utilities[i].length; j++)
            {
                utilities[i][j] = value;
            }
        }
    }
    
    /**
     * Fills each entry in the 'qValues' array with value 'value'
     * @param value : the value to fill each 'qValues' entry with
     */
    private void fillQValueValues(double value)
    {
        for (int i = 0; i < qValues.length; i++)
        {
            for (int j = 0; j < qValues[i].length; j++)
            {
                qValues[i][j] = new QValues();
                qValues[i][j].fillQValues(value);
            }
        }
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
    
    /**
     * Note that the utility for velocity vector [-4,2] will be accessed
     * at index [1,7].  Add velocity + 5 to get the correct index.
     * @return The associated utility for each velocity combination between [-5,5]
     */
    public double[][] getUtilities() 
    {
        return utilities;
    }
    
    public void setUtilities(double[][] utilities) 
    {
        this.utilities = utilities;
    }
    
    /**
     * Set a specific array index value in utilities
     * @param row
     * @param col
     * @param value 
     */
    public void setUtility(int row, int col, double value)
    {
        utilities[row][col] = value;
    }
    
    public double[][] getTempUtilities() 
    {
        return tempUtilities;
    }
    
    /**
     * Set a specific array index value in tempUtilities
     * @param row
     * @param col
     * @param value 
     */
    public void setTempUtility(int row, int col, double value)
    {
        tempUtilities[row][col] = value;
    }
    
    public double getReward() 
    {
        return reward;
    }

    /**
     * Calculates the correct index for the provided velocities and returns
     * the qValue stored at that index.
     * @param rowVel : value between [-5,5]
     * @param colVel : value between [-5,5]
     * @return the qValues stored at the given index.
     */
    public QValues getQValues(int rowVel, int colVel)
    {
        // If 'F' state, all QValues are 1
        if (type == 'F')
        {
            for (int i = 0; i < qValues.length; i++)
            {
                for (int j = 0; j < qValues[i].length; j++)
                {
                    qValues[i][j].fillQValues(1.0);
                }
            }
        }
        int rowVelIndex = rowVel + 5;
        int colVelIndex = colVel + 5;
        
        return qValues[rowVelIndex][colVelIndex];
    }
    
    public double getQValue(int rowVel, int colVel, int rowAccel, int colAccel)
    {
        // If 'F' state, all QValues are 1
        if (type == 'F')
        {
            for (int i = 0; i < qValues.length; i++)
            {
                for (int j = 0; j < qValues[i].length; j++)
                {
                    qValues[i][j].fillQValues(1.0);
                }
            }
        }
        
        int rowVelIndex = rowVel + 5;
        int colVelIndex = colVel + 5;
        
        return qValues[rowVelIndex][colVelIndex].getQValue(rowAccel, colAccel);
    }
    
    /**
     * Set/Update the QValue for a specific velocity state and acceleration action of this Cell
     * @param rowVel : value between [-5,5]
     * @param colVel : value between [-5,5]
     * @param rowAccel : value between [-1,1]
     * @param colAccel : value between [-1,1]
     * @param value : the Q-Value
     */
    public void setQValue(int rowVel, int colVel, int rowAccel, int colAccel, double value)
    {
        int rowVelIndex = rowVel + 5;
        int colVelIndex = colVel + 5;
        
        qValues[rowVelIndex][colVelIndex].setQValue(rowAccel, colAccel, value);
    }
}
