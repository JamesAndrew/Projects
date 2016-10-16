package WumpusWorld;

/**
 * A single piece of information used in sentences.
 * Is often a Predicate with a Term
 * 
 * @author David Rice
 * @version October 2016
 */
public class KBAtomConstant 
{
    // evaluates to either true or false
    boolean value;
    // SHINY, WINDY, etc. Must match the predefined allowed predicates to work correctly
    String predicate;
    // this will change. having all atom terms be rooms for now
    Room term;
    
    public KBAtomConstant(String predicate, Room term)
    {
        this.predicate = predicate;
        this.term = term;
    }
    
    /**
     * returns the atomic truth value for the predicate assigned to this atom
     * @return 
     */
    public boolean evaluateAtom()
    {
        boolean value = false;
        switch(predicate)
        {
            case ("SHINY"):
                if (term.isShiny()) value = true;
                break;
            case ("HASGOLD"):
                if (term.isHasGold()) value = true;
                break;
            default:
                throw new RuntimeException("The predicate of this atom"
                        + "does not have a defined value");
        }
        
        return value;
    }
}
