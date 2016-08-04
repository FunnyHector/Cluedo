package game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import card.Location;
import card.Weapon;
import rules.StandardCluedo;
import tile.Entrance;
import tile.Position;
import tile.Room;
import tile.Tile;
import card.Card;
import card.Character;

/**
 * This class represents all status of a Cluedo game.
 *
 * @author Hector
 *
 */
public class Game {

    private Suggestion solution;
    private Board board;
    private int numPlayers;

    private List<Player> players;
    private List<Card> remainingCards;
    private List<WeaponToken> weaponTokens;
    private Character currentPlayer;
    private Character winner;

    // a random number generator
    private static final Random RAN = new Random();

    // a StringBuilder to manipulate strings
    private static StringBuilder BOARD_STRING = new StringBuilder();

    /**
     *
     * @param numPlayer
     */
    public Game(int numPlayers) {

        board = new Board(StandardCluedo.BOARD_STRING);
        players = new ArrayList<>(Character.values().length);
        this.numPlayers = numPlayers;
        currentPlayer = Character.Miss_Scarlet;
        winner = null;

        // first add and six dummy tokens on board
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

        // put six weapons in random rooms
        setWeapons();
    }

    public List<Character> getPlayableCharacters() {
        List<Character> playableCharacters = new ArrayList<>();
        for (Player p : players) {
            if (!p.isPlaying()) {
                playableCharacters.add(p.getToken());
            }
        }

        playableCharacters.sort((c1, c2) -> c1.ordinal() - c2.ordinal());

        return playableCharacters;
    }

    public void joinPlayer(Character playerChoice) {
        players.get(playerChoice.ordinal()).setPlaying(true);
    }

    public void kickPlayerOut(Character playerChoice) {
        players.get(playerChoice.ordinal()).setPlaying(false);
        numPlayers--;
    }

    /**
     * This method randomly choose a character, a room, and a weapon to create a solution,
     * then shuffles all remaining cards, and deal them evenly to all players.
     *
     *
     */
    public void creatSolutionAndDealCards() {

        remainingCards = new ArrayList<>();

        List<Character> characterCards = new ArrayList<>(
                Arrays.asList(Character.values()));
        Character solCharacter = characterCards
                .remove(RAN.nextInt(characterCards.size()));
        remainingCards.addAll(characterCards);

        // get a random location card as solution, put the other cards into card
        // pile.
        List<Location> locationCards = new ArrayList<>(Arrays.asList(Location.values()));
        Location solLocation = locationCards.remove(RAN.nextInt(locationCards.size()));
        remainingCards.addAll(locationCards);

        // get a random weapon card as solution, put the other cards into card
        // pile.
        List<Weapon> weaponCards = new ArrayList<>(Arrays.asList(Weapon.values()));
        Weapon solWeapon = weaponCards.remove(RAN.nextInt(weaponCards.size()));
        remainingCards.addAll(weaponCards);

        // create a solution
        solution = new Suggestion(solCharacter, solLocation, solWeapon);

        // now deal cards evenly to all players
        while (remainingCards.size() >= numPlayers) {
            Collections.shuffle(remainingCards);

            for (Player p : players) {
                if (p.isPlaying()) {
                    p.drawACard(
                            remainingCards.remove(RAN.nextInt(remainingCards.size())));
                }
            }
        }
    }

    private void setWeapons() {

        weaponTokens = new ArrayList<>();
        // six weapons
        List<Weapon> weaponList = new ArrayList<>(Arrays.asList(Weapon.values()));

        // nine rooms
        List<Room> roomList = new ArrayList<>(Arrays.asList(StandardCluedo.KITCHEN,
                StandardCluedo.BALL_ROOM, StandardCluedo.CONSERVATORY,
                StandardCluedo.BILLARD_ROOM, StandardCluedo.LIBRARY, StandardCluedo.STUDY,
                StandardCluedo.HALL, StandardCluedo.LOUNGE, StandardCluedo.DINING_ROOM));

        // randomly distribute weapons
        for (int i = 0; i < 6; i++) {
            int weaponNo = RAN.nextInt(weaponList.size());
            int roomNo = RAN.nextInt(roomList.size());
            WeaponToken weaponToken = new WeaponToken(weaponList.remove(weaponNo),
                    roomList.remove(roomNo));
            weaponTokens.add(weaponToken);
        }
    }

