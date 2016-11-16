package classification;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * The data class represents the List<List<Integer>> structure in a more object
 * oriented way and has many helpful functions
 */
public class DataSet 
{
    private String name;
    public List<Vector> vectors = new ArrayList<>();
    /**
     * Constructor takes in a data set from the Experiment class and gives it
     * a name based on the empirically known first line of data
     * @param dataSet
     */
    public DataSet(ArrayList<ArrayList<Integer>> dataSet) 
    { 
        ArrayList<Integer> iris_data_set = new ArrayList<>(Arrays.asList(0, 1, 1, 1, 1));
        // name the data set based on known first value
        if (arrayListEquals(dataSet.get(0), iris_data_set))
            this.name = "iris_data_set";
        else throw new RuntimeException("The first line of the input data set did not match a known data set");
          
        System.out.println("name: " + this.name);
    }
    
    /**
     * custom .equals method for ArrayList<Double>
     * @param a1
     * @param a2
     * @return true if a1 == a2
     */
    private boolean arrayListEquals(ArrayList<Integer> a1, ArrayList<Integer> a2)
    {
        if (a1.size() != a2.size()) return false;
        for (int i = 0; i < a1.size(); i++)
        {
            if ((!Objects.equals(a1.get(i), a2.get(i)))) return false;
        }
        return true;
    }
}
