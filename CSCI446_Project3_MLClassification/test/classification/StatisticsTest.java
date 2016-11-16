/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classification;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author David
 */
public class StatisticsTest 
{
    @Test
    public void testUpdateMatrix() 
    {
        String algName = "test alg name";
        String dataSetName = "test data set name";
        double[][] matrix = new double[][]{
            {0, 2, 3, 2},
            {0, 1, 2, 1},
            {1, 4, 2, 3},
            {1, 3, 4, 3}
        };
        Statistics.updateMatrix(algName, dataSetName, matrix);
        double[][] testAlgData = Statistics.getResults().get("test alg name").get("test data set name");
        for (double[] row : testAlgData)
        {
            for (double entry : row)
            {
                System.out.format("%.2f ", entry);
            }
            System.out.println();
        }
        
        assertArrayEquals(matrix, testAlgData);
    } 
}
