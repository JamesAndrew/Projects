package classification;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 * The driver provides a pre-processed (normalized, unneeded features removed,
 * unknown features handled) List of data sets to a 'Experiment' instance.
 * Each data set is a List<ArrayList> data type where the ArrayList is 
 * each data point vector
 */
public class Driver 
{
    public static void main(String[] args) throws FileNotFoundException 
    {
        // The list of all pre-processed data sets
        ArrayList<ArrayList<ArrayList<Double>>> dataSets = new ArrayList<>();
        
        ArrayList<String> fileInputPaths = new ArrayList<>();
        // mocking a pre-processed data set until the pre-processing is fully ready
        fileInputPaths.add("DataSets/mock-data-set.txt");
        
        for (String dataTextFile : fileInputPaths)
        {
            ArrayList<ArrayList<Double>> currentDataSet = generateDataSet(dataTextFile);
            printADataSet(currentDataSet);
        }
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
                System.out.format("%.3f,", value);
            }
            System.out.println();
        }
    }
}