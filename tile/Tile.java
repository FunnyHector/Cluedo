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
        return "[" + x + " , " + y + "]";
        // return "0";
    }
    
    public String toStringOnBoard() {
        return "0";
    }

    @Override
    public String optionString(Position destination) {
        if (destination instanceof Tile) {
            Tile destinationTile = (Tile) destination;
            if (destinationTile.x - this.x == 1) {
                return "Move east.";
            } else if (destinationTile.x - this.x == -1) {
                return "Move west.";
            } else if (destinationTile.y - this.y == 1) {
                return "Move south.";
            } else if (destinationTile.y - this.y == -1) {
                return "Move north.";
            } else {
                throw new GameError("Shouldn't move two tiles away once");
            }
        } else {
            throw new GameError("Shouldn't move from a tile to a room");
        }
    }

    // TODO hashcode and equals

}
