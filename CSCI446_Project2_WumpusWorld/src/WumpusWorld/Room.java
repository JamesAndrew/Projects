package WumpusWorld;

/**
 * The grid is made of cells that can be traveled to.
 * Each cell property must be a boolean value
 * @version 09/27/16
 */
public class Room 
{
    
    private boolean isBlocked;
    private boolean isEmpty;
    private boolean isPit;
    private boolean isBreezy;
    private boolean isShiny;        // goal state if this is true
    private boolean isSmelly;
    private boolean isWumpus;
    
    private final int roomRow;
    private final int roomColumn;
    
    public Room(int row, int col)
    {
        roomRow = row;
        roomColumn = col;
    }
    
    public void randomRoom()
    {
        
    }
    
    public boolean isEmpty()
    {
        return isEmpty;
    }
    
    public boolean hasBreeze()
    {
        return isBreezy;
    }
    
    public boolean hasPit()
    {
        return isPit;
    }
    
    public boolean hasGold()
    {
        return isShiny;
    }
    
    public boolean hasStench()
    {
        return isSmelly;
    }
    
    public boolean hasWumpus()
    {
        return isWumpus;
    }
}
