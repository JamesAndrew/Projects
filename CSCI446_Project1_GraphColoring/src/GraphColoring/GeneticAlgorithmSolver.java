package GraphColoring;

import java.util.HashMap;
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
    /**
     * Tunable parameters
     */
    private double childPopRatio = 0.5;
    private int tournamentSize = 2;
    private double repairChance = 0.10;
    // a value of 1 means only repair the current choromosome. 2 means repair
    // the chromosome and its connected vertices, etc.
    private int repairDepth = 1;
    
    /**
     * Other class properties
     */
    int parentSetSize;
    private Map<Integer, Vertex> parentSet;
    int childSetSize;
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
        for (int i = 0; i < 1; i++)
        //while (!satisfied)
        {
            // select parent set to keep based on tournament selection
            int[] leftoverVertexNums = selectParentSet();
            
            // apply crossover to generate children
            crossover(leftoverVertexNums);
            
            // apply tournament selection, crossover, mutation until 
            
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
    
    /**
     * Apply tournament selection until the parent set is full
     * @returns an integer array of the vertex numbers not used by the parents
     */
    private int[] selectParentSet()
    {
        Random rand = new Random();
        Vertex[] tournamentPlayers = new Vertex[tournamentSize];
        
        // loop until parent set is full
        while (parentSet.size() < parentSetSize)
        {
            // assign [tournament_size] players at random
            for (int i = 0; i < tournamentSize; i++)
            {
                int chromosomeIndex = rand.nextInt(graph.getGraphSize());
                tournamentPlayers[i] = theGraph.get(chromosomeIndex);
            }
            // return the winner of the tournament
            Vertex winner = tournamentSelection(tournamentPlayers);
            // and add to parentSet if a it hasn't been added yet
            parentSet.putIfAbsent(winner.getVertexNum(), winner);
        }
        
        // generate int array of vertex numbers that are not parents
        int[] leftoverVertexNums = new int[childSetSize];
        int value = 0;
        
        for (int i = 0; i < leftoverVertexNums.length; i++)
        {
            while (parentSet.containsKey(value))
            {
                value++;
            }
            leftoverVertexNums[i] = value;
            value++;
        }
        
        // <editor-fold defaultstate="collapsed" desc="Print chosen and leftover vertex numbers">
        System.out.println("Parent set vertex numbers: ");
        for (Vertex vertex : parentSet.values())
        {
            System.out.format("%d, ", vertex.getVertexNum());
        }
        System.out.println();
        System.out.println("Leftover vertex numbers: ");
        for (int entry : leftoverVertexNums)
        {
            System.out.format("%d, ", entry);
        }
        System.out.println();
        // </editor-fold>
        
        return leftoverVertexNums;
    }
    
    private void setAllFitnesses()
    {
        for (int key : theGraph.keySet())
        {
            Vertex currentVert = theGraph.get(key);
            currentVert.fitness = currentVert.calculateFitness();
        }
    }
    
    /**
     * Choose the most fit vertex out of a list of combatants 
     * @param players : an array of [tournament_size] Vertices
     * @return the most fit vertex
     */
    private Vertex tournamentSelection(Vertex[] players)
    {
        double bestFitness = 0;
        Vertex winner = null;
        
        for (Vertex player : players)
        {
            double currentFitness = player.calculateFitness();
            if (currentFitness >= bestFitness)
            {
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
     * Fill the children set using crossover
     * @param leftoverVertexNums : IDs to be assigned to by children 
     */
    private void crossover(int[] leftoverVertexNums)
    {
        // loop until child set is filled
        while (childSet.size() < childSetSize)
        {
            
        }
    }
    
    /**
     * Overrides updateGraph to also provide values for the chromosome 
     * subsets
     * @param graph 
     */
    @Override
    public void updateGraph(Graph graph)
    {
        this.graph = graph;
        this.theGraph = graph.theGraph;
        
        double temp = Math.floor(graph.getGraphSize() * childPopRatio);        
        childSetSize = (int) temp;
        parentSetSize = graph.getGraphSize() - childSetSize;
        
        childSet = new HashMap<>(childSetSize);
        parentSet = new HashMap<>(parentSetSize);
        
        if (parentSetSize + childSetSize != graph.theGraph.size())
        {
            System.out.format("parent set size: %d, child set size: %d%n", parentSetSize, childSetSize);
            throw new RuntimeException("parentSet and childSet size do not add to chromosome size.");
        }
    }
}
