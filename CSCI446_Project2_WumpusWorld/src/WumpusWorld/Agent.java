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
    private boolean seeGlitter;
    private boolean alive;
    
    /**
     * Resources
     */
    private int score;
    private int arrows;
    
    Cell currentCell;
    
    public Agent(Cell start)
    {
        score = 0;
        currentCell = start;
        alive = true;
        life();
    }
    
    public void life()
    {
        System.out.println("The agent has entered the cave.");
        while(alive == true)
        {
            agentStatus();
            if(currentCell.hasPit() == true)
            {
                System.out.println("The agent has fell down a pit.");
                die();
            }
        }
        agentStatus();
    }
    
    public void agentStatus()
    {
        System.out.println("== Agent Status ==");
        System.out.println("Score: " + score);
    }
    
    /**
     * Actuators
     */
    /* when the explorer dies, the score is decreased by a thousand and the 
    agent program terminates in failure */
    public void die()
    {
        score -= 1000;
        alive = false;
        System.out.println("The agent has died.");
    }
    
    public void fireArrow()
    {
        arrows--;
        score -= 10;
        // if Wumpus hit
        
        // if missed
    }
    
    /* if the explorer finds gold, the score is increased by a thousand and the
    agent program terminates in success */
    public void takeGold()
    {
        score += 1000;
    }
}
