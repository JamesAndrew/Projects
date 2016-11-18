package classification;

import java.util.ArrayList;

/**
 * A vector is an array list of n integer values.
 * The first value is the category, the following values are the features
 */
public class Vector 
{
    private int[] value;
    
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
     * @return 
     */
    public boolean contains(int attribute, int index)
    {
        if (index < value.length)
        {
            return value[index] == attribute; 
        }
        return false;
    }
    
    /**
     * @param feature : the index or value (they're the same) of the feature column
     * @param queryValue
     * @return true if this vector have value 'value' for its feature at index 'feature'
     */
    public boolean hasFeatureValue(int feature, int queryValue)
    {
        return value[feature] == queryValue;
    }
    
    public int[] getValue()
    {
        return value;
    }
    
    /**
     * @return the number at feature entry 'feature'
     */
    public int getFeatureValue(int feature)
    {
        return value[feature];
    }
    
    /**
     * prints the integer values in 'value'
     */
    @Override
    public String toString()
    {
        String output = "[";
        for (int data : value)
        {
            output = output + data + " ";
        }
        output = output + "]";
        return output;
    }
}
