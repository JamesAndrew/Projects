/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package WumpusWorld;

import Exceptions.PendingException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 *
 * @author James
 */
public class AgentStatistics 
{
    // First Key: run #
    // Second Key: Category ("numDecisionsMade", "goldFound", "wumpiKilled", "pitFalls", "wumpusDeaths", "cellsExplored"
    // Second Value: Integer values of their respective category
    public HashMap<Integer, HashMap<String, Integer>> ReactiveAgentStats = new HashMap<>();
    public HashMap<String, Integer> innerMap = new HashMap<>();
    
    public static Map<Integer, Integer> ReactiveAgentStatsTemp = new HashMap<>();
    public static PrintWriter results;
    
    public AgentStatistics()
    {
                
    }
    
    public void addKBStats(int s, int p)
    {
        
    }
    
    public void addReactiveStats(int s, int p)
    {
        ReactiveAgentStatsTemp.put(s, p);
    }
    
    public String generateResultsOutput()
    {
        StringBuilder sb = new StringBuilder();
        
        // testing
        HashMap<String, Integer> test1 = new HashMap<>();
        test1.put("numDecisionsMade", 1);
        test1.put("goldFound",        1);
        test1.put("wumpiKilled",      1);
        test1.put("pitFalls",         1);
        test1.put("wumpusDeaths",     1);
        test1.put("cellsExplored",    1000);
        
        HashMap<String, Integer> test2 = new HashMap<>();
        test2.put("numDecisionsMade", 2);
        test2.put("goldFound",        2);
        test2.put("wumpiKilled",      2);
        test2.put("pitFalls",         2);
        test2.put("wumpusDeaths",     2);
        test2.put("cellsExplored",    2000);
        
        HashMap<String, Integer> test3 = new HashMap<>();
        test3.put("numDecisionsMade", 3);
        test3.put("goldFound",        3);
        test3.put("wumpiKilled",      3);
        test3.put("pitFalls",         3);
        test3.put("wumpusDeaths",     3);
        test3.put("cellsExplored",    3000);
        
        HashMap<String, Integer> test4 = new HashMap<>();
        test4.put("numDecisionsMade", 4);
        test4.put("goldFound",        4);
        test4.put("wumpiKilled",      4);
        test4.put("pitFalls",         4);
        test4.put("wumpusDeaths",     4);
        test4.put("cellsExplored",    4000);
        
        ReactiveAgentStats.put(1, test1);   
        ReactiveAgentStats.put(2, test2);   
        ReactiveAgentStats.put(3, test3);   
        ReactiveAgentStats.put(4, test4
        );   
        
        int i = 1;
        for (HashMap<String, Integer> value : ReactiveAgentStats.values())
        {
            System.out.format("Current value: %d%n", value.get("numDecisionsMade"));
            
            HashMap<String, Integer> currentMap = value;
            System.out.format("%d, %d, %d, %d, %d, %d, %d%n", 
                i,
                currentMap.get("numDecisionsMade"),
                currentMap.get("goldFound"),
                currentMap.get("wumpiKilled"),
                currentMap.get("pitFalls"),
                currentMap.get("wumpusDeaths"),
                currentMap.get("cellsExplored"));
            
            sb.append(String.format("%d, %d, %d, %d, %d, %d, %d%n", 
                i,
                currentMap.get("numDecisionsMade"),
                currentMap.get("goldFound"),
                currentMap.get("wumpiKilled"),
                currentMap.get("pitFalls"),
                currentMap.get("wumpusDeaths"),
                currentMap.get("cellsExplored")               
                ));
        }
        
        return sb.toString();
    }
    
    public void printResults()
    {
        
    }
}