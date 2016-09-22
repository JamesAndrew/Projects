package GraphColoring;

import java.util.Map;

/**
 * Graph coloring constraint solver using the basic backtracking strategy
 *
 * @version 09/08/16
 */
public class SimpleBacktrackingSolver extends ConstraintSolver
{
    private int numPoints;
    private int numberOfNodeColorings;

    public SimpleBacktrackingSolver()
    {

    }

    @Override
    public void runSolver()
    {
        numPoints = graph.getGraphSize();
        numberOfNodeColorings = 0;
        System.out.println(backtrack(0));
        System.out.format("Nodes colored: %d%n", numberOfNodeColorings);
    }

    private boolean backtrack(int point)
    {
        for (int color = 1; color <= maxColors; color++)
        {
            theGraph.get(point).color = color;
            numberOfNodeColorings++;

            if (pointSatisfiesConstraint(point))
            {
                if (allAdjacentColored(point))
                {
                    return true;
                } else
                {
                    for (int i = 0; i < numPoints; i++)
                    {
                        if (theGraph.get(point).edges.containsKey(i) && theGraph.get(i).color == -1)
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
        theGraph.get(point).color = -1;
        return false;
    }

    private boolean allAdjacentColored(int point)
    {
        for (int i = 0; i < numPoints; i++)
        {
            if (theGraph.get(point).edges.containsKey(i) && theGraph.get(i).color == -1)
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
            if (entry.getValue().color == -1)
            {
                return false;
            }
        }
        return true;
    }
}
