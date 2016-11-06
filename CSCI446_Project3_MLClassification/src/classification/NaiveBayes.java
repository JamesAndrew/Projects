/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classification;

/**
 *
 * @author James
 */
public class NaiveBayes 
{
    public NaiveBayes()
    {
        System.out.println("=== Naive Bayes Experiment ===");
        DummyNB();
    }
    
    private void DummyNB()
    {
        System.out.println("=== Dummy Naive Bayes Method ===");
        int [][] train_data = { {1000025, 5, 1, 1, 1, 2, 1, 3, 1, 1, 2},
                          {1017122, 8, 10, 10, 8, 7, 10, 9, 7, 1, 4},
                          {1036172, 2, 1, 1, 1, 2, 1, 2, 1, 1, 2},
                          {1041801, 5, 3, 3, 3, 2, 3, 4, 4, 1, 4}};
        int [][] test_data = { {1047630, 7, 4, 6, 4, 6, 1, 4, 3, 1, 4},
                               {1048672, 4, 1, 1, 1, 2, 1, 2, 1, 1, 2} };

        
        int attributes = 10;
        int classes = 2;
        
        double [][] stats = new double[classes][2];
                
        int [] freq = new int [classes];
        
        for (int i = 0; i < train_data.length; i++)
        {
            for(int j = 0; j < train_data[i].length; j++)
            {
                System.out.print(train_data[i][j] + " ");
                if (j == (train_data[i].length - 1))
                {
                    if (train_data[i][j] == 2)
                    {
                        freq[0]++;
                        System.out.print("Frequency of 2 = " + freq[0]);
                    }
                    else if (train_data[i][j] == 4)
                    {
                        freq[1]++;
                        System.out.print("Frequency of 4 = " + freq[1]);
                    }
                }
                
            }
            System.out.println();
            if (i == (train_data.length-1))
            {
                stats[0][0] = (double)freq[0]/(double)train_data.length;
                stats[1][0] = (double)freq[1]/(double)train_data.length;
            }
            
            System.out.println();
        }
        
        
        System.out.println("Prior Probability for class 2 = " + stats[0][0]);
        System.out.println("Prior Probability for class 4 = " + stats[1][0]);
    }
    
    // calculate probabilities
}
