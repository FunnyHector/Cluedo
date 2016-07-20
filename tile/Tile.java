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
    private final int x, y;

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

    /**
     * 
     * @return
     */
    public int getX() {
        return x;
    }

    /**
     * 
     * @return
     */
    public int getY() {
        return y;
    }

    @Override
    public String toString() {
        return "0";
    }

    // TODO hashcode and equals

}
