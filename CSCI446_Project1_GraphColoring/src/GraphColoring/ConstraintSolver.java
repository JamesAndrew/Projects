package GraphColoring;

import java.util.Map;

/**
 * Abstract class that all constraint solving classes will extent to use the
 * `SatisfiedConstraint()` method to check if the problem is satisfied
 *
 * @version 09/08/16
 */
public abstract class ConstraintSolver
{
    /**
     * The following properties are for tracking metrics to be used in the statistical
     * ResultCalculator class. Each value for these properties represents the total
     * amount over all 10 provided graphs. (In other words, if the GeneticAlgorithmSolver
     * only gave a valid coloring on 7 of the 10 graphs, validColorings would equal 7.
     * If the average amount of decisions made was 100, decisions made would equal 1000).
     */
    protected int decisionsMade;
    protected int validColorings;
    protected int verticesVisited;   // might not use this one. Lets talk about it
    protected int verticesRecolored; 
    
    // Set by the driver to decide whether 3 or 4 colors are allowed for the run
    protected int maxColors;
    
    /**
     * Each solver has class variables that store the pointer to the current graph
     * being used
     */
    protected Graph graph;
    protected Map<Integer, Vertex> theGraph;
    
    /**
     * The logic of the current solver instance
     */
    public abstract void runSolver();
    
    /**
     * replace the current graph references with the next graph to run the 
     * solver on 
     * @param graph : the next graph to use
     */
    public void updateGraph(Graph graph)
    {
        this.graph = graph;
        this.theGraph = graph.theGraph;
    }
    
    /**
     * Determine if the state of a graph satisfied the constraint
     * @param graph : The graph to check satisfiability on 
     * @param colors 
     * @param point 
     * @return boolean : true if the constraint is satisfied
     */
    public boolean SatisfiesConstraint(OriginalGraph graph, int[] colors, int point)
    {
        //currently checks all adjacent points
        int graphSize = graph.getPoints().length;
        for (int i = 0; i < graphSize; i++)
        {
            int pointColor = colors[i];

            if (graph.getEdge(point, i) == 1 && pointColor == colors[point])
            {
                return false;
            }
        }
        return true;
    }

    // <editor-fold defaultstate="collapsed" desc="Basic Getters and Setters">
    /**
     * @return the decisionsMade
     */
    public int getDecisionsMade() {
        return decisionsMade;
    }

    /**
     * @return the validColorings
     */
    public int getValidColorings() {
        return validColorings;
    }

    /**
     * @return the verticesVisited
     */
    public int getVerticesVisited() {
        return verticesVisited;
    }

    /**
     * @return the verticesRecolored
     */
    public int getVerticesRecolored() {
        return verticesRecolored;
    }
    // </editor-fold>
}
