package game;

import card.Weapon;
import tile.Room;

public class WeaponToken {

    private final Weapon token;
    private Room room;

    public WeaponToken(Weapon token, Room room) {
        this.token = token;
        this.room = room;
    }

    public Weapon getToken() {
        return token;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

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
