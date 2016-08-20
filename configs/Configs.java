package configs;

import card.Location;
import tile.Room;

/**
 * This class contains most of configurations to construct a game board. All fields and
 * methods are static to use.
 * 
 * @author Hector
 *
 */
public class Configs {
    /**
     * The number of dices used in game
     */
    public static final int NUM_DICE = 2;
    /**
     * Minimum player needed
     */
    public static final int MIN_PLAYER = 3;
    /**
     * Maximum player to join into game.
     */
    public static final int MAX_PLAYER = 6;
    /**
     * the horizontal boundary coordinate of Cluedo game board.
     */
    public static final int BOARD_WIDTH = 24;
    /**
     * the vertical boundary coordinate of Cluedo game board.
     */
    public static final int BOARD_HEIGHT = 25;
    /**
     * The static object of Kitchen
     */
    public static final Room KITCHEN = new Room(Location.Kitchen, Location.Study);
    /**
     * The static object of ball room
     */
    public static final Room BALL_ROOM = new Room(Location.Ball_room, null);
    /**
     * The static object of conservatory
     */
    public static final Room CONSERVATORY = new Room(Location.Conservatory,
            Location.Lounge);
    /**
     * The static object of billard room
     */
    public static final Room BILLARD_ROOM = new Room(Location.Billard_Room, null);
    /**
     * The static object of Library
     */
    public static final Room LIBRARY = new Room(Location.Library, null);
    /**
     * The static object of study room
     */
    public static final Room STUDY = new Room(Location.Study, Location.Kitchen);
    /**
     * The static object of hall
     */
    public static final Room HALL = new Room(Location.Hall, null);
    /**
     * The static object of lounge
     */
    public static final Room LOUNGE = new Room(Location.Lounge, Location.Conservatory);
    /**
     * The static object of dining room
     */
    public static final Room DINING_ROOM = new Room(Location.Dining_Room, null);

    /**
     * This method take into the symbolic location (Card object), and returns the
     * corresponding Room object.
     * 
     * @param loc
     *            --- symbolic location (Card object)
     * @return --- the corresponding, static Room object
     */
    public static Room getRoom(Location loc) {
        switch (loc) {
        case Ball_room:
            return BALL_ROOM;
        case Billard_Room:
            return BILLARD_ROOM;
        case Conservatory:
            return CONSERVATORY;
        case Dining_Room:
            return DINING_ROOM;
        case Hall:
            return HALL;
        case Kitchen:
            return KITCHEN;
        case Library:
            return LIBRARY;
        case Lounge:
            return LOUNGE;
        case Study:
            return STUDY;
        default:
            return null; // dead code
        }
    }

    // @formatter:off
    
    /**
     *  a string used to print out text-based UI. This is used as a canvas.
     */
    public static final String UI_STRING_A = 
            "■■■■■■■■■ ■■■■ ■■■■■■■■■\n" +
            "┌KIT─□■   ┌──┐   ■┌CSTY┐\n" + 
            "│    │  B─L  R─M  │    │\n" +
            "│    │  │      │  │    │\n" + 
            "│    │  │      │  ↑│  ┌┘\n" +
            "└┐   │  →      ←   └──□■\n" + 
            "■└──↑┘  │      │        \n" +
            "        └↑────↑┘       ■\n" + 
            "■                 ┌────┐\n" +
            "┌───┐             →    B\n" + 
            "D   └──┐  ┌───┐   │    L\n" +
            "I      │  │B  │   │    D\n" + 
            "N      ←  │A M│   └───↑┘\n" +
            "I      │  │S E│        ■\n" + 
            "N      │  │E N│   ┌─↓─┐■\n" +
            "└─────↑┘  │  T│  ┌┘   └┐\n" + 
            "■         └───┘  →     │\n" +
            "                 └┐   ┌┘\n" + 
            "■        ┌─↓↓─┐   └LIB┘■\n" +
            "□─────↓  │    │         \n" + 
            "│     │  │    ←        ■\n" +
            "L     │  │    │  ↓─────□\n" + 
            "O     │  │    │  │     │\n" +
            "U    ┌┘  │    │  └┐    │\n" + 
            "N─G─E┘■ ■└HALL┘■ ■└STUDY\n";

