package view;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JPanel;

import configs.Configs;
import game.GameError;
import ui.GUIClient;

public class BoardCanvas extends JPanel {

    private GUIClient gui;

    // The game board image is made according to this pixel size
    public static final int TILE_WIDTH = 32;

    public static final int BOARD_IMG_WIDTH = TILE_WIDTH * Configs.BOARD_WIDTH;
    public static final int BOARD_IMG_HEIGHT = TILE_WIDTH * Configs.BOARD_HEIGHT;

    public static final int PADDING_LEFT = 10;
    public static final int PADDING_RIGHT = PADDING_LEFT;
    public static final int PADDING_TOP = 20;
    public static final int PADDING_DOWN = PADDING_LEFT;

    public BoardCanvas(GUIClient guiClient) {
        super();
        this.gui = guiClient;
    }

    @Override
    protected void paintComponent(Graphics g) {

        // g.setColor(Color.red);
        // g.fillRect(PADDING_LEFT, PADDING_TOP, BOARD_IMG_WIDTH, BOARD_IMG_HEIGH);

        g.drawImage(GUIClient.GAME_BOARD, PADDING_LEFT, PADDING_TOP, BOARD_IMG_WIDTH,
                BOARD_IMG_HEIGHT, this);

    }

}
