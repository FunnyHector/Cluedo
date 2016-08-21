package ui;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import game.Board;
import game.Game;
import game.GameError;
import game.Player;
import game.Suggestion;
import tile.Entrance;
import tile.Position;
import tile.Room;
import tile.RoomTile;
import tile.Tile;
import view.BoardCanvas;
import view.CustomMenu;
import view.NumberSetupDialog;
import view.PlayerPanelCanvas;
import view.PlayerSetupDialog;
import view.SuggestionDialog;
import view.token.CharacterToken;
import view.token.WeaponToken;
import card.Card;
import card.Character;
import card.Location;
import card.Weapon;
import configs.Configs;

public class GUIClient extends JFrame {

    private static final String IMAGE_PATH = "resources/";
    public static final Image INIT_SCREEN = loadImage("Initial_Screen.png");
    public static final Image GAME_BOARD = loadImage("Game_Board.png");
    public static final ImageIcon CORRECT = new ImageIcon(loadImage("Icon_Correct.png"));
    public static final ImageIcon INCORRECT = new ImageIcon(
            loadImage("Icon_Incorrect.png"));
    public static final ImageIcon ACCUSE_ICON = new ImageIcon(
            loadImage("Icon_Incorrect.png"));

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

    public GUIClient() {
        welcomeScreen();
    }

    @SuppressWarnings("serial")
    private void welcomeScreen() {

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
     * Set the given player as human controlled, give it a name.
     * 
     * @param playerChoice
     *            --- the character chosen by a player
     * @param name
     *            --- the customised name
     */
    public void joinPlayer(Character playerChoice, String name) {
        game.joinPlayer(playerChoice, name);
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

        // This is to prevent the window from losing focus after JPanel repaint()
        window.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseMoved(MouseEvent e) {
                window.requestFocusInWindow();
            }

            @Override
            public void mouseDragged(MouseEvent e) {
            }
        });

