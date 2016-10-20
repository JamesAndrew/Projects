package WumpusWorld;

import Exceptions.PendingException;
import java.util.*;

/**
 * The knowledge base is in charge of storing all sentences (axioms, inferences, etc.)
 * It also provides functionality to add new sentences to the database
 * though agent percept and self-inference and has the method 'ask()' which
 * returns the next Room to go to.
 * 
 * Notable inference algorithms such as Unification 
 * and Backward-Chaining are in this class.
 * 
 * @author David Rice
 * @version October, 2016
 */
public class KnowledgeBase 
{
    private List<KBImplication> kb_impl = new ArrayList<>();
    private List<KBcnf> kb_cnf = new ArrayList<>();
    
    public KnowledgeBase() 
    { 
        // SHINY(C_xy) => HASGOLD(C_xy)
        addToKBcnf(
            new KBAtomVariable(true, "SHINY", new int[]{0,0}), 
            new KBAtomVariable(false, "HASGOLD", new int[]{0,0})
        );
    }
    
    /**
     * @param question : The query asked that is determined true or not. Must
     * be an atomic sentence. Example: isSafe(Cell31)
     * @return ture if the atomic sentence is inferred to be true
     */
    public boolean query(KBAtomConstant question)
    {
        // temporary knowledge base set up for the current context
        List<KBcnf> tempKB = new ArrayList<>();
        tempKB.addAll(kb_cnf);
        
        // add the negation of the question to the temp kb
        question.flipNegation();
        KBcnf query_as_cnf = new KBcnf(question);
        tempKB.add(query_as_cnf);
        
        // temp test
        System.out.println("actualKB: " + kb_cnf.toString());
        System.out.println("tempKB: " + tempKB.toString());
        
        // (pick up here) run the resolution algorithm 
        return resolution(tempKB, question);
    }
    
    private boolean resolution(List<KBcnf> kb, KBAtomConstant query)
    {
        // run unification on the current kb
        kb = unify(kb, query);
        System.out.println("Unified KB: " + kb.toString());
        
        // run resolution algorithm
        return resolution_subroutine(kb);
    }
    
    /**
     * @param kb : the temp kb with the negation of the query added in to it
     * @param query : atomic sentence to determine true or false
     * @return : true if query is true, false otherwise
     */
    private boolean resolution_subroutine(List<KBcnf> kb)
    {
        Set<KBcnf> localKb = new LinkedHashSet<>();
        localKb.addAll(kb);
        do
        {
            Set<KBcnf> generatedSentences = new LinkedHashSet<>();
            
            // pairwise comparison of each sentence in kb
            for (KBcnf cnfI : localKb)
            {
                for (KBcnf cnfJ : kb)
                {
                    if (cnfI.equals(cnfJ)) { } // do nothing 
                    else
                    {
                        KBcnf resolventClause = gen_resolvent_clause(cnfI, cnfJ);
//                        System.out.println("\ncnfI: " + cnfI.toString());
//                        System.out.println("cnfJ: " + cnfJ.toString());
                        
                        // if a new resolvent sentence is made
                        if (!(resolventClause.equals(cnfI)))
                        {
//                            System.out.println("resolventClause: " + resolventClause.toString());
                            // return successful query if resolvent is empty sentence
                            if (resolventClause.getAtoms().isEmpty()) return true;
                            // otherwise add new generated clause to the generate KBcnf list if it is unique
                            else 
                            {
                                boolean unique = true;
                                for (KBcnf cnf : generatedSentences)
                                {
                                    if (cnf.equals(resolventClause)) unique = false;
                                }
                                if (unique) generatedSentences.add(resolventClause);
                            }
                        }
                    }
                }
            }
            // reture false query if generated senteces is a subset of the actual kb
            boolean stillNewResolvents = true;
            for (KBcnf genCNF : generatedSentences)
            {
                for (KBcnf kbCNF : localKb)
                {
                    if (genCNF.equals(kbCNF)) stillNewResolvents = false;
                }
            }
            if (!stillNewResolvents) return false;
            // otherwise update the local knowledge base to include the new resolvent sentences
            else 
            {
//                System.out.println("generatedSentences: " + generatedSentences.toString());
//                System.out.println("localKb: " + localKb.toString());
                // add generated cnf if unique to localKb
                for (KBcnf genCNF : generatedSentences)
                {
                    boolean unique = true;
                    for (KBcnf kbCNF : localKb)
                    {
                        if (genCNF.equals(kbCNF)) unique = false;
                    }
                    if (unique) localKb.add(genCNF);
                }
                
//                System.out.println("updated local kb: " + localKb.toString());
                generatedSentences.clear();
            }
        } while (true);
    }
    
