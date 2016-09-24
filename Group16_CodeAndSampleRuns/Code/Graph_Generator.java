package GraphColoring;

import java.io.PrintWriter;
import java.util.*;

/**
 *
 * @author David
 */
public class Graph_Generator 
{
    // create random instance 
    Random rand = new Random();
    // theGraph : a collection of vertices where the key is the vertex number
    // and the value is the vertex instance
    private final Map<Integer, Vertex> theGraph = new HashMap<>();
    // constant to refer to for the provided graph size
    private final int graphSize;
    
    /**
     * results and runs PrintWriter
     */
    protected static PrintWriter results;
    protected static PrintWriter runs;
    
    /** constructor to initialize Graph class and its attributes
     * 
     * @param n The number of vertices to have
     * @param run : writer used for text file output
     */
    public Graph_Generator(int n, PrintWriter run)
    {
        graphSize = n;
        
        runs = run;
    }
    
    public Graph generateGraph() 
    {
        fillGraphVertices();
        printVertexPlacements();
        connectEdges();
        printGraph();
        return new Graph(theGraph, runs); 
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
        Random randInt = new Random();
        
        for (int i = 0; i < graphSize; i++)
        {
            double xValue = (double)randInt.nextInt(1001) / 1000;
            double yValue = (double)randInt.nextInt(1001) / 1000;
            
            Vertex newVertex = new Vertex(xValue, yValue);
            generatedVertices.add(i, newVertex);
        }
        
        // <editor-fold defaultstate="collapsed" desc="Print vertex distances from 0 before sort">
//        System.out.println("Vertex distance order before sort: ");
//        generatedVertices.stream().forEach((vertex) -> {
//            System.out.format("%f ", distance(0, 0, vertex.getxValue(), vertex.getyValue()));
//        });
        // </editor-fold>
        
        // sort the array based on distance to (0,0) using MergeSort
        MergeSort vertexSort = new MergeSort();
        generatedVertices = vertexSort.sort(generatedVertices);
        
        // <editor-fold defaultstate="collapsed" desc="Print vertex distances from 0 after sort">
//        System.out.println("\nVertex distance order after sort: ");
//        generatedVertices.stream().forEach((vertex) -> {
//            System.out.format("%f ", distance(0, 0, vertex.getxValue(), vertex.getyValue()));
//        });
//        System.out.println("\n");
        // </editor-fold>
        
        // assign the vertex its number and fill class field 'theGraph' with ordered vertices
        for (int i = 0; i < generatedVertices.size(); i++)
        {
            Vertex currentVertex = generatedVertices.get(i);
            currentVertex.setVertexNum(i); 
            theGraph.put(i, generatedVertices.get(i));
        }
    }
    
    // <editor-fold defaultstate="collapsed" desc="All methods for connecting graph adges">
    /**
     * Pick node, find closest node to it and connect edge if valid.
     *
     * @param graph
     */
    private void connectEdges()
    {
        Map<Integer, ArrayList> vertices = initializeVertexMap();

        while (!vertices.isEmpty())//!vertices.isEmpty()
        {
            // variable for random index used to select random point
            ArrayList keysAsArray = new ArrayList(vertices.keySet());
            int chosenPt = (int) keysAsArray.get(rand.nextInt(keysAsArray.size()));
            boolean edgeSet = false;

            //System.out.format("Point %d (%f, %f) chosen.%n", chosenPt, theGraph.get(chosenPt).getxValue(), theGraph.get(chosenPt).getyValue());

            while (!edgeSet && vertices.containsKey(chosenPt))
            {
                int closest = -1;        // closest distance to selected point 
                float dist;             // current distance being checked
                float pclosest = 100;   // closest distance to selected point seen thus far

                // loop through all points
                ArrayList<Integer> current = vertices.get(chosenPt);
                for (Iterator<Integer> iterator = current.iterator(); iterator.hasNext();) //for (int secondPt = 0; secondPt < numVertices; secondPt++)
                {
                    int secondPt = iterator.next();
                    // only consider pairs that do not have an edge between them, both directions
                    if (!theGraph.get(chosenPt).edges.containsKey(secondPt))
                    {
                        dist = distance(theGraph.get(chosenPt).getxValue(), theGraph.get(chosenPt).getyValue(), 
                                theGraph.get(secondPt).getxValue(), theGraph.get(secondPt).getyValue());

                        if (dist > 0 && dist < pclosest)
                        {
                            closest = secondPt;
                            pclosest = dist;
                        }

                        //System.out.format("Distance between point %d and %d is %f.%n", chosenPt, secondPt, dist);
                    } else
                    {
                        iterator.remove();
                    }
                }
                //System.out.format("Closest point is %d.%n%n", closest);
                
                if (!current.isEmpty())
                {
                    if (!edgesIntersect(chosenPt, closest))
                    {
                        Vertex chosenV = theGraph.get(chosenPt); 
                        Vertex secondV = theGraph.get(closest); 
                        chosenV.setEdge(secondV);
                        secondV.setEdge(chosenV); 
                        //graph.setEdge(chosenPt, closest, 1); // add the edge to the graph 
                        //graph.setEdge(closest, chosenPt, 1); // add symmetric edge since graph is symmetric 
                        edgeSet = true; 
                    } else
                    {
                        //System.out.println("Collision Detected\n");
                        current.remove(current.indexOf(closest));
                    }
                } else
                {
                    {
                        vertices.remove(chosenPt);
                    }
                }
            }
        }
    }
    
