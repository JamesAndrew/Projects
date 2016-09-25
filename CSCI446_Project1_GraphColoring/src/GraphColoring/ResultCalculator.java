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
        result_data.format("=== %s Results ===%n%n", runType);
        
        for (Map.Entry<Class<?>, int[]> entry : runValues.entrySet())
        {
            int[] dataArray = entry.getValue();
            
            result_data.format("= %s =%n", entry.getKey().getSimpleName());
            result_data.format("Total Decisions Made: %d%n", dataArray[0]);
            result_data.format("Total graphs interacted with: %d%n", dataArray[2]);
            result_data.format("Average Decisions Made: %.2f%n", calculateAverage(dataArray, 0));
            result_data.format("Max Decisions Made: %d%n", dataArray[3]);
            result_data.format("Min Decisions Made: %d%n", dataArray[4]);
            result_data.format("Successful Colorings: %d%n%n", dataArray[1]);
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
        double val = inputData[indexOfInterest] / inputData[2];
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
    
    /**
     * prints out final details of the run results
     */
//    public void printRunResults(int numVertices)
//    {
//        int iterations = 0; 
//        for (Map.Entry<Class<?>, ArrayList<int[]>> entry : runValues.entrySet())
//        {
//            float averageDecisions = 0; 
//            float averageColorings = 0; 
//            results.println("Solver: " + entry.getKey().getSimpleName() + " Graph size: " + numVertices);
//            for (Iterator<int[]> iterator = entry.getValue().iterator(); iterator.hasNext();)
//            {
//                iterations++;
//                int[] current = iterator.next();
//                averageDecisions += current[1];
//                averageColorings += current[2];
//                results.format("%d,%d,%d%n", current[0], current[1], current[2]);
//                iterator.remove();
//            }
//            results.format("Average decisions:%f, Average successful colorings:%f.%n%n", averageDecisions / iterations, averageColorings / iterations);
//            //System.out.println("Solver: " + s.getClass().getSimpleName() + " Totals: ");
//            //System.out.println(solverTotalsMap.get(s.getClass())[0] + "," + solverTotalsMap.get(s.getClass())[1]);
//        }
//    }
//    
//    public void printTotals(int iterations) 
//    {
//        System.out.println(iterations);
//        for (Map.Entry<Class<?>, int[]> entry : runValues.entrySet())
//        {
//            results.println(entry.getKey().getSimpleName() + " average decisions: " + (float) entry.getValue()[0] / iterations);
//            results.println(entry.getKey().getSimpleName() + " average successful colorings: " + (float) entry.getValue()[1] / iterations + "\n");
//        }
//    }
   
    public void closeWriter() 
    {
        result_data.close(); 
    }
}
