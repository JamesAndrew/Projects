
package classification;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.Stack;

/**
 * Temp class that discretizes features of continuously valued data
 * @author David
 */
public class Temp_Discretizer 
{
    // class-scoped variable that is filled during the recurisve calls of Discretize
    // each value is where a 'cut' will occur in the sorted data set array
    private static ArrayList<Double> cutPoints = new ArrayList<>();
    // tunable parameter to affect halting condition to stop making cuts. 
    // lower values will make more cuts. 0.50 seemed to work well for the small 
    // testing data set of size 24 with 3 classifications
    private static double cutSensitivity = 0.35;
    
    /**
     * Start with a .txt file of a one-feature data set with continuous values,
     * end with an ArrayList<ArrayList<Integer>> of discretized values
     * @param args
     * @throws java.io.FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException 
    {
        // transform the .txt file into an ArrayList<ArrayList<Double>>
        ArrayList<ArrayList<Double>> currentDataSet = generateDataSet("DataSets/new-house-votes-84.txt");
        
        // repeat process for each feature column:
        for (int featColumn = 1; featColumn < currentDataSet.get(0).size(); featColumn++)
        {
            // clear cutPoints after each column 
            cutPoints.clear();
            
            System.out.format("= Discretizing feature column %d =%n", featColumn);
            currentDataSet = sortByCurrentFeature(currentDataSet, featColumn);
            printADoubleDataSet(currentDataSet);
            
            // method that fills *n* partition locations in 'cutPoints' by calling the recursive 'Discretize' function
            Discretize(currentDataSet, featColumn);
            Collections.sort(cutPoints);
            
            // modify cut points to be the median value between the two feature values it should be between
            for (int i = 0, j = 0; i < currentDataSet.size(); i++)
            {
                if (cutPoints.get(j) < currentDataSet.get(i).get(featColumn))
                {
                    double cutL = currentDataSet.get(i-1).get(featColumn);
                    double cutR = currentDataSet.get(i).get(featColumn);
                    double newCutValue = (cutL + cutR) / 2;
                    cutPoints.set(j, newCutValue);
                    j++;
                }
                if (j == cutPoints.size()) break;
            }
            
            System.out.println("\nFinal cut points, values only:");
            for (Double point : cutPoints) System.out.format("%.3f%n", point);
            
            // helpful print output stuff to make sense of what happened
            System.out.format("%nFinal cut points (feature column %d)%n", featColumn);
            Stack<Double> stack = new Stack();
            for (int i = cutPoints.size() - 1; i >= 0; i--)
            {
                stack.push(cutPoints.get(i));
            }
            for (ArrayList<Double> vector : currentDataSet)
            {
                if (stack.size() > 0)
                {
                    if (stack.peek() < vector.get(featColumn))
                    {
                        System.out.println("------------------------------");
                        stack.pop();
                    }
                }
                
                for (Double value : vector)
                {
                    System.out.format("%.3f ", value);
                }
                System.out.println();
            }

            // Discritize the ArrayList of doubles for the current feature column
            // by assigning each 'bin' an incrementing, arbitrary integer value 
            // (these could just as well be 'A', 'B', etc.)
            currentDataSet = assignDiscreteValues(currentDataSet, featColumn);
            System.out.println("\nFinal discretized points after running feature column " + featColumn + ":");
            printADoubleDataSet(currentDataSet);
            System.out.println("===========================================\n");
        }
        
        // Finally, turn every value in the ArrayList<ArrayList<Double>> into an ArrayList<ArrayList<Integer>>
        ArrayList<ArrayList<Integer>> discretizedData = new ArrayList<>();
        for (int i = 0; i < currentDataSet.size(); i++)
        {
            ArrayList<Integer> currentNewVector = new ArrayList<>();
            ArrayList<Double>  currentOldVector = currentDataSet.get(i);
            
            for (Double value : currentOldVector) currentNewVector.add(value.intValue());
            discretizedData.add(currentNewVector);
        }
        System.out.println("Final discrete data set of integers: ");
        printADiscreteDataSet(discretizedData);
    }
    
    /**
     * Methods used during recursion calls: 
     *  - entropy           : utility calculation procedure
     *  - classEntropy      : utility calculation procedure (just did this calculation in one line, no method)
     *  - gain              : utility calculation procedure
     *  - findMinEntropyCut : finds the index in the current subset that results in the least entropy 
     *  - mDLP              : "Minimal Description Length Principle". The halting condition.
     * 
     * Note that this currently only discretizes on feature column. A simple loop 
     * can extend this to sort all feature columns one at a time.
     * 
     * Might be good for me to have a more clarifying name, but note that this method
     * does not actually complete the discretization. It just generates the cut points.
     * The actual discretization process happens once we have the cut points.
     * 
     * I'm using single-letter variables to match the equations from various academic papers
     * 
     * @param S : a data set with 1 feature (for now) that is sorted by feature values
     * @param featColumn : the current feature column discretization is happening to
     * @return fills class variable 'cutPoints' with a list of where the cut points will be
     */
    private static void Discretize(ArrayList<ArrayList<Double>> S, int featColumn)
    {
        // end the recursion if S is a set of size 1
        if (S.size() == 1)
        {
            // add cut point for that single entry 
            double cutPoint = S.get(0).get(featColumn);
            if (!cutPoints.contains(cutPoint)) cutPoints.add(cutPoint);
            return;
        }
        
        // assign the index in the current subset that results in the least entropy. This is 'T' in the Fayyad paper
        int minEntropyIndex = findMinEntropyCut(S);
        
        // get number of classifications in current set S, S_1, and S_2 (used for MDLPC claculation below)
        ArrayList<ArrayList<Double>> S_1 = new ArrayList<>();
	ArrayList<ArrayList<Double>> S_2 = new ArrayList<>();
        ArrayList<Double> sClasses = new ArrayList<>();
        ArrayList<Double> s1Classes = new ArrayList<>();
        ArrayList<Double> s2Classes = new ArrayList<>();
        
        for (int leftSet = 0; leftSet <= minEntropyIndex; leftSet++) 
		S_1.add(leftSet, S.get(leftSet));
	for (int rightSet = minEntropyIndex+1, s2Index = 0; rightSet < S.size(); rightSet++, s2Index++) 
		S_2.add(s2Index, S.get(rightSet));
        
        for (ArrayList<Double> entry : S)
        {
            Double currentClass = entry.get(0);
            if (!sClasses.contains(currentClass)) sClasses.add(currentClass);
        }
        for (ArrayList<Double> entry : S_1)
        {
            Double currentClass = entry.get(0);
            if (!s1Classes.contains(currentClass)) s1Classes.add(currentClass);
        }
        for (ArrayList<Double> entry : S_2)
        {
            Double currentClass = entry.get(0);
            if (!s2Classes.contains(currentClass)) s2Classes.add(currentClass);
        }
        
        int k = sClasses.size();
        int k1 = s1Classes.size();
        int k2 = s2Classes.size();
        
        // halting condition: MDLPC criterion (see Fayyad paper)
        double gain = gain(S, minEntropyIndex);
        double threshold = ((Math.log10(S.size()-1) / Math.log10(2)) / S.size()) 
                + ((Math.log10(Math.pow(3,k)-2) / Math.log10(2) - (k*entropy(S) - k1*entropy(S_1) - k2*entropy(S_2))) / S.size());
        // take a fraction of the threshold depending on data size?
        threshold *= cutSensitivity;
        
//        System.out.format("Halting condition: gain: %f, threshold: %f%n", gain, threshold);
        if (gain < threshold)
        {
            // return if halting condition is met...
//            System.out.println("= Halting condition met =");
        }
        else
        {
            // otherwise add the cut point to the final list and... 
            double cutPoint = S.get(minEntropyIndex).get(featColumn);
            if (!cutPoints.contains(cutPoint)) cutPoints.add(cutPoint);
//            System.out.format("= Added %f to the cutPoints list =%n%n", S.get(minEntropyIndex).get(1));
            
            // recursively check the subsets on both sides of the cut
            Discretize(S_1, featColumn);
            Discretize(S_2, featColumn);
        }
    }
    
