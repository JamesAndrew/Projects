package WumpusWorld;

import java.util.ArrayList;

/**
 * The grid is made of cells that can be traveled to.
 * Each cell property must be a boolean value
 * @version 09/27/16
 */
public class Room 
{
    private boolean isBlocked;
    private boolean isPit;
    private boolean isBreezy;
    private boolean isShiny;        // goal state if this is true
    private boolean exists;
    private boolean isSmelly;
    private boolean isWumpus;
    private boolean isExplored;     // initialized to false?
    private boolean isSafe;
    
    private boolean hasGold;
    private boolean hasObst;
    
    private final int roomRow;
    private final int roomColumn;
    
    public Room(int row, int col)
    {
        roomRow = row;
        roomColumn = col;
        
        // initiall all rooms are empty (not smelly, not breezy, etc)
        isExplored  = false;
        isBreezy    = false;
        isSmelly    = false;
        isBlocked   = false;
        isShiny     = false;
        isWumpus    = false;
        isPit       = false;
        isShiny     = false;
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
     * @return an atom list each attribute the agent cares about
     */
    public ArrayList<KBAtom> returnRoomAttributes()
    {
        ArrayList<KBAtom> attributes = new ArrayList<>();
        
        attributes.add(new KBAtomConstant(!isBlocked, "BLOCKED", this));
        attributes.add(new KBAtomConstant(!isBreezy, "WINDY", this));
        attributes.add(new KBAtomConstant(!isShiny, "SHINY", this));
        attributes.add(new KBAtomConstant(!isSmelly, "SMELLY", this));
        attributes.add(new KBAtomConstant(!isExplored, "EXPLORED", this));
        
        return attributes;
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
    public boolean isExists() 
    {
        return exists;
    }
    public void setExists(boolean exists) 
    {
        this.exists = exists;
    }
    public boolean isIsSafe() 
    {
        return isSafe;
    }
    public void setIsSafe(boolean isSafe) 
    {
        this.isSafe = isSafe;
    }
    
    @Override
    public String toString()
    {
        String value = "(" + roomRow + ", " + roomColumn + ")";
        return value;
    }
}