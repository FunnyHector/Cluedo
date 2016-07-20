package game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import card.Location;
import card.Weapon;
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

    // a random number generator
    private static final Random RAN = new Random();

    /**
     * 
     * @param numPlayer
     */
    public Game(int numPlayer) {
        board = new Board();
        players = new ArrayList<>(numPlayer);
        // TODO add players

        // create solution, and deal cards evenly to players
        creatSolutionAndDealCards();

    }

    /**
     * This method randomly choose a character, a room, and a weapon to create a solution,
     * then shuffles all remaining cards, and deal them evenly to all players.
     * 
     * TODO if there are leftover cards, they will be visible to all players
     * 
     */
    private void creatSolutionAndDealCards() {

        List<Card> cardPile = new ArrayList<>();

        // get a random character card as solution, put the other cards into card pile.
        List<Character> characterCards = Arrays.asList(Character.values());
        Character solCharacter = characterCards
                .remove(RAN.nextInt(characterCards.size()));
        cardPile.addAll(characterCards);

        // get a random location card as solution, put the other cards into card pile.
        List<Location> locationCards = Arrays.asList(Location.values());
        Location solLocation = locationCards.remove(RAN.nextInt(locationCards.size()));
        cardPile.addAll(locationCards);

        // get a random weapon card as solution, put the other cards into card pile.
        List<Weapon> weaponCards = Arrays.asList(Weapon.values());
        Weapon solWeapon = weaponCards.remove(RAN.nextInt(weaponCards.size()));
        cardPile.addAll(weaponCards);

        // create a solution
        solution = new Suggestion(solCharacter, solLocation, solWeapon);

        // now deal cards evenly to all players
        int numPlayer = players.size();
        while (cardPile.size() >= numPlayer) {
            Collections.shuffle(cardPile);

            for (int i = 0; i < numPlayer; i++) {
                players.get(i).drawACard(cardPile.remove(RAN.nextInt(cardPile.size())));
            }
        }

        // if there are cards left, show them to all players.
        if (!cardPile.isEmpty()) {
            /*
             * TODO do something here. or do something in constructor so that the client
             * can let players know.
             */
        }

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

}
