package WumpusWorld;

/**
 *        Sentence -> Atomic Sentence | Complex Sentence
 * Atomic Sentence ->       Predicate | Predicate(Terms)
 * 
 * @author David
 */
public class KBAtomicSentence extends KBSentence
{
    // Atomic predicate name
    String name;
    // Value in the predicate. Always a room for now
    Room constant;
    
    public KBAtomicSentence(String name, Room constant) 
    { 
        this.name = name;
        this.constant = constant;
    }
    
    public boolean value() throws Exception
    {
        boolean truthValue = false;
        switch (name)
        {
            case("SHINY"): truthValue = constant.isShiny(); break;
            case("PIT"): truthValue = constant.isPit(); break;
            case("SMELLY"): truthValue = constant.isSmelly(); break;
            default:
                throw new Exception("Atomic sentence queried with a predicate not coded yet");
        }
        
        return truthValue;
    }
}
