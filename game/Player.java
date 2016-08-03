package game;

import java.util.ArrayList;
import java.util.List;

import card.Card;
import card.Character;
import tile.Position;
import tile.Room;
import tile.Tile;

/**
 * This class represents a player in Cluedo game. A player can either be a
 * human-controlled player, or a dummy token on board (it's not played by anybody, but it
 * has to be on board to indicate a suspect).
 * 
 * @author Hector
 *
 */
public class Player {

    private final Character token;
    /*
     * playerID is set to from 1 to 6, 0 indicates the player is not controlled by
     * anybody, i.e. only a dummy token on board
     */
    private int playerID;
    private Position position;
    private List<Card> cards;
    private int remainingSteps;

    /**
     * 
     * @param token
     * @param isInGame
     */
    public Player(Character token, Position pos) {
        this.token = token;
        this.position = pos;
        playerID = 0;
        cards = new ArrayList<>();
    }

    public List<Card> getCards() {
        return cards;
    }

    public boolean hasCard(Card card) {
        return cards.contains(card);
    }

    /**
     * This method draws a card for the player.
     * 
     * @param card
     */
    public void drawACard(Card card) {
        cards.add(card);
    }
    
    public int getID() {
        return playerID;
    }
    
    public void setID(int playerID) {
        this.playerID = playerID;
    }

    /**
     * 
     * @return
     */
    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Character getToken() {
        return token;
    }

    public void setRemainingSteps(int steps) {
        remainingSteps = steps;
    }

    public int getRemainingSteps() {
        return remainingSteps;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + playerID;
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
        if (playerID != other.playerID)
            return false;
        return true;
    }

}
