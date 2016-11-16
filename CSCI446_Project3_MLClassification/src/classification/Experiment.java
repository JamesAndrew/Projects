package classification;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

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
     * @param algorithmList : the machine learning algorithms to run 
     * @throws java.lang.InstantiationException 
     * @throws java.lang.IllegalAccessException 
     * @throws java.lang.NoSuchMethodException 
     */
    public void runExperiment(List<Class<?>> algorithmList) throws InstantiationException, 
            IllegalAccessException, NoSuchMethodException, IllegalArgumentException, InvocationTargetException
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
            
            // for each machine learning categorizer...
            for (Class<?> categorizer : algorithmList)
            {
                // for each 10-fold cross validation run...
                for (int cvRun = 0; cvRun < partitions.length; cvRun++)
                {
                    // create the 9 training folds
                    DataSet[] trainingFolds = generateTrainingFolds(partitions, cvRun);
                    // create the 1 testing fold
                    DataSet testingFold = partitions[cvRun];
                    
                    // (re)instantiate the categorizer
                    Class[] args = new Class[2];
                    args[0] = trainingFolds.getClass();
                    args[1] = testingFold.getClass();
                    Categorizer currentCategorizer = (Categorizer)categorizer.getDeclaredConstructor(args).newInstance(trainingFolds, testingFold);
                    
                    // train
                    currentCategorizer.Train();
                    // then test and save results
                    currentCategorizer.Test();
                }
            }
        }
        
//        throw new UnsupportedOperationException("Not supported yet."); 
    }
    
    /**
     * @param partitions
     * @param cvRun : the one partition index to not add
     * @return Return the 9 partitions that are not the current cross validation run testing fold
     */
    private DataSet[] generateTrainingFolds(DataSet[] partitions, int cvRun)
    {
        DataSet[] trainers = new DataSet[partitions.length - 1];
        
        int trainersIndx = 0;
        // for each partition...
        for (int i = 0; i < partitions.length; i++)
        {
            // if this is the fold to not add...
            if (i == cvRun)
            {
                // do nothing
            }
            else
            {
                trainers[trainersIndx] = partitions[i];
                trainersIndx++;
            }
        }
        
        return trainers;
    }
}
