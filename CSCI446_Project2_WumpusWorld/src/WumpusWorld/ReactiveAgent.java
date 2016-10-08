package WumpusWorld;

import java.util.Random;

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
    private int safe[][]; // array to keep track of danger, 0 - unknown, 1 - safe, 2 - dangerous
    
    Random rand = new Random(); // uniform random number generator
    
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
        
        initArrows();
        initDirection();
        initSafe();
        
        int moves = 2;
        while (moves > 0)
        {
            
            updateState();
            Action();
            
            moves--;
            System.out.println("=== " + moves + " moves left");
        }
        
        agentStatus();
    }
    
    private void updateSensors()
    {
        feelBreeze = currentRoom.isBreezy();
        smellStench = currentRoom.isSmelly();
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
        System.out.println("arrows " + arrows);
        System.out.println("The agent is in room " + currentRoom.getRoomRow() + " " + currentRoom.getRoomColumn());
        System.out.println("There are " + actualWorld.getWumpi() + " wumpi");
        System.out.println();
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
    
    public void reasonForward()
    {
        Forward();
    }
    
    public void Forward()
    {
        int r = currentRoom.getRoomRow();
        int c = currentRoom.getRoomColumn();
        Room checkRoom = actualWorld.getRoom(r, c);
        
        boolean canMove = true;
        if (direction == 0)
        {
            if (c == 0)
            {
                System.out.println("the edge is there");
                canMove = false;
            }
            else if (c > 0)
            {
                checkRoom = actualWorld.getRoom(r, c-1);
                if (checkRoom.isBlocked())
                {
                    System.out.println("the agent felt a bump");
                    checkRoom.setIsBlocked(true);
                }
                else
                    currentRoom = checkRoom;
            }
        }
        else if (direction == 1)
        {
            if (r == 0)
            {
                System.out.println("the edge is there");
                canMove = false;
            }
            else if (r > 0)
            {
                checkRoom = actualWorld.getRoom(r-1, c);
                if (checkRoom.isBlocked())
                {
                    System.out.println("the agent felt a bump");
                    checkRoom.setIsBlocked(true);
                }
                else
                    currentRoom = checkRoom;
            }
        }
        else if (direction == 2)
        {
            if (c == actualWorld.getSize()-1)
            {
                System.out.println("the edge is there");
                canMove = false;
            }
            else if (c < actualWorld.getSize()-1)
            {
                checkRoom = actualWorld.getRoom(r, c+1);
                if (checkRoom.isBlocked())
                {
                    System.out.println("the agent felt a bump");
                    checkRoom.setIsBlocked(true);
                }
                else
                    currentRoom = checkRoom;
            }
        }
        else if (direction == 3)
        {
            if (r == actualWorld.getSize()-1)
            {
                System.out.println("the edge is there");
                canMove = false;
            }
            else if (r < actualWorld.getSize()-1)
            {
                checkRoom = actualWorld.getRoom(r+1, c);
                if (checkRoom.isBlocked())
                {
                    System.out.println("the agent felt a bump");
                    checkRoom.setIsBlocked(true);
                }
                else
                    currentRoom = checkRoom;
            }
        }
        
        if (canMove == true)
        {
            System.out.println("The agent has moved forward.");
            countAction();
        }
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
    
    // determine whether the agent should shoot, where, and shoot if conditions met
    public void reasonShootAction()
    {
        shoot();
    }
    
    public void shoot()
    {
        if (arrows > 0)
        {
            System.out.println("The agent has fired an arrow");

            countAction();
            arrows--;
            score -= 10;
            
            int r = currentRoom.getRoomRow();
            int c = currentRoom.getRoomColumn();
            // if Wumpus hit
            if (direction == 0)
            {
                for(int l = c; l > -1; l--)
                {
                    Room check = actualWorld.getRoom(r, l);
                    if (check.isWumpus())
                    {
                        check.setIsWumpus(false);
                    }
                    else
                        System.out.println("The agent missed");
                }
            }

            else if (direction == 1)
            {
                for(int u = r; u > -1; u--)
                {
                    Room check = actualWorld.getRoom(u, c);
                    if (check.isWumpus())
                    {
                        check.setIsWumpus(false);
                    }
                    else
                        System.out.println("The agent missed");
                }
            }
            
            else if (direction == 2)
            {
                for(int l = c; l < actualWorld.getSize(); l++)
                {
                    Room check = actualWorld.getRoom(l, c);
                    if (check.isWumpus())
                    {
                        check.setIsWumpus(false);
                    }
                    else
                        System.out.println("The agent missed");
                }
            }
            
            else if (direction == 3)
            {
                for(int u = r; u < actualWorld.getSize(); u++)
                {
                    Room check = actualWorld.getRoom(u, c);
                    if (check.isWumpus())
                    {
                        check.setIsWumpus(false);
                    }
                    else
                        System.out.println("The agent missed");
                }
            }
            
            else
                System.out.println("error, lost sense of direction");
        }
        else
            System.out.println("The agent has no arrows");
    }
    
    public void initArrows()
    {
        arrows = actualWorld.getWumpi();
    }
    
    public void initDirection()
    {
        direction = rand.nextInt(4);
    }
    
    public void initSafe()
    {
        int roomSize = actualWorld.getSize();
        safe = new int [roomSize][roomSize];
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
        System.out.println("You heard a monster scream as it died");
        heardScream = true;                
        countAction();
        score += 10;
    }
    
    public void countAction()
    {
        score--;
        actions++;
    }
    
    /**
     * these are the model based functions from the model-based reflex agent
     */
    // update the perceivedWorld of the agent
    public void updateState()
    {
        updateSensors();
        agentStatus();
    }
    
    // determine and perform the agents next move
    public void Action()
    {
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
                reasonShootAction();
            }

            if (currentRoom.isEmpty() == true)
            {
                System.out.println("The room is empty");
                safe[currentRoom.getRoomRow()][currentRoom.getRoomColumn()] = 1;
            }
        }

        reasonForward();
    }
}
