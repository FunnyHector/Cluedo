package card;

/**
 * This interface represents a card in Cluedo game. It could be a Character card, a Room
 * (Location) card, or a weapon card.
 * 
 * @author Hector
 *
 */
public interface Card {

    /**
     * This is an alternative version of toString() method who returns a single character
     * String to print a symbol on text-based graphical board.
     * 
     * @return --- a single character String to print a symbol on text-based graphical
     *         board.
     */
    public String toStringOnBoard();

}
