package card;

/**
 * This enum class represents a Weapon card in Cluedo game. There are six weapons,
 * Candlestick, Dagger, Lead Pipe, Revolver, Rope, and Spanner.<br>
 * <br>
 * Note that this class is also used to symbolically represent the weapon token in game.
 * 
 * @author Hector
 *
 */
public enum Weapon implements Card {

    Candlestick, Dagger, Lead_Pipe, Revolver, Rope, Spanner;

    @Override
    public String toString() {
        return this.name().replaceAll("_", " ");
    }

    @Override
    public char toStringOnBoard() {
        char s = ' ';
        switch (this.ordinal()) {
        case 0:
            s = 'c';
            break;
        case 1:
            s = 'd';
            break;
        case 2:
            s = 'p';
            break;
        case 3:
            s = 'g';
            break;
        case 4:
            s = 'r';
            break;
        case 5:
            s = 's';
            break;
        default:
        }
        return s;
    }

}
