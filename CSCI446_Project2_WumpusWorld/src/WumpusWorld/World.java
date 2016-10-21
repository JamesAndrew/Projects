package WumpusWorld;
/**
 * Represents the "dungeon" and contains a collection of cells (rooms)
 * @version 09/27/16
 */
public class World 
{
    private static Room [][] rooms;
    private static int size;
    
    /**
     * constructor the agent will use for perceived world
     * @param s : number of rows for the s*s sized world
     */
    public World(int s)
    {
        System.out.println("== Initializing the Actual World ==");
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
