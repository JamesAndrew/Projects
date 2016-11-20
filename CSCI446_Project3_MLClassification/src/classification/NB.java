package classification;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * TAN class is an implementation of Naive Bayes.
 * Receives an array of training sets and a single test set as class parameters and
 * and makes predictions on test data classifiers based on learned training data.
 */
public class NB extends Categorizer
{

    private int totalClasses = 0;
    private int[][] foldResult;
    private Map<Integer, Integer> classCounts = new HashMap<>();
    private Map<Integer, ArrayList<TANNode>> nodesByClass = new HashMap<>();
    private Map<Integer, Integer> indexCounts = new HashMap<>();

    public TAN(DataSet[] trainingFolds, DataSet testingFold)
    {
        super(trainingFolds, testingFold);
        categorizerName = "Naive-Bayes";
        // foldResult is an (n x n) matrix where n = number of classifications
        int matrixSize = trainingSet.getNumClassifications();
        foldResult = new int[matrixSize][];
        for (int i = 0; i < foldResult.length; i++)
        {
            foldResult[i] = new int[matrixSize];
        }
    }

    /**
     * Method for controlling training of NB
     */
    @Override
    public void Train()
    {
        System.out.println("Training");
        System.out.println("Calculating probabilities of training data");
        setUpNodes();
        setTotalClasses();
    }

    /**
     * Controls iteration and comparisons of testing dataset
     */
    @Override
    public int[][] Test()
    {
        System.out.println("Testing");
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
//            System.out.println("Actual class:" + classification + " Predicted class:" + prob[1]);
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
                            node.incrementOccurrence();
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
            return prob;
        }
        return .00001;
    }

    /**
     * Calculate total number of classes
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
    }
}
