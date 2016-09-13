package GraphColoring;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 *
 * @author David
 */
public class TempGraph 
{
    // graphMap : a collection of Vertex keys and a set of vertex values
    // where the values are the other vertices the key vertex is connected to
    public final Map<Vertex, Set<Vertex>> graphMap = new HashMap<>();
    // Once graphMap has been made, each veretx is assigned an integer value as
    // a key and is assigned to this field: theGraph
    public Map<Integer, Vertex> theGraph = new HashMap<>();
    // constant to refer to for the provided graph size
    private final int graphSize;
    // 
    
    /** constructor to initialize Graph class and its attributes
     * 
     * @param n The number of vertices to have
     */
    public TempGraph(int n)
    {
        graphSize = n;
        fillGraphVertices();
        theGraphHashToNumberedVertices();
        fillGraphEdges();
    }
    
    /**
     * Instantiates a new vertex with randomized (x,y) location
     */
    private void fillGraphVertices() 
    {
        Random rand = new Random();
        for (int i = 0; i < graphSize; i++)
        {
            double xValue = (double)rand.nextInt(101) / 100;
            double yValue = (double)rand.nextInt(101) / 100;
            
            Vertex newVertex = new Vertex(xValue, yValue);
            graphMap.put(newVertex, new HashSet<>());
        }
    }
    
    /**
     * Assign each vertex a connection to n other vertices
     * TODO: have this work according to the logic the assignment asks for. For
     * now I just have some arbitrary edge connection scheme going on
     */
    private void fillGraphEdges()
    {
        theGraph.keySet().stream().forEach((key) -> {
            theGraph.keySet().stream().forEach((otherKey) -> {
                if (key.equals(otherKey))
                    System.out.println("Current vertices are the same");
                else
                {
                    setEdge(theGraph.get(key), theGraph.get(otherKey));
                    System.out.format("Connected vertex %d to vertex %d.%n", key, otherKey);
                }
            });
            System.out.println();
        });
    }
    /**
     * Add an undirected edge to each Vertex's edge set (the value in the 
     * <K,V> hashmap
     * The method does the same operation for both source and destination
     * because of the undirected nature of the graph
     * @param sourceV : The vertex currently being looked at
     * @param destV : The vertex to connect sourceV to
     */
    private void setEdge(Vertex sourceV, Vertex destV) 
    {
        graphMap.get(sourceV).add(destV);
        graphMap.get(destV).add(sourceV);
    }
    
    /**
     * Give me (DR) your thoughts on this. Having graphMap be a K,V map
    of Vertex objects is a good idea to me, but it could additionally be nice to 
    have a map that holds graphMap's map such that each vertex key has
    an associated int number to it.  Might be a good approach, especially for debugging 
    or visually displaying things
     */
    private void theGraphHashToNumberedVertices()
    {
        Integer vertexNumber = 1;
        Iterator<Vertex> graphMapItr = graphMap.keySet().iterator();
        while (graphMapItr.hasNext())
        {
            theGraph.put(vertexNumber, graphMapItr.next());
            vertexNumber++;
        }
    }
    
    public void printPointLocations()
    {
        System.out.println("graphMap point locations:");
        graphMap.keySet().stream().forEach((vertex) -> 
        {
            System.out.format("(%.3f, %.3f)%n", vertex.getxValue(), vertex.getyValue());
        });
    }
    
    public void printPointLocationsAndVertexNumber()
    {
        System.out.println("theGraph point locations:");
        theGraph.keySet().stream().forEach((Integer) -> {
            System.out.format("Vertex %d: (%.3f, %.3f)%n", Integer,
                theGraph.get(Integer).getxValue(), theGraph.get(Integer).getyValue());
        });
    }
}
