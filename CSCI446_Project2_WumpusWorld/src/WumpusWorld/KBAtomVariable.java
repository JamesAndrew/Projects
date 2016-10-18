
package WumpusWorld;

public class KBAtomVariable
{
    // true if the atom is negated (e.g. !P(x))
    protected boolean negation;
    // SHINY, WINDY, etc. Must match the predefined allowed predicates to work correctly
    protected String predicate;
    
    public KBAtomVariable(boolean negation, String predicate)
    {
        this.negation = negation;
        this.predicate = predicate;
    }
    /**
     * Variables need a context for each evaluation
     * @param context : the specific (constant) atom being addressed
     */
    public boolean evaluate(KBAtomConstant context)
    {
        return context.evaluate();
    }
}
