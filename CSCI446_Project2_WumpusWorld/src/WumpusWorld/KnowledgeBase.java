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
        throw new PendingException();
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
        System.out.println("Unified KB: " + in_kb.toString());
        throw new PendingException();
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
}
