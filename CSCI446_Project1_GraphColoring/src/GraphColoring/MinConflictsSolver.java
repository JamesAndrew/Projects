package GraphColoring;

import java.util.Iterator;
import java.util.Map;

/**
 * Graph coloring constraint solver using the min conflicts strategy 
 * @version 09/08/16
 */
public class MinConflictsSolver extends ConstraintSolver
{
    private final TempGraph graph;
    private final Map<Integer, Vertex> theGraph;
    
    public MinConflictsSolver(TempGraph graph)
    {
        this.graph = graph;
        theGraph = graph.theGraph;
        minConflictsStrategy();
    }
    
    private void minConflictsStrategy()
    {
        System.out.println("minConflicts algorithm");
        Iterator itr = graph.theGraph.keySet().iterator();
        
        int steps = 0; // number of steps allowed till termination 
        System.out.println("number of steps: ");
        // keeping trying solutions till specified steps 
        // create a random complete assignment 
        
        // iterate over conflicting variables
        // if assignment is a solution, stop
        // else, carry on
    }
}
