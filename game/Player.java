package game;

import java.util.ArrayList;
import java.util.List;

import card.Card;
import card.Character;
import tile.Position;

/**
 * This class represents a player in Cluedo game. A player can either be a
 * human-controlled player, or a dummy token on board (it's not played by anybody, but it
 * has to be on board to indicate a suspect).
 * 
 * @author Hector
 *
 */
public class Player {

    /**
     * the character of this player
     */
    private final Character token;
    /**
     * player's name
     */
    private String name;
    /**
     * it's position on board
     */
    private Position position;
    /**
     * cards drawn by this player. dummy token won't have any card in it.
     */
    private List<Card> cards;
    /**
     * remaining steps to move
     */
    private int remainingSteps;
    /**
     * a flag to indicate whether this player can continue to play.
     */
    private boolean isPlaying;

    /**
     * Construct a player, initialise its character, position, and whether playing or not.
     * 
     * @param token
     *            --- which character
     * @param pos
     *            -- its position
     * @param isPlaying
     *            --- whether this player is in game (human controllable)
     */
    public Player(Character token, Position pos, boolean isPlaying) {
        this.token = token;
        this.position = pos;
        this.isPlaying = isPlaying;
        cards = new ArrayList<>();
    }

    /**
     * Which character is it?
     * 
     * @return --- the character
     */
    public Character getToken() {
        return token;
    }

    /**
     * Let the player customise his name (The token's name remains unchanged).
     * 
     * @param name
     *            --- the customised name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * What's this player's name?
     * 
     * @return --- the player's name
     */
    public String getName() {
        return name;
    }

    /**
     * Get current position of this token.
     * 
     * @return --- current position
     */
    public Position getPosition() {
        return position;
    }

    /**
     * Set current position to the given position.
     * 
     * @param position
     *            --- the new position
     */
    public void setPosition(Position position) {
        this.position = position;
    }

    /**
     * What cards are drawn by this player?
     * 
     * @return --- all cards drawn as a list of Card
     */
    public List<Card> getCards() {
        return cards;
    }

    /**
     * This method draws a card for this player.
     * 
     * @param card
     */
    public void drawACard(Card card) {
        cards.add(card);
    }

    /**
     * Examine whether this player has given card.
     * 
     * @param card
     *            --- the card to be examined
     * @return --- true if this player has it; false if not.
     */
    public boolean hasCard(Card card) {
        return cards.contains(card);
    }

    /**
     * Get how many remaining steps this player can move.
     * 
     * @return --- the number of remaining steps
     */
    public int getRemainingSteps() {
        return remainingSteps;
    }

    /**
     * Set how many remaining steps this player can move.
     * 
     * @param remaingingSteps
     *            --- the number of remaining steps
     */
    public void setRemainingSteps(int remaingingSteps) {
        if (remaingingSteps < 0) {
            throw new GameError("Remaining steps should be at least 0.");
        }
        remainingSteps = remaingingSteps;
    }

    /**
     * Is this token being played by a human player?
     * 
     * @return --- true if this token is played by a human player; false if it's only a
     *         dummy token on board.
     */
    public boolean isPlaying() {
        return isPlaying;
    }

    /**
     * This method join current player into game (i.e. a player choosed this character)
     * 
     * @param isPlaying
     *            --- true if this token is played by a human player; false if it's only a
     *            dummy token on board.
     */
    public void setPlaying(boolean isPlaying) {
        this.isPlaying = isPlaying;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((token == null) ? 0 : token.hashCode());
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
        Player other = (Player) obj;
        if (token != other.token)
            return false;
        return true;
    }

}
