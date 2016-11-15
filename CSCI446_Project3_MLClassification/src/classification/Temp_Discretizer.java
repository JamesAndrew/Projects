
package classification;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Temp class that discretizes features of continuously valued data
 * @author David
 */
public class Temp_Discretizer 
{
    // class-scoped variable that is filled during the recurisve calls of Discretize
    // each value is where a 'cut' will occur in the sorted data set array
    private static ArrayList<Double> cutPoints;
    
    /**
     * Start with a .txt file of a one-feature data set with continuous values,
     * end with an ArrayList<ArrayList<Integer>> of discretized values
     * @param args
     * @throws java.io.FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException 
    {
        // transform the .txt file into an ArrayList<ArrayList<Double>>
        ArrayList<ArrayList<Double>> currentDataSet = generateDataSet("DataSets/tiny-mock-data-for-discretizing.txt");
        
        // sort the data set (low-to-high) based on Feature_1 values
        currentDataSet = sortFeature(currentDataSet);
        printADataSet(currentDataSet);
        
        // method that fills *n* partition locations in 'cutPoints' by calling the recursive 'Discretize' function
        Discretize(currentDataSet);
    }
    
    /**
     * Methods used during recursion calls: 
     *  - entropy           : utility calculation procedure
     *  - classEntropy      : utility calculation procedure
     *  - gain              : utility calculation procedure
     *  - findMinEntropyCut : finds the index in the current subset that results in the least entropy 
     *  - mDLP              : "Minimal Description Length Principle". The halting condition.
     * 
     * Note that this currently only discretizes on feature column. A simple loop 
     * can extend this to sort all feature columns one at a time.
     * 
     * Might be good for me to have a more clarifying name, but note that this method
     * does not actually complete the discretization. It just generates the cut points.
     * The actual discretization process happens once we have the cut points.
     * 
     * I'm using single-letter variables to match the equations from various academic papers
     * 
     * @param S a data set with 1 feature (for now) that is sorted by feature values
     * @return fills class variable 'cutPoints' with a list of where the cut points will be
     */
    private static void Discretize(ArrayList<ArrayList<Double>> S)
    {
        // end the recursion if S is a set of size 1
        if (S.size() == 1)
        {
            
        }
        // assign the index in the current subset that results in the least entropy 
        int minEntropyIndex = findMinEntropyCut(S);
    }
    
    /**
     * Iterate through each data point in S and calculate the class information entropy
     * if a partition happened at that index. Return whichever index minimizes entropy
     * @param S
     * @return The index that minimizes entropy
     */
    private static int findMinEntropyCut(ArrayList<ArrayList<Double>> S)
    {
        int    bestIndex = -1;
        double minEntropy = Double.MAX_VALUE;
        
        // for each feature entry... (keeping as an ArrayList<ArrayList<Double>> to have the classification attached with it)
        for (int i = 1; i < S.size(); i++)
        {
            // make a cut point at this entry and calculate the resulting entropy
            
        }
        
        if (bestIndex < 0) throw new RuntimeException("index never set in find min entropy cut");
        else
            return bestIndex;
    }
    
    private static ArrayList<ArrayList<Double>> sortFeature(ArrayList<ArrayList<Double>> input)
    {
        // just using bubble sort for now becuase this is a prototype. 
        boolean swapped = true;
        while (swapped)
        {
            swapped = false;
            for (int i = 1; i < input.size(); i++)
            {
                if (input.get(i-1).get(1) > input.get(i).get(1))
                {
                    ArrayList<Double> temp = input.get(i);
                    input.set(i, input.get(i-1));
                    input.set(i-1, temp);
                    swapped = true;
                }
            }
        }
        return input;
    }
    
    /**
     * Assumes the input dataset is already preprocessed with all values as doubles
     * and the first value is the categorization
     * 
     * @param filePath : the string file path used for the new File(path) call
     * @return An ArrayList<ArrayList> representation of the provided .txt data set
     * @throws FileNotFoundException 
     */
    private static ArrayList<ArrayList<Double>> generateDataSet(String filePath) throws FileNotFoundException
    {
        int lineNums;
        try (Scanner scanner0 = new Scanner(new File(filePath))) 
        {
            lineNums = 0;
            while (scanner0.hasNextLine())
            {
                lineNums++;
                scanner0.nextLine();
            }
        }
        
        String[][] fileAsString;
        int i;
        try (Scanner scanner1 = new Scanner(new File(filePath))) 
        {
            fileAsString = new String[lineNums][];
            i = 0;
            while (scanner1.hasNextLine())
            {
                String rowString = scanner1.nextLine();
                String[] rowSplit = rowString.split(",");
                fileAsString[i] = rowSplit;
                i++;
            }
        }
        
        // turn the double string array into its ArrayList<ArrayList> representation
        ArrayList<ArrayList<Double>> dataSet = new ArrayList<>();
        
        for (int dataPoint = 0; dataPoint < fileAsString.length; dataPoint++)
        {
            ArrayList<Double> vector = new ArrayList<>();
            String[] currentVector = fileAsString[dataPoint];
            
            for (int j = 0; j < currentVector.length; j++)
            {
                vector.add(Double.parseDouble(currentVector[j]));
            }
            dataSet.add(vector);
        }
        return dataSet;
    }
    
    private static void printADataSet(ArrayList<ArrayList<Double>> data)
    {
        for (ArrayList<Double> vector : data)
        {
            for (Double value : vector)
            {
                System.out.format("%.3f, ", value);
            }
            System.out.println();
        }
        System.out.println();
    }
}
