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
        Cell test = new Cell();
        test.randomCell();
        
        System.out.println("=== Wumpus World Simulation Initialized ===");
        Agent simple = new Agent(test);
        System.out.println("=== Simulation Concluded ===");
    }
}
