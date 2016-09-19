package GraphColoring;

import java.util.Iterator;
import java.util.Map;

/**
 * This strategy is not needed but is used as an initial approach to get a sense
 * of how this project will work
 * @version 09/08/16
 */
public class GreedySolver extends ConstraintSolver
{
    private final Graph graph;
    private final Map<Integer, Vertex> theGraph;
    
    public GreedySolver(Graph graph)
    {
        this.graph = graph;
        theGraph = graph.theGraph;
        greedyColoringStrategy();
    }
    
    private void greedyColoringStrategy()
    {
        Iterator itr = graph.theGraph.keySet().iterator();
        
        // Color first vertex with first color
        int firstKey = (int)itr.next();
        theGraph.get(firstKey).color = 1;
        System.out.format("Gave vertex %d color %d.%n", theGraph.get(firstKey).vertexNum, theGraph.get(firstKey).color);
        
        // For all other vertices color the vertex the lowest available color 
        //that is not used by any vertex it is connected to
        while (itr.hasNext())
        {
            int nextKey = (int)itr.next();
            Vertex currentVertex = theGraph.get(nextKey);
            currentVertex.color = 1;
            
            for (Iterator<Vertex> it = currentVertex.edges.values().iterator(); it.hasNext();) 
            {
                Vertex connectedVertex = it.next();
                if (currentVertex.color == connectedVertex.color)
                {
                    currentVertex.color++;
                    //System.out.format("Found adjacent node with same color. Increasing color to %d%n", currentVertex.color);
                    // restart the iterator
                    it = currentVertex.edges.values().iterator();
                }
            }
            
            System.out.format("Gave vertex %d color %d.%n", currentVertex.vertexNum, currentVertex.color);
        }
    }
}
