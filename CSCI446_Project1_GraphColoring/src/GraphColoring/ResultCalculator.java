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
    private Map<Class<?>, int[]> solverTotalsMap = new HashMap<Class<?>, int[]>();
    private Map<Class<?>, ArrayList<int[]>> solverSingleRunMap = new HashMap<Class<?>, ArrayList<int[]>>();
    ConstraintSolver solver;

    public ResultCalculator()
    {
        this.solver = solver;
        try
        {
            results = new PrintWriter(new FileWriter("Experimenting_With_Experimental_results.txt"));
        } catch (IOException ex)
        {
            Logger.getLogger(ResultCalculator.class.getName()).log(Level.SEVERE, null, ex);
        }
        for (Class<?> solverClass : Driver.solverList)
        {
            solverTotalsMap.put(solverClass, new int[2]);
        }
        for (Class<?> solverClass : Driver.solverList)
        {
            solverSingleRunMap.put(solverClass, new ArrayList<int[]>());
        }
    }

    /**
     * Work in progress. May take 1 to 4 inputs depending how we want to
     * calculate metrics
     *
     * @return
     */
    public void calculateRunMetrics(ArrayList<ConstraintSolver> solvers, int numVertices, int iteration)
    {
        for (ConstraintSolver s : solvers)
        {
            solverTotalsMap.get(s.getClass())[0] += s.decisionsMade;
            solverTotalsMap.get(s.getClass())[1] += s.validColorings;

            solverSingleRunMap.get(s.getClass()).add(new int[]
            {
                iteration, s.decisionsMade, s.validColorings
            });

            
        }
    }

    public void printRuns(int numVertices)
    {
        int iterations = 0; 
        for (Map.Entry<Class<?>, ArrayList<int[]>> entry : solverSingleRunMap.entrySet())
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
        for (Map.Entry<Class<?>, int[]> entry : solverTotalsMap.entrySet())
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
