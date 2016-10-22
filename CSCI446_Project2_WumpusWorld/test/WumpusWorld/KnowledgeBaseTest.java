package WumpusWorld;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author David
 */
public class KnowledgeBaseTest {
    
    public KnowledgeBaseTest() { }
    
    @Test
    public void test_KB_axioms_safe_centered()
    {
        ActualWorld world = new ActualWorld(3);
        KnowledgeBase kb = new KnowledgeBase();
        
        // no smell or wind from left cell
        KBAtom safePercept0 = new KBAtomConstant(false, "EXPLORED", ActualWorld.getRoom(0, 1)); 
        KBAtom safePercept1 = new KBAtomConstant(true, "SMELLY",    ActualWorld.getRoom(0, 1)); 
        KBAtom safePercept2 = new KBAtomConstant(true, "WINDY",     ActualWorld.getRoom(0, 1)); 
        // is smell from right cell
        KBAtom safePercept3 = new KBAtomConstant(false, "EXPLORED", ActualWorld.getRoom(1, 0)); 
        KBAtom safePercept4 = new KBAtomConstant(true,  "SMELLY",   ActualWorld.getRoom(1, 0)); 
        KBAtom safePercept5 = new KBAtomConstant(false, "WINDY",    ActualWorld.getRoom(1, 0)); 
        
        kb.update(safePercept0);
        kb.update(safePercept1);
        kb.update(safePercept2);
        kb.update(safePercept3);
        kb.update(safePercept4);
        kb.update(safePercept5);
        
        KBAtomConstant queryAtom1 = new KBAtomConstant(true, "SAFE", ActualWorld.getRoom(1, 1)); // is room (1,1) safe
        System.out.println("query: " + queryAtom1.toString());
        boolean expectedOutput1 = true;
        boolean actualOutput1 = kb.query(queryAtom1);   
        System.out.format("query result: %b%n%n", actualOutput1);
        assertEquals(expectedOutput1, actualOutput1);
    }
    
    @Test
    public void test_KB_axioms_safe_leftWall()
    {
        ActualWorld world = new ActualWorld(3);
        KnowledgeBase kb = new KnowledgeBase();
        
        KBAtom safePercept0 = new KBAtomConstant(false, "EXPLORED", ActualWorld.getRoom(0, 2)); 
        KBAtom safePercept1 = new KBAtomConstant(true,  "SMELLY",   ActualWorld.getRoom(0, 2)); 
        KBAtom safePercept2 = new KBAtomConstant(true,  "WINDY",    ActualWorld.getRoom(0, 2)); 
        // is smell from other adjacent cell
        KBAtom safePercept3 = new KBAtomConstant(false, "EXPLORED", ActualWorld.getRoom(1, 1)); 
        KBAtom safePercept4 = new KBAtomConstant(true,  "SMELLY",   ActualWorld.getRoom(1, 1)); 
        KBAtom safePercept5 = new KBAtomConstant(false, "WINDY",    ActualWorld.getRoom(1, 1)); 
        
        kb.update(safePercept0);
        kb.update(safePercept1);
        kb.update(safePercept2);
        kb.update(safePercept3);
        kb.update(safePercept4);
        kb.update(safePercept5);
        
        KBAtomConstant queryAtom1 = new KBAtomConstant(false, "SAFE", ActualWorld.getRoom(0, 1)); // is room (0,1) safe
        System.out.println("query: " + queryAtom1.toString());
        boolean expectedOutput1 = true;
        boolean actualOutput1 = kb.query(queryAtom1);   
        System.out.format("query result: %b%n%n", actualOutput1);
        assertEquals(expectedOutput1, actualOutput1);
    }
    
