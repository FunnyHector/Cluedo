package game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import card.Location;
import card.Weapon;
import configs.CluedoConfigs;
import tile.Entrance;
import tile.Position;
import tile.Room;
import tile.Tile;
import card.Card;
import card.Character;

/**
 * This class represents a running Cluedo game.
 *
 * @author Hector
 *
 */
public class Game {

    // the solution created at beginning
    private Suggestion solution;
    // the game board
    private Board board;
    // number of players
    private int numPlayers;
    // all six players (including dummy tokens) as a list
    private List<Player> players;
    // after cards are evenly dealt, all remaining cards are in this list.
    private List<Card> remainingCards;
    // all six weapon tokens as a list
    private List<WeaponToken> weaponTokens;
    // which character is currently acting
    private Character currentPlayer;
    // who is the winner
    private Character winner;
    // a random number generator
    private static final Random RAN = new Random();
    // a StringBuilder to manipulate strings
    private static StringBuilder BOARD_STRING = new StringBuilder();

    /**
     * Construct the game.
     * 
     * @param numPlayer
     *            --- how many (human controlled) players are playing.
     */
    public Game(int numPlayers) {
        board = new Board();
        players = new ArrayList<>(Character.values().length);
        this.numPlayers = numPlayers;
        // Miss Scarlet always roll first
        currentPlayer = Character.Miss_Scarlet;
        winner = null;

        // then add all six dummy tokens on board
        players.add(new Player(Character.Miss_Scarlet,
                board.getStartPosition(Character.Miss_Scarlet), false));
        players.add(new Player(Character.Colonel_Mustard,
                board.getStartPosition(Character.Colonel_Mustard), false));
        players.add(new Player(Character.Mrs_White,
                board.getStartPosition(Character.Mrs_White), false));
        players.add(new Player(Character.The_Reverend_Green,
                board.getStartPosition(Character.The_Reverend_Green), false));
        players.add(new Player(Character.Mrs_Peacock,
                board.getStartPosition(Character.Mrs_Peacock), false));
        players.add(new Player(Character.Professor_Plum,
                board.getStartPosition(Character.Professor_Plum), false));

        // last, put six weapons in random rooms
        setWeapons();
    }

    /**
     * This method randomly put six weapons in random rooms.
     */
    private void setWeapons() {

        weaponTokens = new ArrayList<>();
        // six weapons
        List<Weapon> weaponList = new ArrayList<>(Arrays.asList(Weapon.values()));

        // nine rooms
        List<Room> roomList = new ArrayList<>(Arrays.asList(CluedoConfigs.KITCHEN,
                CluedoConfigs.BALL_ROOM, CluedoConfigs.CONSERVATORY,
                CluedoConfigs.BILLARD_ROOM, CluedoConfigs.LIBRARY, CluedoConfigs.STUDY,
                CluedoConfigs.HALL, CluedoConfigs.LOUNGE, CluedoConfigs.DINING_ROOM));

        // randomly distribute weapons
        for (int i = 0; i < 6; i++) {
            int weaponNo = RAN.nextInt(weaponList.size());
            int roomNo = RAN.nextInt(roomList.size());
            WeaponToken weaponToken = new WeaponToken(weaponList.remove(weaponNo),
                    roomList.remove(roomNo));
            weaponTokens.add(weaponToken);
        }
    }

