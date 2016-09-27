package GraphColoring;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Statistical behavior analyzer. Interacts with a ConstraintSolver class and
 * provides metrics based on its number of decisions made, valid colorings,
 * vertices visited, and vertices recolored
 *
 * @version 09/19/16
 */
public class ResultCalculator
{
    // Run parameters:
    private int maxColors;
    private int runSuiteIterations;
    private int numberOfGraphs;
    private int initialNumVertices;
    private int vertexGrowthSize;
    
    // A mapping of an algotihm to its integer array representing data from its run suite.
    // Array index 0: Total decisions made (over entire run suite)
    // Array index 1: Successful colorings (over entire run suite)
    // Array index 2: Total times data was provided 
    // Array index 3: Max decisions made
    // Array index 4: Min decisions made
    private Map<Class<?>, int[]> runValues = new HashMap<>();
    // the print writer for the final run output (not for logging)
    private static PrintWriter result_data;
    // the run is either an instance suite or run suite
    private String runType;
    
    public ResultCalculator(String fileName, int colors, int suiteIterations, int numGraphs, int initNumVertices, int vertexGrowth)
    {
        maxColors = colors;
        runSuiteIterations = suiteIterations;
        numberOfGraphs = numGraphs;
        initialNumVertices = initNumVertices;
        vertexGrowthSize = vertexGrowth;
        
        
        try
        {
            result_data = new PrintWriter(new FileWriter(new File("Output_Files_Results_and_Logs", fileName)));
        } catch (IOException ex)
        {
            Logger.getLogger(ResultCalculator.class.getName()).log(Level.SEVERE, null, ex);
        }
        for (Class<?> solverClass : Driver.solverList)
        {
            // need min decisions to intiially be very large and max decisions be very small
            runValues.put(solverClass, new int[]{0, 0, 0, Integer.MIN_VALUE, Integer.MAX_VALUE});
        }
    }
    
    /**
     * After each new run, clear out the data in the result calculator
     */
    public void clearClassVariables()
    {
        runValues.clear();
    }

    /**
     * Updates information for the result of one graph being run on one
     * specific algorithm
     * Array index 0: Total successful decisions made (over entire run suite)
     * Array index 1: Successful colorings (over entire run suite)
     * Array index 2: Total times data was provided for satisfied graph
     * Array index 3: Max decisions made
     * Array index 4: Min decisions made
     *
     * @param solver
     */
    public void calculateInstanceMetrics(ConstraintSolver solver)
    {
        boolean satisfiedConstraint = solver.isSatisfiesConstraint();
        System.out.println("INSTANCE valid coloring: " + satisfiedConstraint);
        
        // only tally decisions made for the run if it satisfied the constraint
        if (satisfiedConstraint)
        {
            // only tally decisions made for the run if it satisfied the constraint
            runValues.get(solver.getClass())[0] += solver.getDecisionsMade();
            // total successfull colorings +1 if satisfied
            runValues.get(solver.getClass())[1] += 1;
            // update max decisions if largest decision yet for successful coloring
            if (solver.getDecisionsMade() > runValues.get(solver.getClass())[3])
            {
                runValues.get(solver.getClass())[3] = solver.getDecisionsMade();
            }
            // update min decisions if smallest decision yet for successful coloring
            if (solver.getDecisionsMade() < runValues.get(solver.getClass())[4])
            {
                runValues.get(solver.getClass())[4] = solver.getDecisionsMade();
            }
        }
        // total times data provided + 1
        runValues.get(solver.getClass())[2] += 1;
    }
    
    /**
     * prints out final details of the run results
     */
    public void printRunResults()
    {
        String formatHeader = "%-40s%-20s%-20s%-20s%-20s%n";
        String formatData = "%-5s%-35s%-20.2f%-20d%-20d%-20.6f%n";
        
        result_data.format("=== %s Results ===%n", "", runType);
        result_data.println("Run parameters: ");
        result_data.format(" - Max Colors: %d%n - Graphs Interacted With per Algorithm: %d%n - Graphs per Iteration: %d%n - Initial Num Vertices: %d%n - Vertex Growth Size: %d%n%n", 
                maxColors, runSuiteIterations, numberOfGraphs, initialNumVertices, vertexGrowthSize);
        result_data.format(formatHeader, "Name of Algorithm", "Avg. Decisions", "Max Decisions", "Min Decisions", "Ratio of Successful Colorings");
        result_data.println("Successful Colorings: ");
        for (Map.Entry<Class<?>, int[]> entry : runValues.entrySet())
        {
            int[] dataArray = entry.getValue();            
            result_data.format(formatData, "",
                    entry.getKey().getSimpleName(), 
                    calculateAverageDecisions(dataArray), 
                    calculateMaxDecisions(dataArray), 
                    calculateMinDecisions(dataArray), 
                    calculateSuccessfulColorings(dataArray));
        }
    }

    /**
     * Calculates average rounded two decimal places
     * Array index 0: Total successful decisions made (over entire run suite)
     * Array index 1: Successful colorings (over entire run suite)
     * Array index 2: Total times data was provided 
     * Array index 3: Max successful decisions made
     * Array index 4: Min successful  decisions made
     * @param inputData : note that index 2 of inputData is the total samples (not including failures)
     * @param indexOfInterest 
     * @return 
     */
    private double calculateAverageDecisions(int[] inputData)
    {
        double val;
        if (inputData[1] == 0)
        {
            val =  -1;
        }
        else
        {
            val = (double)inputData[0] / (double)inputData[2];
            val = val*100;
            val = Math.round(val);
            val = val /100;
        }
        
        return val;
    }
    
    private double calculateSuccessfulColorings(int[] inputData)
    {
        double val;
        if (inputData[1] == 0)
        {
            val =  0.0;
        }
        else
        {
            val = (double)inputData[1] / (double)inputData[2];
            val = val*100;
            val = Math.round(val);
            val = val /100;
        }
        
        return val;
    }
    
    private int calculateMaxDecisions(int[] inputData)
    {
        if (inputData[3] == Integer.MIN_VALUE)
        {
            return -1;
        }
        else
        {
            return inputData[3];
        }
    }
    
    private int calculateMinDecisions(int[] inputData)
    {
        if (inputData[4] == Integer.MAX_VALUE)
        {
            return -1;
        }
        else
        {
            return inputData[4];
        }
    }
    
    /**
     * @return the runType
     */
    public String getRunType() 
    {
        return runType;
    }

    /**
     * @param runType the runType to set
     */
    public void setRunType(String runType) 
    {
        this.runType = runType;
    }
    
 
    public void closeWriter() 
    {
        result_data.close(); 
    }
}
