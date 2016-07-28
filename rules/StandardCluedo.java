package rules;

import java.util.Arrays;

import card.Location;
import card.Weapon;
import game.WeaponToken;
import tile.Position;
import tile.Room;
import tile.Tile;

public class StandardCluedo {

    // nine rooms
    public static final Room KITCHEN = new Room(Location.Kitchen, Location.Study);
    public static final Room BALL_ROOM = new Room(Location.Ball_room, null);
    public static final Room CONSERVATORY = new Room(Location.Conservatory,
            Location.Lounge);
    public static final Room BILLARD_ROOM = new Room(Location.Billard_Room, null);
    public static final Room LIBRARY = new Room(Location.Library, null);
    public static final Room STUDY = new Room(Location.Study, Location.Kitchen);
    public static final Room HALL = new Room(Location.Hall, null);
    public static final Room LOUNGE = new Room(Location.Lounge, Location.Conservatory);
    public static final Room DINING_ROOM = new Room(Location.Dining_Room, null);
    
    
    // @formatter:off
    // a string used to print out text-based UI
    public static final String UI_STRING_A = 
            "■■■■■■■■■ ■■■■ ■■■■■■■■■\n" +
            "┌────□■   ┌──┐   ■┌────┐\n" + 
            "│KITC│  ┌─┘  └─┐  │CSTY│\n" +
            "│HEN │  │ BALL │  │    │\n" + 
            "│    │  │ ROOM │  ↑│  ┌┘\n" +
            "└┐   │  →      ←   └──□■\n" + 
            "■└──↑┘  │      │        \n" +
            "        └↑────↑┘       ■\n" + 
            "■                 ┌────┐\n" +
            "┌───┐             →BLRD│\n" + 
            "│   └──┐  ┌───┐   │    │\n" +
            "│DINING│  │CRI│   │    │\n" + 
            "│ROOM  ←  │ME │   └───↑┘\n" +
            "│      │  │SCE│        ■\n" + 
            "│      │  │NE │   ┌─↓─┐■\n" +
            "└─────↑┘  │   │  ┌┘LIB└┐\n" + 
            "■         └───┘  →     │\n" +
            "                 └┐   ┌┘\n" + 
            "■        ┌─↓↓─┐   └───┘■\n" +
            "□─────↓  │HALL│         \n" + 
            "│LOUNG│  │    ←        ■\n" +
            "│E    │  │    │  ↓─────□\n" + 
            "│     │  │    │  │STUDY│\n" +
            "│    ┌┘  │    │  └┐    │\n" + 
            "└────┘■ ■└────┘■ ■└────┘\n";

    // an alternative string used to print out text-based UI
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

    
    /*
     * a string used to construct the board. This could be defined outside of source code,
     * for instance in a .txt file, but since Cluedo is brand-registered, and the board
     * layout is not so much modifiable under those game rules, so it's easier to be put
     * just here.
     */
    public static final String BOARD_STRING = 
            "         #    $         \n" +
            "       000    000       \n" + 
            "      00        00      \n" +
            "      00        00 CCCC \n" + 
            " AAAA 00        003 CC  \n" +
            "  AA  0b2 BBB  2b0c     \n" + 
            "    1 00   BBB  0000000%\n" +
            "0000a000 2    2 0000000 \n" + 
            " 00000000b0000b000      \n" +
            "     000000000000d4     \n" + 
            "        00     000 DDD  \n" +
            "        00     000 DDD  \n" + 
            "       9i0     000    4 \n" +
            " III    00     00000e0d \n" + 
            "  III   00     000  5   \n" +
            "      9 00     00       \n" + 
            " 00000i000     0e5  EEE \n" +
            "@0000000000gg0000  EEE  \n" + 
            " 00000h00  77  000      \n" +
            "      800      00000000^\n" + 
            "       00     7g0f00000 \n" +
            "       00 GGG  006      \n" + 
            " HHH   00  GGG 00       \n" +
            "  HHH  00      00  FFFF \n" + 
            "       !        0       \n";
    // @formatter:on

}
