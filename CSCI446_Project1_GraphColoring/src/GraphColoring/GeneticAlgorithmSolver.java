package GraphColoring;

import java.util.Random;

/**
 * Graph coloring constraint solver using backtracking with forward checking
 * Note that the chromosome array is already represented by the graph Map<Integer, Vertex>
 * collection because each vertex is a chromosome where the color of each vertex
 * is the chromosome value.
 * @version 09/08/16
 */
public class GeneticAlgorithmSolver extends ConstraintSolver
{
    
    public GeneticAlgorithmSolver()
    {
        maxColors = 4;
    }

    @Override
    public void runSolver() 
    {
        // randomize the colors of all vertices
        initializePopulation();
        // calculate initial fitness of each vertex after randomization
        calculateAllFitness();
    }
    
    /**
     * Assign each chromosome (graph map vertex entry) a randomized color
     * between 0 and maxColors
     */
    private void initializePopulation()
    {
        Random rand = new Random();
        
        for (int key : theGraph.keySet())
        {
            Vertex currentVert = theGraph.get(key);
            int randColor = rand.nextInt(maxColors);
            currentVert.color = randColor;
        }
    }
    
    private void calculateAllFitness()
    {
        for (int key : theGraph.keySet())
        {
            Vertex currentVert = theGraph.get(key);
            currentVert.fitness = currentVert.calculateFitness();
        }
    }
}
