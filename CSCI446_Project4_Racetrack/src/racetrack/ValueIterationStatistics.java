
package racetrack;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Tracks the following desired results/statistics from Value Iteration:
 *   - Discount factor vs. Convergence rate
 *   - Discount factor vs. Racecar performance 
 * 
 * Use difference selections of the discount factor 
 *   and take the average of 10 runs for each discount factor
 */
public class ValueIterationStatistics 
{
    // holds the discount factors that are they keys (x-axis) of the results
    private static final double[] discountFactors = new double[]{ 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, 1.0 };
    // The key is the value of the discount factor, the value (stack) is the
    // convergence results from each of the ten runs
    private static final HashMap<Double, ArrayList<Integer>> discount_convergence = new HashMap<>();
    // The key is the value of the discount factor, the value (stack) is the
    // number of iterations it took for the racecar to reach the finish line
    private static final HashMap<Double, ArrayList<Integer>> discount_raceResults = new HashMap<>();
    // number of training iterations for each of the 10 runs
    private static final ArrayList<Integer> trainingIterations = new ArrayList<>();
    // number of steps it took the racecar to get to the finish line for each of the 10 runs
    private static final ArrayList<Integer> raceResults = new ArrayList<>();
    
    /**
     * initialize the hash maps
     */
    public static void initializeValueIterationStatistics()
    {
        // initialize each hashmap with the discount factor keys and a new stack
        for (double value : discountFactors)
        {
            discount_convergence.put(value, new ArrayList<>());
            discount_raceResults.put(value, new ArrayList<>());
        }
    }
    
    /**
     * push the current convergence value onto the stack linked to the correct
     * discount factor
     * 
     * @param discountFactor
     * @param value : the convergence value
     */
    public static void putConvergence(Double discountFactor, Integer value)
    {
        ArrayList tempList = discount_convergence.get(discountFactor);
        tempList.add(value);
    }
    
    /**
     * @param discount : the key of the hashmap
     * @return the averaged values of all convergence values belonging to
     * discount_raceResults for key 'discount'
     */
    private static double getAverageDiscountConvergence(Double discount)
    {
        ArrayList<Integer> temp = discount_convergence.get(discount);
        double average = 0.0;
        
        for (Integer value : temp)
        {
            average += value;
        }
        average = (double)average / (double)temp.size();
        
        return average;
    }
    
    /**
     * push the current race results onto the stack linked to the correct
     * discount factor
     * 
     * @param discountFactor
     * @param value : the race results value
     */
    public static  void putRaceResults(Double discountFactor, Integer value)
    {
        ArrayList tempList = discount_raceResults.get(discountFactor);
        tempList.add(value);
    }
    
    /**
     * @param discount : the key of the hashmap
     * @return the averaged values of all convergence values belonging to
     * discount_raceResults for key 'discount'
     */
    private static double getAverageDiscountRaceResults(Double discount)
    {
        ArrayList<Integer> temp = discount_raceResults.get(discount);
        double average = 0;
        
        for (Integer value : temp)
        {
            average += value;
        }
        average = (double)average / (double)temp.size();
        
        return average;
    }
    
    /**
     * Put the number of training iterations the current run (out of 10 runs) took
     * @param numIterations 
     */
    public static void putTrainingIterations(Integer numIterations)
    {
        trainingIterations.add(numIterations);
    }
    
    /**
     * Takes the average of all values in numTrainingIterations
     * @return the average
     */
    private static double getAverageTrainingIterations()
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
    public static void putRaceResults(Integer numSteps)
    {
        raceResults.add(numSteps);
    }
    
    /**
     * Takes the average of all values in race results
     * @return the average
     */
    private static double getRaceResults()
    {
        double average = 0.0;
        for (Integer value : raceResults)
        {
            average += value;
        }
        average = (double)average / (double)raceResults.size();
        return average;
    }
    
    /**
     * prints the following:
     *   - average training iterations
     *   - average race results (number of steps to finish)
     *   - table of discount factor vs. average convergence rate
     *   - table of discount factor vs. average race results
     */
    public static void printAllResults()
    {
        System.out.format("     === Displaying Results for Value Iteration ===%n");
        System.out.format("%-30s%-30s%n", "Average Training Iterations", "Average Steps to Finish Race");
        System.out.format("%-30.3f%-30.3f%n%n", getAverageTrainingIterations(), getRaceResults());
        
        System.out.format("Convergence Rate For Each Discount Paremeter Setting:%n");
        System.out.format("%-10.2f%-10.2f%-10.2f%-10.2f%-10.2f%-10.2f%-10.2f%n", 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, 1.0);
        System.out.println("------------------------------------------------------------------------------------------");
        System.out.format("%-10.2f%-10.2f%-10.2f%-10.2f%-10.2f%-10.2f%-10.2f%n%n",
            getAverageDiscountConvergence(0.4),
            getAverageDiscountConvergence(0.5),
            getAverageDiscountConvergence(0.6),
            getAverageDiscountConvergence(0.7),
            getAverageDiscountConvergence(0.8),
            getAverageDiscountConvergence(0.9),
            getAverageDiscountConvergence(1.0));
        
        System.out.format("Average Steps to Finish Race For Each Discount Paremeter Setting:%n");
        System.out.format("%-10.2f%-10.2f%-10.2f%-10.2f%-10.2f%-10.2f%-10.2f%n", 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, 1.0);
        System.out.println("------------------------------------------------------------------------------------------");
        System.out.format("%-10.2f%-10.2f%-10.2f%-10.2f%-10.2f%-10.2f%-10.2f%n%n",
            getAverageDiscountRaceResults(0.4),
            getAverageDiscountRaceResults(0.5),
            getAverageDiscountRaceResults(0.6),
            getAverageDiscountRaceResults(0.7),
            getAverageDiscountRaceResults(0.8),
            getAverageDiscountRaceResults(0.9),
            getAverageDiscountRaceResults(1.0));
    }
}
