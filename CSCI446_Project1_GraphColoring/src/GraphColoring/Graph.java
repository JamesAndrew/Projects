package GraphColoring;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

// class Graph represents a graph object with attributes points and matrix 
public class Graph {
    public final Map<Integer, Vertex> theGraph;
    
    // constructor to initialize Graph class and its attributes
    public Graph(Map<Integer, Vertex> theGraph) 
    {
     this.theGraph = theGraph;    
    }
}
