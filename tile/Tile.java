package tile;

import game.GameError;

/**
 * This class represents a single tile on Cluedo game board (tiles that are out of rooms).
 * 
 * @author Hector
 *
 */
public class Tile extends Position {

    // the coordinates of this tile
    public final int x, y;

    /**
     * 
     * @param x
     * @param y
     */
    public Tile(int x, int y) {
        super();
        if (x < 0 || x > 23 || y < 0 || y > 24) {
            throw new GameError("Invalid Coordinates");
        }

        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        // return "[" + x + " , " + y + "]";
        return "0";
    }
    
    public String toStringOnBoard() {
        return "0";
    }

    // TODO hashcode and equals

}
