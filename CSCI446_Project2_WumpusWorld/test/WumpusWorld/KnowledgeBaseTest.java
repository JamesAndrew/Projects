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
    public void test_KB_axioms_safe()
    {
        World world = new World(3);
        KnowledgeBase kb = new KnowledgeBase();
        
        KBAtom obstPercept1 = new KBAtomConstant(true, "OBST", World.getRoom(0, 0)); 
        KBAtom obstPercept2 = new KBAtomConstant(true, "PIT", World.getRoom(0, 0));  
        KBAtom obstPercept3 = new KBAtomConstant(true, "WUMPUS", World.getRoom(0, 0)); 
        KBAtom obstPercept4 = new KBAtomConstant(true, "BLOCKED", World.getRoom(0, 0)); 
        
        KBAtom obstPercept5 = new KBAtomConstant(false, "SMELLY", World.getRoom(0, 1)); 
        KBAtom obstPercept6 = new KBAtomConstant(false, "WINDY", World.getRoom(0, 2)); 
        
        KBAtom obstPercept7 = new KBAtomConstant(false, "WUMPUS", World.getRoom(1, 0));  
        
        kb.update(obstPercept1);
        kb.update(obstPercept2);
        kb.update(obstPercept3);
        kb.update(obstPercept4);
        kb.update(obstPercept5);
        kb.update(obstPercept6);
        kb.update(obstPercept7);
        
        KBAtomConstant queryAtom1 = new KBAtomConstant(false, "SAFE", World.getRoom(0, 0)); // is room (0,0) safe given the current KB?
        System.out.println("query: " + queryAtom1.toString());
        boolean expectedOutput1 = true;
        boolean actualOutput1 = kb.query(queryAtom1);   
        System.out.format("query result: %b%n%n", actualOutput1);
        assertEquals(expectedOutput1, actualOutput1);
        
        KBAtomConstant queryAtom3 = new KBAtomConstant(false, "SAFE", World.getRoom(0, 1)); // is room (0,1) safe given the current KB?
        System.out.println("query: " + queryAtom3.toString());
        boolean expectedOutput3 = true;
        boolean actualOutput3 = kb.query(queryAtom3);   
        System.out.format("query result: %b%n%n", actualOutput3);
        assertEquals(expectedOutput3, actualOutput3);
        
        KBAtomConstant queryAtom4 = new KBAtomConstant(false, "SAFE", World.getRoom(0, 2)); // is room (0,2) safe given the current KB?
        System.out.println("query: " + queryAtom4.toString());
        boolean expectedOutput4 = true;
        boolean actualOutput4 = kb.query(queryAtom4);   
        System.out.format("query result: %b%n%n", actualOutput4);
        assertEquals(expectedOutput4, actualOutput4);
        
        KBAtomConstant queryAtom5 = new KBAtomConstant(false, "SAFE", World.getRoom(1, 0)); // is room (1,0) safe given the current KB? (room 1,0 has a wumpus)
        System.out.println("query: " + queryAtom5.toString());
        boolean expectedOutput5 = false;
        boolean actualOutput5 = kb.query(queryAtom5);   
        System.out.format("query result: %b%n%n", actualOutput5);
        assertEquals(expectedOutput5, actualOutput5);
    }
    
    @Test
    public void test_KB_axioms_not_safe()
    {
        World world = new World(3);
        KnowledgeBase kb = new KnowledgeBase();
        
        KBAtom obstPercept1 = new KBAtomConstant(false, "WUMPUS", World.getRoom(0, 0)); 
        KBAtom obstPercept2 = new KBAtomConstant(false, "PIT", World.getRoom(0, 1));  
        KBAtom obstPercept3 = new KBAtomConstant(false, "OBST", World.getRoom(0, 2)); 
        
        kb.update(obstPercept1);
        kb.update(obstPercept2);
        kb.update(obstPercept3);
        
        KBAtomConstant queryAtom1 = new KBAtomConstant(false, "SAFE", World.getRoom(0, 0)); // is room (0,0) safe given the current KB?
        System.out.println("query: " + queryAtom1.toString());
        boolean expectedOutput1 = false;
        boolean actualOutput1 = kb.query(queryAtom1);   
        System.out.format("query result: %b%n%n", actualOutput1);
        assertEquals(expectedOutput1, actualOutput1);
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
    public void test_KB_axioms_resolveWumpus()
    {
        World world = new World(3);
        KnowledgeBase kb = new KnowledgeBase();
        
        KBAtom smellyPercept1 = new KBAtomConstant(false, "SMELLY", World.getRoom(0, 1)); 
        KBAtom smellyPercept2 = new KBAtomConstant(false, "SMELLY", World.getRoom(1, 2)); 
        KBAtom smellyPercept3 = new KBAtomConstant(false, "SMELLY", World.getRoom(2, 1)); 
        KBAtom smellyPercept4 = new KBAtomConstant(false, "SMELLY", World.getRoom(1, 0)); 
        
        kb.update(smellyPercept1);
        kb.update(smellyPercept2);
        kb.update(smellyPercept3);
        kb.update(smellyPercept4);
        
        KBAtomConstant queryAtom1 = new KBAtomConstant(false, "WUMPUS", World.getRoom(1, 1)); // is there a wumpus in room (1,1)?
        System.out.println("query: " + queryAtom1.toString());
        boolean expectedOutput1 = true;
        boolean actualOutput1 = kb.query(queryAtom1);   
        System.out.format("query result: %b%n%n", actualOutput1);
        assertEquals(expectedOutput1, actualOutput1);
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
