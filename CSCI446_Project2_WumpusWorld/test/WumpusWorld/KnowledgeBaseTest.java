package WumpusWorld;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author David
 */
public class KnowledgeBaseTest {
    
    public KnowledgeBaseTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void test_KB_axioms_shiny()
    {
        World world = new World(3);
        KnowledgeBase kb = new KnowledgeBase();
        
        KBAtom shinyPercept1 = new KBAtomConstant(false, "SHINY", World.getRoom(0, 0)); // is shiny
        KBAtom shinyPercept2 = new KBAtomConstant(true, "SHINY", World.getRoom(0, 1));  // is not shiny
        
        kb.update(shinyPercept1);
        kb.update(shinyPercept2);
        
        KBAtomConstant queryAtom1 = new KBAtomConstant(false, "HASGOLD", World.getRoom(0, 0)); // does room (x,y) have gold given the current KB?
        System.out.println("query: " + queryAtom1.toString());
        boolean expectedOutput1 = true;
        boolean actualOutput1 = kb.query(queryAtom1);   
        System.out.format("query result: %b%n%n", actualOutput1);
        assertEquals(expectedOutput1, actualOutput1);
        
        KBAtomConstant queryAtom2 = new KBAtomConstant(false, "HASGOLD", World.getRoom(0, 1)); // does room (x,y) have gold given the current KB?
        System.out.println("query: " + queryAtom2.toString());
        boolean expectedOutput2 = false;
        boolean actualOutput2 = kb.query(queryAtom2);   
        System.out.format("query result: %b%n%n", actualOutput2);
        assertEquals(expectedOutput2, actualOutput2);
    }
    
    @Test
    public void test_KB_axioms_obst()
    {
        World world = new World(3);
        KnowledgeBase kb = new KnowledgeBase();
        
        KBAtom obstPercept1 = new KBAtomConstant(false, "OBST", World.getRoom(0, 0)); // has obstacle
        KBAtom obstPercept2 = new KBAtomConstant(true, "OBST", World.getRoom(0, 1));  // does not have obstacle
        
        kb.update(obstPercept1);
        kb.update(obstPercept2);
        
        KBAtomConstant queryAtom1 = new KBAtomConstant(false, "BLOCKED", World.getRoom(0, 0)); // is room (x,y) blocked given the current KB?
        System.out.println("query: " + queryAtom1.toString());
        boolean expectedOutput1 = true;
        boolean actualOutput1 = kb.query(queryAtom1);   
        System.out.format("query result: %b%n%n", actualOutput1);
        assertEquals(expectedOutput1, actualOutput1);
        
        KBAtomConstant queryAtom2 = new KBAtomConstant(false, "BLOCKED", World.getRoom(0, 1)); //  // is room (x,y) blocked given the current KB?
        System.out.println("query: " + queryAtom2.toString());
        boolean expectedOutput2 = false;
        boolean actualOutput2 = kb.query(queryAtom2);   
        System.out.format("query result: %b%n%n", actualOutput2);
        assertEquals(expectedOutput2, actualOutput2);
    }
    
    /**
     * test that running the gen_resolvent_clause method on two CNFs
     * produces a new CNF with the resolved atom not in it
     */
    @Test
    public void test_gen_resolvent_clause()
    {
        Room room22 = new Room(2,2);
        
        KBcnf cnf1 = new KBcnf(
                new KBAtomConstant(true, "SHINY", room22),
                new KBAtomConstant(false, "HASGOLD", room22)
        );
        KBcnf cnf2 = new KBcnf(
                new KBAtomConstant(false, "SHINY", room22)
        );
        
        KBcnf expectedResult = new KBcnf(
                new KBAtomConstant(false, "HASGOLD", room22)
        );
        
        KnowledgeBase kb = new KnowledgeBase();
        kb.setKb_cnf(null);
        
        KBcnf actualResult = kb.gen_resolvent_clause(cnf1, cnf2);
        System.out.println("expected result: " + expectedResult.toString());
        System.out.println("actual result: " + actualResult.toString());
        assertEquals(expectedResult.toString(), actualResult.toString());
    }
}
