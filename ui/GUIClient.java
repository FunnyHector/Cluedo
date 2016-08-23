package ui;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Set;
import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import game.Board;
import game.Game;
import game.GameError;
import game.Player;
import game.Suggestion;
import tile.Position;
import tile.Room;
import tile.RoomTile;
import tile.Tile;
import view.BoardCanvas;
import view.CustomMenu;
import view.HelpDialog;
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

/**
 * A GUI client for Cluedo game.
 * 
 * @author Hector
 *
 */
@SuppressWarnings("serial")
public class GUIClient extends JFrame {

    /**
     * The height of main frame
     */
    public static final int HEIGHT = BoardCanvas.BOARD_IMG_HEIGHT
            + BoardCanvas.PADDING_TOP + BoardCanvas.PADDING_DOWN;
    /**
     * The width of game board (left panel)
     */
    public static final int LEFT_PANEL_WIDTH = BoardCanvas.BOARD_IMG_WIDTH
            + BoardCanvas.PADDING_LEFT + BoardCanvas.PADDING_RIGHT;
    /**
     * the width of game board (right panel)
     */
    public static final int RIGHT_PANEL_WIDTH = PlayerPanelCanvas.WIDTH
            + PlayerPanelCanvas.PADDING_LEFT + PlayerPanelCanvas.PADDING_RIGHT;

    // =========== Views ================

    /**
     * the main window
     */
    private JPanel window;
    /**
     * game board on left
     */
    private BoardCanvas boardPanel;
    /**
     * player panel on right
     */
    private PlayerPanelCanvas playerPanel;

    // ============= models ===================

    /**
     * the game
     */
    private Game game;

    /**
     * the number of players
     */
    private int numPlayers;
    /**
     * the number of dices
     */
    private int numDices;

    /**
     * Construct a GUI to run Cluedo
     */
    public GUIClient() {
        welcomeScreen();
    }

    /**
     * Initialise the main frame, menuBar, and a welcome screen
     */
    private void welcomeScreen() {

        this.setTitle("Cluedo");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // =================== Let's make menu first ===================
        JMenuBar menuBar = new CustomMenu(this);
        this.setJMenuBar(menuBar);

        // ============ then the welcome screen =====================
        window = new JPanel() {
            protected void paintComponent(Graphics g) {
                g.drawImage(INIT_SCREEN, 0, 0, this);
            }
        };
        window.setPreferredSize(
                new Dimension(INIT_SCREEN.getWidth(this), INIT_SCREEN.getHeight(this)));

        // pack and ... display (SHOUT: why not save?)
        this.add(window);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setResizable(false);
        this.pack();
        this.setVisible(true);
    }

    /**
     * Pop up a dialog to setup how many players and how many dices are used in game
     */
    public void setupNumPlayers() {
        new NumberSetupDialog(this, SwingUtilities.windowForComponent(this),
                "Setup Wizard");
    }

    /**
     * Pop up a dialog to join players
     */
    public void setupPlayers() {
        new PlayerSetupDialog(this, SwingUtilities.windowForComponent(this),
                "Join Players");
    }

    /**
     * Initialise the game with the given number of players and number of dices
     * 
     * @param numPlayers
     *            --- how many players
     * @param numDices
     *            --- how many dices are used in game
     */
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
     * This method construct the in-game GUI, and let the game begin.
     */
    public void startGame() {

        // first let's finish initialising the game
        game.decideWhoMoveFirst();
        game.creatSolution();
        game.dealCard();

        // remove the welcome screen, and load into the game interface
        this.remove(window);
        window = new JPanel();
        window.setLayout(new BoxLayout(window, BoxLayout.X_AXIS));

        // now make the left panel, which is game board
        boardPanel = new BoardCanvas(this);
        boardPanel.setPreferredSize(new Dimension(LEFT_PANEL_WIDTH, HEIGHT));
        boardPanel.setBorder(creatTitledBorder("Board"));

        // now the right panel (player panel)
        playerPanel = new PlayerPanelCanvas(this);
        playerPanel.setPreferredSize(new Dimension(RIGHT_PANEL_WIDTH, HEIGHT));
        playerPanel.setBorder(creatTitledBorder("Player Panel"));

        // now put them together
        window.add(boardPanel);
        window.add(playerPanel);

        // add key bindings
        addKeyBindings(window);

        // enable the no brainer mode on menu
        ((CustomMenu) this.getJMenuBar()).enableEasyModeMenu();

        // last, pack and display
        this.add(window);
        this.pack();
        this.validate();
        this.setResizable(false);
        this.setVisible(true);
    }

