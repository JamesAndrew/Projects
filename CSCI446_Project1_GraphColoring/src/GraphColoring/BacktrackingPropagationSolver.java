package GraphColoring;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

/**
 * Graph coloring constraint solver using backtracking with forward checking
 *
 * @version 09/08/16
 */
public class BacktrackingPropagationSolver extends ConstraintSolver
{
    private int numPoints;
    int validColorings = 0;

    public BacktrackingPropagationSolver()
    {

    }

    @Override
    public void runSolver()
    {
        numPoints = graph.getGraphSize();
        decisionsMade = 0;

        //set up lists to keep track of valid colorings for each node 
        ArrayList<ArrayList<Integer>> valColorings = new ArrayList<>();
        for (int i = 0; i < numPoints; i++)
        {
            valColorings.add(new ArrayList<>());
            for (int j = 0; j < maxColors; j++)
            {
                valColorings.get(i).add(j);                                     addDecision();
            }
        }

        if(backtrack((0), valColorings))
        {
            validColorings++; 
        }
        
        runs.print("\nRun finished.");
        graph.printGraph();
        
        runs.println("=== Program Output Values ===");
        runs.format("Valid Coloring: %b, with %d out of %d nodes correctly colored.%n",
                graphSatisfiesConstraint(), graph.calculateFitness(), graph.getGraphSize());
        runs.format("Decisions Made: %d", decisionsMade);
    }

     /**
     * Recursive backtracking algorithm to traverse graph 
     * @param point
     * @return true if path found, false if not
     */
    private boolean backtrack(int point, ArrayList<ArrayList<Integer>> valColors)
    {
        //iterate through current list of valid node colorings 
        ArrayList<ArrayList<Integer>> valColorings = new ArrayList<>(valColors);
        for (int i = 0; i < numPoints; i++)
        {
            valColorings.add(new ArrayList<>(valColors.get(i)));         addDecision();
        }
//        for (int adf : validColorings.get(point)){
//            System.out.println(adf);
//        }
        for (Iterator<Integer> iterator = valColorings.get(point).iterator(); iterator.hasNext();)
        {
            int color = theGraph.get(point).color = iterator.next();            addDecision();
            runs.format("%nNode %d given color %d%n", point, color);
            if (pointSatisfiesConstraint(point) && propagate(point, valColorings))
            {
                if (allAdjacentColored(point))
                {
                    return true;
                } else
                {
                    for (int i = 0; i < numPoints; i++)
                    {                                                           addDecision();
                        if (theGraph.get(point).edges.containsKey(i)    
                                && theGraph.get(i).color == -1)
                        {
                                                                                addDecision();
                            //return true if end found else re color current node
                            if (backtrack(i, valColorings) && allNodesColored())
                            {
                                runs.println("Solution found");
                                return true;
                            }
                            else 
                            {
                                break;
                            }
                        }
                    }
                }
            }
            runs.println("Backtracking...");
        }
        theGraph.get(point).color = -1;                                         addDecision();
        return false;
    }

     /**
     * Check that all adjacent points are colored 
     * @param point
     * @return 
     */
    private boolean allAdjacentColored(int point)
    {
        for (int i = 0; i < numPoints; i++)
        {                                                                       addDecision();
            if (theGraph.get(point).edges.containsKey(i) && theGraph.get(i).color == -1)
            {
                return false;
            }
        }
        return true;
    }

     /**
     * Check that all nodes have been colored 
     * @return 
     */
    private boolean allNodesColored()
    {
        for (Map.Entry<Integer, Vertex> entry : theGraph.entrySet())
        {                                                                       addDecision();
            if (entry.getValue().color == -1)
            {
                return false;
            }
        }
        return true;
    }

    /**
     * move through graph and set valid colorings relative to surrounding nodes
     * @param node
     * @param validColorings
     * @return 
     */
    private boolean propagate(int node, ArrayList<ArrayList<Integer>> validColorings)
    {
        //iterate through all edges
        Map<Integer, Vertex> edges = theGraph.get(node).edges;
        for (Map.Entry<Integer, Vertex> entry : edges.entrySet())
        {
            int edgePoint = entry.getKey();                                     addDecision();
            if (theGraph.get(edgePoint).color == -1) 
            {
                //current node has only 1 valid coloring. remove from edges
                if (theGraph.get(node).color == -1 && validColorings.get(node).size() == 1)
                {
                    runs.format("removing color %d from node %d's valid colors%n", validColorings.get(node).get(0), edgePoint);
                    validColorings.get(edgePoint).remove(validColorings.get(node).get(0));
                }
                else 
                {
                    //remove current node color from edges
                    runs.format("removing color %d from node %d's valid colors%n", theGraph.get(node).color, edgePoint);
                     validColorings.get(edgePoint).remove((Integer) theGraph.get(node).color); 
                }
                if (validColorings.get(edgePoint).isEmpty()) 
                {
                    runs.println("Invalid future coloring found");
                    //reached point with no valid coloring
                    return false; 
                }
                if (validColorings.get(edgePoint).size() == 1) 
                {
                    //do not move from node with one valid coloring to node with one different valid coloring
                    if (theGraph.get(node).color == -1 && validColorings.get(node).size() == 1 && validColorings.get(edgePoint).get(0) != validColorings.get(node).get(0))
                    {
                        
                    }
                    else
                    {
                        return propagate(edgePoint, validColorings);
                    } 
                }
            }
        }
        return true;
    }
    
    private void addDecision()
    {
        decisionsMade++;
    }
}
