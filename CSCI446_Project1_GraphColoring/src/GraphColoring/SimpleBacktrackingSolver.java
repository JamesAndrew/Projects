package GraphColoring;

import java.util.Map;

/**
 * Graph coloring constraint solver using the basic backtracking strategy
 *
 * @version 09/08/16
 */
public class SimpleBacktrackingSolver extends ConstraintSolver {

    private int numPoints;

    public SimpleBacktrackingSolver() {

    }

    @Override
    public void runSolver() {
        runs.println();
        
        numPoints = graph.getGraphSize();
        decisionsMade = 0;
       backtrack(0);                                                            addDecision();
        
        runs.print("\nRun finished.");
        graph.printGraph();
        
        runs.println("=== Program Output Values ===");
        runs.format("Valid Coloring: %b, with %d out of %d nodes correctly colored.%n",
                graphSatisfiesConstraint(), graph.calculateFitness(), graph.getGraphSize());
        runs.format("Decisions Made: %d", decisionsMade);
    }

    /**
     * Recursive backtracking algorithm to traverse graph
     *
     * @param point
     * @return true if path found, false if not
     */
    private boolean backtrack(int point) {
        //iterate through all possible colors
        for (int color = 0; color < maxColors; color++) {           
            theGraph.get(point).color = color;                                  addDecision();
            runs.format("Node %d given color %d%n", point, color);
            if (pointSatisfiesConstraint(point)) {                              addDecision();
                if (allAdjacentColored(point)) {                                addDecision();
                    runs.println("All surrounding nodes colored.");
                    return true;                                                
                } else {
                    //iterate through all edges 
                    for (int i = 0; i < numPoints; i++) {
                        //move to uncolored edge nodes
                        if (theGraph.get(point).edges.containsKey(i) 
                                && theGraph.get(i).color == -1) {               addDecision();
                            //return true if end found else re color current node
                            if (backtrack(i) && allNodesColored()) {            addDecision();
                                runs.println("Solution found");
                                return true;
                            } else {
                                break;
                            }
                        }
                    }
                }
            }
            runs.println("Backtracking...\n");
        }
        theGraph.get(point).color = -1;
        return false;
    }

    /**
     * Check that all adjacent points are colored
     *
     * @param point
     * @return
     */
    private boolean allAdjacentColored(int point) {
        for (int i = 0; i < numPoints; i++) {
            if (theGraph.get(point).edges.containsKey(i) 
                    && theGraph.get(i).color == -1) {                           addDecision();
                return false;
            }
        }
        return true;
    }

    /**
     * Check that all nodes have been colored
     *
     * @return
     */
    private boolean allNodesColored() {
        for (Map.Entry<Integer, Vertex> entry : theGraph.entrySet()) {
            if (entry.getValue().color == -1) {                                 addDecision();
                return false;
            }
        }
        return true;
    }
    
    private void addDecision()
    {
        decisionsMade++;
    }
}
