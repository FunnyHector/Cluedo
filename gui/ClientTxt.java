package gui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.Map.Entry;

import card.Card;
import card.Character;
import card.Location;
import card.Weapon;
import game.Game;
import game.Player;
import game.Suggestion;
import tile.Position;
import tile.Room;

public class ClientTxt {

    private static final int MIN_PLAYER = 3;
    private static final int MAX_PLAYER = 6;

    private static final Scanner SCANNER = new Scanner(System.in);

    public static void main(String[] args) {

        welcomeMsg();

        Game game = setupGame();

        runGame(game);

        gameStop(game);

        SCANNER.close();
    }

    private static void welcomeMsg() {
        System.out.println("==============Cluedo text-based client v0.1==============");

        System.out.println("Some welcome message");

    }

    private static Game setupGame() {

        // set how many players
        System.out.println("How many players?");
        int numPlayers = parseInt(MIN_PLAYER, MAX_PLAYER);

        int boardType = 1;
        Game game = new Game(numPlayers, boardType);

        // let players choose which character to play with
        int playerID = 0;
        while (playerID != numPlayers) {
            playerID++;
            // list all choosable cards
            System.out.println("Player " + playerID + " please choose your character:");
            List<Character> playableCharacters = game.getPlayableCharacters();
            int size = playableCharacters.size();
            for (int i = 0; i < size; i++) {
                System.out.println(
                        "" + (i + 1) + ". " + playableCharacters.get(i).toString());
            }
            // make a choice
            int choice = parseInt(1, size);

            // join this player in
            game.joinPlayer(playerID, playableCharacters.get(choice - 1));
            choice = 0;
        }

        // create solution, and deal cards
        game.creatSolutionAndDealCards();

        return game;
    }

    private static void runGame(Game game) {
        System.out.println("============Game running============");

        while (game.updateAndgetGameStatus()) {
            // print board
            System.out.println(game.getBoardString());
            // prompt possible moves, and player choose to make move
            go(game);
        }
    }

    private static void go(Game game) {

        int currentPlayerID = game.getCurrentPlayerID();
        Player currentPlayer = game.getCurrentPlayer();
        int remainingSteps = currentPlayer.getRemainingSteps();

        System.out.println("Player " + currentPlayerID + ", "
                + currentPlayer.getToken().toString() + "'s move.");

        // if this player hasn't roll a dice
        if (remainingSteps == 0) {
            int roll = game.rollDice(currentPlayer);
            System.out.println("You rolled " + roll + ".");
            remainingSteps = currentPlayer.getRemainingSteps();
        }

        System.out.println("You have " + remainingSteps + " steps left.");

        Position currentPos = currentPlayer.getPosition();
        List<Position> movablePos = game.getMovablePositions(currentPlayer);
        int menuNo = 1;

        // two helper flags
        boolean hasSuggestionOption = false;
        boolean hasNowhereToGo = false;

        // prompt options of movable positions
        for (Position destination : movablePos) {
            System.out.println("" + menuNo + ". " + currentPos.optionString(destination));
            menuNo++;
        }

        // prompt accusation option
        System.out.println("" + menuNo + ". Make accusation.");
        menuNo++;

        // prompt make suggestion if the player is in a room
        if (currentPos instanceof Room) {
            System.out.println("" + menuNo + ". Make suggestion.");
            hasSuggestionOption = true;
            menuNo++;
        }

        // means the player is blocked by other players, so he cannot move
        if (movablePos.isEmpty()) {
            System.out.println("" + menuNo + ". Nowhere to move, end turn.");
            hasNowhereToGo = true;
            menuNo++;
        }

        // get player's choice
        menuNo--;
        int choice = parseInt(1, menuNo);

        // player chose to move to one of movable positions
        if (choice <= movablePos.size()) {
            Position destination = movablePos.get(choice - 1);
            game.movePlayer(currentPlayer, destination);

            // if player has entered a room, he can make a suggestion
            if (destination instanceof Room) {
                // first update the board display
                System.out.println(game.getBoardString());
                // move into a room, now the player can make suggestion
                Suggestion suggestion = makeSuggestion(game, destination);
                // now compare the suggestion, and other players try to reject it
                rejectSuggestion(game, currentPlayerID, suggestion);

                // prompt if the player want to make accusation now
                System.out.println("Do you want to make an accusation now?");
                System.out.println("1. Yes");
                System.out.println("2. No");
                int yesNo = parseInt(1, 2);

                if (yesNo == 1) {
                    // made an accusation
                    makeAccusation(game, currentPlayerID, currentPlayer);
                }

                remainingSteps = 0;

            } else {
                // move to another tile, remainingSteps--
                remainingSteps--;
            }

        } else if (choice == movablePos.size() + 1) {
            // made an accusation
            makeAccusation(game, currentPlayerID, currentPlayer);
            remainingSteps = 0;
        } else if (choice == movablePos.size() + 2) {

            if (hasSuggestionOption && !hasNowhereToGo) {
                // made a suggestion
                Suggestion suggestion = makeSuggestion(game, currentPos);
                // now compare the suggestion, and other players try to reject it
                rejectSuggestion(game, currentPlayerID, suggestion);
                remainingSteps = 0;

            } else if (!hasSuggestionOption && hasNowhereToGo) {
                // has nowhere to go, the player choose to end turn
                remainingSteps = 0;
            }

        } else if (choice == movablePos.size() + 3) {
            // has nowhere to go, the player choose to end turn
            remainingSteps = 0;
        }

        currentPlayer.setRemainingSteps(remainingSteps);

        // if current player has no step left, it's next player's turn
        if (remainingSteps == 0) {
            game.currentPlayerEndTurn();
        }

    }