    /**
     * This method randomly choose one character, one room, and one weapon to create a
     * solution, then shuffles all remaining cards, and deal them evenly to all players.
     */
    public void creatSolutionAndDealCards() {
        remainingCards = new ArrayList<>();
    
        // let's get all Character cards first
        List<Character> characterCards = new ArrayList<>(
                Arrays.asList(Character.values()));
        // randomly choose one as the murderer
        Character solCharacter = characterCards
                .remove(RAN.nextInt(characterCards.size()));
        // then put the rest character cards in the card pile
        remainingCards.addAll(characterCards);
    
        // then let's get all Location cards
        List<Location> locationCards = new ArrayList<>(Arrays.asList(Location.values()));
        // randomly choose one as the crime scene
        Location solLocation = locationCards.remove(RAN.nextInt(locationCards.size()));
        // then put the rest location cards in the card pile
        remainingCards.addAll(locationCards);
    
        // then let's get all Weapon cards
        List<Weapon> weaponCards = new ArrayList<>(Arrays.asList(Weapon.values()));
        // randomly choose one as the murder weapon
        Weapon solWeapon = weaponCards.remove(RAN.nextInt(weaponCards.size()));
        // then put the rest location cards in the card pile
        remainingCards.addAll(weaponCards);
    
        // now we have a solution
        solution = new Suggestion(solCharacter, solLocation, solWeapon);
    
        // last, deal cards randomly and evenly to all players
        while (remainingCards.size() >= numPlayers) {
            Collections.shuffle(remainingCards); // MAXIMUM RANDOMNESS = ANARCHY !!
            for (Player p : players) {
                if (p.isPlaying()) {
                    p.drawACard(
                            remainingCards.remove(RAN.nextInt(remainingCards.size())));
                }
            }
        }
    }

    /**
     * Get all playable characters, i.e. those who hasn't been chosen by any player yet.
     * 
     * @return --- all playable characters as a list
     */
    public List<Character> getPlayableCharacters() {
        List<Character> playableCharacters = new ArrayList<>();

        for (Player p : players) {
            if (!p.isPlaying()) {
                // only those who hasn't been chosen
                playableCharacters.add(p.getToken());
            }
        }

        return playableCharacters;
    }

    /**
     * Set the given player as human controlled
     * 
     * @param playerChoice
     *            --- the character chosen by a player
     */
    public void joinPlayer(Character playerChoice) {
        players.get(playerChoice.ordinal()).setPlaying(true);
    }

    /**
     * Kick the given player out, so he / she cannot play any more.
     * 
     * @param character
     *            --- the character to be kicked out
     */
    public void kickPlayerOut(Character character) {
        players.get(character.ordinal()).setPlaying(false);
        numPlayers--;
        if (numPlayers == 1) {
            for (Player p : players) {
                if (p.isPlaying()) {
                    setWinner(p.getToken());
                }
            }
        }
    }

    /**
     * Get the player who need to move.
     * 
     * @return --- the current player
     */
    public Character getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * let current player end turn.
     */
    public void currentPlayerEndTurn() {
        Player player = getPlayerByCharacter(currentPlayer);
        while (!player.isPlaying()) {
            // if this character is kicked out or not controlled by a player, skip him
            currentPlayer = currentPlayer.nextCharacter();
            player = getPlayerByCharacter(currentPlayer);
        }
    }

    /**
     * Get the player's position.
     * 
     * @param character
     *            --- the player
     * @return --- the player's position
     */
    public Position getPlayerPosition(Character character) {
        return getPlayerByCharacter(character).getPosition();
    }

    /**
     * Move a character to the given position.
     * 
     * @param character
     *            --- the character to be moved
     * @param position
     *            --- where to move
     */
    public void movePlayer(Character character, Position position) {
        board.movePlayer(getPlayerByCharacter(character), position);
    }

    /**
     * Move a weapon to the given room.
     * 
     * @param weapon
     *            --- the character to be moved
     * @param room
     *            --- which room to move into.
     */
    public void moveWeapon(Weapon weapon, Room room) {
        for (WeaponToken wt : weaponTokens) {
            if (wt.getToken().equals(weapon)) {
                board.moveWeapon(wt, room);
            }
        }
    }

    /**
     * Get the remaining cards as a list. Note that the returned list could be empty if
     * all cards are dealt.
     * 
     * @return --- the remaining cards as a list
     */
    public List<Card> getRemainingCards() {
        return remainingCards;
    }

    /**
     * Get how many steps left for the player to move.
     * 
     * @param character
     *            --- the player
     * @return --- how many steps left for the player to move.
     */
    public int getRemainingSteps(Character character) {
        return getPlayerByCharacter(character).getRemainingSteps();
    }

