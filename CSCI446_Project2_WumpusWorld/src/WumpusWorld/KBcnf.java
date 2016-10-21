package WumpusWorld;

import com.rits.cloning.Cloner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * CNF is composed of conjunctions of disjunctions of atomic sentences
 * @author David
 */
public class KBcnf 
{
    // each arraylist entry is connected by a conjunction when the inner-entry
    // items are connected by disjunctions 
    private ArrayList<ArrayList<KBAtom>> disjunctions = new ArrayList<>();
    
    public KBcnf(KBAtom... atoms)
    {
        ArrayList<KBAtom> disjunction = new ArrayList<>();
        for (KBAtom atom : atoms)
        {
            disjunction.add(atom);
        }
        this.disjunctions.add(disjunction);
    }
    
    public KBcnf(ArrayList<KBAtom>... disjuctions)
    {
        for (ArrayList<KBAtom> disjSentence : disjuctions) this.disjunctions.add(disjSentence);
    }
    
    public KBcnf(ArrayList<ArrayList<KBAtom>> disjunctions)
    {
        this.disjunctions.addAll(disjunctions);
    }
    
    /**
     * Assuming only disjunctions, if any atom value evaluates to true, the
     * CNF statement is true 
     * @param context
     */
    public boolean evaluate(KBAtomConstant context)
    {
        ArrayList<Boolean> disjValues = new ArrayList<>(Arrays.asList(new Boolean[this.disjunctions.size()]));
        Collections.fill(disjValues, Boolean.FALSE);
        
        // if all disjunctive sentences evaluate to true, the conjunction of
        // all disjunctions is also true.
        // for each collection of disjunctions in the cnf sentence
        for (int i = 0; i < disjValues.size(); i++)//(ArrayList<KBAtom> disjSentence : this.disjunctions)
        {
            // search for a true value in the current disjunctive collection of atoms
            for (KBAtom atom : this.disjunctions.get(i))
            {
                if (atom instanceof KBAtomConstant)
                {
                    KBAtomConstant current = (KBAtomConstant) atom;
                    boolean value = current.evaluate();
                    if (value) 
                    {
                        disjValues.add(i, Boolean.TRUE);
                        break;
                    }
                }
                else if (atom instanceof KBAtomVariable)
                {
                    KBAtomVariable current = (KBAtomVariable) atom;
                    boolean value = current.evaluate(context);
                    if (value) 
                    {
                        disjValues.add(i, Boolean.TRUE);
                        break;
                    }
                }
                else throw new RuntimeException("The atoms in KBcnf.evaluate() "
                        + "didn't evaluate to the correct type.");
            }
        }
        
        // check that conjunction of all disjunctions evaluates to true
        for (Boolean value : disjValues)
        {
            if (value) return true;
        }
        return false;
    }
    
    /**
     * returns a list of all atoms in the disjunctions collections
     * @return 
     */
    public List<KBAtom> generateAtomList()
    {
        List<KBAtom> atomList = new ArrayList<>();
        for (ArrayList<KBAtom> disjunction : this.disjunctions)
        {
            for (KBAtom atom : disjunction)
            {
                atomList.add(atom);
            }
        }
        
        return atomList;
    }
   
    @Override
    public String toString()
    {
        return disjunctions.toString();
    }
    
    @Override
    public boolean equals(Object obj)
    {
        if (!(obj instanceof KBcnf)) return false;
        KBcnf other = (KBcnf)obj;
        
        List<KBAtom> thisAtomList = this.generateAtomList();
        List<KBAtom> otherAtomList = other.generateAtomList();
        if (thisAtomList.size() != otherAtomList.size()) return false;
        
        boolean idential = true;
        for (int i = 0; i < thisAtomList.size(); i++)
        {
            KBAtom currentAtom = thisAtomList.get(i);
            KBAtom otherAtom = otherAtomList.get(i);
            if (!(currentAtom.toString().equals(otherAtom.toString()))) idential = false;
        }
        
        return idential;
    }

    public ArrayList<ArrayList<KBAtom>> getDisjunctions() {
        return disjunctions;
    }
}
