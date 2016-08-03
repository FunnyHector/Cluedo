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
//    private final int playerID;
    private Position position;
    private List<Card> cards;
    private int remainingSteps;
    
    /**
     * If a player lose, he will be marked as out of game (false); or, if this character
     * is not played by human player, i.e. it's only a token on board, it's also marked as
     * out of game. True indicates that a human player is playing this character and he is
     * not yet lost.
     */
    private boolean isInGame;

    /**
     * 
     * @param token
     * @param isInGame
     */
    public Player(Character token, boolean isInGame, Position pos) {

        this.token = token;
        // initialise the position as the default position on board
        this.position = pos;

        // playerID is set to from 1 to 6
//        this.playerID = token.ordinal() + 1;
        this.isInGame = isInGame;
        cards = new ArrayList<>();
    }
    
    public List<Card> getCards() {
        return cards;
    }

    /**
     * This method draws a card for the player.
     * 
     * @param card
     */
    public void drawACard(Card card) {
        cards.add(card);
    }

    /**
     * This method kicks a player out of game if he is lost.
     */
    public void setInOrOut(boolean in) {
        this.isInGame = in;
    }

    /**
     * 
     * @return
     */
    public boolean isInGame() {
        return isInGame;
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


    
    

//    /**
//     * 
//     * @param board
//     */
//    public void moveNorth(Board board, int steps) {
//        if (!board.CanMoveNorth(position, steps)) {
//            throw new GameError("Player " + playerID + "cannot move north.");
//        }
//        position = board.northTile(position, steps);
//    }
//
//    /**
//     * 
//     * @param board
//     */
//    public void moveEast(Board board, int steps) {
//        if (!board.CanMoveEast(position, steps)) {
//            throw new GameError("Player " + playerID + "cannot move east.");
//        }
//        position = board.eastTile(position, steps);
//    }
//
//    /**
//     * 
//     * @param board
//     */
//    public void moveSouth(Board board, int steps) {
//        if (!board.CanMoveSouth(position, steps)) {
//            throw new GameError("Player " + playerID + "cannot move south.");
//        }
//        position = board.southTile(position, steps);
//    }
//
//    /**
//     * 
//     * @param board
//     */
//    public void moveWest(Board board, int steps) {
//        if (!board.CanMoveWest(position, steps)) {
//            throw new GameError("Player " + playerID + "cannot move west.");
//        }
//        position = board.westTile(position, steps);
//    }
//
//    /**
//     * This method moves a player into the given room.
//     * 
//     * 
//     * NB: this method should only be used when a player moves into a room whether from a
//     * tile or a room with secret passage.
//     * 
//     * 
//     * @param position
//     */
//    public void moveIntoRoom(Room room) {
//        if (position instanceof Tile && !room.canEnterFromPosition((Tile) position)) {
//            throw new GameError("cannot move into " + room.toString());
//        }
//
//        if (position instanceof Room && ((Room) position).getSecPas() != room.getRoom()) {
//            throw new GameError("cannot move into " + room.toString());
//        }
//
//        position = room;
//
//    }

}
