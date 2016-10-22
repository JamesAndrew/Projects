package WumpusWorld;

/**
 * The grid is made of cells that can be traveled to.
 * Each cell property must be a boolean value
 * @version 09/27/16
 */
public class Room 
{
    private boolean exists;
    
    private boolean isBlocked;
    private boolean isPit;
    private boolean isBreezy;
    private boolean isShiny;        // goal state if this is true
    private boolean isSmelly;
    private boolean isWumpus;
    private boolean isSafe;
    private boolean isExplored;     // initialized to false
    
    private boolean hasGold;
    private boolean hasObst;
    
    private final int roomRow;
    private final int roomColumn;
    
    public Room(int row, int col)
    {
        roomRow = row;
        roomColumn = col;
//        this.isExplored = false;
    }
    /**
     * used to create a fake room that doesn't exist when the logic goes out of
     * bounds
     * @param existsValue 
     */
    public Room(boolean existsValue)
    {
        exists = existsValue;
        roomRow = -1;
        roomColumn = -1;
    }
    /**
     * Easily set all states of the current room
     */
    public void setAllProperties(boolean blocked, boolean pit,
        boolean breezy, boolean shiny, boolean smelly, boolean wumpus)
    {
        this.isBlocked = blocked;
        this.isPit = pit;
        this.isBreezy = breezy;
        this.isShiny = shiny;
        this.isSmelly = smelly;
        this.isWumpus = wumpus;
    }
    
    public int getRoomRow()
    {
        return roomRow;
    }
    
    public int getRoomColumn()
    {
        return roomColumn;
    }
    
    public void setIsBlocked(boolean b)
    {
        isBlocked = b;
    }
    
    public boolean isBlocked()
    {
        return isBlocked;
    }
    
    public void setIsBreezy(boolean b)
    {
        isBreezy = b;
    }
    
    public boolean isBreezy()
    {
        return isBreezy;
    }
    
    public void setIsPit(boolean p)
    {
        isPit = p;
    }
    
    public boolean isPit()
    {
        return isPit;
    }
    
    public void setIsShiny(boolean s)
    {
        isShiny = s;
    }
    
    public boolean isShiny()
    {
        return isShiny;
    }
    
    public void setIsSmelly(boolean s)
    {
        isSmelly = s;
    }
    
    public boolean isSmelly()
    {
        return isSmelly;
    }
    
    public void setIsWumpus(boolean w)
    {
        isWumpus = w;
    }
    
    public boolean isWumpus()
    {
        return isWumpus;
    }
    
    public boolean isHasGold() 
    {
        return hasGold;
    }
    

    public boolean isHasObst() 
    {
        return hasObst;
    }
    
    public boolean isIsSafe() {
        return isSafe;
    }

    public boolean isExists() 
    {
        return exists;
    }
    public void setExists(boolean exists) 
    {
        this.exists = exists;
    }
    public void setHasGold(boolean hasGold) 
    {
        this.hasGold = hasGold;
    }
    public boolean isIsExplored() 
    {
        return isExplored;
    }
    public void setIsExplored(boolean isExplored) 
    {
        this.isExplored = isExplored;
    }
}
