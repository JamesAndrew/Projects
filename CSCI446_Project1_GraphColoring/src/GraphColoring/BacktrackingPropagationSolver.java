package GraphColoring;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Graph coloring constraint solver using backtracking with forward checking
 * @version 09/08/16
 */
public class BacktrackingPropagationSolver extends ConstraintSolver
{
    private int numColors;
    private int[] colors;
    private OriginalGraph graph;
    private int numPoints;
    private int numberOfNodeColorings; 
    private ArrayList<ArrayList<Integer>> validColorings; 

    public BacktrackingPropagationSolver()
    {

    }

    public void solve(OriginalGraph graph, int numColors)
    {
        this.numColors = numColors;
        this.graph = graph;
        numPoints = graph.getPoints().length;
        colors = new int[numPoints];
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

        for (int c : colors)
        {
            System.out.println(c);
        }
    }

    private boolean backtrack(int point)
    {
        for (Iterator<Integer> iterator = validColorings.get(point).iterator(); iterator.hasNext();)
        {
            colors[point] = iterator.next();
            numberOfNodeColorings++; 
            if (satisfiesAllConstraint())
            {
                if (allAdjacentColored(point))
                {
                    return true;
                } else
                {
                    for (int i = 0; i < numPoints; i++)
                    {
                        if (graph.getEdge(point, i) == 1 && colors[i] == 0)
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
        colors[point] = 0;
        return false;
    }

    private boolean allAdjacentColored(int point)
    {
        for (int i = 0; i < numPoints; i++)
        {
            if (graph.getEdge(point, i) == 1 && colors[i] == 0)
            {
                return false;
            }
        }
        return true;
    }

    private boolean allNodesColored()
    {
        for (int color : colors)
        {
            if (color == 0)
            {
                return false; 
            }
        }
        return true; 
    }
    
    private boolean satisfiesAllConstraint()
    {
        boolean satisfied = true; 
        for (int i = 0; i < numPoints; i++) 
        {
            for (int j = 0; j < numPoints; j++) {
                if (graph.getEdge(i, j) == 1)
                {
                    if (colors[i] != 0) 
                    {
                        if (colors[i] == colors[j])
                        {
                            satisfied = false; 
                        }
                        validColorings.get(j).remove((Integer) colors[i]);
                    }
                    else if (validColorings.get(i).size() == 1){
                        validColorings.get(j).remove(validColorings.get(i).get(0));
                    }
                    if (validColorings.get(j).isEmpty())
                    {
                        satisfied = false; 
                    }
                }
            }
        }
        return satisfied; 
    }
    
}
