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
    // how to automate the runs:
    // suiteType 1: do an instance suite
    // suiteType 2: do a run suite
    private final static int suiteType = 2;
    // number of times to run each graph size during an instance suite
    private final static int instanceSuiteIterations = 1;
    // number of iterations of n-graph runs to do for a run suite
    private final static int runSuiteIterations = 1;
    // number of graphs to use for teach run suite iteration
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
    private static PrintWriter results;
    private static PrintWriter runs;

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
        int currentGraphIteration = 0;
        int numVertices = initialNumVertices;
        int totalIterations = 0; 
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
            results = new PrintWriter(new FileWriter(filename + "_results.txt"));
            runs = new PrintWriter(new FileWriter(filename + "_runs.txt"));
        } catch (IOException e)
        {
            System.err.println("Caught IOException: " + e.getMessage());
        }

        // </editor-fold>
        
        /**
         * Start initializing experiment
         */
        results.println("=== Starting Runs ===");
        results.format("Number of graphs for each solver: %d%n"
                + "Vertex growth size: %d%n"
                + "Solvers being used: %s%n%n",
                numberOfGraphs, vertexGrowthSize, printSolversUsed());

        // how to automate the runs.
        // suiteType 1: do an instance suite
        // suiteType 2: do a run suite
        switch (suiteType)
        {
            // instance suite
            case 1:
                
                break;
                
            // run suite
            case 2:
                // repeat [run_suite_iterations] times
                results.println("Beginning Run Suite...");
                for (int iteration = 0; iteration < runSuiteIterations; iteration++)
                {
                    results.println("Run suite iteration: " + iteration);
                    
                    // while there are more graphs to generate
                    while (currentGraphIteration < numberOfGraphs)
                    {
                        results.println("Graph iteration: " + currentGraphIteration);
                        
                        Graph currentGraph = new Graph_Generator(numVertices, runs).generateGraph();
                        for (ConstraintSolver solver : solvers)
                        {
                            printNextRunData(solver.getClass(), numVertices);
                            
                            solver.updateGraph(currentGraph);
                            solver.setMaxColors(maxColors);
                            solver.assignPrintWriter(runs);
                            solver.runSolver();

                            // <editor-fold defaultstate="collapsed" desc="Print graph after solver run if desired">
        //                System.out.println("\n=== Graph Print After Current Solver Run: ===");
        //                currentGraph.printGraph();
                            // </editor-fold>
                        
                            results.format("Instance Decisions Made: %d%n", solver.getDecisionsMade());
                            results.format("Instance valid coloring: %b%n", solver.isSatisfiesConstraint());
                            
                            calc.calculateInstanceMetrics(solver);
                        }
                        
                        // increase class variables to set up for next graph
                        numVertices += vertexGrowthSize;
                        currentGraphIteration++;
                    }
                    
                    // set up for next run suite iteration
                    currentGraphIteration = 0;
                    numVertices = initialNumVertices;
                    totalIterations++; 
                }
            break;
        }
        
        calc.printTotals(totalIterations);
        calc.closeWriter(); 
        results.close();
        runs.close();
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
        results.println("\n= Running next solver =");
        results.format("Current algorithm: %s%n", currentSolver.getSimpleName());
        results.format("Graph size: %d%n", size);
    }
    // </editor-fold>
}
