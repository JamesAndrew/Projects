package WumpusWorld;

import java.util.ArrayList;

/**
 * CNF is composed of conjunctions of disjunctions of atomic sentences
 * For now just assuming only disjunctions (single termed CNF)
 * @author David
 */
public class KBcnf 
{
    private ArrayList<KBAtom> atoms = new ArrayList<>();
    
    public KBcnf(KBAtom... atoms)
    {
        for (KBAtom atom : atoms)
        this.atoms.add(atom);
    }
    
    public KBcnf(ArrayList<KBAtom> atoms)
    {
        this.atoms.addAll(atoms);
    }
    
    /**
     * Assuming only disjunctions, if any atom value evaluates to true, the
     * CNF statement is true 
     * @param 
     */
    public boolean evaluate(KBAtomConstant context)
    {
        boolean value = false;
        for (KBAtom atom : atoms)
        {
            if (atom instanceof KBAtomConstant)
            {
                KBAtomConstant current = (KBAtomConstant) atom;
                value = current.evaluate();
            }
            else if (atom instanceof KBAtomVariable)
            {
                KBAtomVariable current = (KBAtomVariable) atom;
                value = current.evaluate(context);
            }
            else throw new RuntimeException("The atoms in KBcnf.evaluate() "
                    + "didn't evaluate to the correct type.");
        }
        return value;
    }
    
    
    @Override
    public String toString()
    {
        return atoms.toString();
    }
    
    @Override
    public boolean equals(Object obj)
    {
        if (!(obj instanceof KBcnf)) return false;
        KBcnf other = (KBcnf)obj;
        if (this.atoms.size() != other.getAtoms().size()) return false;
        for (int i = 0; i < this.atoms.size(); i++)
        {
            if (!(this.atoms.get(i)).equals(other.getAtoms().get(i))) return false;
        }
        return true;
        
    }

    public ArrayList<KBAtom> getAtoms() {
        return atoms;
    }
}
