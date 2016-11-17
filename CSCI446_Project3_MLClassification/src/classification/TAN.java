package classification;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 *
 */
public class TAN extends Categorizer
{
    private Map classCounts = new HashMap(); 
    private Map nodesByClass = new HashMap();

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
        //initialize nodes and add to nodesByClass with calssifier as key
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
