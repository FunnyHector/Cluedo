package card;

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
}
