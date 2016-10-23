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
    
    // density of wompui
    private final double wumpusProbability = 0.1;
    
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
        // iterate over all cells and place items based on their probability
        for (int row = 0; row < size; row++)
        {
            for (int col = 0; col < size; col++)
            {
                if (random.nextDouble() <= wumpusProbability)
                {
                    Room currentRoom = rooms[row][col];
                    placeWumpusAndSmells(currentRoom, row, col);
                    break;
                }
            }
        }
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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

    
}
