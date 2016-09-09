package GraphColoring;

// class Graph represents a graph object with attributes points and matrix 
public class Graph {

    private final float[][] points; // stores the points of vertices 
    private final int[][] matrix; // stores the adjacency matrix representing the edges

    // constructor to initialize Graph class and its attributes
    public Graph(int n) 
    {
        points = new float[n][2];
        matrix = new int[n][n];
    }

    // method to set points coordinates given index i, value of x, and value of y 
    public void setPoint(int i, float x, float y) 
    {
        points[i][0] = x;
        points[i][1] = y;
    }

    // method to get x points coordinate 
    public float getXPoint(int i) 
    {
        return points[i][0];
    }

    // method to get y points coordinate 
    public float getYPoint(int i) 
    {
        return points[i][1];
    }

    // method to set points values given index i, index j, and value of v
    public void setEdge(int i, int j, int v) 
    {
        matrix[i][j] = v;
    }

    // method to get matrix value given index i and index j
    public int getEdge(int i, int j) 
    {
        return matrix[i][j];
    }

    /**
     * @return the points
     */
    public float[][] getPoints() {
        return points;
    }
}
