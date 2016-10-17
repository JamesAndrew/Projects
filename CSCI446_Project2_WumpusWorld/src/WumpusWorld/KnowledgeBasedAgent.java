package WumpusWorld;

import java.util.ArrayList;

/**
 *
 */
public class KnowledgeBasedAgent
{

    private World world;

    public KnowledgeBasedAgent()
    {
    }

    public ArrayList<Room> getPath(Room start, Room finish)
    {
        ArrayList<Room> path = new ArrayList<Room>();
        return getPath(start, finish, path);
    }

    private ArrayList<Room> getPath(Room current, Room finish, ArrayList<Room> path)
    {
        path.add(current);
        if (current == finish)
        {
            //path found
            return path;
        }
        //agent not already traveling in a particular direction 
        if (path.size() == 1)
        {
            for (int i = current.getRoomColumn() - 1; i <= current.getRoomColumn() + 1; i++)
            {
                for (int j = current.getRoomRow() - 1; j <= current.getRoomRow() + 1; j++)
                {
                    //look at each room within the world size that is adjacent to current 
                    if ((i >= 0 && i <= world.getSize()) && (j >= 0 && j <= world.getSize()) && (Math.abs(i) != Math.abs(j)) && (i != current.getRoomColumn() && j != current.getRoomRow()))
                    {
                        Room nextRoom = world.getRoom(i, j);
                        if (nextRoom.isSafe())
                        {
                            ArrayList<Room> possiblePath = getPath(nextRoom, finish, path);
                            if (possiblePath != null)
                            {
                                return possiblePath;
                            }
                        }
                    }
                }
            }
        } else
        {
            //try to continue in the direction that the agent was already traveling in
            int previousX = path.get(-1).getRoomColumn();
            int previousY = path.get(-1).getRoomRow();
            int currentX = current.getRoomColumn();
            int currentY = current.getRoomRow();
            ArrayList<Room> possiblePath = null;

            if (previousX == currentX)
            {
                Room nextRoom;
                if (previousY < currentY)
                {
                    if (currentY + 1 <= world.getSize())
                    {
                        nextRoom = world.getRoom(currentX, currentY + 1);
                        if (nextRoom.isSafe())
                        {
                            possiblePath = getPath(nextRoom, finish, path);
                        }
                    }
                } else if (currentY - 1 >= 0)
                {                    
                    {
                        nextRoom = world.getRoom(currentX, currentY - 1);
                        if (nextRoom.isSafe())
                        {
                            possiblePath = getPath(nextRoom, finish, path);
                        }
                    }
                }
            } else if (previousY == currentY)
            {
                Room nextRoom;
                if (previousX < currentX)
                {
                    if (currentX + 1 <= world.getSize())
                    {
                        nextRoom = world.getRoom(currentX + 1, currentY);
                        if (nextRoom.isSafe())
                        {
                            possiblePath = getPath(nextRoom, finish, path);
                        }
                    }
                } else if (currentX - 1 >= 0)
                {
                    nextRoom = world.getRoom(currentX - 1, currentY);
                    if (nextRoom.isSafe())
                    {
                        possiblePath = getPath(nextRoom, finish, path);
                    }
                }
            }
            if (possiblePath != null)
            {
                return possiblePath;
            }
        }
        //path not found from this room 
        return null;
    }

}