    /**
     * Set how many steps left for the player to move.
     * 
     * @param character
     *            --- the player
     * @param remainingSteps
     *            --- how many steps left for the player to move.
     */
    public void setRemainingSteps(Character character, int remainingSteps) {
        getPlayerByCharacter(character).setRemainingSteps(remainingSteps);
    }

    /**
     * Check whether the given character has the given card in hand.
     * 
     * @param character
     *            --- the character
     * @param card
     *            --- the card
     * @return --- true if he has; false is he has not
     */
    public boolean playerHasCard(Character character, Card card) {
        return getPlayerByCharacter(character).hasCard(card);
    }

    /**
     * check whether the player's hand is empty. As long as the player is
     * human-controlled, his hand is not empty, so he can attempt to reject.
     * 
     * @param player
     *            --- the player
     * @return --- true is the player has no card; false if not.
     */
    public boolean playerHandEmpty(Character player) {
        return !getPlayerByCharacter(player).isPlaying();
    }

    /**
     * Let the player attempt to reject the given suggestion. This method will return the
     * first card that can be rejected. With the same player and the same suggestion, each
     * time the method should return different possible result, because the order of
     * checking process is random. If the player cannot reject the given suggestion, null
     * will be returned.
     * 
     * @param player
     *            --- the player
     * @param suggestion
     *            --- the suggestion to check
     * @return --- the card that can prove the suggestion wrong; or null if the player
     *         cannot reject it.
     */
    public Card playerRejectSuggestion(Character player, Suggestion suggestion) {

        List<Card> cardsInSuggetion = suggestion.asList();
        // shuffle so that it randomly reject the first rejectable card
        Collections.shuffle(cardsInSuggetion);

        for (Card card : cardsInSuggetion) {
            if (playerHasCard(player, card)) {
                return card; // only reject one card
            }
        }

        return null;
    }

    /**
     * Check whether the given suggestion is wrong.
     * 
     * @param suggestion
     *            --- the suggestion to check
     * @return --- true if it's a correct accusation; false if wrong
     */
    public boolean checkAccusation(Suggestion suggestion) {
        return solution.equals(suggestion);
    }

    /**
     * Let the player roll dices.
     *
     * @return --- the number rolled
     */
    public int rollDice(Character character) {
        // e.g. two dices can roll out 2 - 12;
        int roll = RAN.nextInt(5 * CluedoConfigs.NUM_DICE + 1) + CluedoConfigs.NUM_DICE;
        getPlayerByCharacter(character).setRemainingSteps(roll);
        return roll;
    }

    /**
     * This method checks the given character's position, and returns all possible
     * positions to move to. The positions in the list returned will be of a certain
     * order, which is: north tile -> east tile -> south tile -> west tile -> room if
     * standing at an entrance -> exits (entrances) if in a room -> room if via the secret
     * passage in current room. Any position that cannot be accessible will not be added
     * in this list. In particular, a tile on which has another player standing will not
     * be added in.<br>
     * <br>
     * This ensured order is to make the option menu more predictable.
     * 
     * @param character
     *            --- the player
     * @return --- a list of positions that are all movable.
     */
    public List<Position> getMovablePositions(Character character) {

        Player player = getPlayerByCharacter(character);

        List<Position> movablePos = new ArrayList<>();

        // if there are tiles in four directions
        if (board.lookNorth(player) != null) {
            movablePos.add(board.lookNorth(player));
        }
        if (board.lookEast(player) != null) {
            movablePos.add(board.lookEast(player));
        }
        if (board.lookSouth(player) != null) {
            movablePos.add(board.lookSouth(player));
        }
        if (board.lookWest(player) != null) {
            movablePos.add(board.lookWest(player));
        }

        // if the player is standing at an entrance to a room
        if (board.atEntranceTo(player) != null) {
            movablePos.add(board.atEntranceTo(player));
        }

        // if the player is in a room, get the exits
        List<Entrance> entrances = board.lookForExit(player);
        if (entrances != null && !entrances.isEmpty()) {
            for (Entrance e : entrances) {
                movablePos.add(e);
            }
        }

        // if the player is in a room, and there is a secret passage
        if (board.lookForSecPas(player) != null) {
            movablePos.add(board.lookForSecPas(player));
        }

        // check if any other player standing there, then it's not an option
        for (Player existingPlayer : players) {
            Iterator<Position> itr = movablePos.iterator();
            while (itr.hasNext()) {
                Position nextPos = itr.next();
                if (nextPos instanceof Tile
                        && nextPos.equals(existingPlayer.getPosition())) {
                    itr.remove();
                }
            }
        }

        return movablePos;
    }