    @Test
    public void test_KB_axioms_safe_topLeftCorner()
    {
        ActualWorld world = new ActualWorld(3);
        KnowledgeBase kb = new KnowledgeBase();
        
        KBAtom safePercept0 = new KBAtomConstant(false, "EXPLORED",  ActualWorld.getRoom(0, 1)); 
        KBAtom safePercept1 = new KBAtomConstant(true,  "SMELLY",    ActualWorld.getRoom(0, 1)); 
        KBAtom safePercept2 = new KBAtomConstant(true,  "WINDY",     ActualWorld.getRoom(0, 1)); 
        // is smell from other adjacent cell
        KBAtom safePercept3 = new KBAtomConstant(false, "EXPLORED", ActualWorld.getRoom(1, 2)); 
        KBAtom safePercept4 = new KBAtomConstant(true,  "SMELLY",   ActualWorld.getRoom(1, 2)); 
        KBAtom safePercept5 = new KBAtomConstant(false, "WINDY",    ActualWorld.getRoom(1, 2)); 
        
        kb.update(safePercept0);
        kb.update(safePercept1);
        kb.update(safePercept2);
        kb.update(safePercept3);
        kb.update(safePercept4);
        kb.update(safePercept5);
        
        KBAtomConstant queryAtom1 = new KBAtomConstant(false, "SAFE", ActualWorld.getRoom(0, 2)); // is room (0,1) safe
        System.out.println("query: " + queryAtom1.toString());
        boolean expectedOutput1 = true;
        boolean actualOutput1 = kb.query(queryAtom1);   
        System.out.format("query result: %b%n%n", actualOutput1);
        assertEquals(expectedOutput1, actualOutput1);
    }
    
    @Test
    public void test_KB_axioms_safe_topWall()
    {
        ActualWorld world = new ActualWorld(3);
        KnowledgeBase kb = new KnowledgeBase();
        
        KBAtom safePercept0 = new KBAtomConstant(false, "EXPLORED", ActualWorld.getRoom(1, 1)); 
        KBAtom safePercept1 = new KBAtomConstant(true,  "SMELLY",   ActualWorld.getRoom(1, 1)); 
        KBAtom safePercept2 = new KBAtomConstant(true,  "WINDY",    ActualWorld.getRoom(1, 1)); 
        // is smell from other adjacent cell
        KBAtom safePercept3 = new KBAtomConstant(false, "EXPLORED", ActualWorld.getRoom(0, 2)); 
        KBAtom safePercept4 = new KBAtomConstant(true,  "SMELLY",   ActualWorld.getRoom(0, 2)); 
        KBAtom safePercept5 = new KBAtomConstant(false, "WINDY",    ActualWorld.getRoom(0, 2)); 
        
        kb.update(safePercept0);
        kb.update(safePercept1);
        kb.update(safePercept2);
        kb.update(safePercept3);
        kb.update(safePercept4);
        kb.update(safePercept5);
        
        KBAtomConstant queryAtom1 = new KBAtomConstant(false, "SAFE", ActualWorld.getRoom(1, 2)); // is room safe?
        System.out.println("query: " + queryAtom1.toString());
        boolean expectedOutput1 = true;
        boolean actualOutput1 = kb.query(queryAtom1);   
        System.out.format("query result: %b%n%n", actualOutput1);
        assertEquals(expectedOutput1, actualOutput1);
    }
    
