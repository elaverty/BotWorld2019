package actor;

import grid.Location;
import gui.RatBotsColorAssigner;
import java.awt.Color;
import java.util.ArrayList;
import world.RatBotWorld;
/**
 * Bots are the competitors in the BotWorld game.  Every Bot has a BotBrain 
 that acts as its 'brain' by making decisions for its actions.  
 * @author Spock
 */
public class Bot extends GameObject
{       
    /**
     * Each Bot has a BotBrain as it's 'brain'.  The Bot 'tells' the BotBrain 
 what it 'sees' and the BotBrain makes the decision on how to act.  
     */
    private BotBrain aBot; 
        
    private int score;
    private int roundsWon;
    private int matchesWon;
    private int matchesTied;
    private int matchesLost;
    private int totalScore = 0;
    
    private int mostRecentChoice = 0;
    
    /**
     * Constructs a red bot with a generic BotBrain.  
     */
    public Bot()
    {
        setColor(Color.RED);
        aBot = new BotBrain();
        initialize();
    }
    /**
     * Constructs a Rat with the given color and given BotBrain for its 'brain'.
     * All rats have a BotBrain that chooses their action each turn.  
     * @param rb the BotBrain that makes decisions for this Rat.
     */
    public Bot(BotBrain b)
    {
        aBot = b;
        if(b.getPreferredColor() == null)
        {
            setColor(RatBotsColorAssigner.getAssignedColor());
            b.setPreferredColor(getColor());
            
        }
        else    
            setColor(b.getPreferredColor());
        initialize();
    }
    /**
     * Constructs a copy of this Bot (that does not include the BotBrain.)
     * @param in the Bot being copied.
     */
    public Bot(Bot in)
    {
        super(in);
        aBot = new BotBrain();
        setLocation(in.getLocation());
        score = in.getScore();
    }

    
    public int getMostRecentChoice() { return mostRecentChoice; }
    
    
    /**
     * Overrides the <code>act</code> method in the <code>GameObject</code> 
 class to act based on what the BotBrain decides.
     */
    @Override
    public void act()
    {
        //Ask the BotBrain what to do. 
        giveDataToBot();
        int choice = aBot.chooseAction();
        mostRecentChoice = choice;
        
        turn((choice%1000)); //Turn to face the direction of the choice.
        if( (choice%1000)%90 != 0) choice = -1; //An invalid choice.  
        
        if(choice < 0)
        {
            //REST
        }
        else if(0 <= choice && choice <= 999) 
        {
            //MOVE
            move();
        }
        else if(1000 <= choice && choice <= 1999) 
        {
            //DART
            dart();
        }
        else if(2000 <= choice && choice <= 2999) 
        {
            //BUILD BLOCK
            buildBlock();
        }
        else if(3000 <= choice && choice <= 3999) 
        {
            //BUILD WALLS
            buildWall();
        }
        
        //Color change...
        if(!getColor().equals(aBot.getPreferredColor())
                && aBot.getPreferredColor() != null)
            setColor(aBot.getPreferredColor());
        
    } //end of act() method
    
    /**
     * Turns the Bot
     * @param newDirection the direction to turn to.   
     */
    public void turn(int newDirection)
    {
        setDirection(newDirection);
    }
     public boolean canMove()
    {
        return canMove(getLocation(), getDirection());
    }
   
    /**
     * This method does not allow you to move onto other Rats or Blocks 
     */
    public boolean canMove(Location loc, int dir)
    {
        Location next = loc.getAdjacentLocation(dir);
        if(!getGrid().isValid(next)) 
            return false;
        //Make sure destination is not occupied by another Bot
        if(getGrid().get(next) instanceof Bot)
            return false;
        //Make sure destination is not occupied by a Block
        if(getGrid().get(next) instanceof Block)
            return false;
        
        return true;
    }
    
    /**
     * Moves the rat forward, putting a Tail into the location it previously
     * occupied.
     */
    public void move()
    {        
        if(canMove())
        {
            Location old = getLocation();
            Location next = old.getAdjacentLocation(getDirection());

            processDestination(next);
            moveTo(next);
            placeTail(old);
        }
    }
    
    private void dart()
    {
        Location next = getLocation();
        Location old = getLocation();
        while(canMove(next,getDirection()))
        {
            
            next = next.getAdjacentLocation(getDirection()); 
            if(getGrid().get(next) == null || getGrid().get(next) instanceof Tail)
            {
                moveTo(next);
                placeTail(old);
                old = new Location(next.getRow(), next.getCol());
            }
        }
        processDestination(next);
        moveTo(next);
    }
    
    private void processDestination(Location next)
    {
        GameObject nextObj = getGrid().get(next);

        if(nextObj instanceof Prize)
        {
            Prize m = (Prize)nextObj;
            score+=m.getValue();  
        }
    }
    
    private void placeTail(Location loc)
    {
        Tail t = new Tail(getColor());
        t.putSelfInGrid(getGrid(), loc);
    }
    
