package tile;

import game.GameError;

/**
 * This class represents a special type of Tile, which is the tile at the entrance of a
 * room. Only from this type of Tile, a player can enter a room. Player cannot enter any
 * room from normal Tiles.
 * 
 * @author Hector
 *
 */
public class Entrance extends Tile {

    /**
     * The room that the player can enter from this entrance tile.
     */
    private Room toRoom;

    /**
     * Construct an entrance tile.
     * 
     * @param x
     *            --- horizontal coordinate
     * @param y
     *            --- vertical coordinate
     * @param toRoom
     *            --- The room that the player can enter from this entrance tile.
     */
    public Entrance(int x, int y, Room toRoom) {
        super(x, y);
        this.toRoom = toRoom;
    }

    /**
     * Get the room that the player can enter from this entrance tile.
     * 
     * @return --- The room that the player can enter from this entrance tile.
     */
    public Room toRoom() {
        return toRoom;
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
        } else if (destination instanceof Room) {
            Room destinationRoom = (Room) destination;
            return "Enter " + destinationRoom.getRoom().toString() + ".";
        } else {
            throw new GameError(
                    "Shouldn't move from an entrance to " + destination.toString());
        }
    }

    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((toRoom == null) ? 0 : toRoom.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        Entrance other = (Entrance) obj;
        if (toRoom == null) {
            if (other.toRoom != null)
                return false;
        } else if (!toRoom.equals(other.toRoom))
            return false;
        return true;
    }

}