    /**
     * Whether game has a winner (i.e. game end)
     * 
     * @return --- true if game is still running, there is no winner yet; false if not.
     */
    public boolean isGameRunning() {
        return winner == null;
    }

    /**
     * Get the winner.
     * 
     * @return --- winner
     */
    public Character getWinner() {
        if (winner == null) {
            // assertion
            if (numPlayers > 1) {
                throw new GameError("number of players shouldn't > 1");
            }
    
            for (Player p : players) {
                if (p.isPlaying()) {
                    return p.getToken();
                }
            }
    
            throw new GameError("should at least have one player left");
        } else {
            return winner;
        }
    }

    /**
     * Set a player as winner.
     * 
     * @param character
     *            --- the player
     */
    public void setWinner(Character character) {
        winner = character;
    }

    /**
     * This method returns the ASCII, text-based game board as a String. On this board,
     * players' position, weapon's position are updated.
     * 
     * @return --- the text-based game board to print in console
     */
    public String getBoardString() {

        // first clear the StringBuilder
        BOARD_STRING.delete(0, BOARD_STRING.length());

        BOARD_STRING.append("=======Game Board=======\n");

        int width = CluedoConfigs.BOARD_WIDTH + 1;

        // get the canvas first
        char[] boardChars = CluedoConfigs.UI_STRING_A.toCharArray();

        // draw players by replacing his character on his position
        for (Player p : players) {
            Position pos = p.getPosition();
            if (pos instanceof Tile) {
                // normal tile
                Tile tile = (Tile) pos;
                int index = tile.x + tile.y * width;
                boardChars[index] = p.getToken().toStringOnBoard();
            } else if (pos instanceof Room) {
                // inside a room
                Room room = (Room) pos;
                Tile decoTile = room.getNextDecoTile();
                int index = decoTile.x + decoTile.y * width;
                // find a empty space to draw the player inside the room.
                while (boardChars[index] != ' ') {
                    decoTile = room.getNextDecoTile();
                    index = decoTile.x + decoTile.y * width;
                }
                boardChars[index] = p.getToken().toStringOnBoard();
            }
        }

        // draw the weapon tokens by replacing its character on his position
        for (WeaponToken w : weaponTokens) {
            Room room = w.getRoom();
            Tile decoTile = room.getNextDecoTile();
            int index = decoTile.x + decoTile.y * width;
            while (boardChars[index] != ' ') {
                decoTile = room.getNextDecoTile();
                index = decoTile.x + decoTile.y * width;
            }
            boardChars[index] = w.getToken().toStringOnBoard();
        }

        BOARD_STRING.append(boardChars);
        BOARD_STRING.append("========================\n");

        // put remaining cards after the ASCII board
        if (!remainingCards.isEmpty()) {
            BOARD_STRING.append("[Remaining cards]:\n");
            for (Card c : remainingCards) {
                BOARD_STRING.append(c.toString());
                BOARD_STRING.append("\n");
            }
        }

        // shows what cards are in the current player's hand
        Player player = getPlayerByCharacter(currentPlayer);

        BOARD_STRING.append("[Cards in hand]:\n");
        for (Card c : player.getCards()) {
            BOARD_STRING.append(c.toString());
            BOARD_STRING.append("\n");
        }

        BOARD_STRING.append("========================\n");

        return BOARD_STRING.toString();
    }

    /**
     * A helper method to get the corresponding Player of given Character.
     * 
     * @param character
     *            --- the given character
     * @return
     */
    private Player getPlayerByCharacter(Character character) {
        return players.get(character.ordinal());
    }
}
