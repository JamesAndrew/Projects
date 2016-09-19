package GraphColoring;

import java.util.Scanner;

public class Driver {
    
    static Graph graph;
    
    public static void main(String[] args) 
    {
        startupProcedure();
        GreedySolver greedySolver = new GreedySolver(graph);
        graph.printGraph();
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
        
        Graph_Generator graphGen = new Graph_Generator(numVertices);
        graph = graphGen.generateGraph();
    }
}
