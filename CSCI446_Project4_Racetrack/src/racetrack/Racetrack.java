/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package racetrack;

/**
 *
 */
public class Racetrack
{
    private final Cell[][] track;
    
    public Racetrack(int r, int c)
    {
        track = new Cell[r][c];
    }
    
    public void setTrack(int r, int c, char t)
    {
        track[r][c] = new Cell(r, c, t);
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

    public Cell[][] getTrack() 
    {
        return track;
    }
}
