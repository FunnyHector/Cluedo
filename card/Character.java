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

    Miss_Scarlet(24, 7), Colonel_Mustard(17, 0), Mrs_White(0, 9), The_Reverend_Green(3,
            14), Mrs_Peacock(6, 23), Professor_Plum(19, 23);

    public final int startPosX;
    public final int startPosY;

    private Character(int startPosX, int startPosY) {
        this.startPosX = startPosX;
        this.startPosY = startPosY;
    }

    @Override
    public String toString() {
        return this.name().replaceAll("_", " ");
    }

    @Override
    public String toStringOnBoard() {
        String s = null;
        switch (this.ordinal()) {
        case 0:
            s = "S";
            break;
        case 1:
            s = "M";
            break;
        case 2:
            s = "W";
            break;
        case 3:
            s = "G";
            break;
        case 4:
            s = "C";
            break;
        case 5:
            s = "P";
            break;
        default:
        }
        return s;
    }

}
