package tile;

import java.util.ArrayList;
import java.util.List;

import card.Location;
import game.GameError;

/**
 * This class represents a room on Cluedo game board. Each room has one or more entrance
 * that allows player to enter into. Some rooms have secret passage to another room.
 * 
 * @author Hector
 *
 */
public class Room extends Position {

    /**
     * The Location
     */
    private final Location room;
    /**
     * The end of the secret passage if this room has one. null if it doesn't have one.
     */
    private final Location secPasTo;
    /**
     * a list of entrance tiles only on which a player can enter this room. The order is
     * crucial as we identify each entrance by it's index inside this list. Also, we need
     * to make sure each entrance only appear once here since we are using only one
     * instance for each room, we need to make sure
     */
    private final List<Entrance> entrances;

    /**
     * Construct a Room with current location and the location where the secret passage
     * lead to if applicable
     * 
     * @param room
     *            --- this room
     * @param secPasTo
     *            --- the room that the secret passage lead to. null if this room has no
     *            secret passage
     */
    public Room(Location room, Location secPasTo) {
        this.room = room;
        this.secPasTo = secPasTo;
        entrances = new ArrayList<>();
    }

    /**
     * What room is it?
     * 
     * @return --- this room
     */
    public Location getRoom() {
        return room;
    }

    /**
     * Does this room has a secret passage to somewhere?
     * 
     * @return --- true for yes; false for no
     */
    public boolean hasSecPas() {
        return secPasTo != null;
    }

    /**
     * Which room does the secret passage lead to?
     * 
     * @return --- the room that the secret passage lead to. null if this room has no
     *         secret passage
     */
    public Location getSecPasTo() {
        return secPasTo;
    }

    /**
     * What entrances / exits are there in this room?
     * 
     * @return --- all entrances / exits as a list
     */
    public List<Entrance> getEntrances() {
        return new ArrayList<>(entrances);
    }

    /**
     * Add an entrance to this room.
     * 
     * @param entrance
     *            --- An entrance tile of this room
     */
    public void addEntrances(Entrance entrance) {
        // do not add duplicates. (It does happen in JUnit test)
        if (!entrances.contains(entrance)) {
            entrances.add(entrance);
        }
    }

    @Override
    public String optionString(Position destination) {
        if (destination instanceof Room) {
            Room destinationRoom = (Room) destination;
            return "Take the secret passage to " + destinationRoom.toString() + ".";
        } else if (destination instanceof Entrance) {
            Entrance exitOfRoom = (Entrance) destination;
            return "Exit room from exit (" + exitOfRoom.x + ", " + exitOfRoom.y + ").";
        } else {
            throw new GameError("Shouldn't move from a room to a tile (not entrance)");
        }
    }

    @Override
    public String toString() {
        return room.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((room == null) ? 0 : room.hashCode());
        result = prime * result + ((secPasTo == null) ? 0 : secPasTo.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Room other = (Room) obj;
        if (room != other.room)
            return false;
        if (secPasTo != other.secPasTo)
            return false;
        return true;
    }

}
