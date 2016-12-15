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
    
    private final int numRuns = 1;
    
    public Experiment() { }
    
    /**
     * Assigns utilities to each cell for every possible velocity state using
     * Value Iteration 
     */
    public void runValueIteration() throws IOException
    {
        double epsilon = 0.00000001;
        double[] discountFactors = new double[]{ 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, 1.0 };
//        double[] discountFactors = new double[]{ 0.6 };
        // initizliat stats objects
        ValueIterationStatistics.initializeValueIterationStatistics();
        
        // for each discount factor...
        for (double discount : discountFactors)
        {
            System.out.format("== Tunable Parameters: ==%n"
            + "Discount factor: %.3f%n"
            + "Epsilon halting condition: %.9f%n"
            + "Number of runs to average over: %d%n",
            discount, epsilon, numRuns);
            // run algorithm n times to get averaged results
            for (int i = 0; i < numRuns; i++)
            {
                // instantiate new racetrack
                L = ConvertToRacetrack("tracks/L-track.txt", L, "L");
                O = ConvertToRacetrack("tracks/O-track.txt", O, "O");
                R = ConvertToRacetrack("tracks/R-track.txt", R, "R");
                
                // instantiate learner with correct discount factor and map:
                ValueIteration viLearner = new ValueIteration(L, discount, epsilon);
                
                // train the racetrack
                viLearner.trainUtilities();
                
                // run the racecar on the racetrack
                L.run(false, discount);
                System.out.format("-Run %d of %d finished-%n", i+1, numRuns);
            }
            System.out.println("\n");
        }
        
        // print final stats results
        ValueIterationStatistics.printAllResults();
    }
    
    /**
     * Run the QLearning algorithm
     */
    public void runQLearning() throws IOException
    {
        double epsilon = 0.00000001;
//        Double[] discountFactors = new Double[]{ 0.4, 0.6, 0.8, 1.0 };
	Double[] discountFactors = new Double[]{ 0.6 };
//        Double[] learningFactors = new Double[]{ 0.4, 0.6, 0.8, 1.0 };
        Double[] learningFactors = new Double[]{ 0.8 };
        // initizliat stats objects
	QLearningStatistics.initializeQLearningStatistics();
        
        // for each discount factor...
	for (Double discount : discountFactors)
        {
            System.out.format("== Tunable Parameters: ==%n"
            + "  epsilon halt threshold:  %.9f%n"
            + "  greedy action selection chance: %.4f%n"
            + "  discount factor: %.4f%n"
            + "  learning factor: %.4f%n",
                epsilon, 0.3, discount, 0.8);
            // run algorithm 10 times to get averaged results
            for (int i = 0; i < numRuns; i++)
            {
                // instantiate new racetrack
                L = ConvertToRacetrack("tracks/L-track.txt", L, "L");
                O = ConvertToRacetrack("tracks/O-track.txt", O, "O");
                R = ConvertToRacetrack("tracks/R-track.txt", R, "R");

                // instantiate learner with correct discount factor and track:
                QLearning qLearner = new QLearning(L, discount, null, epsilon);

                // train the racetrack
                qLearner.learnTrack();
                
                System.out.format("-Run %d of %d finished-%n", i+1, numRuns);
            }
	}
        
//        // for each learning factor...
//        for (Double learnValue : learningFactors)
//        {
//            System.out.format("== Tunable Parameters: ==%n"
//            + "  epsilon halt threshold:  %f%n"
//            + "  greedy action selection chance: %.4f%n"
//            + "  discount factor: %.4f%n"
//            + "  learning factor: %.4f%n",
//                epsilon, 0.3, 0.6, learnValue);
//            // run algorithm 10 times to get averaged results
//            for (int i = 0; i < numRuns; i++)
//            {
//                // instantiate new racetrack
//                L = ConvertToRacetrack("tracks/L-track.txt", L, "L");
//                O = ConvertToRacetrack("tracks/O-track.txt", O, "O");
//                R = ConvertToRacetrack("tracks/R-track.txt", R, "R");
//
//                // instantiate learner with correct learning factor and track:
//                QLearning qLearner = new QLearning(L, null, learnValue, epsilon);
//
//                // train the racetrack
//                qLearner.learnTrack();
//            }
//	}
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
}
