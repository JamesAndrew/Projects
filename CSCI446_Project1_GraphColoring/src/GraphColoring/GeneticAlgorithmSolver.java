package GraphColoring;

import com.rits.cloning.Cloner;
import java.util.ArrayList;
import java.util.Random;

/**
 * @version 09/08/16
 */
public class GeneticAlgorithmSolver extends ConstraintSolver
{

    /**
     * Tunable parameters
     */
    // The population is made of individuals which are a graph with [num_vertices] nodes
    private final int populationSize = 3;
    
    /**
     * Other class properties
     */
    // the population of graphs
    private ArrayList<Graph> population = new ArrayList<>(populationSize);
    
    public GeneticAlgorithmSolver()
    {
        maxColors = 4;
    }
    
    @Override
    public void runSolver() 
    {
        initializePopulation();
        setAllFitnesses();
    }
    
    /**
     * Calculates and assigns the fitness for all individuals in the population
     */
    private void setAllFitnesses()
    {
        for (Graph individual : population)
        {
            individual.calculateFitness();
            System.out.println("TESTING");
            individual.printGraph();
        }
    }
    
    /**
     * Assign the input graph to each index in the population array, then
     * randomize the colors of all nodes in all graphs in the population array
     */
    private void initializePopulation()
    {
        Random rand = new Random();
        for (int i = 0; i < populationSize; i++)
        {
            Graph newGraph = deepGraphCopy();
            
            for (Vertex neighbor : newGraph.theGraph.values())
            {
                int randColor = rand.nextInt(maxColors);
                neighbor.color = randColor;
            }
            
            population.add(newGraph);
        }
    }
    
    /**
     * creates new memory locations for each population individual Graph
     */
    private Graph deepGraphCopy()
    {
        Cloner cloner = new Cloner();
        
        Graph newGraphInMemory = cloner.deepClone(graph);
        
        return newGraphInMemory;
    }
}
