package classification;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * The data class represents the List<List<Integer>> structure in a more object oriented way.
 */
public class DataSet 
{
    private String name;
    private final Vector[] vectors;
    
    /**
     * Constructor takes in a data set from the Experiment class and gives it
     * a name based on the empirically known first line of data
     * @param dataSet
     */
    public DataSet(ArrayList<ArrayList<Integer>> dataSet) 
    { 
        // associate a data set with its pre-known first line of data
        ArrayList<Integer> iris_data_set = new ArrayList<>(Arrays.asList(0, 1, 1, 1, 1));
        ArrayList<Integer> cancer_data_set = new ArrayList<>(Arrays.asList(0, 5, 1, 1, 1, 2, 1, 3, 1, 1));
        ArrayList<Integer> house_data_set = new ArrayList<>(Arrays.asList(0, 0, 1, 0, 1, 1, 1, 0, 0, 0, 1, 0, 1, 1, 1, 0, 1));        
        ArrayList<Integer> glass_data_set = new ArrayList<>(Arrays.asList(4, 5, 1, 9, 1, 1, 19, 1, 1, 1, 1));
        ArrayList<Integer> soybean_data_set = new ArrayList<>(Arrays.asList(0, 4, 0, 2, 1, 1, 1, 0, 1, 0, 2, 1, 1, 0, 2, 2, 0, 0, 0, 1, 0, 3, 1, 1, 1, 0, 0, 0, 0, 4, 0, 0, 0, 0, 0, 0));
        
        // name the data set based on known first value
        if (arrayListEquals(dataSet.get(0), iris_data_set))
            this.name = "iris_data_set";
        else if(arrayListEquals(dataSet.get(0), cancer_data_set))
            this.name = "cancer_data_set";
        else if(arrayListEquals(dataSet.get(0), house_data_set))
            this.name = "house_data_set";
        else if(arrayListEquals(dataSet.get(0), glass_data_set))
            this.name = "glass_data_set";
        else if(arrayListEquals(dataSet.get(0), soybean_data_set))
            this.name = "soybean_data_set";
        else 
            throw new RuntimeException("The first line of the input data set did not match a known data set."
                + "Check your data set with the associations in the DataSet class constructor.");
          
        // fill the collection of vectors
        vectors = new Vector[dataSet.size()];
        for (int i = 0; i < dataSet.size(); i++)
        {
            ArrayList<Integer> entry = dataSet.get(i);
            Vector vector = new Vector(entry);
            vectors[i] = vector;
        }
        
        System.out.println(this.name + " added to DataSet.vectors.");
    }
    /**
     * Create a new DataSet with a known vectors size
     * @param size : the size to make the vectors property
     */
    public DataSet(int size)
    {
        vectors = new Vector[size];
    }
    
    /**
     * Used during 10-fold cross validation
     * Stratifies and generates 10 subsets
     * 
     * Note (todo) : not stratifying for now. Will deal with that later
     * 
     * @return a data set array of size 10 with all vectors represented in a stratified form
     */
    public DataSet[] fillPartitions() 
    {
        DataSet[] partitions = new DataSet[10];
        ArrayList<Integer> usedIndexes = new ArrayList<>();
        int partitionSize = vectors.length / 10;
        int jumpSize = vectors.length / partitionSize;
        int i = 0;
        
        // for each partition
        for (int partitionsIndex = 0; partitionsIndex < partitions.length; partitionsIndex++)
        {
            DataSet currentPartition = new DataSet(partitionSize);

            // repeat until the current partition is filled
            for (int vectIndx = 0; vectIndx < currentPartition.getVectors().length; vectIndx++)
            {
                // get an unused index
                while(alreadyChosen(usedIndexes, i))
                {
                    i = (i+1) % vectors.length;
                }
                // get the vector at that index and add to the currentPartition
                Vector vector = this.vectors[i];
                currentPartition.addVector(vector, vectIndx);

                // add i to list of used indexes
                usedIndexes.add(i);
                // increase i by jump size
                i = (i+jumpSize) % vectors.length;
            }

            // add the now-filled partition to partitions
            partitions[partitionsIndex] = currentPartition;
        }
        
        return partitions;
    }
    
    /**
     * @return true is index exists in usedIndexes
     */
    private boolean alreadyChosen(ArrayList<Integer> usedIndexes, int index)
    {
        if (usedIndexes.isEmpty()) return false;
        for (int entry : usedIndexes)
        {
            if (index == entry) return true;
        }
        return false;
    }
    
    /**
     * Add a Vector to the vectors array at a specified index
     * @param value
     * @param index 
     */
    public void addVector(Vector value, int index)
    {
        vectors[index] = value;
    }
    
