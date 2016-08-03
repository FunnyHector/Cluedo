package tile;

/**
 * This abstract class represents a position on Cluedo game baord. It can either be a
 * tile, or a Room.
 * 
 * @author Hector
 *
 */
public abstract class Position {
    
    public abstract String optionString(Position destination);
    

}
