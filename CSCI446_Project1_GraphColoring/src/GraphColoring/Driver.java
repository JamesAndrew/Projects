package GraphColoring;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Driver {
    /**
     * Test settings and parameters. Based on these values we should be able
     * to do as many runs on as many solvers as desired.
     */
    private final static int numberOfGraphs = 1;
    // initial amount of nodes to have for first graph generation
    private final static int initialNumVertices = 4;
    // how many more vertices to have for each iteration of the graph
    private final static int vertexGrowthSize = 0;
    // put the solvers you want the program to run on in here
    private final static List<Class<?>> solverList = Arrays.asList(
            GeneticAlgorithmSolver.class
    );
    
    public static void main(String[] args) throws InstantiationException, IllegalAccessException 
    {
        runSolvers();
    }
    
    /**
     * For current graph iteration 'A', generate the graph with 'n' vertices.
     * Send that graph to each of the algorithm solvers. Once each solver has run
     * its algorithm on the current graph, generate the next graph and repeat.
     */
    private static void runSolvers() throws InstantiationException, IllegalAccessException
    {
        int currentGraphIteration = 0;
        int numVertices = initialNumVertices;
        ArrayList<ConstraintSolver> solvers = instantiateSolvers();
        
        System.out.println("=== Starting Runs ===");
        System.out.format("Number of graphs for each solver: %d%n"
                + "Vertex growth size: %d%n"
                + "Solvers being used: %s%n%n",
                numberOfGraphs, vertexGrowthSize, printSolversUsed());
        
        // while there are more graphs to generate
        while (currentGraphIteration < numberOfGraphs)
        {
            // generate the graph to use this iteration
            Graph currentGraph = new Graph_Generator(numVertices).generateGraph();
            for (ConstraintSolver solver : solvers) 
            {
                printNextRunData(solver.getClass(), numVertices);
                solver.updateGraph(currentGraph);
                solver.runSolver();
                currentGraph.printGraph();
            }
            
            numVertices += vertexGrowthSize;
            currentGraphIteration++;
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
     * @param currentSolver
     * @param size
     */
    public static void printNextRunData(Class currentSolver, int size)
    {
        System.out.println("= Running next solver =");
        System.out.format("Current algorithm: %s%n", currentSolver.getSimpleName());
        System.out.format("Graph size: %d%n", size);
    }
    // </editor-fold>
}
