package WumpusWorld;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import sun.security.krb5.internal.ktab.KeyTabConstants;

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
