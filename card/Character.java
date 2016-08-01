package card;

/**
 * This enum class represents a character card in Cluedo game. There are six characters,
 * Miss Scarlet, Colonel Mustard, Mrs White, The Reverend Green, Mrs Peacock, and
 * Professor Plum. Each of them has a unique starting position on board.
 * 
 * @author Hector
 * 
 */
public enum Character implements Card {

    Miss_Scarlet, Colonel_Mustard, Mrs_White, The_Reverend_Green, Mrs_Peacock, Professor_Plum;

    @Override
    public String toString() {
        return this.name().replaceAll("_", " ");
    }

    @Override
    public char toStringOnBoard() {
        char s = ' ';
        switch (this.ordinal()) {
        case 0:
            s = 'S';
            break;
        case 1:
            s = 'M';
            break;
        case 2:
            s = 'W';
            break;
        case 3:
            s = 'G';
            break;
        case 4:
            s = 'C';
            break;
        case 5:
            s = 'P';
            break;
        default:
        }
        return s;
    }

}
