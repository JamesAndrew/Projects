
package classification;

/**
 * Static class that the Experiment class uses to update the run result values 
 * for each data set on each machine learning categorizer for each 10-fold CV run.
 * 
 * The goal here is to easily extract the data needed for the confusion matrix
 * and macro-average for each data set -> categorizer -> CV run result.
 * 
 * The confusion matrix needs:
 *  - True Positive value for each classification
 *  - True Negative value for each classification
 *  - False Positive value for each classification
 *  - False Negative value for each classification
 *  - Total population size
 *  - Population size of each classification members
 * 
 * The macro average needs:
 *  - Precision and Recall metrics which can be evaluated from the above values
 */
public class Statistics 
{
    
}
