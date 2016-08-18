package game;

import java.util.List;

import tile.Entrance;
import tile.Position;
import tile.Room;
import tile.Tile;
import card.Character;
import configs.Configs;

/**
 * This class represents a Cluedo game board.
 * 
 * @author Hector
 *
 */
public class Board {

    // board is created as a 2D array of positions
    private Position[][] board;

    // six starting tiles for each character
    private static Tile scarletStart;
    private static Tile mustardStart;
    private static Tile whiteStart;
    private static Tile greenStart;
    private static Tile peacockStart;
    private static Tile plumStart;

    /**
     * Construct a board.
     */
    public Board() {

        String boardString = Configs.BOARD_STRING;
        int height = Configs.BOARD_HEIGHT;
        int width = Configs.BOARD_WIDTH;

        board = new Position[height][width];
        int index = 0; // index to track chars
        int x, y; // coordinates

        while (index < boardString.length()) {
            x = index % (width + 1);
            y = index / (width + 1);

            // skip the '\n' character
            if (x == width) {
                index++;
                continue;
            }

            // probably the longest switch in my life...
            switch (boardString.charAt(index)) {

            // walkable tiles, tiles that are out of all rooms
            case '0':
                board[y][x] = new Tile(x, y);
                break;

            // ' ' (space) represents walls and unenterable tiles
            case ' ':
                board[y][x] = null;
                break;

            // 1-9 represents nine rooms on board.
            case '1':
                board[y][x] = Configs.KITCHEN;
                break;
            case '2':
                board[y][x] = Configs.BALL_ROOM;
                break;
            case '3':
                board[y][x] = Configs.CONSERVATORY;
                break;
            case '4':
                board[y][x] = Configs.BILLARD_ROOM;
                break;
            case '5':
                board[y][x] = Configs.LIBRARY;
                break;
            case '6':
                board[y][x] = Configs.STUDY;
                break;
            case '7':
                board[y][x] = Configs.HALL;
                break;
            case '8':
                board[y][x] = Configs.LOUNGE;
                break;
            case '9':
                board[y][x] = Configs.DINING_ROOM;
                break;

            /*
             * '!', '@', '#', '$', '%', '^' (shift + 1-6) represents six starting tiles
             * for player tokens. '!' indicates Scarlet's start position, '@' for mustard,
             * and so on.
             */
            case '!':
                scarletStart = new Tile(x, y);
                board[y][x] = scarletStart;
                break;
            case '@':
                mustardStart = new Tile(x, y);
                board[y][x] = mustardStart;
                break;
            case '#':
                whiteStart = new Tile(x, y);
                board[y][x] = whiteStart;
                break;
            case '$':
                greenStart = new Tile(x, y);
                board[y][x] = greenStart;
                break;
            case '%':
                peacockStart = new Tile(x, y);
                board[y][x] = peacockStart;
                break;
            case '^':
                plumStart = new Tile(x, y);
                board[y][x] = plumStart;
                break;

            /*
             * 'a' - 'i' represents entrance to each room, 'a' is entrance to room '1',
             * 'b' to room '2', and so on.
             */
            case 'a':
                Entrance entrToKitchen = new Entrance(x, y, Configs.KITCHEN);
                board[y][x] = entrToKitchen;
                Configs.KITCHEN.addEntrances(entrToKitchen);
                break;
            case 'b':
                Entrance entrToBall = new Entrance(x, y, Configs.BALL_ROOM);
                board[y][x] = entrToBall;
                Configs.BALL_ROOM.addEntrances(entrToBall);
                break;
            case 'c':
                Entrance entrToCSVY = new Entrance(x, y, Configs.CONSERVATORY);
                board[y][x] = entrToCSVY;
                Configs.CONSERVATORY.addEntrances(entrToCSVY);
                break;
            case 'd':
                Entrance entrToBLDR = new Entrance(x, y, Configs.BILLARD_ROOM);
                board[y][x] = entrToBLDR;
                Configs.BILLARD_ROOM.addEntrances(entrToBLDR);
                break;
            case 'e':
                Entrance entrToLBRY = new Entrance(x, y, Configs.LIBRARY);
                board[y][x] = entrToLBRY;
                Configs.LIBRARY.addEntrances(entrToLBRY);
                break;
            case 'f':
                Entrance entrToSTDY = new Entrance(x, y, Configs.STUDY);
                board[y][x] = entrToSTDY;
                Configs.STUDY.addEntrances(entrToSTDY);
                break;
            case 'g':
                Entrance entrToHall = new Entrance(x, y, Configs.HALL);
                board[y][x] = entrToHall;
                Configs.HALL.addEntrances(entrToHall);
                break;
            case 'h':
                Entrance entrToLounge = new Entrance(x, y, Configs.LOUNGE);
                board[y][x] = entrToLounge;
                Configs.LOUNGE.addEntrances(entrToLounge);
                break;
            case 'i':
                Entrance entrToDining = new Entrance(x, y, Configs.DINING_ROOM);
                board[y][x] = entrToDining;
                Configs.DINING_ROOM.addEntrances(entrToDining);
                break;

            /*
             * 'A' - 'I' represents decorative tiles in each room, which has no
             * functionality on board, only indicates where to draw player token and
             * weapon token. 'A' marks where to draw in room '1', 'b' to room '2', and so
             * on.
             */
            case 'A':
                Tile decoTile_A = new Tile(x, y);
                board[y][x] = decoTile_A;
                Configs.KITCHEN.addDecoTiles(decoTile_A);
                break;
            case 'B':
                Tile decoTile_B = new Tile(x, y);
                board[y][x] = decoTile_B;
                Configs.BALL_ROOM.addDecoTiles(decoTile_B);
                break;
            case 'C':
                Tile decoTile_C = new Tile(x, y);
                board[y][x] = decoTile_C;
                Configs.CONSERVATORY.addDecoTiles(decoTile_C);
                break;
            case 'D':
                Tile decoTile_D = new Tile(x, y);
                board[y][x] = decoTile_D;
                Configs.BILLARD_ROOM.addDecoTiles(decoTile_D);
                break;
            case 'E':
                Tile decoTile_E = new Tile(x, y);
                board[y][x] = decoTile_E;
                Configs.LIBRARY.addDecoTiles(decoTile_E);
                break;
            case 'F':
                Tile decoTile_F = new Tile(x, y);
                board[y][x] = decoTile_F;
                Configs.STUDY.addDecoTiles(decoTile_F);
                break;
            case 'G':
                Tile decoTile_G = new Tile(x, y);
                board[y][x] = decoTile_G;
                Configs.HALL.addDecoTiles(decoTile_G);
                break;
            case 'H':
                Tile decoTile_H = new Tile(x, y);
                board[y][x] = decoTile_H;
                Configs.LOUNGE.addDecoTiles(decoTile_H);
                break;
            case 'I':
                Tile decoTile_I = new Tile(x, y);
                board[y][x] = decoTile_I;
                Configs.DINING_ROOM.addDecoTiles(decoTile_I);
                break;

            default:
            }
            index++;
        }
    }

