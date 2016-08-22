package test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.junit.Test;

import card.Character;
import card.Location;
import card.Weapon;
import configs.Configs;
import game.Game;
import game.Suggestion;
import tile.Entrance;
import tile.Position;
import tile.Tile;
import view.token.WeaponToken;

public class GameRunTest {

    /**
     * Test that players are properly cycled.
     */
    @Test
    public void playerCycling() {
        for (int numPlayers = Configs.MIN_PLAYER; numPlayers <= Configs.MAX_PLAYER; numPlayers++) {
            Game game = new Game(numPlayers, Configs.NUM_DICE);

            game.joinPlayer(Character.Colonel_Mustard, "");
            game.joinPlayer(Character.The_Reverend_Green, "");
            game.joinPlayer(Character.Professor_Plum, "");

            game.creatSolution();
            game.dealCard();
            game.decideWhoMoveFirst();

            assertEquals("should be Colonel Mustard's turn", game.getCurrentPlayer(),
                    Character.Colonel_Mustard);
            game.currentPlayerEndTurn();
            assertEquals("should be Colonel Mustard's turn", game.getCurrentPlayer(),
                    Character.The_Reverend_Green);
            game.currentPlayerEndTurn();
            assertEquals("should be Colonel Mustard's turn", game.getCurrentPlayer(),
                    Character.Professor_Plum);
            game.currentPlayerEndTurn();
            assertEquals("should be Colonel Mustard's turn", game.getCurrentPlayer(),
                    Character.Colonel_Mustard);
        }
    }

    /**
     * Test that the player get correct options for movement if standing in a room
     */
    @Test
    public void moveOptionInRoom() {
        for (int numPlayers = Configs.MIN_PLAYER; numPlayers <= Configs.MAX_PLAYER; numPlayers++) {

            for (Character c : Character.values()) {
                Game game = new Game(numPlayers, Configs.NUM_DICE);

                for (int i = 0; i < numPlayers; i++) {
                    game.joinPlayer(Character.values()[i], "");
                }

                game.creatSolution();
                game.dealCard();
                game.decideWhoMoveFirst();

                // ============== test lounge ===============

                // put the player in lounge
                game.movePlayer(c, Configs.LOUNGE);
                List<Position> positions = game.getMovablePositions(c);
                int size = positions.size();
                System.out.println(size);
                // secret passage
                if (!positions.contains(Configs.CONSERVATORY)) {
                    fail("Should be able to go to CONSERVATORY");
                }
                // exit
                Entrance exit = new Entrance(6, 18, Configs.LOUNGE);
                if (!positions.contains(exit)) {
                    fail("Should be able to exit lounge");
                }
                assertEquals("In lounge there should be two positions to move to", 2,
                        positions.size());

                // put another player at the entrance, that should block one position
                game.movePlayer(c.nextCharacter(), exit);

                positions = game.getMovablePositions(c);
                // secret passage
                if (!positions.contains(Configs.CONSERVATORY)) {
                    fail("Should be able to go to CONSERVATORY");
                }
                // exit
                if (positions.contains(exit)) {
                    fail("Should not be able to exit lounge, someone is blocking");
                }
                assertEquals("In lounge there should be two positions to move to", 1,
                        positions.size());

                // ==================try ball room again=================

                // put the player in lounge
                game.movePlayer(c, Configs.BALL_ROOM);
                positions = game.getMovablePositions(c);
                // no secret passage, choose a room to test
                if (positions.contains(Configs.HALL)) {
                    fail("Should not be able to go to HALL");
                }
                // there are 4 exits in ball room
                Entrance exit_1 = new Entrance(7, 5, Configs.BALL_ROOM);
                Entrance exit_2 = new Entrance(9, 8, Configs.BALL_ROOM);
                Entrance exit_3 = new Entrance(14, 8, Configs.BALL_ROOM);
                Entrance exit_4 = new Entrance(16, 5, Configs.BALL_ROOM);
                if (!positions.contains(exit_1)) {
                    fail("Should be able to exit lounge from (7,5)");
                }
                if (!positions.contains(exit_2)) {
                    fail("Should be able to exit lounge from (9,8)");
                }
                if (!positions.contains(exit_3)) {
                    fail("Should be able to exit lounge from (14,8)");
                }
                if (!positions.contains(exit_4)) {
                    fail("Should be able to exit lounge from (16,5)");
                }
                assertEquals("In BALL ROOM there should be 4 positions to move to", 4,
                        positions.size());

                // put another player at the entrance, that should block one position
                game.movePlayer(c.nextCharacter(), exit_1);
                positions = game.getMovablePositions(c);
                // exit
                if (positions.contains(exit_1)) {
                    fail("Should not be able to exit lounge from (7,5), someone is blocking");
                }
                assertEquals(
                        "In BALL ROOM, with one blocked exit, there should be 3 positions to move to",
                        3, positions.size());

                // put another player at the entrance, AGAIN, to block another position
                game.movePlayer(c.nextCharacter().nextCharacter(), exit_2);
                positions = game.getMovablePositions(c);
                // exit
                if (positions.contains(exit_2)) {
                    fail("Should not be able to exit lounge from (9,8), someone is blocking");
                }
                assertEquals(
                        "In lounge, with 2 blocked exit, there should be 2 positions to move to",
                        2, positions.size());
            }
        }
    }

