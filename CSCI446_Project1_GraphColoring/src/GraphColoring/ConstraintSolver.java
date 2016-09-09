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
        throw new NotImplementedException();
    }
}
