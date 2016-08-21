package view;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import configs.Configs;
import card.Card;
import card.Character;
import card.Location;
import card.Weapon;
import game.GameError;
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

public class BoardCanvas extends JPanel {

    public static final ImageIcon[] CHARACTER_TOKEN_IMG = {
            new ImageIcon(loadImage("Token_Miss_Scarlet.png")),
            new ImageIcon(loadImage("Token_Colonel_Mustard.png")),
            new ImageIcon(loadImage("Token_Mrs_White.png")),
            new ImageIcon(loadImage("Token_The_Reverend_Green.png")),
            new ImageIcon(loadImage("Token_Mrs_Peacock.png")),
            new ImageIcon(loadImage("Token_Professor_Plum.png")) };

    public static final ImageIcon[] WEAPON_TOKEN_IMG = {
            new ImageIcon(loadImage("Token_Candlestick.png")),
            new ImageIcon(loadImage("Token_Dagger.png")),
            new ImageIcon(loadImage("Token_Lead_Pipe.png")),
            new ImageIcon(loadImage("Token_Revolver.png")),
            new ImageIcon(loadImage("Token_Rope.png")),
            new ImageIcon(loadImage("Token_Spanner.png")) };

    private static final int[][] NO_BRAINER_POS = { { 5, 6 }, { 15, 7 }, { 23, 4 },
            { 23, 12 }, { 22, 18 }, { 23, 24 }, { 14, 24 }, { 5, 24 }, { 7, 15 } };

    private static final JLabel[] CROSS_ON_ROOM = createCrossOnRoom();
    private static final JLabel[] QUESTION_ON_ROOM = creatQuestionOnRoom();

    // The game board image is made according to this pixel size
    public static final int TILE_WIDTH = 32;

    public static final int BOARD_IMG_WIDTH = TILE_WIDTH * Configs.BOARD_WIDTH;
    public static final int BOARD_IMG_HEIGHT = TILE_WIDTH * Configs.BOARD_HEIGHT;

    public static final int PADDING_LEFT = 10;
    public static final int PADDING_RIGHT = PADDING_LEFT;
    public static final int PADDING_TOP = 20;
    public static final int PADDING_DOWN = PADDING_LEFT;

    private GUIClient gui;
    public CharacterToken[] characterTokens;

    public BoardCanvas(GUIClient guiClient) {
        super();
        this.gui = guiClient;

        addMouseListenerOnBoard(this);

        // we use absolute layout
        this.setLayout(null);

        characterTokens = createCharacterToken(CHARACTER_TOKEN_IMG);

        // add character and weapon tokens
        for (int i = 0; i < characterTokens.length; i++) {
            this.add(characterTokens[i]);
        }

        WeaponToken[] weaponTokens = gui.getWeaponTokens();
        for (int i = 0; i < weaponTokens.length; i++) {
            this.add(weaponTokens[i]);
        }

        // add question marks and cross for no brainer mode
        for (int i = 0; i < QUESTION_ON_ROOM.length; i++) {
            this.add(QUESTION_ON_ROOM[i]);
        }

        for (int i = 0; i < CROSS_ON_ROOM.length; i++) {
            this.add(CROSS_ON_ROOM[i]);
        }

        // update tokens' positions
        update();

    }

