package WumpusWorld;

/**
 * The grid is made of cells that can be traveled to.
 * Each cell property must be a boolean value
 * @version 09/27/16
 */
public class Cell 
{
    private boolean isEmpty;
    private boolean hasObstacle;
    private boolean hasPit;
    private boolean hasBreeze;
    private boolean hasGold;        // goal state if this is true
    private boolean hasWumpus;
    private boolean hasStench;
    
    public Cell()
    {
        
    }
    
    public void randomCell()
    {
        
    }
    
    public boolean isEmpty()
    {
        return isEmpty;
    }
    
    public boolean hasBreeze()
    {
        return hasBreeze;
    }
    
    public boolean hasPit()
    {
        return hasPit;
    }
    
    public boolean hasGold()
    {
        return hasGold;
    }
    
    public boolean hasStench()
    {
        return hasStench;
    }
    
    public boolean hasWumpus()
    {
        return hasWumpus;
    }
}
