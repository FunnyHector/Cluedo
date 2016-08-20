package tile;

import card.Location;

public class RoomTile extends Room {

    private int x;
    private int y;
    private boolean isHoldingToken;

    public RoomTile(Room room, int x, int y) {
        super(room.getRoom(), room.getSecPasTo());
        this.x = x;
        this.y = y;
        isHoldingToken = false;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isHoldingToken() {
        return isHoldingToken;
    }

    public void setHoldingToken(boolean isHoldingToken) {
        this.isHoldingToken = isHoldingToken;
    }
}
