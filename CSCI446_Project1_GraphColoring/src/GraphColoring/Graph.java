package GraphColoring;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

// class Graph represents a graph object with attributes points and matrix 
public class Graph {
    public final Map<Integer, Vertex> theGraph;
    private int graphSize; 
    
    // constructor to initialize Graph class and its attributes
    public Graph(Map<Integer, Vertex> theGraph) 
    {
        this.theGraph = theGraph;   
        graphSize = theGraph.size();
    }
    
        // <editor-fold defaultstate="collapsed" desc="Various print methods">
    /**
     * Look through all entries in theGraph and display meaningful details
     */
    public final void printGraph()
    {
        System.out.println("\n=== Printing Graph State... ===");
        theGraph.keySet().stream().map((key) -> {
            System.out.format("Graph node %d. Printing vertex details...%n", key);
            return key;
        }).map((key) -> theGraph.get(key)).forEach((currentVertex) -> {
            printVertexDetails(currentVertex);
        });
        System.out.println();
    }
    
    /**
     * Print to console the vertex number, (x,y) value, color (if assigned),
     * and connected vertex numbers
     * @param vertex : The vertex to print details about
     */
    public void printVertexDetails(Vertex vertex)
    {
        System.out.format("Vertex number: %d, Location: (%f, %f), Color: %d%n", 
                vertex.vertexNum, vertex.getxValue(), vertex.getyValue(), vertex.color);
        if (!vertex.edges.isEmpty())
        {
            Iterator itr = vertex.edges.entrySet().iterator();
            System.out.print("Edges: ");
            while(itr.hasNext())
            {
                Map.Entry pair = (Map.Entry)itr.next();
                int key = (int)pair.getKey();
                Vertex value = (Vertex)pair.getValue();
                if (key != value.vertexNum)
                {
                    System.out.format("%n%nConnected vertex has mismatch between key "
                            + "and value.vertexNum. Key = %d, vaule.vertexNum = %d. Continuing...%n%n",
                            key, value.vertexNum);
                }
                System.out.format("%d, ", key);
            }
            System.out.println();
        }
        else 
            System.out.println("Edges: none assigned yet");
        
    }
    
    /**
     * Print to console the vertex numbers the input vertex is connected 
     * (has an edge) to.
     * @param vertex : The input vertex
     */
    public void printVertexConnections(Vertex vertex)
    {
        System.out.format("Vertex %d is connected to vertices: ", vertex.vertexNum);
        for (Integer key : vertex.edges.keySet()) 
        {
            System.out.format("%d, ", key);
        }
        System.out.println();
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Basic Getters and Setters">
    /**
     * @return the graphSize
     */
    public int getGraphSize() 
    {
        return graphSize;
    }
    // </editor-fold>
}
