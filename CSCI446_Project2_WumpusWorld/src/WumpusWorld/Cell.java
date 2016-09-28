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
    private boolean hasGold;        // goal state if this is true
    private boolean hasWumpus;
    
    public Cell()
    {
        
    }
    
    public void randomCell()
    {
        hasPit = true;
    }
    
    /**
     *
     * @return
     */
    public boolean hasPit()
    {
        return hasPit;
    }
}
