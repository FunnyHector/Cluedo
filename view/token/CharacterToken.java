package view.token;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import card.Character;
import tile.RoomTile;
import tile.Tile;

public class CharacterToken extends AbstractToken {

    private Character character;
    private RoomTile roomTile;

    public CharacterToken(ImageIcon img, int x, int y, Character character) {
        super(img, x, y);
        this.character = character;
        roomTile = null;
    }

    public Character getCharacter() {
        return character;
    }

    /**
     * This method set the character token in specified room
     * 
     * @param roomTile
     *            --- the room to set in.
     */
    public void setRoomTile(RoomTile roomTile) {
        if (this.roomTile != null) {
            this.roomTile.setHoldingToken(false);
        }
        roomTile.setHoldingToken(true);
        this.roomTile = roomTile;
        super.moveTo(roomTile.getX(), roomTile.getY());
    }

    public void moveToTile(Tile tile) {
        super.moveTo(tile.x, tile.y);
    }

    /**
     * which room is it in? Note that if this character token is not in a room, this
     * method returns null.
     * 
     * @return --- the room it is located, or null if it is outside any room
     */
    public RoomTile getRoomTile() {
        return roomTile;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((character == null) ? 0 : character.hashCode());
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
        CharacterToken other = (CharacterToken) obj;
        if (character != other.character)
            return false;
        return true;
    }

}