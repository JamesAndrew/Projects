package GraphColoring;

import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

/**
 * Graph coloring constraint solver using the min conflicts strategy 
 * @version 09/08/16
 */
public class MinConflictsSolver extends ConstraintSolver
{
    private final TempGraph graph;
    private final Map<Integer, Vertex> theGraph;
    private int maxColor = 4;
    private int steps;
    
    public MinConflictsSolver(TempGraph graph, int s)
    {
        this.graph = graph;
        theGraph = graph.theGraph;
        steps = s;
        minConflictsStrategy();
    }
    
    private void minConflictsStrategy()
    {
        // create Scanner object to take input from user
        Scanner input = new Scanner(System.in);
        // create Random object to generate random numbers
        Random rand = new Random();
        
        // create iterator to loop through nodes
        Iterator itr = graph.theGraph.keySet().iterator();
        
        System.out.println("minConflicts algorithm");
        
        /**
         * keeping trying solutions till a solution has been found or 
         * the specified number of steps
         */
        int step = 0;
        while (step < steps)
        {
            System.out.println("step " + step);
            
            // start up new iterator
            itr = graph.theGraph.keySet().iterator();
            
            /**
             * Create a random complete assignment, not necessarily correct
             */
            // Color first vertex with a random color
            int firstKey = (int)itr.next();
            theGraph.get(firstKey).color = rand.nextInt(maxColor-1) + 1;
            System.out.format("Gave vertex %d color %d.%n", theGraph.get(firstKey).vertexNum, theGraph.get(firstKey).color);
        
            // For all other vertices color the vertex a random color
            while (itr.hasNext())
            {
                int nextKey = (int)itr.next();
                Vertex currentVertex = theGraph.get(nextKey);
                currentVertex.color = rand.nextInt(maxColor-1) + 1;

                System.out.format("Gave vertex %d color %d.%n", currentVertex.vertexNum, currentVertex.color);
            }    
            graph.printGraph();
            
            /**
             * Go through all nodes with conflicts and resolve them
             */
            // start up new iterator
            itr = graph.theGraph.keySet().iterator();
            while (itr.hasNext())
            {
                int nextKey = (int)itr.next();
                Vertex currentVertex = theGraph.get(nextKey);
                if (Conflicts(nextKey) > 0)
                {
                    resolveConflicts(nextKey);
                }
                System.out.format("Gave vertex %d color %d.%n", currentVertex.vertexNum, currentVertex.color);
            }    
            step++;
            
            
        }
        // if not solved by now, stop
        System.out.println("minConflicts finished");
    }
    
    /**
     * Check if problem solved yet
     */
    private boolean checkSolution() 
    {
        /**
         * Check if random assignment is a solution
         */
        boolean checkSolution = checkIfSolution();
        if (checkSolution == false)
        {
            System.out.println("Not a solution");
            return false;
        }
        else if (checkSolution == true)
        {
            System.out.println("It's a solution");
            return true;
        }
        else
            System.out.println("checkIfSolution() seems to have failed...");

        return false;
    }
    
    /**
    * Check if the assignment is a solution 
    */
    private boolean checkIfSolution() 
    {
        boolean check = false; // flag to indicate if solution or not
        
        // start up new iterator
        Iterator itr = graph.theGraph.keySet().iterator();
            
        // iterate through all the vertices
        while (itr.hasNext())
        {
            int nextKey = (int)itr.next();
            Vertex currentVertex = theGraph.get(nextKey);

            for (Iterator<Vertex> it = currentVertex.edges.values().iterator(); it.hasNext();) 
            {
                Vertex connectedVertex = it.next();
                if (currentVertex.color == connectedVertex.color)
                {
                    return false;
                }
            }
        }
        return true;
    }
    
    
    /**
     * Count the number of conflicts
     * @return the number of conflicts  
     */
    private int Conflicts(int curKey) 
    {
        int conflicts = 0;
        // start up new iterator
        Iterator itr = graph.theGraph.keySet().iterator();
            
        Vertex currentVertex = theGraph.get(curKey);

        for (Iterator<Vertex> it = currentVertex.edges.values().iterator(); it.hasNext();) 
        {
            Vertex connectedVertex = it.next();
            if (currentVertex.color == connectedVertex.color)
            {
                conflicts++;
            }
        }
        System.out.println("Key " + curKey + " has " + conflicts + " conflicts");
        return conflicts;
    }
    
    /**
     * Resolves as many conflicts of given node as possible by iterating
     * over conflicting variables and choosing the color which causes the 
     * least number of conflicts
     * @param curKey 
     */
    private void resolveConflicts(int curKey)
    {
        int bestColor = 1;
        int leastConflicts = theGraph.size(); // assume it conflicts with every node, just to start with something
        // loop through the colors
        for(int curColor = 1; curColor < maxColor + 1; curColor++)
        {
            System.out.println("Key " + curKey + " with color " + curColor);
            theGraph.get(curKey).color = curColor;
            if(Conflicts(curKey) < leastConflicts)
            {
                leastConflicts = Conflicts(curKey);
                bestColor = curColor;
            }
        }
        theGraph.get(curKey).color = bestColor;
        graph.printGraph();
    }

    
}
