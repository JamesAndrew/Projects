
package classification;

/**
 * Solver using Decision Tree Classification, specifically the Iterative Dichotomiser 3
 * Leaf Nodes:      Classification to give the query data point
 * Non-leaf Nodes:  Decision node (attribute test at each branch)
 * 
 * @author David
 */
public class ID3 extends Categorizer
{
    // other class properties
    int[][] foldResult;         // stores the confusion matrix for current run

    /**
     * Some constructor logic defined in abstract class
     * @param trainingFolds
     * @param testingFold
     */
    public ID3(DataSet[] trainingFolds, DataSet testingFold)
    {
        super(trainingFolds, testingFold);
        categorizerName = "ID3";
        
        // foldResult is an (n x n) matrix where n = number of classifications
        int matrixSize = trainingSet.getNumClassifications();
        foldResult = new int[matrixSize][];
        for (int i = 0; i < foldResult.length; i++)
        {
            foldResult[i] = new int[matrixSize];
        }
    }
    
    /**
     * Training builds the tree from fixed, known examples.
     */
    @Override
    public void Train() 
    {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    /**
     * 
     * @return the classification results as a confusion matrix
     */
    @Override
    public int[][] Test() {
        throw new UnsupportedOperationException("Not supported yet."); 
    }
    
}
