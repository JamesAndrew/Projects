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
            
            // for each machine learning categorizer...
            for (Class<?> categorizer : algorithmList)
            {
                // Holds the 10 fold-run confusion matrix results (used to take an average after the 10 runs complete)
                ArrayList<int[][]> foldRunResults = new ArrayList<>();
                // Crappy way of saving the categorizer name for the 'updateMatrix' call
                String categorizerName = "not set yet";
                
                // for each 10-fold cross validation run...
//                for (int cvRun = 0; cvRun < 1; cvRun++) // temp to just run once
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
                    categorizerName = currentCategorizer.getCategorizerName();
                    
                    // train
                    currentCategorizer.Train();
                    
                    // test and save results for the current fold run
                    int[][] foldResult = currentCategorizer.Test();
                    foldRunResults.add(foldResult);
                }
                
                // average all values in the 10-fold CV run and sent to the Statistics class
                double[][] averagedResults = averageFoldResults(foldRunResults);
                
                // and add to the statistics class
                Statistics.updateMatrix(categorizerName, currentDataSet.getName(), averagedResults);
            }
        }
        // program finishes once each data set has run (across all 5 categorizers)
        Statistics.printConfusionMatrix();
    }
    
    /**
     * @param input : a list of 10 int[][] resulting from the 10-fold CV runs
     * @return : the average of each entry as a single double[][] instance
     */
    private double[][] averageFoldResults(ArrayList<int[][]> input)
    {
        double[][] returnedArray = new double[input.get(0).length][];
        for (int i = 0; i < returnedArray.length; i++) 
            returnedArray[i] = new double[returnedArray.length]; 
            
        // for each row...
        for (int i = 0; i < returnedArray.length; i++)
        {
            // for each column entry...
            for (int j = 0; j < returnedArray.length; j++)
            {
                // calculate the average of all arrays at this index
                double avgValue = 0.0;
                for (int[][] fold : input)
                {
                    avgValue += fold[i][j];
                }
                avgValue = avgValue / (double)input.size();
                
                // and add to the array to return
                returnedArray[i][j] = avgValue;
            }
        }
        
        return returnedArray;
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
