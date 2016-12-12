/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package racetrack;

import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Random;

public class Racetrack
{
    private final String name;
    private final Cell[][] track;
    private Cell currentCarLoc;
    private Cell lastCarLoc;
    private ArrayList<Cell> start = new ArrayList();
    private ArrayList<Cell> finish = new ArrayList();
    private boolean finished = false;
    private int moves;
    public Racetrack(int r, int c, String in_name)
    {
        track = new Cell[r][c];
        name = in_name;
    }

    /**
     *
     * @param r
     * @param c
     * @param t
     */
    public void setTrack(int r, int c, char t)
    {
        Cell newCell = new Cell(r, c, t);
        track[r][c] = newCell;
        if (t == 'S')
        {
            start.add(newCell);
        } else if (t == 'F')
        {
            finish.add(newCell);
        }
    }

    /**
     *
     */
    public void printTrack()
    {
        int rowLength = track.length;
        int colLength = track[0].length;

        for (int r = 0; r < rowLength; r++)
        {
            for (int c = 0; c < colLength; c++)
            {
                System.out.print(track[r][c].getType());
            }
            System.out.println();
        }
    }

    public void printTrack(RaceCar car)
    {
        int rowLength = track.length;
        int colLength = track[0].length;

        for (int r = 0; r < rowLength; r++)
        {
            for (int c = 0; c < colLength; c++)
            {
                if (r == car.getLocation().getRow() && c == car.getLocation().getCol())
                {
                    System.out.print("C");
                } else
                {
                    System.out.print(track[r][c].getType());
                }
            }
            System.out.println();
        }
    }

    /**
     * Prints track and location without needing a race car instance
     */
    public void printTrackWithAgentLocation(int row, int col)
    {
        int rowLength = track.length;
        int colLength = track[0].length;

        for (int r = 0; r < rowLength; r++)
        {
            for (int c = 0; c < colLength; c++)
            {
                if (r == row && c == col)
                {
                    System.out.print('O');
                }
                else
                {
                    System.out.print(track[r][c].getType());
                }
            }
            System.out.println();
        }
    }
    
    public int getNumMoves()
    {
        return moves;
    }

    public void getCrashLocations()
    {

    }

    public void getTrackHeatmap()
    {

    }

    /**
     * @param resetOnCollision 
     */
    public void run(boolean resetOnCollision)
    {
        moves = 0;
        Random rand = new Random();
        RaceCar car = new RaceCar(start.get(rand.nextInt(start.size())), track);
        lastCarLoc = car.getLocation();
        currentCarLoc = lastCarLoc;
        while (!finished)
        {
            //attempt to apply acceleration to get to next cell
            car.accelerate(nextCell(car));
            currentCarLoc = car.getLocation();
            if (currentCarLoc == lastCarLoc || collisionCheck(lastCarLoc, currentCarLoc))
            {
                if (resetOnCollision)
                {
                    car.reset(start.get(rand.nextInt(start.size())));
                } else
                {
                    car.reset(lastCarLoc);
                }
            }
            lastCarLoc = car.getLocation();
            printTrack(car);
            moves++;
        }
        System.out.println("Moves: " + moves);
    }

    /**
     *
     * @param car
     * @return
     */
    private Cell nextCell(RaceCar car)
    {
        double maxUtil = -100;
        Cell nextCell = car.getLocation();

        for (int x = -1; x < 2; x++)
        {
            for (int y = -1; y < 2; y++)
            {
                int nextC = car.getLocation().getCol() + car.getYVelocity() + y;
                int nextR = car.getLocation().getRow() + car.getXVelocity() + x;
                if (car.getXVelocity() + x <= 5 && car.getXVelocity() + x >= -5 && car.getYVelocity() + y <= 5 && car.getYVelocity() + y >= -5)
                {
                    if (withinBounds(nextR, nextC) && !(nextR == car.getLocation().getRow() && nextC == car.getLocation().getCol())
                            && track[nextR][nextC].getUtilities()[car.getXVelocity() + x + 5][car.getYVelocity() + y + 5] > maxUtil)
                    {
                        maxUtil = track[nextR][nextC].getUtilities()[car.getXVelocity() + x + 5][car.getYVelocity() + y + 5];
                        nextCell = track[nextR][nextC];
                    }
                }
            }
        }
        return nextCell;
    }

