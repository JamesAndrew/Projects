package classification;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * The driver provides a pre-processed (discretized, unneeded features removed,
 * unknown features handled) list of data sets to a 'Experiment' instance.
 * Each data set is a List<ArrayList> data type where the ArrayList is 
 * each data point vector
 */
public class Driver 
{
    // put the solvers you want the program to run on in here
    public final static List<Class<?>> algorithmList = Arrays.asList(
        NearestNeighbor.class,
        TAN.class
    );
    
    public static void main(String[] args) throws FileNotFoundException, InstantiationException, 
            IllegalAccessException, NoSuchMethodException, IllegalArgumentException, InvocationTargetException 
    {
        // The list of all pre-processed data sets
        ArrayList<ArrayList<ArrayList<Integer>>> dataSets = new ArrayList<>();
        ArrayList<String> fileInputPaths = new ArrayList<>();
        
        
        // Add the preprocessed sets here once ready:
        fileInputPaths.add("DataSets/Preprocessed/house-votes-preprocessed.txt");
        
        for (String dataTextFile : fileInputPaths)
        {
            ArrayList<ArrayList<Integer>> currentDataSet = generateDataSet(dataTextFile);
            dataSets.add(currentDataSet);
        }
        
        // instantiate the Experiment runner and provide it all the pre-processed data sets
        Experiment experimentRunner = new Experiment(dataSets);
        experimentRunner.runExperiment(algorithmList);
    }
    
    /**
     * Assumes the input dataset is already preprocessed with all values as doubles
     * and the first value is the categorization
     * 
     * @param filePath : the string file path used for the new File(path) call
     * @return An ArrayList<ArrayList> representation of the provided .txt data set
     * @throws FileNotFoundException 
     */
    private static ArrayList<ArrayList<Integer>> generateDataSet(String filePath) throws FileNotFoundException
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
                String[] rowSplit = rowString.split(", ");
                fileAsString[i] = rowSplit;
                i++;
            }
        }
        
        // turn the double string array into its ArrayList<ArrayList> representation
        ArrayList<ArrayList<Integer>> dataSet = new ArrayList<>();
        
        for (int dataPoint = 0; dataPoint < fileAsString.length; dataPoint++)
        {
            ArrayList<Integer> vector = new ArrayList<>();
            String[] currentVector = fileAsString[dataPoint];
            
            for (int j = 0; j < currentVector.length; j++)
            {
                vector.add(Integer.parseInt(currentVector[j]));
            }
            dataSet.add(vector);
        }
        return dataSet;
    }
    
    
    private static void printADataSet(ArrayList<ArrayList<Integer>> data)
    {
        for (ArrayList<Integer> vector : data)
        {
            for (Integer value : vector)
            {
                System.out.format("%.3f,", value);
            }
            System.out.println();
        }
    }
}