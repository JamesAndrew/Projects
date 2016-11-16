package classification;

public class NearestNeighbor extends Categorizer
{
    public static String categorizerName = "KNN";
    
    /**
     * Constructor logic defined in abstract class
     * @param trainingFolds
     * @param testingFold
     */
    public NearestNeighbor(DataSet[] trainingFolds, DataSet testingFold)
    {
        super(trainingFolds, testingFold);
    }
    
    @Override
    public void Train() 
    {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public int[][] Test() 
    {
        throw new UnsupportedOperationException("Not supported yet."); 
    }
    
}