package WumpusWorld;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KBAgent 
{
    private Integer[] currentRoom = new Integer[]{0,0};
    // current direction agent is facing. options are "west", "north", "east", and "south"
    private String currentDirection = "east";
    // arrow stock
    private int arrows = World.getNumArrows();
    // flips to true once gold is found or no safe room exists
    private boolean endState = false;
    // mapping of frontier - cells adjacent to explored cells that are potentials to go to next
    private Map<Integer, Room> frontier = new HashMap<>();
    // Statistics and points tracking
    private AgentStatistics stats = new AgentStatistics();
    private Integer score = 0;
    // the knowledge base used for queries. is updated by the agent
    private KnowledgeBase kb;
    
    public KBAgent()
    {
        kb = new KnowledgeBase();
        // initialize frontier with the cell above and to the right (0,1) and (1,0)
        frontier.put(01, World.getRoom(0, 1));
        frontier.put(10, World.getRoom(1, 0));
    }

    public void findGold() 
    {
        while (!endState)
//        for (int i = 0; i < 6; i++) // temp forced loop while testing
        {
            System.out.format("\nCurrent Room: (%d, %d)%n", currentRoom[0], currentRoom[1]);
            
            // update kb about current room
            ArrayList<KBAtom> roomPercepts = perceiveRoom(currentRoom);
            for (KBAtom atom : roomPercepts) kb.update(atom);
            printKbPercepts();
            if (endState) break;
            
            // ask where to go next or what action to take
            int[] action = kb.requestAction(currentRoom[0], currentRoom[1], frontier);
            if (!endState) performAction(action);
        }
        
        stats.addReactiveStats(World.getSize(), score);
        System.out.format("%n=== Final Score: %d ===%n", score);
        System.out.println("=== Simulation Concluded ===");
    }

    /**
     * Use KBAction to pick up gold, shoot, or move
     * @param actionEnum 
     * 
     * 1: move to a new cell
     * 2: shoot arrow
     * 3: exit dungeon (no safe rooms to move to)
     * 
     * index 0 is the action, index 1 and 2 is the row and column to move to
     */
    private void performAction(int[] action)
    {
        switch(action[0])
        {
            case 1:
                Integer frontierKey = Integer.valueOf(String.valueOf(action[1]) + String.valueOf(action[2]));
                int distance = Math.abs(action[1] - currentRoom[0]) + Math.abs(action[2] - currentRoom[1]);
                currentRoom[0] = action[1];
                currentRoom[1] = action[2];
                updateFrontier(frontierKey);
                System.out.format("Moved to room (%d, %d)%n", currentRoom[0], currentRoom[1]);
//                printFrontier();
                score -= distance;
                System.out.println("Score: " + score);
                break;
            case 2:
                arrows--;
                break;
            case 3:
                endState = true;
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
    private ArrayList<KBAtom> perceiveRoom(Integer[] currentRoom) 
    {
//        System.out.format("current room attributes: %n");
        
        int row = currentRoom[0];
        int column = currentRoom[1];
        
        // curent room is now explored
        World.getRoom(row, column).setIsExplored(true);
        
        // for any known property about the current room, add those perceptions 
        ArrayList<KBAtom> perceptions;
        perceptions = World.getRoom(row, column).returnRoomAttributes();
        // is the shiny percept happens, set flag to exit the program
        for (KBAtom atom : perceptions)
        {
            KBAtomConstant current = (KBAtomConstant) atom;
            if (current.predicate.equals("SHINY") && !current.negation)
            {
                System.out.println("=== Gold has been found. Picking up gold and exiting. ===");
                score += 1000;
                System.out.println("Score: " + score);
                endState = true;
            }
        }
        
        return perceptions;
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

    private void updateFrontier(Integer newRoomKey) 
    {
        // remove room just moved in to from frontier
        frontier.remove(newRoomKey);
        
        // add adjacent rooms given they haven't already been explored and are in the bounds of the map
        ArrayList<Integer[]> adjRooms = new ArrayList<>();
        adjRooms.add(new Integer[]{currentRoom[0]-1, currentRoom[1]   });
        adjRooms.add(new Integer[]{currentRoom[0],   currentRoom[1]+1 });
        adjRooms.add(new Integer[]{currentRoom[0]+1, currentRoom[1]   });
        adjRooms.add(new Integer[]{currentRoom[0],   currentRoom[1]-1 });
        
        for (Integer[] entry : adjRooms)
        {
            if      (entry[0] < 0 || entry[0] >= World.getSize()) { } // do nothing
            else if (entry[1] < 0 || entry[1] >= World.getSize()) { } // do nothing
            else
            {
                Room currentRoom = World.getRoom(entry[0], entry[1]);
                Integer frontierKey = Integer.valueOf(String.valueOf(entry[0]) + String.valueOf(entry[1]));
                if (!(currentRoom.isIsExplored())) frontier.put(frontierKey, currentRoom);
            }
        }
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
    
    private void printFrontier()
    {
        System.out.println("Updated frontier: ");
        for (Integer key : frontier.keySet())
        {
            System.out.format("(%d, %d)%n", frontier.get(key).getRoomRow(), frontier.get(key).getRoomColumn());
        }
    }
}
