
package classification;

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
     * There is a confusion matrix (n x n int array) for each Categorizer.
     * Rows represent the actual classification. Columns represent the predicted
     * values. See https://en.wikipedia.org/wiki/Confusion_matrix
     */
    public static double[][] cancer_matrix;
    public static double[][] glass_matrix;
    public static double[][] house_votes_matrix;
    public static double[][] iris_matrix;
    public static double[][] soybean_matrix;
    
    /**
     * Called by the Experiment class. Instantiates the above 5 class variables
     * with the correct dimensions 
     * @param name : Name of the dataset as defined in the DataSet class
     * @param dimensions : number of classifications
     */
    public static void instantiateMatrix(String name, int dimensions)
    {
        switch (name)
        {
            case "cancer_data_set":
                cancer_matrix = new double[dimensions][];
                for (int i = 0; i < cancer_matrix.length; i++)
                {
                    cancer_matrix[i] = new double[dimensions];
                }
                break;
            case "glass_data_set":
                glass_matrix = new double[dimensions][];
                for (int i = 0; i < glass_matrix.length; i++)
                {
                    glass_matrix[i] = new double[dimensions];
                }
                break;
            case "house_data_set":
                house_votes_matrix = new double[dimensions][];
                for (int i = 0; i < house_votes_matrix.length; i++)
                {
                    house_votes_matrix[i] = new double[dimensions];
                }
                break;
            case "iris_data_set":
                iris_matrix = new double[dimensions][];
                for (int i = 0; i < iris_matrix.length; i++)
                {
                    iris_matrix[i] = new double[dimensions];
                }
                break;
            case "soybean_data_set":
                soybean_matrix = new double[dimensions][];
                for (int i = 0; i < soybean_matrix.length; i++)
                {
                    soybean_matrix[i] = new double[dimensions];
                }
                break;
            default:
                throw new RuntimeException("The name provided in instantiateMatrix "
                        + "doesn't match a known data set name.");
        }
    }
}
