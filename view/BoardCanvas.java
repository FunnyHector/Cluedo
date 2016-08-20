package view;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import configs.Configs;
import card.Character;
import card.Weapon;
import game.GameError;
import tile.Room;
import tile.Tile;
import ui.GUIClient;

import static ui.GUIClient.loadImage;

public class BoardCanvas extends JPanel {

    public static final ImageIcon[] CHARACTER_TOKEN_IMG = {
            new ImageIcon(loadImage("Token_Miss_Scarlet.png")),
            new ImageIcon(loadImage("Token_Colonel_Mustard.png")),
            new ImageIcon(loadImage("Token_Mrs_White.png")),
            new ImageIcon(loadImage("Token_The_Reverend_Green.png")),
            new ImageIcon(loadImage("Token_Mrs_Peacock.png")),
            new ImageIcon(loadImage("Token_Professor_Plum.png")) };

    public static final CharacterToken[] CHRACTER_TOKENS = createCharacterToken(
            CHARACTER_TOKEN_IMG);

    public static final ImageIcon[] WEAPON_TOKEN_IMG = {
            new ImageIcon(loadImage("Token_Candlestick.png")),
            new ImageIcon(loadImage("Token_Dagger.png")),
            new ImageIcon(loadImage("Token_Lead_Pipe.png")),
            new ImageIcon(loadImage("Token_Revolver.png")),
            new ImageIcon(loadImage("Token_Rope.png")),
            new ImageIcon(loadImage("Token_Spanner.png")) };

    public static final WeaponToken[] WEAPON_TOKENS = createcreateWeaponToken(
            WEAPON_TOKEN_IMG);

    // The game board image is made according to this pixel size
    public static final int TILE_WIDTH = 32;

    public static final int BOARD_IMG_WIDTH = TILE_WIDTH * Configs.BOARD_WIDTH;
    public static final int BOARD_IMG_HEIGHT = TILE_WIDTH * Configs.BOARD_HEIGHT;

    public static final int PADDING_LEFT = 10;
    public static final int PADDING_RIGHT = PADDING_LEFT;
    public static final int PADDING_TOP = 20;
    public static final int PADDING_DOWN = PADDING_LEFT;

    private GUIClient gui;

    public BoardCanvas(GUIClient guiClient) {
        super();
        this.gui = guiClient;

        addMouseListenerOnBoard(this);

        // we use absolute layout
        this.setLayout(null);

        // add character tokens
        for (int i = 0; i < CHRACTER_TOKENS.length; i++) {
            this.add(CHRACTER_TOKENS[i]);
            // TODO need to change the position
            setCharacterTokenOnPos(CHRACTER_TOKENS[i], 2 * i, 3 * i);
        }

        // add weapon tokens
        for (int i = 0; i < WEAPON_TOKENS.length; i++) {
            this.add(WEAPON_TOKENS[i]);
            // TODO need to change the position
            // setCharacterTokenOnPos(WEAPON_TOKENS[i], 3 * i, 2 * i);

        }

    }

    private void setCharacterTokenOnPos(CharacterToken characterToken, int x, int y) {
        int height = characterToken.getIcon().getIconHeight();
        int width = characterToken.getIcon().getIconWidth();
        characterToken.setBounds(PADDING_LEFT + TILE_WIDTH * x,
                PADDING_TOP + TILE_WIDTH * y - (height - TILE_WIDTH + 2), width, height);
    }

    private void setWeaponTokenInRoom(Graphics g, ImageIcon tokenImg, Room room) {
        // TODO Auto-generated method stub
    }

    private static CharacterToken[] createCharacterToken(ImageIcon[] charactertokenImg) {
        CharacterToken[] tokens = new CharacterToken[charactertokenImg.length];
        for (int i = 0; i < charactertokenImg.length; i++) {
            Character c = Character.get(i);
            tokens[i] = new CharacterToken(charactertokenImg[i], c);
            tokens[i].setBorder(null);
            tokens[i].setToolTipText(c.toString());
            addMouseListenerOnCharacterToken(tokens[i]);
        }
        return tokens;
    }

    private static WeaponToken[] createcreateWeaponToken(ImageIcon[] weaponTokenImg) {

        WeaponToken[] tokens = new WeaponToken[weaponTokenImg.length];
        for (int i = 0; i < weaponTokenImg.length; i++) {
            Weapon w = Weapon.get(i);
            tokens[i] = new WeaponToken(weaponTokenImg[i], w);
            tokens[i].setBorder(null);
            tokens[i].setToolTipText(w.toString());
            addMouseListenerOnWeaponToken(tokens[i]);
        }
        return tokens;
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

                int x = (e.getX() - PADDING_LEFT) / 32;
                int y = (e.getY() - PADDING_TOP) / 32;

                System.out.println();

                System.out.println("clicked on Tile(" + x + ", " + y + ")");

            }

            @Override
            public void mousePressed(MouseEvent e) {
                // TODO Auto-generated method stub

                int x = (e.getX() - PADDING_LEFT) / 32;
                int y = (e.getY() - PADDING_TOP) / 32;

                System.out.println("pressed on Tile(" + x + ", " + y + ")");

            }

            @Override
            public void mouseReleased(MouseEvent e) {
                // TODO Auto-generated method stub

                int x = (e.getX() - PADDING_LEFT) / 32;
                int y = (e.getY() - PADDING_TOP) / 32;

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

    @Override
    protected void paintComponent(Graphics g) {

        // g.setColor(Color.red);
        // g.fillRect(PADDING_LEFT, PADDING_TOP, BOARD_IMG_WIDTH, BOARD_IMG_HEIGH);

        g.drawImage(GUIClient.GAME_BOARD, PADDING_LEFT, PADDING_TOP, BOARD_IMG_WIDTH,
                BOARD_IMG_HEIGHT, this);

    }

}

class CharacterToken extends JLabel {

    private Character character;

    public CharacterToken(ImageIcon img, Character character) {
        super(img);
        this.character = character;
    }

    public Character getCharacter() {
        return character;
    }
}

class WeaponToken extends JLabel {

    private Weapon weapon;

    public WeaponToken(ImageIcon img, Weapon weapon) {
        super(img);
        this.weapon = weapon;
    }

    public Weapon getCharacter() {
        return weapon;
    }
}