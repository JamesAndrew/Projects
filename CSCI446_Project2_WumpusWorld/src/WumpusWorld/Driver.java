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
        World theWorld = new World(2);
        
        System.out.println("=== Wumpus World Simulation Initialized ===");
        ReactiveAgent simple = new ReactiveAgent(0, 0, theWorld);
        System.out.println("=== Simulation Concluded ===");
    }
}
