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
public class NaiveBayes extends Categorizer
{ 
    private int classes;
    private int attr_values;
    private int attributes;
    
    private double [] class_stats;
    private double [][][] attr_stats;
    private int [] class_freq;
    private int [][][] attr_freq;
    
    int[][] foldResult;         // stores the confusion matrix for current run
    
    public NaiveBayes(DataSet[] trainingFolds, DataSet testingFold)
    {
        super(trainingFolds, testingFold);
        categorizerName = "NB";
    
        System.out.println("=== Naive Bayes Experiment ===");
        
        // foldResult is an (n x n) matrix where n = number of classifications
        int matrixSize = trainingSet.getNumClassifications();
        foldResult = new int[matrixSize][];
        for (int i = 0; i < foldResult.length; i++)
        {
            foldResult[i] = new int[matrixSize];
        }
        
        attributes = trainingFolds[0].getVectors()[0].features().length;
        attr_values = 11;
        classes = trainingFolds[0].getNumClassifications();
        
        class_stats = new double[classes];
        attr_stats = new double[classes][attributes][attr_values];
                
        class_freq = new int [classes];
        attr_freq = new int [classes][attributes][attr_values];
    }
    
    @Override
    public void Train() 
    {
        System.out.println("=== Training Naive Bayes Model ===");
        
        
        System.out.println("Classes: " + classes);
        System.out.println("Attributes: " + attributes);
        
        
        
        for (int i = 0; i < trainingFolds.length; i++)
        {
            Vector [] currentPoint = trainingFolds[i].getVectors();
            for(int j = 0; j < currentPoint.length; j++)
            {
                System.out.println(currentPoint[j]);
            }
        }
        
        
        
        
        
        
        
        /**
         * determine the Naive Bayes Model
         */
        // loop through the data and determine frequencies
//        for (int i = 0; i < train_data.length; i++)
//        {
//            for(int j = 0; j < train_data[i].length; j++)
//            {
//                System.out.print(train_data[i][j] + " ");
//                
//                 check if class label
//                if (j == (train_data[i].length - 1))
//                {
//                    if (train_data[i][j] == 2)
//                    {
//                        class_freq[0]++;
//                        System.out.print("Frequency of 2 = " + class_freq[0]);
//                    }
//                    else if (train_data[i][j] == 4)
//                    {
//                        class_freq[1]++;
//                        System.out.print("Frequency of 4 = " + class_freq[1]);
//                    }
//                }
//                
//                 else assume attribute value
//                else
//                {
//                    int class_label = train_data[i][train_data[i].length-1];
//                    int c = 0;
//                    if(class_label == 2)
//                    {
//                        c = 0;
//                    }
//                    else if (class_label == 4)
//                    {
//                        c = 1;
//                    }
//                     check if attribute i is equal to some value k from 0 to 10
//                    switch (train_data[i][j]) {
//                        case 0:
//                            attr_freq[c][j][0]++;
//                            break;
//                        case 1:
//                            attr_freq[c][j][1]++;
//                            break;
//                        case 2:
//                            attr_freq[c][j][2]++;
//                            break;
//                        case 3:
//                            attr_freq[c][j][3]++;
//                            break; 
//                        case 4:
//                            attr_freq[c][j][4]++;
//                            break;
//                        case 5:
//                            attr_freq[c][j][5]++;
//                            break;
//                        case 6:
//                            attr_freq[c][j][6]++;
//                            break;
//                        case 7:
//                            attr_freq[c][j][7]++;
//                            break;
//                        case 8:
//                            attr_freq[c][j][8]++;
//                            break;
//                        case 9:
//                            attr_freq[c][j][9]++;
//                            break;
//                        case 10:
//                            attr_freq[c][j][10]++;
//                            break;
//                        default:
//                            attr_freq[c][j][0]++;
//                            break;
//                    }
//                }
//                
//                
//            }
//            System.out.println();
//             if on class attribute, determine prior probabilities of each class
//            if (i == (train_data.length-1))
//            {
//                class_stats[0] = (double)class_freq[0]/(double)train_data.length;
//                class_stats[1] = (double)class_freq[1]/(double)train_data.length;
//            }
//            
//            System.out.println();
//        }
//        
//         go through frequencies and determine probabilities
//        for(int i = 0; i < attr_stats.length; i++)
//        {
//            for(int j = 0; j < attr_stats[i].length; j++)
//            {
//                for(int k = 0; k < attr_stats[i][j].length; k++)
//                {
//                    System.out.println(attr_freq[i][j][k]);
//                    attr_stats[i][j][k] = (double)attr_freq[i][j][k]/(double)class_freq[i];
//                    
//                     check if any probabilities are 0
//                    if (attr_stats[i][j][k] == 0)
//                    {
//                         apply m-estimate
//                        attr_stats[i][j][k] = mEst((double)attr_freq[i][j][k], (double)class_freq[i]);
//                    }
//                    
//                     print final probabilities 
//                    System.out.println("P(A" + j
//                            + " = " + k + " | C" + i
//                            + ") = " + attr_stats[i][j][k]);
//                }
//            }
//        }
//         print prior probabilities of each class
//        System.out.println("Prior Probability for class 2 = " + class_stats[0]);
//        System.out.println("Prior Probability for class 4 = " + class_stats[1]); 
    }

    @Override
    public int[][] Test() 
    {
        /**
         * Classify test data using Naive Bayes Parameters 
         */
        System.out.println("== Classifying test data ==");
        System.out.println(testingFold.toString());
        
//        double [] posteriors = new double [classes];
//        for (int i = 0; i < posteriors.length; i++)
//        {
//            posteriors[i] = 1.0;
//        }
//        
//        for (int i = 0; i < test_data.length; i++)
//        {
//            for (int j = 0; j < test_data[i].length; j++)
//            {
//                for (int k = 0; k < classes; k++)
//                {
//                    if (j == test_data[i].length - 1)
//                    {
//                        posteriors[k] = posteriors[k]*class_stats[k];
//                    }
//                    else
//                    {
//                        if (test_data[i][j] > test_data[0].length)
//                        {
//                            posteriors[k] = posteriors[k]*1.0;
//                        }
//                        else
//                        {
//                            posteriors[k] = posteriors[k]*attr_stats[k][j][test_data[i][j]];
//                        }
//                    }
//                }
//            }
//            
//            int classified = 0;
//            for (int l = 0; l < posteriors.length; l++)
//            {
//                if (posteriors[l] > posteriors[classified])
//                {
//                    classified = l;
//                }
//            }
//            System.out.println("Example " + i + "clasified as " + classified);
//        }
          int [][] stuff = {{0, 1}, {1, 0}};
          return stuff;
    } 
    
    
    // m-estimate to handle 0 probabilities
    public double mEst(double nc, double n)
    {
        double estimate;
        double m = 1.0;
        double p;
        p = 1.0/(double)attr_values;
        
        estimate = (nc + m*p)/(n + m);
        
        return estimate;
    }
}
