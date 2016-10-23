package WumpusWorld;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KBAgent 
{
    private int[] currentRoom = new int[]{0,0};
    // current direction agent is facing. options are "west", "north", "east", and "south"
    private String currentDirection = "east";
    // arrow stock
    private int arrows = 1;
    // flips to true once gold is found or no safe room exists
    private boolean endState = false;
    // mapping of frontier - cells adjacent to explored cells that are potentials to go to next
    Map<int[], Room> frontier = new HashMap<>();
    // the knowledge base used for queries. is updated by the agent
    private KnowledgeBase kb;
    
    public KBAgent()
    {
        kb = new KnowledgeBase();
        // initialize frontier with the cell above and to the right (0,1) and (1,0)
        frontier.put(new int[]{0,1}, ActualWorld.getRoom(0, 1));
        frontier.put(new int[]{1,0}, ActualWorld.getRoom(1, 0));
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
            int[] action = kb.requestAction(currentRoom[0], currentRoom[1]);
            if (!endState) performAction(action);
            
            
            
            
            KBAtomConstant queryAtom1 = new KBAtomConstant(false, "SAFE", ActualWorld.getRoom(currentRoom[0] + 1, currentRoom[1]));
            System.out.println("Query result: " + kb.query(queryAtom1));
            if (kb.query(queryAtom1)) 
            {
                System.out.println("Moving to next room");
                currentRoom[0] = 1;
            }
        }
    }

    /**
     * Use KBAction to pick up gold, shoot, or move
     * @param actionEnum 
     * 
     * 1: move to a new cell
     * 2: shoot arrow
     * 
     * index 0 is the action, index 1 and 2 is the row and column to move to
     */
    private void performAction(int[] action)
    {
        switch(action[0])
        {
            case 1:
                // change this later to actually do safe path traversal
                int[] newRoom = new int[]{action[1], action[2]};
                currentRoom[0] = newRoom[0];
                currentRoom[1] = newRoom[1];
                updateFrontier(newRoom);
                break;
            case 2:
                arrows--;
                break;
            default:
                throw new RuntimeException("The provided action integer does not match to the switch statement in KBAgent");
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
        // is the shiny percept happens, set flag to exit the program
        for (KBAtom atom : perceptions)
        {
            KBAtomConstant current = (KBAtomConstant) atom;
            if (current.predicate.equals("SHINY") && !current.negation) endState = true;
        }
        
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
    
    private void turnCCW()
    {
        Map<String, String> ccwMapping = new HashMap<>();
        ccwMapping.put("north", "west");
        ccwMapping.put("east", "north");
        ccwMapping.put("south", "east");
        ccwMapping.put("west", "south");
        
        this.currentDirection = ccwMapping.get(currentDirection);
    }
    
    private void turnCW()
    {
        Map<String, String> ccwMapping = new HashMap<>();
        ccwMapping.put("north", "east");
        ccwMapping.put("east", "south");
        ccwMapping.put("south", "west");
        ccwMapping.put("west", "north");
        
        this.currentDirection = ccwMapping.get(currentDirection);
    }

    private void updateFrontier(int[] newRoom) 
    {
        // remove room just moved in to from frontier
        frontier.remove(newRoom);
        
        // add adjacent rooms given they haven't already been explored and are in the bounds of the map
        ArrayList<int[]> adjRooms = new ArrayList<>();
        adjRooms.add(new int[]{newRoom[0]-1, newRoom[1]   });
        adjRooms.add(new int[]{newRoom[0],   newRoom[1]+1 });
        adjRooms.add(new int[]{newRoom[0]+1, newRoom[1]   });
        adjRooms.add(new int[]{newRoom[0],   newRoom[1]-1 });
        
        for (int[] entry : adjRooms)
        {
            if      (entry[0] < 0 || entry[0] >= ActualWorld.getSize()) { } // do nothing
            else if (entry[1] < 0 || entry[1] >= ActualWorld.getSize()) { } // do nothing
            else
            {
                Room currentRoom = ActualWorld.getRoom(entry[0], entry[1]);
                if (!(currentRoom.isIsExplored())) frontier.put(entry, currentRoom);
            }
        }
    }
}
