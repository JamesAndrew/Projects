package WumpusWorld;

public class TempMain_David 
{
    public static void main(String[] args)
    { 
        World aWorld = new World(3);
        
        // hardcode actual-world state
        // Wumpi:
        World.getRoom(2,1).setIsWumpus(true);
        World.getRoom(4,1).setIsSmelly(true);
//        World.getRoom(1,4).setIsSmelly(true);
        
        // Smells:
        World.getRoom(1, 1).setIsSmelly(true);
        World.getRoom(2, 0).setIsSmelly(true);
        World.getRoom(3, 1).setIsSmelly(true);
        World.getRoom(2, 2).setIsSmelly(true);
        
//        World.getRoom(0, 4).setIsSmelly(true);
//        World.getRoom(1, 3).setIsSmelly(true);
//        World.getRoom(2, 4).setIsSmelly(true);
        
        World.getRoom(4, 0).setIsSmelly(true);
        World.getRoom(3, 1).setIsSmelly(true);
        World.getRoom(4, 2).setIsSmelly(true);
        
        
        // Gold:
        World.getRoom(8,8).setIsShiny(true);
        World.getRoom(8,8).setHasGold(true);
        
        // set agent and begin routine
        KBAgent agent = new KBAgent();
        agent.findGold();
    }
}