    @Test
    public void test_KB_axioms_safe_topRightCorner()
    {
        ActualWorld world = new ActualWorld(3);
        KnowledgeBase kb = new KnowledgeBase();
        
        KBAtom safePercept0 = new KBAtomConstant(false, "EXPLORED", ActualWorld.getRoom(1, 2)); 
        KBAtom safePercept1 = new KBAtomConstant(true,  "SMELLY",   ActualWorld.getRoom(1, 2)); 
        KBAtom safePercept2 = new KBAtomConstant(true,  "WINDY",    ActualWorld.getRoom(1, 2)); 
        // is smell from other adjacent cell
        KBAtom safePercept3 = new KBAtomConstant(false, "EXPLORED", ActualWorld.getRoom(2, 1)); 
        KBAtom safePercept4 = new KBAtomConstant(true,  "SMELLY",   ActualWorld.getRoom(2, 1)); 
        KBAtom safePercept5 = new KBAtomConstant(false, "WINDY",    ActualWorld.getRoom(2, 1)); 
        
        kb.update(safePercept0);
        kb.update(safePercept1);
        kb.update(safePercept2);
        kb.update(safePercept3);
        kb.update(safePercept4);
        kb.update(safePercept5);
        
        KBAtomConstant queryAtom1 = new KBAtomConstant(false, "SAFE", ActualWorld.getRoom(2, 2)); // is room safe?
        System.out.println("query: " + queryAtom1.toString());
        boolean expectedOutput1 = true;
        boolean actualOutput1 = kb.query(queryAtom1);   
        System.out.format("query result: %b%n%n", actualOutput1);
        assertEquals(expectedOutput1, actualOutput1);
    }
    
    @Test
    public void test_KB_axioms_safe_rightWall()
    {
        ActualWorld world = new ActualWorld(3);
        KnowledgeBase kb = new KnowledgeBase();
        
        KBAtom safePercept0 = new KBAtomConstant(false, "EXPLORED", ActualWorld.getRoom(1, 1)); 
        KBAtom safePercept1 = new KBAtomConstant(true,  "SMELLY",   ActualWorld.getRoom(1, 1)); 
        KBAtom safePercept2 = new KBAtomConstant(true,  "WINDY",    ActualWorld.getRoom(1, 1)); 
        // is smell from other adjacent cell
        KBAtom safePercept3 = new KBAtomConstant(false, "EXPLORED", ActualWorld.getRoom(2, 0)); 
        KBAtom safePercept4 = new KBAtomConstant(true,  "SMELLY",   ActualWorld.getRoom(2, 0)); 
        KBAtom safePercept5 = new KBAtomConstant(false, "WINDY",    ActualWorld.getRoom(2, 0)); 
        
        kb.update(safePercept0);
        kb.update(safePercept1);
        kb.update(safePercept2);
        kb.update(safePercept3);
        kb.update(safePercept4);
        kb.update(safePercept5);
        
        KBAtomConstant queryAtom1 = new KBAtomConstant(false, "SAFE", ActualWorld.getRoom(2, 1)); // is room safe?
        System.out.println("query: " + queryAtom1.toString());
        boolean expectedOutput1 = true;
        boolean actualOutput1 = kb.query(queryAtom1);   
        System.out.format("query result: %b%n%n", actualOutput1);
        assertEquals(expectedOutput1, actualOutput1);
    }
    
    @Test
    public void test_KB_axioms_safe_bottomRightCorner()
    {
        ActualWorld world = new ActualWorld(3);
        KnowledgeBase kb = new KnowledgeBase();
        
        KBAtom safePercept0 = new KBAtomConstant(false, "EXPLORED", ActualWorld.getRoom(1, 1)); 
        KBAtom safePercept1 = new KBAtomConstant(true,  "SMELLY",   ActualWorld.getRoom(1, 1)); 
        KBAtom safePercept2 = new KBAtomConstant(true,  "WINDY",    ActualWorld.getRoom(1, 1)); 
        // is smell from other adjacent cell
        KBAtom safePercept3 = new KBAtomConstant(false, "EXPLORED", ActualWorld.getRoom(2, 0)); 
        KBAtom safePercept4 = new KBAtomConstant(true,  "SMELLY",   ActualWorld.getRoom(2, 0)); 
        KBAtom safePercept5 = new KBAtomConstant(false, "WINDY",    ActualWorld.getRoom(2, 0)); 
        
        kb.update(safePercept0);
        kb.update(safePercept1);
        kb.update(safePercept2);
        kb.update(safePercept3);
        kb.update(safePercept4);
        kb.update(safePercept5);
        
        KBAtomConstant queryAtom1 = new KBAtomConstant(false, "SAFE", ActualWorld.getRoom(2, 1)); // is room safe?
        System.out.println("query: " + queryAtom1.toString());
        boolean expectedOutput1 = true;
        boolean actualOutput1 = kb.query(queryAtom1);   
        System.out.format("query result: %b%n%n", actualOutput1);
        assertEquals(expectedOutput1, actualOutput1);
    }
    
