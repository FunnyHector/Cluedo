package game;

import card.Character;
import card.Location;
import card.Weapon;

/**
 * This class represents a suggestion made by player, or the solution selected at the
 * beginning of the game, i.e. it's a combination of one character, one room, and one
 * weapon.
 * 
 * @author Hector
 *
 */
public class Suggestion {

    private Character character;
    private Location location;
    private Weapon weapon;

    /**
     * 
     * @param character
     * @param location
     * @param weapon
     */
    public Suggestion(Character character, Location location, Weapon weapon) {
        super();
        this.character = character;
        this.location = location;
        this.weapon = weapon;
    }

    /**
     * This method tells whether current suggestion is correct (same as solution).
     * 
     * @param game
     * @return
     */
    public boolean isCorrect(Game game) {
        return this.equals(game.getSolution());
    }

    // TODO equals and hashcode
}
