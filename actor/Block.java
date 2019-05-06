/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package actor;

import java.awt.Color;

/**
 *
 * @author spockm
 */
public class Block extends GameObject
{
    public static final int TEMPORARY_DURATION = 10;
    public static final int PERMANENT_DURATION = 1000;
    private int duration; //How many turns the Block will stay on the screen for.
    
    public Block()
    {
        duration = PERMANENT_DURATION;
        setColor(Color.BLACK);
    }
    public Block(int dur)
    {
        duration = dur;
        setColor(Color.BLACK);
    }
    public Block(Block b)
    {
        duration = b.getDuration();
        setLocation(b.getLocation());
        setColor(Color.BLACK);
    }
    
    public int getDuration()
    {
        return duration;
    }
    public void setDuration(int dur)
    {
        duration = dur;
    }
    public void act()
    {
        duration--;
        if(duration < TEMPORARY_DURATION)
            this.setColor(Color.DARK_GRAY);
        if(duration == 0)
            this.removeSelfFromGrid();
    }
    
    
    @Override
    public String toString()
    {
        String result = "Block: ";
        return result;
    }
    
    @Override
    public GameObject getClone()
    {
        GameObject clone = new Block(this);
        return clone;
    }
}
