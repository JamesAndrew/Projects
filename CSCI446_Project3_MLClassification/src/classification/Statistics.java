
package classification;

import java.util.HashMap;

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
 *  - Total population size
 *  - Population size of each classification members
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
        HashMap<String, double[][]> keyValue = new HashMap<>();
        keyValue.put(dataSetName, matrix);
        results.put(algName, keyValue);
    }

    public static HashMap<String, HashMap<String, double[][]>> getResults() 
    {
        return results;
    }
}


//    /**
//     * There is a confusion matrix (n x n int array) for each Categorizer.
//     * Rows represent the actual classification. Columns represent the predicted
//     * values. See https://en.wikipedia.org/wiki/Confusion_matrix
//     */
//    private static double[][] cancer_matrix;
//    private static double[][] glass_matrix;
//    private static double[][] house_votes_matrix;
//    private static double[][] iris_matrix;
//    private static double[][] soybean_matrix;

//    /**
//     * Called by the Experiment class. Instantiates the above 5 class variables
//     * with the correct dimensions 
//     * @param name : Name of the dataset as defined in the DataSet class
//     * @param dimensions : number of classifications
//     */
//    public static void instantiateMatrix(String name, int dimensions)
//    {
//        switch (name)
//        {
//            case "cancer_data_set":
//                cancer_matrix = new double[dimensions][];
//                for (int i = 0; i < cancer_matrix.length; i++)
//                {
//                    cancer_matrix[i] = new double[dimensions];
//                }
//                break;
//            case "glass_data_set":
//                glass_matrix = new double[dimensions][];
//                for (int i = 0; i < glass_matrix.length; i++)
//                {
//                    glass_matrix[i] = new double[dimensions];
//                }
//                break;
//            case "house_data_set":
//                house_votes_matrix = new double[dimensions][];
//                for (int i = 0; i < house_votes_matrix.length; i++)
//                {
//                    house_votes_matrix[i] = new double[dimensions];
//                }
//                break;
//            case "iris_data_set":
//                iris_matrix = new double[dimensions][];
//                for (int i = 0; i < iris_matrix.length; i++)
//                {
//                    iris_matrix[i] = new double[dimensions];
//                }
//                break;
//            case "soybean_data_set":
//                soybean_matrix = new double[dimensions][];
//                for (int i = 0; i < soybean_matrix.length; i++)
//                {
//                    soybean_matrix[i] = new double[dimensions];
//                }
//                break;
//            default:
//                throw new RuntimeException("The name provided in instantiateMatrix "
//                        + "doesn't match a known data set name.");
//        }
//    }