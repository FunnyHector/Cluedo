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
             *  '!', '@', '#', '$', '%', '^' (shift + 1-6) represents six starting tiles for player tokens.
             *  '!' indicates Scarlet's start position, '@' for mustard, and so on.
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

        
//        for (y = 0; y < board.length; y++) { 
//            for (x = 0; x < board[0].length; x++) { 
//                if (board[y][x] == null) { 
//                    System.out.print(" "); 
//                } else { 
//                    System.out.print(board[y][x]);
//                }
//            }
//            System.out.println();
//        }
         
    }

    /**
     * 
     * @param x
     * @param y
     * @return
     */
    public Position getPosition(int x, int y) {
        return board[x][y];
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
            return null;  // dead code
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

        if (playerTile.x - 1 < 0) {
            return null;
        }

        // this method should not return a Room
        if (board[playerTile.x - 1][playerTile.y] instanceof Room) {
            return null;
        }

        return (Tile) board[playerTile.x - 1][playerTile.y];
    }

    public void moveNorth(Player player) {
        Tile northTile = lookNorth(player);
        if (northTile != null) {
            player.setPosition(northTile);
        } else {
            throw new GameError(
                    "Cannot move north from " + player.getPosition().toString());
        }
    }

    public Tile lookSouth(Player player) {

        Position playerPos = player.getPosition();

        // Player can only look around out of rooms
        if (playerPos instanceof Room) {
            return null;
        }

        Tile playerTile = (Tile) playerPos;

        if (playerTile.x + 1 > 24) {
            return null;
        }

        if (board[playerTile.x + 1][playerTile.y] instanceof Room) {
            return null;
        }

        return (Tile) board[playerTile.x + 1][playerTile.y];

    }

    public void moveSouth(Player player) {
        Tile southTile = lookSouth(player);
        if (southTile != null) {
            player.setPosition(southTile);
        } else {
            throw new GameError(
                    "Cannot move south from " + player.getPosition().toString());
        }
    }

    public Tile lookEast(Player player) {

        Position playerPos = player.getPosition();

        // Player can only look around out of rooms
        if (playerPos instanceof Room) {
            return null;
        }

        Tile playerTile = (Tile) playerPos;

        if (playerTile.y + 1 > 23) {
            return null;
        }

        if (board[playerTile.x][playerTile.y + 1] instanceof Room) {
            return null;
        }

        return (Tile) board[playerTile.x][playerTile.y + 1];

    }

    public void moveEast(Player player) {
        Tile eastTile = lookEast(player);
        if (eastTile != null) {
            player.setPosition(eastTile);
        } else {
            throw new GameError(
                    "Cannot move east from " + player.getPosition().toString());
        }

    }

    public Tile lookWest(Player player) {

        Position playerPos = player.getPosition();

        // Player can only look around out of rooms
        if (playerPos instanceof Room) {
            return null;
        }

        Tile playerTile = (Tile) playerPos;

        if (playerTile.y - 1 < 0) {
            return null;
        }

        if (board[playerTile.x][playerTile.y - 1] instanceof Room) {
            return null;
        }

        return (Tile) board[playerTile.x][playerTile.y - 1];
    }

    public void moveWest(Player player) {
        Tile westTile = lookWest(player);
        if (westTile != null) {
            player.setPosition(westTile);
        } else {
            throw new GameError(
                    "Cannot move west from " + player.getPosition().toString());
        }
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

    public void enterRoom(Player player) {
        Room room = atEntranceTo(player);
        if (room != null) {
            player.setPosition(room);
        } else {
            throw new GameError(
                    "Cannot enter room from " + player.getPosition().toString());
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

    public void exitRoom(Player player, Entrance entrance) {
        List<Entrance> entrances = lookForExit(player);
        if (entrances != null && entrances.contains(entrance)) {
            player.setPosition(entrance);
        } else {
            throw new GameError(
                    "Cannot exit room from " + player.getPosition().toString());
        }
    }

    public void moveWeapon(WeaponToken weaponToken, Room room) {
        weaponToken.setRoom(room);
    }
    
    public static void main(String[] args) {
        new Board(StandardCluedo.BOARD_STRING);
        
        
    }
}

// /**
// * This method tells whether a player can move towards south from a given position.
// *
// * NB: this method cannot tell whether a player can move into a room. eg. if a
// player
// * is standing at a tile facing an room entrance, the result will be false if this
// * method is called.
// *
// * @param position
// * @return
// */
// public boolean CanMoveSouth(Position position, int steps) {
// if (position instanceof Room) {
// return false;
// }
//
// Tile tile = (Tile) position;
//
// int tileY = tile.getY();
// if (tileY + steps > 24) {
// return false;
// }
//
// for (int i = 1; i <= steps; i++) {
// Position posSouth = postions[tile.getX()][tileY + i];
// if (posSouth == null) {
// return false;
// }
// }
//
// return true;
// }
//
// /**
// * This method returns the position on south of the given position.
// *
// *
// * @param tile
// * @return
// */
// public Position southTile(Position position, int steps) {
// if (!(position instanceof Tile)) {
// throw new GameError(
// "wrong method(Board.southTile()) call from a room object");
// }
// Tile tile = (Tile) position;
// if (tile.getY() + steps > 24) {
// throw new GameError("Out of board.");
// }
// return postions[tile.getX()][tile.getY() + steps];
// }
//
// /**
// *
// * @param position
// * @return
// */
// public boolean CanMoveNorth(Position position, int steps) {
// if (position instanceof Room) {
// return false;
// }
//
// Tile tile = (Tile) position;
//
// int tileY = tile.getY();
// if (tileY - steps < 0) {
// return false;
// }
//
// for (int i = 1; i <= steps; i++) {
// Position posNorth = postions[tile.getX()][tileY - i];
// if (posNorth == null) {
// return false;
// }
// }
//
// return true;
// }
//
// /**
// *
// * @param position
// * @return
// */
// public Position northTile(Position position, int steps) {
// if (!(position instanceof Tile)) {
// throw new GameError(
// "wrong method(Board.northTile()) call from a room object");
// }
// Tile tile = (Tile) position;
// if (tile.getY() - steps < 0) {
// throw new GameError("Out of board.");
// }
// return postions[tile.getX()][tile.getY() - steps];
// }
//
// /**
// *
// * @param position
// * @return
// */
// public boolean CanMoveEast(Position position, int steps) {
// if (position instanceof Room) {
// return false;
// }
//
// Tile tile = (Tile) position;
//
// int tileX = tile.getX();
// if (tileX + steps > 23) {
// return false;
// }
//
// for (int i = 1; i <= steps; i++) {
// Position posEast = postions[tileX + i][tile.getY()];
// if (posEast == null) {
// return false;
// }
// }
//
// return true;
// }
//
// /**
// *
// * @param position
// * @return
// */
// public Position eastTile(Position position, int steps) {
// if (!(position instanceof Tile)) {
// throw new GameError("wrong method(Board.eastTile()) call from a room object");
// }
// Tile tile = (Tile) position;
// if (tile.getX() + steps > 23) {
// throw new GameError("Out of board.");
// }
// return postions[tile.getX() + steps][tile.getY()];
// }
//
// /**
// *
// * @param position
// * @return
// */
// public boolean CanMoveWest(Position position, int steps) {
// if (position instanceof Room) {
// return false;
// }
//
// Tile tile = (Tile) position;
//
// int tileX = tile.getX();
// if (tileX - steps < 0) {
// return false;
// }
//
// for (int i = 1; i <= steps; i++) {
// Position posEast = postions[tileX - i][tile.getY()];
// if (posEast == null) {
// return false;
// }
// }
//
// return true;
// }
//
// /**
// *
// * @param position
// * @return
// */
// public Position westTile(Position position, int steps) {
// if (!(position instanceof Tile)) {
// throw new GameError("wrong method(Board.westTile()) call from a room object");
// }
// Tile tile = (Tile) position;
// if (tile.getX() - steps < 0) {
// throw new GameError("Out of board.");
// }
// return postions[tile.getX() - steps][tile.getY()];
// }
//
// /**
// * for debug, will be deleted
// *
// * @param args
// */
// public static void main(String[] args) {
// if (args.length > 2) {
// System.out.print("Please specify a game file to construct a customised"
// + " Cluedo, or use no argument to load default cluedo.");
// } else if (args.length < 2) {
// new Board(StandardCluedo.BOARD_STRING);
// } else {
// // read the customised Cluedo file.
// // some future work to be done
// }
// }
