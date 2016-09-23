package GraphColoring;

import java.util.Map;

/**
 * Graph coloring constraint solver using backtracking with forward checking
 * @version 09/08/16
 */
public class BacktrackingForwardCheckingSolver extends ConstraintSolver
{
    private int numPoints;
    private int numberOfNodeColorings; 

    public BacktrackingForwardCheckingSolver()
    {

    }

    /**
     * 
     */
    @Override
    public void runSolver()
    {
        numPoints = graph.getGraphSize();
        numberOfNodeColorings = 0; 
        System.out.println(backtrack(0));
        System.out.format("Nodes colored: %d%n", numberOfNodeColorings);
    }

    /**
     * Recursive backtracking algorithm to traverse graph 
     * @param point
     * @return true if path found, false if not
     */
    private boolean backtrack(int point)
    {
        //for each color, set node to color and move to first non colored edge
        for (int color = 0; color < maxColors; color++)
        {
            theGraph.get(point).color = color;
            if (pointSatisfiesConstraint(point))
            {
                numberOfNodeColorings++; 
                if (allAdjacentColored(point))
                {
                    return true;
                } else
                {
                    //move through each edge
                    for (int i = 0; i < numPoints; i++)
                    {
                        if (theGraph.get(point).edges.containsKey(i) && theGraph.get(i).color == -1)
                        {
                            //point reached with all edge nodes colored and all graph nodes colored. 
                            if (backtrack(i) && allNodesColored())
                            {
                                return true;
                            }
                            else 
                            {
                                break;
                            }
                        }
                    }
                }
            }
        }
        theGraph.get(point).color = -1;
        return false;
    }

    /**
     * Check that all adjacent points are colored 
     * @param point
     * @return 
     */
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

    /**
     * Check that all nodes have been colored 
     * @return 
     */
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
