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
        Room test = new Room();
        test.randomRoom();
        
        System.out.println("=== Wumpus World Simulation Initialized ===");
        ReactiveAgent simple = new ReactiveAgent(test);
        System.out.println("=== Simulation Concluded ===");
    }
}