    /**
     * Iterate through each data point in S and calculate the class information entropy
     * if a partition happened at that index. Return whichever index minimizes entropy
     * @param S
     * @return The index that minimizes entropy
     */
    private static int findMinEntropyCut(ArrayList<ArrayList<Double>> S)
    {
        int    bestIndex = -1;
        double minEntropy = Double.MAX_VALUE;
        
        // for each feature entry... (keeping as an ArrayList<ArrayList<Double>> to have the classification attached with it)
        for (int i = 0; i < S.size(); i++)
        {
            // make a cut point at this entry and calculate the resulting entropy
            int cutIndex = i;
            
            // create two subsets from the entries to the left (inclusive) and right (exclusive) of the cut point
            ArrayList<ArrayList<Double>> S_1 = new ArrayList<>();
            ArrayList<ArrayList<Double>> S_2 = new ArrayList<>();
            
            for (int leftSet = 0; leftSet <= i; leftSet++) 
                S_1.add(leftSet, S.get(leftSet));
            for (int rightSet = i+1, s2Index = 0; rightSet < S.size(); rightSet++, s2Index++) 
                S_2.add(s2Index, S.get(rightSet));
            
            // calculate the entropy of each subset
            double S1_Entropy = entropy(S_1);
            double S2_Entropy = entropy(S_2);
            
            // calculate average class entropy as a result of the cut
            double classEntropy = ((double)S_1.size() / (double)S.size())*S1_Entropy + ((double)S_2.size() / (double)S.size())*S2_Entropy;
            
            // if this entropy is small, reassign minEntropy value
            if (classEntropy < minEntropy) 
            {
                bestIndex = cutIndex;
                minEntropy = classEntropy;
            }
        }
        
//        System.out.println("Current S:");
//        printADoubleDataSet(S);
//        System.out.format("Best index: %d with entropy %f%n", bestIndex, minEntropy);
        
        if (bestIndex < 0) throw new RuntimeException("index never set in find min entropy cut");
        else
            return bestIndex;
    }
    
