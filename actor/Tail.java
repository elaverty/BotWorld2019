package actor;

import java.awt.Color;

/**
 * A <code>Tail</code> is a GameObject that darkens over time. 
 * Rats leave a tail as they move. <br />
 */

public class Tail extends GameObject
{
    private static final Color DEFAULT_COLOR = Color.PINK;
    private static double DARKENING_FACTOR = 0.04;
    private static int DURATION = 10;
    public static void toggleDURATION() 
    { 
        if(DURATION==10)
            DURATION = 40;
        else DURATION = 10;
        DARKENING_FACTOR = .4/DURATION; 
    } 
    
    private int age;
    
    /**
     * Constructs a pink tail.
     */
    public Tail()
    {
        age = 0;
        setColor(DEFAULT_COLOR);
    }

    /**
     * Constructs a tail of a given color.
     * @param initialColor the initial color of this tail
     */
    public Tail(Color initialColor)
    {
        age = 0;
        setColor(initialColor);
    }

    /**
     * Constructs a copy of this Tail.
     * @param in the Tail being copied.
     */
    public Tail(Tail in)
    {
        super(in);
        age = 0;
        setColor(in.getColor());
    }
    /**
     * Causes the color of this tail to darken.
     */
    @Override
    public void act()
    {
        age++;
        fadeOut();
    }
    
    
    /**
     * Fades out the color of a destroyed tail until it is nearly the same 
     * color as the arena (light gray) at which point it is removed.
     */   
    public void fadeOut()
    {
        Color c = getColor();
        Color base = Color.LIGHT_GRAY;
        int red = (int) (c.getRed() - (c.getRed()-base.getRed()) * (DARKENING_FACTOR));
        int green = (int) (c.getGreen() - (c.getGreen()-base.getGreen()) * (DARKENING_FACTOR));
        int blue = (int) (c.getBlue() - (c.getBlue()-base.getBlue()) * (DARKENING_FACTOR));

        setColor(new Color(red, green, blue));
        
        if(age > DURATION)
            removeSelfFromGrid();        
    }
    
    
    @Override
    public String toString()
    {
        return "Tail";
    }
    
    @Override
    public GameObject getClone()
    {
        GameObject clone = new Tail(this);
        return clone;
    }

}
