package GraphColoring;

import java.util.Map;
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
    // the parent subset of the chromosomes
    private Map<Integer, Vertex> parentSet;
    private Map<Integer, Vertex> childSet;
    
    public GeneticAlgorithmSolver()
    {
        maxColors = 4;
    }

    @Override
    public void runSolver() 
    {
        // randomize the colors of all vertices
        initializePopulation();
        // set initial fitness of each vertex after randomization
        setAllFitnesses();
        boolean satisfied = graphSatisfiesConstraint();
        
        // run the algorithm until all vertices are valid colors
        for (int i = 0; i < 10; i++)
        //while (!satisfied)
        {
            // generate parents subset through tournament selection
        }
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
    
    private void setAllFitnesses()
    {
        for (int key : theGraph.keySet())
        {
            Vertex currentVert = theGraph.get(key);
            currentVert.fitness = currentVert.calculateFitness();
        }
    }
}
