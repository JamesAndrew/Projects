package GraphColoring;

/**
 * Graph coloring constraint solver using the basic backtracking strategy
 *
 * @version 09/08/16
 */
public class SimpleBacktrackingSolver extends ConstraintSolver
{

    private int numColors;
    private int[] colors;
    private OriginalGraph graph;
    private int numPoints;
    private int numberOfNodeColorings; 

    public SimpleBacktrackingSolver()
    {

    }

    public void solve(OriginalGraph graph, int numColors)
    {
        this.numColors = numColors;
        this.graph = graph;
        numPoints = graph.getPoints().length;
        colors = new int[numPoints];
        numberOfNodeColorings = 0; 
        System.out.println(backtrack(0));
        System.out.format("Nodes colored: %d%n", numberOfNodeColorings);
        for (int c : colors)
        {
            System.out.println(c);
        }
    }

    private boolean backtrack(int point)
    {
        for (int color = 1; color <= numColors; color++)
        {
            colors[point] = color;
            numberOfNodeColorings++; 
            for (int c : colors)
            {
                System.out.println(c);
            }
            System.out.println();
            if (pointSatisfiesConstraint(graph, colors, point))
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
                            if (backtrack(i)  && allNodesColored())
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

    @Override
    public void runSolver() 
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
