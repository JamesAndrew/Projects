package racetrack;

/**
 * Each cell has 11x11 possible states (each velocity vector) and each 
 * state has 3x3 possible actions (each acceleration value).
 * This class holds the Q-Value for each 3x3 acceleration (action) value.
 * 
 * The Cell class holds an array of QValues associated to each state in order
 * to query a Q-Value for a specific acceleration action
 */
public class QValues 
{
    // The Q-Value for each possible acceleration pairing
    private final double[][] qValues = new double[3][3];
    
    public QValues()
    {
        
    }
    
    public void fillQValues(double value)
    {
        for (int i = 0; i < qValues.length; i++)
        {
            for (int j = 0; j < qValues.length; j++)
            {
                qValues[i][j] = value;
            }
        }
    }
    
    public double getQValue(int rowAccel, int colAccel)
    {
        int rowIndex = rowAccel + 1;
        int colIndex = colAccel + 1;
        return qValues[rowIndex][colIndex];
    }
    
    public void setQValue(int rowAccel, int colAccel, double value)
    {
        int rowIndex = rowAccel + 1;
        int colIndex = colAccel + 1;
        qValues[rowIndex][colIndex] = value;
    }
}
