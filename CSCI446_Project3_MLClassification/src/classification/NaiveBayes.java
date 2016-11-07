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

        
        int attributes = 9;
        int attr_values = 9;
        int classes = 2;
        
        double [] class_stats = new double[classes];
        double [][][] attr_stats = new double[classes][attributes+1][attr_values+1];
                
        int [] class_freq = new int [classes];
        int [][][] attr_freq = new int [classes][attributes+1][attr_values+2];
                
        for (int i = 0; i < train_data.length; i++)
        {
            for(int j = 1; j < train_data[i].length; j++)
            {
                System.out.print(train_data[i][j] + " ");
                
                // check if class label
                if (j == (train_data[i].length - 1))
                {
                    if (train_data[i][j] == 2)
                    {
                        class_freq[0]++;
                        System.out.print("Frequency of 2 = " + class_freq[0]);
                    }
                    else if (train_data[i][j] == 4)
                    {
                        class_freq[1]++;
                        System.out.print("Frequency of 4 = " + class_freq[1]);
                    }
                }
                
                // else assume attribute value
                else
                {
                    int class_label = train_data[i][train_data[i].length-1];
                    int c = 0;
                    if(class_label == 2)
                    {
                        c = 0;
                    }
                    else if (class_label == 4)
                    {
                        c = 1;
                    }
                    // check if attribute i is equal to some value k from 0 to 10
                    switch (train_data[i][j]) {
                        case 0:
                            attr_freq[c][j][0]++;
                            break;
                        case 1:
                            attr_freq[c][j][1]++;
                            break;
                        case 2:
                            attr_freq[c][j][2]++;
                            break;
                        case 3:
                            attr_freq[c][j][3]++;
                            break; 
                        case 4:
                            attr_freq[c][j][4]++;
                            break;
                        case 5:
                            attr_freq[c][j][5]++;
                            break;
                        case 6:
                            attr_freq[c][j][6]++;
                            break;
                        case 7:
                            attr_freq[c][j][7]++;
                            break;
                        case 8:
                            attr_freq[c][j][8]++;
                            break;
                        case 9:
                            attr_freq[c][j][9]++;
                            break;
                        case 10:
                            attr_freq[c][j][10]++;
                            break;
                        default:
                            break;
                    }
                }
                
                
            }
            System.out.println();
            if (i == (train_data.length-1))
            {
                class_stats[0] = (double)class_freq[0]/(double)train_data.length;
                class_stats[1] = (double)class_freq[1]/(double)train_data.length;
            }
            
            System.out.println();
        }
        
        for(int i = 0; i < attr_stats.length; i++)
        {
            for(int j = 0; j < attr_stats[i].length; j++)
            {
                for(int k = 0; k < attr_stats[i][j].length; k++)
                {
                    System.out.println(attr_freq[i][j][k]);
                    attr_stats[i][j][k] = (double)attr_freq[i][j][k]/(double)class_freq[i];
                    System.out.println("The probability of attribute " + j
                            + "takes on value " + k + " given class " + i
                            + " = " + attr_stats[i][j][k]);
                }
            }
        }
        System.out.println("Prior Probability for class 2 = " + class_stats[0]);
        System.out.println("Prior Probability for class 4 = " + class_stats[1]);
    }
    
    // calculate probabilities
}
