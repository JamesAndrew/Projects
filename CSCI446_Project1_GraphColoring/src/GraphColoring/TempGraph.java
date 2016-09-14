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
        //theGraphHashToNumberedVertices();
        //fillGraphEdges();
    }
    
    /**
     * Instantiates a new vertex with randomized (x,y) location.
     * Then gives each vertex a unique, incremental Integer identifier
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
        
        // <editor-fold defaultstate="collapsed" desc="Print vertex distance from 0 before sort">
        System.out.println("Vertex distance order before sort: ");
        generatedVertices.stream().forEach((vertex) -> {
            System.out.format("%f ", distance(0, 0, vertex.getxValue(), vertex.getyValue()));
        });
        // </editor-fold>
        
        MergeSort vertexSort = new MergeSort();
        generatedVertices = vertexSort.sort(generatedVertices);
        
        // <editor-fold defaultstate="collapsed" desc="Print vertex distance from 0 after sort">
        System.out.println("\nVertex distance order after sort: ");
        generatedVertices.stream().forEach((vertex) -> {
            System.out.format("%f ", distance(0, 0, vertex.getxValue(), vertex.getyValue()));
        });
        System.out.println("\n");
        // </editor-fold>
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
//        edges.get(sourceV).add(destV);
//        edges.get(destV).add(sourceV);
    }
    
    /**
     * Give me (DR) your thoughts on this. Having edges be a K,V map
    of Vertex objects is a good idea to me, but it could additionally be nice to 
    have a map that holds edges's map such that each vertex key has
    an associated int number to it.  Might be a good approach, especially for debugging 
    or visually displaying things
     */
    private void theGraphHashToNumberedVertices()
    {
//        Integer vertexNumber = 1;
//        Iterator<Vertex> graphMapItr = edges.keySet().iterator();
//        while (graphMapItr.hasNext())
//        {
//            Vertex currentVertex = graphMapItr.next();
//            currentVertex.vertexNum = vertexNumber;
//            theGraph.put(vertexNumber, currentVertex);
//            vertexNumber++;
//        }
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
    
    public void printPointLocations()
    {
//        System.out.println("graphMap point locations:");
//        edges.keySet().stream().forEach((vertex) -> 
//        {
//            System.out.format("(%.3f, %.3f)%n", vertex.getxValue(), vertex.getyValue());
//        });
    }
    
    public void printPointLocationsAndVertexNumber()
    {
        System.out.println("theGraph point locations:");
        theGraph.keySet().stream().forEach((Integer) -> {
            System.out.format("Vertex %d: (%.3f, %.3f)%n", Integer,
                theGraph.get(Integer).getxValue(), theGraph.get(Integer).getyValue());
        });
    }

    /**
     * @return the graphSize
     */
    public int getGraphSize() 
    {
        return graphSize;
    }
    
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
