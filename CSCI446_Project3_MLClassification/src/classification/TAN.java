package classification;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 *
 *
 */
public class TAN extends Categorizer
{

    private int totalClasses = 0;
    private int[][] foldResult;
    private Map<Integer, Integer> classCounts = new HashMap<Integer, Integer>();
    private Map<Integer, ArrayList<TANNode>> nodesByClass = new HashMap<Integer, ArrayList<TANNode>>();
    private Map<Integer, Integer> indexCounts = new HashMap<Integer, Integer>();

    public TAN(DataSet[] trainingFolds, DataSet testingFold)
    {
        super(trainingFolds, testingFold);
        categorizerName = "TAN";
        // foldResult is an (n x n) matrix where n = number of classifications
        int matrixSize = trainingSet.getNumClassifications();
        foldResult = new int[matrixSize][];
        for (int i = 0; i < foldResult.length; i++)
        {
            foldResult[i] = new int[matrixSize];
        }
    }

    /**
     * Method for controlling training of TAN
     */
    @Override
    public void Train()
    {
        setUpNodes();
        setInfluences();
        setTotalClasses();
    }

    /**
     * Controls iteration and comparisons of testing dataset
     */
    @Override
    public int[][] Test()
    {
        //loop through each line of test data
        for (Vector v : testingFold.getVectors())
        {
            int classification = v.classification();
            int[] features = v.features();
            double[] prob =
            {
                0, 0
            };
            //calculate probability of each class 
            Iterator it = nodesByClass.entrySet().iterator();
            while (it.hasNext())
            {
                Map.Entry pair = (Map.Entry) it.next();
                int cls = (int) pair.getKey();
                //multiply probabilities for each feature
                double localProb = (double) classCounts.get(cls) / totalClasses;
                for (int i = 0; i < features.length; i++)
                {
                    localProb = localProb * getProbability(features[1], i, cls, v);
                }
                if (localProb > prob[0])
                {
                    prob[0] = localProb;
                    prob[1] = cls;
                }
            }
            //System.out.println("Actual:" + classification + " Predicted:" + prob[1]);
            foldResult[classification][(int)prob[1]]++;
        }
        return foldResult;
    }

    /**
     * Initialize nodes that represent attributes
     */
    private void setUpNodes()
    {
        //initialize nodes and add to nodesByClass with classifier as key
        for (Vector v : trainingSet.getVectors())
        {
            int classifier = v.classification();
            int[] features = v.features();
            for (int i = 0; i < features.length; i++)
            {
                //create new node and check if already present
                boolean duplicate = false;
                TANNode newNode = new TANNode(features[i], i, classifier);
                for (ArrayList<TANNode> nodeList : nodesByClass.values())
                {
                    for (TANNode n : nodeList)
                    {
                        TANNode node = (TANNode) n;
                        if (node.equals(newNode))
                        {
                            //increase occurence if found
                            node.incrementOccurence();
                            duplicate = true;
                            break;
                        }
                    }
                }
                //add to class list if not already there 
                if (!duplicate)
                {
                    ArrayList nodeList = nodesByClass.get(classifier);
                    if (nodeList != null)
                    {
                        nodeList.add(newNode);
                        nodesByClass.put(classifier, nodeList);
                    } else
                    {
                        nodesByClass.put(classifier, new ArrayList(Arrays.asList(newNode)));
                    }
                }
            }
            //increase class count 
            if (classCounts.get(classifier) != null)
            {
                int count = classCounts.get(classifier);
                count++;
                classCounts.replace(classifier, count);
            } else
            {
                classCounts.put(classifier, 1);
            }
        }
    }

    /**
     * Set all possible influential nodes for each node
     */
    private void setInfluences()
    {
        Iterator it = nodesByClass.entrySet().iterator();
        while (it.hasNext())
        {
            //calculate relation weight for each node in given class 
            Map.Entry pair = (Map.Entry) it.next();
            ArrayList<TANNode> nodes = (ArrayList) pair.getValue();
            for (TANNode node1 : nodes)
            {
                for (TANNode node2 : nodes)
                {
                    if (node1.getClassifier() == node2.getClassifier() && !node1.equals(node2))
                    {
                        node1.addToAllInfluences(node2, calculateWeight(node1, node2));
                    }
                }
            }
        }
    }

    /**
     * Calculate weights between influential attributes
     */
    private double calculateWeight(TANNode node1, TANNode node2)
    {
        int bothOccur = 0;
        int oneOccurs = 0;
        int twoOccurs = 0;
        int classOccurs = 0;
        int setLength = 0;
        //determine probabilities of  node1 compared to node 2 
        for (Vector v : trainingSet.getVectors())
        {
            setLength++;
            boolean found1 = false;
            boolean found2 = false;
            if (v.classification() == node1.getClassifier())
            {
                classOccurs++;
                if (v.contains(node1.getTraitValue(), node1.getTraitIndex()))
                {
                    oneOccurs++;
                    found1 = true;
                }
                if (v.contains(node2.getTraitValue(), node2.getTraitIndex()))
                {
                    twoOccurs++;
                    found2 = true;
                }
                if (found1 && found2)
                {
                    bothOccur++;
                }
            }
        }
        //prob calculations
        double numerator = (double) bothOccur / classOccurs;
        double denom = ((double) oneOccurs / classOccurs) * ((double) twoOccurs / classOccurs);
        return ((double) bothOccur / setLength) * Math.log(numerator / denom);
    }

    /**
     * Get the probability of an attribute given test attribute info
     */
    private double getProbability(int attVal, int index, int classVal, Vector vector)
    {
        TANNode matchNode = null;
        ArrayList<TANNode> nodes = (ArrayList) nodesByClass.get(classVal);
        //find node that matches test attribute 
        for (TANNode node : nodes)
        {
            if (node.getTraitValue() == attVal && node.getTraitIndex() == index)
            {
                matchNode = node;
                break;
            }
        }
        //calculate probability of found node 
        if (matchNode != null)
        {
            double prob = (double) matchNode.occurs() / classCounts.get(matchNode.getClassifier());
            if (influencePresent(matchNode, vector))
            {
                return prob * 10;
            }
            return prob / 10;
            //Naive: return (double) matchNode.occurs() / classCounts.get(matchNode.getClassifier());
        }
        return .00001;
    }

    /**
     * Calculate total number of classes and set most influential nodes
     */
    private void setTotalClasses()
    {
        int total = 0;
        //iterate through classes and add
        Iterator it = classCounts.entrySet().iterator();
        while (it.hasNext())
        {
            Map.Entry pair = (Map.Entry) it.next();
            int count = (int) pair.getValue();
            total += count;
        }
        totalClasses = total;
        //iterate through all nodes and set final influences
        Iterator it2 = nodesByClass.entrySet().iterator();
        while (it2.hasNext())
        {
            Map.Entry pair = (Map.Entry) it2.next();
            ArrayList<TANNode> nodes = (ArrayList) pair.getValue();
            for (TANNode node : nodes)
            {
                node.setMostInfluential();
            }
        }
    }

    /**
     * Determine if influential node is present in current test vector
     */
    private boolean influencePresent(TANNode node, Vector vector)
    {
        int classifier = vector.classification();
        int[] features = vector.features();
        TANNode influence = node.getInfluence();
        //look through current vector for match on influence node
        for (int i = 0; i < features.length; i++)
        {
            if (influence != null && influence.getClassifier() == classifier && influence.getTraitValue() == features[i] && influence.getTraitIndex() == i)
            {
                return true;
            }
        }
        return false;
    }
}
