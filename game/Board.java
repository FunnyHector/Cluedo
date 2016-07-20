package game;

import java.util.Arrays;

import card.Location;
import tile.Position;
import tile.Room;
import tile.Tile;

/**
 * This class represents a Cluedo game board.
 * 
 * @author Hector
 *
 */
public class Board {

    // nine rooms are created as static fields
    private static final Room KITCHEN = new Room(Location.Kitchen, Location.Study,
            Arrays.asList(new Tile(4, 7)));
    private static final Room BALL_ROOM = new Room(Location.Ball_room, null, Arrays
            .asList(new Tile(7, 5), new Tile(9, 8), new Tile(14, 8), new Tile(16, 5)));
    private static final Room CONSERVATORY = new Room(Location.Conservatory,
            Location.Lounge, Arrays.asList(new Tile(18, 5)));
    private static final Room BILLARD_ROOM = new Room(Location.Billard_Room, null,
            Arrays.asList(new Tile(17, 9), new Tile(22, 13)));
    private static final Room LIBRARY = new Room(Location.Library, null,
            Arrays.asList(new Tile(20, 13), new Tile(16, 16)));
    private static final Room STUDY = new Room(Location.Study, Location.Kitchen,
            Arrays.asList(new Tile(17, 20)));
    private static final Room HALL = new Room(Location.Hall, null,
            Arrays.asList(new Tile(11, 17), new Tile(12, 17), new Tile(15, 20)));
    private static final Room LOUNGE = new Room(Location.Lounge, Location.Conservatory,
            Arrays.asList(new Tile(6, 18)));
    private static final Room DINING_ROOM = new Room(Location.Dining_Room, null,
            Arrays.asList(new Tile(8, 12), new Tile(6, 16)));

    // board is created as a 2d array of positions
    private Position[][] postions;

    // @formatter:off
    // a string used to print out text-based UI
    public static final String UI_STRING = 
            "■■■■■■■■■ ■■■■ ■■■■■■■■■\n" +
            "┌────□■   ┌──┐   ■┌────┐\n" + 
            "│KITC│  ┌─┘  └─┐  │CSTY│\n" +
            "│HEN │  │ BALL │  │    │\n" + 
            "│    │  │ ROOM │  ↑│  ┌┘\n" +
            "└┐   │  →      ←   └──□■\n" + 
            "■└──↑┘  │      │        \n" +
            "        └↑────↑┘       ■\n" + 
            "■                 ┌────┐\n" +
            "┌───┐             →BILL│\n" + 
            "│   └──┐  ┌───┐   │ARD │\n" +
            "│DINING│  │CRI│   │    │\n" + 
            "│ROOM  ←  │ME │   └───↑┘\n" +
            "│      │  │SCE│        ■\n" + 
            "│      │  │NE │   ┌─↓─┐■\n" +
            "└─────↑┘  │   │  ┌┘LIB└┐\n" + 
            "■         └───┘  → RARY│\n" +
            "                 └┐   ┌┘\n" + 
            "■        ┌─↓↓─┐   └───┘■\n" +
            "□─────↓  │HALL│         \n" + 
            "│LOUNG│  │    ←        ■\n" +
            "│E    │  │    │  ↓─────□\n" + 
            "│     │  │    │  │STUDY│\n" +
            "│    ┌┘  │    │  └┐    │\n" + 
            "└────┘■ ■└────┘■ ■└────┘\n";

    /*
     * a string used to construct the board. This could be defined outside of source code,
     * for instance in a .txt file, but since Cluedo is brand-registered, and the board
     * layout is not so much modifiable under those game rules, so it's easier to be put
     * just here.
     */
    private static final String BOARD_STRING = 
            "         0    0         \n" +
            "       000    000       \n" + 
            "      00        00      \n" +
            "      00        00      \n" + 
            "      00        003     \n" +
            "      002      2000     \n" + 
            "    1 00        00000000\n" +
            "00000000 2    2 0000000 \n" + 
            " 00000000000000000      \n" +
            "     00000000000004     \n" + 
            "        00     000      \n" +
            "        00     000      \n" + 
            "       900     000    4 \n" +
            "        00     00000000 \n" + 
            "        00     000  5   \n" +
            "      9 00     00       \n" + 
            " 000000000     005      \n" +
            "00000000000000000       \n" + 
            " 00000000  77  000      \n" +
            "      800      000000000\n" + 
            "       00     700000000 \n" +
            "       00      006      \n" + 
            "       00      00       \n" +
            "       00      00       \n" + 
            "       0        0       \n";
    // @formatter:on

