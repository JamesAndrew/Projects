package WumpusWorld;

/**
 *
 * @version 09/27/16
 */
public class TempMain_James 
{

    /**
     *
     * @param args
     */
    public static void main(String[] args)
    { 
        System.out.format("=== Wumpus World Simulation Initialized ===%n%n");
        World theWorld = new World(3, 0.2f, 0.2f, 0.2f);
        ReactiveAgent simple = new ReactiveAgent(theWorld, 1000);
        System.out.format("%n%n=== Simulation Concluded ===");
        
        int mapSize = 5;
        for(int i = 0; i < 5; i++)
        {
            theWorld = new World(mapSize, 0.2f, 0.2f, 0.2f);
            simple = new ReactiveAgent(theWorld, 1000);
            mapSize += 5;
        }
        AgentStatistics statistics = new AgentStatistics();
        statistics.printResults();
    }
}