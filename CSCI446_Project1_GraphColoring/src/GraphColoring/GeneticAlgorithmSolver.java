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
     * In order to be dynamically set, some instantiations are found in the 
     * update graph method
     */
    // The population is made of individuals which are a graph with [num_vertices] nodes
    private final int tournamentSize = 2;
    // population size must equal the number of vertices in the graph
    private int populationSize;
    
    private int parentSetSize;
    private ArrayList<Graph> parentSet;
    
    private int childSetSize;
    private ArrayList<Graph> childSet; 
    
    /**
     * Other class properties
     */
    // the population of graphs
    private ArrayList<Graph> population;
    private Graph bestGraph;
    
    public GeneticAlgorithmSolver()
    {
        maxColors = 4;
    }
    
    @Override
    public void runSolver() 
    {
        initializePopulation();
        setAllFitnesses();
        
        // loop until constraint is meet
        boolean satisfied = false;
        int loopIteration = 0;
        //while (!satisfied)
        for (int i = 0; i < 3; i++)
        {
            System.out.format("%n== Loop Iteration: %d ==%n", loopIteration);
            // reset parent and child subsets
            parentSet.clear();
            childSet.clear();
            
            // assign parent set though tournament selection
            parentSet = selectParentSet(population);
            
            // <editor-fold defaultstate="collapsed" desc="Print population and parent population fitnesses">
//            System.out.println("Current population fitnesses: ");
//            printPopulationValues(population);
//            System.out.println("Current parent set fitnesses: ");
//            printPopulationValues(parentSet);
            // </editor-fold>
            
            // assign children set through crossover
            childSet = selectChildrenSet(parentSet);
            
            // <editor-fold defaultstate="collapsed" desc="Print population and parent population fitnesses">
//            System.out.println("Current children set fitnesses and chromosomes: ");
//            printPopulationValues(childSet);
            // </editor-fold>
            
            // set bestGraph and determine loop condition. Exit if satisfied
            satisfied = determineStatus();
            loopIteration++;
        }
    }
    
    /**
     * Apply tournament selection until the parent set is full
     * @returns a graph collection of parent set
     */
    private ArrayList<Graph> selectParentSet(ArrayList<Graph> input)
    {
        Random rand = new Random();
        Graph[] tournamentPlayers = new Graph[tournamentSize];
        ArrayList<Graph> returnedSet = new ArrayList<>();
        
        // loop until parent set is full
        while (returnedSet.size() < parentSetSize)
        {
            // assign [tournament_size] players at random
            for (int i = 0; i < tournamentSize; i++)
            {
                int individualIndex = rand.nextInt(graph.getGraphSize());
                tournamentPlayers[i] = population.get(individualIndex);
            }
            // return the winner of the tournament
            Graph winner = tournamentSelection(tournamentPlayers);
            // and add to the subset to return
            returnedSet.add(winner);
        }
        
        return returnedSet;
    }
    
    /**
     * Apply crossover by picking two individuals from the parent population 
     * which generate two new children from half of the chromosomes from each parent 
     * @param input : the parent subset of the population
     * @return returnedSet : a filled list of children individuals
     */
    private ArrayList<Graph> selectChildrenSet(ArrayList<Graph> input)
    {
        Random rand = new Random();
        Graph[] parents = new Graph[2];
        ArrayList<Graph> returnedSet = new ArrayList<>();
        int chromosomeLength = graph.getGraphSize();
        
        // assign new graph instances to each child 
        for (int i = 0; i < childSetSize; i++)
        {
            returnedSet.add(deepGraphCopy());
        }
        
        // loop through child set and update colors
        for (int i = 0; i < returnedSet.size(); i++)
        {
            parents[0] = input.get(rand.nextInt(input.size()));
            parents[1] = input.get(rand.nextInt(input.size()));
            
            ArrayList<Integer> chromosome1 = new ArrayList<>();
            ArrayList<Integer> chromosome2 = new ArrayList<>();
            
            // Enumerate over the length of a chromosome array
            for (int j = 0; j < chromosomeLength; j++)
            {
                if (j < chromosomeLength / 2)
                {
                    chromosome1.add(parents[0].getChromosomeArray().get(j));
                    chromosome2.add(parents[1].getChromosomeArray().get(j));
                }
                else
                {
                    chromosome1.add(parents[1].getChromosomeArray().get(j));
                    chromosome2.add(parents[0].getChromosomeArray().get(j));
                }
            }
            
            // <editor-fold defaultstate="collapsed" desc="Print Crossover Process">
//            System.out.println("TESTING CHILDREN");
//            System.out.format("parent1 array: %s%n", parents[0].getChromosomeArray().toString());
//            System.out.format("parent2 array: %s%n", parents[1].getChromosomeArray().toString());
//            System.out.format("chromosome1 array: %s%n", chromosome1.toString());
//            System.out.format("chromosome2 array: %s%n", chromosome2.toString());
            // </editor-fold>
            
            // add the chromosomes to the returned children set
            updateChromosomes(returnedSet, chromosome1, i);
            i++;
            if (i >= returnedSet.size())
            {
                break;
            }
            updateChromosomes(returnedSet, chromosome2, i);
        }
        
        return returnedSet;
    }
    
    /**
     * @param returnedSet : The subpopulation of children individuals
     * @param newChromosome : the int array to set each vertex's color (in order) to
     * @param index : the current vertex index to target in the returnedSet
     */
    private void updateChromosomes(ArrayList<Graph> returnedSet, ArrayList<Integer> newChromosome, int index)
    {
        Graph currentGraph = returnedSet.get(index);
        // for each vertex in the current graph
        for (int i = 0; i < newChromosome.size(); i++)
        {
            Vertex currentVertex = currentGraph.theGraph.get(i);
            currentVertex.color = newChromosome.get(i);
        }
    }
    
    /**
     * Choose the most fit graph out of a list of combatants 
     * @param players : an array of [tournament_size] graphs
     * @return the most fit graph
     */
    private Graph tournamentSelection(Graph[] players)
    {
        int bestFitness = 0;
        Graph winner = null;
        
        for (Graph player : players)
        {
            int currentFitness = player.calculateFitness();
            if (currentFitness >= bestFitness)
            {
                bestFitness = currentFitness;
                winner = player;
            }
        }
        if (winner == null)
        {
            throw new RuntimeException("A winner was never set during tournament selection");
        }
        
        return winner;
    }
    
    /**
     * Calculates and assigns the fitness for all individuals in the population
     */
    private void setAllFitnesses()
    {
        for (Graph individual : population)
        {
            individual.calculateFitness();
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
     * finds the most fit individual in the population
     */
    private Graph findMostFit()
    {
        int fitness = 0;
        Graph mostFitGraph = null;
        
        for (Graph individual : population)
        {
            if (individual.getFitness() > fitness)
            {
                mostFitGraph = individual;
            }
        }
        
        if (mostFitGraph == null)
        {
            throw new RuntimeException("Nothing was assigned to mostFitGraph in findMostFit() method.");
        }
        
        return mostFitGraph;
    }
    
    private boolean determineStatus()
    {
        // Assign graph to the most fit graph in the population
        bestGraph = findMostFit();
        graph = bestGraph;
        // If all colors are valid, exit the loop
        return graphSatisfiesConstraint();
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
    
    @Override
    public void updateGraph(Graph graph)
    {
        this.graph = graph;
        this.theGraph = graph.theGraph;
        
        // dynamically set tunable parameters
        populationSize = graph.getGraphSize();
        parentSetSize = populationSize / 2;
        parentSet = new ArrayList<>(parentSetSize);
        childSetSize = populationSize - parentSetSize;
        childSet = new ArrayList<>(childSetSize);
        population = new ArrayList<>(populationSize);
    }
    
    // <editor-fold defaultstate="collapsed" desc="Various print methods">
    /**
     * Output each population's size and fitness
     * @param graphSet : the list of Graph items to get values on
     */
    public void printPopulationValues(ArrayList<Graph> graphSet)
    {
        int i = 0;
        for (Graph individual : graphSet)
        {
            ArrayList<Integer> theArray = individual.getChromosomeArray();
            System.out.format("Individual %d's fitness: %d |", i, individual.getFitness());
            System.out.format(" chromosomes: %s%n", theArray.toString());
            i++;
        }
    }
    // </editor-fold>
}
