
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
    // Use a hashmap to represent the collection of branches (edges and child nodes). 
    // Traverse by querying the associated node for an integer feature value as the key
    private HashMap<Integer, ID3Node> children = new HashMap<>();
    private boolean isMasterRoot = false;
    
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
    
    /**
     * @return 'F(nodeValue)' if the node has a feature value (is not a root)
     *         and 'C(nodeValue)' if node is a classification (is root)
     */
    public String printValue()
    {
        String value = "value not set";
        if (isLeaf())       value = "C(" + nodeValue + ")";
        else if (!isLeaf()) value = "F(" + nodeValue + ")";
        return value;
    }
    
    public int getNodeValue()
    {
        return nodeValue;
    }
    public void setNodeValue(int nodeValue) 
    {
        this.nodeValue = nodeValue;
    }
    public HashMap<Integer, ID3Node> getChildren() 
    {
        return children;
    }
    public void setChildren(HashMap<Integer, ID3Node> children) 
    {
        this.children = children;
    }
    public boolean isIsMasterRoot() 
    {
        return isMasterRoot;
    }
    public void setIsMasterRoot(boolean isMasterRoot) 
    {
        this.isMasterRoot = isMasterRoot;
    }
}