    @Test
    public void test_KB_axioms_safe_bottomWall()
    {
        ActualWorld world = new ActualWorld(3);
        KnowledgeBase kb = new KnowledgeBase();
        
        KBAtom safePercept0 = new KBAtomConstant(false, "EXPLORED", ActualWorld.getRoom(0, 0)); 
        KBAtom safePercept1 = new KBAtomConstant(true,  "SMELLY",   ActualWorld.getRoom(0, 0)); 
        KBAtom safePercept2 = new KBAtomConstant(true,  "WINDY",    ActualWorld.getRoom(0, 0)); 
        // is smell from other adjacent cell
        KBAtom safePercept3 = new KBAtomConstant(false, "EXPLORED", ActualWorld.getRoom(1, 1)); 
        KBAtom safePercept4 = new KBAtomConstant(true,  "SMELLY",   ActualWorld.getRoom(1, 1)); 
        KBAtom safePercept5 = new KBAtomConstant(false, "WINDY",    ActualWorld.getRoom(1, 1)); 
        
        kb.update(safePercept0);
        kb.update(safePercept1);
        kb.update(safePercept2);
        kb.update(safePercept3);
        kb.update(safePercept4);
        kb.update(safePercept5);
        
        KBAtomConstant queryAtom1 = new KBAtomConstant(false, "SAFE", ActualWorld.getRoom(1, 0)); // is room safe?
        System.out.println("query: " + queryAtom1.toString());
        boolean expectedOutput1 = true;
        boolean actualOutput1 = kb.query(queryAtom1);   
        System.out.format("query result: %b%n%n", actualOutput1);
        assertEquals(expectedOutput1, actualOutput1);
    }
    
    @Test
    public void test_KB_axioms_safe_bottomLeftCorner()
    {
        ActualWorld world = new ActualWorld(3);
        KnowledgeBase kb = new KnowledgeBase();
        
        KBAtom safePercept0 = new KBAtomConstant(false, "EXPLORED", ActualWorld.getRoom(0, 1)); 
        KBAtom safePercept1 = new KBAtomConstant(true,  "SMELLY",   ActualWorld.getRoom(0, 1)); 
        KBAtom safePercept2 = new KBAtomConstant(true,  "WINDY",    ActualWorld.getRoom(0, 1)); 
        // is smell from other adjacent cell
        KBAtom safePercept3 = new KBAtomConstant(false, "EXPLORED", ActualWorld.getRoom(1, 0)); 
        KBAtom safePercept4 = new KBAtomConstant(true,  "SMELLY",   ActualWorld.getRoom(1, 0)); 
        KBAtom safePercept5 = new KBAtomConstant(false, "WINDY",    ActualWorld.getRoom(1, 0)); 
        
        kb.update(safePercept0);
        kb.update(safePercept1);
        kb.update(safePercept2);
        kb.update(safePercept3);
        kb.update(safePercept4);
        kb.update(safePercept5);
        
        KBAtomConstant queryAtom1 = new KBAtomConstant(false, "SAFE", ActualWorld.getRoom(0, 0)); // is room safe?
        System.out.println("query: " + queryAtom1.toString());
        boolean expectedOutput1 = true;
        boolean actualOutput1 = kb.query(queryAtom1);   
        System.out.format("query result: %b%n%n", actualOutput1);
        assertEquals(expectedOutput1, actualOutput1);
    }
    
