package classification;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

public class TempMain_David 
{
    public static void main(String[] args) throws FileNotFoundException 
    {
        // pre-process iris.txt and assign it to a Data class instance
        Scanner s0 = new Scanner(new File("DataSets/iris.txt"));
        int lineNums = 0;
        while (s0.hasNextLine())
        {
            lineNums++;
            s0.nextLine();
        }
        s0.close();
        
        double[][] list = new double[lineNums][];
        Scanner s1 = new Scanner(new File("DataSets/iris.txt"));
        int i = 0;
        while (s1.hasNextLine())
        {
            String rowString = s1.nextLine();
            String[] rowSplit = rowString.split(",");
            list[i] = new double[rowSplit.length];
            for (int j = 0; j < list[i].length; j++)
            {
                String value = rowSplit[j];
                // classification value is the last entry, transform into double
                // and place at index 0 
                if (j == list[i].length - 1)
                {
                    double classification = -1;
                    switch(value)
                    {
                        case("Iris-setosa"):
                            classification = 0;
                            break;
                        case("Iris-versicolor"):
                            classification = 1;
                            break;
                        case("Iris-virginica"):
                            classification = 2;
                            break;
                        default:
                            throw new RuntimeException("couldn't switch off of iris class value");
                    }
                    list[i][0] = classification;
                }
                else
                {
                    int colIndex = j+1;
                    list[i][colIndex] = Double.parseDouble(value);
                }
            }
            i++;
        }
        s1.close();
        
        // print the array
        for (int i1 = 0; i1 < list.length; i1++)
        {
            System.out.print("[");
            for (int j = 0; j < list[i1].length; j++)
            {
                System.out.print(list[i1][j] + ",");
            }
            System.out.print("]");
            System.out.println();
        }
        
        Categorizer kNN = new NearestNeighbor(list);
    }
}
