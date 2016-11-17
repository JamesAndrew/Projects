
package classification;

import java.util.ArrayList;

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
    ID3Node rootNode;           // The root node of the ID3 Decision tree that is built after Train() runs

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
     * This method sets up the initial state needed and then called
     * the recursive ID3 procedure.
     * 
     * 
     */
    @Override
    public void Train() 
    {
        ArrayList<Integer> features = trainingSet.getFeaturesList();
        rootNode = id3_Recursive(trainingSet, features);
    }

    /**
     * 
     * @return the classification results as a confusion matrix
     */
    @Override
    public int[][] Test() 
    {
        throw new UnsupportedOperationException("Not supported yet."); 
    }
    
    /**
     * ID3 recursive algorithm
     * 
     * @param S : The current data (sub)set
     * @param remainingFeatures : Set of allowed features to interact with
     * @return : the current node as a root
     */
    private ID3Node id3_Recursive(DataSet S, ArrayList<Integer> remainingFeatures)
    {
        // instantiate root node for this recursive scope
        ID3Node root = new ID3Node();
        
        // Base Cases //
        // if all the values in S have the same classification, assign root that classification label and return
        if (S.getNumClassifications() == 1)
        {
            int leafValue = S.getVectors()[0].classification();
            root.setNodeValue(leafValue);
            return root;
        }
        // if there are no remaining features to branch off of, assign root the majority category 
        // amongst data points still in S 
        if (remainingFeatures.isEmpty())
        {
            int leafValue = S.getMajorityClassification();
            root.setNodeValue(leafValue);
            return root;
        }
        
        // begin recursive tree building loop
        
        
        throw new UnsupportedOperationException("Not supported yet."); 
    }
    
}
