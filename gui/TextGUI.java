package gui;

import java.util.List;
import java.util.Scanner;

import card.Card;
import card.Character;
import card.Location;
import card.Weapon;
import configs.CluedoConfigs;
import game.Game;
import game.Suggestion;
import tile.Position;
import tile.Room;

public class TextGUI {

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
        int numPlayers = parseInt(CluedoConfigs.MIN_PLAYER, CluedoConfigs.MAX_PLAYER);

        Game game = new Game(numPlayers);

        // let players choose which character to play with
        // int playerID = 0;
        int playerIndex = 0;
        while (playerIndex != numPlayers) {
            playerIndex++;
            // list all choosable cards
            if (playerIndex == 1) {
                System.out.println("Please choose the 1st character:");
            } else if (playerIndex == 2) {
                System.out.println("Please choose the 2nd character:");
            } else if (playerIndex == 3) {
                System.out.println("Please choose the 3rd character:");
            } else {
                System.out.println("Please choose the " + playerIndex + "th character:");
            }

            List<Character> playableCharacters = game.getPlayableCharacters();
            int size = playableCharacters.size();
            for (int i = 0; i < size; i++) {
                System.out.println(
                        "" + (i + 1) + ". " + playableCharacters.get(i).toString());
            }
            // make a choice
            int choice = parseInt(1, size);

            // join this player in
            game.joinPlayer(playableCharacters.get(choice - 1));
            choice = 0;
        }

        // create solution, and deal cards
        game.creatSolutionAndDealCards();

        return game;
    }

    private static void runGame(Game game) {

        while (game.isGameRunning()) {
            // print board
            System.out.println(game.getBoardString());
            // prompt possible moves, and player choose to make move
            go(game);
        }
    }

    private static void go(Game game) {

        Character currentPlayer = game.getCurrentPlayer();
        int remainingSteps = game.getRemainingSteps(currentPlayer);

        System.out.println(currentPlayer.toString() + "'s move.");

        // if this player hasn't roll a dice
        if (remainingSteps == 0) {
            int roll = game.rollDice(currentPlayer);
            System.out.println("You rolled " + roll + ".");
            remainingSteps = game.getRemainingSteps(currentPlayer);
        }

        System.out.println("You have " + remainingSteps + " steps left.");

        Position currentPos = game.getPlayerPosition(currentPlayer);
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
                rejectSuggestion(game, currentPlayer, suggestion);

                // prompt if the player want to make accusation now
                System.out.println("Do you want to make an accusation now?");
                System.out.println("1. Yes");
                System.out.println("2. No");
                int yesNo = parseInt(1, 2);

                if (yesNo == 1) {
                    // made an accusation
                    makeAccusation(game, currentPlayer);
                }

                remainingSteps = 0;

            } else {
                // move to another tile, remainingSteps--
                remainingSteps--;
            }

        } else if (choice == movablePos.size() + 1) {
            // made an accusation
            makeAccusation(game, currentPlayer);
            remainingSteps = 0;
        } else if (choice == movablePos.size() + 2) {

            if (hasSuggestionOption && !hasNowhereToGo) {
                // made a suggestion
                Suggestion suggestion = makeSuggestion(game, currentPos);
                // now compare the suggestion, and other players try to reject it
                rejectSuggestion(game, currentPlayer, suggestion);

                // prompt if the player want to make accusation now
                System.out.println("Do you want to make an accusation now?");
                System.out.println("1. Yes");
                System.out.println("2. No");
                int yesNo = parseInt(1, 2);

                if (yesNo == 1) {
                    // made an accusation
                    makeAccusation(game, currentPlayer);
                }

                remainingSteps = 0;

            } else if (!hasSuggestionOption && hasNowhereToGo) {
                // has nowhere to go, the player choose to end turn
                remainingSteps = 0;
            }

        } else if (choice == movablePos.size() + 3) {
            // has nowhere to go, the player choose to end turn
            remainingSteps = 0;
        }

        game.setRemainingSteps(currentPlayer, remainingSteps);

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

        System.out.println(suspect.toString() + " commited crime with:");

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
        game.movePlayer(suspect, (Room) destination);

        // now the player has made a suggestion
        System.out.println(
                "Your suggestion is:\nSuspect: " + suspect.toString() + "\nWeapon: "
                        + weapon.toString() + "\nLocation: " + location.toString());

        return new Suggestion(suspect, location, weapon);
    }

    private static void rejectSuggestion(Game game, Character currentPlayer,
            Suggestion suggestion) {

        for (Character c : Character.values()) {
            // as long as this player has drawn cards, he can attempt to reject;
            if (c != currentPlayer && !game.playerHandEmpty(c)) {
                Card rejectedCard = game.playerRejectSuggestion(c, suggestion);
                if (rejectedCard != null) {
                    System.out
                            .println(c.toString() + " rejects your suggestion with card: "
                                    + rejectedCard.toString());
                } else {
                    // this player cannot reject this suggestion
                    System.out.println(c.toString() + " cannot reject your suggestion.");
                }
            }
        }
    }

    private static void makeAccusation(Game game, Character currentPlayer) {

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

        System.out.println(suspect.toString() + " commited crime with:");

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
            game.kickPlayerOut(currentPlayer);
        }
    }

    private static void gameStop(Game game) {
        // TODO set game stop, prompt the winner
        // prompt do you want to play again blahblah

        Character winner = game.getWinner();

        System.out.println("Winner is Player " + winner.toString() + "!");

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

}
