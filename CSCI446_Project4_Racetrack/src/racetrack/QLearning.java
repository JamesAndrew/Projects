
package racetrack;

public class QLearning 
{
    // tunable parameters //
    // threshold to stop training the race track
    private final double epsilon = 0.00001;    
    // discount factor - low values decrement additive rewards
    private final double gamma = 0.95;
    // learning factor - lower values take longer to converge but give better results
    private final double alpha = 0.5;
    
    // other parameters //
    // track currently being worked with 
    private final Racetrack track;
    
    /**
     * Constructor
     * @param in_track : race track to assign [state, action] values to
     */
    public QLearning(Racetrack in_track)
    {
        track = in_track;
    }
    
    /**
     * Begins the Q-Learning procedure as described in figure 21.8 on page 844
     * of Russell and Norvig.
     */
    public void learnTrack()
    {
        
    }
}