    /**
     * If we can't ensure that an adjacent room is not smelly or windy, room is not safe
     */
    @Test
    public void test_KB_axioms_not_safe()
    {
        ActualWorld world = new ActualWorld(3);
        KnowledgeBase kb = new KnowledgeBase();
        
        // smell from adjacent cell
        KBAtom safePercept0 = new KBAtomConstant(false, "EXPLORED", ActualWorld.getRoom(1, 0)); 
        KBAtom safePercept1 = new KBAtomConstant(true,  "SMELLY",   ActualWorld.getRoom(1, 0)); 
        KBAtom safePercept2 = new KBAtomConstant(false, "WINDY",    ActualWorld.getRoom(1, 0)); 
        
        kb.update(safePercept0);
        kb.update(safePercept1);
        kb.update(safePercept2);
        
        KBAtomConstant queryAtom1 = new KBAtomConstant(false, "SAFE", ActualWorld.getRoom(1, 1)); // is room safe?
        System.out.println("query: " + queryAtom1.toString());
        boolean expectedOutput1 = false;
        boolean actualOutput1 = kb.query(queryAtom1);   
        System.out.format("query result: %b%n%n", actualOutput1);
        assertEquals(expectedOutput1, actualOutput1);
        
         KBAtomConstant queryAtom2 = new KBAtomConstant(false, "SAFE", ActualWorld.getRoom(2, 2)); // is room safe?
        System.out.println("query: " + queryAtom2.toString());
        boolean expectedOutput2 = false;
        boolean actualOutput2 = kb.query(queryAtom2);   
        System.out.format("query result: %b%n%n", actualOutput2);
        assertEquals(expectedOutput2, actualOutput2);
    }
    
    @Test
    public void test_KB_axioms_obst()
    {
        ActualWorld world = new ActualWorld(3);
        KnowledgeBase kb = new KnowledgeBase();
        
        KBAtom obstPercept1 = new KBAtomConstant(false, "OBST", ActualWorld.getRoom(0, 0)); // has obstacle
        KBAtom obstPercept2 = new KBAtomConstant(true, "OBST", ActualWorld.getRoom(0, 1));  // does not have obstacle
        
        kb.update(obstPercept1);
        kb.update(obstPercept2);
        
        KBAtomConstant queryAtom1 = new KBAtomConstant(false, "BLOCKED", ActualWorld.getRoom(0, 0)); // is room (x,y) blocked given the current KB?
        System.out.println("query: " + queryAtom1.toString());
        boolean expectedOutput1 = true;
        boolean actualOutput1 = kb.query(queryAtom1);   
        System.out.format("query result: %b%n%n", actualOutput1);
        assertEquals(expectedOutput1, actualOutput1);
        
        KBAtomConstant queryAtom2 = new KBAtomConstant(false, "BLOCKED", ActualWorld.getRoom(0, 1)); //  // is room (x,y) blocked given the current KB?
        System.out.println("query: " + queryAtom2.toString());
        boolean expectedOutput2 = false;
        boolean actualOutput2 = kb.query(queryAtom2);   
        System.out.format("query result: %b%n%n", actualOutput2);
        assertEquals(expectedOutput2, actualOutput2);
    }
    
    @Test
    public void test_KB_axioms_shiny()
    {
        ActualWorld world = new ActualWorld(3);
        KnowledgeBase kb = new KnowledgeBase();
        
        KBAtom shinyPercept1 = new KBAtomConstant(false, "SHINY", ActualWorld.getRoom(0, 0)); // is shiny
        KBAtom shinyPercept2 = new KBAtomConstant(true, "SHINY", ActualWorld.getRoom(0, 1));  // is not shiny
        
        kb.update(shinyPercept1);
        kb.update(shinyPercept2);
        
        KBAtomConstant queryAtom1 = new KBAtomConstant(false, "HASGOLD", ActualWorld.getRoom(0, 0)); // does room (x,y) have gold given the current KB?
        System.out.println("query: " + queryAtom1.toString());
        boolean expectedOutput1 = true;
        boolean actualOutput1 = kb.query(queryAtom1);   
        System.out.format("query result: %b%n%n", actualOutput1);
        assertEquals(expectedOutput1, actualOutput1);
        
        KBAtomConstant queryAtom2 = new KBAtomConstant(false, "HASGOLD", ActualWorld.getRoom(0, 1)); // does room (x,y) have gold given the current KB?
        System.out.println("query: " + queryAtom2.toString());
        boolean expectedOutput2 = false;
        boolean actualOutput2 = kb.query(queryAtom2);   
        System.out.format("query result: %b%n%n", actualOutput2);
        assertEquals(expectedOutput2, actualOutput2);
    }
    
