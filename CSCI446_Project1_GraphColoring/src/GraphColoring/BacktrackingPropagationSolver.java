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

    private int numColors;
    private int numPoints;
    private int numberOfNodeColorings;
    private ArrayList<ArrayList<Integer>> validColorings;

    public BacktrackingPropagationSolver()
    {

    }

    public void runSolver(int numColors)
    {
        this.numColors = numColors;
        numPoints = graph.getGraphSize();
        numberOfNodeColorings = 0;

        validColorings = new ArrayList<ArrayList<Integer>>();
        for (int i = 0; i < numPoints; i++)
        {
            validColorings.add(new ArrayList<Integer>());
            for (int j = 1; j <= numColors; j++)
            {
                validColorings.get(i).add(j);
            }
        }

        System.out.println(backtrack(0));
        System.out.format("Nodes colored: %d%n", numberOfNodeColorings);
    }

    private boolean backtrack(int point)
    {
        for (Iterator<Integer> iterator = validColorings.get(point).iterator(); iterator.hasNext();)
        {
            theGraph.get(point).color = iterator.next();
            numberOfNodeColorings++;
            if (propagate(point))
            {
                if (allAdjacentColored(point))
                {
                    return true;
                } else
                {
                    for (int i = 0; i < numPoints; i++)
                    {
                        if (theGraph.get(point).edges.containsKey(i) && theGraph.get(i).color == 0)//colors[i] == 0) //getEdge(point, i) == 1 && colors[i] == 0)
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



    private boolean propagate(int node)
    {
        Map<Integer, Vertex> edges = theGraph.get(node).edges;
        for (Map.Entry<Integer, Vertex> entry : edges.entrySet())
        {

        }
        return true;
    }

    @Override
    public void runSolver()
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
