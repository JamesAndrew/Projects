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
public class WorldTest {
    
    public WorldTest() {
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
    public void testGetRoom() {
        System.out.println("getRoom");
        int row = 0;
        int col = 0;
        Room expResult = null;
        Room result = World.getRoom(row, col);
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetRooms() {
        System.out.println("getRooms");
        Room[][] expResult = null;
        Room[][] result = World.getRooms();
        assertArrayEquals(expResult, result);
        fail("The test case is a prototype.");
    }
    
}
