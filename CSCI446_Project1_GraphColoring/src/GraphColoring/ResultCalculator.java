package GraphColoring;

import Exceptions.NotImplementedException;

/**
 * Statistical behavior analyzer.  
 * Interacts with a ConstraintSolver class and provides metrics based on
 * its number of decisions made, valid colorings, vertices visited, and vertices recolored
 * @version 09/19/16
 */
public class ResultCalculator 
{
    ConstraintSolver solver;
    
    public ResultCalculator(ConstraintSolver solver)
    {
        this.solver = solver;
    }
    
    /**
     * Work in progress. May take 1 to 4 inputs depending how we want to calculate metrics
     * @return 
     */
    public double calculateRunMetrics()
    {
        throw new NotImplementedException();
    }
}