    @Test
    public void test_KB_axioms_resolveWumpus_centered()
    {
        ActualWorld world = new ActualWorld(3);
        KnowledgeBase kb = new KnowledgeBase();
        
        KBAtom smellyPercept1 = new KBAtomConstant(false, "SMELLY", ActualWorld.getRoom(0, 1)); 
        KBAtom smellyPercept2 = new KBAtomConstant(false, "SMELLY", ActualWorld.getRoom(1, 2)); 
        KBAtom smellyPercept3 = new KBAtomConstant(false, "SMELLY", ActualWorld.getRoom(2, 1)); 
        KBAtom smellyPercept4 = new KBAtomConstant(false, "SMELLY", ActualWorld.getRoom(1, 0)); 
        
        kb.update(smellyPercept1);
        kb.update(smellyPercept2);
        kb.update(smellyPercept3);
        kb.update(smellyPercept4);
        
        KBAtomConstant queryAtom1 = new KBAtomConstant(false, "WUMPUS", ActualWorld.getRoom(1, 1)); // is there a wumpus in room (1,1)?
        System.out.println("query: " + queryAtom1.toString());
        boolean expectedOutput1 = true;
        boolean actualOutput1 = kb.query(queryAtom1);   
        System.out.format("query result: %b%n%n", actualOutput1);
        assertEquals(expectedOutput1, actualOutput1);
    }
    
    @Test
    public void test_KB_axioms_resolveWumpus_leftWall()
    {
        ActualWorld world = new ActualWorld(3);
        KnowledgeBase kb = new KnowledgeBase();
        
        KBAtom smellyPercept1 = new KBAtomConstant(false, "SMELLY", ActualWorld.getRoom(0, 0)); 
        KBAtom smellyPercept2 = new KBAtomConstant(false, "SMELLY", ActualWorld.getRoom(1, 1)); 
        KBAtom smellyPercept3 = new KBAtomConstant(false, "SMELLY", ActualWorld.getRoom(0, 2)); 
        
        kb.update(smellyPercept1);
        kb.update(smellyPercept2);
        kb.update(smellyPercept3);
        
        KBAtomConstant queryAtom1 = new KBAtomConstant(false, "WUMPUS", ActualWorld.getRoom(0, 1)); // is there a wumpus in room (0, 1)?
        System.out.println("query: " + queryAtom1.toString());
        boolean expectedOutput1 = true;
        boolean actualOutput1 = kb.query(queryAtom1);   
        System.out.format("query result: %b%n%n", actualOutput1);
        assertEquals(expectedOutput1, actualOutput1);
    }
    
    @Test
    public void test_KB_axioms_resolveWumpus_topLeftCorner()
    {
        ActualWorld world = new ActualWorld(3);
        KnowledgeBase kb = new KnowledgeBase();
        
        KBAtom smellyPercept1 = new KBAtomConstant(false, "SMELLY", ActualWorld.getRoom(0, 1)); 
        KBAtom smellyPercept2 = new KBAtomConstant(false, "SMELLY", ActualWorld.getRoom(1, 2)); 
        
        kb.update(smellyPercept1);
        kb.update(smellyPercept2);
        
        KBAtomConstant queryAtom1 = new KBAtomConstant(false, "WUMPUS", ActualWorld.getRoom(0, 2)); 
        System.out.println("query: " + queryAtom1.toString());
        boolean expectedOutput1 = true;
        boolean actualOutput1 = kb.query(queryAtom1);   
        System.out.format("query result: %b%n%n", actualOutput1);
        assertEquals(expectedOutput1, actualOutput1);
    }
    
