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
        addToKBcnf(new KBAtomVariable(true, "SHINY"), new KBAtomVariable(false, "HASGOLD"));
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
        System.out.println(tempKB.toString());
        
        // (pick up here) run the resolution algorithm 
        return resolution(tempKB);
    }
    
    private boolean resolution(List<KBcnf> kb)
    {
        return true;
    }
    
    /**
     * @param input : A first-order definite clause provided by either the agent
     * or cyclical techniques in the Knowledge Base
     * @post kbSentences is updated
     */
    public void update(KBAtomVariable input)
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