    public void update() {

        WeaponToken[] weaponTokens = gui.getWeaponTokens();
        Set<Card> knownCards = gui.getKnownCards();
        boolean isNoBrainerMode = gui.isNoBrainerMode();

        // if it is no brainer mode, draw some cross or question marks on rooms
        if (isNoBrainerMode) {
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

        // update character tokens
        for (int i = 0; i < characterTokens.length; i++) {

            // if it is no brainer mode, reset the tooltip for tokens.
            characterTokens[i].setNoBrainer(isNoBrainerMode);
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

        // update weapon tokens
        for (int i = 0; i < weaponTokens.length; i++) {

            // if it is no brainer mode, reset the tooltip for tokens.
            weaponTokens[i].setNoBrainer(isNoBrainerMode);
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

    private void setCharacterTokenOnTile(CharacterToken characterToken, int x, int y) {
        int height = characterToken.getIcon().getIconHeight();
        int width = characterToken.getIcon().getIconWidth();
        characterToken.setBounds(PADDING_LEFT + TILE_WIDTH * x,
                PADDING_TOP + TILE_WIDTH * y - (height - TILE_WIDTH + 2), width, height);
    }

    private void setTokenInRoom(JLabel token, RoomTile roomTile) {
        int height = token.getIcon().getIconHeight();
        int width = token.getIcon().getIconWidth();
        token.setBounds(PADDING_LEFT + TILE_WIDTH * roomTile.getX(),
                PADDING_TOP + TILE_WIDTH * roomTile.getY() - (height - TILE_WIDTH + 2),
                width, height);

    }

    private void setNoBrainerCrossForRoom(JLabel cross, int x, int y) {
        int height = cross.getIcon().getIconHeight();
        int width = cross.getIcon().getIconWidth();
        cross.setBounds(PADDING_LEFT + TILE_WIDTH * x,
                PADDING_TOP + TILE_WIDTH * y - (height - TILE_WIDTH + 2), width, height);
    }

    private CharacterToken[] createCharacterToken(ImageIcon[] charactertokenImg) {
        CharacterToken[] tokens = new CharacterToken[charactertokenImg.length];
        for (int i = 0; i < charactertokenImg.length; i++) {
            Character c = Character.get(i);
            Tile tile = gui.getStartPosition(c);
            tokens[i] = new CharacterToken(charactertokenImg[i], tile.x, tile.y, c);
            tokens[i].setBorder(null);
            tokens[i].setToolTipText(c.toString() + ". \n(Player: "
                    + gui.getPlayerByCharacter(c).getName() + ")");
            addMouseListenerOnCharacterToken(tokens[i]);
        }
        return tokens;
    }

    private static JLabel[] creatQuestionOnRoom() {
        JLabel[] questionIcons = new JLabel[Location.values().length];
        for (int i = 0; i < Location.values().length; i++) {
            questionIcons[i] = new JLabel(AbstractToken.QUESTION_ICON);
            int width = AbstractToken.QUESTION_ICON.getIconWidth();
            int Height = AbstractToken.QUESTION_ICON.getIconHeight();

            questionIcons[i].setBounds(PADDING_LEFT + TILE_WIDTH * NO_BRAINER_POS[i][0],
                    PADDING_TOP + TILE_WIDTH * NO_BRAINER_POS[i][1], width, Height);

            questionIcons[i].setVisible(false);
        }

        return questionIcons;
    }

    private static JLabel[] createCrossOnRoom() {
        JLabel[] crossIcons = new JLabel[Location.values().length];
        for (int i = 0; i < Location.values().length; i++) {
            crossIcons[i] = new JLabel(AbstractToken.CROSS_ICON);
            int width = AbstractToken.CROSS_ICON.getIconWidth();
            int Height = AbstractToken.CROSS_ICON.getIconHeight();

            crossIcons[i].setBounds(PADDING_LEFT + TILE_WIDTH * NO_BRAINER_POS[i][0],
                    PADDING_TOP + TILE_WIDTH * NO_BRAINER_POS[i][1], width, Height);

            crossIcons[i].setVisible(false);
        }

        return crossIcons;
    }

    private static void addMouseListenerOnCharacterToken(CharacterToken jlabel) {
        // TODO Auto-generated method stub
        jlabel.addMouseListener(new MouseListener() {

            @Override
            public void mouseReleased(MouseEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void mousePressed(MouseEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void mouseExited(MouseEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void mouseEntered(MouseEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void mouseClicked(MouseEvent e) {
                // TODO Auto-generated method stub
                System.out.println(jlabel.getCharacter().toString() + "is clicked");

            }
        });

    }

    private static void addMouseListenerOnWeaponToken(WeaponToken jlabel) {
        // TODO Auto-generated method stub
        jlabel.addMouseListener(new MouseListener() {

            @Override
            public void mouseReleased(MouseEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void mousePressed(MouseEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void mouseExited(MouseEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void mouseEntered(MouseEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void mouseClicked(MouseEvent e) {
                // TODO Auto-generated method stub

            }
        });

    }

    private void addMouseListenerOnBoard(BoardCanvas boardCanvas) {
        // TODO Auto-generated method stub
        boardCanvas.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
                // TODO Auto-generated method stub

                int x = (e.getX() - PADDING_LEFT) / TILE_WIDTH;
                int y = (e.getY() - PADDING_TOP) / TILE_WIDTH;

                System.out.println("clicked on Tile(" + x + ", " + y + ")");

            }

            @Override
            public void mousePressed(MouseEvent e) {
                // TODO Auto-generated method stub

                int x = (e.getX() - PADDING_LEFT) / TILE_WIDTH;
                int y = (e.getY() - PADDING_TOP) / TILE_WIDTH;

                System.out.println("pressed on Tile(" + x + ", " + y + ")");

            }

            @Override
            public void mouseReleased(MouseEvent e) {
                // TODO Auto-generated method stub

                int x = (e.getX() - PADDING_LEFT) / TILE_WIDTH;
                int y = (e.getY() - PADDING_TOP) / TILE_WIDTH;

                System.out.println("released on Tile(" + x + ", " + y + ")");

            }

            @Override
            public void mouseEntered(MouseEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void mouseExited(MouseEvent e) {
                // TODO Auto-generated method stub

            }
        });
    }

    public CharacterToken[] getCharacterTokens() {
        return characterTokens;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(GUIClient.GAME_BOARD, PADDING_LEFT, PADDING_TOP, BOARD_IMG_WIDTH,
                BOARD_IMG_HEIGHT, this);
        // TODO draw rectangle on available moving positions

    }

}
