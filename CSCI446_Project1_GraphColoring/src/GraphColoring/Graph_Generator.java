package GraphColoring;

/**
 * Refactor most methods in Graph into this method once we decide the
 approach is good
 * @author David Rice
 * @version 09/12/16
 */
public class Graph_Generator 
{
    private final int size;
    
    public Graph_Generator(int size)
    {
        this.size = size;
    }
    
    public Graph generateGraph()
    {
        Graph graph = new Graph(size);
        
        return graph;
    }
}
