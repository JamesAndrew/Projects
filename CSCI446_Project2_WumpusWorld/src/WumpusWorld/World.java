package WumpusWorld;

import java.util.Random;

/**
 * Represents the "dungeon" and contains a collection of cells (rooms)
 * @version 09/27/16
 */
public class World 
{
    private static Room [][] rooms;
    private static int size;
    private Random random = new Random();
    
    // density of dangers
    private final double wumpusProbability = 0.10;
    private final double pitProbability = 0.05;
    private final double obstructionProbability = 0.05;
    
    // number of wumpi/arrows (used for setting initial number of arrows)
    private static int numArrows;
    
   /**
     * constructor the agent will use for perceived world
     * @param s : number of rows for the s*s sized world
     */
    public World(int s)
    {
        size = s;
        rooms = new Room[size][size];
        for (int i = 0; i < size; i++)
        {
            for (int j = 0; j < size; j++)
            {
                rooms[i][j] = new Room(i, j);
            }
        }
        
        // place wumpi, smells, pits, gold etc.
        placeObjects();
    }
    
    private void placeObjects() 
    {
        int numWumpi = 0;
        int numPits = 0;  
        
        // iterate over all cells and place items based on their probability
        for (int row = 0; row < size; row++)
        {
            for (int col = 0; col < size; col++)
            {
                if (random.nextDouble() <= wumpusProbability)
                {
                    if (row + col <= 2) { } // don't place in (0,0), (1,0), or (0,1)
                    else
                    {
                        Room currentRoom = rooms[row][col];
                        placeWumpusAndSmells(currentRoom, row, col);
                        numWumpi++;
                        break;
                    }
                }
                if (random.nextDouble() <= pitProbability)
                {
                    if (row + col <= 2) { } // don't place in (0,0), (1,0), or (0,1)
                    else
                    {
                        Room currentRoom = rooms[row][col];
                        placePitAndBreezes(currentRoom, row, col);
                        numPits++;
                        break;
                    }
                }
                if (random.nextDouble() <= obstructionProbability)
                {
                    if (row + col <= 2) { } // don't place in (0,0), (1,0), or (0,1)
                    else 
                    {
                        Room currentRoom = rooms[row][col];
                        currentRoom.setIsBlocked(true);
                    }
                }
            }
        }
        
        // if no wumpus or pit generated, guarantee at least one is placed
        if (numWumpi == 0)
        {
            int randRow = 0;
            int randCol = 0;
            boolean validRoom = false;
            while (randRow + randCol <= 1 && !validRoom)
            {
                randRow = random.nextInt(size);
                randCol = random.nextInt(size);
                if (!(rooms[randRow][randCol].isBlocked()) &&
                    !(rooms[randRow][randCol].isPit())     &&
                    !(rooms[randRow][randCol].isShiny())   &&
                    !(rooms[randRow][randCol].isWumpus()))
                {
                    validRoom = true;
                }
            }
            
            placeWumpusAndSmells(rooms[randRow][randCol], randRow, randCol);
            numWumpi = 1;
        }
        if (numPits == 0)
        {
            int randRow = 0;
            int randCol = 0;
            boolean validRoom = false;
            while (randRow + randCol <= 1 && !validRoom)
            {
                randRow = random.nextInt(size);
                randCol = random.nextInt(size);
                if (!(rooms[randRow][randCol].isBlocked()) &&
                    !(rooms[randRow][randCol].isPit())     &&
                    !(rooms[randRow][randCol].isShiny())   &&
                    !(rooms[randRow][randCol].isWumpus()))
                {
                    validRoom = true;
                }
            }
            
            placePitAndBreezes(rooms[randRow][randCol], randRow, randCol);
        }
        numArrows = numWumpi;
        placeGold();
    }
    
    private void placeGold()
    {
        int randRow = 0;
        int randCol = 0;
        boolean validRoom = false;
        while (randRow + randCol <= 3 && !validRoom)
        {
            randRow = random.nextInt(size);
            randCol = random.nextInt(size);
            if (!(rooms[randRow][randCol].isBlocked()) &&
                !(rooms[randRow][randCol].isPit())     &&
                !(rooms[randRow][randCol].isWumpus()))
            {
                validRoom = true;
            }
            if (randRow + randCol <= 3) validRoom = false;
        }
        rooms[randRow][randCol].setIsShiny(true);
        rooms[randRow][randCol].setHasGold(true);
    }
    
