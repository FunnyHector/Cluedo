package game;

import java.util.List;

import rules.StandardCluedo;
import tile.Entrance;
import tile.Position;
import tile.Room;
import tile.Tile;
import card.Character;

/**
 * This class represents a Cluedo game board.
 * 
 * @author Hector
 *
 */
public class Board {

    // board is created as a 2d array of positions
    private Position[][] board;

    // six starting tiles for each player token

    private static Tile scarletStart;
    private static Tile mustardStart;
    private static Tile whiteStart;
    private static Tile greenStart;
    private static Tile peacockStart;
    private static Tile plumStart;

    /**
     * 
     */
    public Board(String boardString) {
        board = new Position[25][24];
        int index = 0;
        int x, y;

        while (index < boardString.length()) {
            x = index % 25;
            y = index / 25;

            // skip the '\n' character
            if (x == 24) {
                index++;
                continue;
            }

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
                board[y][x] = StandardCluedo.KITCHEN;
                break;
            case '2':
                board[y][x] = StandardCluedo.BALL_ROOM;
                break;
            case '3':
                board[y][x] = StandardCluedo.CONSERVATORY;
                break;
            case '4':
                board[y][x] = StandardCluedo.BILLARD_ROOM;
                break;
            case '5':
                board[y][x] = StandardCluedo.LIBRARY;
                break;
            case '6':
                board[y][x] = StandardCluedo.STUDY;
                break;
            case '7':
                board[y][x] = StandardCluedo.HALL;
                break;
            case '8':
                board[y][x] = StandardCluedo.LOUNGE;
                break;
            case '9':
                board[y][x] = StandardCluedo.DINING_ROOM;
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
                Entrance entrToKitchen = new Entrance(x, y, StandardCluedo.KITCHEN);
                board[y][x] = entrToKitchen;
                StandardCluedo.KITCHEN.addEntrances(entrToKitchen);
                break;
            case 'b':
                Entrance entrToBall = new Entrance(x, y, StandardCluedo.BALL_ROOM);
                board[y][x] = entrToBall;
                StandardCluedo.BALL_ROOM.addEntrances(entrToBall);
                break;
            case 'c':
                Entrance entrToCSVY = new Entrance(x, y, StandardCluedo.CONSERVATORY);
                board[y][x] = entrToCSVY;
                StandardCluedo.CONSERVATORY.addEntrances(entrToCSVY);
                break;
            case 'd':
                Entrance entrToBLDR = new Entrance(x, y, StandardCluedo.BILLARD_ROOM);
                board[y][x] = entrToBLDR;
                StandardCluedo.BILLARD_ROOM.addEntrances(entrToBLDR);
                break;
            case 'e':
                Entrance entrToLBRY = new Entrance(x, y, StandardCluedo.LIBRARY);
                board[y][x] = entrToLBRY;
                StandardCluedo.LIBRARY.addEntrances(entrToLBRY);
                break;
            case 'f':
                Entrance entrToSTDY = new Entrance(x, y, StandardCluedo.STUDY);
                board[y][x] = entrToSTDY;
                StandardCluedo.STUDY.addEntrances(entrToSTDY);
                break;
            case 'g':
                Entrance entrToHall = new Entrance(x, y, StandardCluedo.HALL);
                board[y][x] = entrToHall;
                StandardCluedo.HALL.addEntrances(entrToHall);
                break;
            case 'h':
                Entrance entrToLounge = new Entrance(x, y, StandardCluedo.LOUNGE);
                board[y][x] = entrToLounge;
                StandardCluedo.LOUNGE.addEntrances(entrToLounge);
                break;
            case 'i':
                Entrance entrToDining = new Entrance(x, y, StandardCluedo.DINING_ROOM);
                board[y][x] = entrToDining;
                StandardCluedo.DINING_ROOM.addEntrances(entrToDining);
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
                StandardCluedo.KITCHEN.addDecoTiles(decoTile_A);
                break;
            case 'B':
                Tile decoTile_B = new Tile(x, y);
                board[y][x] = decoTile_B;
                StandardCluedo.BALL_ROOM.addDecoTiles(decoTile_B);
                break;
            case 'C':
                Tile decoTile_C = new Tile(x, y);
                board[y][x] = decoTile_C;
                StandardCluedo.CONSERVATORY.addDecoTiles(decoTile_C);
                break;
            case 'D':
                Tile decoTile_D = new Tile(x, y);
                board[y][x] = decoTile_D;
                StandardCluedo.BILLARD_ROOM.addDecoTiles(decoTile_D);
                break;
            case 'E':
                Tile decoTile_E = new Tile(x, y);
                board[y][x] = decoTile_E;
                StandardCluedo.LIBRARY.addDecoTiles(decoTile_E);
                break;
            case 'F':
                Tile decoTile_F = new Tile(x, y);
                board[y][x] = decoTile_F;
                StandardCluedo.STUDY.addDecoTiles(decoTile_F);
                break;
            case 'G':
                Tile decoTile_G = new Tile(x, y);
                board[y][x] = decoTile_G;
                StandardCluedo.HALL.addDecoTiles(decoTile_G);
                break;
            case 'H':
                Tile decoTile_H = new Tile(x, y);
                board[y][x] = decoTile_H;
                StandardCluedo.LOUNGE.addDecoTiles(decoTile_H);
                break;
            case 'I':
                Tile decoTile_I = new Tile(x, y);
                board[y][x] = decoTile_I;
                StandardCluedo.DINING_ROOM.addDecoTiles(decoTile_I);
                break;

            default:
            }
            index++;
        }
    }

    /**
     * 
     * @param x
     * @param y
     * @return
     */
    public Position getPosition(int x, int y) {
        return board[y][x];
    }

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
     * This method should not return a Room !!!!!
     * 
     * 
     * @param player
     * @return
     */
    public Tile lookNorth(Player player) {

        Position playerPos = player.getPosition();

        // Player can only look around out of rooms
        if (playerPos instanceof Room) {
            return null;
        }

        Tile playerTile = (Tile) playerPos;

        if (playerTile.y - 1 < 0) {
            return null;
        }

        // this method should not return a Room
        if (board[playerTile.y - 1][playerTile.x] instanceof Room) {
            return null;
        }

        return (Tile) board[playerTile.y - 1][playerTile.x];
    }

    public Tile lookSouth(Player player) {

        Position playerPos = player.getPosition();

        // Player can only look around out of rooms
        if (playerPos instanceof Room) {
            return null;
        }

        Tile playerTile = (Tile) playerPos;

        if (playerTile.y + 1 > 24) {
            return null;
        }

        if (board[playerTile.y + 1][playerTile.x] instanceof Room) {
            return null;
        }

        return (Tile) board[playerTile.y + 1][playerTile.x];

    }

    public Tile lookEast(Player player) {

        Position playerPos = player.getPosition();

        // Player can only look around out of rooms
        if (playerPos instanceof Room) {
            return null;
        }

        Tile playerTile = (Tile) playerPos;

        if (playerTile.x + 1 > 23) {
            return null;
        }

        if (board[playerTile.y][playerTile.x + 1] instanceof Room) {
            return null;
        }

        return (Tile) board[playerTile.y][playerTile.x + 1];

    }

    public Tile lookWest(Player player) {

        Position playerPos = player.getPosition();

        // Player can only look around out of rooms
        if (playerPos instanceof Room) {
            return null;
        }

        Tile playerTile = (Tile) playerPos;

        if (playerTile.x - 1 < 0) {
            return null;
        }

        if (board[playerTile.y][playerTile.x - 1] instanceof Room) {
            return null;
        }

        return (Tile) board[playerTile.y][playerTile.x - 1];
    }

    public Room atEntranceTo(Player player) {
        Position playerPos = player.getPosition();

        if (playerPos instanceof Entrance) {
            Entrance entrance = (Entrance) playerPos;
            return entrance.toRoom();
        } else {
            return null;
        }
    }

    public List<Entrance> lookForExit(Player player) {
        Position playerPos = player.getPosition();

        if (playerPos instanceof Room) {
            Room room = (Room) playerPos;
            return room.getEntrances();
        } else {
            return null;
        }
    }

    public Room lookForSecPas(Player player) {
        Position playerPos = player.getPosition();
        if (playerPos instanceof Room) {
            Room room = (Room) playerPos;
            if (room.hasSecPas()) {
                return StandardCluedo.getRoom(room.getSecPasTo());
            }
        }
        return null;
    }

    public void movePlayer(Player player, Position position) {
        /*
         * this method shouldn't need to do sanity check, because it should always be
         * called after check validity by calling lookNorth / lookSouth / lookWest /
         * lookEast / atEntrance / lookForExit
         */
        player.setPosition(position);
    }

    public void moveWeapon(WeaponToken weaponToken, Room room) {
        weaponToken.setRoom(room);
    }
}