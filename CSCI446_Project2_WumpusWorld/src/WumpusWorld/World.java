package WumpusWorld;

/**
 * Represents the "dungeon" and contains a collection of cells (rooms)
 * @version 09/27/16
 */
public class World 
{
    private Room theWorld[][];
    private int size;
    
    public World(int s)
    {
        size = s;
        for (int i = 0; i < size; i++)
        {
            for (int j = 0; j < size; i++)
            {
                theWorld[i][j] = new Room(i, j);
            }
        }
    }
    
    public int getSize()
    {
        return size;
    }
    
}
