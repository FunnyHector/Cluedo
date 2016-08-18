package ui;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.awt.image.ImageObserver;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import game.Game;
import game.GameError;
import view.BoardCanvas;
import view.CustomMenu;
import view.NumberSetupDialog;
import view.PlayerPanelCanvas;
import view.PlayerSetupDialog;
import card.Character;

public class GUIClient extends JFrame implements ActionListener, KeyListener {

    private static final String IMAGE_PATH = "resources/";

    public static final Image[] CHARACTER_IMG = {
            loadImage("Character_Miss_Scarlet.png"),
            loadImage("Character_Colonel_Mustard.png"),
            loadImage("Character_Mrs_White.png"),
            loadImage("Character_The_Reverend_Green.png"),
            loadImage("Character_Mrs_Peacock.png"),
            loadImage("Character_Professor_Plum.png") };

    public static final Image[] Weapon_IMAGES = { loadImage("Weapon_Candlestick.png"),
            loadImage("Weapon_Dagger.png"), loadImage("Weapon_Lead_Pipe.png"),
            loadImage("Weapon_Revolver.png"), loadImage("Weapon_Rope.png"),
            loadImage("Weapon_Spanner.png") };

    public static final Image[] Location_IMAGES = { loadImage("Location_Kitchen.png"),
            loadImage("Location_Ball_room.png"), loadImage("Location_Conservatory.png"),
            loadImage("Location_Billard_Room.png"), loadImage("Location_Library.png"),
            loadImage("Location_Study.png"), loadImage("Location_Hall.png"),
            loadImage("Location_Lounge.png"), loadImage("Location_Dining_Room.png") };

    public static final Image INIT_SCREEN = loadImage("Initial_Screen.png");

    // TODO Have to find a good board which have same width and height
    public static final Image GAME_BOARD = loadImage("Game_Board.png");



    public static final int LEFT_PANEL_WIDTH = BoardCanvas.BOARD_IMG_WIDTH
            + BoardCanvas.PADDING_LEFT + BoardCanvas.PADDING_RIGHT;
    public static final int RIGHT_PANEL_WIDTH = PlayerPanelCanvas.WIDTH
            + PlayerPanelCanvas.PADDING_LEFT + PlayerPanelCanvas.PADDING_RIGHT;
    public static final int PLAYER_PANEL_HEIGHT = PlayerPanelCanvas.HEIGHT
            + PlayerPanelCanvas.PADDING_TOP + PlayerPanelCanvas.PADDING_DOWN;
    public static final int TEXT_CONSOLE_HEIGHT = BoardCanvas.BOARD_IMG_HEIGHT
            + BoardCanvas.PADDING_TOP + BoardCanvas.PADDING_DOWN - PLAYER_PANEL_HEIGHT;

    public static final int TEXT_ROW = 30;
    public static final int TEXT_COL = 60;

    // =========== Views ================

    // the main window
    private JPanel window;

    private JTextArea textConsole;
    private BoardCanvas gameBoardPanel;
    private PlayerPanelCanvas playerPanel;

    // ============= models ===================
    private Game cluedoGame;
    private int numPlayers;
    private int numDices;

    // a helper booleans
    private boolean noBrainer;

    public GUIClient() {
        initialise();
    }

    @SuppressWarnings("serial")
    private void initialise() {

        this.setTitle("Cluedo");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // =================== Let's make menu first ===================
        JMenuBar menuBar = new CustomMenu(this);
        this.setJMenuBar(menuBar);

        // ============ then the welcome screen =====================
        window = new JPanel() {
            protected void paintComponent(Graphics g) {
                g.drawImage(INIT_SCREEN, 0, 0, null);
            }
        };
        window.setPreferredSize(
                new Dimension(INIT_SCREEN.getWidth(this), INIT_SCREEN.getHeight(this)));

        this.add(window);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setResizable(false);
        this.pack();
        this.setVisible(true);
    }

    public void setupNumPlayers() {
        @SuppressWarnings("unused")
        JDialog frame = new NumberSetupDialog(this,
                SwingUtilities.windowForComponent(this), "Setup Wizard");
    }

    public void setupPlayers() {
        @SuppressWarnings("unused")
        JDialog frame = new PlayerSetupDialog(this,
                SwingUtilities.windowForComponent(this), "Setup Wizard");
    }

