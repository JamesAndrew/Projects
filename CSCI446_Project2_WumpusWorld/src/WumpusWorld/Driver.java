package WumpusWorld;

/**
 *
 * @version 09/27/16
 */
public class Driver 
{

    /**
     *
     * @param args
     */
    public static void main(String[] args)
    { 
        System.out.format("=== Wumpus World Simulation Initialized ===%n%n");
        
        World theWorld = new World(2, 0.5f, 0.5f, 0.5f);
        
        ReactiveAgent simple = new ReactiveAgent(theWorld);
        
        
        System.out.format("%n%n=== Simulation Concluded ===");
    }
}
