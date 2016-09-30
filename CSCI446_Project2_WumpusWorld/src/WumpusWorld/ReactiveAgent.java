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
    
    private int currentRoom[];
    private final World actualWorld;
    private World perceivedWorld;
    
    public ReactiveAgent(int row, int col, World w)
    {
        score = 0;
        actions = 0;
        
        actualWorld = w;
        perceivedWorld = new World(actualWorld.getSize());
        
        currentRoom[0] = row;
        currentRoom[1] = col;
        heardScream = false;
        alive = true;
        life();
    }
    
    public void life()
    {
        System.out.println("The agent has entered the cave.");
        
        // update coordinates
        
        
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
