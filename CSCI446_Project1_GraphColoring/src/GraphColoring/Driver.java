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
    private final static int numberOfGraphs = 3;
    // initial amount of nodes to have for first graph generation
    private final static int initialNumVertices = 10;
    // how many more vertices to have for each iteration of the graph
    private final static int vertexGrowthSize = 10;
    //number of times to run each graph size 
    private final static int suiteSize = 10;
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
        filename = "GA_testing";
        //filename = input.next();

        try
        {
            results = new PrintWriter(new FileWriter(filename + "_results.txt"));
            runs = new PrintWriter(new FileWriter(filename + "_runs.txt"));
        } catch (IOException e)
        {
            System.err.println("Caught IOException: " + e.getMessage());
        }

        /**
         * Start initializing experiment
         */
        runs.println("=== Starting Runs ===");
        runs.format("Number of graphs for each solver: %d%n"
                + "Vertex growth size: %d%n"
                + "Solvers being used: %s%n%n",
                numberOfGraphs, vertexGrowthSize, printSolversUsed());

        results.println("Solver Decisions");

        // while there are more graphs to generate
        while (currentGraphIteration < numberOfGraphs)
        {
            results.println("Graph " + currentGraphIteration);
            for (int i = 0; i < suiteSize; i++)
            {
                // generate the graph to use this iteration
                Graph currentGraph = new Graph_Generator(numVertices, runs).generateGraph();
                for (ConstraintSolver solver : solvers)
                {
                    results.print(solver.getClass() + " ");
                    printNextRunData(solver.getClass(), numVertices);
                    solver.updateGraph(currentGraph);

                    solver.setMaxColors(4);

                    solver.printFile(runs);

                    solver.runSolver();

                    // <editor-fold defaultstate="collapsed" desc="Print graph after solver run if desired">
//                System.out.println("\n=== Graph Print After Current Solver Run: ===");
//                currentGraph.printGraph();
                    // </editor-fold>
                    results.print(solver.getDecisionsMade() + " ");
                    results.println(solver.getValidColorings());
                }
                totalIterations++; 
                calc.calculateRunMetrics(solvers, numVertices, i);
            }
            numVertices += vertexGrowthSize;
            currentGraphIteration++;
            calc.printRuns(numVertices);
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
        runs.println("= Running next solver =");
        runs.format("Current algorithm: %s%n", currentSolver.getSimpleName());
        runs.format("Graph size: %d%n", size);
    }
    // </editor-fold>
}
