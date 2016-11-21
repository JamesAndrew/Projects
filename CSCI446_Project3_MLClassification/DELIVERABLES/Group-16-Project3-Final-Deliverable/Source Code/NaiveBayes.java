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
        int [][] data = { {1000025, 5, 1, 1, 1, 2, 1, 3, 1, 1, 2},
            {1002945, 5, 4, 4, 5, 7, 10, 3, 2, 1, 2} };
        
    }
}
