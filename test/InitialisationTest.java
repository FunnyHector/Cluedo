package test;

import org.junit.*;

import card.Card;
import card.Character;
import card.Location;
import card.Weapon;
import configs.Configs;
import game.Game;
import game.GameError;
import game.Player;
import tile.Tile;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class InitialisationTest {

    /**
     * Test that the number of players is properly initialised, and players are added
     */
    @Test
    public void numPlayers() {
        for (int numPlayers = Configs.MIN_PLAYER; numPlayers <= Configs.MAX_PLAYER; numPlayers++) {
            Game game = new Game(numPlayers, Configs.NUM_DICE);

            for (int i = 0; i < numPlayers; i++) {
                Character c = Character.values()[i];
                game.joinPlayer(c, "");
            }

            int count = 0;
            for (Character c : Character.values()) {
                if (game.getPlayerByCharacter(c).isPlaying()) {
                    count++;
                }
            }

            assertEquals("Wrong number of players", numPlayers, count);
        }
    }

    /**
     * Test that the game cannot construct an invalid tile
     */
    @Test
    public void validTile() {

        int[] xCoords = { -1, -5, 5, Configs.BOARD_WIDTH, Configs.BOARD_WIDTH + 1 };
        int[] yCoords = { 5, -5, -1, Configs.BOARD_WIDTH, Configs.BOARD_WIDTH + 1 };

        for (int i = 0; i < xCoords.length; i++) {
            try {
                @SuppressWarnings("unused")
                Tile tile = new Tile(xCoords[i], yCoords[i]);
                fail("invalid coordinates to construct tiles");
            } catch (GameError e) {
            }
        }
    }

    /**
     * Test that the game should roll a reasonable number
     */
    @Test
    public void validDiceRoll() {
        Game game = new Game(3, Configs.NUM_DICE);
        game.joinPlayer(Character.Miss_Scarlet, "");
        game.joinPlayer(Character.Mrs_White, "");
        game.joinPlayer(Character.Mrs_Peacock, "");
        game.creatSolution();
        game.dealCard();
        game.decideWhoMoveFirst();

        int[] diceRoll;
        // let's roll...100 times...
        for (int i = 0; i < 100; i++) {
            diceRoll = game.rollDice(game.getCurrentPlayer());
            int total = 0;
            for (int j = 0; j < diceRoll.length; j++) {
                total += (diceRoll[j] + 1);
            }
            if (total < Configs.NUM_DICE || total > Configs.NUM_DICE * 6) {
                fail("Strange dice!!!");
            }
        }
    }

    /**
     * Test that choosing character is properly done, and those who were not chosen should
     * not be playing
     */
    @Test
    public void choosingCharacter() {
        for (int numPlayers = Configs.MIN_PLAYER; numPlayers <= Configs.MAX_PLAYER; numPlayers++) {
            Game game = new Game(numPlayers, Configs.NUM_DICE);

            game.joinPlayer(Character.Miss_Scarlet, "");
            game.joinPlayer(Character.Mrs_White, "");
            game.joinPlayer(Character.Mrs_Peacock, "");

            assertTrue("Miss Scarlet should be playing",
                    game.getPlayerByCharacter(Character.Miss_Scarlet).isPlaying());
            assertFalse("Colonel Mustard should not be playing",
                    game.getPlayerByCharacter(Character.Colonel_Mustard).isPlaying());
            assertTrue("Mrs White should be playing",
                    game.getPlayerByCharacter(Character.Mrs_White).isPlaying());
            assertFalse("The Reverend Green should not be playing",
                    game.getPlayerByCharacter(Character.The_Reverend_Green).isPlaying());
            assertTrue("Mrs Peacock should be playing",
                    game.getPlayerByCharacter(Character.Mrs_Peacock).isPlaying());
            assertFalse("Professor Plum should not be playing",
                    game.getPlayerByCharacter(Character.Professor_Plum).isPlaying());
        }
    }

    /**
     * Test that the solution is properly created, and solution cards won't go into
     * player's hand.
     */
    @Test
    public void creatSolution() {
        for (int numPlayers = Configs.MIN_PLAYER; numPlayers <= Configs.MAX_PLAYER; numPlayers++) {
            Game game = new Game(numPlayers, Configs.NUM_DICE);

            List<Player> players = new ArrayList<>();

            for (int i = 0; i < numPlayers; i++) {
                Character c = Character.values()[i];
                game.joinPlayer(c, "");
                players.add(game.getPlayerByCharacter(c));
            }

            game.creatSolution();

            boolean hasCharacter = false, hasWeapon = false, hasLocation = false;
            for (Card c : game.getSolution().asList()) {
                if (c instanceof Character) {
                    hasCharacter = true;
                } else if (c instanceof Weapon) {
                    hasWeapon = true;
                } else if (c instanceof Location) {
                    hasLocation = true;
                }
            }

            assertTrue("Should have one Character, one Weapon, and one Location.",
                    hasCharacter && hasWeapon && hasLocation);

            game.dealCard();

            for (Player p : players) {
                for (Card c : game.getSolution().asList()) {
                    if (p.getCards().contains(c)) {
                        fail("Player's hand should not have any card in solution");
                    }
                }
            }
        }
    }

    /**
     * Test that the cards are dealt evenly and without duplicates, and if there are
     * leftover cards after dealing, the number of leftover cards should be correct.
     */
    @Test
    public void dealingCards() {
        for (int numPlayers = Configs.MIN_PLAYER; numPlayers <= Configs.MAX_PLAYER; numPlayers++) {
            Game game = new Game(numPlayers, Configs.NUM_DICE);

            List<Player> players = new ArrayList<>();

            for (int i = 0; i < numPlayers; i++) {
                Character c = Character.values()[i];
                game.joinPlayer(c, "");
                players.add(game.getPlayerByCharacter(c));
            }

            game.creatSolution();
            game.dealCard();

            int cardInHand = -1;
            int cardleft = -1;
            switch (numPlayers) {
            case 3:
                cardInHand = 6;
                cardleft = 0;
                break;
            case 4:
                cardInHand = 4;
                cardleft = 2;
                break;
            case 5:
                cardInHand = 3;
                cardleft = 3;
                break;
            case 6:
                cardInHand = 3;
                cardleft = 0;
                break;
            default:
            }

            List<Card> allCards = new ArrayList<>();

            for (Player p : players) {
                List<Card> hand = p.getCards();
                assertEquals("Each of three players should have six cards", hand.size(),
                        cardInHand);
                allCards.addAll(hand);
            }

            for (int i = 0; i < allCards.size() - 1; i++) {
                if (allCards.get(i).equals(allCards.get(i + 1))) {
                    fail("Player should not have duplicate cards");
                }
            }

            if (!game.getRemainingCards().isEmpty()) {
                assertEquals("Number of remaining cards is wrong", cardleft,
                        game.getRemainingCards().size());
            }
        }
    }

    /**
     * test that players won't get same cards
     */
    @Test
    public void noDuplicateCards() {
        for (int numPlayers = Configs.MIN_PLAYER; numPlayers <= Configs.MAX_PLAYER; numPlayers++) {
            Game game = new Game(numPlayers, Configs.NUM_DICE);
            List<Player> players = new ArrayList<>();
            Set<Card> cardPile = new HashSet<>();
            for (int i = 0; i < numPlayers; i++) {
                Character c = Character.values()[i];
                game.joinPlayer(c, "");
                players.add(game.getPlayerByCharacter(c));
            }
            game.creatSolution();
            game.dealCard();

            for (Player p : players) {
                List<Card> hand = p.getCards();
                cardPile.addAll(hand);
            }

            cardPile.addAll(game.getRemainingCards());

            assertEquals("There should be 18 cards", cardPile.size(), 18);

        }
    }

}
