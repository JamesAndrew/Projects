package classification;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 *
 *
 */
public class TANNode
{
    private int numOccurences = 1; 
    private double traitValue;
    private int traitNumber;
    private double classifier;
    private HashMap allInfluences = new HashMap();
    private TANNode influence;

    public TANNode(double traitValue, int traitNumber, double classifier)
    {
        this.traitValue = traitValue;
        this.traitNumber = traitNumber;
        this.classifier = classifier;
    }

    public int occurs()
    {
        return numOccurences; 
    }
    
    public double getClassifier()
    {
        return classifier;
    }

    public double getTraitValue()
    {
        return traitValue;
    }

    public int getTraitNumber()
    {
        return traitNumber;
    }

    public TANNode getInfluence()
    {
        return influence;
    }

    public void addToAllInfluences(TANNode node, double weight)
    {
        allInfluences.put(node, weight);
    }

    public void setMostInfluential()
    {
        double highestWeight = 0; 
        Iterator it = allInfluences.entrySet().iterator();
        while (it.hasNext())
        {
            Map.Entry pair = (Map.Entry) it.next();
            TANNode key = (TANNode) pair.getKey();
            double weight = (double) pair.getValue();
            if (weight > highestWeight)
            {
                influence = key; 
            }
        }
    }

    public boolean equals(TANNode compareNode)
    {
        if (compareNode.getClassifier() == classifier && compareNode.getTraitNumber() == traitNumber && compareNode.getTraitValue() == traitValue)
        {
            return true;
        }
        return false;
    }
}
