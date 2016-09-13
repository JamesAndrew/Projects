package GraphColoring;

import java.util.Scanner;

public class Driver {
    
    static TempGraph graph;
    
    public static void main(String[] args) 
    {
        startupProcedure();
    }
    
    /**
     * todo: decide reliable, helpful things to do at "startup" such as
     * instantiate graph, decide which algorithm solver to use, etc.
     */
    public static void startupProcedure()
    {
        System.out.println("Enter number of vertices...:");
        Scanner input = new Scanner(System.in);
        int numVertices = input.nextInt();
        System.out.println();
        
        graph = new TempGraph(numVertices);
        graph.printPointLocations();
        graph.printPointLocationsAndVertexNumber();
    }
}
