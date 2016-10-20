/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package WumpusWorld;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 *
 * @author James
 */
public class AgentStatistics 
{
    public static Map<Integer, Integer> KBAgentStats = new HashMap<>();
    public static Map<Integer, Integer> ReactiveAgentStats = new HashMap<>();
    public static PrintWriter results;
    
    public AgentStatistics()
    {
        
        try
        {
            results = new PrintWriter(new FileWriter(new File("Results")));
        } catch (IOException ex)
        {
            
        }
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
        results.println("WorldSize     Score");
        
        // iterate map using entryset in for loop
        ReactiveAgentStats.entrySet().stream().forEach((entry) -> {
            //print keys and values
            results.println(entry.getKey() + "     " +entry.getValue());
        });
        
        results.close();
    }
}