    @Test
    public void test_KB_axioms_resolveWumpus_topWall()
    {
        ActualWorld world = new ActualWorld(3);
        KnowledgeBase kb = new KnowledgeBase();
        
        KBAtom smellyPercept1 = new KBAtomConstant(false, "SMELLY", ActualWorld.getRoom(0, 2)); 
        KBAtom smellyPercept2 = new KBAtomConstant(false, "SMELLY", ActualWorld.getRoom(1, 1)); 
        KBAtom smellyPercept3 = new KBAtomConstant(false, "SMELLY", ActualWorld.getRoom(2, 2)); 
        
        kb.update(smellyPercept1);
        kb.update(smellyPercept2);
        kb.update(smellyPercept3);
        
        KBAtomConstant queryAtom1 = new KBAtomConstant(false, "WUMPUS", ActualWorld.getRoom(1, 2)); 
        System.out.println("query: " + queryAtom1.toString());
        boolean expectedOutput1 = true;
        boolean actualOutput1 = kb.query(queryAtom1);   
        System.out.format("query result: %b%n%n", actualOutput1);
        assertEquals(expectedOutput1, actualOutput1);
    }
    
    @Test
    public void test_KB_axioms_resolveWumpus_topRightCorner()
    {
        ActualWorld world = new ActualWorld(3);
        KnowledgeBase kb = new KnowledgeBase();
        
        KBAtom smellyPercept1 = new KBAtomConstant(false, "SMELLY", ActualWorld.getRoom(1, 2)); 
        KBAtom smellyPercept2 = new KBAtomConstant(false, "SMELLY", ActualWorld.getRoom(2, 1)); 
        
        kb.update(smellyPercept1);
        kb.update(smellyPercept2);
        
        KBAtomConstant queryAtom1 = new KBAtomConstant(false, "WUMPUS", ActualWorld.getRoom(2, 2)); 
        System.out.println("query: " + queryAtom1.toString());
        boolean expectedOutput1 = true;
        boolean actualOutput1 = kb.query(queryAtom1);   
        System.out.format("query result: %b%n%n", actualOutput1);
        assertEquals(expectedOutput1, actualOutput1);
    }
    
    @Test
    public void test_KB_axioms_resolveWumpus_rightWall()
    {
        ActualWorld world = new ActualWorld(3);
        KnowledgeBase kb = new KnowledgeBase();
        
        KBAtom smellyPercept1 = new KBAtomConstant(false, "SMELLY", ActualWorld.getRoom(2, 2)); 
        KBAtom smellyPercept2 = new KBAtomConstant(false, "SMELLY", ActualWorld.getRoom(1, 1)); 
        KBAtom smellyPercept3 = new KBAtomConstant(false, "SMELLY", ActualWorld.getRoom(2, 0)); 
        
        kb.update(smellyPercept1);
        kb.update(smellyPercept2);
        kb.update(smellyPercept3);
        
        KBAtomConstant queryAtom1 = new KBAtomConstant(false, "WUMPUS", ActualWorld.getRoom(2, 1)); 
        System.out.println("query: " + queryAtom1.toString());
        boolean expectedOutput1 = true;
        boolean actualOutput1 = kb.query(queryAtom1);   
        System.out.format("query result: %b%n%n", actualOutput1);
        assertEquals(expectedOutput1, actualOutput1);
    }
    