    /**
     * Get the Position at given coordinate
     * 
     * @param x
     *            --- the horizontal coordinate
     * @param y
     *            --- the vertical coordinate
     * @return --- the Position at given coordinate
     */
    public Position getPosition(int x, int y) {
        return board[y][x];
    }

    /**
     * get the start position of given character.
     * 
     * @param character
     *            --- the character
     * @return --- the start position of this character
     */
    public Tile getStartPosition(Character character) {
        switch (character) {
        case Miss_Scarlet:
            return scarletStart;
        case Colonel_Mustard:
            return mustardStart;
        case Mrs_White:
            return whiteStart;
        case The_Reverend_Green:
            return greenStart;
        case Mrs_Peacock:
            return peacockStart;
        case Professor_Plum:
            return plumStart;
        default:
            return null; // dead code
        }
    }

    /**
     * Let the player look at north, see which *Tile* or *Entrance* is there. Note that
     * the player cannot see a room even if a room is at north. Also note that the player
     * cannot see anything at north if he is standing inside a room.
     * 
     * @param player
     *            --- the player
     * @return --- the Tile or Entrance at north, one step away, if there is one. Or null
     *         if there is a wall or nothing. If the player is in a room, this method will
     *         always return null.
     */
    public Tile lookNorth(Player player) {
        Position playerPos = player.getPosition();
        // Player can only look north out of rooms
        if (playerPos instanceof Room) {
            return null;
        }
        Tile playerTile = (Tile) playerPos;
        // boundary check
        if (playerTile.y - 1 < 0) {
            return null;
        }
        // this method should not return a Room
        if (board[playerTile.y - 1][playerTile.x] instanceof Room) {
            return null;
        }
        // only return a Tile or a Entrance
        return (Tile) board[playerTile.y - 1][playerTile.x];
    }

    /**
     * Let the player look at south, see which *Tile* or *Entrance* is there. Note that
     * the player cannot see a room even if a room is at south. Also note that the player
     * cannot see anything at south if he is standing inside a room.
     * 
     * @param player
     *            --- the player
     * @return --- the Tile or Entrance at south, one step away, if there is one. Or null
     *         if there is a wall or nothing. If the player is in a room, this method will
     *         always return null.
     */
    public Tile lookSouth(Player player) {
        Position playerPos = player.getPosition();
        // Player can only look south out of rooms
        if (playerPos instanceof Room) {
            return null;
        }
        Tile playerTile = (Tile) playerPos;
        // boundary check
        if (playerTile.y + 1 > Configs.BOARD_HEIGHT - 1) {
            return null;
        }
        // this method should not return a Room
        if (board[playerTile.y + 1][playerTile.x] instanceof Room) {
            return null;
        }
        // only return a Tile or a Entrance
        return (Tile) board[playerTile.y + 1][playerTile.x];
    }

