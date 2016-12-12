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
 */
public class Experiment 
{
    public Racetrack L;
    public Racetrack O;
    public Racetrack R;
    // temporary, simple racetracks for sample runs and testing
    public Racetrack simple;
    public Racetrack simple2;
    
    public Experiment() throws IOException
    {
//        L = ConvertToRacetrack("tracks/L-track.txt", L, "L");
//        L.printTrack();
//        
        O = ConvertToRacetrack("tracks/O-track.txt", O, "O");
        O.printTrack();
//        
//        R = ConvertToRacetrack("tracks/R-track.txt", R, "R");
//        R.printTrack();
//        
//        simple = ConvertToRacetrack("tracks/Simple-track.txt", simple, "simple");
//        simple.printTrack();
//        
//        simple2 = ConvertToRacetrack("tracks/Simple-track2.txt", simple2, "simple2");
//        simple2.printTrack();
    }
    
    /**
     * Assigns utilities to each cell for every possible velocity state using
     * Value Iteration 
     */
    public void runValueIteration()
    {
        // instantiate and give track to run on
        ValueIteration valueIteration = new ValueIteration(O);
        // being training which solves utility of each cell for every velocity value
        valueIteration.trainUtilities();
        O.run(false);
    }
    
    /**
     * Run the QLearning algorithm
     */
    public void runQLearning()
    {
        QLearning qLearn = new QLearning(O);
        qLearn.learnTrack();
    }
    
    private Racetrack ConvertToRacetrack(String fileName, Racetrack T, String trackName) throws IOException
    {
        // create file reader for dataset file and wrap it in a buffer
        FileReader fileReader = null;
        try 
        {
                fileReader = new FileReader(fileName);
        } catch (FileNotFoundException e) { }
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        
        String line = null;
        try 
        {
            line = bufferedReader.readLine();
        } catch (IOException e) { }
        String[] data = line.split(",");
                        
        int rows = Integer.parseInt(data[0]);
        int cols = Integer.parseInt(data[1]);
        System.out.println("Track " + fileName + " (r, c)= (" + rows + ", " + cols + ")"); 
        T = new Racetrack(rows, cols, trackName);
        for (int i = 0; i < rows; i++)
        {
            try {
                line = bufferedReader.readLine();
            } catch (IOException e) {
                }
            for(int j = 0; j < line.length(); j++)
            {
                T.setTrack(i, j, line.charAt(j));
            }
        }
        
        return T;
    }
    
    private void DisplayResults()
    {
        
    }
}
