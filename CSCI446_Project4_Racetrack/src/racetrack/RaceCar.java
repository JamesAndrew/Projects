package racetrack;

import java.util.Random;

/**
 *
 *
 */
public class RaceCar
{

    private int velocityX = 0;
    private int velocityY = 0;
    private int accelerationX = 0;
    private int accelerationY = 0;
    private Cell location;

    public RaceCar(Cell start)
    {
        location = start; 
    }

    /*
     * Apply x and y acceleration to racecar
     */
    public void accelerate(int xAcc, int yAcc)
    {
        Random rand = new Random();
        int accProb = rand.nextInt(10);
        //reject if acceleration not within -1 to 1
        if (xAcc <= 1 && xAcc >= -1 && yAcc <= 1 && yAcc >= -1)
        {
            //check if acceleration is to be applied
            if (accProb > 1)
            {
                //increment velocity if not going to  exceed max
                if (velocityX + xAcc <= 5 && velocityX + xAcc >= -5)
                {
                    velocityX += xAcc;
                }
                if (velocityY + yAcc <= 5 && velocityY + yAcc >= -5)
                {
                    velocityY += yAcc;
                }
            }
        }
    }

    public void reset(Cell start)
    {
        velocityX = 0;
        velocityY = 0;
        accelerationX = 0;
        accelerationY = 0;
        location = start; 
    }
    
    public void move()
    {
        
    }
    
    public Cell getLocation()
    {
        return location; 
    }
}
