package tile;

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
    }

}
