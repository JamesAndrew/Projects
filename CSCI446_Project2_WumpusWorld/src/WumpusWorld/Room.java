package WumpusWorld;

/**
 * The grid is made of cells that can be traveled to.
 * Each cell property must be a boolean value
 * @version 09/27/16
 */
public class Room 
{
    private boolean isSafe; 
    private boolean isBlocked;
    private boolean isEmpty;    // empty include breezes and stenches
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
        isEmpty = true;
		isSafe = false; 
    }
    
    public int getRoomRow()
    {
        return roomRow;
    }
    
    public int getRoomColumn()
    {
        return roomColumn;
    }
    
    public void setIsEmpty(boolean e)
    {
        if(e == true)
        {
            setIsPit(false);
            setIsWumpus(false);
        }
        isEmpty = e;
    }
    
    public boolean isEmpty()
    {
        return isEmpty;
    }
    
    public void setIsBlocked(boolean b)
    {
        if (b == true)
            setIsEmpty(true);
        isBlocked = b;
    }
    
    public boolean isBlocked()
    {
        return isBlocked;
    }
    
    public void setIsBreezy(boolean b)
    {
        if(b == true)
            setIsEmpty(true);
        isBreezy = b;
    }
    
    public boolean isBreezy()
    {
        return isBreezy;
    }
    
    public void setIsPit(boolean p)
    {
        if(p == true)
            setIsEmpty(false);
        isPit = p;
    }
    
    public boolean isPit()
    {
        return isPit;
    }
    
    public void setIsShiny(boolean s)
    {
        if(s == true)
            setIsEmpty(false);
        isShiny = s;
    }
    
    public boolean isShiny()
    {
        return isShiny;
    }
    
    public void setIsSmelly(boolean s)
    {
        if(s == true)
            setIsEmpty(true);
        isSmelly = s;
    }
    
    public boolean isSmelly()
    {
        return isSmelly;
    }
    
    public void setIsWumpus(boolean w)
    {
        if(w == true)
            setIsEmpty(false);
        isWumpus = w;
    }
    
    public boolean isWumpus()
    {
        return isWumpus;
    }
	
	public void setToSafe()
	{
		isSafe = true; 
	}
	
	public boolean isSafe()
	{
		return isSafe; 
	}
}