    public void startGame() {

        // remove the welcome screen, and load into the game interface
        this.remove(window);

        window = new JPanel();
        window.setLayout(new BoxLayout(window, BoxLayout.X_AXIS));

        // now make the left panel, which is game board
        gameBoardPanel = new BoardCanvas(this);

        // gameBoardPanel.setBorder(BorderFactory.createEmptyBorder());

        TitledBorder gameBoardPanel_border = BorderFactory
                .createTitledBorder(
                        BorderFactory.createCompoundBorder(
                                BorderFactory.createSoftBevelBorder(BevelBorder.RAISED),
                                BorderFactory.createEtchedBorder(EtchedBorder.LOWERED)),
                        "Board", TitledBorder.CENTER, TitledBorder.TOP);

        gameBoardPanel.setBorder(gameBoardPanel_border);

        // now the right panel (player panel and a text console)
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        // rightPanel.setBorder(BorderFactory.createEmptyBorder());

        // now make the right-top split panel, which is a text console
        // TODO change it
        textConsole = new JTextArea(TEXT_ROW, TEXT_COL);
        textConsole.setLineWrap(true);
        textConsole.setWrapStyleWord(true);
        textConsole.setEditable(false);
        JScrollPane textPanel = new JScrollPane(textConsole);

        TitledBorder textPanel_border = BorderFactory
                .createTitledBorder(
                        BorderFactory.createCompoundBorder(
                                BorderFactory.createSoftBevelBorder(BevelBorder.RAISED),
                                BorderFactory.createEtchedBorder(EtchedBorder.LOWERED)),
                        "Text Console", TitledBorder.CENTER, TitledBorder.TOP);

        textPanel.setBorder(textPanel_border);

        // now make the right-bottom split panel, which is player panel (cards and dices)
        playerPanel = new PlayerPanelCanvas(this);

        TitledBorder playerPanel_border = BorderFactory
                .createTitledBorder(
                        BorderFactory.createCompoundBorder(
                                BorderFactory.createSoftBevelBorder(BevelBorder.RAISED),
                                BorderFactory.createEtchedBorder(EtchedBorder.LOWERED)),
                        "Player Panel", TitledBorder.CENTER, TitledBorder.TOP);

        playerPanel.setBorder(playerPanel_border);

        // now put up the right split panel
        rightPanel.add(textPanel);
        rightPanel.add(playerPanel);

        // now put them together
        window.add(gameBoardPanel);
        window.add(rightPanel);

        // set preferred size
        textPanel.setPreferredSize(new Dimension(RIGHT_PANEL_WIDTH, TEXT_CONSOLE_HEIGHT));
        playerPanel
                .setPreferredSize(new Dimension(RIGHT_PANEL_WIDTH, PLAYER_PANEL_HEIGHT));
        // rightPanel
        // .setPreferredSize(new Dimension(RIGHT_PANEL_WIDTH, PLAYER_PANEL_HEIGHT));

        gameBoardPanel.setPreferredSize(new Dimension(LEFT_PANEL_WIDTH,
                PLAYER_PANEL_HEIGHT + TEXT_CONSOLE_HEIGHT));
        // windowPanel.setPreferredSize(new Dimension(FULL_WIDTH, FULL_HEIGHT));
        // window.setPreferredSize(new Dimension(FULL_WIDTH, FULL_HEIGHT));

        // last
        this.add(window);

        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.pack();
        this.validate();
        this.repaint();
        this.setResizable(false);
        this.setVisible(true);

        // ============= initialise fields ================
        noBrainer = false;
        cluedoGame = new Game(numPlayers, numDices);

        System.out.println("start");

        System.out.println("===========================");

        System.out.println("gameBoardPanel width:" + gameBoardPanel.getWidth());
        System.out.println("gameBoardPanel height:" + gameBoardPanel.getHeight());
        System.out.println("gameBoardPanel + border width:"
                + gameBoardPanel_border.getMinimumSize(gameBoardPanel).getWidth());
        System.out.println("gameBoardPanel + border height:"
                + gameBoardPanel_border.getMinimumSize(gameBoardPanel).getHeight());
        System.out.println("===========================");

        System.out.println("textPanel width:" + textPanel.getWidth());
        System.out.println("textPanel height:" + textPanel.getHeight());
        System.out.println("textPanel + border width:"
                + textPanel_border.getMinimumSize(textPanel).getWidth());
        System.out.println("textPanel + border height:"
                + textPanel_border.getMinimumSize(textPanel).getHeight());
        System.out.println("===========================");

        System.out.println("playerPanel width:" + playerPanel.getWidth());
        System.out.println("playerPanel height:" + playerPanel.getHeight());
        System.out.println("playerPanel + border width:"
                + playerPanel_border.getMinimumSize(playerPanel).getWidth());
        System.out.println("playerPanel + border height:"
                + playerPanel_border.getMinimumSize(playerPanel).getHeight());
        System.out.println("===========================");

        System.out.println("rightPanel width:" + rightPanel.getWidth());
        System.out.println("rightPanel height:" + rightPanel.getHeight());
        System.out.println("window width:" + window.getWidth());
        System.out.println("window height:" + window.getHeight());
        System.out.println("Frame width:" + this.getWidth());
        System.out.println("Frame height:" + this.getHeight());

        System.out.println("===========================");

    }

    public void setNumPlayers(int numPlayers) {
        this.numPlayers = numPlayers;
    }

    public int getNumPlayers() {
        return numPlayers;
    }

    public void setNumDices(int numDices) {
        this.numDices = numDices;
    }

    public void setNobrainerMode(boolean nobrainer) {
        this.noBrainer = nobrainer;
    }

    @Override
    public void keyPressed(KeyEvent arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void keyReleased(KeyEvent arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void keyTyped(KeyEvent arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
        // TODO Auto-generated method stub

    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    GUIClient window = new GUIClient();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static Image loadImage(String filename) {
        // using the URL means the image loads when stored
        // in a jar or expanded into individual files.
        URL imageURL = BoardCanvas.class.getResource(IMAGE_PATH + filename);

        try {
            Image img = ImageIO.read(imageURL);
            return img;
        } catch (IOException e) {
            // we've encountered an error loading the image. There's not much we
            // can actually do at this point, except to abort the game.
            throw new GameError("Unable to load image: " + filename);
        }
    }

}
