package GraphColoring;

import com.rits.cloning.Cloner;
import java.util.ArrayList;
import java.util.Iterator;
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
    private final double childMutationRate = 0.20;
    private final double allMutationRate = 0.15;
    // number of population to remove based on highest penalty values
    private final int penaltySize = 1;
    private final int tournamentSize = 2;
    private final int populationSize = 40;
    private final int parentSetSize = populationSize / 2;
    private final int childSetSize = populationSize - parentSetSize;
    
    /**
     * Other class properties
     */
    // the population of graphs
    private final ArrayList<Graph> population = new ArrayList<>(populationSize);
    private ArrayList<Graph> parentSet = new ArrayList<>(parentSetSize);
    private ArrayList<Graph> childSet = new ArrayList<>(childSetSize);
    private Graph bestGraph;
    
    public GeneticAlgorithmSolver()
    {
        maxColors = 4;
    }
    
    // used for displaying run data values
    private final int loopIterationPrintMod = 1000;
    
    @Override
    public void runSolver() 
    {
        runs.println("Tunable parameter settings: ");
        runs.format(" - Population Size: %d%n - Parent Size: %d%n - Child Size: %d%n - Mutation Rate: %f%n - Penalty Size: %d%n - Tournament Size: %d%n", 
                populationSize, parentSetSize, childSetSize, allMutationRate, penaltySize, tournamentSize);
        
        initializePopulation();
        setAllFitnesses();
        
        // loop until constraint is meet
        boolean satisfied = false;
        int loopIteration = 0;
        while (!satisfied && loopIteration < 10000) // use a number in the range of 10^4 officially
        {
            if (loopIteration % loopIterationPrintMod == 0)
                runs.format("%n== Current Generation: %d ==%n", loopIteration);
            // reset parent and child subsets
            parentSet.clear();
            childSet.clear();
            
            // assign parent set though tournament selection
            parentSet = selectParentSet(population);                            addDecision();
            
            // <editor-fold defaultstate="collapsed" desc="Print population and parent population fitnesses">
//            if (loopIteration % loopIterationPrintMod == 0)
//            {
//                runs.println("Current population fitnesses and chromosomes: ");
//                printPopulationValues(population);
//                runs.println("Current parent set fitnesses and chromosomes: ");
//                printPopulationValues(parentSet);
//            }
            // </editor-fold>
            
            // assign children set through crossover
            childSet = selectChildrenSet(parentSet);                            addDecision();
            
            // <editor-fold defaultstate="collapsed" desc="Print population and children population fitnesses">
//            if (loopIteration % loopIterationPrintMod == 0)
//            {
//                runs.println("Current children set fitnesses and chromosomes: ");
//                printPopulationValues(childSet);
//            }
            // </editor-fold>
            
            // mutate some of the children
            // <editor-fold defaultstate="collapsed" desc="Print children chromosomes before mutation">
//            if (loopIteration % loopIterationPrintMod == 0)
//            {
//                runs.println("Children chromosomes before mutation: " );
//                for (Graph child : childSet)
//                {
//                    runs.format("%s, %n", child.getChromosomeArray());
//                }
//                runs.println();
//            }
            // </editor-fold>
            mutateChildren(childSet);
            // <editor-fold defaultstate="collapsed" desc="Print children chromosomes after mutation">
//            if (loopIteration % loopIterationPrintMod == 0)
//            {
//                runs.println("Children chromosomes after mutation: " );
//                for (Graph child : childSet)
//                {
//                    runs.format("%s, %n", child.getChromosomeArray());
//                }
//                runs.println();
//            }
            // </editor-fold>
            
            // generate new population from children and parents
            evolve();                                                           addDecision();
                    
            // apply penalty function to least fit population individuals
            penalize();
            
            // apply a second mutation strategy to all individuals
            mutateAll();
            
            // <editor-fold defaultstate="collapsed" desc="Print evolved and repaired population">
//            runs.println("Evolved and repaired population fitnesses and chromosomes: ");
//            printPopulationValues(population);
            // </editor-fold>
            
            // set bestGraph and determine loop condition. Exit if satisfied
            satisfied = determineStatus();                                      addDecision();
            
            // <editor-fold defaultstate="collapsed" desc="Print best graph state for current generation">
//            if (loopIteration % loopIterationPrintMod == 0)
//            {
//                runs.format("Satisfied value: %b%n", satisfied);
//                runs.println("\nBest Graph Value: " + graph.getFitness() + " out of " + graph.getGraphSize());
//                //bestGraph.printGraph();
//            }
            // </editor-fold>
            
            loopIteration++;
        }
        
        // <editor-fold defaultstate="collapsed" desc="Print final graph state">
        runs.format("%n= Final Generation =%n");
        runs.format("Satisfied value: %b%n", satisfied);
        runs.println("Current population fitnesses and chromosomes: ");
        printPopulationValues(population);
        runs.print("The Graph Value: " + graph.getFitness() + " out of " + graph.getGraphSize());
//        bestGraph.printGraph();
        // </editor-fold>
    }
    
    /**
     * For each individual, pick n% of nodes and change their color to a random 
     * color if the chromosome's fitness is not already a valid coloring
     * @param children 
     */
    private void mutateChildren(ArrayList<Graph> children)
    {
        Random rand = new Random();
        int numMutations = (int) Math.ceil(children.size() * childMutationRate);
        
        // for each child individual 
        for (Graph child : children)
        {
            // for n mutations
            for (int i = 0; i < numMutations; i++)
            {
                // get a random chromosome from the graph
                int randIndex = rand.nextInt(child.theGraph.size());
                Vertex currentVertex = child.theGraph.get(randIndex);
                
                // change its color to a random color if it isn't valid
                if (!currentVertex.getFitness())
                {
                    int newColor = currentVertex.color;
                    while (newColor == currentVertex.color)
                    {
                        newColor = rand.nextInt(maxColors);                     
                    }
                    currentVertex.color = newColor;                             addDecision();
                }
            }
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
                int individualIndex = rand.nextInt(parentSetSize);
                tournamentPlayers[i] = population.get(individualIndex);         addDecision();
            }
            // return the winner of the tournament
            Graph winner = tournamentSelection(tournamentPlayers);
            // and add to the subset to return
            returnedSet.add(winner);                                            addDecision();
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
            parents[0] = input.get(rand.nextInt(input.size()));                 addDecision();
            parents[1] = input.get(rand.nextInt(input.size()));                 addDecision();
            
            ArrayList<Integer> chromosome1 = new ArrayList<>();
            ArrayList<Integer> chromosome2 = new ArrayList<>();
            
            // Enumerate over the length of a chromosome array
            for (int j = 0; j < chromosomeLength; j++)
            {
                if (j < chromosomeLength / 2)
                {
                    int newValue = (parents[0].getChromosomeArray().get(j) + 1) % maxColors;
                    chromosome1.add(newValue);                                  addDecision();
                    chromosome2.add(parents[1].getChromosomeArray().get(j));    addDecision();
                }
                else
                {
                    int newValue = (parents[1].getChromosomeArray().get(j) + 1) % maxColors;
                    chromosome1.add(newValue);                                  addDecision();
                    chromosome2.add(parents[0].getChromosomeArray().get(j));    addDecision();
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
            currentVertex.color = newChromosome.get(i);                         addDecision();
        }
    }
    
    /**
     * Regenerates the population list as a combination of the child and 
     * parent subsets
     */
    private void evolve()
    {
        if (childSet.size() + parentSet.size() != population.size())
        {
            throw new RuntimeException("Union of child set and parent set does not add up to population size.");
        }
        population.clear();
        population.addAll(childSet);
        population.addAll(parentSet);
    }
    
    private void mutateAll()
    {
        Random rand = new Random();
        int numMutations = (int) Math.ceil(graph.getGraphSize() * allMutationRate);
        
        // for each individual
        for (Graph individual : population)
        {
            // for n repairs
            for (int i = 0; i < numMutations; i++)
            {
                // get a random chromosome from the graph
                int randIndex = rand.nextInt(individual.theGraph.size());
                Vertex currentVertex = individual.theGraph.get(randIndex);
                
                // change its color to the most fit color if it isn't valid
                if (!currentVertex.getFitness())
                {
                    int newColor = currentVertex.mostFitColor(maxColors);
                    while (newColor == currentVertex.color)
                    {
                        newColor = rand.nextInt(maxColors);                     
                    }
                    currentVertex.color = newColor;                             addDecision();
                }
                // if it is valid, iterate until a non-valid node is found
                else
                {
                    Iterator vertexItr = individual.theGraph.values().iterator();
                    while (vertexItr.hasNext())
                    {
                        Vertex nextVert = (Vertex) vertexItr.next();
                        if (!nextVert.getFitness())
                        {
                            int newColor = currentVertex.mostFitColor(maxColors);
                            while (newColor == currentVertex.color)
                            {
                                newColor = rand.nextInt(maxColors);
                            }
                            currentVertex.color = newColor;                     addDecision();
                        }
                        break;
                    }
                }
            }
        }
    }
    
    /**
     * remove [penalty_size] individuals from the population
     */
    private void penalize()
    {
        Random rand = new Random();
        
        for (int i = 0; i < penaltySize; i++)
        {
            // find fitness of worst individual
            int lowestFitness = graph.getGraphSize();
            Graph worstIndividual = null;

            for (Graph individual : population)
            {
                if (individual.getFitness() < lowestFitness)
                {
                    lowestFitness = individual.getFitness();
                    worstIndividual = individual;                
                }
            }

            if (worstIndividual == null)
            {
                throw new RuntimeException("The worst individual was never set in penalize function.");
            }

            // remove worst individual
            population.remove(worstIndividual);                                 addDecision();

            // replace with new individual
            Graph newIndividual = deepGraphCopy();
            // randomize color values in new individual
            for (Vertex neighbor : newIndividual.theGraph.values())
            {
                int randColor = rand.nextInt(maxColors);
                neighbor.color = randColor;                                     
            }
            population.add(newIndividual);                                      
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
            individual.calculateFitness();                                      addDecision();
        }
    }
    
    /**
     * Assign the input graph to each index in the population array, then
     * randomize the colors of all nodes in all graphs in the population array
     */
    private void initializePopulation()
    {
        population.clear();
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
        bestGraph = tempFindMostFit();
        graph = bestGraph;
        super.graph = bestGraph;
        // If all colors are valid, exit the loop
        return tempSatisfies();
    }
    
    private Graph tempFindMostFit()
    {
        Graph mostFitGraph = null;
        int mostFitValue = 0;
                
        for (Graph member : population)
        {
            int currentGraphFitness = 0;
            
            for (Vertex chromosome : member.theGraph.values())
            {
                int currentColor = chromosome.color;
                boolean fitChromosome = true;
                
                for (Vertex neighbor : chromosome.edges.values())
                {
                    if (currentColor == neighbor.color)
                    {
                        fitChromosome = false;
                    }
                }
                if (fitChromosome)
                {
                    currentGraphFitness++;
                }
            }
            
            if (currentGraphFitness > mostFitValue)
            {
                mostFitValue = currentGraphFitness;
                mostFitGraph = member;
            }
        }
        
        return mostFitGraph;
    }
    
    private boolean tempSatisfies()
    {
         boolean satisfied = true;
        // for each vertex in the graph...
        for (Vertex vertex : graph.theGraph.values()) 
        {
            int currentColor = vertex.color;
            
            // for each vertex the current vertex is connected to...
            for (Vertex neighbor : vertex.edges.values())
            {
                if (currentColor == neighbor.color)
                {
                    satisfied = false;
                    break;
                }
            }
            if (!satisfied)
            {
                break;
            }
        }
        return satisfied;
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
    }
    
    /**
     * add +1 to decisions made for the current instance run
     */
    public void addDecision()
    {
        decisionsMade++;
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
            runs.format("Individual %d's fitness: %d |", i, individual.getFitness());
            runs.format(" chromosomes: %s%n", theArray.toString());
            i++;
        }
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="All the methods needed for doing merge sort">
    /**
     * Takes an arraylist of graph and sorts them into a new arraylist in order
     * of fitness from worst to best
     * Uses merge sort.
     * Merge sort implementation referenced from 
     * http://www.java2novice.com/java-sorting-algorithms/merge-sort/
     * and nearly every java algorithms book written
     */
    class MergeSort
    {
        private ArrayList<Graph> inputArray;
        private Graph[] tempArray;
        private int length;
        
        /**
         * @param input : The array of graphs to sort
         * @return the graphs in sorted order from worst to best
         */
        private ArrayList<Graph> sort(ArrayList<Graph> input)
        {
            this.inputArray = input;
            length = inputArray.size();
            tempArray = new Graph[length];
            doMergeSort(0, length - 1);
            
            return inputArray;
        }
        
        private void doMergeSort(int lower, int higher)
        {
            if (lower < higher)
            {
                int middle = lower + (higher - lower) / 2;
                doMergeSort(lower, middle);
                doMergeSort(middle+1, higher);
                mergeParts(lower, middle, higher);
            }
        }
        
        private void mergeParts(int lower, int middle, int higher)
        {
            for (int i = lower; i <= higher; i++)
            {
                tempArray[i] = inputArray.get(i);
            }
            int i = lower;
            int j = middle + 1;
            int k = lower;
            while (i <= middle && j <= higher)
            {
                if (getFitnessAtIndex(i, tempArray) <= getFitnessAtIndex(j, tempArray))
                {
                    inputArray.set(k, tempArray[i]);
                    i++;
                }
                else
                {
                    inputArray.set(k, tempArray[j]);
                    j++;
                }
                k++;
            }
            while (i <= middle)
            {
                inputArray.set(k, tempArray[i]);
                k++;
                i++;
            }
        }
        
        private int getFitnessAtIndex(int index, Graph[] array)
        {
            Graph currentGraph = array[index];
            return currentGraph.getFitness();
        }
    }
    // </editor-fold>
}