        /**
     * Set map to keep track of valid nodes to make connection to. key = point
     * number, value = arraylist of currently valid nodes key node can connect
     * to.
     *
     * @return
     */
    private Map<Integer, ArrayList> initializeVertexMap()
    {
        ArrayList<Integer> possibleConnections = new ArrayList<>();
        for (int i = 0; i < graphSize; i++)
        {
            possibleConnections.add(i);
        }
        Map<Integer, ArrayList> vertices = new HashMap<>();
        for (int i = 0; i < graphSize; i++)
        {
            ArrayList<Integer> comparisonVertices = new ArrayList<>(possibleConnections);
            comparisonVertices.remove(i);
            vertices.put(i, comparisonVertices);
        }
        return vertices;
    }


    /**
     * Check whether or not edge between chosen point will intersect with
     * existing edges
     *
     * @param graph
     * @param point1
     * @param point2
     * @return : true if intersection found, false if not
     */
    private boolean edgesIntersect(int point1, int point2)
    {
        float[] eq1 = getLineEquation(point1, point2);

        //find all existing edges and calculate whether or not there is a possible intersection
        for (int i = 0; i < theGraph.keySet().size(); i++)
        {
            for (int j = 0; j < theGraph.keySet().size(); j++)
            {
                //if edge doesn't already exist
                if (theGraph.get(i).edges.containsKey(j) && !(i == point1 && j == point2))
                {
                    float x = 0;
                    float y = 0;
                    float[] eq2 = getLineEquation(i, j);
                    //line 1 is vertical 
                    if (eq1 == null && eq2 != null)
                    {
                        x = (float) theGraph.get(point1).getxValue(); // getXPoint(point1);
                        y = (eq2[0] * x) + eq2[1];
                    } //line 2 is vertical
                    else if (eq2 == null && eq1 != null)
                    {
                        x = (float) theGraph.get(i).getxValue();
                        y = (eq1[0] * x) + eq1[1];
                    } else if (eq1 == null && eq2 == null)
                    {
                        //two vertical lines
                    } //find intersect point between two non vertical lines
                    else
                    {
                        float m = eq2[0] - eq1[0];
                        float b = eq1[1] - eq2[1];
                        if (m == 0)
                        {
                            return false;
                        }
                        x = b / m;
                        y = (eq1[0] * x) + eq1[1];
                    }
                    if (checkBounds(x, y, point1, point2) && checkBounds(x, y, i, j))
                    {
                        return true;
                    }

                }
            }
        }
        return false;
    }

    /**
     * Check if a point falls within the x and y bounds of an edge between two
     * points
     *
     * @param graph
     * @param xIntercept
     * @param yIntercept
     * @param point1
     * @param point2
     * @return : true if within range, false if not
     */
    private boolean checkBounds(float xIntercept, float yIntercept, int point1, int point2)
    {
        //System.out.format("(%f, %f) 1:%d 2:%d.%n", xIntercept, yIntercept, point1, point2);
        //if intercept falls within x range of edge
        if ((xIntercept > theGraph.get(point1).getxValue() + .0001 && xIntercept < theGraph.get(point2).getxValue() - .0001)
                || (xIntercept < theGraph.get(point1).getxValue() - .0001 && xIntercept > theGraph.get(point2).getxValue() + .0001))
        {
            //if intercept falls within y range of edge
            if ((yIntercept > theGraph.get(point1).getyValue() + .0001 && yIntercept < theGraph.get(point2).getyValue() - .0001)
                    || (yIntercept < theGraph.get(point1).getyValue() - .0001 && yIntercept > theGraph.get(point2).getyValue() + .0001))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * get equation of line given two points
     *
     * @param graph
     * @param point1
     * @param point2
     * @return array containing slope and y intercept or null if vertical
     */
    private float[] getLineEquation(int point1, int point2)
    {
        float[] mb = new float[2];
        float x1 = (float) theGraph.get(point1).getxValue(); //getXPoint(point1);
        float y1 = (float) theGraph.get(point1).getyValue(); //getYPoint(point1);
        float x2 = (float) theGraph.get(point2).getxValue(); //getXPoint(point2);
        float y2 = (float) theGraph.get(point2).getyValue(); //getYPoint(point2);

        if (x1 != x2)
        {
            float m = (y2 - y1) / (x2 - x1);
            float b = (m * -x1) + y1;
            mb[0] = m;
            mb[1] = b;
            return mb;
        }
        return null;
    }
    // </editor-fold>
    
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
    
    public void printVertexPlacements()
    {
        runs.println("= Printing Vertex Locations =");
        for (Integer key : theGraph.keySet())
        {
            Vertex currentVertex = theGraph.get(key);
            runs.format("Vertex %d: (%f, %f)%n", key, currentVertex.getxValue(), currentVertex.getyValue());
        }
        runs.println();
    }
    
    /**
     * Print to console the vertex number, (x,y) value, color (if assigned),
     * and connected vertex numbers
     * @param vertex : The vertex to print details about
     */
    public void printVertexDetails(Vertex vertex)
    {
        runs.format("Vertex number: %d, Location: (%f, %f), Color: %d%n", vertex.getVertexNum(), vertex.getxValue(), vertex.getyValue(), vertex.color);
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
            runs.println("Edges: none assigned yet");
        
    }
    
    /**
     * Print to console the vertex numbers the input vertex is connected 
     * (has an edge) to.
     * @param vertex : The input vertex
     */
    public void printVertexConnections(Vertex vertex)
    {
        runs.format("Vertex %d is connected to vertices: ", vertex.getVertexNum());
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