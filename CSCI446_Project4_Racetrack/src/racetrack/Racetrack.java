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
    
    /**
     * Print the track with utilities shown instead of char values for a specified velocity vector
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
                System.out.format("(%c)%-7.2f ", track[i][j].getType(), track[i][j].getUtilities()[rowVelIndex][colVelIndex]);
            }
            System.out.println();
        }
    }
}
