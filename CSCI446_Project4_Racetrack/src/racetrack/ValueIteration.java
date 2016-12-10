
package racetrack;

/**
 * 
 */
public class ValueIteration 
{
    // tunable parameters
    private final double gamma   = 0.5;                 // discount factor
    private final double epsilon = 0.000001;            // halting condition
    
    // track currently being worked with 
    private Racetrack track;
    
    
    public ValueIteration(Racetrack in_track)
    {
        track = in_track;
    }
    
    /**
     * Runs the value iteration algorithm on page 653 of Russell & Norvig
     */
    public void trainUtilities()
    {
        int tempI = 0;             // temp
        
        // loop until delta is less than than threshold value:
        // delta < episilon(1-gamma)/gamma
        do
        {
            // loop over all possible states //
            // for each track cell...
            for (int row = 0; row < track.getTrack().length; row++)
            {
                for (int col = 0; col < track.getTrack()[row].length; col++)
                {
                    // if the track is not a wall...
                    if (track.getTrack()[row][col].getType() != '#')
                    {
                        Cell currentCell = track.getTrack()[row][col];
                        
                        // for each posssible velocity state at the cell
                        for (int xVel = 0; xVel < currentCell.getUtilities().length; xVel++)
                        {
                            for (int yVel = 0 ; yVel < currentCell.getUtilities()[xVel].length; yVel++)
                            {
                                // the utility of this cell at this velocity is calculated using the bellman equation
                                currentCell.getUtilities()[xVel][yVel] = bellmanEquation(currentCell, xVel, yVel);
                            }
                        }
                    }
                }
            }
            tempI++;        // temp used for testing
        } 
        while (tempI < 1);
    }
    
    /**
     * Calculates a cell's next utility value for a given velocity 
     *     U_{i+1}(s) <- R(s) + gamma*MaxAction[sum_{s'} P(s'|s,a)*U(s')]
     * This equation is from the Russell and Norvig book on page 653
     * 
     * @param cell : the cell to calculate next utility for
     * @param xVel : the cell's x-velocity state, a number between [-5,5]
     * @param yVel : the cell's y-velocity state, a number between [-5,5]
     * @return U_{i+1}(s) : the updated utility for the cell at state [xVel,yVel]
     */
    private double bellmanEquation(Cell cell, int xVel, int yVel)
    {
        double R = cell.getReward();
        // holds the values of the summation over s' for each action that can be taken from this state
        
        
        return 0.0;
    }
}