    private static Suggestion makeSuggestion(Game game, Position destination) {

        Location location = ((Room) destination).getRoom();

        System.out.println(
                "What suggestion do you want to make in " + location.toString() + "?");

        // prompt all characters
        for (Character c : Character.values()) {
            System.out.println("" + (c.ordinal() + 1) + ". " + c.toString());
        }

        int choiceCharacter = parseInt(1, Character.values().length);

        // get player's choice
        Character suspect = null;
        switch (choiceCharacter) {
        case 1:
            suspect = Character.Miss_Scarlet;
            break;
        case 2:
            suspect = Character.Colonel_Mustard;
            break;
        case 3:
            suspect = Character.Mrs_White;
            break;
        case 4:
            suspect = Character.The_Reverend_Green;
            break;
        case 5:
            suspect = Character.Mrs_Peacock;
            break;
        case 6:
            suspect = Character.Professor_Plum;
            break;
        default: // dead code
        }

        System.out.println("" + suspect.toString() + " commited crime with:");

        // prompt all weapons
        for (Weapon w : Weapon.values()) {
            System.out.println("" + (w.ordinal() + 1) + ". " + w.toString());
        }

        int choiceWeapon = parseInt(1, Weapon.values().length);

        // get player's choice
        Weapon weapon = null;
        switch (choiceWeapon) {
        case 1:
            weapon = Weapon.Candlestick;
            break;
        case 2:
            weapon = Weapon.Dagger;
            break;
        case 3:
            weapon = Weapon.Lead_Pipe;
            break;
        case 4:
            weapon = Weapon.Revolver;
            break;
        case 5:
            weapon = Weapon.Rope;
            break;
        case 6:
            weapon = Weapon.Spanner;
            break;
        default: // dead code
        }

        // move the weapon in this suggestion into this room
        game.moveWeapon(weapon, (Room) destination);
        // and the suspect as well
        game.movePlayer(game.getPlayerByCharacter(suspect), (Room) destination);

        // now the player has made a suggestion
        System.out.println(
                "Your suggestion is:\nSuspect: " + suspect.toString() + "\nWeapon: "
                        + weapon.toString() + "\nLocation: " + location.toString());

        return new Suggestion(suspect, location, weapon);
    }

