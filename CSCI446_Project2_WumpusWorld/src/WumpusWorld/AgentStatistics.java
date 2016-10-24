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
import java.util.Map;

/**
 *
 * @author James
 */
public class AgentStatistics 
{
    // tracks the run number
    private static Integer runNum = 1;
    // First Key: run #
    // Second Key: Category ("numDecisionsMade", "goldFound", "wumpiKilled", "pitFalls", "wumpusDeaths", "cellsExplored", "score"
    // Second Value: Integer values of their respective category
    private static HashMap<Integer, HashMap<String, Integer>> reactiveAgentStats = new HashMap<>();
    
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
    
    // adds the HashMap<String, Integer> from most recent run to reactiveAgentStats
    public static void addNewRun(HashMap<String, Integer> input)
    {
        reactiveAgentStats.put(runNum, input);
        runNum++;
    }
    
    public void addReactiveStats(int s, int p)
    {
        ReactiveAgentStats.put(s, p);
    }
    
    public String generateResultsOutput()
    {
        StringBuilder sb = new StringBuilder();
        
//        // example of what gets passed in
//        HashMap<String, Integer> test1 = new HashMap<>();
//        test1.put("numDecisionsMade", 1);
//        test1.put("goldFound",        1);
//        test1.put("wumpiKilled",      1);
//        test1.put("pitFalls",         1);
//        test1.put("wumpusDeaths",     1);
//        test1.put("cellsExplored",    1000);
//        test1.put("score",    1000);
//        
//        HashMap<String, Integer> test2 = new HashMap<>();
//        test2.put("numDecisionsMade", 2);
//        test2.put("goldFound",        2);
//        test2.put("wumpiKilled",      2);
//        test2.put("pitFalls",         2);
//        test2.put("wumpusDeaths",     2);
//        test2.put("cellsExplored",    2000);
//        test2.put("score",    2000);
//        
//        reactiveAgentStats.put(1, test1);   
//        reactiveAgentStats.put(2, test2);   
        
        int i = 1;
        for (HashMap<String, Integer> value : reactiveAgentStats.values())
        {
            HashMap<String, Integer> currentMap = value;
            System.out.format("%s: %d%n %s: %d%n %s: %d%n %s: %d%n %s: %d%n %s: %d%n %s: %d%n %s: %d%n", 
                "Run #: ", i,
                "Num Decisions Made: ", currentMap.get("numDecisionsMade"),
                "Gold Found?: ", currentMap.get("goldFound"),
                "Wumpi Killed: ", currentMap.get("wumpiKilled"),
                "Pit falls: ", currentMap.get("pitFalls"),
                "Deaths to wumpus: ", currentMap.get("wumpusDeaths"),
                "Num cells explored: ", currentMap.get("cellsExplored"),
                "Total score: ", currentMap.get("score"));
            
            sb.append(String.format("%d, %d, %d, %d, %d, %d, %d, %d%n", 
                i,
                currentMap.get("numDecisionsMade"),
                currentMap.get("goldFound"),
                currentMap.get("wumpiKilled"),
                currentMap.get("pitFalls"),
                currentMap.get("wumpusDeaths"),
                currentMap.get("cellsExplored"),             
                currentMap.get("score")               
                ));
            i++;
        }
        
        return sb.toString();
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
