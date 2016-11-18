package classification;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * TANNode class represents a single attribute value for a dataset. 
 * Duplicate attribute values are accounted for by incrementing occurrence value
 *
 */
public class TANNode
{
    private int numOccurrences = 1; 
    private int traitValue;
    private int traitIndex;
    private int classifier;
    private HashMap allInfluences = new HashMap();
    private TANNode influence;

    public TANNode(int traitValue, int traitIndex, int classifier)
    {
        this.traitValue = traitValue;
        this.traitIndex = traitIndex;
        this.classifier = classifier;
    }

    public int occurs()
    {
        return numOccurrences; 
    }
    
    public void incrementOccurrence()
    {
        numOccurrences++;
    }
    
    public int getClassifier()
    {
        return classifier;
    }

    public int getTraitValue()
    {
        return traitValue;
    }

    public int getTraitIndex()
    {
        return traitIndex;
    }

    public TANNode getInfluence()
    {
        return influence;
    }

    public void addToAllInfluences(TANNode node, double weight)
    {
        allInfluences.put(node, weight);
    }
    
    public boolean influencesContains(TANNode node)
    {
        if (allInfluences.containsKey(node))
        {
            return true;
        }
        return false; 
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
        return (compareNode.getClassifier() == classifier && compareNode.getTraitIndex() == traitIndex && compareNode.getTraitValue() == traitValue);
    }
}