    /**
     * @return an arraylist of the arbitrary Integer feature categories this data set contains
     */
    public ArrayList<Integer> getFeaturesList()
    {
        ArrayList<Integer> features = new ArrayList<>();
        int[] someVector = vectors[0].getValue();
        
        for (int i = 1; i < someVector.length; i++)
        {
            features.add(i);
        }
        return features;
    }
    
    /**
     * @param classification : the actual value of the classification
     * @return : a subset of this data set with only values with 'classification' in it
     */
    public DataSet getSubsetByClassification(int classification)
    {
        ArrayList<Vector> tempVectors = new ArrayList<>();
        for (Vector point : vectors)
        {
            if (point.classification() == classification) tempVectors.add(point);
        }
        
        DataSet subset = new DataSet(tempVectors.size());
        for (int i = 0; i < tempVectors.size(); i++)
        {
            subset.addVector(tempVectors.get(i), i);
        }
        
        return subset;
    }
    
    /**
     * @param feature : the index or value (they're the same) of the feature column
     * @param featureValue : the value that feature needs to have in order to add to the subset
     * @return : a subset of this data set with only data points that have
     *           value 'featureValue' for feature 'feature'
     */
    public DataSet getSubsetByFeatureValue(int feature, int featureValue)
    {
        ArrayList<Vector> tempVectors = new ArrayList<>();
        for (Vector point : vectors)
        {
            if (point.hasFeatureValue(feature, featureValue)) tempVectors.add(point);
        }
        
        DataSet subset = new DataSet(tempVectors.size());
        for (int i = 0; i < tempVectors.size(); i++)
        {
            subset.addVector(tempVectors.get(i), i);
        }
        
        return subset;
    }
    
    /**
     * @return : an integer array with the value of each unique classification
     */
    public int[] getClassifcationValues()
    {
        int[] classification;
        ArrayList<Integer> classificationsArrayList = new ArrayList<>();
        // get list of each unique classification 
        for (Vector point : vectors)
        {
            if (!(classificationsArrayList.contains(point.classification())))
            {
                classificationsArrayList.add(point.classification());
            }
        }
        
        // translate into a int[] object
        classification = new int[classificationsArrayList.size()];
        for (int i = 0; i < classificationsArrayList.size(); i++)
        {
            classification[i] = classificationsArrayList.get(i);
        }
        
        return classification;
    }
    
    /**
     * @return the number of unique classifications this data set has
     */
    public int getNumClassifications()
    {
        ArrayList<Integer> classifications = new ArrayList<>();
        for (Vector vector : vectors)
        {
            if (!(classifications.contains(vector.classification())))
                classifications.add(vector.classification());
        }
        return classifications.size();
    }
    
    /**
     * @return the classification value that appears most in the data set
     */
    public int getMajorityClassification()
    {
        HashMap<Integer, Integer> classAndSize = new HashMap<>();
        ArrayList<Integer> classifications = new ArrayList<>();
        
        // get list of each unique classification 
        for (Vector point : vectors)
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
        for (Vector point : vectors)
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
     * Used in K-NN algorithm. 
     * k is chosen to be one less than the total number of data points in 
     * the classification with the least amount of values
     * 
     * @return number of data points in the smallest classification set 
     */
    public int getSizeOfSmallestClassificationSet()
    {
        HashMap<Integer, Integer> classAndSize = new HashMap<>();
        ArrayList<Integer> classifications = new ArrayList<>();
        
        // get list of each unique classification 
        for (Vector point : vectors)
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
        
        // Tally how many times each classification appeas
        for (Vector point : vectors)
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
        
        // Get smallest value
        int smallestValue = Integer.MAX_VALUE;
        for (Map.Entry<Integer, Integer> entry : classAndSize.entrySet())
        {
            if (entry.getValue() < smallestValue)
            {
                smallestValue = entry.getValue();
            }
        }
        
        return smallestValue;
    }
    
    /**
     * custom .equals method for ArrayList<Double>
     * @param a1
     * @param a2
     * @return true if a1 == a2
     */
    private boolean arrayListEquals(ArrayList<Integer> a1, ArrayList<Integer> a2)
    {
        if (a1.size() != a2.size()) return false;
        for (int i = 0; i < a1.size(); i++)
        {
            if ((!Objects.equals(a1.get(i), a2.get(i)))) return false;
        }
        return true;
    }
    
    /**
     * Just prints the vector values in vectors
     * @return 
     */
    @Override
    public String toString()
    {
        String output = "";
        for (Vector point : vectors)
        {
            output = output + point.toString() + "\n";
        }
        return output;
    }
    
    public Vector[] getVectors() 
    {
        return vectors;
    }
    
    public String getName() 
    {
        return name;
    }
}
