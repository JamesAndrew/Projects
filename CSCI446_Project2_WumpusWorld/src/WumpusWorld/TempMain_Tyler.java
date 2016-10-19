package WumpusWorld;

import java.util.ArrayList;

public class TempMain_Tyler 
{
    public static void main(String[] args)
    { 
        World world = new World(5);
        world.getRoom(0, 0).setToSafe();
        world.getRoom(0, 1).setToSafe();
        world.getRoom(0, 2).setToSafe();
        world.getRoom(0, 3).setToSafe();
        world.getRoom(1, 2).setToSafe();
        world.getRoom(2, 2).setToSafe();
        world.getRoom(3, 2).setToSafe();
        world.getRoom(2, 1).setToSafe();
        
        Room start = world.getRoom(0, 0);
        Room finish = world.getRoom(2, 1);
        
        KnowledgeBasedAgent kba = new KnowledgeBasedAgent(world); 
        ArrayList<Room> path = kba.getPath(start, finish);
        
        for (Room r : path)
        {
            System.out.println(r.getRoomRow() + ", " + r.getRoomColumn());
        }
        kba.takePath(path);
    }
}
