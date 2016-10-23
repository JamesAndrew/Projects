package WumpusWorld;

public class TempMain_David 
{
    public static void main(String[] args)
    { 
        /*
        * First simple approach: test resolution on a simple 3x3 ActualWorld with
        * hand-placed wumpus, smells, and gold
        */
        ActualWorld aWorld = new ActualWorld(3);
        
        // hardcode actual-world state
        ActualWorld.getRoom(2,0).setIsWumpus(true);
        ActualWorld.getRoom(1,0).setIsSmelly(true);
        ActualWorld.getRoom(2,1).setIsSmelly(true);
        ActualWorld.getRoom(2,2).setIsShiny(true);
        ActualWorld.getRoom(2,2).setHasGold(true);
        
        // set agent and begin routine
        KBAgent agent = new KBAgent();
        agent.findGold();
    }
}
