
package WumpusWorld;

public abstract class KBAtom 
{
    // true if the atom is negated (e.g. !P(x))
    protected boolean negation;
    // SHINY, WINDY, etc. Must match the predefined allowed predicates to work correctly
    protected String predicate;
    
    @Override
    public abstract String toString();
}
