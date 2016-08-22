package view;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Set;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import configs.Configs;
import card.Card;
import card.Character;
import card.Location;
import game.Player;
import tile.Position;
import tile.Room;
import tile.RoomTile;
import tile.Tile;
import ui.GUIClient;
import view.token.AbstractToken;
import view.token.CharacterToken;
import view.token.WeaponToken;

import static ui.GUIClient.loadImage;

@SuppressWarnings("serial")
public class BoardCanvas extends JPanel {

    /**
     * The width and Height of each Tile. Note this constant is important as most of board
     * display is calculated based upon this value
     */
    private static final int TILE_WIDTH = 32;
    /**
     * The width of board
     */
    public static final int BOARD_IMG_WIDTH = TILE_WIDTH * Configs.BOARD_WIDTH;
    /**
     * The height of board
     */
    public static final int BOARD_IMG_HEIGHT = TILE_WIDTH * Configs.BOARD_HEIGHT;
    /**
     * the padding size on left
     */
    public static final int PADDING_LEFT = 10;
    /**
     * the padding size on right
     */
    public static final int PADDING_RIGHT = PADDING_LEFT;
    /**
     * the padding size on Top
     */
    public static final int PADDING_TOP = 20;
    /**
     * the padding size on down
     */
    public static final int PADDING_DOWN = PADDING_LEFT;
    /**
     * Game's main GUI
     */
    private GUIClient gui;
    /**
     * An array of character tokens
     */
    public CharacterToken[] characterTokens;

    /**
     * Construct a custom panel for display game board
     * 
     * @param guiClient
     *            --- the Main GUI of this game
     */
    public BoardCanvas(GUIClient guiClient) {
        super();
        this.gui = guiClient;
        addMouseListenerOnBoard(this);

        // we use absolute layout
        this.setLayout(null);

        characterTokens = createCharacterToken(CHARACTER_TOKEN_IMG);

        // add character and weapon tokens on board
        for (int i = 0; i < characterTokens.length; i++) {
            this.add(characterTokens[i]);
        }
        WeaponToken[] weaponTokens = gui.getWeaponTokens();
        for (int i = 0; i < weaponTokens.length; i++) {
            this.add(weaponTokens[i]);
        }

        // add question marks and cross for easy mode
        for (int i = 0; i < QUESTION_ON_ROOM.length; i++) {
            this.add(QUESTION_ON_ROOM[i]);
        }
        for (int i = 0; i < CROSS_ON_ROOM.length; i++) {
            this.add(CROSS_ON_ROOM[i]);
        }

        // update tokens' positions
        update();
    }

    /**
     * This method ask gui for game's status, and update the display of player panel.
     */
    public void update() {

        WeaponToken[] weaponTokens = gui.getWeaponTokens();
        Set<Card> knownCards = gui.getKnownCards();
        boolean isEasyMode = gui.isEasyMode();

        // if it is easy mode, draw some cross or question marks on rooms
        if (isEasyMode) {
            for (Location l : Location.values()) {
                if (knownCards.contains(l)) {
                    CROSS_ON_ROOM[l.ordinal()].setVisible(true);
                    QUESTION_ON_ROOM[l.ordinal()].setVisible(false);
                } else {
                    QUESTION_ON_ROOM[l.ordinal()].setVisible(true);
                    CROSS_ON_ROOM[l.ordinal()].setVisible(false);
                }
            }
        } else {
            for (JLabel jLabel : CROSS_ON_ROOM) {
                jLabel.setVisible(false);
            }
            for (JLabel jLabel : QUESTION_ON_ROOM) {
                jLabel.setVisible(false);
            }
        }

        // update character tokens' position
        for (int i = 0; i < characterTokens.length; i++) {

            // if it is easy mode, reset the tooltip for tokens.
            characterTokens[i].setEasyMode(isEasyMode);
            if (knownCards.contains(characterTokens[i].getCharacter())) {
                characterTokens[i].setIsKnown(true);
            } else {
                characterTokens[i].setIsKnown(false);
            }

            Player player = gui.getPlayerByCharacter(Character.get(i));
            Position pos = player.getPosition();
            if (pos instanceof Tile) {
                Tile tile = (Tile) pos;
                setCharacterTokenOnTile(characterTokens[i], tile.x, tile.y);
            } else if (pos instanceof Room) {
                setTokenInRoom(characterTokens[i], characterTokens[i].getRoomTile());
            }
        }

        // update weapon tokens' position
        for (int i = 0; i < weaponTokens.length; i++) {

            // if it is easy mode, reset the tooltip for tokens.
            weaponTokens[i].setEasyMode(isEasyMode);
            if (knownCards.contains(weaponTokens[i].getWeapon())) {
                weaponTokens[i].setIsKnown(true);
            } else {
                weaponTokens[i].setIsKnown(false);
            }

            setTokenInRoom(weaponTokens[i], weaponTokens[i].getRoomTile());
        }

        // repaint
        repaint();
    }