    private void buildBlock()
    {
        Location next = getLocation().getAdjacentLocation(getDirection());
        if(getGrid().isValid(next) && 
                !(getGrid().get(next) instanceof Bot) &&
                !(getGrid().get(next) instanceof Block))
        {
            new Block().putSelfInGrid(getGrid(), next);
            //Add a Prize!
            Location p = getGrid().getEmptyRandomPrizeLocation();
            if(p != null)
                new Prize(0,Prize.BUILT_VALUE).putSelfInGrid(getGrid(), p);                
            
        }
    }
    
    private void buildWall()
    {
        ArrayList<Location> places = new ArrayList<Location>();
        Location centerOfWall = getLocation().getAdjacentLocation(getDirection());
        places.add(centerOfWall);
        Location left = new Location(centerOfWall.getRow(), centerOfWall.getCol());
        Location right = new Location(centerOfWall.getRow(), centerOfWall.getCol());
        
        int WALL_SIZE_FROM_CENTER = 3;
        for(int q=0; q<WALL_SIZE_FROM_CENTER; q++)
        {
            left = left.getAdjacentLocation(getDirection()-90);
            places.add(left);
            right = right.getAdjacentLocation(getDirection()+90);
            places.add(right);
        }
        
        for(Location loc : places)
        {
            if(getGrid().isValid(loc) && 
                    !(getGrid().get(loc) instanceof Bot) &&
                    !(getGrid().get(loc) instanceof Block) )
            {
                new Block(Block.TEMPORARY_DURATION).putSelfInGrid(getGrid(), loc);
            }            
        }
    }
    
    @Override
    public String toString()
    {
        return "Rat: "+ aBot.getName();
    }

    /**
     * Updates the most recent data (location, grid and status)
 information to the BotBrain.  This allows the BotBrain to make a decision
 based on current data every turn.  
     */
    public final void giveDataToBot()
    {
        //score, energy, col, row, myStuff ================
        aBot.setScore(score);
        aBot.setLocation(getLocation().getCol(), getLocation().getRow());
        //match stuff: bestScore, roundsWon ===================
        aBot.setBestScore(this.calculateBestScore());
        aBot.setRoundsWon(this.getRoundsWon());
        //world stuff: moveNumber, roundNumber ================
        aBot.setMoveNumber(RatBotWorld.getMoveNum());
        aBot.setRoundNumber(RatBotWorld.getRoundNum());

        //theArena!============================================
        int numRows = getGrid().getNumRows();
        int numCols = getGrid().getNumCols();
        GameObject[][] theArena = new GameObject[numRows][numCols];
        for(int row=0; row<numRows; row++)
        {
            for(int col=0; col<numCols; col++)
            {
                GameObject a = getGrid().get(new Location(row, col));
                if(a != null && !(a instanceof Tail) )
                    theArena[row][col] = a.getClone();
                //Might need to do each with instanceof here...
                
            }
        }
        aBot.setArena(theArena);
            
 }
    
    public int calculateBestScore()
    {
        int bestScore = getScore();
        ArrayList<Bot> bots = getGrid().getAllRats();
        for(Bot b : bots)
        {
            if(b.getScore() > bestScore)
            {
                bestScore = b.getScore();
            }
        }  
        return bestScore;
    }
    /**
     * Accessor method to get the BotBrain from a Bot.
     * @return the BotBrain 'brain' of this BotBrain.
     */
    public BotBrain getRatBot()
    {
        return aBot;
    }

    /**
     * Gets the current score (from this round).  
     * @return the score
     */
    public int getScore() { return score; }
    /**
     * Sets the current score of this Bot.
     * @param in the score
     */
    public void setScore(int in) { score = in; }
    /**
     * Adds the given amount to score of this Bot.  
     * @param add the amount to add
     */
    public void addToScore(int add) { score += add; }
    /**
     * Gets the total points scored over all rounds in this match for this Bot.
     * @return the total score
     */
    public int getTotalScore() { return totalScore; }

    /**
     * Gets the number of rounds won by this Bot in the match.
     * @return the rounds won
     */
    public int getRoundsWon() { return roundsWon; }
    
    
    /**
     * Sets the number of rounds won by this Bot in the match.
     * @param in the rounds won
     */
    public void setRoundsWon(int in) { roundsWon = in; }
    /**
     * Increases the number of rounds won in this match by this Bot by one.
     */
    public void increaseRoundsWon() { roundsWon++; }

    // These methods are used for the RoundRobin tourney.
    public int getMatchesWon() { return matchesWon; }
    public int getMatchesTied() { return matchesTied; }
    public int getMatchesLost() { return matchesLost; }
    public void increaseMatchesWon() { matchesWon++; }
    public void increaseMatchesTied() { matchesTied++; }
    public void increaseMatchesLost() { matchesLost++; }
     /**
     * Initializes this Bot for a new round.  
     */
    public final void initialize()
    {
        totalScore += score;
        score = 0;
        aBot.initForRound();
    }
    
    public void clearScores()
    {
        score = 0;
        roundsWon = 0;
        matchesWon = 0;
        matchesTied = 0;
        matchesLost = 0;
        totalScore = 0;
    }

    @Override
    public GameObject getClone()
    {
        GameObject clone = new Bot(this);
        return clone;
    }
    
}