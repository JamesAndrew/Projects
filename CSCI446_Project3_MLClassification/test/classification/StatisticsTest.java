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
        String alg1Name = "alg1";
        String alg2Name = "alg2";
        
        String dataSet1Name = "dataset1";
        String dataSet2Name = "dataset2"; 
        
        double[][] matrix1 = new double[][]{
            {0, 2, 3, 2},
            {0, 1, 2, 1},
            {1, 4, 2, 3},
            {1, 3, 4, 3}
        };
        double[][] matrix2 = new double[][]{
            {0, 1, 2, 3},
            {1, 2, 3, 4},
            {0, 4, 2, 3},
            {1, 3, 4, 3}
        };
        
        Statistics.updateMatrix(alg1Name, dataSet1Name, matrix1);
        Statistics.updateMatrix(alg2Name, dataSet2Name, matrix2);
        
        double[][] testAlg1Data = Statistics.getResults().get("alg1").get("dataset1");
        double[][] testAlg2Data = Statistics.getResults().get("alg2").get("dataset2");
        
        System.out.println("testAlg1Data: ");
        for (double[] row : testAlg1Data)
        {
            for (double entry : row)
            {
                System.out.format("%.2f ", entry);
            }
            System.out.println();
        }
        System.out.println("testAlg2Data: ");
        for (double[] row : testAlg2Data)
        {
            for (double entry : row)
            {
                System.out.format("%.2f ", entry);
            }
            System.out.println();
        }
        
        assertArrayEquals(matrix1, testAlg1Data);
        assertArrayEquals(matrix2, testAlg2Data);
    } 
}
