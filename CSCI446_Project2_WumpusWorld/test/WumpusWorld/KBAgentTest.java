/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package WumpusWorld;

import java.util.HashMap;
import java.util.Map;
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
public class KBAgentTest {
    
    public KBAgentTest() { }
    @Test
    public void testFindGold() 
    {
        Map<Integer, Room> frontier = new HashMap<>();
        frontier.put(10, new Room(false));
        frontier.put(01, new Room(false));
        
        System.out.println("frontier: " + frontier.get(10));
        frontier.remove(10);
        System.out.println("frontier: " + frontier.get(10));
        
        
        assertEquals(1, 1);
    }
    
}
