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
    private static PrintWriter results;
    // A mapping of an algotihm to its integer array representing data from its run suite.
    // Array index 0: Total decisions made (over entire run suite)
    // Array index 1: Successful colorings (over entire run suite)
    private Map<Class<?>, int[]> runValues = new HashMap<>();
    ConstraintSolver solver;

    public ResultCalculator()
    {
        try
        {
            results = new PrintWriter(new FileWriter("Experimenting_With_Experimental_results.txt"));
        } catch (IOException ex)
        {
            Logger.getLogger(ResultCalculator.class.getName()).log(Level.SEVERE, null, ex);
        }
        for (Class<?> solverClass : Driver.solverList)
        {
            runValues.put(solverClass, new int[2]);
        }
    }

    /**
     * Updates information for the result of one graph being run on one
     * specific algorithm
     * Array index 0: Total decisions made (over entire run suite)
     * Array index 1: Successful colorings (over entire run suite)
     *
     * @param solver
     */
    public void calculateInstanceMetrics(ConstraintSolver solver)
    {
        runValues.get(solver.getClass())[0] += solver.decisionsMade;
        if (solver.isSatisfiesConstraint())
        {
            runValues.get(solver.getClass())[1]++;
        }
        
    }

    /**
     * prints out final details of the run results
     * @param numVertices 
     */
    public void printRunResults(int numVertices)
    {
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
    }
    
    public void printTotals(int iterations) 
    {
        System.out.println(iterations);
        for (Map.Entry<Class<?>, int[]> entry : runValues.entrySet())
        {
            results.println(entry.getKey().getSimpleName() + " average decisions: " + (float) entry.getValue()[0] / iterations);
            results.println(entry.getKey().getSimpleName() + " average successful colorings: " + (float) entry.getValue()[1] / iterations + "\n");
        }
    }
    
    public void closeWriter() 
    {
        results.close(); 
    }
}
