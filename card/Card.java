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
     * This is an alternative version of toString() method which returns a single char
     * String to print a symbol on text-based graphical board.
     * 
     * @return --- a single char String to print a symbol on text-based graphical board.
     */
    public char toStringOnBoard();

}
