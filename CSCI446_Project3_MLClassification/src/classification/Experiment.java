package classification;

import java.util.ArrayList;

/**
 * Experiment runs the 10-fold cross validation procedure
 */
public class Experiment 
{
    // Collection of the 5 data sets in 'DataSet' object form
//    List<DataSet> dataSets = new ArrayList<>();
    DataSet[] dataSets;
    
    /**
     * Constructor expects a pre-processed List<List<List<Integer>>> 
     * where the first vector value is the category and the rest are 
     * the discretized feature values.
     * The Experiment constructor is in charge of making the appropriate
     * 'DataSet' class representations of the provided list
     * @param in_dataSets : pre-processed data from Driver
     */
    public Experiment(ArrayList<ArrayList<ArrayList<Integer>>> in_dataSets) 
    { 
        // fill the dataSets collection
        dataSets = new DataSet[in_dataSets.size()];
        for (int i = 0; i < in_dataSets.size(); i++)
        {
            this.dataSets[i] = new DataSet(in_dataSets.get(i));
        }
    }
    
    /**
     * Runs 10-fold cross validation with training, testing, and result generation
     * as outlined in the design document
     */
    public void runExperiment()
    {
        // for each data set...
        for (int dataSetsIndex = 0; dataSetsIndex < dataSets.length; dataSetsIndex++)
        {
            DataSet currentDataSet = dataSets[dataSetsIndex];
            
            // create 10 partitions                   
            DataSet[] partitions = currentDataSet.fillPartitions();
            
            for (int i = 0; i < partitions.length; i++)
            {
                System.out.format("Partition %d: %n", i);
                DataSet current = partitions[i];
                System.out.println(current.toString());
            }
        }
        
//        throw new UnsupportedOperationException("Not supported yet."); 
    }
}
