package WumpusWorld;

/**
 * Represents the "dungeon" and contains a collection of cells (rooms)
 * @version 09/27/16
 */
public class World 
{
    private final Room [][] theWorld;
    private final int size;
    private final float pPit;
    private final float pObs;
    private final float pWumpus;
    
    // constructor the agent will use for perceived world
    public World(int s)
    {
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
    }
    
    public int getSize()
    {
        return size;
    }
    
    
    public void setUpWorld()
    {
        
    }
    
    public Room getRoom(int row, int col)
    {
        return theWorld[row][col];
    }
}
