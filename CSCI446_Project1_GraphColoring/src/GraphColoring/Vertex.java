package GraphColoring;

import Exceptions.NotImplementedException;
import java.util.*;

/**
 * A vertex / Point / Node that a Graph will be composed of.
 * The Vertex class encapsulates important data values such as location,
 * and color into a single data type
 * @version 09/12/16
 */
public class Vertex 
{
    // The vetex's location on the unit square
    private final double xValue;
    private final double yValue;
    // The "Color" of the vertex. Just a value of 1, 2, 3, or 4.
    // Initial color is -1 "not assigned"
    public int color = -1;
    // The arbitrary "number" the vertex is identified as according to the
    // 'theGraph' field in TempGraph
    public int vertexNum;
    // edges : a collection of vertices that this vertex has an edge to.
    // Key = The desired connected vertex's integer identifier
    // Value = The vertex object
    public Map<Integer, Vertex> edges = new HashMap<>();
    
    /**
     * Instantiate a vertex with given (x,y) values. 
     * Initial color is -1 ("not assigned")
     * @param x : x value between [0,1]
     * @param y : y value between [0,1]
     */
    public Vertex(double x, double y)
    {
        xValue = x;
        yValue = y;
    }

    /**
     * Add an <Integer, Vertex> entry in edges to show that this vertex
     * if connected to the provided vertex.
     * To maintain an undirected graph, 
     * @param destination The vertex to connect to
     */
    public void setEdge(Vertex destination)
    {
        throw new NotImplementedException();
    }
    
    /**
     * @return the xValue
     */
    public double getxValue() 
    {
        return xValue;
    }
    /**
     * @return the yValue
     */
    public double getyValue() 
    {
        return yValue;
    }
}
