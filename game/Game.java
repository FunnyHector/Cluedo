package game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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

    /*
     * a map to manage players, the key is player id. key being 0 indicates the player is
     * not controled by anybody, i.e. a dummy token on board
     */
    private Map<Player, Integer> players;
    private List<Card> remainingCards;
    private List<WeaponToken> weaponTokens;
    private int currentPlayerID;

    // a random number generator
    private static final Random RAN = new Random();

    /**
     *
     * @param numPlayer
     */
    public Game(int numPlayers, int boardType) {

        board = new Board(StandardCluedo.BOARD_STRING);
        players = new HashMap<>();
        this.numPlayers = numPlayers;
        currentPlayerID = 1;

        // first add and six dummy tokens on board
        players.put(new Player(Character.Miss_Scarlet, false,
                board.getStartPosition(Character.Miss_Scarlet)), new Integer(0));
        players.put(
                new Player(Character.Colonel_Mustard, false,
                        board.getStartPosition(Character.Colonel_Mustard)),
                new Integer(0));
        players.put(new Player(Character.Mrs_White, false,
                board.getStartPosition(Character.Mrs_White)), new Integer(0));
        players.put(
                new Player(Character.The_Reverend_Green, false,
                        board.getStartPosition(Character.The_Reverend_Green)),
                new Integer(0));
        players.put(new Player(Character.Mrs_Peacock, false,
                board.getStartPosition(Character.Mrs_Peacock)), new Integer(0));
        players.put(
                new Player(Character.Professor_Plum, false,
                        board.getStartPosition(Character.Professor_Plum)),
                new Integer(0));

        // put six weapons in random rooms
        setWeapons();

        // if (boardType == 1) {

        // } else {
        // System.out.println("Beta version doesn't support user-customised board.");
        // System.exit(0);
        // }
    }

    public List<Character> getPlayableCharacters() {
        List<Character> playableCharacters = new ArrayList<>();
        for (Entry<Player, Integer> en : players.entrySet()) {
            if (en.getValue() == 0) {
                playableCharacters.add(en.getKey().getToken());
            }
        }

        playableCharacters.sort((c1, c2) -> c1.ordinal() - c2.ordinal());

        return playableCharacters;
    }

    public void joinPlayer(int playerID, Character playerChoice) {

        Player p = null;

        for (Entry<Player, Integer> en : players.entrySet()) {
            if (en.getKey().getToken().equals(playerChoice)) {
                p = en.getKey();
                break;
            }
        }

        players.put(p, new Integer(playerID));
    }

    public void playerOut(int playerID) {
        for (Entry<Player, Integer> en : players.entrySet()) {
            if (en.getValue() == playerID) {
                en.getKey().setInOrOut(false);
                numPlayers--;
                break;
            }
        }
    }

    // public void addPlayers(int numPlayer) {
    // players = new ArrayList<>(numPlayer);
    // players.add(new Player(Character.Miss_Scarlet, false,
    // board.getStartPosition(Character.Miss_Scarlet)));
    // players.add(new Player(Character.Colonel_Mustard, false,
    // board.getStartPosition(Character.Colonel_Mustard)));
    // players.add(new Player(Character.Mrs_White, false,
    // board.getStartPosition(Character.Mrs_White)));
    // players.add(new Player(Character.The_Reverend_Green, false,
    // board.getStartPosition(Character.The_Reverend_Green)));
    // players.add(new Player(Character.Mrs_Peacock, false,
    // board.getStartPosition(Character.Mrs_Peacock)));
    // players.add(new Player(Character.Professor_Plum, false,
    // board.getStartPosition(Character.Professor_Plum)));
    // }

    /**
     * This method randomly choose a character, a room, and a weapon to create a solution,
     * then shuffles all remaining cards, and deal them evenly to all players.
     *
     *
     */
    public void creatSolutionAndDealCards() {

        remainingCards = new ArrayList<>();

        // get a random character card as solution, put the other cards into
        // card pile.
        // List<Character> characterCards = new ArrayList<>(Character.values());

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

            for (Entry<Player, Integer> en : players.entrySet()) {
                if (en.getValue() != 0) {
                    en.getKey().drawACard(
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

    public List<Card> getRemainingCards() {
        return remainingCards;
    }

    public Player getCurrentPlayer() {
        for (Entry<Player, Integer> en : players.entrySet()) {
            if (en.getValue() == currentPlayerID) {
                return en.getKey();
            }
        }

        return null; // dead code
    }

    public void currentPlayerEndTurn() {
        currentPlayerID++;
        if (currentPlayerID > numPlayers) {
            currentPlayerID = 1;
        }
    }

    // public Player getNextPlayer() {
    // for (Entry<Player, Integer> en : players.entrySet()) {
    // if (en.getValue() == currentPlayerID) {
    // currentPlayerID++;
    // if (currentPlayerID > numPlayers) {
    // currentPlayerID = 1;
    // }
    // return en.getKey();
    // }
    // }
    //
    // return null; // dead code
    // }

    public int getCurrentPlayerID() {
        return currentPlayerID;
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
    public List<Position> getMovablePositions(Player player) {
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
        for (Player existingPlayer : players.keySet()) {
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

    /**
     * Roll two dices, gives a random number between 2 to 12
     *
     * @return
     */
    public int rollDice(Player player) {
        // two dices can roll out 2 - 12;
        int roll = RAN.nextInt(11) + 2;
        player.setRemainingSteps(roll);
        return roll;
    }

    /**
     *
     * @return
     */
    public Suggestion getSolution() {
        return solution;
    }
    
    public Board getBoard() {
        return board;
    }

    public boolean updateAndgetGameStatus() {
        return numPlayers > 1;
    }

    // public List<Player> getPlayers() {
    // return players;
    // }

    public String getBoardString() {

        // get the canvas first
        char[] boardChars = StandardCluedo.UI_STRING_A.toCharArray();

        // draw players by replacing his character on his position
        for (Player p : players.keySet()) {
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

        String boardString = String.valueOf(boardChars);

        // put remaining cards after the ASCII board
        if (!remainingCards.isEmpty()) {
            boardString = boardString + "[Remaining cards]:\n";
            for (Card c : remainingCards) {
                boardString = boardString + c.toString() + "\n";
            }
        }
        
        // shows what cards are in the current player's hand
        Player player = null;
        for (Entry<Player, Integer> en : players.entrySet()) {
            if (en.getValue() == currentPlayerID) {
                player = en.getKey();
            }
        }
        
        boardString = boardString + "[Cards in hand]:\n";
        List<Card> cards = player.getCards();
        for (Card c : player.getCards()) {
            boardString = boardString + c.toString() + "\n";
        }

        return boardString;
    }

}
