package GraphColoring;

import java.util.Scanner;
import java.util.Random;

public class Graph_Generator
{

    private int numVertices;

    // create Scanner instance input prompt user for arguments 
    Scanner input = new Scanner(System.in);

    // create random instance 
    Random rand = new Random();

    public Graph_Generator()
    {
        generate();
    }

    /**
     * Create a scatter of random points, connect the vertices such that edges
     * do not cross, and return the representing Graph object
     *
     * @return a Graph object of n vertices
     */
    private Graph generate()
    {
        System.out.println("=== Graph generator started ===");
        System.out.println("Enter number of vertices...:");
        numVertices = input.nextInt();
        System.out.println();

        Graph graph = new Graph(numVertices);

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

    private void findClosestPoint(Graph graph)
    {
        int checkVertices[] = new int[numVertices];

        int x = 0;
        while (x < 5)
        {
            // variable for random index used to select random point
            int chosenPt = rand.nextInt(numVertices);
            System.out.println("Point " + graph.getXPoint(chosenPt) + ", " + graph.getYPoint(chosenPt) + " chosen.");

            int closest = 0;        // closest distance to selected point 
            float dist;             // current distance being checked
            float pclosest = 100;   // closest distance to selected point seen thus far

            // loop through all points
            for (int secondPt = 0; secondPt < numVertices; secondPt++)
            {
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
                }
            }
            System.out.format("Closest point is %d.%n%n", closest);
            graph.setEdge(chosenPt, closest, 1); // add the edge to the graph 
            graph.setEdge(closest, chosenPt, 1); // add symmetric edge since graph is symmetric 
            x++;
        }
    }

    /**
     * Select some point X at random and connect X by a straight line to the
     * nearest point Y such that X is not already connected to Y and the line
     * crosses no other line.
     *
     * @param graph : the graph to interact with
     */
    private void connectVertices(Graph graph)
    {

    }

    private boolean edgesIntersect(Graph graph, int point1, int point2)
    {
        float x1 = graph.getXPoint(point1);
        float x2 = graph.getXPoint(point2);
        float y1 = graph.getYPoint(point1);
        float y2 = graph.getYPoint(point1);
        float a1 = y2 - y1;
        float b1 = x1 - x2;
        float c1 = (a1 * x1) + (b1 * y1);

        for (int i = 0; i < graph.getPoints().length; i++)
        {
            for (int j = 0; j < graph.getPoints().length; j++)
            {
                if (graph.getEdge(i, j) != 0 && !(i == point1 && j == point2))
                {
                    float a2 = graph.getYPoint(j) - graph.getYPoint(i);
                    float b2 = graph.getXPoint(i) - graph.getXPoint(j);
                    float c2 = (a2 * graph.getXPoint(i)) + (b2 * graph.getYPoint(i));

                    float det = a1 * b2 - a2 * b1;
                    if (det == 0)
                    {
                        return false;
                    } else
                    {
                        float x = (b2 * c1 - b1 * c2) / det;
                        float y = (a1 * c2 - a2 * c1) / det;
                        return checkBounds(graph, x, y, point1, point2); 
                    }
                }
            }
        }
        return false;
    }

    private boolean checkBounds(Graph graph, float xIntercept, float yIntercept, int point1, int point2)
    {
        if (graph.getXPoint(point1) < graph.getXPoint(point2))
        {
            if (xIntercept > graph.getXPoint(point1) && xIntercept < graph.getXPoint(point2))
            {
                return true;
            }
        } else if (xIntercept < graph.getXPoint(point1) && xIntercept > graph.getXPoint(point2))
        {
            return true;
        }
        return false; 
    }

    private void printPointLocations(Graph graph)
    {
        System.out.println("Generated point locations:");
        for (float[] point : graph.getPoints())
        {
            System.out.format("(%.3f, %.3f)%n", point[0], point[1]);
        }
        System.out.println();
    }

    private void printGraph(Graph graph)
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
