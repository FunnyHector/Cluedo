package game;


import card.Weapon;
import tile.Position;
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
}
