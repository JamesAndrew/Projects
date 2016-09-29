package WumpusWorld;

/**
 *
 * @version 09/28/16
 */
public final class Agent 
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
    
    /**
     * Resources
     */
    private int score;
    private int arrows;
    private int actions; // how many actions taken so far
    
    Cell currentCell;
    
    public Agent(Cell start)
    {
        score = 0;
        actions = 0;
        currentCell = start;
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
            if(currentCell.isEmpty())
            {
                System.out.println("The agent is in an empty cell");
            }
            
            if(currentCell.hasBreeze())
            {
                System.out.println("The agent feels a breeze");
                feelBreeze = true;
            }
            
            if(currentCell.hasStench())
            {
                System.out.println("The agent smells a horrible stench");
                smellStench = true;
            }
            
            if(currentCell.hasPit() == true)
            {
                fall();
            }
            
            if(currentCell.hasGold() == true)
            {
                takeGold();
            }
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
        System.out.println("The agent has fell down a pit.");
        die();
    }
    
    public void moveForward()
    {
        
    }
    
    public void fireArrow()
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
