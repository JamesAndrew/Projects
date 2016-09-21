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
    
    // fitness is determined by how many color violations the current vertex
    // is causing. A fitness of 1 means no violations occur.
    public double fitness;
    
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
     * Fitness is determined by how many color violations the current vertex
     * is causing. A fitness of 1 means no violations occur. This method takes
     * a list of connected vertices as input. Currently using a linear scale
     * of 1 - (conflicts / connected_edges) to calculate the value
     * @param connections : list of vertices this vertex has edges to
     * @return A number between 0 and 1 that represents how fit this vertex is
     */
    public double calculateFitness(Vertex[] connections)
    {
        int numConnections = connections.length;
        int numConflicts = 0;
        
        for (Vertex vertex : connections)
        {
            if (this.color == vertex.color)
            {
                numConflicts++;
            }
        }
        
        double fitnessValue = 1 - (numConflicts / numConnections);
        
        return fitnessValue;
    }
    
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
    // </editor-fold>
}