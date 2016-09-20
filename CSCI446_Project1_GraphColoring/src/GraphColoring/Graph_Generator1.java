package GraphColoring;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.Random;

public class Graph_Generator1
{

    private int numVertices;

    // create Scanner instance input prompt user for arguments 
    Scanner input = new Scanner(System.in);

    // create random instance 
    Random rand = new Random();

    public Graph_Generator1()
    {
        Graph1 graph = generate();
        SimpleBacktrackingSolver btSolver = new SimpleBacktrackingSolver(); 
        btSolver.solve(graph, 4);
        BacktrackingForwardCheckingSolver fSolver = new BacktrackingForwardCheckingSolver(); 
        fSolver.solve(graph, 4);
    }

    /**
     * Create a scatter of random points, connect the vertices such that edges
     * do not cross, and return the representing Graph object
     *
     * @return a Graph object of n vertices
     */
    private Graph1 generate()
    {
        System.out.println("=== Graph generator started ===");
        System.out.println("Enter number of vertices...:");
        numVertices = input.nextInt();
        System.out.println();

        Graph1 graph = new Graph1(numVertices);

        // for each vertex point generate a random (x,y) location between [0,1]
        for (int i = 0; i < numVertices; i++)
        {
            graph.setPoint(i, ((float) rand.nextInt(101)) / 100, ((float) rand.nextInt(101)) / 100);
        }
        //printPointLocations(graph);
        graph.sortPoints();
        printPointLocations(graph);

        connectVertices(graph);

        findClosestPoint(graph);

        printGraph(graph);

        return graph;
    }

    /**
     * Mathematical function for Euclidian distance between two 2-d points.
     * Distance between points P1(x_1, y_1) and P2(x_2, y_2) is distance(P1,P2)
     * = sqrt[(x_2 - x_1)^2 + (y_2 - y_1)^2]
     *
     * @param x1 Pt1's x value
     * @param y1 Pt1's y value
     * @param x2 Pt2's x value
     * @param y2 Pt2's y value
     * @return the distance as a float
     */
    private float distance(float x1, float y1, float x2, float y2)
    {
        return (float) Math.sqrt((Math.pow((x2 - x1), 2) + Math.pow((y2 - y1), 2)));
    }

