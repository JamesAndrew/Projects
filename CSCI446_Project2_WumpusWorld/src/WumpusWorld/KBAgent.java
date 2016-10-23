package WumpusWorld;

import java.util.ArrayList;
import java.util.List;

public class KBAgent 
{
    private int[] currentRoom = new int[]{0,0};
    // current direction agent is facing. options are "west", "north", "east", and "south"
    private String currentDirection = "east";
    // flips to true once gold is found or no safe room exists
    private boolean endState = false;
    // the knowledge base used for queries. is updated by the agent
    private KnowledgeBase kb;
    
    public KBAgent()
    {
        kb = new KnowledgeBase();
    }

    public void findGold() 
    {
//        while (!endState)
        for (int i = 0; i < 1; i++) // temp forced loop while testing
        {
            System.out.format("\nCurrent Room: (%d, %d)%n", currentRoom[0], currentRoom[1]);
            // update kb about current room
            ArrayList<KBAtom> roomPercepts = perceiveRoom(currentRoom);
            for (KBAtom atom : roomPercepts) kb.update(atom);
            printKbPercepts();

            // ask where to go next or what action to take
            KBAtomConstant queryAtom1 = new KBAtomConstant(false, "SAFE", ActualWorld.getRoom(currentRoom[0] + 1, currentRoom[1]));
            System.out.println("Query result: " + kb.query(queryAtom1));
            if (kb.query(queryAtom1)) 
            {
                System.out.println("Moving to next room");
                currentRoom[0] = 1;
            }
            
            System.out.format("\nCurrent Room: (%d, %d)%n", currentRoom[0], currentRoom[1]);
            // update kb about current room
            ArrayList<KBAtom> roomPercepts2 = perceiveRoom(currentRoom);
            for (KBAtom atom : roomPercepts2) kb.update(atom);
            printKbPercepts();
            // ask where to go next or what action to take
            KBAtomConstant queryAtom2 = new KBAtomConstant(false, "SAFE", ActualWorld.getRoom(2, 0));
            System.out.println("Query result: " + kb.query(queryAtom2));
            if (kb.query(queryAtom2)) 
            {
                System.out.println("No");
            }
            else
            {
                System.out.println("Moving to next room");
                currentRoom[1] = 1;
            }
            
            System.out.format("\nCurrent Room: (%d, %d)%n", currentRoom[0], currentRoom[1]);
            // update kb about current room
            ArrayList<KBAtom> roomPercepts3 = perceiveRoom(currentRoom);
            for (KBAtom atom : roomPercepts3) kb.update(atom);
            printKbPercepts();
            // ask where to go next or what action to take
            KBAtomConstant queryAtom3 = new KBAtomConstant(false, "SAFE", ActualWorld.getRoom(1, 2));
            System.out.println("Query result: " + kb.query(queryAtom3));
            if (kb.query(queryAtom3)) 
            {
                System.out.println("Moving to next room");
                currentRoom[1] = 2;
            }
            
            System.out.format("\nCurrent Room: (%d, %d)%n", currentRoom[0], currentRoom[1]);
            // update kb about current room
            ArrayList<KBAtom> roomPercepts4 = perceiveRoom(currentRoom);
            for (KBAtom atom : roomPercepts4) kb.update(atom);
            printKbPercepts();
            // ask where to go next or what action to take
            KBAtomConstant queryAtom4 = new KBAtomConstant(false, "SAFE", ActualWorld.getRoom(2, 2));
            System.out.println("Query result: " + kb.query(queryAtom4));
            if (kb.query(queryAtom4)) 
            {
                System.out.println("Moving to next room");
                currentRoom[0] = 2;
            }
            
            System.out.format("\nCurrent Room: (%d, %d)%n", currentRoom[0], currentRoom[1]);
            // update kb about current room
            ArrayList<KBAtom> roomPercepts5 = perceiveRoom(currentRoom);
            for (KBAtom atom : roomPercepts5) kb.update(atom);
            printKbPercepts();
        }
    }

    /**
     * Query the actual world for the current room.
     * Return properties that are set as true. Transform into atomic statement
     * @param currentRoom
     * @return 
     */
    private ArrayList<KBAtom> perceiveRoom(int[] currentRoom) 
    {
//        System.out.format("current room attributes: %n");
        
        int row = currentRoom[0];
        int column = currentRoom[1];
        
        ArrayList<KBAtom> perceptions = new ArrayList<>();
        
        // curent room is now explored
        ActualWorld.getRoom(row, column).setIsExplored(true);
        // for any known property about the current room, add those perceptions 
        perceptions = ActualWorld.getRoom(row, column).returnRoomAttributes();
        
        return perceptions;
    }
    
    private void printKbPercepts()
    {
        List<KBcnf> percepts = kb.getKb_cnf();
        
        System.out.println("Knowledge base percepts: ");
        for (int i = 0; i < percepts.size(); i++)
        {
            System.out.format("%s, ", percepts.get(i).toString());
            if ((i+1) % 5 == 0) System.out.println();
        }
        System.out.println();
    }
}