    /**
     * This method updates game board and player panel display according to the model
     * (game).
     */
    public void update() {
        if (game.isGameRunning()) {
            boardPanel.update();
            playerPanel.update();
        } else {
            // game stopped, we must have a winner
            int choice = JOptionPane.showConfirmDialog(window,
                    game.getWinner().toString()
                            + " are the only player left. Congratulations, "
                            + game.getPlayerByCharacter(game.getWinner()).getName()
                            + " are the winner!\n" + "Do you want to play again?",
                    game.getWinner().toString() + " won!", JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.INFORMATION_MESSAGE, CORRECT);

            if (choice == JOptionPane.OK_OPTION) {
                // start a new game
                setupNumPlayers();
            } else {
                // exit
                System.exit(0);
            }
        }
    }

    /**
     * Pop up a help dialog.
     */
    public void popUpHelp() {
        new HelpDialog(this, SwingUtilities.windowForComponent(this), "Help");
    }

    /**
     * Pop up a dialog for player to make suggestion.
     */
    public void popUpSuggestion() {
        new SuggestionDialog(this, SwingUtilities.windowForComponent(this),
                "Make a Suggestion", false);

    }

    /**
     * After the player has made his suggestion, this method evaluate the suggestion, and
     * pop up a option panel to show how other players refuted this suggestion.
     * 
     * @param sug
     *            --- the suggestion made by player
     */
    public void makeSuggestion(Suggestion sug) {
        // move the involved character and weapon into the involved location
        movePlayer(sug.character, Configs.getRoom(sug.location));
        moveWeapon(sug.weapon, getAvailableRoomTile(sug.location));

        // let's see how others refute it
        String s = game.refuteSuggestion(sug);
        JOptionPane.showMessageDialog(window, s, "Refution from other players",
                JOptionPane.INFORMATION_MESSAGE);

        /*
         * After the player has made a suggestion, he can choose to make an accusation
         * right away
         */
        int choice = JOptionPane.showConfirmDialog(window,
                "Do you want to make an accusation right away?", "Make Accusation?",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE,
                ACCUSE_ICON);

        if (choice == JOptionPane.OK_OPTION) {
            popUpAccusation();
        }
    }

    /**
     * Pop up a dialog for player to make accusation.
     */
    public void popUpAccusation() {
        new SuggestionDialog(this, SwingUtilities.windowForComponent(this),
                "Make an Accusation", true);
    }