    /**
     * Pick node, find closest node to it and connect edge if valid.
     *
     * @param graph
     */
    private void findClosestPoint(Graph1 graph)
    {
        Map<Integer, ArrayList> vertices = initializeVertexMap();

        while (!vertices.isEmpty())//!vertices.isEmpty()
        {
            // variable for random index used to select random point
            ArrayList keysAsArray = new ArrayList(vertices.keySet());
            int chosenPt = (int) keysAsArray.get(rand.nextInt(keysAsArray.size()));
            boolean edgeSet = false;
            System.out.println("Point " + graph.getXPoint(chosenPt) + ", " + graph.getYPoint(chosenPt) + " chosen.");

            while (!edgeSet && vertices.containsKey(chosenPt))
            {
                int closest = -1;        // closest distance to selected point 
                float dist;             // current distance being checked
                float pclosest = 100;   // closest distance to selected point seen thus far

                System.out.println(chosenPt);

                // loop through all points
                ArrayList<Integer> current = vertices.get(chosenPt);
                for (Iterator<Integer> iterator = current.iterator(); iterator.hasNext();)//for (int secondPt = 0; secondPt < numVertices; secondPt++)
                {
                    int secondPt = iterator.next();
                    // only consider pairs that do not have an edge between them, both directions
                    if (graph.getEdge(chosenPt, secondPt) != 1 && graph.getEdge(secondPt, chosenPt) != 1)
                    {
                        dist = distance(graph.getXPoint(chosenPt), graph.getYPoint(chosenPt), graph.getXPoint(secondPt), graph.getYPoint(secondPt));

                        if (dist > 0 && dist < pclosest)
                        {
                            closest = secondPt;
                            pclosest = dist;
                        }

                        System.out.format("Distance between point %d and %d is %f.%n", chosenPt, secondPt, dist);
                    } else
                    {
                        iterator.remove();
                    }
                }
                System.out.format("Closest point is %d.%n%n", closest);
                
                if (!current.isEmpty())
                {
                    if (!edgesIntersect(graph, chosenPt, closest))
                    {
                        graph.setEdge(chosenPt, closest, 1); // add the edge to the graph 
                        graph.setEdge(closest, chosenPt, 1); // add symmetric edge since graph is symmetric 
                        edgeSet = true; 
                    } else
                    {
                        System.out.println("Collision Detected\n");
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
        ArrayList<Integer> possibleConnections = new ArrayList<Integer>();
        for (int i = 0; i < numVertices; i++)
        {
            possibleConnections.add(i);
        }
        Map<Integer, ArrayList> vertices = new HashMap<Integer, ArrayList>();
        for (int i = 0; i < numVertices; i++)
        {
            ArrayList<Integer> comparisonVertices = new ArrayList<Integer>(possibleConnections);
            comparisonVertices.remove(i);
            vertices.put(i, comparisonVertices);
        }
        return vertices;
    }

    /**
     * Select some point X at random and connect X by a straight line to the
     * nearest point Y such that X is not already connected to Y and the line
     * crosses no other line.
     *
     * @param graph : the graph to interact with
     */
    private void connectVertices(Graph1 graph)
    {

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
    private boolean edgesIntersect(Graph1 graph, int point1, int point2)
    {
        float[] eq1 = getLineEquation(graph, point1, point2);

        //find all existing edges and calculate whether or not there is a possible intersection
        for (int i = 0; i < graph.getPoints().length; i++)
        {
            for (int j = 0; j < graph.getPoints().length; j++)
            {
                //if edge doesn't already exist
                if (graph.getEdge(i, j) != 0 && !(i == point1 && j == point2))
                {
                    float x = 0;
                    float y = 0;
                    float[] eq2 = getLineEquation(graph, i, j);
                    //line 1 is vertical 
                    if (eq1 == null && eq2 != null)
                    {
                        x = graph.getXPoint(point1);
                        y = (eq2[0] * x) + eq2[1];
                    } //line 2 is vertical
                    else if (eq2 == null && eq1 != null)
                    {
                        x = graph.getXPoint(i);
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
                    if (checkBounds(graph, x, y, point1, point2) && checkBounds(graph, x, y, i, j))
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
    private boolean checkBounds(Graph1 graph, float xIntercept, float yIntercept, int point1, int point2)
    {
        //System.out.format("(%f, %f) 1:%d 2:%d.%n", xIntercept, yIntercept, point1, point2);
        //if intercept falls within x range of edge
        if ((xIntercept > graph.getXPoint(point1) + .001 && xIntercept < graph.getXPoint(point2) - .001)
                || (xIntercept < graph.getXPoint(point1) - .001 && xIntercept > graph.getXPoint(point2) + .001))//(graph.getXPoint(point1) < graph.getXPoint(point2))
        {
            //if intercept falls within y range of edge
            if ((yIntercept > graph.getYPoint(point1) + .001 && yIntercept < graph.getYPoint(point2) - .001)
                    || (yIntercept < graph.getYPoint(point1) - .001 && yIntercept > graph.getYPoint(point2) + .001))
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
    private float[] getLineEquation(Graph1 graph, int point1, int point2)
    {
        float[] mb = new float[2];
        float x1 = graph.getXPoint(point1);
        float y1 = graph.getYPoint(point1);
        float x2 = graph.getXPoint(point2);
        float y2 = graph.getYPoint(point2);

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

    private void printPointLocations(Graph1 graph)
    {
        System.out.println("Generated point locations:");
        for (float[] point : graph.getPoints())
        {
            System.out.format("(%.3f, %.3f)%n", point[0], point[1]);
        }
        System.out.println();
    }

    private void printGraph(Graph1 graph)
    {
        System.out.println("Generated graph:");
        for (int i = 0; i < numVertices; i++)
        {
            for (int j = 0; j < numVertices; j++)
            {
                System.out.print(graph.getEdge(i, j) + " ");
            }
            System.out.println();
        }
        System.out.println();
    }
}
