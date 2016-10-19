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
    private int score = 0;     // agent score
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

    public KnowledgeBasedAgent(World world)
    {
        perceivedWorld = world;
        currentRoom = perceivedWorld.getRoom(0, 0);
    }

    /**
     * Move through safe room path and subtract for each move 
     * @param path List of path of rooms to take 
     */
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
        System.out.println(score);
    }

    /**
     * Helper method for calling recursive 
     * @param start Start room 
     * @param finish Goal room 
     * @return 
     */
    public ArrayList<Room> getPath(Room start, Room finish)
    {
        ArrayList<Room> path = new ArrayList<Room>();
        return getPath(start, finish, path);
    }

    /**
     * Find path from current room to goal room using only safe rooms
     * @param current
     * @param finish
     * @param path List of safe rooms in path to goal 
     * @return 
     */
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
            //iterate through adjacent rooms
            for (int i = current.getRoomRow() - 1; i <= current.getRoomRow() + 1; i++)
            {
                for (int j = current.getRoomColumn() - 1; j <= current.getRoomColumn() + 1; j++)
                {
                    //look at each room within the world size that is adjacent to current 
                    if ((i >= 0 && i <= perceivedWorld.getSize()) && (j >= 0 && j <= perceivedWorld.getSize()) && notDiagonal(current, i, j) && !path.contains(perceivedWorld.getRoom(i, j)))
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
            int previousColumn = path.get(path.size() - 2).getRoomColumn();
            int previousRow = path.get(path.size() - 2).getRoomRow();
            int currentColumn = current.getRoomColumn();
            int currentRow = current.getRoomRow();
            ArrayList<Room> possiblePath = null;

            if (previousColumn == currentColumn)
            {
                Room nextRoom;
                //moving up
                if (previousRow < currentRow)
                {
                    if (currentRow + 1 <= perceivedWorld.getSize())
                    {
                        nextRoom = perceivedWorld.getRoom(currentRow + 1, currentColumn);
                        if (nextRoom.isSafe())
                        {
                            possiblePath = getPath(nextRoom, finish, path);
                        }
                    }
                    //moving down
                } else if (currentRow - 1 >= 0)
                {
                    {
                        nextRoom = perceivedWorld.getRoom(currentRow - 1, currentColumn);
                        if (nextRoom.isSafe())
                        {
                            possiblePath = getPath(nextRoom, finish, path);
                        }
                    }
                }
            } else if (previousRow == currentRow)
            {
                Room nextRoom;
                //moving right
                if (previousColumn < currentColumn)
                {
                    if (currentColumn + 1 <= perceivedWorld.getSize())
                    {
                        nextRoom = perceivedWorld.getRoom(currentRow, currentColumn + 1);
                        if (nextRoom.isSafe())
                        {
                            possiblePath = getPath(nextRoom, finish, path);
                        }
                    }
                    //moving left
                } else if (currentColumn - 1 >= 0)
                {
                    nextRoom = perceivedWorld.getRoom(currentRow, currentColumn - 1);
                    if (nextRoom.isSafe())
                    {
                        possiblePath = getPath(nextRoom, finish, path);
                    }
                }
            }
            if (possiblePath != null)
            {
                return possiblePath;
            } else
            {
                //iterate through all adjacent 
                for (int i = current.getRoomRow() - 1; i <= current.getRoomRow() + 1; i++)
                {
                    for (int j = current.getRoomColumn() - 1; j <= current.getRoomColumn() + 1; j++)
                    {
                        //look at each room within the world size that is adjacent to current 
                        if ((i >= 0 && i <= perceivedWorld.getSize()) && (j >= 0 && j <= perceivedWorld.getSize()) && notDiagonal(current, i, j) && !path.contains(perceivedWorld.getRoom(i, j)))
                        {
                            Room nextRoom = perceivedWorld.getRoom(i, j);
                            if (nextRoom.isSafe())
                            {
                                possiblePath = getPath(nextRoom, finish, path);
                                if (possiblePath != null)
                                {
                                    return possiblePath;
                                }
                            }
                        }
                    }
                }
            }
        }
        //path not found from this room 
        path.remove(path.size() - 1);
        return null;
    }

    /**
     * Check if room is at a diagonal to current room 
     * @param current current room 
     * @param i row to check 
     * @param j column to check 
     * @return 
     */
    public boolean notDiagonal(Room current, int i, int j)
    {
        if (current.getRoomRow() - 1 == i || current.getRoomRow() + 1 == i)
        {
            if (current.getRoomColumn() - 1 == j || current.getRoomColumn() + 1 == j)
            {
                return false;
            }
        }
        return true;
    }

}