    /**
     * Check if the car has crossed the finish line
     *
     * @return
     */
    private boolean finishLineCheck()
    {
        Line2D finishLine1 = new Line2D.Double(finish.get(0).getRow(), finish.get(0).getCol(),
                finish.get(finish.size() - 1).getRow(), finish.get(finish.size() - 1).getCol() + 1);
        Line2D finishLine2 = new Line2D.Double(finish.get(0).getRow() + 1, finish.get(0).getCol(),
                finish.get(finish.size() - 1).getRow() + 1, finish.get(finish.size() - 1).getCol() + 1);
        Line2D path = new Line2D.Double(currentCarLoc.getRow(), currentCarLoc.getCol(), lastCarLoc.getRow(), lastCarLoc.getCol());
        return (path.intersectsLine(finishLine1) || path.intersectsLine(finishLine2) || currentCarLoc.getType() == 'F');
    }

    /**
     * Check if wall collision occurs and whether collision is before or after
     * finish line
     *
     * @return
     */
    private boolean collisionCheck(Cell point1, Cell point2)
    {
        Line2D path = new Line2D.Double(point2.getRow(), point2.getCol(), point1.getRow(), point1.getCol());
        int minR = Math.min(point2.getRow(), point1.getRow());
        int maxR = Math.max(point2.getRow(), point1.getRow());
        int minC = Math.min(point2.getCol(), point1.getCol());
        int maxC = Math.max(point2.getCol(), point1.getCol());
        double maxDiff = 0;
        boolean collision = false;
        Cell resetCell = lastCarLoc;
        if (finishLineCheck())
        {
            finished = true;
        }
        for (int i = minR; i <= maxR; i++)
        {
            for (int j = minC; j <= maxC; j++)
            {
                Line2D edge1 = new Line2D.Double(i, j, i, j + 0.95);
                Line2D edge2 = new Line2D.Double(i, j, i + 0.95, j);
                Line2D edge3 = new Line2D.Double(i + 0.95, j, i + 0.95, j + 0.95);
                Line2D edge4 = new Line2D.Double(i + 0.95, j + 0.95, i, j + 0.95);
                if (path.intersectsLine(edge1) || path.intersectsLine(edge2) || path.intersectsLine(edge3) || path.intersectsLine(edge4))
                {
                    if (track[i][j].getType() == '#')
                    {
                        int finishDist = Math.abs(point1.getRow() - finish.get(0).getRow());
                        int collisionDist = Math.abs(point1.getRow() - i);
                        if (!finished || (finished && finishDist > collisionDist))
                        {
                            finished = false;
                            collision = true;
                        }
                    } else
                    {
                        double rDiff = Math.pow(point1.getRow() - i, 2);
                        double cDiff = Math.pow(point1.getCol() - j, 2);
                        double totalDiff = Math.sqrt(rDiff + cDiff);
                        if (totalDiff > maxDiff)
                        {
                            maxDiff = totalDiff;
                            resetCell = track[i][j];
                        }
                    }
                }
            }
        }
        if (collision)
        {
            System.out.println("Collision");
            lastCarLoc = resetCell; 
        }
        return collision;
    }

    /**
     * Make sure given row column are within track boundaries
     *
     * @param row
     * @param col
     * @return
     */
    private boolean withinBounds(int row, int col)
    {
        return (row >= 0 && row < track.length && col >= 0 && col < track[0].length);
    }

    /**
     *
     * @return
     */
    public Cell[][] getTrack()
    {
        return track;
    }

    /**
     * Print the track with utilities shown instead of char values for a
     * specified velocity vector
     *
     * @param rowVel : value of row-component velocity. Add 5 for correct index
     * @param colVel : value of col-component velocity. Add 5 for correct index
     */
    public void printTrackWithUtilities(int rowVel, int colVel)
    {
        int rowVelIndex = rowVel + 5;
        int colVelIndex = colVel + 5;
        for (int i = 0; i < track.length; i++)
        {
            for (int j = 0; j < track[i].length; j++)
            {
                System.out.format("(%c)%8.3f ", track[i][j].getType(), track[i][j].getUtilities()[rowVelIndex][colVelIndex]);
            }
            System.out.println();
        }
    }

    /**
     * @return a list of the [row,col] indices of each cell with a 'S' value 
     */
    public ArrayList<int[]> getRacetrackStartIndices()
    {
        ArrayList<int[]> startLocations = new ArrayList<>();
        
        for (int i = 0; i < track.length; i++)
        {
            for (int j = 0; j < track[i].length; j++)
            {
                if (track[i][j].getType() == 'S')
                    startLocations.add(new int[]{i,j});
            }
        }
        
        return startLocations;
    }
    
    /**
     * Get this track's name. Will be either 'R', 'L', 'O', or 'simple'
     * @return 
     */
    public String getName() 
    {
        return name;
    }
}
