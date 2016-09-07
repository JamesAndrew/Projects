import java.util.Scanner;
import java.util.Random;

public class Graph_Generator {

    private int numVertices;

    // create Scanner instance input prompt user for arguments 
    Scanner input = new Scanner(System.in);

    // create random instance 
    Random rand = new Random();

    public Graph_Generator() {
        generate();
    }

    // the generate method creates a scatter of random points and returns a 2D array of integers 
    private Graph generate() {
        System.out.println("=== Graph generator started ===");
        System.out.println("Enter number of vertices...:");
        numVertices = input.nextInt();
        System.out.println();
        
        Graph graph = new Graph(numVertices);

        // for each vertex point...
        System.out.println("Generated point locations:");
        for (int i = 0; i < numVertices; i++) 
        {
            graph.setPoint(i, ((float) rand.nextInt(101)) / 100, ((float) rand.nextInt(101)) / 100);
            System.out.println(graph.getXPoint(i) + ", " + graph.getYPoint(i));
        }
        System.out.println();

        int x = 0;
        while (x < 3) 
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
            	if (graph.getEdge(chosenPt, secondPt) != 1 && graph.getEdge(secondPt, chosenPt) != 1)
            	{
	                dist = distance(graph.getXPoint(chosenPt), graph.getYPoint(chosenPt), graph.getXPoint(secondPt), graph.getYPoint(secondPt));
	
	                if (dist > 0 && dist < pclosest) {
	                    closest = secondPt;
	                    pclosest = dist;
	                }
	
	                System.out.format("Distance between point %d and %d is %f.%n", chosenPt, secondPt, dist);
            	}
            }
            System.out.format("Closest point is %d.%n%n", closest);
            //System.out.println("closest point is " + closest);
            graph.setEdge(chosenPt, closest, 1);
            graph.setEdge(closest, chosenPt, 1);
            x++;
        }

        return graph;
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
    private float distance(float x1, float y1, float x2, float y2) 
    {
        return (float) Math.sqrt((Math.pow((x2 - x1), 2) + Math.pow((y2 - y1), 2)));
    }
}