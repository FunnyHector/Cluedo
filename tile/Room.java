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

    // Room name
    private final Location room;
    // The end of the secret passage if this room has one. null if it doesn't have one.
    private final Location secPasTo;
    // a collection of entrance tiles only on which a player can enter this room.
    private final List<Entrance> entrances;
    // a collection of decorative tiles for drawing tokens
    private final List<Tile> decoTiles;
    private int decoIndex = 0;

    // Three tiles inside this room (three is at most we need for drawing tokens)
//    private final Tile playerTile_1;
//    private final Tile playerTile_2;
//    private final Tile weaponTile;

    /**
     * 
     * @param room
     * @param secPasTo
     * @param entrances
     */
    public Room(Location room, Location secPasTo) {
        this.room = room;
        this.secPasTo = secPasTo;
        entrances = new ArrayList<>();
        decoTiles = new ArrayList<>();
//        this.playerTile_1 = playerTile_1;
//        this.playerTile_2 = playerTile_2;
//        this.weaponTile = weaponTile;
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
        return secPasTo != null;
    }

    /**
     * 
     * @return
     */
    public Location getSecPasTo() {
        return secPasTo;
    }
    
    public List<Entrance> getEntrances() {
        return entrances;
    }

    public void addEntrances(Entrance entrance) {
        entrances.add(entrance);
    }
    
    public void addDecoTiles(Tile decoTile) {
        decoTiles.add(decoTile);
    }
    
    public Tile getNextDecoTile() {
        int index = decoIndex;
        decoIndex++;
        if (decoIndex >= decoTiles.size()) {
            decoIndex = 0;  // get only 
        }
        return decoTiles.get(index);
    }
    


    @Override
    public String toString() {
        return room.toString();
        //return "" + (room.ordinal() + 1);
    }

    @Override
    public String optionString(Position destination) {
        if (destination instanceof Room) {
            Room destinationRoom = (Room) destination;
            return "Take the secret passage to " + destinationRoom.getSecPasTo().toString() + ".";
        } else if (destination instanceof Entrance) {
            Entrance exitOfRoom = (Entrance) destination;
            return "Exit room from exit (" + exitOfRoom.x + ", " + exitOfRoom.y + ").";
        } else {
            throw new GameError("Shouldn't move from a room to a tile (not entrance)");
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + decoIndex;
        result = prime * result + ((decoTiles == null) ? 0 : decoTiles.hashCode());
        result = prime * result + ((entrances == null) ? 0 : entrances.hashCode());
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
        if (decoIndex != other.decoIndex)
            return false;
        if (decoTiles == null) {
            if (other.decoTiles != null)
                return false;
        } else if (!decoTiles.equals(other.decoTiles))
            return false;
        if (entrances == null) {
            if (other.entrances != null)
                return false;
        } else if (!entrances.equals(other.entrances))
            return false;
        if (room != other.room)
            return false;
        if (secPasTo != other.secPasTo)
            return false;
        return true;
    }
    
}
