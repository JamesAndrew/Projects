/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
public class KBAtomConstantTest {
    
    public KBAtomConstantTest() {
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
    public void testEqualsRetrunsTrue() 
    {
        Room room = new Room(2,2);
        KBAtomConstant queryAtom1 = new KBAtomConstant(false, "HASGOLD", room);
        KBAtomConstant queryAtom2 = new KBAtomConstant(false, "HASGOLD", room);
        
        boolean expectedResult = true;
        boolean actualResult = queryAtom1.equals(queryAtom2);
        assertEquals(expectedResult, actualResult);
    }
    
    @Test
    public void testEqualsRetrunsFalse() 
    {
        Room room1 = new Room(2,2);
        Room room2 = new Room(2,3);
        
        KBAtomConstant query1 = new KBAtomConstant(false, "HASGOLD", room1);
        KBAtomConstant query2 = new KBAtomConstant(false, "HASGOLD", room2);
        
        KBAtomConstant query3 = new KBAtomConstant(false, "HASGOLD", room1);
        KBAtomConstant query4 = new KBAtomConstant(true, "HASGOLD", room1);
        
        KBAtomConstant query5 = new KBAtomConstant(false, "HASGOLD", room1);
        KBAtomConstant query6 = new KBAtomConstant(false, "SMELLY", room1);
        
        boolean expectedResult = false;
        boolean actualResult1 = query1.equals(query2);
        boolean actualResult2 = query3.equals(query4);
        boolean actualResult3 = query5.equals(query6);
        
        assertEquals(expectedResult, actualResult1);
        assertEquals(expectedResult, actualResult2);
        assertEquals(expectedResult, actualResult3);
    }
    
}
