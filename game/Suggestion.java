package game;

import java.util.ArrayList;
import java.util.List;

import card.Card;
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

    public List<Card> asList() {
        List<Card> list = new ArrayList<>();
        list.add(character);
        list.add(weapon);
        list.add(location);
        return list;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((character == null) ? 0 : character.hashCode());
        result = prime * result + ((location == null) ? 0 : location.hashCode());
        result = prime * result + ((weapon == null) ? 0 : weapon.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Suggestion other = (Suggestion) obj;
        if (character != other.character)
            return false;
        if (location != other.location)
            return false;
        if (weapon != other.weapon)
            return false;
        return true;
    }

}