    /**
     * 
     */
    public Board() {
        postions = new Position[24][25];
        int index = 0;
        int x, y;

        while (index < BOARD_STRING.length()) {
            x = index % 25;
            y = index / 25;

            // skip the '\n' character
            if (x == 24) {
                index++;
                continue;
            }

            switch (BOARD_STRING.charAt(index)) {
            case '0':
                postions[x][y] = new Tile(x, y);
                break;
            case '1':
                postions[x][y] = KITCHEN;
                break;
            case '2':
                postions[x][y] = BALL_ROOM;
                break;
            case '3':
                postions[x][y] = CONSERVATORY;
                break;
            case '4':
                postions[x][y] = BILLARD_ROOM;
                break;
            case '5':
                postions[x][y] = LIBRARY;
                break;
            case '6':
                postions[x][y] = STUDY;
                break;
            case '7':
                postions[x][y] = HALL;
                break;
            case '8':
                postions[x][y] = LOUNGE;
                break;
            case '9':
                postions[x][y] = DINING_ROOM;
                break;
            default:
                postions[x][y] = null;
            }
            index++;
        }

        /*
         * [DEBUG] for (y = 0; y < postions[0].length; y++) { for (x = 0; x <
         * postions.length; x++) { if (postions[x][y] == null) { System.out.print(" "); }
         * else { System.out.print(postions[x][y]); } } System.out.println(); }
         */
    }

    /**
     * 
     * @param x
     * @param y
     * @return
     */
    public Position getPosition(int x, int y) {
        return postions[x][y];
    }

    /**
     * This method tells whether a player can move towards south from a given position.
     * 
     * NB: this method cannot tell whether a player can move into a room. eg. if a player
     * is standing at a tile facing an room entrance, the result will be false if this
     * method is called.
     * 
     * @param position
     * @return
     */
    public boolean CanMoveSouth(Position position, int steps) {
        if (position instanceof Room) {
            return false;
        }

        Tile tile = (Tile) position;

        int tileY = tile.getY();
        if (tileY + steps > 24) {
            return false;
        }

        for (int i = 1; i <= steps; i++) {
            Position posSouth = postions[tile.getX()][tileY + i];
            if (posSouth == null) {
                return false;
            }
        }

        return true;
    }

    /**
     * This method returns the position on south of the given position.
     * 
     * 
     * @param tile
     * @return
     */
    public Position southTile(Position position, int steps) {
        if (!(position instanceof Tile)) {
            throw new GameError(
                    "wrong method(Board.southTile()) call from a room object");
        }
        Tile tile = (Tile) position;
        if (tile.getY() + steps > 24) {
            throw new GameError("Out of board.");
        }
        return postions[tile.getX()][tile.getY() + steps];
    }

    /**
     * 
     * @param position
     * @return
     */
    public boolean CanMoveNorth(Position position, int steps) {
        if (position instanceof Room) {
            return false;
        }

        Tile tile = (Tile) position;

        int tileY = tile.getY();
        if (tileY - steps < 0) {
            return false;
        }

        for (int i = 1; i <= steps; i++) {
            Position posNorth = postions[tile.getX()][tileY - i];
            if (posNorth == null) {
                return false;
            }
        }

        return true;
    }

    /**
     * 
     * @param position
     * @return
     */
    public Position northTile(Position position, int steps) {
        if (!(position instanceof Tile)) {
            throw new GameError(
                    "wrong method(Board.northTile()) call from a room object");
        }
        Tile tile = (Tile) position;
        if (tile.getY() - steps < 0) {
            throw new GameError("Out of board.");
        }
        return postions[tile.getX()][tile.getY() - steps];
    }

    /**
     * 
     * @param position
     * @return
     */
    public boolean CanMoveEast(Position position, int steps) {
        if (position instanceof Room) {
            return false;
        }

        Tile tile = (Tile) position;

        int tileX = tile.getX();
        if (tileX + steps > 23) {
            return false;
        }

        for (int i = 1; i <= steps; i++) {
            Position posEast = postions[tileX + i][tile.getY()];
            if (posEast == null) {
                return false;
            }
        }

        return true;
    }

    /**
     * 
     * @param position
     * @return
     */
    public Position eastTile(Position position, int steps) {
        if (!(position instanceof Tile)) {
            throw new GameError("wrong method(Board.eastTile()) call from a room object");
        }
        Tile tile = (Tile) position;
        if (tile.getX() + steps > 23) {
            throw new GameError("Out of board.");
        }
        return postions[tile.getX() + steps][tile.getY()];
    }

    /**
     * 
     * @param position
     * @return
     */
    public boolean CanMoveWest(Position position, int steps) {
        if (position instanceof Room) {
            return false;
        }

        Tile tile = (Tile) position;

        int tileX = tile.getX();
        if (tileX - steps < 0) {
            return false;
        }

        for (int i = 1; i <= steps; i++) {
            Position posEast = postions[tileX - i][tile.getY()];
            if (posEast == null) {
                return false;
            }
        }

        return true;
    }

    /**
     * 
     * @param position
     * @return
     */
    public Position westTile(Position position, int steps) {
        if (!(position instanceof Tile)) {
            throw new GameError("wrong method(Board.westTile()) call from a room object");
        }
        Tile tile = (Tile) position;
        if (tile.getX() - steps < 0) {
            throw new GameError("Out of board.");
        }
        return postions[tile.getX() - steps][tile.getY()];
    }

    /**
     * for debug, will be deleted
     * 
     * @param args
     */
    public static void main(String[] args) {
        new Board();
    }
}
