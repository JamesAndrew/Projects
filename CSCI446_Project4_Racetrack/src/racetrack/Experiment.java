/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package racetrack;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;


/**
 *
 * @author James
 */
public class Experiment 
{
    public Racetrack L;
    public Racetrack O;
    public Racetrack R;
    
    public Experiment() throws IOException
    {
        L = ConvertToRacetrack("tracks/L-track.txt", L);
        L.printTrack();
        
        O = ConvertToRacetrack("tracks/O-track.txt", O);
        O.printTrack();
        
        R = ConvertToRacetrack("tracks/R-track.txt", R);
        R.printTrack();
    }
    
    /**
     * 
     * @param fileName
     * @param T
     * @return Racetrack object T
     * @throws IOException 
     * Creates T using fileName
     */
    private Racetrack ConvertToRacetrack(String fileName, Racetrack T) throws IOException
    {
        
        // create file reader for dataset file and wrap it in a buffer
        FileReader fileReader = new FileReader(fileName);
        
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        
        String line = null;
        try {
            line = bufferedReader.readLine();
        } catch (IOException e) {
            }
        
        String[] data = line.split(",");
                        
        int starts = 0; // counter for start blocks
        int finishes = 0; // counter for finish blocks
        int rows = Integer.parseInt(data[0]);
        int cols = Integer.parseInt(data[1]);
        System.out.println("Track " + fileName + " (r, c)= (" + rows + ", " + cols + ")"); 
        T = new Racetrack(rows, cols);
        for (int i = 0; i < rows; i++)
        {
            try {
                line = bufferedReader.readLine();
            } catch (IOException e) {
                }
            for(int j = 0; j < line.length(); j++)
            {
                T.setTrack(i, j, line.charAt(j));
                if(line.charAt(j) == 'S')
                {
                    T.setStart(starts, j, i);
                    starts++;
                }
                else if(line.charAt(j) == 'F')
                {
                    T.setFinish(finishes, j, i);
                    finishes++;
                }
            }
            line = null;
        }
        data = null;
        
        bufferedReader.close();
        fileReader.close();
        return T;
    }
    
    private void DisplayResults()
    {
        
    }
}
