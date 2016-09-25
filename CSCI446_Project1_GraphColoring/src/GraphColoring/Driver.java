package GraphColoring;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.*;

public class Driver
{
    // Parameters used for running multiple runs 
    // this will produce [numRuns] files. e.g. if this is set to 10 and 
    // [nextRunGraphSizeIncrease] is set to 10, this will produce 10 .txt files.
    // The first file shows results for graph size 10, next .txt file for 20, ... etc.
    private final static int numRuns = 1;
    private final static int nextRunGraphSizeIncrease = 10;
    private final static String fileOutputName = "Run_Results_GraphSize_";

    /**
     * Test settings and parameters. Based on these values we should be able to
     * do as many runs on as many solvers as desired.
     */
    // set constraint to 3 or 4 max colors allowed
    private final static int maxColors = 4;
    // number of iterations of n-graph runs to do for a run suite
    private final static int runSuiteIterations = 20;
    // number of graphs to use for each run suite iteration
    private final static int numberOfGraphs = 1;
    // initial amount of nodes to have for first graph generation
    private static int initialNumVertices = 100;
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
        // run the entire process n amount of times for n results on n different graph sizes
        for (int newRun = 0; newRun < numRuns; newRun++)
        {
            // <editor-fold defaultstate="collapsed" desc="Prerequisite setup before the run actually happens">
            int numVertices = initialNumVertices;
            ArrayList<ConstraintSolver> solvers = instantiateSolvers();

            // name of current run of experiment for logging .txt files (not official output)
            String filename = "instance_suite_testing";
            try
            {
                results_log = new PrintWriter(new FileWriter(new File("Output_Files_Results_and_Logs", filename + "_results_log.txt")));
                run_log = new PrintWriter(new FileWriter(new File("Output_Files_Results_and_Logs", filename + "_runs_log.txt")));
            } catch (IOException e)
            {
                System.err.println("Caught IOException: " + e.getMessage());
            }
            // assign name for current official run_results output file
            StringBuilder sb = new StringBuilder();
            sb.append(numVertices);
            sb.append(".txt");
            String resultsFilename = fileOutputName + sb.toString();
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
            ResultCalculator calc = new ResultCalculator(resultsFilename, maxColors, runSuiteIterations, numberOfGraphs, initialNumVertices, vertexGrowthSize);
            calc.setRunType("Run Suite");
            // repeat [run_suite_iterations] times
            results_log.println("Beginning Run Suite...");
            for (int iteration = 0; iteration < runSuiteIterations; iteration++)
            {
                results_log.format("%n== Run suite iteration: %d ==%n", iteration);
                System.out.println("\n= Run suite iteration: " + iteration + " =");

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
                        
                        // clear results values stored in the solver after each run
                        solver.resetRunMetrics();
                    }

                    // increase class variables to set up for next graph
                    numVertices += vertexGrowthSize;
                }

                // set up for next run suite iteration
                numVertices = initialNumVertices;
            }

            calc.printRunResults();
            calc.clearClassVariables();

            calc.closeWriter(); 
            results_log.close();
            run_log.close();
            
            initialNumVertices += nextRunGraphSizeIncrease;
        }
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
