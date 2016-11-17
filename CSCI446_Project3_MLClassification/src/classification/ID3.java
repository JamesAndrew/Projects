
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
        
        // else begin recursive tree building loop... //
        // calculate the feature that best classifies the data
        int bestFeature = calculateMaxGainFeature(S);
        
        
        throw new UnsupportedOperationException("Not supported yet."); 
    }
    
    /**
     * Calculates the information gain for each feature in S and returns
     * the feature value with the highest gain
     * 
     * @param S : the current data set 
     * @return : the feature value with the highest gain
     */
    private int calculateMaxGainFeature(DataSet S)
    {
        
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    /**
     * Gain(S, A) = Entropy(S) - SUM((|Sv| / |S|) * Entropy(Sv))
     * Sums over each subset of S where S was split by each feature value of feature A
     * 
     * @param S : the current data set
     * @param feature : the feature (value and index in the data set will match)
     *                  used as a parameter in the gain equation 
     * @return : The information gain of example S on feature A
     */
    private double informationGain(DataSet S, int feature)
    {
        double sum = 0.0;
        double totalEntropy = entropy(S);
        int[] featureValues = S.getValuesOfAFeature(feature);
        ArrayList<DataSet> subsetsByFeatureValue = new ArrayList<>();
        
        // put each classification subset into the array list
        for (int entry : featureValues)
        {
            DataSet subset = S.getSubsetByFeatureValue(feature, entry);
            subsetsByFeatureValue.add(subset);
        }
        
        // run the summation formula
        for (DataSet subset : subsetsByFeatureValue)
        {
            double proportion = subset.getVectors().length / S.getVectors().length;
            sum += -1 * proportion * entropy(subset);
        }
        
        double gain = totalEntropy * sum;
        return gain;
    }
    
    /**
     * Entropy(S) = SUM(-p(I) log2 p(I))
     * Sums over each classification in S
     * 
     * @param S : the current data set
     * @return : the entropy of S
     */
    private double entropy(DataSet S)
    {
        double entropy = 0.0;
        int[] classifications = S.getClassifcationValues();
        ArrayList<DataSet> subsetsByClassification = new ArrayList<>();
        
        // put each classification subset into the array list
        for (int classification : classifications)
        {
            DataSet subset = S.getSubsetByClassification(classification);
            subsetsByClassification.add(subset);
        }
        
        // run the summation formula
        for (DataSet subset : subsetsByClassification)
        {
            double proportion = subset.getVectors().length / S.getVectors().length;
            entropy += -1 * proportion * (Math.log10(proportion) / Math.log10(2));
        }
        
        return entropy;
    }
}
