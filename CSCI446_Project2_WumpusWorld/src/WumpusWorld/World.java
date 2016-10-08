package WumpusWorld;

import java.util.Random;

/**
 * Represents the "dungeon" and contains a collection of cells (rooms)
 * @version 09/27/16
 */
public class World 
{
    private final Room [][] theWorld;
    private Room start;
    private final int size;
    private final float pPit;
    private final float pObs;
    private final float pWumpus;
    private int wumpi;
    
    // constructor the agent will use for perceived world
    public World(int s)
    {
        System.out.println("== Initializing the Perceived World ==");
        
        pPit = 0;
        pObs = 0;
        pWumpus = 0;
        
        size = s;
        theWorld = new Room[size][size];
        for (int i = 0; i < size; i++)
        {
            for (int j = 0; j < size; j++)
            {
                theWorld[i][j] = new Room(i, j);
            }
        }
    }
    
    // constructor for the actual world
    public World(int s, float p, float o, float w)
    {
        System.out.println("== Initializing the Actual World ==");
        pPit = p;
        pObs = o;
        pWumpus = w;
        
        size = s;
        theWorld = new Room[size][size];
        for (int i = 0; i < size; i++)
        {
            for (int j = 0; j < size; j++)
            {
                theWorld[i][j] = new Room(i, j);
            }
        }
        
        setUpWorld();
    }
    
    private void setUpWorld()
    {
        System.out.println(" = Setting Up Map = ");
         
        initWorld();
    }
    
    private void initWorld()
    {
        Random rand = new Random();
        
        /**
         * place the gold
         */
        int r = rand.nextInt(size);
        int c = rand.nextInt(size);
        theWorld[r][c].setIsShiny(true);
        System.out.println("The gold is in room " + r + " " + c);
            
        
        /**
         * Place the start room in a random empty 
         */
        boolean startPlaced = false;
        while(!startPlaced)
        {
            r = rand.nextInt(size);
            c = rand.nextInt(size);
            
            if (theWorld[r][c].isShiny())
            {
                startPlaced = false;
            }
            
            else
            {
                startPlaced = true;
                start = theWorld[r][c];
                System.out.println("The agent is in room " + r + " " + c);
            }
        }
        
        /**
         * Add the pits and wumpi
         */
        for (int i = 0; i < size; i++)
        {
            for (int j = 0; j < size; j++)
            {
                if (theWorld[i][j].isShiny())
                {
                    System.out.println("occupied by gold");
                }
                
                else if ( (i == start.getRoomRow()) && (j == start.getRoomColumn()) )
                {
                    System.out.println("starting point");
                }
                
                else
                {
                    float o = rand.nextFloat();
                
                    if (o < pObs)
                    {
                        theWorld[i][j].setIsBlocked(true);
                    }

                    else
                    {
                        float p = rand.nextFloat();

                        if (p < pPit)
                        {
                            theWorld[i][j].setIsPit(true);
                            if(i == 0 && j == 0)
                            {
                                theWorld[i][j+1].setIsBreezy(true);
                                theWorld[i+1][j].setIsBreezy(true);
                            }

                            else if(i == 0 && j < size-1)
                            {
                                theWorld[i][j-1].setIsBreezy(true);
                                theWorld[i][j+1].setIsBreezy(true);
                                theWorld[i+1][j].setIsBreezy(true);
                            }

                            else if(i == 0 && j == size-1)
                            {
                                theWorld[i][j-1].setIsBreezy(true);
                                theWorld[i+1][j].setIsBreezy(true);
                            }

                            else if(i < size-1 && j == 0)
                            {
                                theWorld[i-1][j].setIsBreezy(true);
                                theWorld[i][j+1].setIsBreezy(true);
                                theWorld[i+1][j].setIsBreezy(true);
                            }

                            else if(i < size-1 && j == size-1)
                            {
                                theWorld[i-1][j].setIsBreezy(true);
                                theWorld[i][j-1].setIsBreezy(true);
                                theWorld[i+1][j].setIsBreezy(true);
                            }

                            else if(i == size-1 && j == 0)
                            {
                                theWorld[i-1][j].setIsBreezy(true);
                                theWorld[i][j+1].setIsBreezy(true);
                            }

                            else if(i == size-1 && j < size-1)
                            {
                                theWorld[i][j-1].setIsBreezy(true);
                                theWorld[i-1][j].setIsBreezy(true);
                                theWorld[i][j+1].setIsBreezy(true);
                            }

                            else if(i == size-1 && j == size-1)
                            {
                                theWorld[i][j-1].setIsBreezy(true);
                                theWorld[i-1][j].setIsBreezy(true);
                            }

                            else
                            {
                                theWorld[i-1][j].setIsBreezy(true);
                                theWorld[i][j+1].setIsBreezy(true);
                                theWorld[i+1][j].setIsBreezy(true);
                                theWorld[i][j-1].setIsBreezy(true);
                            }
                        }

                        float w = rand.nextFloat();
                        if (w < pWumpus)
                        {
                            theWorld[i][j].setIsWumpus(true);
                            wumpi++;
                            if(i == 0 && j == 0)
                            {
                                theWorld[i][j+1].setIsSmelly(true);
                                theWorld[i+1][j].setIsSmelly(true);
                            }

                            else if(i == 0 && j < size-1)
                            {
                                theWorld[i][j-1].setIsSmelly(true);
                                theWorld[i][j+1].setIsSmelly(true);
                                theWorld[i+1][j].setIsSmelly(true);
                            }

                            else if(i == 0 && j == size-1)
                            {
                                theWorld[i][j-1].setIsSmelly(true);
                                theWorld[i+1][j].setIsSmelly(true);
                            }

                            else if(i < size-1 && j == 0)
                            {
                                theWorld[i-1][j].setIsSmelly(true);
                                theWorld[i][j+1].setIsSmelly(true);
                                theWorld[i+1][j].setIsSmelly(true);
                            }

                            else if(i < size-1 && j == size-1)
                            {
                                theWorld[i-1][j].setIsSmelly(true);
                                theWorld[i][j-1].setIsSmelly(true);
                                theWorld[i+1][j].setIsSmelly(true);
                            }

                            else if(i == size-1 && j == 0)
                            {
                                theWorld[i-1][j].setIsSmelly(true);
                                theWorld[i][j+1].setIsSmelly(true);
                            }

                            else if(i == size-1 && j < size-1)
                            {
                                theWorld[i][j-1].setIsSmelly(true);
                                theWorld[i-1][j].setIsSmelly(true);
                                theWorld[i][j+1].setIsSmelly(true);
                            }

                            else if(i == size-1 && j == size-1)
                            {
                                theWorld[i][j-1].setIsSmelly(true);
                                theWorld[i-1][j].setIsSmelly(true);
                            }

                            else
                            {
                                theWorld[i-1][j].setIsSmelly(true);
                                theWorld[i][j+1].setIsSmelly(true);
                                theWorld[i+1][j].setIsSmelly(true);
                                theWorld[i][j-1].setIsSmelly(true);
                            }
                        }
                    }
                }
            }
        }
    }
    
    public int getSize()
    {
        return size;
    }
    
    public Room getRoom(int row, int col)
    {
        return theWorld[row][col];
    }
    
    public Room getStart()
    {
        return start;
    }
    
    public int getWumpi()
    {
        return wumpi;
    }
}
