
package racetrack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

/**
 * Tracks the following desired results/statistics from Value Iteration:
 *   - Discount factor vs. Convergence rate
 *   - Discount factor vs. Racecar performance 
 * 
 * Use 5 difference selections of the discount factor [0.2, 0.4, 0.6, 0.8, 1.0]
 *   and take the average of 10 runs for each discount factor
 */
public class ValueIterationStatistics 
{
    // holds the discount factors that are they keys (x-axis) of the results
    private final double[] discountFactors = new double[]{ 0.2, 0.4, 0.6, 0.8, 1.0 };
    // The key is the value of the discount factor, the value (stack) is the
    // convergence results from each of the ten runs
    private final HashMap<Double, Stack<Integer>> discount_convergence = new HashMap<>();
    // The key is the value of the discount factor, the value (stack) is the
    // number of iterations it took for the racecar to reach the finish line
    private final HashMap<Double, Stack<Integer>> discount_raceResults = new HashMap<>();
    // number of training iterations for each of the 10 runs
    private final ArrayList<Integer> trainingIterations = new ArrayList<>();
    // number of steps it took the racecar to get to the finish line for each of the 10 runs
    private final ArrayList<Integer> raceResults = new ArrayList<>();
    
    public ValueIterationStatistics()
    {
        // initialize each hashmap with the 5 discount factor keys and a new stack
        for (double value : discountFactors)
        {
            discount_convergence.put(value, new Stack<>());
            discount_raceResults.put(value, new Stack<>());
        }
    }
    
    /**
     * push the current convergence value onto the stack linked to the correct
     * discount factor
     * 
     * @param discountFactor
     * @param value : the convergence value
     */
    public void putConvergence(Double discountFactor, Integer value)
    {
        Stack tempStack = discount_convergence.get(discountFactor);
        tempStack.push(value);
    }
    
    /**
     * push the current race results onto the stack linked to the correct
     * discount factor
     * 
     * @param discountFactor
     * @param value : the race results value
     */
    public void putRaceResults(Double discountFactor, Integer value)
    {
        Stack tempStack = discount_raceResults.get(discountFactor);
        tempStack.push(value);
    }
    
    /**
     * Put the number of training iterations the current run (out of 10 runs) took
     * @param numIterations 
     */
    public void putTrainingIterations(Integer numIterations)
    {
        trainingIterations.add(numIterations);
    }
    
    /**
     * Takes the average of all values in numTrainingIterations
     * @return the average
     */
    public double getAverageTrainingIterations()
    {
        double average = 0.0;
        for (Integer value : trainingIterations)
        {
            average += value;
        }
        average = (double)average / (double)trainingIterations.size();
        return average;
    }
    
    /**
     * Put the value of the race results for the current run (out of 10 runs) 
     * @param numSteps : number of steps it took the car to go from 'S' to 'F'
     */
    public void putRaceResults(Integer numSteps)
    {
        raceResults.add(numSteps);
    }
    
    /**
     * Takes the average of all values in race results
     * @return the average
     */
    public double getRaceResults()
    {
        double average = 0.0;
        for (Integer value : raceResults)
        {
            average += value;
        }
        average = (double)average / (double)raceResults.size();
        return average;
    }
}