    public void movePlayer(Character character, Position position) {
        board.movePlayer(getPlayerByCharacter(character), position);
    }

    public void moveWeapon(Weapon weapon, Room room) {
        for (WeaponToken wt : weaponTokens) {
            if (wt.getToken().equals(weapon)) {
                board.moveWeapon(wt, room);
            }
        }
    }

    public List<Card> getRemainingCards() {
        return remainingCards;
    }

    public Character getCurrentPlayer() {
        Player player = getPlayerByCharacter(currentPlayer);

        while (!player.isPlaying()) {
            currentPlayer = currentPlayer.nextCharacter();
            player = getPlayerByCharacter(currentPlayer);
        }

        return currentPlayer;
    }

    private Player getPlayerByCharacter(Character cha) {
        return players.get(cha.ordinal());
    }

    public void currentPlayerEndTurn() {
        currentPlayer = currentPlayer.nextCharacter();
    }

    public boolean playerHasCard(Character character, Card card) {
        return getPlayerByCharacter(character).hasCard(card);
    }

    public boolean playerHasCard(Character player) {
        return !getPlayerByCharacter(player).getCards().isEmpty();
    }

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
     * Roll two dices, gives a random number between 2 to 12
     *
     * @return
     */
    public int rollDice(Character character) {
        // two dices can roll out 2 - 12;
        int roll = RAN.nextInt(11) + 2;
        getPlayerByCharacter(character).setRemainingSteps(roll);
        return roll;
    }

    public int getRemainingSteps(Character character) {
        return getPlayerByCharacter(character).getRemainingSteps();
    }

    public void setRemainingSteps(Character character, int remainingSteps) {
        getPlayerByCharacter(character).setRemainingSteps(remainingSteps);
    }

    public Position getPlayerPosition(Character character) {
        return getPlayerByCharacter(character).getPosition();
    }

    // @formatter:off
    /**
     * The Position in the list returned will be of a certain order, which is:
     * 
     * north tile
     * east tile
     * south tile
     * west tile
     * room if standing at an entrance
     * exits (entrances) if in a room
     * room which can go to via the secret passage of current room
     * 
     * 
     * 
     * 
     * @param player
     * @return
     */
    // @formatter:off
    public List<Position> getMovablePositions(Character character) {
        
        Player player = getPlayerByCharacter(character);
        
        // Position playerPos = player.getPosition();
        List<Position> movablePos = new ArrayList<>();

        // if there are TILEs in four directions
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
                if (nextPos instanceof Tile && nextPos.equals(existingPlayer.getPosition())) {
                    itr.remove();
                }
            }
        }

        return movablePos;
    }

    public boolean updateAndgetGameStatus() {
        return numPlayers > 1 || winner == null;
    }
    
    public void setWinner(Character character) {
        winner = character;
    }
    
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
    
    public boolean checkAccusation(Suggestion suggestion) {
        return solution.equals(suggestion);
    }

    public String getBoardString() {
        
        // first clear
        BOARD_STRING.delete(0, BOARD_STRING.length());
        
        BOARD_STRING.append("=======Game Board=======\n");
        
        // get the canvas first
        char[] boardChars = StandardCluedo.UI_STRING_A.toCharArray();

        // draw players by replacing his character on his position
        for (Player p : players) {
            Position pos = p.getPosition();
            if (pos instanceof Tile) {
                Tile tile = (Tile) pos;
                int index = tile.x + tile.y * 25;
                boardChars[index] = p.getToken().toStringOnBoard();
            } else if (pos instanceof Room) {
                Room room = (Room) pos;
                Tile decoTile = room.getNextDecoTile();
                int index = decoTile.x + decoTile.y * 25;
                while (boardChars[index] != ' ') {
                    decoTile = room.getNextDecoTile();
                    index = decoTile.x + decoTile.y * 25;
                }
                boardChars[index] = p.getToken().toStringOnBoard();
            }
        }

        // draw the weapon tokens by replacing its character on his position
        for (WeaponToken w : weaponTokens) {
            Room room = w.getRoom();
            Tile decoTile = room.getNextDecoTile();
            int index = decoTile.x + decoTile.y * 25;
            while (boardChars[index] != ' ') {
                decoTile = room.getNextDecoTile();
                index = decoTile.x + decoTile.y * 25;
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

}
