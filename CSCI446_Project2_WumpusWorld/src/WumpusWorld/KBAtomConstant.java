package WumpusWorld;

/**
 * A single piece of information used in sentences.
 * Is often a Predicate with a Term
 * 
 * @author David Rice
 * @version October 2016
 */
public class KBAtomConstant extends KBAtom
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
        this.negation = negation;
        this.predicate = predicate;
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
            case ("OBST"):
                if (term.isHasObst()) value = true;
                break;
            case ("BLOCKED"):
                if (term.isBlocked()) value = true;
                break;
            default:
                throw new RuntimeException("The predicate of this atom"
                        + "does not have a defined value");
        }
        if (isNegation()) value = !value;
        
        return value;
    }
    
    public void flipNegation()
    {
        this.negation = !this.isNegation();
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
        
        if (this.isNegation()) negationStr = "!";
        output = output + negationStr + this.predicate;
        output = output + "(room(" +  this.term.getRoomRow() + ", " + this.term.getRoomColumn() + "))";
        return output;
    }
    
    @Override
    public boolean equals(Object obj)
    {
        if (!(obj instanceof KBAtomConstant)) return false;
        
        KBAtomConstant other = (KBAtomConstant)obj;
        if ((this.isNegation() == other.isNegation()) && (this.predicate.equals(other.predicate)) && (this.term == other.term))
            return true;
        else return false;
    }
}
