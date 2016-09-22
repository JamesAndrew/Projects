package GraphColoring;

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
    
    // The "Color" of the vertex. Just a value of 0, 1, 2, or 3
    // Initial color is -1 "not assigned"
    public int color = -1;
    
    // fitness is determined by if the current vertex is in conflict with any
    // of its connected vertices. True means no conflict occured
    public boolean fitness = false;
    
    // The arbitrary "number" the vertex is identified as according to the
    // 'theGraph' field in TempGraph
    private int vertexNum;
    
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
     * Fitness is determined by if the current vertex is in conflict with any
     * of its connected vertices. True means no conflict occured
     * @return true if no color violation, false if in violation
     */
    public boolean calculateFitness()
    {
        fitness = true;
        
        if (edges.isEmpty())
        {
            throw new RuntimeException("Vertex " + vertexNum + " has no edges connected to it.");
        }
        
        for (Vertex vertex : edges.values())
        {
            if (color == vertex.color)
            {
                fitness = false;
            }
        }
        
        return fitness;
    }
    
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Basic Getters and Setters">
    /**
     * Add an <Integer, Vertex> entry in edges to show that this vertex
     * if connected to the provided vertex.
     * @param destination The vertex to connect to
     */
    public void setEdge(Vertex destination)
    {
        edges.put(destination.vertexNum, destination);
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
    
    /**
     * @return the vertexNum
     */
    public int getVertexNum() 
    {
        return vertexNum;
    }
    
    /**
     * @param vertexNum the vertexNum to set
     */
    public void setVertexNum(int vertexNum) 
    {
        this.vertexNum = vertexNum;
    }
    
    /**
     * @return the fitness
     */
    public boolean getFitness() 
    {
        return fitness;
    }
    // </editor-fold>
}