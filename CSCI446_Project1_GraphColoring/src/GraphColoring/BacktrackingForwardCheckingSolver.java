package GraphColoring;

import java.util.Map;

/**
 * Graph coloring constraint solver using backtracking with forward checking
 * @version 09/08/16
 */
public class BacktrackingForwardCheckingSolver extends ConstraintSolver
{
    private int numPoints;

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
        verticesVisited = 0; 
        if(backtrack(0))
        {
            validColorings++; 
        }
        System.out.format("Nodes colored: %d%n", verticesVisited);
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
                runs.format("node %d given color %d%n", point, color);
                decisionsMade++; 
                verticesVisited++; 
                if (allAdjacentColored(point))
                {
                    runs.println("All surrounding nodes colored");
                    return true;
                } else
                {
                    //move through each edge
                    for (int i = 0; i < numPoints; i++)
                    {
                        decisionsMade++; 
                        if (theGraph.get(point).edges.containsKey(i) && theGraph.get(i).color == -1)
                        {
                            //point reached with all edge nodes colored and all graph nodes colored. 
                            if (backtrack(i) && allNodesColored())
                            {
                                runs.println("Solution found");
                                return true;
                            }
                            //exit edge loop if unsuccessful coloring found 
                            else 
                            {
                                break;
                            }
                        }
                    }
                }
                runs.println("Backtracking");
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
            decisionsMade++; 
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
            decisionsMade++; 
            if (entry.getValue().color == -1)
            {
                return false;
            }
        }
        return true; 
    }
}
