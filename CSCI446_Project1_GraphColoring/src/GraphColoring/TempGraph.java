package GraphColoring;

import java.util.*;

/**
 *
 * @author David
 */
public class TempGraph 
{
    // theGraph : a collection of vertices where the key is the vertex number
    // and the value is the vertex instance
    public final Map<Integer, Vertex> theGraph = new HashMap<>();
    // constant to refer to for the provided graph size
    private final int graphSize;
    
    /** constructor to initialize Graph class and its attributes
     * 
     * @param n The number of vertices to have
     */
    public TempGraph(int n)
    {
        graphSize = n;
        fillGraphVertices();
        printGraph();
        fillGraphEdges();
        printGraph();
    }
    
    /**
     * Instantiates a new vertex with randomized (x,y) location.
     * Then gives each vertex a unique, incremental Integer identifier.
     * Also orders the ArrayList of vertices in order of their distance 
     * to point (0,0) then filles class variable 'theGraph' where each
     * Integer key also represents the vertex's ordered distance from origin
     */
    private void fillGraphVertices() 
    {
        ArrayList<Vertex> generatedVertices = new ArrayList<>(graphSize);
        Random rand = new Random();
        
        for (int i = 0; i < graphSize; i++)
        {
            double xValue = (double)rand.nextInt(1001) / 1000;
            double yValue = (double)rand.nextInt(1001) / 1000;
            
            Vertex newVertex = new Vertex(xValue, yValue);
            generatedVertices.add(i, newVertex);
        }
        
        // <editor-fold defaultstate="collapsed" desc="Print vertex distances from 0 before sort">
        System.out.println("Vertex distance order before sort: ");
        generatedVertices.stream().forEach((vertex) -> {
            System.out.format("%f ", distance(0, 0, vertex.getxValue(), vertex.getyValue()));
        });
        // </editor-fold>
        
        // sort the array based on distance to (0,0) using MergeSort
        MergeSort vertexSort = new MergeSort();
        generatedVertices = vertexSort.sort(generatedVertices);
        
        // <editor-fold defaultstate="collapsed" desc="Print vertex distances from 0 after sort">
        System.out.println("\nVertex distance order after sort: ");
        generatedVertices.stream().forEach((vertex) -> {
            System.out.format("%f ", distance(0, 0, vertex.getxValue(), vertex.getyValue()));
        });
        System.out.println("\n");
        // </editor-fold>
        
        // assign the vertex its number and fill class field 'theGraph' with ordered vertices
        for (int i = 0; i < generatedVertices.size(); i++)
        {
            Vertex currentVertex = generatedVertices.get(i);
            currentVertex.vertexNum = i;
            theGraph.put(i, generatedVertices.get(i));
        }
    }
    
    /**
     * Assign each vertex a connection to n other vertices
     * TODO: have this work according to the logic the assignment asks for. For
     * now I just have some arbitrary edge connection scheme going on
     */
    private void fillGraphEdges()
    {
        // Create a complete graph by connecting each vertex to every other
        // and running the setEdge() method
        theGraph.keySet().stream().forEach( (key) -> { 
            Vertex currentVertex = theGraph.get(key);
            
            theGraph.keySet().stream().forEach( (otherKey) -> { 
                Vertex otherVertex = theGraph.get(otherKey);
                if (key.equals(otherKey))
                {
                    // skip if the vertex is looking at itself. We don't want self-connections
                    //System.out.println("Current vertices are the same");
                }
                    
                else if (currentVertex.edges.containsKey(otherKey) || otherVertex.edges.containsKey(key))
                {
                    // do nothing, an edge already exists
                    //System.out.println("An edge already exists between these two vertices.");
                }
                else
                {
                    // create a bidirectional connection to maintain an undirected graph
                    currentVertex.setEdge(otherVertex);
                    otherVertex.setEdge(currentVertex);
                    //System.out.format("Connected vertex %d to vertex %d.%n", key, otherKey);
                }
            });
        });
    }
    
    /**
     * Mathematical function for Euclidian distance between two 2-d points.
     * Distance between points P1(x_1, y_1) and P2(x_2, y_2) is
     *     distance(P1,P2) = sqrt[(x_2 - x_1)^2 + (y_2 - y_1)^2]
     * 
     * @param x1 Pt1's x value
     * @param y1 Pt1's y value
     * @param x2 Pt2's x value
     * @param y2 Pt2's y value
     * @return the distance as a float
     */
    private float distance(double x1, double y1, double x2, double y2) 
    {
        return (float) Math.sqrt((Math.pow((x2 - x1), 2) + Math.pow((y2 - y1), 2)));
    }
    
    
    // <editor-fold defaultstate="collapsed" desc="Various print methods">
    /**
     * Look through all entries in theGraph and display meaningful details
     */
    private void printGraph()
    {
        System.out.println("Printing Graph State...");
        for (Integer key : theGraph.keySet()) {
            System.out.format("Graph node %d. Printing vertex details...%n", key);
            Vertex currentVertex = theGraph.get(key);
            printVertexDetails(currentVertex);
        }
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
    
    // <editor-fold defaultstate="collapsed" desc="All the methods needed for doing merge sort">
    /**
     * Takes an arraylist of vertices and sorts them into a new arraylist in order
     * of closest to point (0,0) to furthers.
     * Uses merge sort.
     * Merge sort implementation referenced from 
     * http://www.java2novice.com/java-sorting-algorithms/merge-sort/
     * and nearly every java algorithms book written
     * @param inputArray : The array to sort
     */
    class MergeSort
    {
        private ArrayList<Vertex> inputArray;
        private Vertex[] tempArray;
        private int length;
        
        private ArrayList<Vertex> sort(ArrayList<Vertex> input)
        {
            this.inputArray = input;
            length = inputArray.size();
            tempArray = new Vertex[length];
            doMergeSort(0, length - 1);
            
            return inputArray;
        }
        
        private void doMergeSort(int lower, int higher)
        {
            if (lower < higher)
            {
                int middle = lower + (higher - lower) / 2;
                doMergeSort(lower, middle);
                doMergeSort(middle+1, higher);
                mergeParts(lower, middle, higher);
            }
        }
        
        private void mergeParts(int lower, int middle, int higher)
        {
            for (int i = lower; i <= higher; i++)
            {
                tempArray[i] = inputArray.get(i);
            }
            int i = lower;
            int j = middle + 1;
            int k = lower;
            while (i <= middle && j <= higher)
            {
                if (getDistanceAtIndex(i, tempArray) <= getDistanceAtIndex(j, tempArray))
                {
                    inputArray.set(k, tempArray[i]);
                    i++;
                }
                else
                {
                    inputArray.set(k, tempArray[j]);
                    j++;
                }
                k++;
            }
            while (i <= middle)
            {
                inputArray.set(k, tempArray[i]);
                k++;
                i++;
            }
        }
        
        private double getDistanceAtIndex(int index, Vertex[] array)
        {
            Vertex currentVertex = array[index];
            double x1 = 0.0;
            double y1 = 0.0;
            double x2 = currentVertex.getxValue();
            double y2 = currentVertex.getyValue();
            
            return distance(x1, y1, x2, y2);
        }
        
        private float distance(double x1, double y1, double x2, double y2) 
        {
            return (float) Math.sqrt((Math.pow((x2 - x1), 2) + Math.pow((y2 - y1), 2)));
        }
    }
    // </editor-fold>
}
