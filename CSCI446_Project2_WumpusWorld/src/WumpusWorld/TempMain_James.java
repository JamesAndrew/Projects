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
        World aWorld = new World(5);
        World.printWorld();
        
        // set agent and begin routine
        ReactiveAgent simple = new ReactiveAgent(aWorld, 1000);
                
        AgentStatistics statistics = new AgentStatistics();
        statistics.printResults();
    }
}