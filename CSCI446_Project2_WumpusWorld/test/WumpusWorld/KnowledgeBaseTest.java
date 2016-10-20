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
    public void testQuery() {
        System.out.println("query");
        KBAtomConstant question = null;
        KnowledgeBase instance = new KnowledgeBase();
        boolean expResult = false;
        boolean result = instance.query(question);
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testUpdate() {
        System.out.println("update");
        KBAtom input = null;
        KnowledgeBase instance = new KnowledgeBase();
        instance.update(input);
        fail("The test case is a prototype.");
    }
    
}