    /**
     * If two cnf's have a resolvent clause that can be produced, generate
     * the clause and return it, otherwise return one of the original clauses
     * @param i : cnf clause 1
     * @param j : cnf clause 2
     */
    public KBcnf gen_resolvent_clause(KBcnf i, KBcnf j)
    {
        ArrayList<KBAtom> ijAtoms = new ArrayList<>();
        ijAtoms.addAll(i.getAtoms());
        ijAtoms.addAll(j.getAtoms());
        
        // pairwise comparison of each atom in clause i to each atom in clase j
        for (KBAtom iAtoms : i.getAtoms())
        {
            KBAtomConstant atomI = (KBAtomConstant) iAtoms;
            for (KBAtom jAtoms : j.getAtoms())
            {
                KBAtomConstant atomJ = (KBAtomConstant) jAtoms;
                
                // if one is the perfect negation of the other...
                if(gen_resolvent_clause_subroutine(atomI, atomJ))
                {
                    // remove both atoms from the ijAtoms cnf statement
                    ijAtoms.remove(atomI);
                    ijAtoms.remove(atomJ);
                    // generate the new CNF resolvent clause
                    KBcnf resolventSentence = new KBcnf(ijAtoms);
                    return resolventSentence;
                }
            }
        }
        
        // if no perfect negations were found, return the original CNF
        return i;
    }
    
    private boolean gen_resolvent_clause_subroutine(KBAtomConstant atomA, KBAtomConstant atomB)
    {
        KBAtomConstant i = new KBAtomConstant(atomA.negation, atomA.predicate, atomA.getTerm());
        i.flipNegation();
        
        KBAtomConstant j = new KBAtomConstant(atomB.negation, atomB.predicate, atomB.getTerm());
        
        return i.equals(j);
    }
    
    /**
     * for all atoms in kb, if the atom is an instance of KBAtomVariable, transform
     * it into a KBAtomConstant with offsets applied relevant to the current query item 
     * @param in_kb : non-unified kb from the query function
     * @param query : the current query item
     * @return 
     */
    private List<KBcnf> unify(List<KBcnf> in_kb, KBAtomConstant query)
    {
        for (KBcnf sentence : in_kb)
        {
            for (int i = 0; i < sentence.getAtoms().size(); i++)
            {
                KBAtom atom = sentence.getAtoms().get(i);
                if (atom instanceof KBAtomVariable)
                {
                    KBAtomVariable currentAtom = (KBAtomVariable) atom;
                    KBAtomConstant replacement = currentAtom.convertToConstant(query);
                    sentence.getAtoms().set(i, replacement);
                }
            }
        }
        return in_kb;
    }
    
    /**
     * @param input : A first-order definite clause provided by either the agent
     * or cyclical techniques in the Knowledge Base
     * @post kbSentences is updated
     */
    public void update(KBAtom input)
    {
        KBcnf newData = new KBcnf(input);
        kb_cnf.add(newData);
    }
    
    private void addToKBcnf(KBAtomVariable... atoms)
    {
        KBcnf cnfSentence = new KBcnf(atoms);
        kb_cnf.add(cnfSentence);
    }

    public List<KBcnf> getKb_cnf() {
        return kb_cnf;
    }
    public void setKb_cnf(List<KBcnf> kb_cnf) {
        this.kb_cnf = kb_cnf;
    }
}
