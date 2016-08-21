package card;

import game.GameError;

/**
 * This enum class represents a Room card in Cluedo game. There are nine rooms, Kitchen,
 * Ball room, Conservatory, Billard Room, Library, Study, Hall, Lounge, and Dining
 * Room.<br>
 * <br>
 * Note that this class is also used to symbolically represent the Room position in game.
 * 
 * @author Hector
 *
 */
public enum Location implements Card {

    Kitchen, Ball_room, Conservatory, Billard_Room, Library, Study, Hall, Lounge, Dining_Room;

    @Override
    public String toString() {
        return this.name().replaceAll("_", " ");
    }

    @Override
    public char toStringOnBoard() {
        return ' ';
    }

    /**
     * Get the location whose ordinal is index.
     * 
     * @param index
     *            --- the index (ordinal)
     * @return --- the location at the given index (ordinal)
     */
    public static Location get(int index) {
        switch (index) {
        case 0:
            return Kitchen;
        case 1:
            return Ball_room;
        case 2:
            return Conservatory;
        case 3:
            return Billard_Room;
        case 4:
            return Library;
        case 5:
            return Study;
        case 6:
            return Hall;
        case 7:
            return Lounge;
        case 8:
            return Dining_Room;
        default:
            throw new GameError("Invalid index.");
        }
    }
}
