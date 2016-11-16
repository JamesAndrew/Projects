package classification;

import java.util.ArrayList;

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
    public int getClassification()
    {
        return value[0];
    }
    
    /**
     * @return : An int[] of only the features of this vector
     */
    public int[] getFeatures()
    {
        int[] features = new int[value.length - 1];
        for (int i = 1; i < value.length; i++)
        {
            features[i-1] = value[i];
        }
        
        return features;
    }
}
