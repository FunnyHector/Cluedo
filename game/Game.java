package game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import card.Location;
import card.Weapon;
import rules.StandardCluedo;
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
    private List<Player> players;
    private List<Card> remainingCards;
    private List<WeaponToken> weaponTokens;

    // a random number generator
    private static final Random RAN = new Random();

    /**
     * 
     * @param numPlayer
     */
    public Game(int numPlayer, int boardType) {

        if (boardType == 1) {
            board = new Board(StandardCluedo.BOARD_STRING);
            players = new ArrayList<>(numPlayer);

            // add players, and dummy tokens on board
            int numAdded = 0;
            players.add(new Player(Character.Miss_Scarlet,
                    numAdded++ < numPlayer ? true : false,
                    board.getStartPosition(Character.Miss_Scarlet)));
            players.add(new Player(Character.Colonel_Mustard,
                    numAdded++ < numPlayer ? true : false,
                    board.getStartPosition(Character.Colonel_Mustard)));
            players.add(new Player(Character.Mrs_White, 
                    numAdded++ < numPlayer ? true : false,
                    board.getStartPosition(Character.Mrs_White)));
            players.add(new Player(Character.The_Reverend_Green,
                    numAdded++ < numPlayer ? true : false,
                    board.getStartPosition(Character.The_Reverend_Green)));
            players.add(new Player(Character.Mrs_Peacock,
                    numAdded++ < numPlayer ? true : false,
                    board.getStartPosition(Character.Mrs_Peacock)));
            players.add(new Player(Character.Professor_Plum,
                    numAdded++ < numPlayer ? true : false,
                    board.getStartPosition(Character.Professor_Plum)));

            // create solution, and deal cards evenly to players
            creatSolutionAndDealCards();

            // put six weapons in random rooms
            setWeapons();

        } else {
            System.out.println("Beta version doesn't support user-customised board.");
            System.exit(0);
        }
    }

    /**
     * This method randomly choose a character, a room, and a weapon to create a solution,
     * then shuffles all remaining cards, and deal them evenly to all players.
     * 
     * 
     */
    private void creatSolutionAndDealCards() {

        remainingCards = new ArrayList<>();

        // get a random character card as solution, put the other cards into card pile.
        // List<Character> characterCards = new ArrayList<>(Character.values());
        
        List<Character> characterCards = new ArrayList<>(Arrays.asList(Character.values()));
        Character solCharacter = characterCards
                .remove(RAN.nextInt(characterCards.size()));
        remainingCards.addAll(characterCards);

        // get a random location card as solution, put the other cards into card pile.
        List<Location> locationCards = new ArrayList<>(Arrays.asList(Location.values()));
        Location solLocation = locationCards.remove(RAN.nextInt(locationCards.size()));
        remainingCards.addAll(locationCards);

        // get a random weapon card as solution, put the other cards into card pile.
        List<Weapon> weaponCards = new ArrayList<>(Arrays.asList(Weapon.values()));
        Weapon solWeapon = weaponCards.remove(RAN.nextInt(weaponCards.size()));
        remainingCards.addAll(weaponCards);

        // create a solution
        solution = new Suggestion(solCharacter, solLocation, solWeapon);

        // now deal cards evenly to all players
        int numPlayer = players.size();
        while (remainingCards.size() >= numPlayer) {
            Collections.shuffle(remainingCards);

            for (int i = 0; i < numPlayer; i++) {
                players.get(i).drawACard(
                        remainingCards.remove(RAN.nextInt(remainingCards.size())));
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

    /**
     * Roll two dices, gives a random number between 2 to 12
     * 
     * @return
     */
    public int rollDice() {
        // two dices can roll out 2 - 12;
        return RAN.nextInt(11) + 2;
    }

    /**
     * 
     * @return
     */
    public Suggestion getSolution() {
        return solution;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public String getBoardString() {

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

        return String.valueOf(boardChars);
    }

}
