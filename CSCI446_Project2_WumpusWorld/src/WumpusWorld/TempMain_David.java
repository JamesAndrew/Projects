package WumpusWorld;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

public class TempMain_David 
{
    
    public static void main(String[] args) throws UnsupportedEncodingException, FileNotFoundException, IOException
    { 
        int worldSize = 25;
        // run the world 20 times
        for (int i = 0; i < 20; i++)
        {
            World aWorld = new World(worldSize);
            World.printWorld();

            // set agent and begin routine
            KBAgent agent = new KBAgent();
            agent.findGold();

            // update stats
            AgentStatistics.addNewRun(agent.getStatsMap());
            
            
        }
        String fileOutputName = "Run_Results_GraphSize_" + worldSize + "x" + worldSize + ".txt";
        AgentStatistics statistics = new AgentStatistics();

        try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileOutputName), "utf-8")))
        {
            writer.write(statistics.generateResultsOutput());
        }
    }
}
