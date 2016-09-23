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
    private int maxColor = 4;
    private int steps = 10;
    
    public MinConflictsSolver()
    {
        
    }
    
    @Override
    public void runSolver()
    {
        // create Scanner object to take input from user
        Scanner input = new Scanner(System.in);
        // create Random object to generate random numbers
        Random rand = new Random();
        
        runs.println("Tunable parameter settings:");
        runs.println("Number of allowed steps: " + steps);
        runs.println();
        
        // create iterator to loop through nodes
        Iterator itr = graph.theGraph.keySet().iterator();
        //runs.println("minConflicts algorithm");
        

        // start up new iterator
        itr = graph.theGraph.keySet().iterator();

        /**
         * Create a random complete assignment, not necessarily correct
         */
        // Color first vertex with a random color
        int firstKey = (int)itr.next();
        theGraph.get(firstKey).color = rand.nextInt(maxColor-1) + 1;
        runs.format("Gave vertex %d color %d.%n", theGraph.get(firstKey).getVertexNum(), theGraph.get(firstKey).color);
        
        decisionsMade++;
        
        // For all other vertices color the vertex a random color
        while (itr.hasNext())
        {
            int nextKey = (int)itr.next();
            Vertex currentVertex = theGraph.get(nextKey);
            currentVertex.color = rand.nextInt(maxColor-1) + 1;

            runs.format("Gave vertex %d color %d.%n", currentVertex.getVertexNum(), currentVertex.color);
            decisionsMade++;
        }    
        //graph.printGraph();
        
        /**
         * keeping trying solutions till a solution has been found or 
         * the specified number of steps
         */
        int step = 0;
        while (step < steps)
        {
            runs.println("step " + step);
            /**
             * Go through all nodes with conflicts and resolve them
             */
            // start up new iterator
            itr = graph.theGraph.keySet().iterator();
            while (itr.hasNext())
            { 
                int nextKey = (int)itr.next();
                //runs.println("Node " + nextKey);
                Vertex currentVertex = theGraph.get(nextKey);
                if (Conflicts(nextKey) > 0)
                {
                    resolveConflicts(nextKey);
                }
                runs.format("Gave vertex %d color %d.%n", currentVertex.getVertexNum(), currentVertex.color);
                decisionsMade++;
            }    
            step++;
            
            decisionsMade++;
            if (checkSolution() == true)
            {
                step = steps;
            }
            runs.println();
        }
        // if not solved by now, stop
        runs.println("minConflicts finished");
        runs.println();
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
            runs.println();
            runs.println("Not a solution");
            return false;
        }
        else if (checkSolution == true)
        {
            runs.println();
            runs.println("It's a solution");
            validColorings++;
            return true;
        }
        else
        {
            runs.println();
            runs.println("checkIfSolution() seems to have failed...");
        }
            
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
                decisionsMade++;
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
            //runs.println("Connected Node " + connectedVertex.getVertexNum());
            decisionsMade++;
            if (currentVertex.color == connectedVertex.color)
            {
                conflicts++;
            }
        }
        runs.println("Node " + curKey + " has least number of conflicts at " + conflicts + " conflicts");
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
            runs.print(" Node " + curKey + " with color " + curColor);
            runs.println();
            theGraph.get(curKey).color = curColor;
            decisionsMade++;
            if(Conflicts(curKey) < leastConflicts)
            {
                leastConflicts = Conflicts(curKey);
                bestColor = curColor;
            }
        }
        theGraph.get(curKey).color = bestColor;
        //graph.printGraph();
    }

    
}
