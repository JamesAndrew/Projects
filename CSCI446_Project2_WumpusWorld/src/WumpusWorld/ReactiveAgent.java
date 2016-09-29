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
    
    Room currentRoom;
    
    public ReactiveAgent(Room start)
    {
        score = 0;
        actions = 0;
        currentRoom = start;
        heardScream = false;
        alive = true;
        life();
    }
    
    public void life()
    {
        System.out.println("The agent has entered the cave.");
//        while(alive == true)
//        {
            agentStatus();
            if(currentRoom.isEmpty())
            {
                System.out.println("The agent is in an empty cell");
            }
            
            if(currentRoom.hasBreeze())
            {
                System.out.println("The agent feels a breeze");
                feelBreeze = true;
            }
            
            if(currentRoom.hasStench())
            {
                System.out.println("The agent smells a horrible stench");
                smellStench = true;
            }
            
            if(currentRoom.hasWumpus())
            {
                fightWumpus();
            }
            
            if(currentRoom.hasPit() == true)
            {
                fall();
            }
            
            if(currentRoom.hasGold() == true)
            {
                takeGold();
            }
            
            Forward();
//        }
        agentStatus();
    }
    
    public void agentStatus()
    {
        System.out.println("== Agent Status ==");
        System.out.println("Score: " + score);
        System.out.println("hitObstacle: " + hitObstacle);
        System.out.println("feelBreeze: " + feelBreeze);
        System.out.println("smellStench: " + smellStench);
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
    public void takeGold()
    {
        System.out.println("The agent has found the gold");
        
        countAction();
        score += 1000;
        alive = false;
    }
    
    public void fightWumpus()
    {
        System.out.println("The agent found the wumpus, fought it, and...");
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
