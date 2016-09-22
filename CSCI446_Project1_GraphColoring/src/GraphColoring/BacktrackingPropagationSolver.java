package GraphColoring;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

/**
 * Graph coloring constraint solver using backtracking with forward checking
 *
 * @version 09/08/16
 */
public class BacktrackingPropagationSolver extends ConstraintSolver
{
    private int numPoints;
    private int numberOfNodeColorings;
    //private ArrayList<ArrayList<Integer>> validColorings;

    public BacktrackingPropagationSolver()
    {

    }

    @Override
    public void runSolver()
    {
        numPoints = graph.getGraphSize();
        numberOfNodeColorings = 0;

        ArrayList<ArrayList<Integer>> validColorings = new ArrayList<ArrayList<Integer>>();
        for (int i = 0; i < numPoints; i++)
        {
            validColorings.add(new ArrayList<Integer>());
            for (int j = 1; j <= maxColors; j++)
            {
                validColorings.get(i).add(j);
            }
        }

        System.out.println(backtrack(0, validColorings));
        System.out.format("Nodes colored: %d%n", numberOfNodeColorings);
    }

    private boolean backtrack(int point, ArrayList<ArrayList<Integer>> valColors)
    {
        ArrayList<ArrayList<Integer>> validColorings = new ArrayList<ArrayList<Integer>>(valColors);
        for (Iterator<Integer> iterator = validColorings.get(point).iterator(); iterator.hasNext();)
        {
            theGraph.get(point).color = iterator.next();
            numberOfNodeColorings++;
            if (propagate(point, validColorings) && allAdjacentColored(point))
            {
                if (allAdjacentColored(point))
                {
                    return true;
                } else
                {
                    for (int i = 0; i < numPoints; i++)
                    {
                        if (theGraph.get(point).edges.containsKey(i) && theGraph.get(i).color == -1)//colors[i] == 0) //getEdge(point, i) == 1 && colors[i] == 0)
                        {
                            if (backtrack(i, validColorings) && allNodesColored())
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



    private boolean propagate(int node, ArrayList<ArrayList<Integer>> validColorings)
    {
        Map<Integer, Vertex> edges = theGraph.get(node).edges;
        for (Map.Entry<Integer, Vertex> entry : edges.entrySet())
        {
            int edgePoint = entry.getKey();
            if (theGraph.get(edgePoint).color == -1) 
            {
                if (theGraph.get(node).color == -1)
                {
                    validColorings.get(edgePoint).remove((Integer) validColorings.get(node).get(0));
                }
                else 
                {
                     validColorings.get(edgePoint).remove((Integer) theGraph.get(node).color); 
                }
                if (validColorings.get(edgePoint).isEmpty()) 
                {
                    return false; 
                }
                if (validColorings.get(edgePoint).size() == 1) 
                {
                    propagate(edgePoint, validColorings); 
                }
            }
        }
        return true;
    }
}
