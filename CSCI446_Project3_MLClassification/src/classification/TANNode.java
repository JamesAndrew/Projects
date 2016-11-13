package classification;

/**
 *
 * 
 */
public class TANNode
{
     private double traitValue; 
     private int traitNumber; 
     private double classifier; 
     private TANNode influence; 
     
     public TANNode(double traitValue, int traitNumber, double classifier)
     {
         this.traitValue = traitValue; 
         this.traitNumber = traitNumber; 
         this.classifier = classifier; 
     }
     
     public double getClassifier() 
     {
         return classifier; 
     }
     
     public double getTraitValue()
     {
         return traitValue; 
     }
     
     public int getTraitNumber()
     {
         return traitNumber; 
     }
     
     public void setInfluence(TANNode influence)
     {
         this.influence = influence; 
     }
     
     public TANNode getInfluence() 
     {
         return influence; 
     }
}
