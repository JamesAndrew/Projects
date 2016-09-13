package GraphColoring;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 *
 * @author David
 */
public class TempGraph 
{
    // The graph: a collection of Vertex keys and a set of vertex values
    // where the values are the other vertices the key vertex is connected to
    public final Map<Vertex, Set<Vertex>> theGraph = new HashMap<>();
    // constant to refer to for the provided graph size
    private final int graphSize;
    
    /** constructor to initialize Graph class and its attributes
     * 
     * @param n The number of vertices to have
     */
    public TempGraph(int n)
    {
        graphSize = n;
        fillGraph();
    }
    
    /**
     * Instantiates a new vertex with randomized (x,y) location
     */
    private void fillGraph() 
    {
        Random rand = new Random();
        for (int i = 0; i < graphSize; i++)
        {
            double xValue = (double)rand.nextInt(101) / 100;
            double yValue = (double)rand.nextInt(101) / 100;
            
            Vertex newVertex = new Vertex(xValue, yValue);
            theGraph.put(newVertex, new HashSet<>());
        }
    }
    
    public void printPointLocations()
    {
        System.out.println("theGraph point locations:");
        theGraph.keySet().stream().forEach((vertex) -> 
        {
            System.out.format("(%.3f, %.3f)%n", vertex.getxValue(), vertex.getyValue());
        });
    }
}
