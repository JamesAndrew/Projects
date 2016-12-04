/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package racetrack;

/**
 *
 * @author James
 */
public class Racecar 
{
    private int timestep;
    private int allowedSteps;
    private int x;
    private int y;
    private int v_x;
    private int v_y;
    private int a_x;
    private int a_y;
    
    public Racecar(int aSteps)
    {
        allowedSteps = aSteps;
        updatePhysics();
    }
    
    public void updatePhysics()
    {
        printState();
        while(timestep < allowedSteps)
        {
            updateTimestep();
            updatePosition();
            updateVelocity();
            updateAcceleration(1, 1);
            printState();
        }
    }
    
    public void updateTimestep()
    {
        timestep++;
    }
    
    public void updatePosition()
    {
        x = x + v_x;
        y = y + v_y;
    }
    
    public void updateVelocity()
    {
        v_x = v_x + a_x;
        v_y = v_y + a_y;
        
        if(v_x < -5)
            v_x = -5;
        if(v_x > 5)
            v_x = 5;
        
        if(v_y < -5)
            v_y = -5;
        if(v_y > 5)
            v_y = 5;
    }
    
    public void updateAcceleration(int x_comp, int y_comp)
    {
        if(x_comp == -1 || x_comp == 0 || x_comp == 1)
            a_x = x_comp;
        
        if(y_comp == -1 || y_comp == 0 || y_comp == 1)
            a_y = y_comp;
    }
    
    public void printState()
    {
        System.out.println("== Agent State ==");
        System.out.println("Timestep " + timestep);
        System.out.println("Position (x, y) = (" + x + ", " + y + ")");
        System.out.println("Velocity (v_x, v_y) = (" + v_x + ", " + v_y + ")");
        System.out.println("Velocity (a_x, a_y) = (" + a_x + ", " + a_y + ")");
    }
}