    private void placeWumpusAndSmells(Room currentRoom, int row, int column) 
    {
        currentRoom.setIsWumpus(true);
        
        // place smells in adjacent rooms 
        // (existing rooms on every side)
        if ((row - 1 >= 0) && (row + 1 < size) && (column - 1 >= 0) && (column + 1 < size))
        {
            rooms[row-1][column  ].setIsSmelly(true);
            rooms[row  ][column+1].setIsSmelly(true);
            rooms[row+1][column  ].setIsSmelly(true);
            rooms[row  ][column-1].setIsSmelly(true);
        }
        // query cell left column : no rooms to the left (along y=0 column)
        else if ((row - 1 < 0) && (column - 1 >= 0) && (column + 1 < size))
        {
            rooms[row  ][column+1].setIsSmelly(true);
            rooms[row+1][column  ].setIsSmelly(true);
            rooms[row  ][column-1].setIsSmelly(true);
        }
        // query cell top left corner
        else if (((row - 1 < 0) && (column + 1 >= size)))  
        {
            rooms[row+1][column  ].setIsSmelly(true);
            rooms[row  ][column-1].setIsSmelly(true);
        }
        // query cell top row 
        else if ((column + 1 >= size) && (row - 1 >= 0) && (row + 1 < size))
        {
            rooms[row-1][column  ].setIsSmelly(true);
            rooms[row+1][column  ].setIsSmelly(true);
            rooms[row  ][column-1].setIsSmelly(true);
        }
        // query cell top right corner
        else if ((row + 1 >= size) && column + 1 >= size) 
        {
            rooms[row-1][column  ].setIsSmelly(true);
            rooms[row  ][column-1].setIsSmelly(true);
        }
        // query cell right column
        else if ((row + 1 >= size) && (column - 1 >= 0) && (column + 1 < size))
        {
            rooms[row-1][column  ].setIsSmelly(true);
            rooms[row  ][column+1].setIsSmelly(true);
            rooms[row  ][column-1].setIsSmelly(true);
        }
        // query cell bottom right corner
        else if ((row + 1 >= size) && column - 1 < 0)
        {
            rooms[row-1][column  ].setIsSmelly(true);
            rooms[row  ][column+1].setIsSmelly(true);
        }
        // query cell bottom row
        else if ((column - 1 < 0) && (row - 1 >= 0) && (row + 1 < size))
        {
            rooms[row-1][column  ].setIsSmelly(true);
            rooms[row  ][column+1].setIsSmelly(true);
            rooms[row+1][column  ].setIsSmelly(true);
        }
        // query cell bottom left corner
        else if((row - 1 < 0) && column - 1 < 0)
        {
            rooms[row  ][column+1].setIsSmelly(true);
            rooms[row+1][column  ].setIsSmelly(true);
        }
        else throw new RuntimeException("None of the room positions calculated correctly"
                + " during placeWumpusAndSmells");
        
    }
    
    private void placePitAndBreezes(Room currentRoom, int row, int column) 
    {
        currentRoom.setIsPit(true);
        
        // place smells in adjacent rooms 
        // (existing rooms on every side)
        if ((row - 1 >= 0) && (row + 1 < size) && (column - 1 >= 0) && (column + 1 < size))
        {
            rooms[row-1][column  ].setIsBreezy(true);
            rooms[row  ][column+1].setIsBreezy(true);
            rooms[row+1][column  ].setIsBreezy(true);
            rooms[row  ][column-1].setIsBreezy(true);
        }
        // query cell left column : no rooms to the left (along y=0 column)
        else if ((row - 1 < 0) && (column - 1 >= 0) && (column + 1 < size))
        {
            rooms[row  ][column+1].setIsBreezy(true);
            rooms[row+1][column  ].setIsBreezy(true);
            rooms[row  ][column-1].setIsBreezy(true);
        }
        // query cell top left corner
        else if (((row - 1 < 0) && (column + 1 >= size)))  
        {
            rooms[row+1][column  ].setIsBreezy(true);
            rooms[row  ][column-1].setIsBreezy(true);
        }
        // query cell top row 
        else if ((column + 1 >= size) && (row - 1 >= 0) && (row + 1 < size))
        {
            rooms[row-1][column  ].setIsBreezy(true);
            rooms[row+1][column  ].setIsBreezy(true);
            rooms[row  ][column-1].setIsBreezy(true);
        }
        // query cell top right corner
        else if ((row + 1 >= size) && column + 1 >= size) 
        {
            rooms[row-1][column  ].setIsBreezy(true);
            rooms[row  ][column-1].setIsBreezy(true);
        }
        // query cell right column
        else if ((row + 1 >= size) && (column - 1 >= 0) && (column + 1 < size))
        {
            rooms[row-1][column  ].setIsBreezy(true);
            rooms[row  ][column+1].setIsBreezy(true);
            rooms[row  ][column-1].setIsBreezy(true);
        }
        // query cell bottom right corner
        else if ((row + 1 >= size) && column - 1 < 0)
        {
            rooms[row-1][column  ].setIsBreezy(true);
            rooms[row  ][column+1].setIsBreezy(true);
        }
        // query cell bottom row
        else if ((column - 1 < 0) && (row - 1 >= 0) && (row + 1 < size))
        {
            rooms[row-1][column  ].setIsBreezy(true);
            rooms[row  ][column+1].setIsBreezy(true);
            rooms[row+1][column  ].setIsBreezy(true);
        }
        // query cell bottom left corner
        else if((row - 1 < 0) && column - 1 < 0)
        {
            rooms[row  ][column+1].setIsBreezy(true);
            rooms[row+1][column  ].setIsBreezy(true);
        }
        else throw new RuntimeException("None of the room positions calculated correctly"
                + " during placePitAndBreezes");
    }
    
    public static Room getRoom(int row, int col)
    {
        return rooms[row][col];
    }
    
    public static Room[][] getRooms() 
    {
        return rooms;
    }
    
    public static int getSize()
    {
        return size;
    }

    public static void printWorld()
    {
        for (int row = 0; row < size; row++)
        {
            System.out.format("Row %d: ", row);
            for (int col = 0; col < size; col++)
            {
                Room currentRoom = rooms[row][col];
                System.out.format("(%d,%d)", row, col);
                if (currentRoom.isShiny())        System.out.print("gold   ");
                else if (currentRoom.isWumpus())  System.out.print("wumpus ");
                else if (currentRoom.isSmelly())  System.out.print("smelly ");
                else if (currentRoom.isPit())     System.out.print("pit    ");
                else if (currentRoom.isBreezy())  System.out.print("breezy ");
                else if (currentRoom.isBlocked()) System.out.print("blocked");
                else                              System.out.print("  ---  ");
                if (col != size-1)
                {
                    System.out.print("| ");
                }
                
            }
            System.out.println();
        }
    }

    public static int getNumArrows() 
    {
        return numArrows;
    }
}