    /**
     * This method sets a character token on the given board coordinate
     * 
     * @param characterToken
     *            --- the character token
     * @param x
     *            --- the horizontal coordinate
     * @param y
     *            --- the vertical coordinate
     */
    private void setCharacterTokenOnTile(CharacterToken characterToken, int x, int y) {
        int height = characterToken.getIcon().getIconHeight();
        int width = characterToken.getIcon().getIconWidth();
        characterToken.setBounds(PADDING_LEFT + TILE_WIDTH * x,
                PADDING_TOP + TILE_WIDTH * y - (height - TILE_WIDTH + 2), width, height);
    }

    /**
     * This method sets a token on the given room tile inside a room.
     * 
     * @param token
     *            --- the token
     * @param roomTile
     *            --- the room tile
     */
    private void setTokenInRoom(JLabel token, RoomTile roomTile) {
        int height = token.getIcon().getIconHeight();
        int width = token.getIcon().getIconWidth();
        token.setBounds(PADDING_LEFT + TILE_WIDTH * roomTile.getX(),
                PADDING_TOP + TILE_WIDTH * roomTile.getY() - (height - TILE_WIDTH + 2),
                width, height);
    }

    /**
     * This method creates an array of character tokens.
     * 
     * @param charactertokenImg
     *            --- the image icon used to display the character tokens on board
     * @return --- an array of character tokens
     */
    private CharacterToken[] createCharacterToken(ImageIcon[] charactertokenImg) {
        CharacterToken[] tokens = new CharacterToken[charactertokenImg.length];
        for (int i = 0; i < charactertokenImg.length; i++) {
            Character c = Character.get(i);
            tokens[i] = new CharacterToken(charactertokenImg[i], c);
            tokens[i].setBorder(null);
            tokens[i].setToolTipText(c.toString() + ". \n(Player: "
                    + gui.getPlayerByCharacter(c).getName() + ")");
            addMouseListenerOnToken(tokens[i]);
        }
        return tokens;
    }

    /**
     * This method creates a serious of question mark icons for each room. These icons are
     * used to give the player a help in easy mode
     * 
     * @return --- am array of question mark icons
     */
    private static JLabel[] creatQuestionOnRoom() {
        JLabel[] questionIcons = new JLabel[Location.values().length];
        for (int i = 0; i < Location.values().length; i++) {
            questionIcons[i] = new JLabel(AbstractToken.QUESTION_ICON);
            int width = AbstractToken.QUESTION_ICON.getIconWidth();
            int Height = AbstractToken.QUESTION_ICON.getIconHeight();

            questionIcons[i].setBounds(PADDING_LEFT + TILE_WIDTH * EASYMODE_POS[i][0],
                    PADDING_TOP + TILE_WIDTH * EASYMODE_POS[i][1], width, Height);
            questionIcons[i].setVisible(false);
        }
        return questionIcons;
    }

