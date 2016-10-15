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
    private boolean hitObstacle; // if the agent hit an obstacle 
    private boolean feelBreeze;  // if the agent feels a breeze
    private boolean smellStench; // if the agent smells a stench
    private boolean heardScream; // if the agent heard a wumpus die
    private boolean seeGlitter;  // if the agent found the gold
    
    private boolean alive;       // flag for whether the agent program continues or not 
    private int direction;       // 0 - left, 1 - up, 2 - right, 3 - down 
    private int prevDirection;   // what direction did the agent last face
    /**
     * Resources
     */
    private int score;     // agent score
    private int arrows;    // number of arrows
    private int actions;   // how many actions taken so far
    private int moves = 0; // number of allowed moves (for testing at this point)
    private int turn = 0;  // which turn the agent is on
    
    /**
     * World and Room states
     */
    private Room currentRoom;        // the current room the agent is in
    private final World actualWorld; // the actual World generated 
    private World perceivedWorld;    // what the agent has learned about the world
    
    
    Random rand = new Random(); // uniform random number generator
    
    /**
     * Constructor for ReactiveAgent
     * @param w an actual world generated
     */
    public ReactiveAgent(World w)
    {
        score = 0;
        actions = 0;
        moves = 1000;
        
        actualWorld = w;
        perceivedWorld = new World(actualWorld.getSize());
        currentRoom = actualWorld.getStart();
        
        heardScream = false;
        alive = true;
        life();
    }
    
    /**
     * life contains the main loop of the agent
     */
    public void life()
    {
        System.out.println("The agent has entered the cave.");
        
        // initialize additional "sensors"
        initArrows();
        initDirection();
        
        // keep exploring while moves left
        while (moves > 0)
        {
            // if the agent is dead, no more moves allowed
            if (alive == false)
            {
                moves = 0;
            }
            // if the agent is alive, keep exploring
            else
            {
                updateState(); // update agent's perception of World
                Action();      // determine agent's action 
                moves--;
            }
            
            System.out.println("=== " + moves + " moves left");
            System.out.println("=== turn " + turn);
            turn++;
        }
        
        agentStatus();
    }
    
    /**
     * update sensors and safety matrix
     */
    private void updateSensors()
    {
        int r = currentRoom.getRoomRow();
        int c = currentRoom.getRoomColumn();
        
        // update sensors
        feelBreeze = currentRoom.isBreezy();
        smellStench = currentRoom.isSmelly();
        seeGlitter = currentRoom.isShiny();
        
    }
    
    /**
     * print the current status of agent, including the safety matrix
     */
    private void agentStatus()
    {
        System.out.println("== Agent Status ==");
        System.out.println("Score: " + score);
        System.out.println("Alive: " + alive);
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
        if (hitObstacle == true)
        {
            if (prevDirection == 0)
            {
                if (direction == 1)
                    turnRight();
                else
                    turnLeft();
            }
            else if (prevDirection == 1)
            {
                if (direction == 2)
                    turnRight();
                else
                    turnLeft();
            }
            else if (prevDirection == 2)
            {
                if (direction == 3)
                    turnRight();
                else
                    turnLeft();
            }
            else if (prevDirection == 3)
            {
                if (direction == 0)
                    turnRight();
                else
                    turnLeft();
            }
        }
        else
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
                if (r == 0)
                    turnLeft();
                else
                    turnRight();
            }
            else if (c > 0)
            {
                checkRoom = actualWorld.getRoom(r, c-1);
                if (perceivedWorld.getRoom(r, c-1).isBlocked())
                {
                    canMove = false;
                }
                else if (checkRoom.isBlocked())
                {
                    System.out.println("the agent felt a bump");
                    perceivedWorld.getRoom(r, c-1).setIsBlocked(true);
                    canMove = false;
                    hitObstacle = true;
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
                if (c == 0)
                    turnRight();
                else
                    turnLeft();
            }
            else if (r > 0)
            {
                checkRoom = actualWorld.getRoom(r-1, c);
                if (perceivedWorld.getRoom(r-1, c).isBlocked())
                {
                    canMove = false;
                }
                else if (checkRoom.isBlocked())
                {
                    System.out.println("the agent felt a bump");
                    perceivedWorld.getRoom(r-1, c).setIsBlocked(true);
                    canMove = false;
                    hitObstacle = true;
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
                if (r == 0)
                    turnRight();
                else
                    turnLeft();
            }
            else if (c < actualWorld.getSize()-1)
            {
                checkRoom = actualWorld.getRoom(r, c+1);
                if (perceivedWorld.getRoom(r, c+1).isBlocked())
                {
                    canMove = false;
                }
                else if (checkRoom.isBlocked())
                {
                    System.out.println("the agent felt a bump");
                    perceivedWorld.getRoom(r, c+1).setIsBlocked(true);
                    canMove = false;
                    hitObstacle = true;
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
                if (c == 0)
                    turnLeft();
                else
                    turnRight();
            }
            else if (r < actualWorld.getSize()-1)
            {
                checkRoom = actualWorld.getRoom(r+1, c);
                if (perceivedWorld.getRoom(r+1, c).isBlocked())
                {
                    canMove = false;
                }
                else if (checkRoom.isBlocked())
                {
                    System.out.println("the agent felt a bump");
                    perceivedWorld.getRoom(r+1, c).setIsBlocked(true);
                    canMove = false;
                    hitObstacle = true;
                }
                else
                    currentRoom = checkRoom;
            }
        }
        
        if (canMove == true)
        {
            hitObstacle = false;
            System.out.println("The agent has moved forward.");
            countAction();
        }
    }
    
    public void turnRight()
    {
        System.out.println("The agent has turned right 90 degrees");
        
        countAction();
        prevDirection = direction;
        direction++;
        if (direction == 4)
        {
            direction = 0;
        }
        hitObstacle = false;
    }
    
    public void turnLeft()
    {
        System.out.println("The agent has turned left 90 degrees");
        
        countAction();
        prevDirection = direction;
        direction--;
        if (direction == -1)
        {
            direction = 3;
        }
        hitObstacle = false;
    }
    
    // determine whether the agent should shoot, where, and shoot if conditions met
    public void reasonShootAction()
    {
        int r = currentRoom.getRoomRow();
        int c = currentRoom.getRoomColumn();
        
        if (turn == 0)
            System.out.println("The agent thinks shooting now would be pointless");
        else if (arrows <= 0)
        {
            System.out.println("The agent has no arrows");
        }
        else if (direction == 0)
        {
            if (c == 0)
                System.out.println("The arrow would hit a wall");
            else if (perceivedWorld.getRoom(r, c-1).isBlocked())
                System.out.println("The arrow would hit an obstacle");
        }
        else if (direction == 1)
        {
            if (r == 0)
                System.out.println("The arrow would hit a wall");
            else if (perceivedWorld.getRoom(r-1, c).isBlocked())
                System.out.println("The arrow would hit an obstacle");
        }
        else if (direction == 2)
        {
            if (c == actualWorld.getSize())
                System.out.println("The arrow would hit a wall");
            else if (perceivedWorld.getRoom(r, c+1).isBlocked())
                System.out.println("The arrow would hit an obstacle");
        }
        else if (direction == 3)
        {
            if (r == actualWorld.getSize())
                System.out.println("The arrow would hit a wall");
            else if (perceivedWorld.getRoom(r+1, c).isBlocked())
                System.out.println("The arrow would hit an obstacle");
        }
        else
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
                        perceivedWorld.getRoom(r, l).setIsWumpus(false);
                        killWumpus();
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
                        perceivedWorld.getRoom(u, c).setIsWumpus(false);
                        killWumpus();
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
                        perceivedWorld.getRoom(l, c).setIsWumpus(false);
                        killWumpus();
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
                        perceivedWorld.getRoom(u, c).setIsWumpus(false);
                        killWumpus();
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
            }
            
            reasonForward();
        }
    }
}
