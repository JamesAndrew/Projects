package WumpusWorld;

/**
 *
 * @version 09/28/16
 */
public final class ReactiveAgent 
{
    /**
     * Sensors
     */
    private boolean hitObstacle;
    private boolean feelBreeze;
    private boolean smellStench;
    private boolean heardScream;
    private boolean seeGlitter;
    private boolean alive; // flag for whether the agent program continues or not 
    private int direction; // 0 - left, 1 - up, 2 - right, 3 - down 
    
    /**
     * Resources
     */
    private int score;
    private int arrows;
    private int actions; // how many actions taken so far
    
    private Room currentRoom;
    private final World actualWorld;
    private World perceivedWorld;
    
    public ReactiveAgent(World w)
    {
        score = 0;
        actions = 0;
        
        actualWorld = w;
        perceivedWorld = new World(actualWorld.getSize());
        currentRoom = actualWorld.getStart();
        
        heardScream = false;
        alive = true;
        life();
    }
    
    public void life()
    {
        System.out.println("The agent has entered the cave.");
        System.out.println("The agent is in room " + currentRoom.getRoomRow() + " " + currentRoom.getRoomColumn());
        
        updateSensors();
        agentStatus();
        
        /**
         * Check for end states first
         */
        if (currentRoom.isPit() == true)
        {
            fall();
        }
        
        else if (currentRoom.isWumpus() == true)
        {
            fightWumpus();
        }
        
        else if (seeGlitter == true)
        {
            grab();
        }
        
        /**
         * else check out room
         */
        else
        {
            if (feelBreeze == true)
            {
                System.out.println("The agent feels a slight breeze");
            }
            
            if (smellStench == true)
            {
                System.out.println("The agent smells a horrible stench");
            }
            
            if (currentRoom.isEmpty() == true)
            {
                System.out.println("The room is empty");
            }
        }
        agentStatus();
    }
    
    private void updateSensors()
    {
        feelBreeze = currentRoom.isBreezy();
        smellStench = currentRoom.isBreezy();
        seeGlitter = currentRoom.isShiny();
    }
    
    private void agentStatus()
    {
        System.out.println("== Agent Status ==");
        System.out.println("Score: " + score);
        System.out.println("hitObstacle: " + hitObstacle);
        System.out.println("feelBreeze: " + feelBreeze);
        System.out.println("smellStench: " + smellStench);
        System.out.println("heardScream: " + heardScream);
        System.out.println("seeGlitter: " + seeGlitter);
        System.out.println("Direction: " + direction);
    }
    
    /**
     * Actuators
     */
    /* when the explorer dies, the score is decreased by a thousand and the 
    agent program terminates in failure */
    public void die()
    {
        System.out.println("The agent has died.");
        
        score -= 1000;
        alive = false;
    }
    
    public void fall()
    {
        System.out.println("The agent has fell down a bottomless pit.");
        die();
    }
    
    public void Forward()
    {
        System.out.println("The agent has moved forward.");
        
        countAction();
    }
    
    public void turnRight()
    {
        System.out.println("The agent has turned right 90 degrees");
        
        countAction();
        direction++;
        if (direction == 4)
        {
            direction = 0;
        }
    }
    
    public void turnLeft()
    {
        System.out.println("The agent has turned left 90 degrees");
        
        countAction();
        direction--;
        if (direction == -1)
        {
            direction = 3;
        }
    }
    
    public void shoot()
    {
        System.out.println("The agent has fired an arrow");
        
        countAction();
        arrows--;
        score -= 10;
        // if Wumpus hit
        
        // if missed
    }
    
    /* if the explorer finds gold, the score is increased by a thousand and the
    agent program terminates in success */
    public void grab()
    {
        System.out.println("The agent has found the gold");
        
        countAction();
        score += 1000;
        alive = false;
    }
    
    public void fightWumpus()
    {
        System.out.println("The agent found the wumpus and got eaten");
        die();
    }
    
    public void killWumpus()
    {
        countAction();
        score += 10;
    }
    
    public void countAction()
    {
        score--;
        actions++;
    }
}
