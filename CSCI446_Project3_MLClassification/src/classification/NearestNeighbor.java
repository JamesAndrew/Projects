package classification;

import java.util.ArrayList;
import java.util.HashMap;

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
            // compute the distance from that point to all opints in the trainingSet
            Vector currentPoint = testingFold.getVectors()[i];
            ArrayList<DistanceAndIndex> DIValues = new ArrayList<>();
            for (int j = 0; j < trainingSet.getVectors().length; j++)
            {
                Vector otherPoint = trainingSet.getVectors()[j];
                DistanceAndIndex currentDist = calculateDistance(currentPoint, otherPoint, j);
            }
            
        }
        throw new UnsupportedOperationException();
    }
    
    /**
     * Calculate categorical (discrete) distance between two vectors. Also
     * track which index this is linked to
     * 
     * The equation used is discussed in 
     * http://ce.sharif.edu/courses/84-85/2/ce324/resources/root/Supplementary%20Materials%20for%20Final%20Exam/Data%20Mining%20(Classification)pdf.pdf
     * on page 11
     * 
     * @param a : sample vector
     * @param b : training vector
     * @param index : the index of the training vector in the training set array
     * @return : A data object that contains the distance and index
     */
    private DistanceAndIndex calculateDistance(Vector a, Vector b, int index)
    {
        if (a.getValue().length != b.getValue().length)
            throw new RuntimeException("vectors a and b are of different lengths");
        
        double sum = 0.0;
        for (int i = 0; i < a.getValue().length; i++)
        {
            if (a.getValue()[i] == b.getValue()[i]) sum += 1.0;
        }
        double distance = Math.sqrt(sum);
        
        DistanceAndIndex DIRetruned = new DistanceAndIndex(distance, index);
        return DIRetruned;
    }
    
    class DistanceAndIndex
    {
        double distance;
        int    index;
        
        public DistanceAndIndex(double dist, int indx)
        {
            distance = dist;
            index = indx;
        }
    }
}