    private static void rejectSuggestion(Game game, int currentPlayerID,
            Suggestion suggestion) {

        outer: for (Player p : game.getPlayers()) {
            int playerID = p.getID();
            // every other human-controlled player has to try to reject this suggestion
            if (playerID != currentPlayerID && playerID != 0) {
                List<Card> cardsInSuggetion = suggestion.asList();
                // shuffle so that it randomly reject the first rejectable card
                Collections.shuffle(cardsInSuggetion);

                for (Card card : cardsInSuggetion) {
                    if (game.playerHasCard(playerID, card)) {
                        System.out.println("Player " + playerID
                                + " rejects your suggestion with card: "
                                + card.toString());
                        continue outer; // only reject one card
                    }
                }

                // this player cannot reject this suggestion
                System.out.println(
                        "Player " + playerID + " cannot reject your suggestion.");
            }
        }
    }

    private static void makeAccusation(Game game, int currentPlayerID,
            Player currentPlayer) {

        System.out.println("What accusation do you want to make:");

        // prompt all characters
        for (Character c : Character.values()) {
            System.out.println("" + (c.ordinal() + 1) + ". " + c.toString());
        }

        int choiceCharacter = parseInt(1, Character.values().length);

        // get player's choice
        Character suspect = null;
        switch (choiceCharacter) {
        case 1:
            suspect = Character.Miss_Scarlet;
            break;
        case 2:
            suspect = Character.Colonel_Mustard;
            break;
        case 3:
            suspect = Character.Mrs_White;
            break;
        case 4:
            suspect = Character.The_Reverend_Green;
            break;
        case 5:
            suspect = Character.Mrs_Peacock;
            break;
        case 6:
            suspect = Character.Professor_Plum;
            break;
        default: // dead code
        }

        System.out.println("...commited crime with:");

        // prompt all weapons
        for (Weapon w : Weapon.values()) {
            System.out.println("" + (w.ordinal() + 1) + ". " + w.toString());
        }

        int choiceWeapon = parseInt(1, Weapon.values().length);

        // get player's choice
        Weapon weapon = null;
        switch (choiceWeapon) {
        case 1:
            weapon = Weapon.Candlestick;
            break;
        case 2:
            weapon = Weapon.Dagger;
            break;
        case 3:
            weapon = Weapon.Lead_Pipe;
            break;
        case 4:
            weapon = Weapon.Revolver;
            break;
        case 5:
            weapon = Weapon.Rope;
            break;
        case 6:
            weapon = Weapon.Spanner;
            break;
        default: // dead code
        }

        System.out.println("...in:");

        // prompt all rooms
        for (Location l : Location.values()) {
            System.out.println("" + (l.ordinal() + 1) + ". " + l.toString());
        }

        int choiceRoom = parseInt(1, Location.values().length);

        // get player's choice
        Location location = null;
        switch (choiceRoom) {
        case 1:
            location = Location.Kitchen;
            break;
        case 2:
            location = Location.Ball_room;
            break;
        case 3:
            location = Location.Conservatory;
            break;
        case 4:
            location = Location.Billard_Room;
            break;
        case 5:
            location = Location.Library;
            break;
        case 6:
            location = Location.Study;
            break;
        case 7:
            location = Location.Hall;
            break;
        case 8:
            location = Location.Lounge;
            break;
        case 9:
            location = Location.Dining_Room;
            break;
        default: // dead code
        }

        // now the player has made a accusation
        System.out.println(
                "Your accusation is:\nSuspect: " + suspect.toString() + "\nWeapon: "
                        + weapon.toString() + "\nLocation: " + location.toString());

        Suggestion accusation = new Suggestion(suspect, location, weapon);

        if (game.checkAccusation(accusation)) {
            // win!!
            System.out.println("You Win!");
            game.setWinner(currentPlayer);
        } else {
            // the player is out
            System.out.println("You are wrong! ");

            /*
             * TODO when a player is out, he cannot continue to play, but he has to be in
             * game to try to reject other player's suggestion. This
             * kickPlayerOut(currentPlayerID) method need some change
             * 
             */

            game.kickPlayerOut(currentPlayerID);
        }
    }

