import java.util.Scanner;
import java.util.Random;

public class Graph_Generator 
{
	private int n;
	
	// create Scanner instance input prompt user for arguments 
	Scanner input = new Scanner(System.in);
	
	// create random instance 
	Random rand = new Random();
	
	public Graph_Generator()
	{
		generate();
	}
	
	// the generate method creates a scatter of random points and returns a 2D array of integers 
	private Graph generate()
	{
		System.out.println("Graph generator started");
		System.out.println("enter number of vertices");
		n = input.nextInt();
		
		Graph graph = new Graph(n);

		for (int i = 0; i < n; i++)
		{
			for (int j = 0; j < 2; j++)
			{
				graph.setPoint(i, j, ((float)rand.nextInt(101))/100);
			}
			System.out.println(graph.getXPoint(i) + ", " + graph.getYPoint(i));
		}
		
		int x = 0;
		while (x < 2)
		{
			// variable for random index used to select random point
			int s = rand.nextInt(n);
			System.out.println("Point " + graph.getXPoint(s) + ", " + graph.getYPoint(s) + "chosen.");
			
			int closest = 0; // closet distance to selected point 
			float dist = 0; // current distance being checked
			float pclosest = 5; // closest distance to selected point seen thus far
			
			// loop through all points
			for (int i = 0; i < n; i++)
			{
				dist = distance(graph.getXPoint(s), graph.getYPoint(s), graph.getXPoint(i), graph.getYPoint(i));
				
				if (dist > 0 && dist < pclosest)
				{
					closest = i;
					pclosest = dist;
				}
				
				System.out.println("Distance of point " + i + "is" + dist);			
			}
			System.out.println("closest point is " + closest);
			graph.setEdge(s, closest, 1);
			x++;
		}
		
		return graph;
	}
	
	private float distance(float x1, float x2, float y1, float y2)
	{
		float dist = 0;
		dist = (float) Math.sqrt((Math.pow((x2 - x1), 2) + Math.pow((y2 - y1), 2)));
		return dist;
	}
}
