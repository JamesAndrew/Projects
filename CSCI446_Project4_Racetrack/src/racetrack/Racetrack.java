/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package racetrack;

/**
 *
 * @author James
 */
public class Racetrack 
{
    private final char [][] track;
    
    private final int [][] start;
    private final int [][] finish;
    
    public Racetrack(int r, int c)
    {
        track = new char[r][c];
        start = new int[4][2];
        finish = new int[4][2];
    }
    
    /**
     *
     * @param r
     * @param c
     * @param t
     */
    public void setTrack(int r, int c, char t)
    {
        track[r][c] = t;
    }
    
    public void setStart(int b, int x, int y)
    {
        start[b][0] = x;
        start[b][1] = y;
    }
    
    public void printStart()
    {
        System.out.println("Start line: ");
        
        int s = start.length;
        int st = start[0].length;
        for(int i = 0; i < s; i++)
        {
            System.out.print("Point " + i + " (x, y) = (");
            for(int j = 0; j < st; j++)
            {
                System.out.print(start[i][j] + " ");
            }
            System.out.println(")");
        }
    }
    
    public void setFinish(int b, int x, int y)
    {
        finish[b][0] = x;
        finish[b][1] = y;
    }
    
    public void printFinish()
    {
        System.out.println("Finish line: ");
        
        int f = finish.length;
        int fi = finish[0].length;
        for(int i = 0; i < f; i++)
        {
            System.out.print("Point " + i + " (x, y) = (");
            for(int j = 0; j < fi; j++)
            {
                System.out.print(finish[i][j] + " ");
            }
            System.out.println(")");
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
                System.out.print(track[r][c]);
            }
            System.out.println();
        }
        printStart();
        printFinish();
    }
    
    public void getCrashLocations()
    {
        
    }
    
    public void getTrackHeatmap()
    {
        
    }
}
