package classification;

/**
 * All machine learning algorithms must extend the Categorizer class.
 * Upon instantiation of a Categorizer, the training and testing folds are provided.
 Each machine learning algorithms must define and Train() and Test() method.
 Also, a categorizer needs to define its categorizerName for sake of tracking statistics
 */
public abstract class Categorizer 
{
    protected DataSet[] trainingFolds;
    protected DataSet testingFold;
    protected DataSet trainingSet;    // all vectors in trianingFolds unioned into one DataSet
    protected String categorizerName;  // set the categorizerName of each concrete categorizer in the constructor
    
    /**
     * Constructor used by all implementing classes
     * @param trainingFolds
     * @param testingFold 
     */
    public Categorizer(DataSet[] trainingFolds, DataSet testingFold)
    {
        this.trainingFolds = trainingFolds;
        this.testingFold   = testingFold;
        mergeTrainingFolds();
    }
    
    /**
     * Train the algorithm first
     */
    public abstract void Train();
    
    /**
     * Then test.
     * The test class must return a (n x n) int array representing the
     * confusion matrix results for the current run.
     * See https://en.wikipedia.org/wiki/Confusion_matrix
     */
    public abstract int[][] Test();
    
    /**
     * unions all vectors in trianingFolds into one DataSet
     */
    public void mergeTrainingFolds()
    {
        // union all the training folds into training set
        int traningSetSize = 0;
        for (DataSet fold : trainingFolds)
        {
            traningSetSize += fold.getVectors().length;
        }
        trainingSet = new DataSet(traningSetSize);
        
        int i = 0;
        for (DataSet fold : trainingFolds)
        {
            for (Vector vector : fold.getVectors())
            {
                trainingSet.addVector(vector, i);
                i++;
            }
        }
        if (i != trainingSet.getVectors().length)
            throw new RuntimeException("'i' should equal " + trainingSet.getVectors().length 
                + " but instead equals " + i);
    }
    
    public String getCategorizerName()
    {
        return categorizerName;
    }
}
