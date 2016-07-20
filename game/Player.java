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
    private final int playerID;
    private Position position;
    private List<Card> cards;
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
    public Player(Character token, boolean isInGame) {

        this.token = token;
        // initialise the position as the default position on board
        this.position = new Tile(token.startPosX, token.startPosY);

        // playerID is set to from 1 to 6
        this.playerID = token.ordinal() + 1;
        this.isInGame = isInGame;
        cards = new ArrayList<>();
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
    public void setPlayerOut() {
        isInGame = false;
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
     * @param board
     */
    public void moveNorth(Board board) {
        if (!board.CanMoveNorth(position)) {
            throw new GameError("Player " + playerID + "cannot move north.");
        }
        position = board.northTile(position);
    }

    /**
     * 
     * @param board
     */
    public void moveEast(Board board) {
        if (!board.CanMoveEast(position)) {
            throw new GameError("Player " + playerID + "cannot move east.");
        }
        position = board.eastTile(position);
    }

    /**
     * 
     * @param board
     */
    public void moveSouth(Board board) {
        if (!board.CanMoveSouth(position)) {
            throw new GameError("Player " + playerID + "cannot move south.");
        }
        position = board.southTile(position);
    }

    /**
     * 
     * @param board
     */
    public void moveWest(Board board) {
        if (!board.CanMoveWest(position)) {
            throw new GameError("Player " + playerID + "cannot move west.");
        }
        position = board.westTile(position);
    }

    /**
     * This method moves a player into the given room.
     * 
     * 
     * NB: this method should only be used when a player moves into a room whether from a
     * tile or a room with secret passage.
     * 
     * 
     * @param position
     */
    public void moveIntoRoom(Room room) {
        if (position instanceof Tile && !room.canEnterFromPosition((Tile) position)) {
            throw new GameError("cannot move into " + room.toString());
        }

        if (position instanceof Room && ((Room) position).getSecPas() != room.getRoom()) {
            throw new GameError("cannot move into " + room.toString());
        }

        position = room;

    }

}