    @Test
    public void test_KB_axioms_resolveWumpus_bottomRightCorner()
    {
        ActualWorld world = new ActualWorld(3);
        KnowledgeBase kb = new KnowledgeBase();
        
        KBAtom smellyPercept1 = new KBAtomConstant(false, "SMELLY", ActualWorld.getRoom(2, 1)); 
        KBAtom smellyPercept2 = new KBAtomConstant(false, "SMELLY", ActualWorld.getRoom(1, 0)); 
        
        kb.update(smellyPercept1);
        kb.update(smellyPercept2);
        
        KBAtomConstant queryAtom1 = new KBAtomConstant(false, "WUMPUS", ActualWorld.getRoom(2, 0)); 
        System.out.println("query: " + queryAtom1.toString());
        boolean expectedOutput1 = true;
        boolean actualOutput1 = kb.query(queryAtom1);   
        System.out.format("query result: %b%n%n", actualOutput1);
        assertEquals(expectedOutput1, actualOutput1);
    }
    
    @Test
    public void test_KB_axioms_resolveWumpus_bottomWall()
    {
        ActualWorld world = new ActualWorld(3);
        KnowledgeBase kb = new KnowledgeBase();
        
        KBAtom smellyPercept1 = new KBAtomConstant(false, "SMELLY", ActualWorld.getRoom(0, 0)); 
        KBAtom smellyPercept2 = new KBAtomConstant(false, "SMELLY", ActualWorld.getRoom(1, 1)); 
        KBAtom smellyPercept3 = new KBAtomConstant(false, "SMELLY", ActualWorld.getRoom(2, 0)); 
        
        kb.update(smellyPercept1);
        kb.update(smellyPercept2);
        kb.update(smellyPercept3);
        
        KBAtomConstant queryAtom1 = new KBAtomConstant(false, "WUMPUS", ActualWorld.getRoom(1, 0)); 
        System.out.println("query: " + queryAtom1.toString());
        boolean expectedOutput1 = true;
        boolean actualOutput1 = kb.query(queryAtom1);   
        System.out.format("query result: %b%n%n", actualOutput1);
        assertEquals(expectedOutput1, actualOutput1);
    }
    
    @Test
    public void test_KB_axioms_resolveWumpus_bottomLeftCorner()
    {
        ActualWorld world = new ActualWorld(3);
        KnowledgeBase kb = new KnowledgeBase();
        
        KBAtom smellyPercept1 = new KBAtomConstant(false, "SMELLY", ActualWorld.getRoom(0, 1)); 
        KBAtom smellyPercept2 = new KBAtomConstant(false, "SMELLY", ActualWorld.getRoom(1, 0)); 
        
        kb.update(smellyPercept1);
        kb.update(smellyPercept2);
        
        KBAtomConstant queryAtom1 = new KBAtomConstant(false, "WUMPUS", ActualWorld.getRoom(0, 0)); 
        System.out.println("query: " + queryAtom1.toString());
        boolean expectedOutput1 = true;
        boolean actualOutput1 = kb.query(queryAtom1);   
        System.out.format("query result: %b%n%n", actualOutput1);
        assertEquals(expectedOutput1, actualOutput1);
    }
    
    @Test
    public void test_KB_axioms_resolveWumpus_noWumpus()
    {
        ActualWorld world = new ActualWorld(3);
        KnowledgeBase kb = new KnowledgeBase();
        
        KBAtom smellyPercept1 = new KBAtomConstant(true, "SMELLY", ActualWorld.getRoom(0, 1)); 
        
        kb.update(smellyPercept1);
        
        // wumpus in (1,1) if we know (0,1) isn't smelly?
        KBAtomConstant queryAtom1 = new KBAtomConstant(false, "WUMPUS", ActualWorld.getRoom(1, 1)); 
        System.out.println("query: " + queryAtom1.toString());
        boolean expectedOutput1 = false;
        boolean actualOutput1 = kb.query(queryAtom1);   
        System.out.format("query result: %b%n%n", actualOutput1);
        assertEquals(expectedOutput1, actualOutput1);
        
        // wumpus in (2,2) if we don't know anything about adj squares? This should return no wumpus (need to check for safety instead)
        KBAtomConstant queryAtom2 = new KBAtomConstant(false, "WUMPUS", ActualWorld.getRoom(2, 2)); 
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
