/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package racetrack;

import java.awt.geom.Line2D;
import java.util.ArrayList;

/**
 *
 * @author James
 */
public class Racetrack implements IRacetrack
{
    private Cell[][] track;
    private int[] currentCarLoc;
    private int[] lastCarLoc;
    private ArrayList<int[]> start = new ArrayList(); 
    private ArrayList finish = new ArrayList(); 
    
    
    public Racetrack(int r, int c)
    {
        track = new Cell[r][c];
    }

    public void setTrack(int r, int c, char t)
    {
        track[r][c] = new Cell(r, c, t);
    }
    
    public void addToStart(int r , int c)
    {
        
    }
    
    public void addToFinish(int r , int c)
    {
        
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


    public void getCrashLocations()
    {

    }

    public void getTrackHeatmap()
    {

    }

    private boolean collisionCheck()
    {
        Line2D path = new Line2D.Double(currentCarLoc[0], currentCarLoc[1], lastCarLoc[0], lastCarLoc[1]);
        int minX = Math.min(currentCarLoc[0], lastCarLoc[0]);
        int maxX = Math.max(currentCarLoc[0], lastCarLoc[0]);
        int minY = Math.min(currentCarLoc[1], lastCarLoc[1]);
        int maxY = Math.max(currentCarLoc[1], lastCarLoc[1]);
        for (int i = minX; i <= maxX; i++)
        {
            for (int j = minY; j <= maxY; j++)
            {
                
            }
        }
        return true; 
    }
}
