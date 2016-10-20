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
    private Room prevRoom;           // the prev room the agent was in to only determine last action
    private final World actualWorld; // the actual World generated 
    private World state;             // the agents perception of the world based on last action and model
    
    Random rand = new Random(); // uniform random number generator
    
    /**
     * Constructor for ReactiveAgent
     * @param w an actual world generated
     */
    public ReactiveAgent(World w, int m)
    {
        score = 0;
        actions = 0;
        moves = m;
        
        actualWorld = w;
        currentRoom = actualWorld.getStart();
        prevRoom = new Room(0,0);
        
        state = new World(3);
        
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
            
            updateState(); // update agent's perception of World
            Action();      // determine agent's action based on previous move and model of next move
            moves--;
                        
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
        // handle the rewind when agent dies
        if (alive == false)
        {
            alive = true;
            if (turn == 1)
            {
                currentRoom = actualWorld.getStart();
            }
            else
            {
                currentRoom = prevRoom;
                switch (direction) 
                {
                    case 0:
                        if (actualWorld.getRoom(currentRoom.getRoomRow(), (currentRoom.getRoomColumn()-1)).isWumpus())
                        {
                            state.getRoom(1, 0).setIsWumpus(true);
                        }
                        if (actualWorld.getRoom(currentRoom.getRoomRow(), (currentRoom.getRoomColumn()-1)).isPit())
                        {
                            state.getRoom(1, 0).setIsPit(true);
                        }
                        break;
                    case 1:
                        if (actualWorld.getRoom((currentRoom.getRoomRow()-1), currentRoom.getRoomColumn()).isWumpus())
                        {
                            state.getRoom(0, 1).setIsWumpus(true);
                        }
                        if (actualWorld.getRoom((currentRoom.getRoomRow()-1), currentRoom.getRoomColumn()).isPit())
                        {
                            state.getRoom(0, 1).setIsPit(true);
                        }
                        break;
                    case 2:
                        if (actualWorld.getRoom(currentRoom.getRoomRow(), (currentRoom.getRoomColumn()+1)).isWumpus())
                        {
                            state.getRoom(1, 2).setIsWumpus(true);
                        }
                        if (actualWorld.getRoom(currentRoom.getRoomRow(), (currentRoom.getRoomColumn()+1)).isPit())
                        {
                            state.getRoom(1, 2).setIsPit(true);
                        }
                        break;
                    case 3:
                        if (actualWorld.getRoom((currentRoom.getRoomRow()+1), currentRoom.getRoomColumn()).isWumpus())
                        {
                            state.getRoom(2, 1).setIsWumpus(true);
                        }
                        if (actualWorld.getRoom((currentRoom.getRoomRow()+1), currentRoom.getRoomColumn()).isPit())
                        {
                            state.getRoom(2, 1).setIsPit(true);
                        }
                        break;
                    default:
                        break;
                }
                switch(prevDirection)
                {
                    case 0:
                        prevRoom = actualWorld.getRoom(currentRoom.getRoomRow(), (currentRoom.getRoomColumn() + 1));
                        break;
                    case 1:
                        prevRoom = actualWorld.getRoom((currentRoom.getRoomRow()+1), currentRoom.getRoomColumn());
                        break;
                    case 2:
                        prevRoom = actualWorld.getRoom(currentRoom.getRoomRow(), (currentRoom.getRoomColumn() - 1));
                        break;
                    case 3:
                        prevRoom = actualWorld.getRoom((currentRoom.getRoomRow()-1), currentRoom.getRoomColumn());
                    default:
                        break;
                }
            }
        }
        
        // update sensors
        feelBreeze = currentRoom.isBreezy();
        smellStench = currentRoom.isSmelly();
        seeGlitter = currentRoom.isShiny();
        
        if (hitObstacle == true)
        {
            switch (direction) {
                case 0:
                    state.getRoom(1, 0).setIsBlocked(true);
                    break;
                case 1:
                    state.getRoom(0, 1).setIsBlocked(true);
                    break;
                case 2:
                    state.getRoom(1, 2).setIsBlocked(true);
                    break;
                case 3:
                    state.getRoom(2, 1).setIsBlocked(true);
                    break;
                default:
                    break;
            }
        }
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
    agent goes back to last safe square */
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
            switch (prevDirection) {
                case 0:
                    if (direction == 1)
                        turnRight();
                    else
                        turnLeft();
                    break;
                case 1:
                    if (direction == 2)
                        turnRight();
                    else
                        turnLeft();
                    break;
                case 2:
                    if (direction == 3)
                        turnRight();
                    else
                        turnLeft();
                    break;
                case 3:
                    if (direction == 0)
                        turnRight();
                    else
                        turnLeft();
                    break;
                default:
                    break;
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
        switch (direction) {
            case 0:
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
                    if (state.getRoom(1, 0).isBlocked() || state.getRoom(1,0).isPit() || state.getRoom(1,0).isWumpus())
                    {
                        System.out.println("obstacle, pit, or wumpus there");
                        canMove = false;
                        if (r == 0)
                            turnLeft();
                        else
                            turnRight();
                    }
                    else if (checkRoom.isBlocked())
                    {
                        System.out.println("the agent felt a bump");
                        canMove = false;
                        hitObstacle = true;
                        countAction();
                    }
                    else
                    {
                        prevRoom = currentRoom;
                        currentRoom = checkRoom;
                    }
                }   break;
            case 1:
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
                    if (state.getRoom(0, 1).isBlocked() || state.getRoom(0, 1).isPit() || state.getRoom(0, 1).isWumpus())
                    {
                        System.out.println("obstacle, pit, or wumpus there");
                        canMove = false;
                        if (c == 0)
                            turnRight();
                        else
                            turnLeft();
                    }
                    else if (checkRoom.isBlocked())
                    {
                        System.out.println("the agent felt a bump");
                        canMove = false;
                        hitObstacle = true;
                        countAction();
                    }
                    else
                    {
                        prevRoom = currentRoom;
                        currentRoom = checkRoom;
                    }
                }   break;
            case 2:
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
                    if (state.getRoom(1,2).isBlocked() || state.getRoom(1,2).isPit() || state.getRoom(1,2).isWumpus())
                    {
                        System.out.println("obstacle, pit, or wumpus there");
                        canMove = false;
                        if (r == 0)
                            turnRight();
                        else
                            turnLeft();
                    }
                    else if (checkRoom.isBlocked())
                    {
                        System.out.println("the agent felt a bump");
                        canMove = false;
                        hitObstacle = true;
                        countAction();
                    }
                    else
                    {
                        prevRoom = currentRoom;
                        currentRoom = checkRoom;
                    }
                }   break;
            case 3:
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
                    if (state.getRoom(2, 1).isBlocked() || state.getRoom(2,1).isPit() || state.getRoom(2,1).isWumpus())
                    {
                        System.out.println("obstacle, pit, or wumpus there");
                        canMove = false;
                        if (c == 0)
                            turnLeft();
                        else
                            turnRight();
                    }
                    else if (checkRoom.isBlocked())
                    {
                        System.out.println("the agent felt a bump");
                        canMove = false;
                        hitObstacle = true;
                        countAction();
                    }
                    else
                    {
                        prevRoom = currentRoom;
                        currentRoom = checkRoom;
                    }
                }   break;
            default:
                break;
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
        }
        else if (direction == 1)
        {
            if (r == 0)
                System.out.println("The arrow would hit a wall");
        }
        else if (direction == 2)
        {
            if (c == actualWorld.getSize())
                System.out.println("The arrow would hit a wall");
        }
        else if (direction == 3)
        {
            if (r == actualWorld.getSize())
                System.out.println("The arrow would hit a wall");
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
            switch (direction) {
                case 0:
                    for(int l = c; l > -1; l--)
                    {
                        Room check = actualWorld.getRoom(r, l);
                        if (check.isWumpus())
                        {
                            killWumpus();
                        }
                        else
                            System.out.println("The agent missed");
                    }   break;
                case 1:
                    for(int u = r; u > -1; u--)
                    {
                        Room check = actualWorld.getRoom(u, c);
                        if (check.isWumpus())
                        {
                            killWumpus();
                        }
                        else
                            System.out.println("The agent missed");
                    }   break;
                case 2:
                    for(int l = c; l < actualWorld.getSize(); l++)
                    {
                        Room check = actualWorld.getRoom(l, c);
                        if (check.isWumpus())
                        {
                            killWumpus();
                        }
                        else
                            System.out.println("The agent missed");
                    }   break;
                case 3:
                    for(int u = r; u < actualWorld.getSize(); u++)
                    {
                        Room check = actualWorld.getRoom(u, c);
                        if (check.isWumpus())
                        {
                            killWumpus();
                        }
                        else
                            System.out.println("The agent missed");
                    }   break;
                default:
                    System.out.println("error, lost sense of direction");
                    break;
            }
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
        moves = 0;
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
         * Check for "end" states first
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
