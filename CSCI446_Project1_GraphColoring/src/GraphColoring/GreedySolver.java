package GraphColoring;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map.Entry;

/**
 * This strategy is not needed but is used as an initial approach to get a sense
 * of how this program will work
 * @version 09/08/16
 */
public class GreedySolver extends ConstraintSolver
{
    private final TempGraph graph;
    private final ArrayList<Integer> usedColors = new ArrayList<>();
    private int nextUniqueColor = 1;
    
    public GreedySolver(TempGraph graph)
    {
        this.graph = graph;
        greedyColoringStrategy();
    }
    
    private void greedyColoringStrategy()
    {
        Iterator itr = graph.theGraph.entrySet().iterator();
        
        // Color first vertex with first color
        Entry firstEntry = (Entry) itr.next();
        Vertex firstVertex = (Vertex) firstEntry.getValue();
        
        firstVertex.setColor(nextUniqueColor);
        usedColors.add(nextUniqueColor);
        nextUniqueColor++;
        
        System.out.format("%nVertex number: %d, color: %d, location: (%f, %f), ", 
                firstVertex.vertexNum, firstVertex.getColor(), firstVertex.getxValue(), firstVertex.getyValue());
        
        System.out.print("connected to edges: ");
        graph.edges.get(firstVertex).stream().forEach((vertex) -> 
        {
            System.out.format("%d, ", vertex.vertexNum);
        });
        System.out.println();
        
        // For all other vertices...
        while (itr.hasNext())
        {
            Entry nextEntry = (Entry) itr.next();
            Vertex nextVertex = (Vertex) firstEntry.getValue();
            Integer nextVertexNumber = (Integer) nextEntry.getKey();
            
            // color the vertex the lowest available color that is not used by 
            // any vertex it is connected to //
            // for each connected vertex...
            
            
        }
    }
}