    /**
     * After the player has made his accusation, this method evaluate the suggestion, and
     * pop up a option panel to show if he is the winner or he loses the game.
     * 
     * @param accusation
     *            --- the accusation made by player
     */
    public void makeAccusation(Suggestion accusation) {
        // move the involved character and weapon into the involved location
        movePlayer(accusation.character, Configs.getRoom(accusation.location));
        moveWeapon(accusation.weapon, getAvailableRoomTile(accusation.location));

        // let's see if the accusation is right or wrong
        boolean isCorrect = game.checkAccusation(accusation);
        if (isCorrect) {
            int choice = JOptionPane.showConfirmDialog(window,
                    "Your accusation is correct.\nCongratulations, "
                            + getCurrentPlayer().toString() + "("
                            + game.getPlayerByCharacter(getCurrentPlayer()).getName()
                            + ") is the winner!\n Do you want to play again?",
                    "WINNER!", JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.INFORMATION_MESSAGE, CORRECT);

            // start a new game or quit
            if (choice == JOptionPane.OK_OPTION) {
                setupNumPlayers();
            } else {
                System.exit(0);
            }

        } else {
            JOptionPane.showMessageDialog(window,
                    "Your accusation is WRONG, you are out!", "Incorrect",
                    JOptionPane.ERROR_MESSAGE, INCORRECT);
        }
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
        // move the player
        game.movePlayer(character, position);
        // we move the corresponding character token as well
        CharacterToken[] characterTokens = boardPanel.getCharacterTokens();
        if (position instanceof Tile) {
            // Tile tile = (Tile) position;
            // characterTokens[character.ordinal()].moveToTile(tile);
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
        // move the weapon
        game.moveWeapon(weapon, roomTile);
        // move the corresponding weapon token as well
        WeaponToken[] weaponTokens = game.getWeaponTokens();
        weaponTokens[weapon.ordinal()].setRoomTile(roomTile);
    }

    /**
     * Get the number of players
     * 
     * @return --- the number of players (3 to 6 inclusive)
     */
    public int getNumPlayers() {
        return numPlayers;
    }

    /**
     * Get the number of dices
     * 
     * @return --- the number of dices (1 to 3 inclusive)
     */
    public int getNumDices() {
        return numDices;
    }

    /**
     * Get the game board
     * 
     * @return --- the game board
     */
    public Board getBoard() {
        return game.getBoard();
    }

    /**
     * Get all players (including dummy token not controlled by human).
     * 
     * @return --- all players (including dummy token not controlled by human) as a list
     */
    public List<Player> getAllPlayers() {
        return game.getPlayers();
    }

    /**
     * Get the player who needs to move.
     * 
     * @return --- the current player
     */
    public Character getCurrentPlayer() {
        return game.getCurrentPlayer();
    }

    /**
     * A helper method to get the corresponding Player of given Character.
     * 
     * @param character
     *            --- the given character
     * @return --- the corresponding Player of given Character
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

    /**
     * Is the game run on easy mode?
     * 
     * @return --- true if the game run on easy mode, or false if not.
     */
    public boolean isEasyMode() {
        return game.isEasyMode();
    }

    /**
     * Set the game to easy mode (so that the game will remember clues for
     * player...cheating).
     * 
     * @param isEasyMode
     *            --- a flag to turn on or off easy mode
     */
    public void setEasyMode(boolean isEasyMode) {
        game.setEasyMode(isEasyMode);
    }

    /**
     * Whether game has a winner (i.e. game end)
     * 
     * @return --- true if game is still running, there is no winner yet; false if not.
     */
    public boolean isGameRunning() {
        if (game == null) {
            return false;
        } else {
            return game.isGameRunning();
        }
    }

    /**
     * This method finds the next empty spot in a given room to display player or weapon
     * tokens.
     * 
     * @param location
     *            --- which room we want to display a token
     * @return --- an empty spot to display a token in the given room, or null if the room
     *         is full (impossible to happen with the default board)
     */
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
     * This method gets all cards that is known as not involved in crime.
     * 
     * @return --- all cards that is known as not involved in crime.
     */
    public Set<Card> getKnownCards() {
        return game.getKnownCards();
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

    /**
     * This method does key bindings:<br>
     * <br>
     * W/up arrow for moving up<br>
     * S/down arrow for moving down<br>
     * A/left arrow for moving left<br>
     * D/right arrow for moving right<br>
     * Q fir entering/exiting room<br>
     * E for taking secret passage<br>
     * Space bar for rolling dice<br>
     * Esc for Ending turn.
     */
    private void addKeyBindings(JPanel jpanel) {
        InputMap inputMap = jpanel.getInputMap();
        ActionMap actionMap = jpanel.getActionMap();

        // add UP / W as short-cut key
        Action moveUp = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                playerPanel.tryClickOnUp();
            }
        };
        inputMap.put(KeyStroke.getKeyStroke('w'), "moveUp");
        inputMap.put(KeyStroke.getKeyStroke('W'), "moveUp");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "moveUp");
        actionMap.put("moveUp", moveUp);

