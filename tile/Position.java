package tile;

/**
 * This abstract class represents a position on Cluedo game board. It can either be a
 * tile, or a Room.
 * 
 * @author Hector
 *
 */
public abstract class Position {

    /**
     * This method looks at the type of this Position and the other Position passed by
     * argument, and then form a String as the option menu used in text gui.<br>
     * <br>
     * For example, if this Position is a Tile, and the argument Position is another Tile,
     * then the returned String will be "Move north / east / south / west" according to
     * the direction. If this Position is a Room, and the argument Position is another
     * Room, then the returned String will be "Take the secret passage to" another room.
     * 
     * @param destination
     *            --- the Position to move to
     * @return --- a proper, sensible String to used as option menu in text gui.
     */
    public abstract String optionString(Position destination);

}
