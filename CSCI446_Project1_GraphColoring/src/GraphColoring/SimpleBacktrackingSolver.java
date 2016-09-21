package GraphColoring;

import java.util.Map;

/**
 * Graph coloring constraint solver using the basic backtracking strategy
 *
 * @version 09/08/16
 */
public class SimpleBacktrackingSolver extends ConstraintSolver
{

    private int numColors;
    private int numPoints;
    private int numberOfNodeColorings;

    public SimpleBacktrackingSolver()
    {

    }

    public void runSolver(int numColors)
    {
        this.numColors = numColors;
        numPoints = graph.getGraphSize();
        numberOfNodeColorings = 0;
        System.out.println(backtrack(0));
        System.out.format("Nodes colored: %d%n", numberOfNodeColorings);
    }

    private boolean backtrack(int point)
    {
        for (int color = 1; color <= numColors; color++)
        {
            theGraph.get(point).color = color;
            numberOfNodeColorings++;

            if (SatisfiesConstraint(point))
            {
                if (allAdjacentColored(point))
                {
                    return true;
                } else
                {
                    for (int i = 0; i < numPoints; i++)
                    {
                        if (theGraph.get(point).edges.containsKey(i) && theGraph.get(i).color == 0)
                        {
                            if (backtrack(i) && allNodesColored())
                            {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        theGraph.get(point).color = 0;
        return false;
    }

    private boolean allAdjacentColored(int point)
    {
        for (int i = 0; i < numPoints; i++)
        {
            if (theGraph.get(point).edges.containsKey(i) && theGraph.get(i).color == 0)
            {
                return false;
            }
        }
        return true;
    }

    private boolean allNodesColored()
    {
        for (Map.Entry<Integer, Vertex> entry : theGraph.entrySet())
        {
            if (entry.getValue().color == 0)
            {
                return false;
            }
        }
        return true;
    }

    @Override
    public void runSolver()
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
