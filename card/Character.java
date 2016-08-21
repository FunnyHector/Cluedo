package card;

import game.GameError;

/**
 * This enum class represents a character card in Cluedo game. There are six characters,
 * Miss Scarlet, Colonel Mustard, Mrs White, The Reverend Green, Mrs Peacock, and
 * Professor Plum. Each of them has a unique starting position on board.<br>
 * <br>
 * Note that this class is also used to symbolically represent the player in game.
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

    /**
     * This method returns the next character in turn. When current character ends turn,
     * It's useful for the game to know who the next acting character is.
     * 
     * @return --- the next character in turn.
     */
    public Character nextCharacter() {
        switch (this) {
        case Colonel_Mustard:
            return Mrs_White;
        case Miss_Scarlet:
            return Colonel_Mustard;
        case Mrs_Peacock:
            return Professor_Plum;
        case Mrs_White:
            return The_Reverend_Green;
        case Professor_Plum:
            return Miss_Scarlet;
        case The_Reverend_Green:
            return Mrs_Peacock;
        default:
            return null; // dead code
        }
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

    /**
     * Get the character whose ordinal is index.
     * 
     * @param index
     *            --- the index (ordinal)
     * @return --- the character at the given index (ordinal)
     */
    public static Character get(int index) {
        switch (index) {
        case 0:
            return Miss_Scarlet;
        case 1:
            return Colonel_Mustard;
        case 2:
            return Mrs_White;
        case 3:
            return The_Reverend_Green;
        case 4:
            return Mrs_Peacock;
        case 5:
            return Professor_Plum;
        default:
            throw new GameError("Invalid index.");
        }
    }
}
