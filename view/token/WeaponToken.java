package view.token;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import card.Weapon;
import tile.Room;
import tile.RoomTile;

/**
 * TODO need to update:
 * 
 * This class represents a weapon token on board. In Cluedo game, six tokens of weapon are
 * moved from room to room to represents a weapon used in murder.
 * 
 * @author Hector
 *
 */
public class WeaponToken extends AbstractToken {

    // weapon
    private final Weapon weapon;
    // in which room
    private RoomTile roomTile;
    //

    /**
     * Construct a weapon token on board.
     * 
     * @param token
     *            --- the weapon
     * @param roomTile
     *            --- in which room, and on which tile is this token put
     */
    public WeaponToken(ImageIcon img, Weapon token, RoomTile roomTile) {
        super(img, roomTile.getX(), roomTile.getY());
        this.weapon = token;
        this.roomTile = roomTile;
    }

    /**
     * what weapon is it?
     * 
     * @return --- what weapon
     */
    public Weapon getToken() {
        return weapon;
    }

    /**
     * This method set the weapon in specified room
     * 
     * @param roomTile
     *            --- the room to set in.
     */
    public void setRoomTile(RoomTile roomTile) {
        this.roomTile.setHoldingToken(false);
        roomTile.setHoldingToken(true);
        this.roomTile = roomTile;
        super.moveTo(roomTile.getX(), roomTile.getY());
    }

    /**
     * which room is it in?
     * 
     * @return --- the room it is located
     */
    public RoomTile getRoomTile() {
        return roomTile;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((roomTile == null) ? 0 : roomTile.hashCode());
        result = prime * result + ((weapon == null) ? 0 : weapon.hashCode());
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
        if (roomTile == null) {
            if (other.roomTile != null)
                return false;
        } else if (!roomTile.equals(other.roomTile))
            return false;
        if (weapon != other.weapon)
            return false;
        return true;
    }

}