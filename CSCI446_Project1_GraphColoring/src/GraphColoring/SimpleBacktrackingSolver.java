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
    private Graph graph;
    private int numPoints; 

    public SimpleBacktrackingSolver()
    {

    }

    public void solve(Graph graph, int numColors)
    {
        this.numColors = numColors;
        this.graph = graph;
        numPoints = graph.getPoints().length;
        colors = new int[numPoints];
        backtrack(0); 
    }

    private void backtrack(int point)
    {
        for (int color = 1; color < numColors; color++)
        {
            colors[point] = color;
            if (SatisfiesConstraint(graph, colors))
            {
                for (int i = 0; i < numPoints; i++)
                {
                    if (graph.getEdge(point, i) == 1 && colors[i] == 0)
                    {
                        backtrack(i); 
                    }
                }
            }
        }
    }
}
