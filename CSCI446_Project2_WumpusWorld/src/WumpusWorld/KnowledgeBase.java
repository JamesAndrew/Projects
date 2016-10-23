package WumpusWorld;

import Exceptions.PendingException;
import com.rits.cloning.Cloner;
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
    // kb as a result of exploration. Holds only KBAtomConstants
    private List<KBcnf> percept_kb = new ArrayList<>();
    // mapping of relevant axioms to each query. These are KBAtomVariables
    private Map<String, List<KBcnf>> contextual_kb = new HashMap<>();
    // mapping of query requests to appropriate axiom contexts
    private Map<String, List<String>> contextMapping = new HashMap<>();
    // cloner for deep coppying from the knowledge base
    Cloner cloner = new Cloner();
    
    public KnowledgeBase() 
    { 
        // initialize the contextual_kb categories
        contextual_kb.put("BLOCKED",    new ArrayList<>());
        contextual_kb.put("EXPLORED",   new ArrayList<>());
        contextual_kb.put("HASGOLD",    new ArrayList<>());
        contextual_kb.put("PIT",        new ArrayList<>());
        contextual_kb.put("SAFE",       new ArrayList<>());
        contextual_kb.put("WUMPUS",     new ArrayList<>());
        
        // initialize the contextMapping 
        contextMapping.put("BLOCKED",   new ArrayList<>(Arrays.asList("BLOCKED", "SAFE")));
        contextMapping.put("EXISTS",    new ArrayList<>(Arrays.asList("WUMPUS", "PIT")));
        contextMapping.put("HASGOLD",   new ArrayList<>(Arrays.asList("HASGOLD")));
        contextMapping.put("OBST",      new ArrayList<>(Arrays.asList("SAFE", "BLOCKED")));
        contextMapping.put("PIT",       new ArrayList<>(Arrays.asList("PIT", "SAFE")));
        contextMapping.put("SAFE",      new ArrayList<>(Arrays.asList("EXPLORED")));
        contextMapping.put("SHINY",     new ArrayList<>(Arrays.asList("HASGOLD")));
        contextMapping.put("SMELLY",    new ArrayList<>(Arrays.asList("SAFE", "WUMPUS")));
        contextMapping.put("WINDY",     new ArrayList<>(Arrays.asList("SAFE", "PIT")));
        contextMapping.put("WUMPUS",    new ArrayList<>(Arrays.asList("WUMPUS", "SAFE")));
    }
    
    /**
     * @param question : The query asked that is determined true or not. Must
     * be an atomic sentence. Example: isSafe(Cell31)
     * @return ture if the atomic sentence is inferred to be true
     */
    public boolean query(KBAtomConstant question)
    {
        // temporary knowledge base set up for the current context
        Cloner cloner1 = new Cloner();
        List<KBcnf> tempKB = new ArrayList<>();
        String key = question.predicate;
        
        // specialized axiomatic contexts dependent on queries
        contextual_kb.get(key).clear();
        switch (key) {
            case "BLOCKED":
                ArrayList<KBcnf> isBlockedAxioms = axiom_RoomIsBlocked();
                contextual_kb.get(key).addAll(isBlockedAxioms);
                break;
            case "SAFE":
                // add the room-contextualized 'safe' axioms
                ArrayList<KBcnf> isSafeAxioms = axiom_dynamic_RoomIsSafe(question.getTerm());
                contextual_kb.get(key).addAll(isSafeAxioms);
                break;
            case "HASGOLD":
                ArrayList<KBcnf> isShinyAxioms = axiom_RoomHasGold();
                contextual_kb.get(key).addAll(isShinyAxioms);
                break;
            case "PIT":
                throw new PendingException();
//                break;
            case "WUMPUS":
                // add the room-contextualized wumpus axioms
                ArrayList<KBcnf> hasWumpusAxioms = axiom_dynamic_RoomHasWumpus(question.getTerm());
                contextual_kb.get(key).addAll(hasWumpusAxioms);
                break;
            default:
                throw new RuntimeException("A query on a nonsupported predicate was asked.");
        }
        
        for (KBcnf cnf : contextual_kb.get(key))
        {
            KBcnf clonerCNF = cloner1.deepClone(cnf);
            tempKB.add(clonerCNF);
        }
        
//        System.out.println("Key: " + key);
//        System.out.println("Value: ");
//        for (int i = 0; i < contextual_kb.get(key).size(); i++)
//        {
//            System.out.format("%d: %s%n", i, contextual_kb.get(key).get(i));
//        }
        
        // add the negation of the question to the temp kb
        question.flipNegation();
        ArrayList<KBAtom> negQuestionCNF = new ArrayList<>();
        negQuestionCNF.add(question);
        KBcnf query_as_cnf = new KBcnf(negQuestionCNF);
        tempKB.add(query_as_cnf);
        
        // temp test
//        System.out.println("actualKB: " + percept_kb.toString());
//        System.out.println("tempKB: " + tempKB.toString());
        
        // run the resolution algorithm 
        return resolution(tempKB, question);
    }
    
    /**
     * action 1: move to a new cell
     * action 2: shoot arrow
     * action 3: exit dungeon (no safe rooms to move to)
     * 
     * index 0 is the action, index 1 and 2 is the row and column to move to
     * @param currentRow : agent's current row location
     * @param currentCol : agent's current col location
     * @param frontier : frontier that is tracked by the agent
     * @return [action, row to move to, col to move to]
     */ 
    public int[] requestAction(int currentRow, int currentCol, Map<Integer, Room> frontier)
    {
        int[] returnedAction = new int[3];
        
        // sort frontier from closest to furthest away
        ArrayList<Room> sortedFrontier = sortFrontier(frontier, currentRow, currentCol);
        System.out.println("Sorted Frontier: ");
        for (Room room : sortedFrontier)
        {
            System.out.println(room.toString());
        }
        
        // move to room if room is safe
        boolean isSafe = false;
        for (int i = 0; i < sortedFrontier.size(); i++)
        {
            int newRow = sortedFrontier.get(i).getRoomRow();
            int newCol = sortedFrontier.get(i).getRoomColumn();
            KBAtomConstant safeQuery = new KBAtomConstant(false, "SAFE", ActualWorld.getRoom(newRow, newCol));
            isSafe = query(safeQuery);
            
            System.out.format("Query if room (%d, %d) is safe%n", newRow, newCol);
            System.out.format("Query value: %b%n", isSafe);
            
            if (isSafe)
            {
                returnedAction[0] = 1;
                returnedAction[1] = newRow;
                returnedAction[2] = newCol;
                
                break;
            }
        }
        // if no frontier cell was found safe, you're trapped, so end the simulation
        if (!isSafe)
        {
            System.out.println("No certain safe room exists to move to. Exiting dungeon...");
            returnedAction[0] = 3;
        }
        
        // todo: shoot arrow functionality
        return returnedAction;
    }
    
    private boolean resolution(List<KBcnf> kb, KBAtomConstant query)
    {
        // run unification on the current kb
        List<KBcnf> temp = unify(kb, query);
//        System.out.println("\nUnified KB: ");
//        for (int i = 0; i < temp.size(); i++)
//        {
//            System.out.format("%d: ", i);
//            System.out.println(temp.get(i).toString());
//        }
        
        // run resolution algorithm
        return resolution_subroutine(temp);
    }
    
    /**
     * @param kb : the temp kb with the negation of the query added in to it.
     * The temp kb also split all conjunctions into individual atomic sentences
     * @param query : atomic sentence to determine true or false
     * @return : true if query is true, false otherwise
     */
    private boolean resolution_subroutine(List<KBcnf> kb)
    {
        List<KBcnf> localKb = kb;
        localKb = splitConjunctions(localKb);
//        System.out.println("\nlocalKb for resolution subroutine after conjunctions split: ");
//        for (int i = 0; i < localKb.size(); i++)
//        {
//            System.out.format("%d: ", i);
//            System.out.println(localKb.get(i).toString());
//        }
        
        do
        {
//            System.out.println("\nlocalKb: ");
//            for (int i = 0; i < localKb.size(); i++)
//            {
//                System.out.format("%d: ", i);
//                System.out.println(localKb.get(i).toString());
//            }
            List<KBcnf> generatedSentences = new ArrayList<>();
            
            // pairwise comparison of each sentence in kb
            for (int cnfI_Index = 0; cnfI_Index < localKb.size(); cnfI_Index++)
            {
                ArrayList<ArrayList<KBAtom>> cnfI = localKb.get(cnfI_Index).getDisjunctions();
                
                for (int cnfJ_index = 0; cnfJ_index < localKb.size(); cnfJ_index++)
                {
                    ArrayList<ArrayList<KBAtom>> cnfJ = localKb.get(cnfJ_index).getDisjunctions();
                    if (cnfI.equals(cnfJ)) { } // do nothing 
                    else
                    {
                        // generate new cnf which is (cnfI - resolventClauseAtom) U (cnfJ - resolventClauseAtom)
                        KBcnf first = new KBcnf(cnfI);
                        KBcnf second = new KBcnf(cnfJ);
//                        System.out.println("\ncnfI: " + cnfI.toString());
//                        System.out.println("cnfJ: " + cnfJ.toString());
                        KBcnf resolventCNF = gen_resolvent_clause(first, second);
                        
                        // if a new resolvent sentence is made
                        if (!(resolventCNF.equals(first)))
                        {
//                            System.out.println("resolventCNF: " + resolventCNF.toString() + "\n");
                            // return successful query if resolvent is empty sentence
                            if (resolventCNF.getDisjunctions().get(0).isEmpty()) return true;
                            // otherwise add new generated clause to the generate KBcnf list if it is unique
                            else 
                            {
                                boolean unique = true;
                                for (KBcnf cnf : generatedSentences)
                                {
                                    if (cnf.equals(resolventCNF)) unique = false;
                                }
                                if (unique) generatedSentences.add(resolventCNF);
                            }
                        }
                    }
                }
            }
            //update the local knowledge base to include the new resolvent sentences
//                System.out.println("generatedSentences: " + generatedSentences.toString());
//                System.out.println("localKb: " + localKb.toString());
            // add generated cnf if unique to localKb
            List<KBcnf> temp = cloner.deepClone(localKb);
            for (KBcnf genCNF : generatedSentences)
            {
                boolean unique = true;
                for (KBcnf kbCNF : localKb)
                {
                    if (genCNF.equals(kbCNF)) unique = false;
                }
                if (unique) localKb.add(genCNF);
            }
            
            ///
//            System.out.println("\ntempKB (before localKb is updated): ");
//            for (int i = 0; i < temp.size(); i++)
//            {
//                System.out.format("%d: ", i);
//                System.out.println(temp.get(i).toString());
//            }
//            System.out.println("\nlocalKb after update: ");
//            for (int i = 0; i < localKb.size(); i++)
//            {
//                System.out.format("%d: ", i);
//                System.out.println(localKb.get(i).toString());
//            }
            ///
            
            // reture false query if the localKb had nothing new added to it
            boolean stillNewResolvents = false;
            if (temp.size() != localKb.size()) 
            {
                stillNewResolvents = true;
            }
            else
            {
                for (int i = 0; i < temp.size(); i++)
                {
                    KBcnf oldLocalKbCNF = temp.get(i);
                    KBcnf newLocalKbCNF = localKb.get(i);
                    if (!(oldLocalKbCNF.equals(newLocalKbCNF)))
                    {
                        stillNewResolvents = true;
                        break;
                    }
                }
            }
            if (!stillNewResolvents) return false;
            
//            System.out.println("updated local kb: " + localKb.toString());
            generatedSentences.clear();
//            System.out.println("local resolution kb size: " + localKb.size());
                    
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
     * @return 
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
//            System.out.println("(gen_resolvent_clause) atomI: " + atomI.toString());
            for (KBAtom jAtoms : j.generateAtomList())
            {
                KBAtomConstant atomJ = (KBAtomConstant) jAtoms;
//                System.out.println("(gen_resolvent_clause) atomJ: " + atomJ.toString());
                
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
//        if (i.equals(j)) System.out.println("(gen_resolvent_clause_subroutine) i and j: " + i.toString() + ", " + j.toString());
        
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
//        System.out.println("\nin_kb being unified: " + in_kb.toString());
//        System.out.println("query for unify: " + query.toString() + "\n");
        List<KBcnf> temp_in_kb = in_kb;
        
        // for each axiom
        //for (KBcnf sentence : temp_in_kb)
        for (int sentenceIndex = 0; sentenceIndex < temp_in_kb.size(); sentenceIndex++)
        {
            KBcnf sentence = temp_in_kb.get(sentenceIndex);
//            System.out.println("= current sentence: " + sentence.toString());
            // for each disjunctive sentence in the CNF
            for (int i = 0; i < sentence.getDisjunctions().size(); i++)
            {
                ArrayList<KBAtom> disjSentence = sentence.getDisjunctions().get(i);
//                System.out.println("current disjunctions: " + disjSentence.toString());
                // for each atom in the disjunctive sentence
                for (int j = 0; j < disjSentence.size(); j++)
                {
                    KBAtom atom = disjSentence.get(j);
//                    System.out.println("current atom: " + atom.toString());
                    if (atom instanceof KBAtomVariable)
                    {
                        KBAtomVariable currentAtom = (KBAtomVariable) atom;
                        KBAtomConstant replacement = currentAtom.convertToConstant(query);
//                        System.out.println("Atom replacement :" + replacement.toString());
                        temp_in_kb.get(sentenceIndex).getDisjunctions().get(i).set(j, replacement);
                    }
                }
            }
        }
//        System.out.println("temp_in_kb after: " + temp_in_kb.toString());
        return temp_in_kb;
    }
    
    /**
     * @param input : A first-order definite clause provided by either the agent
     * or cyclical techniques in the Knowledge Base
     * @post percept_kb is updated
     */
    public void update(KBAtom input)
    {
        ArrayList<KBAtom> inputAsCNF = new ArrayList<>(Arrays.asList(input));
        KBcnf newData = new KBcnf(inputAsCNF);
        percept_kb.add(newData);
    }
    
    /**
     * Add sequence of conjuncts of disjuncts to the kb
     * @param conjunctsOfDisjuncts 
     */
    private void addToKBcnf(List<String> context, ArrayList<KBAtom>... conjunctsOfDisjuncts)
    {
        ArrayList<ArrayList<KBAtom>> conjunctions = new ArrayList<>();
        for (ArrayList<KBAtom> disjunction : conjunctsOfDisjuncts)
        {
            conjunctions.add(disjunction);
        }
        KBcnf newCNF = new KBcnf(conjunctions);
        for (String category : context)
        {
            contextual_kb.get(category).add(newCNF);
        }
    }
    
    /**
     * Add sequence of only disjunctive terms to the kb
     * @param context : which contextual KB mappings the axiom belongs to
     * @param atoms 
     */
    private void addToKBcnf(List<String> context, KBAtom... atoms)
    {
        ArrayList<KBAtom> onlyDisjuncts = new ArrayList<>(Arrays.asList(atoms));
        KBcnf cnfSentence = new KBcnf(onlyDisjuncts);
        for (String category : context)
        {
            contextual_kb.get(category).add(cnfSentence);
        }
    }
    
    public List<KBcnf> getKb_cnf() 
    {
        return percept_kb;
    }
    /**
     * used in tests to deal with an empty KB axiom list
     * @param percept_kb
     */
    public void setKb_cnf(List<KBcnf> percept_kb) 
    {
        this.percept_kb = percept_kb;
    }

    private ArrayList<KBcnf> axiom_RoomHasGold() 
    {
        ArrayList<KBAtom> disj = new ArrayList<>();
        disj.addAll(Arrays.asList(
            new KBAtomVariable(true, "SHINY", new int[]{0,0}), 
            new KBAtomVariable(false, "HASGOLD", new int[]{0,0})
        ));
        
        ArrayList<ArrayList<KBAtom>> disjunctions =  new ArrayList<>(Arrays.asList(disj));
        KBcnf newCNF = new KBcnf(disjunctions);
        ArrayList<KBcnf> returnedKB = new ArrayList<>();
        returnedKB.add(newCNF);
        
        // also add all relevent percepts attained thus far
        ArrayList<KBcnf> shinyPercepts = addContextualPercepts("SHINY");
        returnedKB.addAll(shinyPercepts);
        
        return returnedKB;
        
    }

    private ArrayList<KBcnf> axiom_RoomIsBlocked() 
    {
        ArrayList<KBAtom> disj = new ArrayList<>();
        disj.addAll(Arrays.asList(
            new KBAtomVariable(true, "OBST", new int[]{0,0}),
            new KBAtomVariable(false, "BLOCKED", new int[]{0,0})
        ));
        
        ArrayList<ArrayList<KBAtom>> disjunctions =  new ArrayList<>(Arrays.asList(disj));
        KBcnf newCNF = new KBcnf(disjunctions);
        ArrayList<KBcnf> returnedKB = new ArrayList<>();
        returnedKB.add(newCNF);
        
        // also add all relevent percepts attained thus far
        ArrayList<KBcnf> smellyPercepts = addContextualPercepts("OBST");
        returnedKB.addAll(smellyPercepts);
        
        return returnedKB;
    }

    /**
     * If any of the adjacent squares don't have a smell or wind in them, then the room is safe
     * @param term
     * @return 
     */
    private ArrayList<KBcnf> axiom_dynamic_RoomIsSafe(Room term) 
    {
        ArrayList<ArrayList<KBAtom>> conjunctions = new ArrayList<>();
        int row = term.getRoomRow();
        int column = term.getRoomColumn();
        
        // query cell not on wall (existing rooms on every side)
        if ((row - 1 >= 0) && (row + 1 < ActualWorld.getSize()) && (column - 1 >= 0) && (column + 1 < ActualWorld.getSize()))
        {
            // add 4 disjunction lists
            for (int i = 0; i < 4; i++)
            {
                conjunctions.add(new ArrayList<KBAtom>());
            }
            // generate the disjunctive sentences
            conjunctions.get(0).addAll(Arrays.asList(                
                new KBAtomVariable(true, "EXPLORED", new int[]{-1,0}),
                new KBAtomVariable(false,"SMELLY",   new int[]{-1,0}),
                new KBAtomVariable(false,"WINDY",    new int[]{-1,0})
            ));
            conjunctions.get(1).addAll(Arrays.asList(
                new KBAtomVariable(true, "EXPLORED", new int[]{0,1}),
                new KBAtomVariable(false,"SMELLY",   new int[]{0,1}),
                new KBAtomVariable(false,"WINDY",    new int[]{0,1})
            ));
            conjunctions.get(2).addAll(Arrays.asList(
                new KBAtomVariable(true, "EXPLORED", new int[]{1,0}),
                new KBAtomVariable(false,"SMELLY",   new int[]{1,0}),
                new KBAtomVariable(false,"WINDY",    new int[]{1,0})
            ));
            conjunctions.get(3).addAll(Arrays.asList(
                new KBAtomVariable(true, "EXPLORED", new int[]{0,-1}),
                new KBAtomVariable(false,"SMELLY",   new int[]{0,-1}),
                new KBAtomVariable(false,"WINDY",    new int[]{0,-1}),
                new KBAtomVariable(false, "SAFE", new int[]{0,0})
            ));
        }
        // query cell left column : no rooms to the left (along y=0 column)
        else if ((row - 1 < 0) && (column - 1 >= 0) && (column + 1 < ActualWorld.getSize()))
        {
            // add 3 disjunction lists
            for (int i = 0; i < 3; i++)
            {
                conjunctions.add(new ArrayList<KBAtom>());
            }
            // generate the disjunctive sentences
            conjunctions.get(0).addAll(Arrays.asList(
                new KBAtomVariable(true, "EXPLORED", new int[]{0,1}),
                new KBAtomVariable(false,"SMELLY",   new int[]{0,1}),
                new KBAtomVariable(false,"WINDY",    new int[]{0,1})
            ));
            conjunctions.get(1).addAll(Arrays.asList(
                new KBAtomVariable(true, "EXPLORED", new int[]{1,0}),
                new KBAtomVariable(false,"SMELLY",   new int[]{1,0}),
                new KBAtomVariable(false,"WINDY",    new int[]{1,0})
            ));
            conjunctions.get(2).addAll(Arrays.asList(
                new KBAtomVariable(true, "EXPLORED", new int[]{0,-1}),
                new KBAtomVariable(false,"SMELLY",   new int[]{0,-1}),
                new KBAtomVariable(false,"WINDY",    new int[]{0,-1}),
                new KBAtomVariable(false, "SAFE", new int[]{0,0})
            ));
        }
        // query cell top left corner
        else if (((row - 1 < 0) && (column + 1 >= ActualWorld.getSize())))  
        {
            // add 2 disjunction lists
            for (int i = 0; i < 2; i++)
            {
                conjunctions.add(new ArrayList<KBAtom>());
            }
            // generate the disjunctive sentences
            conjunctions.get(0).addAll(Arrays.asList(
                new KBAtomVariable(true, "EXPLORED", new int[]{0,-1}),
                new KBAtomVariable(false,"SMELLY",   new int[]{0,-1}),
                new KBAtomVariable(false,"WINDY",    new int[]{0,-1})
            ));
            conjunctions.get(1).addAll(Arrays.asList(
                new KBAtomVariable(true, "EXPLORED", new int[]{1,0}),
                new KBAtomVariable(false,"SMELLY",   new int[]{1,0}),
                new KBAtomVariable(false,"WINDY",    new int[]{1,0}),
                new KBAtomVariable(false, "SAFE", new int[]{0,0})
            ));
        }
        // query cell top row
        else if ((column + 1 >= ActualWorld.getSize()) && (row - 1 >= 0) && (row + 1 < ActualWorld.getSize()))
        {
            // add 3 disjunction lists
            for (int i = 0; i < 3; i++)
            {
                conjunctions.add(new ArrayList<KBAtom>());
            }
            // generate the disjunctive sentences
            conjunctions.get(0).addAll(Arrays.asList(
                new KBAtomVariable(true, "EXPLORED", new int[]{-1,0}),
                new KBAtomVariable(false,"SMELLY",   new int[]{-1,0}),
                new KBAtomVariable(false,"WINDY",    new int[]{-1,0})
            ));
            conjunctions.get(1).addAll(Arrays.asList(
                new KBAtomVariable(true, "EXPLORED", new int[]{0,-1}),
                new KBAtomVariable(false,"SMELLY",   new int[]{0,-1}),
                new KBAtomVariable(false,"WINDY",    new int[]{0,-1})
            ));
            conjunctions.get(2).addAll(Arrays.asList(
                new KBAtomVariable(true, "EXPLORED", new int[]{1, 0}),
                new KBAtomVariable(false,"SMELLY",   new int[]{1, 0}),
                new KBAtomVariable(false,"WINDY",    new int[]{1, 0}),
                new KBAtomVariable(false, "SAFE", new int[]{0,0})
            ));
        }
        // query cell top right corner
        else if ((row + 1 >= ActualWorld.getSize()) && column + 1 >= ActualWorld.getSize()) 
        {
            // add 2 disjunction lists
            for (int i = 0; i < 2; i++)
            {
                conjunctions.add(new ArrayList<KBAtom>());
            }
            // generate the disjunctive sentences
            conjunctions.get(0).addAll(Arrays.asList(
                new KBAtomVariable(true, "EXPLORED", new int[]{-1,0}),
                new KBAtomVariable(false,"SMELLY",   new int[]{-1,0}),
                new KBAtomVariable(false,"WINDY",    new int[]{-1,0})
            ));
            conjunctions.get(1).addAll(Arrays.asList(
                new KBAtomVariable(true, "EXPLORED", new int[]{0,-1}),
                new KBAtomVariable(false,"SMELLY",   new int[]{0,-1}),
                new KBAtomVariable(false,"WINDY",    new int[]{0,-1}),
                new KBAtomVariable(false, "SAFE", new int[]{0,0})
            ));
        }
        // query cell right column
        else if ((row + 1 >= ActualWorld.getSize()) && (column - 1 >= 0) && (column + 1 < ActualWorld.getSize()))
        {
            // add 3 disjunction lists
            for (int i = 0; i < 3; i++)
            {
                conjunctions.add(new ArrayList<KBAtom>());
            }
            // generate the disjunctive sentences
            conjunctions.get(0).addAll(Arrays.asList(
                new KBAtomVariable(true, "EXPLORED", new int[]{-1, 0}),
                new KBAtomVariable(false,"SMELLY",   new int[]{-1, 0}),
                new KBAtomVariable(false,"WINDY",    new int[]{-1, 0})
            ));
            conjunctions.get(1).addAll(Arrays.asList(
                new KBAtomVariable(true, "EXPLORED", new int[]{0, 1}),
                new KBAtomVariable(false,"SMELLY",   new int[]{0, 1}),
                new KBAtomVariable(false,"WINDY",    new int[]{0, 1})
            ));
            conjunctions.get(2).addAll(Arrays.asList(
                new KBAtomVariable(true, "EXPLORED", new int[]{0, -1}),
                new KBAtomVariable(false,"SMELLY",   new int[]{0, -1}),
                new KBAtomVariable(false,"WINDY",    new int[]{0, -1}),
                new KBAtomVariable(false, "SAFE", new int[]{0,0})
            ));
        }
        // query cell bottom right corner
        else if ((row + 1 >= ActualWorld.getSize()) && column - 1 < 0)
        {
            // add 2 disjunction lists
            for (int i = 0; i < 2; i++)
            {
                conjunctions.add(new ArrayList<KBAtom>());
            }
            // generate the disjunctive sentences
            conjunctions.get(0).addAll(Arrays.asList(
                new KBAtomVariable(true, "EXPLORED", new int[]{-1,0}),
                new KBAtomVariable(false,"SMELLY",   new int[]{-1,0}),
                new KBAtomVariable(false,"WINDY",    new int[]{-1,0})
            ));
            conjunctions.get(1).addAll(Arrays.asList(
                new KBAtomVariable(true, "EXPLORED", new int[]{0, 1}),
                new KBAtomVariable(false,"SMELLY",   new int[]{0, 1}),
                new KBAtomVariable(false,"WINDY",    new int[]{0, 1}),
                new KBAtomVariable(false, "SAFE",    new int[]{0,0})
            ));
        }
        // query cell bottom row
        else if ((column - 1 < 0) && (row - 1 >= 0) && (row + 1 < ActualWorld.getSize()))
        {
            // add 3 disjunction lists
            for (int i = 0; i < 3; i++)
            {
                conjunctions.add(new ArrayList<KBAtom>());
            }
            // generate the disjunctive sentences
            conjunctions.get(0).addAll(Arrays.asList(
                new KBAtomVariable(true, "EXPLORED", new int[]{-1, 0}),
                new KBAtomVariable(false,"SMELLY",   new int[]{-1, 0}),
                new KBAtomVariable(false,"WINDY",    new int[]{-1, 0})
            ));
            conjunctions.get(1).addAll(Arrays.asList(
                new KBAtomVariable(true, "EXPLORED", new int[]{0, 1}),
                new KBAtomVariable(false,"SMELLY",   new int[]{0, 1}),
                new KBAtomVariable(false,"WINDY",    new int[]{0, 1})
            ));
            conjunctions.get(2).addAll(Arrays.asList(
                new KBAtomVariable(true, "EXPLORED", new int[]{1, 0}),
                new KBAtomVariable(false,"SMELLY",   new int[]{1, 0}),
                new KBAtomVariable(false,"WINDY",    new int[]{1, 0}),
                new KBAtomVariable(false, "SAFE",    new int[]{0,0})
            ));  
        }  
        // query cell bottom left corner
        else if((row - 1 < 0) && column - 1 < 0)
        {
            // add 2 disjunction lists
            for (int i = 0; i < 2; i++)
            {
                conjunctions.add(new ArrayList<KBAtom>());
            }
            // generate the disjunctive sentences
            conjunctions.get(0).addAll(Arrays.asList(
                new KBAtomVariable(true, "EXPLORED", new int[]{1, 0}),
                new KBAtomVariable(false,"SMELLY",   new int[]{1, 0}),
                new KBAtomVariable(false,"WINDY",    new int[]{1, 0})
            ));
            conjunctions.get(1).addAll(Arrays.asList(
                new KBAtomVariable(true, "EXPLORED", new int[]{0, 1}),
                new KBAtomVariable(false,"SMELLY",   new int[]{0, 1}),
                new KBAtomVariable(false,"WINDY",    new int[]{0, 1}),
                new KBAtomVariable(false, "SAFE",    new int[]{0,0})
            ));   
        }  
        else
        {
            throw new RuntimeException("None of the room positions calculated correctly"
                    + " during axiom_dynamic_RoomIsSafe");
        }
        KBcnf newCNF = new KBcnf(conjunctions);
        
        ArrayList<KBcnf> returnedKB = new ArrayList<>();
        // add the variable axioms
        returnedKB.add(newCNF);
        // also add all smelly, windy, and obst percepts attained thus far
        ArrayList<KBcnf> contextualPercepts = addContextualPercepts("SMELLY", "WINDY", "EXPLORED");
        returnedKB.addAll(contextualPercepts);
        
        return returnedKB;
    }

    /**
     * Room has wumpus if all adjacent rooms are smelly
     * gets only relevant adjacent cell axioms when querying on a wumpus (or pit)
     */
    private ArrayList<KBcnf> axiom_dynamic_RoomHasWumpus(Room term)
    {
        ArrayList<KBAtom> disj = new ArrayList<>();
        int row = term.getRoomRow();
        int column = term.getRoomColumn();
        
        // query cell not on wall (existing rooms on every side)
        if ((row - 1 >= 0) && (row + 1 < ActualWorld.getSize()) && (column - 1 >= 0) && (column + 1 < ActualWorld.getSize()))
        {
            disj.addAll(Arrays.asList(                
                new KBAtomVariable(true, "SMELLY", new int[]{-1,0}),
                new KBAtomVariable(true, "SMELLY", new int[]{0,1}),
                new KBAtomVariable(true, "SMELLY", new int[]{1,0}),
                new KBAtomVariable(true, "SMELLY", new int[]{0,-1}),
                new KBAtomVariable(false, "WUMPUS", new int[]{0,0})
            ));
        }
        // query cell left column : no rooms to the left (along y=0 column)
        else if ((row - 1 < 0) && (column - 1 >= 0) && (column + 1 < ActualWorld.getSize()))
        {
            disj.addAll(Arrays.asList( 
                new KBAtomVariable(true, "SMELLY", new int[]{0,1}),
                new KBAtomVariable(true, "SMELLY", new int[]{1,0}),
                new KBAtomVariable(true, "SMELLY", new int[]{0,-1}),
                new KBAtomVariable(false, "WUMPUS", new int[]{0,0}) 
            ));     
        }
        // query cell top left corner
        else if (((row - 1 < 0) && (column + 1 >= ActualWorld.getSize())))  
        {
            disj.addAll(Arrays.asList( 
                new KBAtomVariable(true, "SMELLY", new int[]{1,0}),
                new KBAtomVariable(true, "SMELLY", new int[]{0,-1}),
                new KBAtomVariable(false, "WUMPUS", new int[]{0,0})
            )
            );     
        }  
        // query cell top row
        else if ((column + 1 >= ActualWorld.getSize()) && (row - 1 >= 0) && (row + 1 < ActualWorld.getSize()))
        {
            disj.addAll(Arrays.asList( 
                new KBAtomVariable(true, "SMELLY", new int[]{-1,0}),
                new KBAtomVariable(true, "SMELLY", new int[]{1,0}),
                new KBAtomVariable(true, "SMELLY", new int[]{0,-1}),
                new KBAtomVariable(false, "WUMPUS", new int[]{0,0})
            )
            );     
        }  
        // query cell top right corner
        else if ((row + 1 >= ActualWorld.getSize()) && column + 1 >= ActualWorld.getSize()) 
        {
            disj.addAll(Arrays.asList( 
                new KBAtomVariable(true, "SMELLY", new int[]{-1,0}),
                new KBAtomVariable(true, "SMELLY", new int[]{0,-1}),
                new KBAtomVariable(false, "WUMPUS", new int[]{0,0})
            )
            );     
        }     
        // query cell right column
        else if ((row + 1 >= ActualWorld.getSize()) && (column - 1 >= 0) && (column + 1 < ActualWorld.getSize()))
        {
            disj.addAll(Arrays.asList( 
                new KBAtomVariable(true, "SMELLY", new int[]{-1,0}),
                new KBAtomVariable(true, "SMELLY", new int[]{0,1}),
                new KBAtomVariable(true, "SMELLY", new int[]{0,-1}),
                new KBAtomVariable(false, "WUMPUS", new int[]{0,0}) 
            )
            );     
        }  
        // query cell bottom right corner
        else if ((row + 1 >= ActualWorld.getSize()) && column - 1 < 0)
        {
            disj.addAll(Arrays.asList( 
                new KBAtomVariable(true, "SMELLY", new int[]{-1,0}),
                new KBAtomVariable(true, "SMELLY", new int[]{0,1}),
                new KBAtomVariable(false, "WUMPUS", new int[]{0,0})
            )
            );     
        }  
        // query cell bottom row
        else if ((column - 1 < 0) && (row - 1 >= 0) && (row + 1 < ActualWorld.getSize()))
        {
            disj.addAll(Arrays.asList( 
                new KBAtomVariable(true, "SMELLY", new int[]{-1,0}),
                new KBAtomVariable(true, "SMELLY", new int[]{0,1}),
                new KBAtomVariable(true, "SMELLY", new int[]{1,0}),
                new KBAtomVariable(false, "WUMPUS", new int[]{0,0})
            )
            );     
        }  
        // query cell bottom left corner
        else if((row - 1 < 0) && column - 1 < 0)
        {
            disj.addAll(Arrays.asList( 
                new KBAtomVariable(true, "SMELLY", new int[]{0,1}),
                new KBAtomVariable(true, "SMELLY", new int[]{1,0}),
                new KBAtomVariable(false, "WUMPUS", new int[]{0,0})
            )
            );     
        }  
        else
        {
            throw new RuntimeException("None of the room positions calculated correctly"
                    + " during axiom_dynamic_RoomHasWumpus");
        }
        ArrayList<ArrayList<KBAtom>> disjunctions =  new ArrayList<>(Arrays.asList(disj));
        KBcnf newCNF = new KBcnf(disjunctions);
        
        ArrayList<KBcnf> returnedKB = new ArrayList<>();
        // add the variable axioms
        returnedKB.add(newCNF);
        // also add all smelly percepts attained thus far
        ArrayList<KBcnf> smellyPercepts = addContextualPercepts("SMELLY");
        returnedKB.addAll(smellyPercepts);
        
        return returnedKB;
    }
    
    /**
     * Used when filling up a contextual_kb. Fills up that mapping with 
     * only the relevant percept (KBAtomConstant) values it will need to know about
     * @param context
     * @return 
     */
    private ArrayList<KBcnf> addContextualPercepts(String... context)
    {
        ArrayList<KBcnf> returnedPercepts = new ArrayList<>();
        
        for (KBcnf cnf : percept_kb)
        {
            List<KBAtom> currentAtoms = cnf.generateAtomList();
            for (KBAtom atom : currentAtoms)
            {
                // atom constants are results of agent perceptions
                if (atom instanceof KBAtomConstant)
                {
                    for (String predicate : context)
                    {
                        if (atom.predicate.equals(predicate))
                        {
                            KBcnf perceptCNF = new KBcnf(atom);
                            returnedPercepts.add(perceptCNF);
                        }
                    }
                }
            }
        }
        
        return returnedPercepts;
    }

    /**
     * sorts the local frontier from closest to furthest away from current location
     * @param frontier
     * @param row : current row location
     * @param column : current column location
     * @return 
     */
    private ArrayList<Room> sortFrontier(Map<Integer, Room> frontier, int row, int column) 
    {
        ArrayList<Room> sortedList = new ArrayList<>();
        for (Room room : frontier.values())
        {
            if (sortedList.isEmpty()) 
            {
                sortedList.add(room);
            }
            else
            {
                int distance = (Math.abs(room.getRoomRow() - row) + Math.abs(room.getRoomColumn() - column));
                
                for (int i = 0; i < sortedList.size(); i++)
                {
                    Room otherRoom = sortedList.get(i);
                    
                    if ((otherRoom.getRoomRow() == room.getRoomRow()) && (otherRoom.getRoomColumn() == room.getRoomColumn())) { } // do nothing
                    else if (distance <= (Math.abs(otherRoom.getRoomRow() - row) + Math.abs(otherRoom.getRoomColumn() - column)))
                    {
                        sortedList.add(i, room);
                        break;
                    }
                    else if (i == sortedList.size() - 1)
                    {
                        sortedList.add(room);
                        break;
                    }
                    else
                    {
                        sortedList.add(room);
                        break;
                    }
                }
            }
        }
        if (sortedList.size() != frontier.size()) throw new RuntimeException("Sorted list doesn't match frontier size");
        return sortedList;
    }
    
    private void printFrontier(Map<Integer, Room> frontier)
    {
        for (Integer key : frontier.keySet())
        {
            System.out.format("(%d, %d)%n", frontier.get(key).getRoomRow(), frontier.get(key).getRoomColumn());
        }
    }
}