    /**
     * Let the player look at east, see which *Tile* or *Entrance* is there. Note that the
     * player cannot see a room even if a room is at east. Also note that the player
     * cannot see anything at east if he is standing inside a room.
     * 
     * @param player
     *            --- the player
     * @return --- the Tile or Entrance at east, one step away, if there is one. Or null
     *         if there is a wall or nothing. If the player is in a room, this method will
     *         always return null.
     */
    public Tile lookEast(Player player) {
        Position playerPos = player.getPosition();
        // Player can only look east out of rooms
        if (playerPos instanceof Room) {
            return null;
        }
        Tile playerTile = (Tile) playerPos;
        // boundary check
        if (playerTile.x + 1 > Configs.BOARD_WIDTH - 1) {
            return null;
        }
        // this method should not return a Room
        if (board[playerTile.y][playerTile.x + 1] instanceof Room) {
            return null;
        }
        // only return a Tile or a Entrance
        return (Tile) board[playerTile.y][playerTile.x + 1];
    }

    /**
     * Let the player look at west, see which *Tile* or *Entrance* is there. Note that the
     * player cannot see a room even if a room is at west. Also note that the player
     * cannot see anything at west if he is standing inside a room.
     * 
     * @param player
     *            --- the player
     * @return --- the Tile or Entrance at west, one step away, if there is one. Or null
     *         if there is a wall or nothing. If the player is in a room, this method will
     *         always return null.
     */
    public Tile lookWest(Player player) {
        Position playerPos = player.getPosition();
        // Player can only look west out of rooms
        if (playerPos instanceof Room) {
            return null;
        }
        Tile playerTile = (Tile) playerPos;
        // boundary check
        if (playerTile.x - 1 < 0) {
            return null;
        }
        // this method should not return a Room
        if (board[playerTile.y][playerTile.x - 1] instanceof Room) {
            return null;
        }
        // only return a Tile or a Entrance
        return (Tile) board[playerTile.y][playerTile.x - 1];
    }

    /**
     * Let the player look around, see whether he/she is standing at an entrance to a
     * room.
     * 
     * @param player
     *            --- the player
     * @return --- the room that can enter within one step, if there is one. Or null if
     *         there isn't. If the player is in a room, this method will always return
     *         null.
     */
    public Room atEntranceTo(Player player) {
        Position playerPos = player.getPosition();
        if (playerPos instanceof Entrance) {
            Entrance entrance = (Entrance) playerPos;
            return entrance.toRoom();
        } else {
            return null;
        }
    }

    /**
     * Let the player look for exits of the room that he / she is standing in. Note that
     * if the player is not standing in a room, this method will always return null.
     * 
     * @param player
     *            --- the player
     * @return --- all exits of current room as a list. If the player is not standing in a
     *         room, this method will always return null.
     */
    public List<Entrance> lookForExit(Player player) {
        Position playerPos = player.getPosition();
        if (playerPos instanceof Room) {
            Room room = (Room) playerPos;
            return room.getEntrances();
        } else {
            return null;
        }
    }

    /**
     * Let the player look for secret passage to another room if he / she is standing in a
     * room. Note that if the player is not standing in a room, this method will always
     * return null.
     * 
     * @param player
     *            --- the player
     * @return --- the end of the secret passage if there is one in current room. If not,
     *         this method return null. Or, if the player is not standing in a room, this
     *         method also returns null.
     */
    public Room lookForSecPas(Player player) {
        Position playerPos = player.getPosition();
        if (playerPos instanceof Room) {
            Room room = (Room) playerPos;
            if (room.hasSecPas()) {
                return Configs.getRoom(room.getSecPasTo());
            }
        }
        return null;
    }

    /**
     * This method set player's position to another given position, which could be any
     * position on board. In other worlds, this method is not always a reasonable move in
     * normal Cluedo game.<br>
     * <br>
     * Note that this method does no sanity checks, so it should be always guarded by
     * calling lookNorth / lookSouth / lookWest / lookEast / atEntrance / lookForExit in
     * advance.
     * 
     * @param player
     *            --- the player
     * @param position
     *            --- where to move to
     */
    public void movePlayer(Player player, Position position) {
        player.setPosition(position);
    }

    /**
     * This method moves a weapon token to another room.
     * 
     * @param weaponToken
     *            --- the weapon token
     * @param room
     *            --- where to move to
     */
    public void moveWeapon(WeaponToken weaponToken, Room room) {
        weaponToken.setRoom(room);
    }
}