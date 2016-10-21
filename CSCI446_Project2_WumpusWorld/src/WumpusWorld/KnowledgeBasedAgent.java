package WumpusWorld;

import java.util.ArrayList;
import java.util.Random;

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
    private ArrayList<Room> safeRooms = new ArrayList<Room>();
    private ArrayList<Room> pits = new ArrayList<Room>();
    private ArrayList<Room> wumpi = new ArrayList<Room>();
    private ArrayList<Room> obstacles = new ArrayList<Room>(); 

    public KnowledgeBasedAgent(World perceived, World actual)
    {
        perceivedWorld = perceived;
        actualWorld = actual;
        currentRoom = perceivedWorld.getRoom(0, 0);
        currentDirection = Direction.RIGHT;
    }

    /**
     *
     */
    public void explore()
    {
        do
        {
            currentRoom.setVisited();
            Room lastRoom = currentRoom;
            System.out.println("Current Room: " + currentRoom.getRoomRow() + ", " + currentRoom.getRoomColumn());
            updatePercepts();
            takePath(getPath(currentRoom, getNextRoom()));
            if (die() || obstacle())
            {
                currentRoom = lastRoom;
            }
            System.out.println("Score: " + score + "\n");
        } while (goldNotFound());
        System.out.format("Gold found in room: %d, %d%n%n", currentRoom.getRoomRow(), currentRoom.getRoomColumn());
        System.out.println("Score: " + score);
    }

    /**
     *
     */
    public void updatePercepts()
    {
//        for (int i = currentRoom.getRoomRow() - 1; i <= currentRoom.getRoomRow() + 1; i++)
//        {
//            for (int j = currentRoom.getRoomColumn() - 1; j <= currentRoom.getRoomColumn() + 1; j++)
//            {
//                Room current = actualWorld.getRoom(i, j);
//                if (current != null && !current.isBreezy() && !current.isSmelly())
//                {
//                    currentRoom.setToSafe();
//                    safeRooms.add(current);
//                    setSurroundingExplorable(current);
//                }
//            }
//        }
        int row = currentRoom.getRoomRow();
        int column = currentRoom.getRoomColumn();
        if (!actualWorld.getRoom(row, column).isBreezy() && !actualWorld.getRoom(row, column).isSmelly())
        {
            currentRoom.setToSafe();
            if (!safeRooms.contains(currentRoom))
            {
                safeRooms.add(currentRoom);
            }
            setSurroundingExplorable(currentRoom);
        } else if (actualWorld.getRoom(row, column).isBreezy())
        {
            currentRoom.setIsBreezy(true);
            System.out.println("Adding breeze(" + row + "," + column + ") to knowledge base");
        } else if (actualWorld.getRoom(row, column).isSmelly())
        {
            currentRoom.setIsSmelly(true);
            System.out.println("Adding smelly(" + row + "," + column + ") to knowledge base");
        }

    }

    /**
     *
     * @return
     */
    public Room getNextRoom()
    {
        Room nextRoom = null;
        int row = currentRoom.getRoomRow();
        int column = currentRoom.getRoomColumn();
        if (currentRoom.isSafe())
        {
            System.out.println("safe");
            switch (currentDirection)
            {
                case RIGHT:
                    nextRoom = perceivedWorld.getRoom(row, column + 1);
                    break;
                case LEFT:
                    nextRoom = perceivedWorld.getRoom(row, column - 1);
                    break;
                case UP:
                    nextRoom = perceivedWorld.getRoom(row + 1, column);
                    break;
                case DOWN:
                    nextRoom = perceivedWorld.getRoom(row - 1, column);
                    break;
            }
        }
        if (nextRoom == null)
        {
            nextRoom = findLeastExplored();
            if (nextRoom != currentRoom)
            {
                for (int i = nextRoom.getRoomRow() - 1; i <= nextRoom.getRoomRow() + 1; i++)
                {
                    for (int j = nextRoom.getRoomColumn() - 1; j <= nextRoom.getRoomColumn() + 1; j++)
                    {
                        if (perceivedWorld.getRoom(i, j) != null && notDiagonal(nextRoom, i, j) && !perceivedWorld.getRoom(i, j).isSafe() && perceivedWorld.getRoom(i, j) != currentRoom && !perceivedWorld.getRoom(i, j).isBlocked())
                        {
                            nextRoom = perceivedWorld.getRoom(i, j);
                            nextRoom.setExplorable();
                            System.out.println("Action: move(" + nextRoom.getRoomRow() + "," + nextRoom.getRoomColumn() + ")");
                            return nextRoom;
                        }
                    }
                }
            } else
            {
                nextRoom = makeHardDecisions();
                if (nextRoom == currentRoom)
                {
                    nextRoom = crossFingers();
                } else
                {
                    nextRoom.setExplorable();
                    System.out.println("Action: move(" + nextRoom.getRoomRow() + "," + nextRoom.getRoomColumn() + ")");
                    return nextRoom;
                }
            }
        } else
        {
            nextRoom.setExplorable();
            System.out.println("Action: move(" + nextRoom.getRoomRow() + "," + nextRoom.getRoomColumn() + ")");
            return nextRoom;
        }
        return null;
    }

    /**
     *
     * @return
     */
    public Room findLeastExplored()
    {
        int mostExplorables = 0;
        Room leastExplored = currentRoom;
        for (Room r : safeRooms)
        {
            int surroundingUnexplored = 0;
            for (int i = r.getRoomRow() - 1; i <= r.getRoomRow() + 1; i++)
            {
                for (int j = r.getRoomColumn() - 1; j <= r.getRoomColumn() + 1; j++)
                {
                    if (perceivedWorld.getRoom(i, j) != null && notDiagonal(r, i, j) && !perceivedWorld.getRoom(i, j).isSafe() && !perceivedWorld.getRoom(i, j).getVisited())
                    {
                        surroundingUnexplored++;
                    }
                }
            }
            if (surroundingUnexplored != 0 && surroundingUnexplored >= mostExplorables)
            {
                leastExplored = r;
                mostExplorables = surroundingUnexplored;
            }
        }
        return leastExplored;
    }

    /**
     * 
     * @param room 
     */
    public void setSurroundingExplorable(Room room)
    {
        for (int i = room.getRoomRow() - 1; i <= room.getRoomRow() + 1; i++)
        {
            for (int j = room.getRoomColumn() - 1; j <= room.getRoomColumn() + 1; j++)
            {
                if (perceivedWorld.getRoom(i, j) != null && notDiagonal(room, i, j))
                {
                    perceivedWorld.getRoom(i, j).setExplorable();
                }
            }
        }
    }

    /**
     * 
     * @return 
     */
    public Room crossFingers()
    {
        ArrayList<Room> possible = new ArrayList<Room>(); 
        int row = currentRoom.getRoomRow();
        int column = currentRoom.getRoomColumn();
        for (int i = row - 1; i <= row + 1; i++)
        {
            for (int j = column - 1; j <= column + 1; j++)
            {
                Room gamble = perceivedWorld.getRoom(i, j);
                if (gamble != null && !(row == i && column == j))
                {
                    if (!pits.contains(gamble) && !wumpi.contains(gamble))
                    {
                        possible.add(gamble);
                    }
                }
            }
        }
        Random r = new Random(); 
        int index = r.nextInt(possible.size()); 
        return possible.get(index);
        
    }
    
    

    
    /**
     * 
     * @return 
     */
    public Room makeHardDecisions()
    {
        int numDangerous = 4;
        Room nextRoom = null;
        for (int i = currentRoom.getRoomRow() - 1; i <= currentRoom.getRoomRow() + 1; i++)
        {
            for (int j = currentRoom.getRoomColumn() - 1; j <= currentRoom.getRoomColumn() + 1; j++)
            {
                if (perceivedWorld.getRoom(i, j) != null && notDiagonal(currentRoom, i, j))
                {
                    if (!pits.contains(perceivedWorld.getRoom(i, j)) && !wumpi.contains(perceivedWorld.getRoom(i, j)) && !obstacles.contains(perceivedWorld.getRoom(i, j)))
                    {
                        Room checkRoom = perceivedWorld.getRoom(i, j);
                        int danger = 0;
                        for (int k = checkRoom.getRoomRow() - 1; k <= checkRoom.getRoomRow() + 1; k++)
                        {
                            for (int l = checkRoom.getRoomColumn() - 1; l <= checkRoom.getRoomColumn() + 1; l++)
                            {
                                if (perceivedWorld.getRoom(k, l) != null && (perceivedWorld.getRoom(k, l).isSmelly() || perceivedWorld.getRoom(k, l).isBreezy()))
                                {
                                    danger++;
                                }

                            }
                        }
                        if (danger <= numDangerous)
                        {
                            nextRoom = checkRoom;
                            numDangerous = danger;
                        }
                    }
                }
            }
        }
        return nextRoom;
    }

    /**
     * Move through safe room path and subtract for each move
     *
     * @param path List of path of rooms to take
     */
    public void takePath(ArrayList<Room> path)
    {
        if (path != null)
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
    }

    /**
     * Helper method for calling recursive
     *
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
     *
     * @param current
     * @param finish
     * @param path List of safe rooms in path to goal
     * @return
     */
    private ArrayList<Room> getPath(Room current, Room finish, ArrayList<Room> path)
    {
        path.add(current);
        if (current.getRoomRow() == finish.getRoomRow() && current.getRoomColumn() == finish.getRoomColumn())
        {
            //path found
            return path;
        }
        //agent not already traveling in a particular direction 
        if (path.size() >= 0)
        {
            //iterate through adjacent rooms
            for (int i = current.getRoomRow() - 1; i <= current.getRoomRow() + 1; i++)
            {
                for (int j = current.getRoomColumn() - 1; j <= current.getRoomColumn() + 1; j++)
                {
                    //look at each room within the world size that is adjacent to current 
                    if ((i >= 0 && i < perceivedWorld.getSize()) && (j >= 0 && j < perceivedWorld.getSize()) && notDiagonal(current, i, j) && !path.contains(perceivedWorld.getRoom(i, j)))
                    {
                        Room nextRoom = perceivedWorld.getRoom(i, j);
                        if (nextRoom.isSafe() || nextRoom.isExplorable())
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
                    if (currentRow + 1 < perceivedWorld.getSize())
                    {
                        nextRoom = perceivedWorld.getRoom(currentRow + 1, currentColumn);
                        if (nextRoom.isSafe() || nextRoom.isExplorable())
                        {
                            possiblePath = getPath(nextRoom, finish, path);
                        }
                    }
                    //moving down
                } else if (currentRow - 1 >= 0)
                {
                    {
                        nextRoom = perceivedWorld.getRoom(currentRow - 1, currentColumn);
                        if (nextRoom.isSafe() || nextRoom.isExplorable())
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
                    if (currentColumn + 1 < perceivedWorld.getSize())
                    {
                        nextRoom = perceivedWorld.getRoom(currentRow, currentColumn + 1);
                        if (nextRoom.isSafe() || nextRoom.isExplorable())
                        {
                            possiblePath = getPath(nextRoom, finish, path);
                        }
                    }
                    //moving left
                } else if (currentColumn - 1 >= 0)
                {
                    nextRoom = perceivedWorld.getRoom(currentRow, currentColumn - 1);
                    if (nextRoom.isSafe() || nextRoom.isExplorable())
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
                        if ((i >= 0 && i < perceivedWorld.getSize()) && (j >= 0 && j < perceivedWorld.getSize()) && notDiagonal(current, i, j) && !path.contains(perceivedWorld.getRoom(i, j)))
                        {
                            Room nextRoom = perceivedWorld.getRoom(i, j);
                            if (nextRoom.isSafe() || nextRoom.isExplorable())
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
     *
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

    /**
     *
     * @return
     */
    public boolean goldNotFound()
    {
        int row = currentRoom.getRoomRow();
        int column = currentRoom.getRoomColumn();
        if (actualWorld.getRoom(row, column).isShiny())
        {
            score += 1000;
            return false;
        }
        return true;
    }

    /**
     * 
     * @return 
     */
    public boolean die()
    {
        int row = currentRoom.getRoomRow();
        int column = currentRoom.getRoomColumn();
        if (actualWorld.getRoom(row, column).isPit())
        {
            pits.add(perceivedWorld.getRoom(row, column));
            System.out.println("Death by Pit in room " + currentRoom.getRoomRow() + "," + currentRoom.getRoomColumn());
            System.out.println("Adding pit(" + row + "," + column + ") to knowledge base");
            score -= 1000;
            return true;
        } else if (actualWorld.getRoom(row, column).isWumpus())
        {
            wumpi.add(perceivedWorld.getRoom(row, column));
            System.out.println("Death by Wumpus in room " + currentRoom.getRoomRow() + "," + currentRoom.getRoomColumn());
            System.out.println("Adding wumpus(" + row + "," + column + ") to knowledge base");
            score -= 1000;
            return true;
        }
        return false;
    }
    
    /**
     * 
     * @return 
     */
    public boolean obstacle() 
    {
        int row = currentRoom.getRoomRow();
        int column = currentRoom.getRoomColumn();
        if (actualWorld.getRoom(row, column).isBlocked()) 
        {
            obstacles.add(perceivedWorld.getRoom(row, column));
            System.out.println("Adding blocked(" + row + "," + column + ") to knowledge base");
            return true; 
        }
        return false; 
    }
}
