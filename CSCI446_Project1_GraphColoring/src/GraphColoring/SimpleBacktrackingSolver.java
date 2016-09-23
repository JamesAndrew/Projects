package GraphColoring;

import java.util.Map;
import java.util.Scanner;

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
        // create Scanner object to take input from user
        Scanner input = new Scanner(System.in);
        runs.println("simpleBacktracking algorithm");
        numPoints = graph.getGraphSize();
        verticesVisited = 0;
        if (backtrack(0)) {
            validColorings++;
        }
        System.out.format("Nodes colored: %d%n", verticesVisited);
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
            decisionsMade++;
            theGraph.get(point).color = color;
            verticesVisited++;
            runs.format("node %d given color %d%n", point, color);
            if (pointSatisfiesConstraint(point)) {
                if (allAdjacentColored(point)) {
                    runs.println("All surrounding nodes colored");
                    return true;
                } else {
                    //iterate through all edges 
                    for (int i = 0; i < numPoints; i++) {
                        decisionsMade++;
                        //move to uncolored edge nodes
                        if (theGraph.get(point).edges.containsKey(i) && theGraph.get(i).color == -1) {
                            //return true if end found else re color current node
                            if (backtrack(i) && allNodesColored()) {
                                runs.println("Solution found");
                                return true;
                            } else {
                                break;
                            }
                        }
                    }
                }
            }
            runs.println("Backtracking");
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
            decisionsMade++;
            if (theGraph.get(point).edges.containsKey(i) && theGraph.get(i).color == -1) {
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
            decisionsMade++;
            if (entry.getValue().color == -1) {
                return false;
            }
        }
        return true;
    }
}
