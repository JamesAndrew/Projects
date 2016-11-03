package classification;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The data class represents the List<List<Double>> structure in a more object
 * oriented way and has many helpful functions
 */
public class DataSet 
{
    private String name;
    private List<Vector> vectors = new ArrayList<>();
    /**
     * Constructor takes in a data set from the Experiment class and gives it
     * a name based on the empirically known first line of data
     * @param dataSet
     */
    public DataSet(ArrayList<ArrayList<Double>> dataSet) 
    { 
        ArrayList<Double> mock_data_set = new ArrayList<>(Arrays.asList(0.0000,0.2222,0.6250,0.0678,0.0417));
        // name the data set based on known first value
        if (arrayListEquals(dataSet.get(0), mock_data_set))
            this.name = "mock_data_set";
        System.out.println("name: " + this.name);
    }
    
    /**
     * custom .equals method for ArrayList<Double>
     * @param a1
     * @param a2
     * @return true if a1 == a2
     */
    private boolean arrayListEquals(ArrayList<Double> a1, ArrayList<Double> a2)
    {
        DecimalFormat df = new DecimalFormat("#.####");
        df.setRoundingMode(RoundingMode.UNNECESSARY);
        
        if (a1.size() != a2.size()) return false;
        for (int i = 0; i < a1.size(); i++)
        {
            double a1Value = Double.parseDouble(df.format(a1.get(i)));
            double a2Value = Double.parseDouble(df.format(a2.get(i)));
            if (a1Value != a2Value) return false;
        }
        return true;
    }
}
