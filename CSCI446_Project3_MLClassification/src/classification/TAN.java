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
    private Map<Integer, Integer> classCounts = new HashMap<Integer, Integer>(); 
    private Map<Integer, ArrayList<TANNode>> nodesByClass = new HashMap<Integer, ArrayList<TANNode>>();

    public TAN(DataSet[] trainingFolds, DataSet testingFold)
    {
        super(trainingFolds, testingFold);
        categorizerName = "TAN";
    }
    
    @Override
    public void Train()
    {        
        setUpNodes();
        setInfluences(); 
    }
    
    @Override
    public int[][] Test()
    {
        return null; 
    }

    private void setUpNodes()
    {
        //initialize nodes and add to nodesByClass with classifier as key
        for (Vector v : trainingSet.getVectors())
        {
            int classifier = v.classification();
            int[] features = v.features();
            for (int i = 1; i < features.length; i++)
            {
                boolean duplicate = false; 
                TANNode newNode = new TANNode(features[i], i, classifier);
                //System.out.println("NewVal:" + newNode.getTraitValue() + " index:" + newNode.getTraitIndex() + " Class:" + newNode.getClassifier());
                for (ArrayList<TANNode> nodeList : nodesByClass.values())
                {
                    for (TANNode n : nodeList)
                    {
                        TANNode node = (TANNode) n; 
                        //System.out.println("Val:" + node.getTraitValue() + " index:" + node.getTraitIndex() + " Class:" + node.getClassifier());                        
                        if (node.equals(newNode))
                        {
                            node.incrementOccurence();
                            duplicate = true; 
                            break;
                        }
                    }
                }
                if (!duplicate)
                {                    
                    ArrayList nodeList = nodesByClass.get(classifier);
                    if (nodeList != null)
                    {
                        nodeList.add(newNode); 
                        nodesByClass.put(classifier, nodeList); 
                    }
                    else
                    {
                        nodesByClass.put(classifier, new ArrayList(Arrays.asList(newNode))); 
                    }                    
                    if (classCounts.get(classifier) != null)
                    {
                        int count = classCounts.get(classifier);
                        count++; 
                        classCounts.replace(classifier, count); 
                    }
                    else
                    {
                        classCounts.put(classifier, 1);
                    }
                }                    
            }
        }
    }

    private void setInfluences()
    {
        Iterator it = nodesByClass.entrySet().iterator();
        while (it.hasNext())
        {
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

    private double calculateWeight(TANNode node1, TANNode node2)
    {
        int bothOccur = 0;
        int oneOccurs = 0;
        int twoOccurs = 0;
        int classOccurs = 0; 
        int setLength = 0; 

        for (Vector v : trainingSet.getVectors())
        {
            setLength++; 
            boolean found1 = false; 
            boolean found2 = false; 
            if (v.classification() == node1.getClassifier()) // (todo: classification now changed to integer)
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
        double numerator = (double) bothOccur / classOccurs; 
        double denom = ((double) oneOccurs / classOccurs) * ((double) twoOccurs / classOccurs); 
        return ((double) bothOccur / setLength) * Math.log(numerator / denom);
    }
    
    public double getProbability(double attVal, int index, double classVal)
    {
        TANNode matchNode = null; 
        ArrayList<TANNode> nodes = (ArrayList) nodesByClass.get(classVal); 
        for (TANNode node : nodes) 
        {
            if (node.getTraitValue() == attVal && node.getTraitIndex() == index)
            {
                matchNode = node; 
                break; 
            }
        }
        if (matchNode != null)
        {
            //if vector does not contain influencer, decrease prob
        }
        return 0.0001; 
    }
}
