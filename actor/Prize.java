package actor;

import java.awt.Color;

/**
 * A <code>Material</code> is an actor that does nothing. 
 * It can be picked up and placed down to fill orders. <br />
 */

public class Prize extends GameObject
{     
    public static final int NUMBER_OF_PRIZE_TYPES = 1;
    
    public static final int SUPER_PRIZE_VALUE = 500;
    public static final int SUPER_PRIZE_TYPE = 0;
    public static final int TURNS_PER_SUPER_PRIZE = 100;
    public static final int TURNS_PER_PRIZE = 10;
    public static final int DEFAULT_VALUE = 10;
    public static final int BUILT_VALUE = 25;
    
    private int type;
    private int value;


    /**
     * Constructs a Starter Cheese.
     */
    public Prize()
    {
        type = (int)(Math.random()*NUMBER_OF_PRIZE_TYPES)+1;
        value = DEFAULT_VALUE;
        setColor(null);
    }
    
    /**
     * Constructs a Cheese. used during the run of play.
     */
    public Prize(int t, int v)
    {
        type = t;
        value = v;
        setColor(null);
        if(v==BUILT_VALUE) setColor(Color.BLUE);
        if(v==SUPER_PRIZE_VALUE) setColor(Color.YELLOW);
    }
    /**
     * Constructs a Cheese that is a copy of another Cheese.
     * @param in the Cheese to be copied.
     */
    public Prize(Prize in)
    {
        super(in);
        type = in.getType();
        setLocation(in.getLocation());
        value = in.getValue();
    }
    
    /**
     * Gets the type of this Material.
     * @return the type of this Material.
     */
    public int getType()
    {
        return type;
    }
    
    public int getValue()
    {
        return value;
    }
    
    @Override
    public GameObject getClone()
    {
        GameObject clone = new Prize(this);
        return clone;
    }

    @Override
    public String toString()
    {
            return "Prize!  value="+value;
    }
}
