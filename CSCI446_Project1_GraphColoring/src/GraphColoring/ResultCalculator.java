package GraphColoring;

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
    ConstraintSolver solver;

    public ResultCalculator()
    {
        try
        {
            result_data = new PrintWriter(new FileWriter("Run_Results.txt"));
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
     * Updates information for the result of one graph being run on one
     * specific algorithm
     * Array index 0: Total decisions made (over entire run suite)
     * Array index 1: Successful colorings (over entire run suite)
     * Array index 2: Total times data was provided 
     * Array index 3: Max decisions made
     * Array index 4: Min decisions made
     *
     * @param solver
     */
    public void calculateInstanceMetrics(ConstraintSolver solver)
    {
        runValues.get(solver.getClass())[0] += solver.decisionsMade;
        
        // total successfull colorings +1 if satisfied
        if (solver.isSatisfiesConstraint())
        {
            runValues.get(solver.getClass())[1]++;
        }
        
        // total times data provided + 1
        runValues.get(solver.getClass())[2]++;
        
        // update max decisions if largest decision yet
        if (solver.decisionsMade > runValues.get(solver.getClass())[3])
        {
            runValues.get(solver.getClass())[3] = solver.decisionsMade;
        }
        
        // update min decisions if smallest decision yet
        if (solver.decisionsMade < runValues.get(solver.getClass())[4])
        {
            runValues.get(solver.getClass())[4] = solver.decisionsMade;
        }
    }
    
    /**
     * prints out final details of the run results
     */
    public void printRunResults()
    {
        result_data.format("%30s=== %s Results ===%n%n", "", runType);
        
        
        
        for (Map.Entry<Class<?>, int[]> entry : runValues.entrySet())
        {
            int[] dataArray = entry.getValue();
            String formatHeaders = "%-20s%-20s%-20s%-20s%n";
            String formatData = "%-20.2f%-20d%-20d%-20.2f%n%n";
            
            result_data.format("%n%nNumber of runs: %d%n%n", dataArray[2]);
            
            result_data.format("%30s= %s =%n", "", entry.getKey().getSimpleName());
            result_data.format(formatHeaders, "Avg. Decisions", "Max Decisions", "Min Decisions", "Ratio of Successful Colorings");
            result_data.format(formatData, calculateAverage(dataArray, 0), dataArray[3], dataArray[4], calculateAverage(dataArray, 1));
        }
    }

    /**
     * Calculates average rounded two decimal places
     * Array index 0: Total decisions made (over entire run suite)
     * Array index 1: Successful colorings (over entire run suite)
     * Array index 2: Total times data was provided 
     * Array index 3: Max decisions made
     * Array index 4: Min decisions made
     * @param inputData : note that index 2 of inputData is the total samples
     * @param indexOfInterest 
     * @return 
     */
    private double calculateAverage(int[] inputData, int indexOfInterest)
    {        
        double val = (double)inputData[indexOfInterest] / (double)inputData[2];
        val = val*100;
        val = Math.round(val);
        val = val /100;
        
        return val;
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
