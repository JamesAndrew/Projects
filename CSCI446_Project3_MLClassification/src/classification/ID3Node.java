
package classification;

import java.util.HashMap;

/**
 * The nodes that make up the ID3. 
 * A node is responsible for having an assigned attribute or classification property.
 * 
 * @author David
 */
public class ID3Node 
{
    // node value is the int representation of either the target feature or classification
    private int nodeValue;
    // Use a hashmap to represent the collection of branch. Traverse by 
    // querying the associated node for an integer feature value as the key
    private HashMap<Integer, ID3Node> children = new HashMap<>();
    
    // often the node construction will not immediately know its value
    public ID3Node() { }
    public ID3Node(int value)
    {
        nodeValue = value;
    }
    
    public boolean isLeaf()
    {
        if (children.isEmpty()) return true;
        else return false;
    }
    
    public int getNodeValue()
    {
        return nodeValue;
    }
    public void setNodeValue(int nodeValue) 
    {
        this.nodeValue = nodeValue;
    }
}
