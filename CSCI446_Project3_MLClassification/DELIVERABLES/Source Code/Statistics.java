
package classification;

import java.util.HashMap;
import java.util.Map;

/**
 * Static class that the Experiment class uses to update the run result values 
 * for each data set on each machine learning categorizer for each 10-fold CV run.
 * 
 * The goal here is to easily extract the data needed for the confusion matrix
 * and macro-average for each data set -> categorizer -> CV run result.
 * 
 * The confusion matrix needs:
 *  - True Positive value for each classification
 *  - True Negative value for each classification
 *  - False Positive value for each classification
 *  - False Negative value for each classification
 * 
 * The macro average needs:
 *  - Precision and Recall metrics which can be evaluated from the above values
 */
public class Statistics 
{
    /**
     * A map data structure is used to categorize the confusion matrices
     * according to the correct Categorizer algorithm
     * 
     * Key 1              : The categorizer algorithm name (needs to be defined in each concrete categorizer class)
     * Key 1 of Value 1   : The data set name (based off of names shown in the DataSet class constructor)
     * Value 2 of Value 1 : The confusion matrix for the provided algorithm and data set
     */
    private static HashMap<String, HashMap<String, double[][]>> results = new HashMap<>();
    
    /**
     * Called by the Experiment class to provide averaged 10-fold CV 
     * data to the correct matrix
     * @param input       : The averaged 10-fold CV data for the 
     * @param algName     : The categorizer algorithm name (needs to be defined in each concrete categorizer class)
     * @param dataSetName : The data set name (based off of names shown in the DataSet class constructor)
     */
    public static void updateMatrix(String algName, String dataSetName, double[][] matrix)
    {
        if (results.containsKey(algName))
        {
            results.get(algName).put(dataSetName, matrix);
        }
        else
        {
            HashMap<String, double[][]> keyValue = new HashMap<>();
            keyValue.put(dataSetName, matrix);
            results.put(algName, keyValue);
        }
    }

    public static HashMap<String, HashMap<String, double[][]>> getResults() 
    {
        return results;
    }
    
    /**
     * Calculate the True Positive Rate (TPR) of an int[][] matrix
     * @param matrix
     * @return The averaged values of the TRP for each classification
     */
    public static double calculateMatrixTPR(int[][] matrix)
    {
        double averageTPR = 0.0; 
        // for each row
        for (int i = 0; i < matrix.length; i++)
        {
            int condPositive = 0;
            
            // for each value in the row
            for (int j = 0 ; j < matrix[i].length; j++)
            {
                condPositive += matrix[i][j];
            }
            
            double currentTPR = (double)matrix[i][i] / (double)condPositive;
            averageTPR += currentTPR;
        }
        
        averageTPR = averageTPR / (double)matrix.length;
        
        return averageTPR;
    }
    
    /**
     * print all confusion matrices in an easy to read way
     */
    public static void printConfusionMatrix()
    {
        // for each algorithm...
        for (Map.Entry<String, HashMap<String, double[][]>> entry1 : results.entrySet())
        {
            System.out.format("=== %s ===%n", entry1.getKey().toUpperCase());
            
            // for each data set...
            for (Map.Entry<String, double[][]> entry2 : entry1.getValue().entrySet())
            {
                System.out.format("%s: %n", entry2.getKey());
                double[][] matrix = entry2.getValue();
                for (int i = 0; i < matrix.length; i++)
                {
                    for (int j = 0; j < matrix[i].length; j++)
                    {
                        System.out.format("%.3f ", matrix[i][j]);
                    }
                    System.out.println();
                }
            }
            System.out.println();
        }
    }
}
//
//     * Key 1              : The categorizer algorithm name (needs to be defined in each concrete categorizer class)
//     * Key 1 of Value 1   : The data set name (based off of names shown in the DataSet class constructor)
//     * Value 2 of Value 1 : The confusion matrix for the provided algorithm and data set
//     */
//    private static HashMap<String, HashMap<String, double[][]>> results = new HashMap<>();