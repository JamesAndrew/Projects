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
        List<KBcnf> localKb = new ArrayList<>();
        localKb.addAll(kb);
        
        do
        {
            List<KBcnf> generatedSentences = new ArrayList<>();
            
            // pairwise comparison of each sentence in kb
            for (KBcnf i : kb)
            {
                for (KBcnf j : kb)
                {
                    if (i.equals(j)) { } // do nothing }
                    else
                    {
                        KBcnf resolventClause = gen_resolvent_clause(i, j);
                        
                        // if a new resolvent sentence is made
                        if (resolventClause != i || resolventClause != j)
                        {
                            // return successful query if resolvent is empty sentence
                            if (resolventClause.getAtoms().isEmpty()) return true;
                            // otherwise add new generated clause to the generate KBcnf list
                            else generatedSentences.add(resolventClause);
                        }
                    }
                }
            }
            // reture false query if generated senteces is a subset of the actual kb
            if (localKb.containsAll(generatedSentences)) return false;
            // otherwise update the local knowledge base to include the new resolvent sentences
            else 
            {
                localKb.addAll(generatedSentences);
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
    private KBcnf gen_resolvent_clause(KBcnf i, KBcnf j)
    {
        
        
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
}
