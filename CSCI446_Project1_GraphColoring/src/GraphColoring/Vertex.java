package GraphColoring;

/**
 * A vertex / Point / Node that a Graph will be composed of.
 * The Vertex class encapsulates important data values such as location,
 * and color into a single data type
 * @version 09/12/16
 */
public class Vertex 
{
    // The vetex's location on the unit square
    private final double xValue;
    private final double yValue;
    // The "Color" of the vertex. Just a value of 1, 2, 3, or 4.
    private int color;
    // The arbitrary "number" the vertex is identified as according to the
    // 'theGraph' field in TempGraph
    public int vertexNum;
    
    /**
     * Instantiate a vertex with given (x,y) values
     * @param x : x value between [0,1]
     * @param y : y value between [0,1]
     */
    public Vertex(double x, double y)
    {
        xValue = x;
        yValue = y;
    }

    /**
     * @return the xValue
     */
    public double getxValue() 
    {
        return xValue;
    }
    /**
     * @return the yValue
     */
    public double getyValue() 
    {
        return yValue;
    }
    /**
     * @return the color
     */
    public int getColor() {
        return color;
    }
    /**
     * @param color the color to set
     */
    public void setColor(int color) {
        this.color = color;
    }
}