    /**
     * Calculates the class entropy of a set
     * Ent(S) = -Sum_i=1^k [P(C_i, S) log_2(P(C_i, S))]
     * 
     * @param S the set to calculate entropy on
     * @return a value representing entropy. high values mean more chaos
     */
    private static double entropy(ArrayList<ArrayList<Double>> S)
    {
        double sum = 0.0;
        ArrayList<Double> classes = new ArrayList<>();
        
        // extract the existing classes for the current set
        for (ArrayList<Double> entry : S)
        {
            Double currentClass = entry.get(0);
            if (!classes.contains(currentClass)) classes.add(currentClass);
        }
        
        // for each classification in S...
        for (Double classification : classes)
        {
            // calculate proportion of current classification in S...
            int frequency = 0;
            for (ArrayList<Double> entry : S)
            {
                if (entry.get(0).intValue() == classification.intValue()) frequency++;
            }
            double proportion = (double)frequency / (double)S.size();
            // and multiply the two values in the summation
            sum += proportion * (Math.log10(proportion) / Math.log10(2));
        }
        
        // final value has -1 multiplied to it
        sum = sum * -1;
        return sum;
    }
    
    /**
     * Gain(A,T;S) = Ent(S) - E(A,T;S)
     * E is the average class entropy equation
     * Follows same approach for calculating E as findMinEntropyCut
     * 
     * @param S : The current feature set (often a subset)
     * @param T : the cut index
     * @return : the gain value
     */
    private static double gain(ArrayList<ArrayList<Double>> S, int T)
    {
        ArrayList<ArrayList<Double>> S_1 = new ArrayList<>();
	ArrayList<ArrayList<Double>> S_2 = new ArrayList<>();
        
        for (int leftSet = 0; leftSet <= T; leftSet++) 
		S_1.add(leftSet, S.get(leftSet));
	for (int rightSet = T+1, s2Index = 0; rightSet < S.size(); rightSet++, s2Index++) 
		S_2.add(s2Index, S.get(rightSet));
        
        // calculate the entropy of each subset
	double S1_Entropy = entropy(S_1);
	double S2_Entropy = entropy(S_2);
        
        double ent = entropy(S);
        double e   = ((double)S_1.size()/(double)S.size())*S1_Entropy + ((double)S_2.size()/(double)S.size())*S2_Entropy;
        double gain = ent - e;
        
        return gain;
    }
    
    /**
     * @param input : the entire data set
     * @param featureColumn : the feature column to sort then discretize
     * @return the same data set, but sorted by the feature column of interest
     */
    private static ArrayList<ArrayList<Double>> sortByCurrentFeature(ArrayList<ArrayList<Double>> input, int featureColumn)
    {
        // just using bubble sort for now becuase this is a prototype. 
        boolean swapped = true;
        while (swapped)
        {
            swapped = false;
            for (int i = 1; i < input.size(); i++)
            {
                if (input.get(i-1).get(featureColumn) > input.get(i).get(featureColumn))
                {
                    ArrayList<Double> temp = input.get(i);
                    input.set(i, input.get(i-1));
                    input.set(i-1, temp);
                    swapped = true;
                }
            }
        }
        return input;
    }
    
