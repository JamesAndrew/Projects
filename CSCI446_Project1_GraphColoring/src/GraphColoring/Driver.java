package GraphColoring;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.*;

public class Driver
{

    /**
     * Test settings and parameters. Based on these values we should be able to
     * do as many runs on as many solvers as desired.
     */
    // set constraint to 3 or 4 max colors allowed
    private final static int maxColors = 4;
    // number of iterations of n-graph runs to do for a run suite
    private final static int runSuiteIterations = 10;
    // number of graphs to use for each run suite iteration
    private final static int numberOfGraphs = 1;
    // initial amount of nodes to have for first graph generation
    private final static int initialNumVertices = 10;
    // how many more vertices to have for each iteration of the graph
    private final static int vertexGrowthSize = 0;
    // put the solvers you want the program to run on in here
    public final static List<Class<?>> solverList = Arrays.asList(
            SimpleBacktrackingSolver.class,
            BacktrackingForwardCheckingSolver.class,
            BacktrackingPropagationSolver.class,
            MinConflictsSolver.class,
            GeneticAlgorithmSolver.class
    );

    /**
     * results and runs PrintWriter
     */
    private static PrintWriter results_log;
    private static PrintWriter run_log;

    public static void main(String[] args) throws InstantiationException, IllegalAccessException
    {
        runSolvers();
    }

    /**
     * For current graph iteration 'A', generate the graph with 'n' vertices.
     * Send that graph to each of the algorithm solvers. Once each solver has
     * run its algorithm on the current graph, generate the next graph and
     * repeat.
     */
    private static void runSolvers() throws InstantiationException, IllegalAccessException
    {
        // <editor-fold defaultstate="collapsed" desc="Prerequisite setup before the run actually happens">
        ResultCalculator calc = new ResultCalculator();
        int numVertices = initialNumVertices;
        ArrayList<ConstraintSolver> solvers = instantiateSolvers();

        /**
         * Create files to write results and sample runs
         */
        // create Scanner instance input prompt user for filename
        Scanner input = new Scanner(System.in);

        // name of current run of experimental
        String filename;

        // prompt user for filename
        System.out.println("filename?");
        filename = "instance_suite_testing";
        //filename = input.next();

        try
        {
            results_log = new PrintWriter(new FileWriter(filename + "_results.txt"));
            run_log = new PrintWriter(new FileWriter(filename + "_runs.txt"));
        } catch (IOException e)
        {
            System.err.println("Caught IOException: " + e.getMessage());
        }

        // </editor-fold>
        
        /**
         * Start initializing experiment
         */
        results_log.println("=== Starting Runs ===");
        results_log.format("Number of graphs for each solver: %d%n"
                + "Vertex growth size: %d%n"
                + "Solvers being used: %s%n%n",
                numberOfGraphs, vertexGrowthSize, printSolversUsed());

        
        // run suite
        calc.setRunType("Run Suite");
        // repeat [run_suite_iterations] times
        results_log.println("Beginning Run Suite...");
        for (int iteration = 0; iteration < runSuiteIterations; iteration++)
        {
            results_log.format("%n== Run suite iteration: %d ==%n", iteration);
            System.out.println("Run suite iteration: " + iteration);

            // while there are more graphs to generate
            for (int i = 0; i < numberOfGraphs; i++)
            {
                results_log.format("%n= Graph iteration: %d =%n", i);

                Graph currentGraph = new Graph_Generator(numVertices, run_log).generateGraph();
                for (ConstraintSolver solver : solvers)
                {
                    printNextRunData(solver.getClass(), numVertices);

                    solver.updateGraph(currentGraph);
                    solver.setMaxColors(maxColors);
                    solver.assignPrintWriter(run_log);
                    solver.runSolver();

                    // <editor-fold defaultstate="collapsed" desc="Print graph after solver run if desired">
//                System.out.println("\n=== Graph Print After Current Solver Run: ===");
//                currentGraph.printGraph();
                    // </editor-fold>

                    results_log.format("Instance Decisions Made: %d%n", solver.getDecisionsMade());
                    results_log.format("Instance valid coloring: %b%n", solver.isSatisfiesConstraint());

                    calc.calculateInstanceMetrics(solver);
                }

                // increase class variables to set up for next graph
                numVertices += vertexGrowthSize;
            }

            // set up for next run suite iteration
            numVertices = initialNumVertices;
        }
        
        calc.printRunResults();
        
        calc.closeWriter(); 
        results_log.close();
        run_log.close();
    }

    private static ArrayList<ConstraintSolver> instantiateSolvers() throws InstantiationException, IllegalAccessException
    {
        ArrayList<ConstraintSolver> solvers = new ArrayList<>(solverList.size());
        for (Class<?> solver : solverList)
        {
            ConstraintSolver currentSolver = (ConstraintSolver) solver.newInstance();
            solvers.add(currentSolver);
        }

        return solvers;
    }

    // <editor-fold defaultstate="collapsed" desc="Various print methods">
    public static String printSolversUsed()
    {
        StringBuilder outputStr = new StringBuilder();

        for (Class<?> solver : solverList)
        {
            outputStr.append(solver.getSimpleName());
            outputStr.append(", ");
        }

        String output = outputStr.toString();
        return output;
    }

    /**
     * Shows which constraint solver is about to be run with associated data
     *
     * @param currentSolver
     * @param size
     */
    public static void printNextRunData(Class currentSolver, int size)
    {
        results_log.println("\nRunning next solver ");
        results_log.format("Current algorithm: %s%n", currentSolver.getSimpleName());
        results_log.format("Graph size: %d%n", size);
        
        System.out.println("\nRunning next solver ");
        System.out.format("Current algorithm: %s%n", currentSolver.getSimpleName());
        System.out.format("Graph size: %d%n", size);
    }
    // </editor-fold>
}
