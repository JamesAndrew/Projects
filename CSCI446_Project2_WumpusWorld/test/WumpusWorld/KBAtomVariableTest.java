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
public class KBAtomVariableTest {
    
    public KBAtomVariableTest() {
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
    public void testEvaluate() {
        System.out.println("evaluate");
        KBAtomConstant context = null;
        KBAtomVariable instance = null;
        boolean expResult = false;
        boolean result = instance.evaluate(context);
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testConvertToConstant() {
        System.out.println("convertToConstant");
        KBAtomConstant query = null;
        KBAtomVariable instance = null;
        KBAtomConstant expResult = null;
        KBAtomConstant result = instance.convertToConstant(query);
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testToString() {
        System.out.println("toString");
        KBAtomVariable instance = null;
        String expResult = "";
        String result = instance.toString();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testEquals() {
        System.out.println("equals");
        Object obj = null;
        KBAtomVariable instance = null;
        boolean expResult = false;
        boolean result = instance.equals(obj);
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }
    
}
