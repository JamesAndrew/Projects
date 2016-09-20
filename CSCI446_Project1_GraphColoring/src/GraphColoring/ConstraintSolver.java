package GraphColoring;

import Exceptions.NotImplementedException;
import java.util.Map;

/**
 * Abstract class that all constraint solving classes will extent to use the
 * `SatisfiedConstraint()` method to check if the problem is satisfied
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
    
    /**
     * Determine if the state of a graph satisfied the constraint
     * @param graph : The graph to check satisfiability on 
     * @return boolean : true if the constraint is satisfied
     */
    public boolean SatisfiesConstraint(Graph graph)
    {
        throw new NotImplementedException();
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
