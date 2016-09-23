package GraphColoring;

import java.util.ArrayList;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Map;

// class Graph represents a graph object with attributes points and matrix 
public class Graph 
{
    public final Map<Integer, Vertex> theGraph;
    private final int graphSize; 
    // fitness is determined by how many color violations the current graph has
    // A fitness of 1 means no violations occur. Initial fitness 
    // is 0 
    private int fitness = 0;
    // The color of each vertex listen in the Map's key order
    private final ArrayList<Integer> chromosomeArray = new ArrayList<>();
    
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
    
    /**
     * Fitness is determined by how many color violations the current graph
     * is causing. A fitness of 1 means no violations occur. This method takes
     * @return a number between [0, graph_size] showing how many vertices were
     * not in conflict in the graph
     */
    public int calculateFitness()
    {
        fitness = 0;
        for (Vertex vertex : theGraph.values())
        {
            boolean vertFitness = vertex.calculateFitness();
            if (vertFitness)
            {
                fitness++;
            }
        }
        return fitness;
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
        //System.out.format("Vertex number: %d, Location: (%f, %f), Color: %d, Fitness: %b%n", 
        runs.format("Vertex number: %d, Location: (%f, %f), Color: %d, Fitness: %b%n", 
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
    
    /**
     * @return the fitness
     */
    public int getFitness() 
    {
        calculateFitness();
        return fitness;
    }
    
    /**
     * updates chromosome array before returning
     * @return the chromosomeArray
     */
    public ArrayList<Integer> getChromosomeArray() 
    {
        chromosomeArray.clear();
        for (Vertex chromosome : theGraph.values())
        {
            chromosomeArray.add(chromosome.color);
        }
        return chromosomeArray;
    }
    // </editor-fold>
}
