package tile;

import game.GameError;

/**
 * 
 * 
 * @author Hector
 *
 */
public class Entrance extends Tile {

    private Room toRoom;

    public Entrance(int x, int y, Room toRoom) {
        super(x, y);
        this.toRoom = toRoom;
    }

    public Room toRoom() {
        return toRoom;
    }

    @Override
    public String toString() {
        return super.toString();
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
