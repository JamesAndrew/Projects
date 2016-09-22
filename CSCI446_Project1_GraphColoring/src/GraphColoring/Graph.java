package GraphColoring;

import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Map;

// class Graph represents a graph object with attributes points and matrix 
public class Graph {
    public final Map<Integer, Vertex> theGraph;
    private final int graphSize; 
    
    /**
     * results and runs PrintWriter
     */
    protected static PrintWriter results;
    protected static PrintWriter runs;
    
    // constructor to initialize Graph class and its attributes
    public Graph(Map<Integer, Vertex> theGraph, PrintWriter run) 
    {
        this.theGraph = theGraph;   
        graphSize = theGraph.size();
        
        runs = run;
    }
    
    // <editor-fold defaultstate="collapsed" desc="Various print methods">
    /**
     * Look through all entries in theGraph and display meaningful details
     */
    public final void printGraph()
    {
        runs.println("\n=== Printing Graph State... ===");
        theGraph.keySet().stream().map((key) -> {
            runs.format("Graph node %d. Printing vertex details...%n", key);
            return key;
        }).map((key) -> theGraph.get(key)).forEach((currentVertex) -> {
            printVertexDetails(currentVertex);
        });
        runs.println();
    }
    
    /**
     * Print to console the vertex number, (x,y) value, color (if assigned), fitness value
     * and connected vertex numbers
     * @param vertex : The vertex to print details about
     */
    private void printVertexDetails(Vertex vertex)
    {
        runs.format("Vertex number: %d, Location: (%f, %f), Color: %d, Fitness: %f%n", 
                vertex.getVertexNum(), vertex.getxValue(), vertex.getyValue(), vertex.color, vertex.getFitness());
        if (!vertex.edges.isEmpty())
        {
            Iterator itr = vertex.edges.entrySet().iterator();
            runs.print("Edges: ");
            while(itr.hasNext())
            {
                Map.Entry pair = (Map.Entry)itr.next();
                int key = (int)pair.getKey();
                Vertex value = (Vertex)pair.getValue();
                if (key != value.getVertexNum())
                {
                    runs.format("%n%nConnected vertex has mismatch between key "
                            + "and value.vertexNum. Key = %d, vaule.vertexNum = %d. Continuing...%n%n",
                            key, value.getVertexNum());
                }
                runs.format("%d, ", key);
            }
            runs.println();
        }
        else 
        {
            runs.println("Edge connections to vertices: ");
            printVertexConnections(vertex);
        }
    }
    
    /**
     * Print to console the vertex numbers the input vertex is connected 
     * (has an edge) to.
     * @param vertex : The input vertex
     */
    public void printVertexConnections(Vertex vertex)
    {
        for (Integer key : vertex.edges.keySet()) 
        {
            runs.format("%d, ", key);
        }
        runs.println();
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
