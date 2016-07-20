package game;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import tile.Position;

public class ClientTxt {

    private Board board;
    private static final Scanner SCANNER = new Scanner(System.in);

    // used to represent player's move directions
    private enum Direction {
        N, E, S, W
    };

    public static void main(String[] args) {

        welcomeMsg();

        setupGame();

        // TODO should check the status first, and close it at last
        SCANNER.close();
    }

    private static void welcomeMsg() {
        System.out.println("==============Cluedo text-based client v0.1==============");

        System.out.println("Some welcome message");

    }

    private static void setupGame() {

        System.out.println("How many players?");

        int numPlayer = parseInt();

    }

    private static int parseInt() {
        // TODO
        return 0;
    }

    private static void parseFreeMove(Player player, int availableSteps) {

        /*
         * this method is designed to parse a free-move input. It would be nice to support
         * free move input, eg, w5a2s1d3, can be parsed as move north 5 steps, then west 2
         * steps, then south 1 step, then east 3 steps, as long as (5 + 2 + 1 + 3 <= 12)
         * && (no wall hit) holds true.
         * 
         * 
         * The method is not complete yet. Need more logic, and perhaps testing. 
         * 
         * But it's already too long.....
         * 
         * 
         */

        String line = SCANNER.nextLine();

        int index = 0; // an index to get each char in player's input
        int totalSteps = 0; // total steps in player's input
        List<Direction> moves = new ArrayList<>(); // an ordered sequence of valid moves
        Direction lastDirection;
        Position pos = player.getPosition();

        while (index < line.length()) {
            index = skipWhiteSpace(index, line);
            char directionChar = line.charAt(index++);

            // deal with the direction character
            switch (directionChar) {
            case 'w':
            case 'W':
                lastDirection = Direction.N;
                break;
            case 'd':
            case 'D':
                lastDirection = Direction.E;
                break;
            case 's':
            case 'S':
                lastDirection = Direction.S;
                break;
            case 'a':
            case 'A':
                lastDirection = Direction.W;
                break;
            default:
                failMsg("invalid input, \"w, a, s, d\" expected at index " + (index - 1));
                return;
            }
            
            // then the steps
            index = skipWhiteSpace(index, line);
            char stepsChar = line.charAt(index++);
            int steps = 0;
            
            if (Character.isDigit(stepsChar)) {
                steps = Integer.parseInt("" + stepsChar);
            } else {
                failMsg("invalid input, digits expected at index " + (index - 1));
                return;
            }
          
            // check if the third character is still a digit
            char nextStepsChar = line.charAt(index);
            if (Character.isDigit(nextStepsChar)) {
                // this digit and the previous digit together indicates the number of steps
                steps = steps * 10 + Integer.parseInt("" + nextStepsChar);
                index++;
            }
            
            // now some sanity checks
            // 1. if the player's free input has more than available moves
            totalSteps += steps;
            if (totalSteps > availableSteps) {
                moves.clear();
                failMsg("You don't have enough moved to make, try to move no more than "
                        + availableSteps + " steps.");
                return;
            }

            // TODO need to do a simulated move, to see if the player will hit a wall
            // condition cannot be true, change it.
            // 2. if the player's free input will hit a wall
            for (int i = 0; i < steps; i++) {
                
            }
            
            
            
            if (true) {
                moves.clear();
                failMsg("Your free-move will hit a wall, try again.");
                return;
            }

        }

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
        // TODO Auto-generated method stub
        System.out.println(msg);
    }

}
