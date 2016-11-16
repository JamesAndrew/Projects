package classification;

import java.util.ArrayList;
import java.util.List;

/**
 * Experiment runs the 10-fold cross validation procedure
 */
public class Experiment 
{
    // Collection of the 5 data sets in 'DataSet' object form
    List<DataSet> dataSets = new ArrayList<>();
    
    /**
     * Constructor expects a pre-processed List<List<List<Integer>>> 
     * where the first vector value is the category and the rest are 
     * the discretized feature values.
     * The Experiment constructor is in charge of making the appropriate
     * 'DataSet' class representations of the provided list
     * @param dataSets
     */
    public Experiment(ArrayList<ArrayList<ArrayList<Integer>>> dataSets) 
    { 
        // fill the dataSets collection
        for (ArrayList<ArrayList<Integer>> dataSet : dataSets)
        {
            this.dataSets.add(new DataSet(dataSet));
        }
    }
    
    /**
     * Runs 10-fold cross validation with training, testing, and result generation
     * as outlined in the design document
     */
    public void runExperiment()
    {
        throw new UnsupportedOperationException("Not supported yet."); 
    }
}
