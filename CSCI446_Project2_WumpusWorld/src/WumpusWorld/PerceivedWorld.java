package WumpusWorld;
/**
 * Represents the "dungeon" and contains a collection of cells (rooms)
 * @version 09/27/16
 */
public class PerceivedWorld 
{
    private Room [][] rooms;
    private final int size;
    
    /**
     * constructor the agent will use for perceived world
     * @param s : number of rows for the s*s sized world
     */
    public PerceivedWorld(int s)
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
    }
    
    public Room getRoom(int row, int col)
    {
        return rooms[row][col];
    }
    
    public int getSize()
    {
        return size;
    }
}
