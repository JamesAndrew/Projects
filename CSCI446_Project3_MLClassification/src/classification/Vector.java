package classification;

import java.util.ArrayList;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import sun.security.util.PendingException;

/**
 * A vector is an array list of n integer values.
 * The first value is the category, the following values are the features
 */
public class Vector 
{
    int[] value;
    
    /**
     * Constructor takes a single discretized data entry from the
     * ArrayList<ArrayList<Integer>> dataSet passed by the Driver and turns
     * it into a vector representation of that data
     * 
     * @param input : a single data point passed by the DataSet class
     */
    public Vector(ArrayList<Integer> input)
    {
        this.value = new int[input.size()];
        for (int i = 0; i < value.length; i++) 
            value[i] = input.get(i);
    }
    
    /**
     * The classification of a data point is the first entry in the vector
     * 
     * @return : The integer representation of the data point's classification
     */
    public int classification()
    {
        return value[0];
    }
    
    /**
     * @return : An integer array of only the features of this vector
     */
    public int[] features()
    {
        int[] features = new int[value.length - 1];
        for (int i = 1; i < value.length; i++)
        {
            features[i-1] = value[i];
        }
        
        return features;
    }
    
    /**
     * (DR) method needed for stuff in the TAN class. Not totally confident in
     * its intended use so added a pending exception to help things compile
     * @param attribute
     * @param index
     */
    public boolean contains(double attribute, int index)
    {
        throw new PendingException();
    }
}
