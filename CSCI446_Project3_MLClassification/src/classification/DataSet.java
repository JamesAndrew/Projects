package classification;

import java.util.ArrayList;
import java.util.Arrays;
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
        
        // name the data set based on known first value
        if (arrayListEquals(dataSet.get(0), iris_data_set))
            this.name = "iris_data_set";
//                        "cancer_data_set"
//                        "glass_data_set"
//                        "house_data_set"
//                        "soybean_data_set"
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
     * Just prints the vector values in vectors
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
    
    public Vector[] getVectors() 
    {
        return vectors;
    }
    
    public String getName() 
    {
        return name;
    }
}