        // add DOWN / S as short-cut key
        Action moveDown = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                playerPanel.tryClickOnDown();
            }
        };
        inputMap.put(KeyStroke.getKeyStroke('s'), "moveDown");
        inputMap.put(KeyStroke.getKeyStroke('S'), "moveDown");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "moveDown");
        actionMap.put("moveDown", moveDown);

        // add LEFT / A as short-cut key
        Action moveLeft = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                playerPanel.tryClickOnLeft();
            }
        };
        inputMap.put(KeyStroke.getKeyStroke('a'), "moveLeft");
        inputMap.put(KeyStroke.getKeyStroke('A'), "moveLeft");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "moveLeft");
        actionMap.put("moveLeft", moveLeft);

        // add RIGHT / D as short-cut key
        Action moveRight = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                playerPanel.tryClickOnRight();
            }
        };
        inputMap.put(KeyStroke.getKeyStroke('d'), "moveRight");
        inputMap.put(KeyStroke.getKeyStroke('D'), "moveRight");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "moveRight");
        actionMap.put("moveRight", moveRight);

        // add Q as short-cut key for entering/exiting room
        Action enterExit = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                playerPanel.tryClickOnEnterExitRoom();
            }
        };
        inputMap.put(KeyStroke.getKeyStroke('q'), "enterExit");
        inputMap.put(KeyStroke.getKeyStroke('Q'), "enterExit");
        actionMap.put("enterExit", enterExit);

        // add E as short-cut key for taking secret passage
        Action secPas = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                playerPanel.tryClickOnSecretOass();
            }
        };
        inputMap.put(KeyStroke.getKeyStroke('e'), "secPas");
        inputMap.put(KeyStroke.getKeyStroke('E'), "secPas");
        actionMap.put("secPas", secPas);

        // add SPACE as short-cut key for rolling dice
        Action rollDice = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                playerPanel.tryClickOnRollDice();
            }
        };
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0), "rollDice");
        actionMap.put("rollDice", rollDice);

        // add ESC as short-cut key for ending turn
        Action endTurn = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                playerPanel.tryClickOnEndTurn();
            }
        };
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "endTurn");
        actionMap.put("endTurn", endTurn);
    }

    /**
     * A helper method to create a titled border
     * 
     * @param string
     *            --- the border tile
     * @return --- a titled border
     */
    private TitledBorder creatTitledBorder(String string) {
        return BorderFactory
                .createTitledBorder(
                        BorderFactory.createCompoundBorder(
                                BorderFactory.createSoftBevelBorder(BevelBorder.RAISED),
                                BorderFactory.createEtchedBorder(EtchedBorder.LOWERED)),
                        string, TitledBorder.CENTER, TitledBorder.TOP);
    }

    /**
     * Main method to start the game.
     * 
     * @param args
     *            --- who cares it in GUI?
     */
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

    /**
     * A helper method to load image
     * 
     * @param filename
     *            --- the file name
     * @return --- the Image object of the given file
     */
    public static Image loadImage(String filename) {
        URL imageURL = BoardCanvas.class.getResource(IMAGE_PATH + filename);
        try {
            Image img = ImageIO.read(imageURL);
            return img;
        } catch (IOException e) {
            throw new GameError("Unable to load image: " + filename);
        }
    }

    /**
     * This String specifies the path for loadImage() to look for images.
     */
    private static final String IMAGE_PATH = "resources/";
    /**
     * The image displayed on welcome screen
     */
    public static final Image INIT_SCREEN = loadImage("Initial_Screen.png");
    /**
     * An icon used to pop a dialog to tell players something is *Correct*
     */
    public static final ImageIcon CORRECT = new ImageIcon(loadImage("Icon_Correct.png"));
    /**
     * An icon used to pop a dialog to tell players something is *Incorrect*
     */
    public static final ImageIcon INCORRECT = new ImageIcon(
            loadImage("Icon_Incorrect.png"));
    /**
     * An icon used to pop a dialog to let players to do accusation
     */
    public static final ImageIcon ACCUSE_ICON = new ImageIcon(
            loadImage("Icon_Accusation.png"));
}