    /**
     * Test that the player get correct options for movement if standing on a normal tile,
     * not entrance.
     */
    @Test
    public void moveOptionOnTile() {
        for (int numPlayers = Configs.MIN_PLAYER; numPlayers <= Configs.MAX_PLAYER; numPlayers++) {

            for (Character c : Character.values()) {
                Game game = new Game(numPlayers, Configs.NUM_DICE);

                for (int i = 0; i < numPlayers; i++) {
                    game.joinPlayer(Character.values()[i], "");
                }

                game.creatSolution();
                game.dealCard();
                game.decideWhoMoveFirst();

                // put the player on a tile
                Tile testTile = new Tile(16, 13);
                Tile northTile = new Tile(16, 12);
                Tile southTile = new Tile(16, 14);
                Tile eastTile = new Tile(17, 13);
                Tile westTile = new Tile(15, 13);
                game.movePlayer(c, testTile);
                List<Position> positions = game.getMovablePositions(c);

                // test four directions
                for (Tile t : Arrays.asList(northTile, southTile, eastTile, westTile)) {
                    if (!positions.contains(t)) {
                        fail("Should be able to go to" + t.toString());
                    }
                }
                assertEquals("There should be 4 positions to move to", 4,
                        positions.size());

                // put another player at north, that should block one position
                game.movePlayer(c.nextCharacter(), northTile);
                positions = game.getMovablePositions(c);
                if (positions.contains(northTile)) {
                    fail("Should not be able to go to" + northTile.toString());
                }
                for (Tile t : Arrays.asList(southTile, eastTile, westTile)) {
                    if (!positions.contains(t)) {
                        fail("Should be able to go to" + t.toString());
                    }
                }
                assertEquals("There should be 3 positions to move to", 3,
                        positions.size());
            }
        }
    }

    /**
     * Test that the player get correct options for movement if standing on a entrance
     */
    @Test
    public void moveOptionOnEntrance() {
        for (int numPlayers = Configs.MIN_PLAYER; numPlayers <= Configs.MAX_PLAYER; numPlayers++) {

            for (Character c : Character.values()) {
                Game game = new Game(numPlayers, Configs.NUM_DICE);

                for (int i = 0; i < numPlayers; i++) {
                    game.joinPlayer(Character.values()[i], "");
                }

                game.creatSolution();
                game.dealCard();
                game.decideWhoMoveFirst();

                // ============== test kitchen's entrance ===============

                // put the player in kitchen
                Entrance entrToKit = new Entrance(4, 7, Configs.KITCHEN);
                Tile southTile = new Tile(4, 8);
                Tile eastTile = new Tile(5, 7);
                Tile westTile = new Tile(3, 7);
                game.movePlayer(c, entrToKit);
                List<Position> positions = game.getMovablePositions(c);

                // test 3 directions
                for (Tile t : Arrays.asList(southTile, eastTile, westTile)) {
                    if (!positions.contains(t)) {
                        fail("Should be able to go to" + t.toString());
                    }
                }
                // enter kitchen
                if (!positions.contains(Configs.KITCHEN)) {
                    fail("Should be able to go into KITCHEN");
                }
                assertEquals("In lounge there should be 4 positions to move to", 4,
                        positions.size());

                // put another player in kitchen, that should block nothing
                game.movePlayer(c.nextCharacter(), Configs.KITCHEN);
                positions = game.getMovablePositions(c);
                // test 3 directions
                for (Tile t : Arrays.asList(southTile, eastTile, westTile)) {
                    if (!positions.contains(t)) {
                        fail("Should be able to go to" + t.toString());
                    }
                }
                // enter kitchen
                if (!positions.contains(Configs.KITCHEN)) {
                    fail("Should be able to go into KITCHEN");
                }
                assertEquals("In lounge there should be 4 positions to move to", 4,
                        positions.size());
            }
        }
    }

