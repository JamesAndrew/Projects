
package racetrack;

import java.util.Random;

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
        int iteration = 0;
        int[] currentState = new int[2];
        
        // repeat until delta threshold is met
        do
        {
            // pick a semi-arbitrary state
            if (iteration == 0) currentState = assignInitialState();
            else
            {
                currentState = randomValidState();
            }
        }
        while (iteration < 3);
    }
    
    /**
     * @return the [row,col] indices close to the current map's finish line
     */
    private int[] assignInitialState()
    {
        int[] rowColIndex = new int[2];
        switch (track.getName())
        {
            case "R":
                rowColIndex[0] = 24;
                rowColIndex[1] = 26;
                break;
            case "L":
                rowColIndex[0] = 3;
                rowColIndex[1] = 34;
                break;
            case "O":
                rowColIndex[0] = 14;
                rowColIndex[1] = 2;
                break;
            case "simple":
                rowColIndex[0] = 2;
                rowColIndex[1] = 8;
                break;
            default:
                throw new RuntimeException("The current track does not have a "
                        + "name that matches with the switch statement");
        }
        
        return rowColIndex;
    }
    
    /**
     * @return A random point on the racetrack that is a '.' character
     */
    private int[] randomValidState()
    {
        Random random = new Random();
        int rowIndex = 0;
        int colIndex = 0;
        
        // generate a random row and column index within the racetrack bounds
        int randRow = random.nextInt(track.getTrack().length);
        int randCol = random.nextInt(track.getTrack()[randRow].length);
        
        // find the closest '.' cell to index [randRow,randCol]
        boolean found = false;
        int i = 0;
        while (!found)
        {
            // west
            if (randCol-i >= 0 && track.getTrack()[randRow][randCol-i].getType() == '.')
            {
                found = true;
                rowIndex = randRow;
                colIndex = randCol - i;
            } 
            // north-west
            else if (randRow-i >= 0 && randCol-i >= 0 
                    && track.getTrack()[randRow-i][randCol-i].getType() == '.')
            {
                found = true;
                rowIndex = randRow - i;
                colIndex = randCol - i;
            } 
            // north
            else if (randRow-i >= 0 && track.getTrack()[randRow-i][randCol].getType() == '.')
            {
                found = true;
                rowIndex = randRow - i;
                colIndex = randCol;
            } 
            // north-east
            else if (randRow-i >= 0 && randCol+i < track.getTrack()[0].length 
                    && track.getTrack()[randRow-i][randCol+i].getType() == '.')
            {
                found = true;
                rowIndex = randRow - 1;
                colIndex = randCol + 1;
            } 
            // east
            else if (randCol+i < track.getTrack()[0].length 
                    && track.getTrack()[randRow][randCol+i].getType() == '.')
            {
                found = true;
                rowIndex = randRow;
                colIndex = randCol + i;
            } 
            // south-east
            else if (randRow+i < track.getTrack().length && randCol+i < track.getTrack()[0].length 
                    && track.getTrack()[randRow+i][randCol+i].getType() == '.')
            {
                found = true;
                rowIndex = randRow + i;
                colIndex = randCol + i;
            } 
            // south
            else if (randRow+i < track.getTrack().length 
                    && track.getTrack()[randRow+i][randCol].getType() == '.')
            {
                found = true;
                rowIndex = randRow + i;
                colIndex = randCol;
            } 
            // south-west
            else if (randRow+i < track.getTrack().length && randCol-i >= 0 
                    && track.getTrack()[randRow+i][randCol-i].getType() == '.')
            {
                found = true;
                rowIndex = randRow + i;
                colIndex = randCol - i;
            } 
            else i++;
        }
        
        if (track.getTrack()[rowIndex][colIndex].getType() != '.')
            throw new RuntimeException("randomValidState()row and column indices"
                    + " didn't assign to a cell with a '.' character.");
        else
        {
            int[] returnedIndex = new int[]{ rowIndex, colIndex };
            return returnedIndex;
        }
    }
}
