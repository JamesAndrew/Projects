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
public class TAN
{

    private DataSet dataSet;
    private Map nodesByClass = new HashMap();

    public TAN(DataSet set)
    {
        this.dataSet = set;
    }

    public void setUpNodes(DataSet set)
    {
        //initialize nodes and add to nodesByClass with calssifier as key
    }

    public void setInfluences()
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

                }
            }
        }
    }

    public double calculateWeight(TANNode node1, TANNode node2)
    {
        int bothOccur = 0;
        int oneOccurs = 0;
        int twoOccurs = 0;
        int classOccurs = 0; 
        int setLength = 0; 

        for (Vector v : dataSet.vectors)
        {
            setLength++; 
            boolean found1 = false; 
            boolean found2 = false; 
            if (v.get(0) == node1.getClassifier())
            {
                classOccurs++; 
                if (v.contains(node1.getTraitValue(), node1.getTraitNumber()))
                {
                    oneOccurs++;
                    found1 = true; 
                }
                if (v.contains(node2.getTraitValue(), node2.getTraitNumber()))
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
}
