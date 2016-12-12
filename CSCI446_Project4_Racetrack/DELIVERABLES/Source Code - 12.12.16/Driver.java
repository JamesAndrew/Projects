package racetrack;

import java.io.IOException;

/**
 *
 */
public class Driver 
{
    public static void main(String[] args) throws IOException
    {
        Experiment experiment = new Experiment();
        experiment.runValueIteration();
//        experiment.runQLearning();
    }
}
