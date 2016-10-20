package WumpusWorld;

/**
 * A single piece of information used in sentences.
 * Is often a Predicate with a Term
 * 
 * @author David Rice
 * @version October 2016
 */
public class KBAtomConstant extends KBAtomVariable
{
    // this will change. having all atom terms be rooms for now
    private Room term;
    
    /**
     * 
     * @param negation : true if the atom is negated (e.g. !P(x))
     * @param predicate : must match predicates list exactly
     * @param term : the specific thing being referred to (e.g. some Room object)
     */
    public KBAtomConstant(boolean negation, String predicate, Room term)
    {
        super(negation, predicate);
        this.term = term;
    }
    
    /**
     * returns the atomic truth value for the predicate assigned to this atom
     * @return 
     */
    public boolean evaluate()
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
        if (negation) value = !value;
        
        return value;
    }
    
    public void flipNegation()
    {
        this.negation = !this.negation;
    }

    public Room getTerm() 
    {
        return term;
    }
    
    @Override
    public String toString()
    {
        String output = "";
        String negationStr = "";
        
        if (this.negation) negationStr = "!";
        output = output + negationStr + this.predicate;
        output = output + "(room(" +  this.term.getRoomRow() + ", " + this.term.getRoomColumn() + "))";
        return output;
    }
    
    
}