    private static void gameStop(Game game) {
        // TODO set game stop, prompt the winner
        // prompt do you want to play again blahblah

        Player winner = game.getWinner();

        System.out.println("Winner is Player " + winner.getID() + "!");

    }

    private static int parseInt(int min, int max) {
        while (true) {
            String line = SCANNER.nextLine();

            if (line.equals("help")) {
                helpMessage();
                continue;
            }

            try {
                int i = Integer.valueOf(line);
                if (i >= min && i <= max) {
                    return i;
                } else {
                    System.out.println(
                            "Please choose between " + min + " and " + max + ":");
                    continue;
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter an integer:");
                continue;
            }
        }
    }

    private static void helpMessage() {
        // TODO Some help message

    }

    private static int skipWhiteSpace(int index, String line) {
        int newIndex = index;
        while ((line.charAt(newIndex) == ' ' || line.charAt(newIndex) == '\t')
                && newIndex < line.length()) {
            newIndex++;
        }
        return newIndex;
    }

    private static void failMsg(String msg) {
        System.out.println(msg);
    }

    // @Deprecated
    // private static void parseFreeMove(Player player, int availableSteps) {
    //
    // /*
    // * this method is designed to parse a free-move input. It would be nice to support
    // * free move input, eg, w5a2s1d3, can be parsed as move north 5 steps, then west 2
    // * steps, then south 1 step, then east 3 steps, as long as (5 + 2 + 1 + 3 <= 12)
    // * && (no wall hit) holds true.
    // *
    // *
    // * The method is not complete yet. Need more logic, and perhaps testing.
    // *
    // * But it's already too long.....
    // *
    // *
    // */
    //
    // String line = SCANNER.nextLine();
    //
    // int index = 0; // an index to get each char in player's input
    // int totalSteps = 0; // total steps in player's input
    // List<Direction> moves = new ArrayList<>(); // an ordered sequence of valid moves
    // Direction lastDirection;
    // Position pos = player.getPosition();
    //
    // while (index < line.length()) {
    // index = skipWhiteSpace(index, line);
    // char directionChar = line.charAt(index++);
    //
    // // deal with the direction character
    // switch (directionChar) {
    // case 'w':
    // case 'W':
    // lastDirection = Direction.N;
    // break;
    // case 'd':
    // case 'D':
    // lastDirection = Direction.E;
    // break;
    // case 's':
    // case 'S':
    // lastDirection = Direction.S;
    // break;
    // case 'a':
    // case 'A':
    // lastDirection = Direction.W;
    // break;
    // default:
    // failMsg("invalid input, \"w, a, s, d\" expected at index " + (index - 1));
    // return;
    // }
    //
    // // then the steps
    // index = skipWhiteSpace(index, line);
    // char stepsChar = line.charAt(index++);
    // int steps = 0;
    //
    // if (Character.isDigit(stepsChar)) {
    // steps = Integer.parseInt("" + stepsChar);
    // } else {
    // failMsg("invalid input, digits expected at index " + (index - 1));
    // return;
    // }
    //
    // // check if the third character is still a digit
    // char nextStepsChar = line.charAt(index);
    // if (Character.isDigit(nextStepsChar)) {
    // // this digit and the previous digit together indicates the number of
    // // steps
    // steps = steps * 10 + Integer.parseInt("" + nextStepsChar);
    // index++;
    // }
    //
    // // now some sanity checks
    // // 1. if the player's free input has more than available moves
    // totalSteps += steps;
    // if (totalSteps > availableSteps) {
    // moves.clear();
    // failMsg("You don't have enough moved to make, try to move no more than "
    // + availableSteps + " steps.");
    // return;
    // }
    //
    // // need to do a simulated move, to see if the player will hit a wall
    // // condition cannot be true, change it.
    // // 2. if the player's free input will hit a wall
    // for (int i = 0; i < steps; i++) {
    //
    // }
    //
    // if (true) {
    // moves.clear();
    // failMsg("Your free-move will hit a wall, try again.");
    // return;
    // }
    //
    // }
    //
    // }

}
