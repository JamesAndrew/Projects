package GraphColoring;

/**
 * Refactor most methods in TempGraph into this method once we decide the
 * approach is good
 * @author David Rice
 * @version 09/12/16
 */
public class Temp_Graph_Generator 
{
    private final int graphSize;
    
    public Temp_Graph_Generator(int n)
    {
        graphSize = n;
    }
    
    public TempGraph generateGraph()
    {
        return new TempGraph(graphSize);
    }
}
