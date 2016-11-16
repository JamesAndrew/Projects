package classification;

public class NearestNeighbor extends Categorizer
{
    // tunable parameters
    int k;          // set in constructor
    
    
    /**
     * Constructor logic defined in abstract class
     * @param trainingFolds
     * @param testingFold
     */
    public NearestNeighbor(DataSet[] trainingFolds, DataSet testingFold)
    {
        super(trainingFolds, testingFold);
        categorizerName = "KNN";
        k = trainingSet.getVectors().length / 15;
        System.out.println("k: " + k);
    }
    
    @Override
    public void Train() 
    {
        // K-NN is a lazy algorithm and has no training element apart from
        // using the 9 training folds as the comparison points
    }

    /**
     * Iterate through each data point in testingFold, assign the point a 
     * classification based on the majority label of the k closest points.
     * Finally, evaluate and return the results as a confusion matrix
     * 
     * @return the classification results as a confusion matrix
     */
    @Override
    public int[][] Test() 
    {
        // for each point in the testing fold...
        for (int i = 0; i < testingFold.getVectors().length; i++)
        {
            
        }
        throw new UnsupportedOperationException();
    }
    
}