
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
    /**
     * Start with a .txt file of a one-feature data set with continuous values,
     * end with an ArrayList<ArrayList<Integer>> of discretized values
     * @param args
     * @throws java.io.FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException 
    {
        // transform the .txt file into an ArrayList<ArrayList<Double>>
        ArrayList<ArrayList<Double>> currentDataSet = generateDataSet("DataSets/mock-data-for-discretizing.txt");
        
        // sort the data set (low-to-high) based on Feature_1 values
        currentDataSet = sortFeature(currentDataSet);
        printADataSet(currentDataSet);
        
        
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
