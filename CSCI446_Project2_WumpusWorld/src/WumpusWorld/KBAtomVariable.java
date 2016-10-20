
package WumpusWorld;

public class KBAtomVariable extends KBAtom
{
    // true if the atom is negated (e.g. !P(x))
    protected boolean negation;
    // SHINY, WINDY, etc. Must match the predefined allowed predicates to work correctly
    protected String predicate;
    // Atom variables must specify if they are the room being looked at, or a 
    // room below, above, left, or right. E.g. to enfore that this constant atom
    // this variable unifies with is the cell to the left of the cell currently
    // queried, set term offset to be [-1, 0]. 
    private int[] termOffset;
    
    public KBAtomVariable(boolean negation, String predicate, int[] offsets)
    {
        this.negation = negation;
        this.predicate = predicate;
        this.termOffset = offsets;
    }
    /**
     * Variables need a context for each evaluation
     * @param context : the specific (constant) atom being addressed
     */
    public boolean evaluate(KBAtomConstant context)
    {
        return context.evaluate();
    }
    
    @Override
    public String toString()
    {
        String output = "";
        String negationStr = "";
        
        if (this.negation) negationStr = "!";
        output = output + negationStr + this.predicate;
        output = output + "(room(x=" + termOffset[0] + ",y=" + termOffset[1] + "))";
        return output;
    }
}
