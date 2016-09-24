package GraphColoring;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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
    private Map<Class<?>, int[]> runSuiteMap = new HashMap<>();
    // A mapping of an algotihm to its integer array representing data from 
    // a instance suite run (1 graph only, done [n] repetitions)
    // Array index 0: Total decisions made (over entire instance suite)
    // Array index 1: Successful colorings (over entire instance suite)
    private Map<Class<?>, int[]> instanceSuiteMap = new HashMap<>();
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
            runSuiteMap.put(solverClass, new int[2]);
        }
        for (Class<?> solverClass : Driver.solverList)
        {
            instanceSuiteMap.put(solverClass, new int[2]);
        }
    }

    /**
     * Work in progress. May take 1 to 4 inputs depending how we want to
     * calculate metrics
     *
     * @param solvers
     * @param numVertices
     * @param iteration
     */
    public void calculateRunMetrics(ArrayList<ConstraintSolver> solvers, int numVertices, int iteration)
    {
        for (ConstraintSolver solver : solvers)
        {
            runSuiteMap.get(solver.getClass())[0] += solver.decisionsMade;
            runSuiteMap.get(solver.getClass())[1] += solver.validColorings;

            instanceSuiteMap.get(solver.getClass()).add(new int[]
            {
                iteration, solver.decisionsMade, solver.validColorings
            });
        }
    }

    public void printRuns(int numVertices)
    {
        int iterations = 0; 
        for (Map.Entry<Class<?>, ArrayList<int[]>> entry : instanceSuiteMap.entrySet())
        {
            float averageDecisions = 0; 
            float averageColorings = 0; 
            results.println("Solver: " + entry.getKey().getSimpleName() + " Graph size: " + numVertices);
            for (Iterator<int[]> iterator = entry.getValue().iterator(); iterator.hasNext();)
            {
                iterations++;
                int[] current = iterator.next();
                averageDecisions += current[1];
                averageColorings += current[2];
                results.format("%d,%d,%d%n", current[0], current[1], current[2]);
                iterator.remove();
            }
            results.format("Average decisions:%f, Average successful colorings:%f.%n%n", averageDecisions / iterations, averageColorings / iterations);
            //System.out.println("Solver: " + s.getClass().getSimpleName() + " Totals: ");
            //System.out.println(solverTotalsMap.get(s.getClass())[0] + "," + solverTotalsMap.get(s.getClass())[1]);
        }
    }
    
    public void printTotals(int iterations) 
    {
        System.out.println(iterations);
        for (Map.Entry<Class<?>, int[]> entry : runSuiteMap.entrySet())
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
