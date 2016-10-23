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
public class KBOperatorTest {
    
    public KBOperatorTest() {
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
    public void testValues() {
        System.out.println("values");
        KBOperator[] expResult = null;
        KBOperator[] result = KBOperator.values();
        assertArrayEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testValueOf() {
        System.out.println("valueOf");
        String name = "";
        KBOperator expResult = null;
        KBOperator result = KBOperator.valueOf(name);
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }
    
}
