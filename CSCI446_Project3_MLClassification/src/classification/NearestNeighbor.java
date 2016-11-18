package classification;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class NearestNeighbor extends Categorizer
{
    // tunable parameters
    int k;                      // set in constructor
    
    // other class properties
    int[][] foldResult;         // stores the confusion matrix for current run
    
    /**
     * Some constructor logic defined in abstract class
     * @param trainingFolds
     * @param testingFold
     */
    public NearestNeighbor(DataSet[] trainingFolds, DataSet testingFold)
    {
        super(trainingFolds, testingFold);
        categorizerName = "KNN";
        
        // k is chosen to be one less than the total number of data points in 
        // the classification with the least amount of values
        k = trainingSet.getSizeOfSmallestClassificationSet() - 1;
        
        // foldResult is an (n x n) matrix where n = number of classifications
        int matrixSize = trainingSet.getNumClassifications();
        foldResult = new int[matrixSize][];
        for (int i = 0; i < foldResult.length; i++)
        {
            foldResult[i] = new int[matrixSize];
        }
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
            // compute the distance from that point to all points in the trainingSet
            Vector currentPoint = testingFold.getVectors()[i];
            ArrayList<DistanceAndIndex> diValues = new ArrayList<>();
            for (int j = 0; j < trainingSet.getVectors().length; j++)
            {
                Vector otherPoint = trainingSet.getVectors()[j];
                DistanceAndIndex currentDist = calculateDistance(currentPoint, otherPoint, j);
                diValues.add(currentDist);
            }
            
            // calculate the k closest points and assign to a variable
            Collections.sort(diValues);
            ArrayList<Vector> kClosestPoints = new ArrayList<>(k);
            for (int j = 0, diValuesInx = 0; j < k; j++, diValuesInx++)
            {
                int setIndex = diValues.get(diValuesInx).index;
                kClosestPoints.add(trainingSet.getVectors()[setIndex]);
            }
            
            // the classification is the majority class of the points in kClosestPoints
            int classification = calculateMajorityClass(kClosestPoints);
            
            // send the classification result to the foldResult statistic array
            addResult(currentPoint.classification(), classification);
        }
        
        return foldResult;        
    }
    
    /**
     * @param input : a list of the k closest vectors
     * @return : the majority classification 
     */
    private int calculateMajorityClass(ArrayList<Vector> input)
    {
        HashMap<Integer, Integer> classAndSize = new HashMap<>();
        ArrayList<Integer> classifications = new ArrayList<>();
        
        // get list of each unique classification 
        for (Vector point : input)
        {
            if (!(classifications.contains(point.classification())))
            {
                classifications.add(point.classification());
            }
        }
        
        // add as keys to the map
        for (Integer value : classifications)
        {
            classAndSize.put(value, 0);
        }
        
        // tally how many times each classification appeas
        for (Vector point : input)
        {
            Integer pointClass = point.classification();
            for (Integer key : classAndSize.keySet())
            {
                if (Objects.equals(key, pointClass))
                {
                    Integer currentVal = classAndSize.get(key);
                    currentVal++;
                    classAndSize.put(key, currentVal);
                }
            }
        }
        
        // get key associated with largest value
        int largestClass = -1;
        int largestValue = -1;
        for (Map.Entry<Integer, Integer> entry : classAndSize.entrySet())
        {
            if (entry.getValue() > largestValue)
            {
                largestValue = entry.getValue();
                largestClass = entry.getKey();
            }
        }
        
        return largestClass;
    }
    
    /**
     * Adds a classification result to the confusion matrix
     * @param expected : expected class
     * @param actual  : actual class
     */
    private void addResult(int expected, int actual)
    {
        foldResult[expected][actual]++;
    }
    
    /**
     * Calculate categorical (discrete) distance between two vectors. Also
     * track which index this is linked to
     * 
     * The equation used is discussed in 
     * http://ce.sharif.edu/courses/84-85/2/ce324/resources/root/Supplementary%20Materials%20for%20Final%20Exam/Data%20Mining%20(Classification)pdf.pdf
     * on page 11
     * 
     * Note that vectors also have the classification included in the vector 
     * as the first value, so this removes the first array entry and only compares
     * features
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
        
        int[] featuresA = a.features();
        int[] featuresB = b.features();
        double sum = 0.0;
        
        for (int i = 0; i < featuresA.length; i++)
        {
            if (featuresA[i] != featuresB[i]) sum += 1.0;
        }
        double distance = Math.sqrt(sum);
        
        DistanceAndIndex DIRetruned = new DistanceAndIndex(distance, index);
        return DIRetruned;
    }
    
    class DistanceAndIndex implements Comparable<DistanceAndIndex>
    {
        double distance;
        int    index;
        
        public DistanceAndIndex(double dist, int indx)
        {
            distance = dist;
            index = indx;
        }

        @Override
        public int compareTo(DistanceAndIndex t) 
        {
            return Double.compare(distance, t.distance);
        }
    }
}