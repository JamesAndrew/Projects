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
public class RoomTest {
    
    public RoomTest() {
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
    public void testGetRoomRow() {
        System.out.println("getRoomRow");
        Room instance = null;
        int expResult = 0;
        int result = instance.getRoomRow();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetRoomColumn() {
        System.out.println("getRoomColumn");
        Room instance = null;
        int expResult = 0;
        int result = instance.getRoomColumn();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testSetIsEmpty() {
        System.out.println("setIsEmpty");
        boolean e = false;
        Room instance = null;
        instance.setIsEmpty(e);
        fail("The test case is a prototype.");
    }

    @Test
    public void testIsEmpty() {
        System.out.println("isEmpty");
        Room instance = null;
        boolean expResult = false;
        boolean result = instance.isEmpty();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testSetIsBlocked() {
        System.out.println("setIsBlocked");
        boolean b = false;
        Room instance = null;
        instance.setIsBlocked(b);
        fail("The test case is a prototype.");
    }

    @Test
    public void testIsBlocked() {
        System.out.println("isBlocked");
        Room instance = null;
        boolean expResult = false;
        boolean result = instance.isBlocked();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testSetIsBreezy() {
        System.out.println("setIsBreezy");
        boolean b = false;
        Room instance = null;
        instance.setIsBreezy(b);
        fail("The test case is a prototype.");
    }

    @Test
    public void testIsBreezy() {
        System.out.println("isBreezy");
        Room instance = null;
        boolean expResult = false;
        boolean result = instance.isBreezy();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testSetIsPit() {
        System.out.println("setIsPit");
        boolean p = false;
        Room instance = null;
        instance.setIsPit(p);
        fail("The test case is a prototype.");
    }

    @Test
    public void testIsPit() {
        System.out.println("isPit");
        Room instance = null;
        boolean expResult = false;
        boolean result = instance.isPit();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testSetIsShiny() {
        System.out.println("setIsShiny");
        boolean s = false;
        Room instance = null;
        instance.setIsShiny(s);
        fail("The test case is a prototype.");
    }

    @Test
    public void testIsShiny() {
        System.out.println("isShiny");
        Room instance = null;
        boolean expResult = false;
        boolean result = instance.isShiny();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testSetIsSmelly() {
        System.out.println("setIsSmelly");
        boolean s = false;
        Room instance = null;
        instance.setIsSmelly(s);
        fail("The test case is a prototype.");
    }

    @Test
    public void testIsSmelly() {
        System.out.println("isSmelly");
        Room instance = null;
        boolean expResult = false;
        boolean result = instance.isSmelly();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testSetIsWumpus() {
        System.out.println("setIsWumpus");
        boolean w = false;
        Room instance = null;
        instance.setIsWumpus(w);
        fail("The test case is a prototype.");
    }

    @Test
    public void testIsWumpus() {
        System.out.println("isWumpus");
        Room instance = null;
        boolean expResult = false;
        boolean result = instance.isWumpus();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testIsHasGold() {
        System.out.println("isHasGold");
        Room instance = null;
        boolean expResult = false;
        boolean result = instance.isHasGold();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testSetAllProperties() {
        System.out.println("setAllProperties");
        boolean blocked = false;
        boolean empty = false;
        boolean pit = false;
        boolean breezy = false;
        boolean shiny = false;
        boolean smelly = false;
        boolean wumpus = false;
        Room instance = null;
        instance.setAllProperties(blocked, empty, pit, breezy, shiny, smelly, wumpus);
        fail("The test case is a prototype.");
    }
    
}
