package classification;

/**
 * All machine learning algorithms must extend the Categorizer class.
 * Upon instantiation of a Categorizer, the training and testing folds are provided.
 * Each machine learning algorithms must define and Train() and Test() method.
 * Also, a categorizer needs to define its name for sake of tracking statistics
 */
public abstract class Categorizer 
{
    private DataSet[] trainingFolds;
    private DataSet testingFold;
    
    /**
     * Constructor used by all implementing classes
     * @param trainingFolds
     * @param testingFold 
     */
    public Categorizer(DataSet[] trainingFolds, DataSet testingFold)
    {
        this.trainingFolds = trainingFolds;
        this.testingFold = testingFold;
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
}
