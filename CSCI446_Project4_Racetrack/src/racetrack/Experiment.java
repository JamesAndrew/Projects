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
    
    public Experiment() { }
    
    /**
     * Assigns utilities to each cell for every possible velocity state using
     * Value Iteration 
     */
    public void runValueIteration() throws IOException
    {
//        double[] discountFactors = new double[]{ 0.2, 0.4, 0.6, 0.8, 1.0 };
        double[] discountFactors = new double[]{ 0.6 };
        // initizliat stats objects
        ValueIterationStatistics.initializeValueIterationStatistics();
        
        // for each discount factor...
        for (double discount : discountFactors)
        {
            // run algorithm 10 times to get averaged results
            for (int i = 0; i < 1; i++)
            {
                // instantiate new racetrack
                 L = ConvertToRacetrack("tracks/L-track.txt", L, "L");
                 L.printTrack();
                // 
                // O = ConvertToRacetrack("tracks/O-track.txt", O, "O");
                // O.printTrack();
                // 
                // R = ConvertToRacetrack("tracks/R-track.txt", R, "R");
                // R.printTrack();
                
                // instantiate learner with correct discount factor:
                ValueIteration viLearner = new ValueIteration(L, discount);
                
                // train the racetrack
                viLearner.trainUtilities();
                
                // run the racecar on the racetrack
                L.run(false, discount);
            }
        }
        
        // print final stats results
        ValueIterationStatistics.printAllResults();
    }
    
    /**
     * Run the QLearning algorithm
     */
    public void runQLearning() throws IOException
    {
//        Double[] discountFactors = new Double[]{ 0.2, 0.4, 0.6, 0.8, 1.0 };
	Double[] discountFactors = new Double[]{ 0.8 };
//        Double[] learningFactors = new Double[]{ 0.5, 0.7, 0.8, 0.9, 1.0 };
        Double[] learningFactors = new Double[]{ 0.9 };
        // initizliat stats objects
	QLearningStatistics.initializeQLearningStatistics();
        
        // for each discount factor...
	for (Double discount : discountFactors)
        {
            // run algorithm 10 times to get averaged results
            for (int i = 0; i < 1; i++)
            {
                // instantiate new racetrack
                 L = ConvertToRacetrack("tracks/L-track.txt", L, "L");
                 L.printTrack();
                // 
                // O = ConvertToRacetrack("tracks/O-track.txt", O, "O");
                // O.printTrack();
                // 
                // R = ConvertToRacetrack("tracks/R-track.txt", R, "R");
                // R.printTrack();

                // instantiate learner with correct discount factor:
                QLearning qLearner = new QLearning(L, discount, null);

                // train the racetrack
                qLearner.learnTrack();
            }
	}
        
        // for each learning factor...
        for (Double learnValue : learningFactors)
        {
            // run algorithm 10 times to get averaged results
            for (int i = 0; i < 1; i++)
            {
                // instantiate new racetrack
                // L = ConvertToRacetrack("tracks/L-track.txt", L, "L");
                // L.printTrack();
                // 
                // O = ConvertToRacetrack("tracks/O-track.txt", O, "O");
                // O.printTrack();
                // 
                // R = ConvertToRacetrack("tracks/R-track.txt", R, "R");
                // R.printTrack();
                // 
                // simple = ConvertToRacetrack("tracks/Simple-track.txt", simple, "simple");
                // simple.printTrack();
                L = ConvertToRacetrack("tracks/Simple-track2.txt", L, "simple2");
                L.printTrack();

                // instantiate learner with correct discount factor:
                QLearning qLearner = new QLearning(L, null, learnValue);

                // train the racetrack
                qLearner.learnTrack();
            }
	}
        
	// print final stats results
	QLearningStatistics.printAllResults();
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