    /**
     * discretize by assigning each 'bin' an incrementing, arbitrary integer 
     * value (these could just as well be 'A', 'B', etc.)
     * 
     * @param S          : the original data set with continuous values
     * @param featColumn : the feature column being discretized
     * @return : the original ArrayList<ArrayList<Double>> dataset 
     *           but with "integer" double values instead of continuous doubles
     */
    private static ArrayList<ArrayList<Double>> assignDiscreteValues(ArrayList<ArrayList<Double>> S, int featColumn)
    {
        Double        discreteValue = 1.0;
        Stack<Double> cuts          = new Stack();
        
        // add an extra placeholder cutpoint at the bottom to avoid a empty stack exception
        cuts.push(99.99);
        for (int i = cutPoints.size() - 1; i >= 0; i--)
        {
            cuts.push(cutPoints.get(i));
        }
        
        // for each data point in the data set...
        int prevCutIndex = -1;
        for (int i = 0; i < S.size(); i++)
        {
            ArrayList<Double> vector = S.get(i);
            // once a cut point is reached...
            if (!cuts.isEmpty() && cuts.peek() < vector.get(featColumn))
            {
                cuts.pop();
                
                // repalce all feature column values in the current bin with the
                // current discreteValue
                for (int binIndex = i; binIndex > prevCutIndex; binIndex--)
                {
                    S.get(binIndex).set(featColumn, discreteValue);
                }
                discreteValue += 1.0;
                prevCutIndex = i;
            }
            // handle final case of last bin (doesn't have a cut point after it)
            else if (i == S.size()-1)
            {
                if (!cuts.isEmpty())
                {
                    cuts.pop();
                }
                
                // repalce all feature column values in the current bin with the
                // current discreteValue
                for (int binIndex = i; binIndex > prevCutIndex; binIndex--)
                {
                    S.get(binIndex).set(featColumn, discreteValue);
                }
                discreteValue += 1.0;
                prevCutIndex = i;
            }
        }
        return S;
    }
    
    /**
     * Assumes the input dataset is already preprocessed with all values as doubles
     * and the first value is the categorization
     * 
     * @param filePath : the string file path used for the new File(path) call
     * @return An ArrayList<ArrayList> representation of the provided .txt data set
     * @throws FileNotFoundException 
     */
    private static ArrayList<ArrayList<Double>> generateDataSet(String filePath) throws FileNotFoundException
    {
        int lineNums;
        try (Scanner scanner0 = new Scanner(new File(filePath))) 
        {
            lineNums = 0;
            while (scanner0.hasNextLine())
            {
                lineNums++;
                scanner0.nextLine();
            }
        }
        
        String[][] fileAsString;
        int i;
        try (Scanner scanner1 = new Scanner(new File(filePath))) 
        {
            fileAsString = new String[lineNums][];
            i = 0;
            while (scanner1.hasNextLine())
            {
                String rowString = scanner1.nextLine();
                String[] rowSplit = rowString.split(",");
                fileAsString[i] = rowSplit;
                i++;
            }
        }
        
        // turn the double string array into its ArrayList<ArrayList> representation
        ArrayList<ArrayList<Double>> dataSet = new ArrayList<>();
        
        for (int dataPoint = 0; dataPoint < fileAsString.length; dataPoint++)
        {
            ArrayList<Double> vector = new ArrayList<>();
            String[] currentVector = fileAsString[dataPoint];
            
            for (int j = 0; j < currentVector.length; j++)
            {
                vector.add(Double.parseDouble(currentVector[j]));
            }
            dataSet.add(vector);
        }
        return dataSet;
    }
    
    private static void printADoubleDataSet(ArrayList<ArrayList<Double>> data)
    {
        for (int i = 0; i < data.size(); i++)
        {
            ArrayList<Double> vector = data.get(i);
            System.out.format("%-3d: ", i);
            for (Double value : vector)
            {
                System.out.format("%.3f, ", value);
            }
            System.out.println();
        }
    }
    
    private static void printADiscreteDataSet(ArrayList<ArrayList<Integer>> data)
    {
        for (int i = 0; i < data.size(); i++)
        {
            ArrayList<Integer> vector = data.get(i);
//            System.out.format("%-3d: ", i);
//            for (Integer value : vector)
            for (int j = 0; j < vector.size(); j++)
            {
                if (j < vector.size() - 1) System.out.format("%d, ", vector.get(j));
                else System.out.format("%d", vector.get(j));
            }
            System.out.println();
        }
    }
}
