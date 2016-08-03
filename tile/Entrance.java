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
        /*
        switch (toRoom.getRoom()) {
        case Kitchen:
            return "a";
        case Ball_room:
            return "b";
        case Conservatory:
            return "c";
        case Billard_Room:
            return "d";
        case Library:
            return "e";
        case Study:
            return "f";
        case Hall:
            return "g";
        case Lounge:
            return "h";
        case Dining_Room:
            return "i";
        default:
            return " ";
        }
        */
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
            throw new GameError("Shouldn't move from an entrance to " + destination.toString());
        }
    }

}