    /**
     * This method creates a serious of cross icons for each room. These icons are used to
     * give the player a help in easy mode
     * 
     * @return --- am array of cross icons
     */
    private static JLabel[] createCrossOnRoom() {
        JLabel[] crossIcons = new JLabel[Location.values().length];
        for (int i = 0; i < Location.values().length; i++) {
            crossIcons[i] = new JLabel(AbstractToken.CROSS_ICON);
            int width = AbstractToken.CROSS_ICON.getIconWidth();
            int Height = AbstractToken.CROSS_ICON.getIconHeight();

            crossIcons[i].setBounds(PADDING_LEFT + TILE_WIDTH * EASYMODE_POS[i][0],
                    PADDING_TOP + TILE_WIDTH * EASYMODE_POS[i][1], width, Height);
            crossIcons[i].setVisible(false);
        }
        return crossIcons;
    }

    /**
     * This method adds mouse listener on tokens.<br>
     * <br>
     * NOTE: this method does nothing right now due to the time constraint. To be
     * completed later.
     * 
     * @param jlabel
     *            --- tokens on which this mouse listener is added
     */
    private void addMouseListenerOnToken(JLabel jlabel) {
        jlabel.addMouseListener(new MouseListener() {
            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseClicked(MouseEvent e) {
            }
        });
    }

    /**
     * This method adds mouse listener on tokens.<br>
     * <br>
     * NOTE: this method does nothing right now due to the time constraint. To be
     * completed later.
     * 
     * @param boardCanvas
     *            --- the board canvas itself
     */
    private void addMouseListenerOnBoard(BoardCanvas boardCanvas) {
        boardCanvas.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });
    }

    /**
     * Get all character tokens
     * 
     * @return --- an array of character tokens
     */
    public CharacterToken[] getCharacterTokens() {
        return characterTokens;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(GAME_BOARD, PADDING_LEFT, PADDING_TOP, this);
        /*
         * TODO draw rectangle on movable positions, so that a clicking on it can actually
         * move the player there
         */
    }

    /**
     * The image used to display the game board
     */
    private static final Image GAME_BOARD = loadImage("Game_Board.png");
    /**
     * Images used to display character tokens
     */
    private static final ImageIcon[] CHARACTER_TOKEN_IMG = {
            new ImageIcon(loadImage("Token_Miss_Scarlet.png")),
            new ImageIcon(loadImage("Token_Colonel_Mustard.png")),
            new ImageIcon(loadImage("Token_Mrs_White.png")),
            new ImageIcon(loadImage("Token_The_Reverend_Green.png")),
            new ImageIcon(loadImage("Token_Mrs_Peacock.png")),
            new ImageIcon(loadImage("Token_Professor_Plum.png")) };
    /**
     * Images used to display weapon tokens
     */
    public static final ImageIcon[] WEAPON_TOKEN_IMG = {
            new ImageIcon(loadImage("Token_Candlestick.png")),
            new ImageIcon(loadImage("Token_Dagger.png")),
            new ImageIcon(loadImage("Token_Lead_Pipe.png")),
            new ImageIcon(loadImage("Token_Revolver.png")),
            new ImageIcon(loadImage("Token_Rope.png")),
            new ImageIcon(loadImage("Token_Spanner.png")) };
    /**
     * This array records the coordinates to display a question mark icon or a cross icon
     * in room in easy mode
     */
    private static final int[][] EASYMODE_POS = { { 5, 6 }, { 15, 7 }, { 23, 4 },
            { 23, 12 }, { 22, 18 }, { 23, 24 }, { 14, 24 }, { 5, 24 }, { 7, 15 } };
    /**
     * A serious of cross icons for each room. These icons are used to give the player a
     * help in easy mode
     */
    private static final JLabel[] CROSS_ON_ROOM = createCrossOnRoom();
    /**
     * a serious of question mark icons for each room. These icons are used to give the
     * player a help in easy mode
     */
    private static final JLabel[] QUESTION_ON_ROOM = creatQuestionOnRoom();
}
