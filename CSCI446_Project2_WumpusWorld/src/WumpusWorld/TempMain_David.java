package WumpusWorld;

public class TempMain_David 
{
    public static void main(String[] args)
    { 
        World aWorld = new World(15);
        World.printWorld();
        
        // set agent and begin routine
        KBAgent agent = new KBAgent();
        agent.findGold();
        
        AgentStatistics statistics = new AgentStatistics();
        statistics.printResults();
    }
}
