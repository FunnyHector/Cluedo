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
import game.Suggestion;
import tile.Position;
import view.BoardCanvas;
import view.CustomMenu;
import view.NumberSetupDialog;
import view.PlayerPanelCanvas;
import view.PlayerSetupDialog;
import view.SuggestionDialog;
import card.Card;
import card.Character;
import card.Location;
import card.Weapon;

public class GUIClient extends JFrame implements KeyListener {

    private static final String IMAGE_PATH = "resources/";
    public static final Image INIT_SCREEN = loadImage("Initial_Screen.png");
    public static final Image GAME_BOARD = loadImage("Game_Board.png");

    public static final int HEIGHT = BoardCanvas.BOARD_IMG_HEIGHT
            + BoardCanvas.PADDING_TOP + BoardCanvas.PADDING_DOWN;
    public static final int LEFT_PANEL_WIDTH = BoardCanvas.BOARD_IMG_WIDTH
            + BoardCanvas.PADDING_LEFT + BoardCanvas.PADDING_RIGHT;
    public static final int RIGHT_PANEL_WIDTH = PlayerPanelCanvas.WIDTH
            + PlayerPanelCanvas.PADDING_LEFT + PlayerPanelCanvas.PADDING_RIGHT;

    public static final int TEXT_ROW = 20;
    public static final int TEXT_COL = 50;

    // =========== Views ================

    // the main window
    private JPanel window;
    // game board on left
    private BoardCanvas gameBoardPanel;
    // player panel on right bottom
    private PlayerPanelCanvas playerPanel;

    // ============= models ===================
    private Game game;

    // some fields to remember game status
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

    public void createNewGame(int numPlayers, int numDices) {
        this.numPlayers = numPlayers;
        this.numDices = numDices;
        game = new Game(numPlayers, numDices);
    }

    /**
     * Set the given player as human controlled
     * 
     * @param playerChoice
     *            --- the character chosen by a player
     */
    public void joinPlayer(Character playerChoice) {
        game.joinPlayer(playerChoice);
    }

    /**
     * This method sets who the first character is to move.
     */
    public void setPlayerMoveFirst() {
        game.setPlayerMoveFirst();
    }

    /**
     * This method randomly choose one character, one room, and one weapon to create a
     * solution, then shuffles all remaining cards, and deal them evenly to all players.
     */
    public void creatSolutionAndDealCards() {
        game.creatSolution();
        game.dealCard();
    }

    public void startGame() {

        // remove the welcome screen, and load into the game interface
        this.remove(window);

        window = new JPanel();
        window.setLayout(new BoxLayout(window, BoxLayout.X_AXIS));

        // now make the left panel, which is game board
        gameBoardPanel = new BoardCanvas(this);

        // gameBoardPanel.setBorder(BorderFactory.createEmptyBorder());

        TitledBorder gameBoardPanel_border = creatTitledBorder("Board");

        gameBoardPanel.setBorder(gameBoardPanel_border);

        // now the right panel (player panel and a text console)
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        // rightPanel.setBorder(BorderFactory.createEmptyBorder());

        // now make the right-bottom split panel, which is player panel (cards and dices)
        playerPanel = new PlayerPanelCanvas(this);

        TitledBorder playerPanel_border = creatTitledBorder("Player Panel");

        playerPanel.setBorder(playerPanel_border);

        // now put up the right split panel
        rightPanel.add(playerPanel);

        // now put them together
        window.add(gameBoardPanel);
        window.add(rightPanel);

        // set preferred size
        playerPanel.setPreferredSize(new Dimension(RIGHT_PANEL_WIDTH, HEIGHT));
        gameBoardPanel.setPreferredSize(new Dimension(LEFT_PANEL_WIDTH, HEIGHT));

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

        // game.joinPlayer(playerChoice);

    }

    private TitledBorder creatTitledBorder(String string) {
        return BorderFactory
                .createTitledBorder(
                        BorderFactory.createCompoundBorder(
                                BorderFactory.createSoftBevelBorder(BevelBorder.RAISED),
                                BorderFactory.createEtchedBorder(EtchedBorder.LOWERED)),
                        string, TitledBorder.CENTER, TitledBorder.TOP);
    }

    public void popUpSuggestion() {
        new SuggestionDialog(this, SwingUtilities.windowForComponent(this),
                "Make a Suggestion", false);
    }

    public void makeSuggestion(Character c, Weapon w, Location l) {
        game.makeSuggestion(new Suggestion(c, w, l));
    }

    public void popUpAccusation() {
        new SuggestionDialog(this, SwingUtilities.windowForComponent(this),
                "Make an Accusation", true);
    }

    public void makeAccusation(Character c, Weapon w, Location l) {
        game.checkAccusation(new Suggestion(c, w, l));

    }

    /**
     * Let the player roll dices.
     *
     * @return --- the number rolled
     */
    public int[] rollDice(Character character) {
        return game.rollDice(character);
    }

    public int getNumPlayers() {
        return numPlayers;
    }

    public int getNumDices() {
        return numDices;
    }

    /**
     * Get the remaining cards as a list. Note that the returned list could be empty if
     * all cards are dealt.
     * 
     * @return --- the remaining cards as a list
     */
    public List<Card> getRemainingCards() {
        return game.getRemainingCards();
    }

    public void setNobrainerMode(boolean nobrainer) {
        this.noBrainer = nobrainer;
    }

    public boolean isGameRunning() {
        if (game == null) {
            return false;
        } else {
            return game.isGameRunning();
        }
    }

    public Position getPosition(int x, int y) {
        return game.getPosition(x, y);
    }

    /**
     * Get the player who need to move.
     * 
     * @return --- the current player
     */
    public Character getCurrentPlayer() {
        return game.getCurrentPlayer();
    }

    /**
     * Get how many steps left for the player to move.
     * 
     * @param character
     *            --- the player
     * @return --- how many steps left for the player to move.
     */
    public int getRemainingSteps(Character character) {
        return game.getRemainingSteps(character);
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

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new GUIClient();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static Image loadImage(String filename) {
        URL imageURL = BoardCanvas.class.getResource(IMAGE_PATH + filename);
        try {
            Image img = ImageIO.read(imageURL);
            return img;
        } catch (IOException e) {
            throw new GameError("Unable to load image: " + filename);
        }
    }

}
