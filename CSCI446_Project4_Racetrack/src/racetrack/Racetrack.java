/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package racetrack;

import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author James
 */
public class Racetrack implements IRacetrack
{

    private Cell[][] track;
    private Cell currentCarLoc;
    private Cell lastCarLoc;
    private ArrayList<Cell> start = new ArrayList();
    private ArrayList<Cell> finish = new ArrayList();
    private boolean finished = false;
    private int moves;

    public Racetrack(int r, int c)
    {
        track = new Cell[r][c];
    }

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

    public void run(boolean resetOnCollision)
    {
        moves = 0;
        Random rand = new Random();
        RaceCar car = new RaceCar(start.get(rand.nextInt(start.size())));
        lastCarLoc = car.getLocation();
        currentCarLoc = lastCarLoc;
        while (!finished)
        {

        }
    }

    private Cell nextCell(RaceCar car)
    {
        double maxUtil = -100;
        Cell nextCell = currentCarLoc;

        for (int x = -1; x < 2; x++)
        {
            for (int y = -1; y < 2; y++)
            {
                int nextR = currentCarLoc.getRow() + car.getYVelocity() + y;
                int nextC = currentCarLoc.getCol() + car.getXVelocity() + x;
                if (car.getXVelocity() + x <= 5 && car.getXVelocity() + x >= -5 && car.getYVelocity() + y <= 5 && car.getYVelocity() + y >= -5)
                {
                    if (withinBounds(nextR, nextC) && !(nextR == currentCarLoc.getRow() && nextC == currentCarLoc.getCol())
                            && track[nextR][nextC].getUtility() > maxUtil)
                    {
                        maxUtil = track[nextR][nextC].getUtility();
                        nextCell = track[nextR][nextC];
                    }
                }
            }
        }
        return nextCell; 
    }

    private boolean finishLineCheck()
    {
        Line2D finishLine = new Line2D.Double(finish.get(0).getRow(), finish.get(0).getCol(),
                finish.get(finish.size() - 1).getRow(), finish.get(finish.size() - 1).getCol() + 1);
        Line2D path = new Line2D.Double(currentCarLoc.getRow(), currentCarLoc.getCol(), lastCarLoc.getRow(), lastCarLoc.getCol());
        if (path.intersectsLine(finishLine))
        {
            return true;
        }
        return false;
    }

    private boolean collisionCheck()
    {
        Line2D path = new Line2D.Double(currentCarLoc.getRow(), currentCarLoc.getCol(), lastCarLoc.getRow(), lastCarLoc.getCol());
        int minR = Math.min(currentCarLoc.getRow(), lastCarLoc.getRow());
        int maxR = Math.max(currentCarLoc.getRow(), lastCarLoc.getRow());
        int minC = Math.min(currentCarLoc.getCol(), lastCarLoc.getCol());
        int maxC = Math.max(currentCarLoc.getCol(), lastCarLoc.getCol());
        for (int i = minR; i <= maxR; i++)
        {
            for (int j = minC; j <= maxC; j++)
            {
                if (track[i][j].getType() == '#')
                {
                    Line2D edge1 = new Line2D.Double(track[i][j].getRow(), track[i][j].getCol(), track[i][j].getRow(), track[i][j].getCol() + 0.95);
                    Line2D edge2 = new Line2D.Double(track[i][j].getRow(), track[i][j].getCol(), track[i][j].getRow() + 0.95, track[i][j].getCol());
                    Line2D edge3 = new Line2D.Double(track[i][j].getRow() + 0.95, track[i][j].getCol(), track[i][j].getRow() + 0.95, track[i][j].getCol() + 0.95);
                    Line2D edge4 = new Line2D.Double(track[i][j].getRow() + 0.95, track[i][j].getCol() + 0.95, track[i][j].getRow(), track[i][j].getCol() + 0.95);
                    if (path.intersectsLine(edge1) || path.intersectsLine(edge2) || path.intersectsLine(edge3) || path.intersectsLine(edge4))
                    {
                        int finishDist = Math.abs(lastCarLoc.getRow() - finish.get(0).getRow());
                        int collisionDist = Math.abs(lastCarLoc.getRow() - i);
                        if (finishLineCheck() && finishDist < collisionDist)
                        {
                            finished = true;
                        } else
                        {
                            finished = false;
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
    
    private boolean withinBounds(int row, int col)
    {
        return (row >= 0 && row < track.length && col >= 0 && col < track[0].length);
    }
}
