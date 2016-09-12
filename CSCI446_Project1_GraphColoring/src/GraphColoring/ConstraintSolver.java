package GraphColoring;

import Exceptions.NotImplementedException;

/**
 * Abstract class that all constraint solving classes will extent to use the
 * `SatisfiedConstraint()` method to check if the problem is satisfied
 * @version 09/08/16
 */
public abstract class ConstraintSolver 
{
    /**
     * Determine if the state of a graph satisfied the constraint
     * @param graph : The graph to check satisfiability on 
     * @return 
     */
    public boolean SatisfiesConstraint(Graph graph)
    {
        int graphSize = graph.getPoints().length; 
        boolean satisfied = true; 
        for (int i =0; i < graphSize; i++) 
        {
            int pointColor = graph.getColor(i); 
            for (int j = 0; j < graphSize; j++) 
            {
                int adjacentColor = graph.getColor(j); 
                if (graph.getEdge(i, j) == 1 && adjacentColor == pointColor)
                {
                    satisfied = false; 
                }
            }
        }
        return satisfied; 
    }
}
