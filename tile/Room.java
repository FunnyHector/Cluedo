package tile;

import java.util.List;

import card.Location;

/**
 * This class represents a room on Cluedo game board. Each room has one or more entrance
 * that allows player to enter into. Some rooms have secret passage to another room.
 * 
 * @author Hector
 *
 */
public class Room extends Position {

    // Room name
    private final Location room;
    // The end of the secret passage if this room has one. null if it doesn't have one.
    private final Location secPasTo;
    // a collection of entrance tiles only on which a player can enter this room.
    private List<Tile> entrances;

    /**
     * 
     * @param room
     * @param secPasTo
     * @param entrances
     */
    public Room(Location room, Location secPasTo, List<Tile> entrances) {
        this.room = room;
        this.secPasTo = secPasTo;
        this.entrances = entrances;
    }

    /**
     * 
     * @return
     */
    public Location getRoom() {
        return room;
    }

    /**
     * 
     * @return
     */
    public boolean hasSecPas() {
        return secPasTo == null;
    }

    /**
     * 
     * @return
     */
    public Location getSecPas() {
        return secPasTo;
    }

    /**
     * This method tells whether a player can enter into this room from a given tile.
     * 
     * @param tile
     * @return
     */
    public boolean canEnterFromPosition(Tile tile) {
        return entrances.contains(tile);
    }

    @Override
    public String toString() {
        // TODO change this toString
        // or do two toString methods, one is for messages, one is for board printing
        return String.valueOf(room.ordinal() + 1);
    }

}
