package game;

import java.util.ArrayList;
import java.util.List;

import card.Card;
import card.Character;
import card.Location;
import card.Weapon;

/**
 * This class represents a combination of one Character, one Location, and one Weapon.
 * Essentially it could be a suggestion or an accusation made by a player, or the solution
 * selected at beginning.
 * 
 * @author Hector
 * 
 */
public class Suggestion {

    /**
     * The character involved in this suggestion / accusation
     */
    public final Character character;
    /**
     * The weapon involved in this suggestion / accusation
     */
    public final Weapon weapon;
    /**
     * The location involved in this suggestion / accusation
     */
    public final Location location;

    /**
     * Construct a suggestion with one Character, one Location, and one Weapon.
     * 
     * @param character
     *            --- the involved Character
     * @param location
     *            --- the involved Location
     * @param weapon
     *            --- the involved weapon
     */
    public Suggestion(Character character, Weapon weapon, Location location) {
        super();
        this.character = character;
        this.location = location;
        this.weapon = weapon;
    }

    /**
     * Convert this suggestion to a list of three cards.
     * 
     * @return --- this suggestion as a list of three cards.
     */
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
