/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package WumpusWorld;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author James
 */
public class AgentStatistics 
{
    public static Map<Integer, Integer> KBAgentStats = new HashMap<>();
    public static Map<Integer, Integer> ReactiveAgentStats = new HashMap<>();
    
    public AgentStatistics()
    {
        
    }
    
    public void addKBStats(int s, int p)
    {
        KBAgentStats.put(s, p);
    }
    
    public void addReactiveStats(int s, int p)
    {
        ReactiveAgentStats.put(s, p);
    }
    
    public void printResults()
    {
        
    }
}
