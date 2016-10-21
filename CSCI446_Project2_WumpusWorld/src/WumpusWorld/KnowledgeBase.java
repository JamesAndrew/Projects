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
    private List<KBcnf> kb_cnf = new ArrayList<>();
    
    public KnowledgeBase() 
    { 
        // SHINY(C_xy) => HASGOLD(C_xy): room has gold
        addToKBcnf(
            new KBAtomVariable(true, "SHINY", new int[]{0,0}), 
            new KBAtomVariable(false, "HASGOLD", new int[]{0,0})
        );
        
        // OBST(C_xy) => BLOCKED(C_xy): room is blocked
        addToKBcnf(
            new KBAtomVariable(true, "OBST", new int[]{0,0}),
            new KBAtomVariable(false, "BLOCKED", new int[]{0,0})
        );
        
        // (smelly || windy || shiny) || (!blocked && !pit && !wumpus) => safe: room is safe
        ArrayList<KBAtom> disj1 = new ArrayList<>(Arrays.asList(new KBAtomVariable(true, "SMELLY", new int[]{0,0})));
        ArrayList<KBAtom> disj2 = new ArrayList<>(Arrays.asList(new KBAtomVariable(true, "WINDY", new int[]{0,0})));
        ArrayList<KBAtom> disj3 = new ArrayList<>(Arrays.asList(new KBAtomVariable(true, "SHINY", new int[]{0,0})));
        ArrayList<KBAtom> disj4 = new ArrayList<>(Arrays.asList(
            new KBAtomVariable(false, "OBST", new int[]{0,0}),
            new KBAtomVariable(false, "PIT", new int[]{0,0}),
            new KBAtomVariable(false, "WUMPUS", new int[]{0,0})
        )
        );
        ArrayList<KBAtom> disj5 = new ArrayList<>(Arrays.asList(new KBAtomVariable(false, "SAFE", new int[]{0,0})));
        addToKBcnf(disj1, disj2, disj3, disj4, disj5);
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
        ArrayList<KBAtom> negQuestionCNF = new ArrayList<>();
        negQuestionCNF.add(question);
        KBcnf query_as_cnf = new KBcnf(negQuestionCNF);
        tempKB.add(query_as_cnf);
        
        // temp test
        System.out.println("actualKB: " + kb_cnf.toString());
        System.out.println("tempKB: " + tempKB.toString());
        
        // run the resolution algorithm 
        return resolution(tempKB, question);
    }
    
    private boolean resolution(List<KBcnf> kb, KBAtomConstant query)
    {
        // run unification on the current kb
        List<KBcnf> unifiedKB = new ArrayList<>();
        List<KBcnf> temp = unify(kb, query);
        unifiedKB.addAll(temp);
        
        System.out.println("Unified KB: ");
        for (int i = 1; i < unifiedKB.size(); i++)
        {
            System.out.format("%d: ", i);
            System.out.println(unifiedKB.get(i).toString());
        }
        
        // run resolution algorithm
        return resolution_subroutine(unifiedKB);
    }
    
    /**
     * @param kb : the temp kb with the negation of the query added in to it.
     * The temp kb also split all conjunctions into individual atomic sentences
     * @param query : atomic sentence to determine true or false
     * @return : true if query is true, false otherwise
     */
    private boolean resolution_subroutine(List<KBcnf> kb)
    {
        List<KBcnf> localKb = new ArrayList<>();
        localKb.addAll(kb);
        localKb = splitConjunctions(localKb);
        
        do
        {
            List<KBcnf> generatedSentences = new ArrayList<>();
            
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
                            System.out.println("resolventClause: " + resolventClause.toString());
                            // return successful query if resolvent is empty sentence
                            if (resolventClause.getDisjunctions().get(0).isEmpty()) return true;
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
     * Remove sentences in the kb that have disjunctions and replace with new
     * kb entries that are the individual sentences
     * @param oldKb : the kb with conjunctions in it
     * @return : an equivalent kb with no conjunctions 
     */
    private List<KBcnf> splitConjunctions(List<KBcnf> oldKb)
    {
        // KBcnfs with more than one entry have conjunctions
        for (int i = 0; i < oldKb.size(); i++)
        {
            KBcnf currentCNF = oldKb.get(i);
            // if the currentCNF has conjunctions
            if (currentCNF.getDisjunctions().size() > 1)
            {
                // make collection of new cnf sentences
                List<KBcnf> splitCNFs = new ArrayList<>();
                // take each ArrayList entry and add it as a new entry to the kb in its cnf form
                for (ArrayList<KBAtom> disjunction : currentCNF.getDisjunctions())
                {
                    KBcnf newCNF = new KBcnf(disjunction);
                    splitCNFs.add(newCNF);
                }
                // remove the kb entry with conjunctions
                oldKb.remove(i);
                // add all the new equavalent split cns
                oldKb.addAll(splitCNFs);
            }
        }
        
        return oldKb;
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
        ijAtoms.addAll(i.generateAtomList());
        ijAtoms.addAll(j.generateAtomList());
        
        // pairwise comparison of each atom in clause i to each atom in clase j
        for (KBAtom iAtoms : i.generateAtomList())
        {
            KBAtomConstant atomI = (KBAtomConstant) iAtoms;
            for (KBAtom jAtoms : j.generateAtomList())
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
            // for each disjunctive sentence in the CNF
            for (int i = 0; i < sentence.getDisjunctions().size(); i++)
            {
                ArrayList<KBAtom> disjSentence = sentence.getDisjunctions().get(i);
                // for each atom in the disjunctive sentence
                for (int j = 0; j < disjSentence.size(); j++)
                {
                    KBAtom atom = disjSentence.get(j);
                    if (atom instanceof KBAtomVariable)
                    {
                        KBAtomVariable currentAtom = (KBAtomVariable) atom;
                        KBAtomConstant replacement = currentAtom.convertToConstant(query);
                        disjSentence.set(j, replacement);  
                    }
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
        ArrayList<KBAtom> inputAsCNF = new ArrayList<>(Arrays.asList(input));
        KBcnf newData = new KBcnf(inputAsCNF);
        kb_cnf.add(newData);
    }
    
    /**
     * Add sequence of conjuncts of disjuncts to the kb
     * @param conjunctsOfDisjuncts 
     */
    private void addToKBcnf(ArrayList<KBAtom>... conjunctsOfDisjuncts)
    {
        ArrayList<ArrayList<KBAtom>> conjunctions = new ArrayList<>();
        for (ArrayList<KBAtom> disjunction : conjunctsOfDisjuncts)
        {
            conjunctions.add(disjunction);
        }
        KBcnf newCNF = new KBcnf(conjunctions);
        kb_cnf.add(newCNF);
    }
    
    /**
     * Add sequence of only disjunctive terms to the kb
     * @param atoms 
     */
    private void addToKBcnf(KBAtomVariable... atoms)
    {
        ArrayList<KBAtom> onlyDisjuncts = new ArrayList<>(Arrays.asList(atoms));
        KBcnf cnfSentence = new KBcnf(onlyDisjuncts);
        kb_cnf.add(cnfSentence);
    }

    public List<KBcnf> getKb_cnf() {
        return kb_cnf;
    }
    /**
     * used in tests to deal with an empty KB axiom list
     */
    public void setKb_cnf(List<KBcnf> kb_cnf) {
        this.kb_cnf = kb_cnf;
    }
}