    /**
     * Test that moves is processed properly
     */
    @Test
    public void validMove() {
        for (int numPlayers = Configs.MIN_PLAYER; numPlayers <= Configs.MAX_PLAYER; numPlayers++) {
            Game game = new Game(numPlayers, Configs.NUM_DICE);
            for (int i = 0; i < numPlayers; i++) {
                game.joinPlayer(Character.values()[i], "");
            }
            game.creatSolution();
            game.dealCard();
            game.decideWhoMoveFirst();
            Character currentPlayer = game.getCurrentPlayer();

            // put the player on a boundary tile
            Tile testTile = new Tile(0, 7);
            Tile eastTile = new Tile(1, 7);

            game.movePlayer(currentPlayer, testTile);
            List<Position> positions = game.getMovablePositions(currentPlayer);

            if (!positions.contains(eastTile)) {
                fail("Should be able to go to" + eastTile.toString());
            }

            assertEquals("There should be 1 positions to move to", 1, positions.size());
        }

    }

    /**
     * Test that the game won't prompt options to let player go out of board.
     */
    @Test
    public void boardBoundary() {
        for (int numPlayers = Configs.MIN_PLAYER; numPlayers <= Configs.MAX_PLAYER; numPlayers++) {
            Game game = new Game(numPlayers, Configs.NUM_DICE);
            for (int i = 0; i < numPlayers; i++) {
                game.joinPlayer(Character.values()[i], "");
            }
            game.creatSolution();
            game.dealCard();
            game.decideWhoMoveFirst();
            Character currentPlayer = game.getCurrentPlayer();

            // put the player on a boundary tile
            Tile testTile = new Tile(0, 7);
            Tile northTile = new Tile(0, 6);
            Tile southTile = new Tile(0, 8);

            game.movePlayer(currentPlayer, testTile);
            List<Position> positions = game.getMovablePositions(currentPlayer);

            if (positions.contains(northTile) || positions.contains(southTile)) {
                fail("Should not go out of boundary");
            }

            assertEquals("There should be 1 positions to move to", 1, positions.size());
        }
    }

    /**
     * Test that the player won't ghost through walls into a room
     */
    @Test
    public void ghostThroughWall() {
        for (int numPlayers = Configs.MIN_PLAYER; numPlayers <= Configs.MAX_PLAYER; numPlayers++) {
            Game game = new Game(numPlayers, Configs.NUM_DICE);
            for (int i = 0; i < numPlayers; i++) {
                game.joinPlayer(Character.values()[i], "");
            }
            game.creatSolution();
            game.dealCard();
            game.decideWhoMoveFirst();
            Character currentPlayer = game.getCurrentPlayer();

            // put the player on a boundary tile
            Tile testTile = new Tile(6, 3);

            game.movePlayer(currentPlayer, testTile);
            List<Position> positions = game.getMovablePositions(currentPlayer);

            if (positions.contains(Configs.KITCHEN)) {
                fail("Should be able to ghost through wall to kitchen");
            }
            assertEquals("There should be 3 positions to move to", 3, positions.size());
        }
    }

    /**
     * Test that the player who made a correct accusation wins
     */
    @Test
    public void correctAccusationWins() {
        for (int numPlayers = Configs.MIN_PLAYER; numPlayers <= Configs.MAX_PLAYER; numPlayers++) {
            Game game = new Game(numPlayers, Configs.NUM_DICE);
            for (int i = 0; i < numPlayers; i++) {
                game.joinPlayer(Character.values()[i], "");
            }
            game.creatSolution();
            game.dealCard();
            game.decideWhoMoveFirst();

            // skips several times, use different character for testing
            Random ran = new Random();
            int skips = ran.nextInt(20);
            for (int i = 0; i < skips; i++) {
                game.currentPlayerEndTurn();
            }

            Character currentPlayer = game.getCurrentPlayer();
            Suggestion solution = game.getSolution();
            game.checkAccusation(solution);

            assertFalse("After a correct accusation, the game should not be running",
                    game.isGameRunning());
            assertEquals("Winner should be " + currentPlayer.toString(), currentPlayer,
                    game.getWinner());
        }
    }

