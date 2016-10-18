package WumpusWorld;

import java.util.ArrayList;

/**
 *
 */
public class KnowledgeBasedAgent
{

    private enum Direction
    {
        LEFT, RIGHT, UP, DOWN
    }
    private Direction currentDirection;

    /**
     * Resources
     */
    private int score;     // agent score
    private int arrows;    // number of arrows
    private int actions;   // how many actions taken so far
    private int moves = 0; // number of allowed moves (for testing at this point)
    private int turn = 0;  // which turn the agent is on

    /**
     * World and Room states
     */
    private Room currentRoom;        // the current room the agent is in
    private World actualWorld; // the actual World generated 
    private World perceivedWorld;    // what the agent has learned about the world

    public KnowledgeBasedAgent()
    {
    }

    public void takePath(ArrayList<Room> path)
    {
        for (Room room : path)
        {
            if (room != currentRoom)
            {
                //moving left
                if (room.getRoomColumn() < currentRoom.getRoomColumn())
                {
                    if (currentDirection == Direction.UP || currentDirection == Direction.DOWN)
                    {
                        score -= 2; 
                    } else if (currentDirection == Direction.RIGHT)
                    {
                        score -= 3; 
                    } else
                    {
                        score -= 1; 
                    }
                    currentDirection = Direction.LEFT; 
                //moving right 
                } else if (room.getRoomColumn() > currentRoom.getRoomColumn())
                {
                    if (currentDirection == Direction.UP || currentDirection == Direction.DOWN)
                    {
                        score -= 2; 
                    } else if (currentDirection == Direction.LEFT)
                    {
                        score -= 3; 
                    } else
                    {
                        score -= 1; 
                    }
                    currentDirection = Direction.RIGHT; 
                //moving down
                } else if (room.getRoomRow() < currentRoom.getRoomRow())
                {
                    if (currentDirection == Direction.LEFT || currentDirection == Direction.RIGHT)
                    {
                        score -= 2; 
                    } else if (currentDirection == Direction.UP)
                    {
                        score -= 3; 
                    } else
                    {
                        score -= 1; 
                    }
                    currentDirection = Direction.DOWN; 
                //moving up
                } else if (room.getRoomRow() > currentRoom.getRoomRow())
                {
                    if (currentDirection == Direction.LEFT || currentDirection == Direction.RIGHT)
                    {
                        score -= 2; 
                    } else if (currentDirection == Direction.DOWN)
                    {
                        score -= 3; 
                    } else
                    {
                        score -= 1; 
                    }
                    currentDirection = Direction.UP; 
                }
                currentRoom = room; 
            }
        }
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
                    if ((i >= 0 && i <= perceivedWorld.getSize()) && (j >= 0 && j <= perceivedWorld.getSize()) && (Math.abs(i) != Math.abs(j)) && (i != current.getRoomColumn() && j != current.getRoomRow()))
                    {
                        Room nextRoom = perceivedWorld.getRoom(i, j);
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
                    if (currentY + 1 <= perceivedWorld.getSize())
                    {
                        nextRoom = perceivedWorld.getRoom(currentX, currentY + 1);
                        if (nextRoom.isSafe())
                        {
                            possiblePath = getPath(nextRoom, finish, path);
                        }
                    }
                } else if (currentY - 1 >= 0)
                {
                    {
                        nextRoom = perceivedWorld.getRoom(currentX, currentY - 1);
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
                    if (currentX + 1 <= perceivedWorld.getSize())
                    {
                        nextRoom = perceivedWorld.getRoom(currentX + 1, currentY);
                        if (nextRoom.isSafe())
                        {
                            possiblePath = getPath(nextRoom, finish, path);
                        }
                    }
                } else if (currentX - 1 >= 0)
                {
                    nextRoom = perceivedWorld.getRoom(currentX - 1, currentY);
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