        // Add a keyAdaptor
        window.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                int k = e.getKeyCode();
                switch (k) {
                case KeyEvent.VK_UP:
                case KeyEvent.VK_W:
                    playerPanel.tryClickOnUp();
                    break;
                case KeyEvent.VK_DOWN:
                case KeyEvent.VK_S:
                    playerPanel.tryClickOnDown();
                    break;
                case KeyEvent.VK_RIGHT:
                case KeyEvent.VK_D:
                    playerPanel.tryClickOnRight();
                    break;
                case KeyEvent.VK_LEFT:
                case KeyEvent.VK_A:
                    playerPanel.tryClickOnLeft();
                    break;
                case KeyEvent.VK_SPACE:
                    playerPanel.tryClickOnLeft();
                    break;
                default:
                }
            }
        });

        ((CustomMenu) this.getJMenuBar()).enableNoBrainerMenu();

        // last
        this.add(window);

        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.pack();
        this.validate();
        this.repaint();
        this.setResizable(false);
        this.setVisible(true);
    }

    public void update() {
        if (game.isGameRunning()) {
            gameBoardPanel.update();
            playerPanel.update();
            window.requestFocusInWindow();
        } else {
            int choice = JOptionPane.showConfirmDialog(window,
                    getCurrentPlayer().toString()
                            + " are the only player left. Congratulations, you are the winner!\n"
                            + "Do you want to play again?",
                    getCurrentPlayer().toString() + " won!", JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.INFORMATION_MESSAGE, CORRECT);

            if (choice == JOptionPane.OK_OPTION) {
                setupNumPlayers();
            }
            window.requestFocusInWindow();
        }
    }

    public void popUpSuggestion() {
        new SuggestionDialog(this, SwingUtilities.windowForComponent(this),
                "Make a Suggestion", false);

    }

    public void makeSuggestion(Character c, Weapon w, Location l) {
        Suggestion suggestion = new Suggestion(c, w, l);
        movePlayer(suggestion.character, Configs.getRoom(suggestion.location));
        moveWeapon(suggestion.weapon, getAvailableRoomTile(suggestion.location));
        String s = game.refuteSuggestion(suggestion);

        JOptionPane.showMessageDialog(window, s, "Refution from other players",
                JOptionPane.INFORMATION_MESSAGE);

        int choice = JOptionPane.showConfirmDialog(window,
                "Do you want to make an accusation right away?", "Make Accusation?",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE,
                ACCUSE_ICON);

        if (choice == JOptionPane.OK_OPTION) {
            popUpAccusation();
        }

    }

    public void popUpAccusation() {
        new SuggestionDialog(this, SwingUtilities.windowForComponent(this),
                "Make an Accusation", true);
    }

    public void makeAccusation(Character c, Weapon w, Location l) {

        Suggestion suggestion = new Suggestion(c, w, l);
        movePlayer(suggestion.character, Configs.getRoom(suggestion.location));
        moveWeapon(suggestion.weapon, getAvailableRoomTile(suggestion.location));

        boolean isCorrect = game.checkAccusation(new Suggestion(c, w, l));
        if (isCorrect) {
            int choice = JOptionPane.showConfirmDialog(window,
                    "Your accusation is correct.\nCongratulations, "
                            + getCurrentPlayer().toString() + "("
                            + game.getPlayerByCharacter(getCurrentPlayer()).getName()
                            + ") is the winner!\nDo you want to play again?",
                    "WINNER!", JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.INFORMATION_MESSAGE, CORRECT);

            if (choice == JOptionPane.OK_OPTION) {
                setupNumPlayers();
            }

        } else {
            JOptionPane.showMessageDialog(window,
                    "Your accusation is WRONG, you are out!", "Incorrect",
                    JOptionPane.ERROR_MESSAGE, INCORRECT);
        }
    }

    /**
     * This method gets all cards that is known as not involved in crime.
     * 
     * @return --- all cards that is known as not involved in crime.
     */
    public Set<Card> getKnownCards() {
        return game.getKnownCards();
    }

    /**
     * Let the player roll dices.
     *
     * @return --- an array of integer, whose length is the number of dice, and each
     *         number is the rolled number of individual dice. Here we use 0 to 5 to
     *         represents 1 - 6 (for simplicity when calling graphical update)
     */
    public int[] rollDice(Character character) {
        return game.rollDice(character);
    }

    /**
     * let current player end turn.
     */
    public void currentPlayerEndTurn() {
        game.currentPlayerEndTurn();
    }

    /**
     * Move a character to the given position.
     * 
     * @param character
     *            --- the character to be moved
     * @param position
     *            --- where to move
     */
    public void movePlayer(Character character, Position position) {

        game.movePlayer(character, position);
        CharacterToken[] characterTokens = gameBoardPanel.getCharacterTokens();

        if (position instanceof Tile) {
            Tile tile = (Tile) position;
            characterTokens[character.ordinal()].moveToTile(tile);
        } else if (position instanceof Room) {
            Room room = (Room) position;
            RoomTile destRoomTile = getAvailableRoomTile(room.getRoom());
            characterTokens[character.ordinal()].setRoomTile(destRoomTile);
        }
    }

    /**
     * Move a weapon to the given room.
     * 
     * @param weapon
     *            --- the character to be moved
     * @param roomTile
     *            --- which room to move into, and on which tile is this token put
     */
    public void moveWeapon(Weapon weapon, RoomTile roomTile) {
        game.moveWeapon(weapon, roomTile);
        WeaponToken[] weaponTokens = game.getWeaponTokens();
        weaponTokens[weapon.ordinal()].setRoomTile(roomTile);
    }

    public int getNumPlayers() {
        return numPlayers;
    }

    public int getNumDices() {
        return numDices;
    }

    public Board getBoard() {
        return game.getBoard();
    }

    public List<Player> getAllPlayers() {
        return game.getPlayers();
    }

    /**
     * A helper method to get the corresponding Player of given Character.
     * 
     * @param character
     *            --- the given character
     * @return
     */
    public Player getPlayerByCharacter(Character character) {
        return game.getPlayerByCharacter(character);
    }

    /**
     * Get all weapon tokens as a list
     * 
     * @return --- all weapon tokens
     */
    public WeaponToken[] getWeaponTokens() {
        return game.getWeaponTokens();
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

    public void setNobrainerMode(boolean isNobrainerMode) {
        game.setNoBrainerMode(isNobrainerMode);
    }

    public boolean isNoBrainerMode() {
        return game.isNoBrainerMode();
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

    public RoomTile getAvailableRoomTile(Location location) {
        return game.getAvailableRoomTile(location);
    }

    /**
     * get the start position of given character.
     * 
     * @param character
     *            --- the character
     * @return --- the start position of this character
     */
    public Tile getStartPosition(Character character) {
        return game.getStartPosition(character);
    }

    /**
     * Get the player's position.
     * 
     * @param character
     *            --- the player
     * @return --- the player's position
     */
    public Position getPlayerPosition(Character character) {
        return game.getPlayerPosition(character);
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

    /**
     * Set how many steps left for the player to move.
     * 
     * @param character
     *            --- the player
     * @param remainingSteps
     *            --- how many steps left for the player to move.
     */
    public void setRemainingSteps(Character character, int remainingSteps) {
        game.setRemainingSteps(character, remainingSteps);
    }

    /**
     * This method checks the given character's position, and returns all possible
     * positions to move to. The positions in the list returned will be of a certain
     * order, which is: north tile -> east tile -> south tile -> west tile -> room if
     * standing at an entrance -> exits (entrances) if in a room -> room if via the secret
     * passage in current room. Any position that cannot be accessible will not be added
     * in this list. In particular, a tile on which has another player standing will not
     * be added in.<br>
     * <br>
     * This ensured order is to make the option menu more predictable.
     * 
     * @param character
     *            --- the player
     * @return --- a list of positions that are all movable.
     */
    public List<Position> getMovablePositions(Character character) {
        return game.getMovablePositions(character);
    }

    private TitledBorder creatTitledBorder(String string) {
        return BorderFactory
                .createTitledBorder(
                        BorderFactory.createCompoundBorder(
                                BorderFactory.createSoftBevelBorder(BevelBorder.RAISED),
                                BorderFactory.createEtchedBorder(EtchedBorder.LOWERED)),
                        string, TitledBorder.CENTER, TitledBorder.TOP);
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
}
