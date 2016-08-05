package game;

import card.Weapon;
import tile.Room;

/**
 * This class represents a weapon token on board. In Cluedo game, six tokens of weapon are
 * moved from room to room to represents a weapon used in murder.
 * 
 * @author Hector
 *
 */
public class WeaponToken {

    // weapon
    private final Weapon token;
    // in which room
    private Room room;

    /**
     * Construct a weapon token on board.
     * 
     * @param token
     *            --- the weapon
     * @param room
     *            --- in which room
     */
    public WeaponToken(Weapon token, Room room) {
        this.token = token;
        this.room = room;
    }

    /**
     * what weapon is it?
     * 
     * @return --- what weapon
     */
    public Weapon getToken() {
        return token;
    }

    /**
     * This method set the weapon in specified room
     * 
     * @param room
     *            --- the room to set in.
     */
    public void setRoom(Room room) {
        this.room = room;
    }

    /**
     * which room is it in?
     * 
     * @return --- the room it is located
     */
    public Room getRoom() {
        return room;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((token == null) ? 0 : token.hashCode());
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
        WeaponToken other = (WeaponToken) obj;
        if (token != other.token)
            return false;
        return true;
    }
}
