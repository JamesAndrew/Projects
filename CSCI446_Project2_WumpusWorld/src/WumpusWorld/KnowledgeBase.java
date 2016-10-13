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
    // The list of all sentences currently known
    private List<KBSentence> kbSentences = new ArrayList<>();
    
    public KnowledgeBase() { }
    
    /**
     * Uses backward-chaining to deduce if a certain sentence is true or not
     * @param question : The query asked that is determined true or not. Must
     * be an atomic sentence. Example: isSafe(Cell31)
     * @return ture if the atomic sentence is inferred to be true
     */
    public boolean query(KBAtomicSentence question)
    {
        
        throw new PendingException();
    }
    
    /**
     * All inputted sentences must be formed as first-order definite clauses
     * e.g.: (P1(a) ^ P2(b) ^ P3(c)) => P4(a, b, c)
     * @param input : A first-order definite clause provided by either the agent
     * or cyclical techniques in the Knowledge Base
     * @post kbSentences is updated
     */
    public void update(KBSentence input)
    {
        
        throw new PendingException();
    }
}