    /**
     * Test that an incorrect accusation kicks the player out
     */
    @Test
    public void wrongAccuserOut() {
        for (int numPlayers = Configs.MIN_PLAYER; numPlayers <= Configs.MAX_PLAYER; numPlayers++) {
            Game game = new Game(numPlayers, Configs.NUM_DICE);
            for (int i = 0; i < numPlayers; i++) {
                game.joinPlayer(Character.values()[i], "");
            }
            game.creatSolution();
            game.dealCard();
            game.decideWhoMoveFirst();

            // skips several times, use different character for testing
            Random ran = new Random();
            int skips = ran.nextInt(20);
            for (int i = 0; i < skips; i++) {
                game.currentPlayerEndTurn();
            }

            Character currentPlayer = game.getCurrentPlayer();
            Suggestion wrongAccusation = getWrongAccusation(game);

            game.checkAccusation(wrongAccusation);

            assertTrue("The game should be running", game.isGameRunning());
            assertEquals("There should be no winner" + currentPlayer.toString(),
                    game.getWinner(), null);
            assertFalse("The player should not be playing any more.",
                    game.getPlayerByCharacter(currentPlayer).isPlaying());
        }
    }

    /**
     * The only one player left will automatically become winner
     */
    @Test
    public void lastSurvivorWin() {
        for (int numPlayers = Configs.MIN_PLAYER; numPlayers <= Configs.MAX_PLAYER; numPlayers++) {
            Game game = new Game(numPlayers, Configs.NUM_DICE);
            for (int i = 0; i < numPlayers; i++) {
                game.joinPlayer(Character.values()[i], "");
            }
            game.creatSolution();
            game.dealCard();
            game.decideWhoMoveFirst();

            // let others make wrong accusation
            for (int i = 0; i < numPlayers - 1; i++) {
                Character currentPlayer = game.getCurrentPlayer();
                Suggestion wrongAccusation = getWrongAccusation(game);
                assertTrue("The game should be running", game.isGameRunning());
                assertEquals("There should be no winner" + currentPlayer.toString(),
                        game.getWinner(), null);

                game.checkAccusation(wrongAccusation);
                assertFalse("The player should not be playing any more.",
                        game.getPlayerByCharacter(currentPlayer).isPlaying());

                game.currentPlayerEndTurn();
            }

            Character currentPlayer = game.getCurrentPlayer();
            assertFalse("The game should not be running", game.isGameRunning());
            assertEquals("Winner should be " + currentPlayer.toString(), game.getWinner(),
                    currentPlayer);
        }
    }

    @Test
    public void suggestionMovesCharacterAndWeapon() {
        for (int numPlayers = Configs.MIN_PLAYER; numPlayers <= Configs.MAX_PLAYER; numPlayers++) {
            Game game = new Game(numPlayers, Configs.NUM_DICE);
            for (int i = 0; i < numPlayers; i++) {
                game.joinPlayer(Character.values()[i], "");
            }
            game.creatSolution();
            game.dealCard();
            game.decideWhoMoveFirst();

            // skips several times, use different character for testing
            Random ran = new Random();
            int skips = ran.nextInt(20);
            for (int i = 0; i < skips; i++) {
                game.currentPlayerEndTurn();
            }

            Character c = Character.values()[ran.nextInt(Character.values().length)];
            Weapon w = Weapon.values()[ran.nextInt(Weapon.values().length)];
            Location l = Location.values()[ran.nextInt(Location.values().length)];
            Suggestion randomAccusation = new Suggestion(c, w, l);

            game.moveTokensInvolvedInSuggestion(randomAccusation);

            for (Character ch : Character.values()) {
                if (ch == c) {
                    if (!game.getPlayerByCharacter(ch).getPosition()
                            .equals(Configs.getRoom(l))) {
                        fail("Player mentioned in suggestion should be moved in the mentioned room");
                    }
                }
            }

            for (WeaponToken wt : game.getWeaponTokens()) {
                if (wt.getWeapon() == w) {
                    if (!Configs.getRoom(wt.getRoomTile().getRoom())
                            .equals(Configs.getRoom(l))) {
                        fail("weapon mentioned in suggestion should be moved in the mentioned room");
                    }
                }
            }
        }
    }

    /**
     * Helper method to get a wrong accusation.
     * 
     * @param game
     *            --- the running game
     * @return --- a wrong suggestion (accusation)
     */
    private Suggestion getWrongAccusation(Game game) {
        Random ran = new Random();

        Suggestion solution = game.getSolution();

        Character c = Character.values()[ran.nextInt(Character.values().length)];
        Weapon w = Weapon.values()[ran.nextInt(Weapon.values().length)];
        Location l = Location.values()[ran.nextInt(Location.values().length)];
        Suggestion wrongAccusation = new Suggestion(c, w, l);

        // make sure this is a wrong accusation
        while (wrongAccusation.equals(solution)) {
            c = Character.values()[ran.nextInt(Character.values().length)];
            w = Weapon.values()[ran.nextInt(Weapon.values().length)];
            l = Location.values()[ran.nextInt(Location.values().length)];
            wrongAccusation = new Suggestion(c, w, l);
        }

        return wrongAccusation;
    }

}
