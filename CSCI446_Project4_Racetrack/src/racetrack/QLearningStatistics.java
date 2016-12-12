
package racetrack;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Tracks the following desired results/statistics from Value Iteration:
 *   - Discount factor vs. Convergence rate
 *   - Discount factor vs. Racecar performance 
 * 
 * Use 5 difference selections of the discount factor [0.2, 0.4, 0.6, 0.8, 1.0]
 *   and take the average of 10 runs for each discount factor
 */
public class QLearningStatistics 
{
    // holds the discount factors that are they keys (x-axis) of the results
    private static final double[] discountFactors = new double[]{ 0.2, 0.4, 0.6, 0.8, 1.0 };
    // The key is the value of the discount factor, the value (stack) is the
    // convergence results from each of the ten runs
    private static final HashMap<Double, ArrayList<Integer>> discount_convergence = new HashMap<>();
    // The key is the value of the discount factor, the value (stack) is the
    // number of iterations it took for the racecar to reach the finish line
    private static final HashMap<Double, ArrayList<Integer>> discount_raceResults = new HashMap<>();
    
    // holds the learning factors that are they keys (x-axis) of the results
    private static final double[] learningFactors = new double[]{ 0.5, 0.7, 0.8, 0.9, 1.0 };
    // The key is the value of the learning factor, the value (stack) is the
    // convergence results from each of the ten runs
    private static final HashMap<Double, ArrayList<Integer>> learning_convergence = new HashMap<>();
    // The key is the value of the learning factor, the value (stack) is the
    // number of iterations it took for the racecar to reach the finish line
    private static final HashMap<Double, ArrayList<Integer>> learning_raceResults = new HashMap<>();
    
    // number of training iterations for each of the 10 runs
    private static final ArrayList<Integer> trainingIterations = new ArrayList<>();
    // number of steps it took the racecar to get to the finish line for each of the 10 runs
    private static final ArrayList<Integer> raceResults = new ArrayList<>();
    
    /**
     * initialize the hash maps
     */
    public static void initializeQLearningStatistics()
    {
        // initialize each hashmap with the 5 discount factor keys and a new stack
        for (double value : discountFactors)
        {
            discount_convergence.put(value, new ArrayList<>());
            discount_raceResults.put(value, new ArrayList<>());
        }
        
        // initialize each hashmap with the 5 discount factor keys and a new stack
        for (double value : learningFactors)
        {
            learning_convergence.put(value, new ArrayList<>());
            learning_raceResults.put(value, new ArrayList<>());
        }
    }
    
    /**
     * push the current convergence value onto the stack linked to the correct
     * discount factor
     * 
     * @param discountFactor
     * @param value : the convergence value
     */
    public static void putDiscountConvergence(Double discountFactor, Integer value)
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
        double average = 0;
        
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
    public static  void putDiscountRaceResults(Double discountFactor, Integer value)
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
     * push the current convergence value onto the stack linked to the correct
     * Learning factor
     * 
     * @param learningFactor
     * @param value : the convergence value
     */
    public static void putLearningConvergence(Double learningFactor, Integer value)
    {
        ArrayList tempList = learning_convergence.get(learningFactor);
        tempList.add(value);
    }
    
    /**
     * @param learningValue : the key of the hashmap
     * @return the averaged values of all convergence values belonging to
     * learning_raceResults for key 'discount'
     */
    private static double getAverageLearningConvergence(Double learningValue)
    {
        ArrayList<Integer> temp = learning_convergence.get(learningValue);
        double average = 0;
        
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
     * @param learningFactor
     * @param value : the race results value
     */
    public static  void putLearningRaceResults(Double learningFactor, Integer value)
    {
        ArrayList tempList = learning_raceResults.get(learningFactor);
        tempList.add(value);
    }
    
    /**
     * @param learningValue : the key of the hashmap
     * @return the averaged values of all convergence values belonging to
     * discount_raceResults for key 'discount'
     */
    private static double getAverageLearningRaceResults(Double learningValue)
    {
        ArrayList<Integer> temp = learning_raceResults.get(learningValue);
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
        System.out.format("     === Displaying Results for QLearningStatistics ===%n");
        System.out.format("%-30s%-30s%n", "Average Training Iterations", "Average Steps to Finish Race");
        System.out.format("%-30.3f%-30.3f%n%n", getAverageTrainingIterations(), getRaceResults());
        
        System.out.format("Convergence Rate For Each Discount Paremeter Setting:%n");
        System.out.format("%-10.2f%-10.2f%-10.2f%-10.2f%-10.2f%n", 0.2, 0.4, 0.6, 0.8, 1.0);
        System.out.println("---------------------------------------------");
        System.out.format("%-10.2f%-10.2f%-10.2f%-10.2f%-10.2f%n%n",
            getAverageDiscountConvergence(0.2),
            getAverageDiscountConvergence(0.4),
            getAverageDiscountConvergence(0.6),
            getAverageDiscountConvergence(0.8),
            getAverageDiscountConvergence(1.0));
        
        System.out.format("Average Steps to Finish Race For Each Discount Paremeter Setting:%n");
        System.out.format("%-10.2f%-10.2f%-10.2f%-10.2f%-10.2f%n", 0.2, 0.4, 0.6, 0.8, 1.0);
        System.out.println("---------------------------------------------");
        System.out.format("%-10.2f%-10.2f%-10.2f%-10.2f%-10.2f%n%n",
            getAverageDiscountRaceResults(0.2),
            getAverageDiscountRaceResults(0.4),
            getAverageDiscountRaceResults(0.6),
            getAverageDiscountRaceResults(0.8),
            getAverageDiscountRaceResults(1.0));
        
        System.out.format("Convergence Rate For Each Learning Paremeter Setting:%n");
        System.out.format("%-10.2f%-10.2f%-10.2f%-10.2f%-10.2f%n", 0.5, 0.7, 0.8, 0.9, 1.0);
        System.out.println("---------------------------------------------");
        System.out.format("%-10.2f%-10.2f%-10.2f%-10.2f%-10.2f%n%n",
            getAverageLearningConvergence(0.5),
            getAverageLearningConvergence(0.7),
            getAverageLearningConvergence(0.8),
            getAverageLearningConvergence(0.9),
            getAverageLearningConvergence(1.0));
        
        System.out.format("Average Steps to Finish Race For Each Learning Paremeter Setting:%n");
        System.out.format("%-10.2f%-10.2f%-10.2f%-10.2f%-10.2f%n", 0.5, 0.7, 0.8, 0.9, 1.0);
        System.out.println("---------------------------------------------");
        System.out.format("%-10.2f%-10.2f%-10.2f%-10.2f%-10.2f%n%n",
            getAverageLearningRaceResults(0.5),
            getAverageLearningRaceResults(0.7),
            getAverageLearningRaceResults(0.8),
            getAverageLearningRaceResults(0.9),
            getAverageLearningRaceResults(1.0));
    }
}
