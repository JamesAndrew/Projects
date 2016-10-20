package WumpusWorld;

import java.util.ArrayList;

/**
 * CNF is composed of conjunctions of disjunctions of atomic sentences
 * For now just assuming only disjunctions (single termed CNF)
 * @author David
 */
public class KBcnf 
{
    ArrayList<KBAtomVariable> atoms = new ArrayList<>();
    
    public KBcnf(KBAtomVariable... atoms)
    {
        for (KBAtomVariable atom : atoms)
        this.atoms.add(atom);
    }
    
    /**
     * Assuming only disjunctions, if any atom value evaluates to true, the
     * CNF statement is true 
     * @param 
     */
    public boolean evaluate(KBAtomConstant context)
    {
        boolean value = false;
        for (KBAtomVariable atom : atoms)
        {
            if (atom instanceof KBAtomConstant)
            {
                KBAtomConstant current = (KBAtomConstant) atom;
                value = current.evaluate();
            }
            else if (atom instanceof KBAtomVariable)
            {
                value = atom.evaluate(context);
            }
            else throw new RuntimeException("The atoms in KBcnf.evaluate() "
                    + "didn't evaluate to the correct type.");
        }
        return value;
    }
}
