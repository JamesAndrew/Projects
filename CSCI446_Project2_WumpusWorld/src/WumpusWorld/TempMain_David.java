package WumpusWorld;

public class TempMain_David 
{
    public static void main(String[] args)
    { 
        /*
        * Second simple approach: test resolution on a 5x5 ActualWorld with
        * hand-placed wumpus, smells, and gold
        */
        ActualWorld aWorld = new ActualWorld(20);
        
        // hardcode actual-world state
        // Wumpi:
        ActualWorld.getRoom(2,1).setIsWumpus(true);
        ActualWorld.getRoom(4,1).setIsSmelly(true);
//        ActualWorld.getRoom(1,4).setIsSmelly(true);
        
        // Smells:
        ActualWorld.getRoom(1, 1).setIsSmelly(true);
        ActualWorld.getRoom(2, 0).setIsSmelly(true);
        ActualWorld.getRoom(3, 1).setIsSmelly(true);
        ActualWorld.getRoom(2, 2).setIsSmelly(true);
        
//        ActualWorld.getRoom(0, 4).setIsSmelly(true);
//        ActualWorld.getRoom(1, 3).setIsSmelly(true);
//        ActualWorld.getRoom(2, 4).setIsSmelly(true);
        
        ActualWorld.getRoom(4, 0).setIsSmelly(true);
        ActualWorld.getRoom(3, 1).setIsSmelly(true);
        ActualWorld.getRoom(4, 2).setIsSmelly(true);
        
        
        // Gold:
        ActualWorld.getRoom(8,8).setIsShiny(true);
        ActualWorld.getRoom(8,8).setHasGold(true);
        
        // set agent and begin routine
        KBAgent agent = new KBAgent();
        agent.findGold();
    }
}
