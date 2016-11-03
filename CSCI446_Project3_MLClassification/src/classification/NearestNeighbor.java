package classification;

import java.util.Arrays;

public class NearestNeighbor implements Categorizer
{
    private double[][] dataSet;
    
    public NearestNeighbor(double[][] in_dataSet) 
    { 
        dataSet = in_dataSet;
        normalizeValues();
        printDataSet();
    }
    
    @Override
    public void Train() 
    {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public void Test() 
    {
        throw new UnsupportedOperationException("Not supported yet."); 
    }
    
    private void normalizeValues()
    {
        double[] feature1 = new double[dataSet.length];
        double[] feature2 = new double[dataSet.length];
        double[] feature3 = new double[dataSet.length];
        double[] feature4 = new double[dataSet.length];
        
        for (int i = 0; i < dataSet.length; i++)
        {
            feature1[i] = dataSet[i][1];
            feature2[i] = dataSet[i][2];
            feature3[i] = dataSet[i][3];
            feature4[i] = dataSet[i][4];
        }
        
        Arrays.sort(feature1);
        Arrays.sort(feature2);
        Arrays.sort(feature3);
        Arrays.sort(feature4);
        
        double feat1Min = feature1[0];
        double feat1Max = feature1[feature1.length - 1];
        
        double feat2Min = feature2[0];
        double feat2Max = feature2[feature2.length - 1];
        
        double feat3Min = feature3[0];
        double feat3Max = feature3[feature3.length - 1];
        
        double feat4Min = feature4[0];
        double feat4Max = feature4[feature4.length - 1];
        
        for (int i = 0; i < dataSet.length; i++)
        {
            dataSet[i][1] = (dataSet[i][1] - feat1Min)/ (feat1Max - feat1Min);
            dataSet[i][2] = (dataSet[i][2] - feat2Min)/ (feat2Max - feat2Min);
            dataSet[i][3] = (dataSet[i][3] - feat3Min)/ (feat3Max - feat3Min);
            dataSet[i][4] = (dataSet[i][4] - feat4Min)/ (feat4Max - feat4Min);
        }
    }
    
    private void printDataSet()
    {
        for (int i1 = 0; i1 < dataSet.length; i1++)
        {
            System.out.print("[");
            for (int j = 0; j < dataSet[i1].length; j++)
            {
                System.out.format("%.4f,", dataSet[i1][j]);
            }
            System.out.print("]");
            System.out.println();
        }
    }
}