    /**
     *  an alternative string used to print out text-based UI. It uses two characters to
     *  represent one tile position, so that the ASCII board is not so skinny.<br>
     *  <br>
     *  NOTE: it's not used currently.
     */
    public static final String UI_STRING_B = 
            "■■■■■■■■■■■■■■■■■■  ■■■■■■■■  ■■■■■■■■■■■■■■■■■■\n" +
            "┌──────────□■■      ┌──────┐      ■■┌──────────┐\n" + 
            "│ KITCHEN  │    ┌───┘      └───┐    │ CONSERVA │\n" +
            "│          │    │   B A L L    │    │ TORY     │\n" + 
            "│          │    │   R O O M    │    └↑┐      ┌─┘\n" +
            "└─┐        │    →              ←      └──────□■■\n" + 
            "■■└─────↑ ─┘    │              │                \n" +
            "                └─↑ ────────↑ ─┘              ■■\n" + 
            "■■                                  ┌──────────┐\n" +
            "┌────────┐                          → BILLARD  │\n" + 
            "│        └─────┐    ┌────────┐      │          │\n" +
            "│ D I N I N G  │    │ CRIME  │      │          │\n" + 
            "│ R O O M      ←    │        │      └─────── ↑─┘\n" +
            "│              │    │ SCENE  │                ■■\n" + 
            "│              │    │        │      ┌───↓ ───┐■■\n" +
            "└───────────↑ ─┘    │        │    ┌─┘LIBRARY └─┐\n" + 
            "■■                  └────────┘    →            │\n" +
            "                                  └─┐        ┌─┘\n" + 
            "■■                ┌─── ↓↓ ───┐      └────────┘■■\n" +
            "□───────────↓┐    │ H A L L  │                  \n" + 
            "│ L O U N G E│    │          ←                ■■\n" +
            "│            │    │          │    ┌↓───────────□\n" + 
            "│            │    │          │    │ S T U D Y  │\n" +
            "│          ┌─┘    │          │    └─┐          │\n" + 
            "└──────────┘■■  ■■└──────────┘■■  ■■└──────────┘\n";

    
    /**
     * a string used to construct the board in text-ui game.<br>
     * <br>
     * '0' : walkable tiles, tiles that are out of all rooms.<br>
     * ' ' : (space) represents walls and unenterable tiles.<br>
     * 1-9 : represents nine rooms on board.<br>
     * '!' : (shift + 1) represents Scarlet's start position.<br>
     * '@' : (shift + 2) represents Mustard's start position.<br>
     * '#' : (shift + 3) represents White's start position.<br>
     * '$' : (shift + 4) represents Green's start position.<br>
     * '%' : (shift + 5) represents Peacock's start position.<br>
     * '^' : (shift + 6) represents Plum's start position.<br>
     * a-i : represents entrance to each room, 'a' is entrance to room '1',
     * 'b' to room '2', and so on.<br>
     * A-I : represents decorative tiles in each room, which has no functionality
     * on board, only indicates where to draw player token and weapon token. 'A' 
     * marks where to draw in room '1', 'b' to room '2', and so on.<br>
     */
    public static final String BOARD_STRING_TXT = 
            "         #    $         \n" +
            "       000    000       \n" + 
            " AAAA 00        00 CCCC \n" +
            " AAAA 00        00 CCCC \n" + 
            " AAAA 00  BBBB  003 CC  \n" +
            "      0b2 BBBB 2b0c     \n" + 
            "    1 00  BBBB  0000000%\n" +
            "0000a000 2    2 0000000 \n" + 
            " 00000000b0000b000      \n" +
            "     000000000000d4 DDD \n" + 
            "        00     000 DDDD \n" +
            "  IIII  00     000 DDD  \n" + 
            "  IIII 9i0     000    4 \n" +
            "  IIII  00     00000e0d \n" + 
            "        00     000  5   \n" +
            "      9 00     00  E E  \n" + 
            " 00000i000     0e5 EEEE \n" +
            "@0000000000gg0000  EEE  \n" + 
            " 00000h00  77  000      \n" +
            "      800      00000000^\n" + 
            "  HHHH 00 GGG 7g0f00000 \n" +
            "  HHHH 00 GGG  006      \n" + 
            "  HHHH 00 GGG  00 FFFFF \n" +
            "       00 GGG  00  FFFF \n" + 
            "       !        0       \n";
    
    /**
     * a string used to construct the board.<br>
     * <br>
     * '0' : walkable tiles, tiles that are out of all rooms.<br>
     * 'x' : (space) represents walls and unenterable tiles.<br>
     * 1-9 : represents nine rooms on board.<br>
     * '!' : (shift + 1) represents Scarlet's start position.<br>
     * '@' : (shift + 2) represents Mustard's start position.<br>
     * '#' : (shift + 3) represents White's start position.<br>
     * '$' : (shift + 4) represents Green's start position.<br>
     * '%' : (shift + 5) represents Peacock's start position.<br>
     * '^' : (shift + 6) represents Plum's start position.<br>
     * a-i : represents entrance to each room, 'a' is entrance to room '1',
     * 'b' to room '2', and so on.<br>
     */
    public static final String BOARD_STRING_GUI = 
            "xxxxxxxxx#xxxx$xxxxxxxxx\n" +
            "111111x0002222000x333333\n" + 
            "111111002222222200333333\n" +
            "111111002222222200333333\n" + 
            "111111002222222200333333\n" +
            "1111110b22222222b0c3333x\n" + 
            "x1111100222222220000000%\n" +
            "0000a000222222220000000x\n" + 
            "x00000000b0000b000444444\n" +
            "99999000000000000d444444\n" + 
            "9999999900xxxxx000444444\n" +
            "9999999900xxxxx000444444\n" + 
            "99999999i0xxxxx000444444\n" +
            "9999999900xxxxx00000e0dx\n" + 
            "9999999900xxxxx00055555x\n" +
            "9999999900xxxxx005555555\n" + 
            "x00000i000xxxxx0e5555555\n" +
            "@0000000000gg00005555555\n" + 
            "x00000h0077777700055555x\n" +
            "88888880077777700000000^\n" + 
            "888888800777777g0f00000x\n" +
            "888888800777777006666666\n" + 
            "888888800777777006666666\n" +
            "888888800777777006666666\n" + 
            "888888x!x777777x0x666666\n";

    // @formatter:on

}
