package racetrack;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Driver_Rice
{
    public static void main(String[] args)
    {
        try
        {
            Experiment e = new Experiment();
        } catch (IOException ex)
        {
            Logger.getLogger(Driver.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
