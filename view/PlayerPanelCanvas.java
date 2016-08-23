package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import game.Player;
import tile.Entrance;
import tile.Room;
import tile.Tile;
import ui.GUIClient;
import card.Card;
import card.Character;
import card.Location;
import card.Weapon;

import static ui.GUIClient.loadImage;

/**
 * This is a custom panel for displaying the player panel on the right. The major
 * functionalities are: displaying cards in hand; displaying cards left undealt;
 * displaying dices; providing buttons to move, roll dice, make suggestion and make
 * accusation; and displaying a profile picture.
 * 
 * @author Hector
 *
 */
@SuppressWarnings("serial")
public class PlayerPanelCanvas extends JPanel {

    // ============ some numbers for swing to set size =============

    /**
     * Panel width
     */
    public static final int WIDTH = 700;
    /**
     * Panel height
     */
    private static final int HEIGHT = BoardCanvas.BOARD_IMG_HEIGHT;
    /**
     * the height of sub-panel for displaying cards in hand
     */
    private static final int SOUTH_PANEL_HEIGHT = 220;
    /**
     * the height of sub-panel for displaying cards left undealt
     */
    private static final int NORTH_PANEL_HEIGHT = SOUTH_PANEL_HEIGHT;
    /**
     * the height of sub-panel for displaying buttons, dices, and profile picture
     */
    private static final int CENTRE_PANEL_HEIGHT = HEIGHT - SOUTH_PANEL_HEIGHT
            - NORTH_PANEL_HEIGHT;
    /**
     * the width of the sub-panel for displaying profile picture
     */
    private static final int WEST_PANEL_WIDTH = 230;
    /**
     * the width of the button panel on mid-east (of the BorderLayout, not of the
     * world...)
     */
    private static final int EAST_PANEL_WIDTH = 320;
    /**
     * the padding size on left
     */
    public static final int PADDING_LEFT = BoardCanvas.PADDING_LEFT;
    /**
     * the padding size on right
     */
    public static final int PADDING_RIGHT = BoardCanvas.PADDING_RIGHT;
    /**
     * the padding size on top
     */
    private static final int PADDING_TOP = BoardCanvas.PADDING_TOP;

    // ============== swing components ======================

    /**
     * The sub-Panel for displaying remaining cards
     */
    private JPanel remainingCardsPanel;
    /**
     * The sub-panel for displaying cards in hand
     */
    private JPanel cardsInHandPanel;
    /**
     * The label for displaying profile picture
     */
    private JLabel profileLabel;
    /**
     * The labels for displaying dices
     */
    private JLabel[] diceLabels;
    /**
     * The Label for displaying remaining steps of current player
     */
    private JLabel remainingStepLabel;
    /**
     * The button for entering / exiting room
     */
    private JButton enterExitRoom;
    /**
     * The button for moving up
     */
    private JButton upButton;
    /**
     * The button for taking the secret passage
     */
    private JButton SecPasButton;
    /**
     * The button for moving left
     */
    private JButton leftButton;
    /**
     * The button for moving down
     */
    private JButton downButton;
    /**
     * The button for moving right
     */
    private JButton rightButton;
    /**
     * The button for rolling dice
     */
    private JButton rollDiceButton;
    /**
     * The button for ending turn
     */
    private JButton endTurnButton;
    /**
     * The button for making suggestion
     */
    private JButton suggestionButton;
    /**
     * The button for making accusation
     */
    private JButton accusationButton;

    // ======== Other fields that actually holds logic =========

    /**
     * Game's main GUI
     */
    private GUIClient gui;
    /**
     * The cards in current player's hand
     */
    private List<Card> cardsInHand;
    /**
     * The remaining cards that are not dealt
     */
    private List<Card> remainingCards;
    /**
     * Current player
     */
    private Character currentPlayer;
    /**
     * An array holding the number rolled by the player
     */
    private int[] diceRolled = null;
    /**
     * How many steps left to move
     */
    private int remainingSteps;

    /**
     * Construct a custom panel for display player related information
     * 
     * @param guiClient
     *            --- the Main GUI of this game
     */
    public PlayerPanelCanvas(GUIClient guiClient) {

        this.gui = guiClient;

        // ================== BorderLayout =====================
        this.setLayout(new BorderLayout(5, 5));

        // =================== North, remaining cards =====================
        remainingCardsPanel = new JPanel();
        remainingCardsPanel.setBackground(null);
        remainingCardsPanel.setOpaque(false);
        remainingCardsPanel.setPreferredSize(new Dimension(WIDTH, NORTH_PANEL_HEIGHT));
        remainingCardsPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        remainingCardsPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // a Label to show some artsy fonts
        JLabel remainingCardLabel = new JLabel(
                new ImageIcon(loadImage("Remaining_Cards.png")), SwingConstants.CENTER);
        remainingCardsPanel.add(remainingCardLabel);

        // display remaining cards.
        remainingCards = gui.getRemainingCards();
        if (remainingCards.isEmpty()) {
            remainingCardLabel.setToolTipText("There is no remaining card.");
        } else {
            remainingCardLabel.setToolTipText(
                    "There are " + remainingCards.size() + " remaining cards.");
        }

        for (Card c : remainingCards) {
            if (c instanceof Character) {
                Character ch = (Character) c;
                remainingCardsPanel.add(CHRACTER_LABELS[ch.ordinal()]);
            } else if (c instanceof Weapon) {
                Weapon we = (Weapon) c;
                remainingCardsPanel.add(WEAPON_LABELS[we.ordinal()]);
            } else {
                Location lo = (Location) c;
                remainingCardsPanel.add(LOCATION_LABELS[lo.ordinal()]);
            }
        }

        // ============== west, a player's profile picture ===============
        profileLabel = new JLabel();
        profileLabel.setOpaque(false);
        profileLabel
                .setPreferredSize(new Dimension(WEST_PANEL_WIDTH, CENTRE_PANEL_HEIGHT));
        profileLabel.setBorder(BorderFactory.createEmptyBorder(PADDING_LEFT, PADDING_TOP,
                PADDING_LEFT, PADDING_LEFT));

        // ============== centre, dice or dices ====================

        // panel for dices
        JPanel dicePanel = new JPanel();
        dicePanel.setBackground(null);
        dicePanel.setOpaque(false);
        dicePanel.setLayout(new BoxLayout(dicePanel, BoxLayout.X_AXIS));

        // another panel to make the dice centre-aligned
        JPanel diceGroup = new JPanel();
        diceGroup.setBackground(null);
        diceGroup.setOpaque(false);
        diceGroup.setLayout(new BoxLayout(diceGroup, BoxLayout.Y_AXIS));

        diceLabels = new JLabel[gui.getNumDices()];
        for (int i = 0; i < diceLabels.length; i++) {
            diceLabels[i] = new JLabel();
            diceLabels[i].setBorder(null);
            diceGroup.add(diceLabels[i], Component.CENTER_ALIGNMENT);

            // add gaps between dices. and do not add a gap after the last dice
            if (i != diceLabels.length - 1) {
                int gap = 0;
                // vary the gap according to number of dices
                if (diceLabels.length == 2) {
                    gap = 25;
                } else if (diceLabels.length == 3) {
                    gap = 5;
                }
                diceGroup.add(Box.createRigidArea(new Dimension(gap, gap)),
                        Component.CENTER_ALIGNMENT);
            }
        }

        diceGroup.setAlignmentY(Component.CENTER_ALIGNMENT);
        diceGroup.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 15));
        dicePanel.add(diceGroup);
        dicePanel.setBorder(BorderFactory.createEmptyBorder(PADDING_LEFT, PADDING_LEFT,
                PADDING_LEFT, PADDING_LEFT));

        // ============ east, buttons ===================
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(null);
        buttonPanel.setOpaque(false);
        buttonPanel
                .setPreferredSize(new Dimension(EAST_PANEL_WIDTH, CENTRE_PANEL_HEIGHT));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(PADDING_LEFT, PADDING_LEFT,
                PADDING_LEFT, PADDING_LEFT));
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));

        // first row, a text
        remainingStepLabel = new JLabel();
        remainingStepLabel.setBackground(null);
        remainingStepLabel.setOpaque(false);
        remainingStepLabel.setFont(new Font("Calibre", 1, 20));
        remainingStepLabel.setForeground(Color.DARK_GRAY);
        remainingStepLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        remainingStepLabel.setBorder(BorderFactory.createEmptyBorder(PADDING_LEFT,
                PADDING_LEFT, PADDING_LEFT, PADDING_LEFT));

        // second row, a grid layout to show movement buttons.
        JPanel movePanel = new JPanel();
        movePanel.setBackground(null);
        movePanel.setOpaque(false);
        movePanel.setLayout(new GridLayout(2, 3, 10, 10));
        movePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        movePanel.setBorder(BorderFactory.createEmptyBorder(PADDING_LEFT, PADDING_LEFT,
                PADDING_LEFT, PADDING_LEFT));

        // six buttons for different directions, enter/exit room, and secret passage
        enterExitRoom = createButton(ENTER_DEFAULT_IMG, ENTER_PRESSED_IMG,
                MOVE_DISABLED_IMG, MOVE_BUTTON_SIZE);
        enterExitRoom.setEnabled(false);
        upButton = createButton(UP_DEFAULT_IMG, UP_PRESSED_IMG, MOVE_DISABLED_IMG,
                MOVE_BUTTON_SIZE);
        SecPasButton = createButton(SECPAS_DEFAULT_IMG, SECPAS_PRESSED_IMG,
                MOVE_DISABLED_IMG, MOVE_BUTTON_SIZE);
        SecPasButton.setEnabled(false);
        leftButton = createButton(LEFT_DEFAULT_IMG, LEFT_PRESSED_IMG, MOVE_DISABLED_IMG,
                MOVE_BUTTON_SIZE);
        downButton = createButton(DOWN_DEFAULT_IMG, DOWN_PRESSED_IMG, MOVE_DISABLED_IMG,
                MOVE_BUTTON_SIZE);
        rightButton = createButton(RIGHT_DEFAULT_IMG, RIGHT_PRESSED_IMG,
                MOVE_DISABLED_IMG, MOVE_BUTTON_SIZE);

        // add listener on them
        enterExitRoom.addActionListener(e -> clickOnEnterExitRoom());
        upButton.addActionListener(e -> clickOnUp());
        SecPasButton.addActionListener(e -> clickOnSecretPass());
        leftButton.addActionListener(e -> clickOnLeft());
        downButton.addActionListener(e -> clickOnDown());
        rightButton.addActionListener(e -> clickOnRight());

        // add button into the panel
        movePanel.add(enterExitRoom);
        movePanel.add(upButton);
        movePanel.add(SecPasButton);
        movePanel.add(leftButton);
        movePanel.add(downButton);
        movePanel.add(rightButton);

        // third row, another gridLayout to show action buttons.
        JPanel actionPanel = new JPanel();
        actionPanel.setBackground(null);
        actionPanel.setOpaque(false);
        actionPanel.setLayout(new GridLayout(2, 2, 10, 10));
        actionPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        actionPanel.setBorder(BorderFactory.createEmptyBorder(PADDING_LEFT, PADDING_LEFT,
                PADDING_LEFT, PADDING_LEFT));

        // four action buttons for roll dice, end turn, suggestion, and accusation
        rollDiceButton = createButton(ROLLDICE_DEFAULT_IMG, ROLLDICE_PRESSED_IMG,
                ACTION_DISABLED_IMG, ACTION_BUTTON_SIZE);
        endTurnButton = createButton(ENDTURN_DEFAULT_IMG, ENDTURN_PRESSED_IMG,
                ACTION_DISABLED_IMG, ACTION_BUTTON_SIZE);
        suggestionButton = createButton(SUGGESTION_DEFAULT_IMG, SUGGESTION_PRESSED_IMG,
                ACTION_DISABLED_IMG, ACTION_BUTTON_SIZE);
        accusationButton = createButton(ACCUSATION_DEFAULT_IMG, ACCUSATION_PRESSED_IMG,
                ACTION_DISABLED_IMG, ACTION_BUTTON_SIZE);

        // add listeners
        rollDiceButton.addActionListener(e -> clickOnRollDice());
        endTurnButton.addActionListener(e -> clickOnEndTurn());
        suggestionButton.addActionListener(e -> clickOnSuggestion());
        accusationButton.addActionListener(e -> clickOnAccusation());

        // add button into the panel
        actionPanel.add(rollDiceButton);
        actionPanel.add(endTurnButton);
        actionPanel.add(suggestionButton);
        actionPanel.add(accusationButton);

        // put them together
        buttonPanel.add(remainingStepLabel);
        buttonPanel.add(Box.createRigidArea(new Dimension(5, 5)));
        buttonPanel.add(movePanel);
        buttonPanel.add(Box.createRigidArea(new Dimension(5, 5)));
        buttonPanel.add(actionPanel);

        // ================= south, cards in hand =================
        cardsInHandPanel = new JPanel();
        cardsInHandPanel.setBackground(null);
        cardsInHandPanel.setOpaque(false);
        cardsInHandPanel.setPreferredSize(new Dimension(WIDTH, SOUTH_PANEL_HEIGHT));
        cardsInHandPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        cardsInHandPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        currentPlayer = gui.getCurrentPlayer();
        cardsInHand = gui.getPlayerByCharacter(currentPlayer).getCards();

        // add cards one by one in a row
        for (Card c : cardsInHand) {
            if (c instanceof Character) {
                Character ch = (Character) c;
                cardsInHandPanel.add(CHRACTER_LABELS[ch.ordinal()]);
            } else if (c instanceof Weapon) {
                Weapon we = (Weapon) c;
                cardsInHandPanel.add(WEAPON_LABELS[we.ordinal()]);
            } else {
                Location lo = (Location) c;
                cardsInHandPanel.add(LOCATION_LABELS[lo.ordinal()]);
            }
        }

        // ========== Adding five components together ==============
        this.add(remainingCardsPanel, BorderLayout.NORTH);
        this.add(profileLabel, BorderLayout.WEST);
        this.add(dicePanel, BorderLayout.CENTER);
        this.add(buttonPanel, BorderLayout.EAST);
        this.add(cardsInHandPanel, BorderLayout.SOUTH);
        this.setVisible(true);

        // update the panel
        update();
    }

    /**
     * This method ask gui for game's status, and update the display of player panel.
     */
    public void update() {
        // ============== west, a player's character picture ===============
        currentPlayer = gui.getCurrentPlayer();
        profileLabel.setIcon(PROFILE_IMG[currentPlayer.ordinal()]);

        // ============== centre, dice or dices ====================
        if (remainingSteps == 0) {
            for (int i = 0; i < diceLabels.length; i++) {
                diceLabels[i].setIcon(null);
            }
        }

        // ============ east, buttons panel ===================
        validateButtons();

        // ================= south, cards in hand =================
        // remove old components
        for (Component com : cardsInHandPanel.getComponents()) {
            cardsInHandPanel.remove(com);
        }
        // add new player's components
        cardsInHand = gui.getPlayerByCharacter(currentPlayer).getCards();
        for (Card c : cardsInHand) {
            if (c instanceof Character) {
                Character ch = (Character) c;
                cardsInHandPanel.add(CHRACTER_LABELS[ch.ordinal()]);
            } else if (c instanceof Weapon) {
                Weapon we = (Weapon) c;
                cardsInHandPanel.add(WEAPON_LABELS[we.ordinal()]);
            } else {
                Location lo = (Location) c;
                cardsInHandPanel.add(LOCATION_LABELS[lo.ordinal()]);
            }
        }

        cardsInHandPanel.setVisible(true);
        cardsInHandPanel.updateUI();
        cardsInHandPanel.repaint();

        // ================ Adding stuff ===================
        this.setVisible(true);
        this.updateUI();
        this.repaint();
        this.repaint();
    }

    /**
     * This method validates all buttons on the button panel. If any button should not be
     * pressed, disable it, forever prevent it from being pressed by player.
     */
    private void validateButtons() {
        // the text label for displaying remaining steps
        remainingStepLabel
                .setText("Remaining Steps: " + gui.getRemainingSteps(currentPlayer));

        /*
         * first if the player hasn't rolled dices, disable all buttons except roll button
         * and return
         */
        if (remainingSteps == 0) {
            enterExitRoom.setEnabled(false);
            upButton.setEnabled(false);
            SecPasButton.setEnabled(false);
            leftButton.setEnabled(false);
            downButton.setEnabled(false);
            rightButton.setEnabled(false);
            suggestionButton.setEnabled(false);
            accusationButton.setEnabled(false);
            endTurnButton.setEnabled(false);
            rollDiceButton.setEnabled(true);
            return;
        }

        // let's disable most actions
        enterExitRoom.setEnabled(false);
        upButton.setEnabled(false);
        SecPasButton.setEnabled(false);
        leftButton.setEnabled(false);
        downButton.setEnabled(false);
        rightButton.setEnabled(false);
        suggestionButton.setEnabled(false);
        accusationButton.setEnabled(true);
        endTurnButton.setEnabled(true);

        // now let's see what options are there for current player.
        Player player = gui.getPlayerByCharacter(currentPlayer);

        // if there are tiles in four directions
        if (gui.getBoard().lookNorth(player) != null) {
            // check if any other player standing there, then it's not an option
            boolean isBlocking = false;
            Tile tile = gui.getBoard().lookNorth(player);
            for (Player existingPlayer : gui.getAllPlayers()) {
                if (tile.equals(existingPlayer.getPosition())) {
                    isBlocking = true;
                    break;
                }
            }
            upButton.setEnabled(!isBlocking);
        }

        if (gui.getBoard().lookEast(player) != null) {
            boolean isBlocking = false;
            Tile tile = gui.getBoard().lookEast(player);
            for (Player existingPlayer : gui.getAllPlayers()) {
                if (tile.equals(existingPlayer.getPosition())) {
                    isBlocking = true;
                    break;
                }
            }
            rightButton.setEnabled(!isBlocking);
        }

        if (gui.getBoard().lookSouth(player) != null) {
            boolean isBlocking = false;
            Tile tile = gui.getBoard().lookSouth(player);
            for (Player existingPlayer : gui.getAllPlayers()) {
                if (tile.equals(existingPlayer.getPosition())) {
                    isBlocking = true;
                    break;
                }
            }
            downButton.setEnabled(!isBlocking);
        }

        if (gui.getBoard().lookWest(player) != null) {
            boolean isBlocking = false;
            Tile tile = gui.getBoard().lookWest(player);
            for (Player existingPlayer : gui.getAllPlayers()) {
                if (tile.equals(existingPlayer.getPosition())) {
                    isBlocking = true;
                    break;
                }
            }
            leftButton.setEnabled(!isBlocking);
        }

        // if the player is standing at an entrance to a room
        if (gui.getBoard().atEntranceTo(player) != null) {
            // since we combine two buttons together, we have to change the image
            enterExitRoom.setName("ENTER");
            enterExitRoom.setIcon(ENTER_DEFAULT_IMG);
            enterExitRoom.setRolloverIcon(ENTER_DEFAULT_IMG);
            enterExitRoom.setPressedIcon(ENTER_PRESSED_IMG);
            enterExitRoom.setEnabled(true);
        }

        // if the player is in a room, get the exits
        List<Entrance> entrances = gui.getBoard().lookForExit(player);
        if (entrances != null && !entrances.isEmpty()) {
            enterExitRoom.setName("EXIT");
            enterExitRoom.setIcon(EXIT_DEFAULT_IMG);
            enterExitRoom.setRolloverIcon(EXIT_DEFAULT_IMG);
            enterExitRoom.setPressedIcon(EXIT_PRESSED_IMG);

            Location room = entrances.get(0).toRoom().getRoom();

            if (room == Location.Kitchen || room == Location.Conservatory
                    || room == Location.Study || room == Location.Lounge) {
                // these rooms has only one exit, if it's blocked, disable the exit button
                boolean isBlocking = false;
                for (Player existingPlayer : gui.getAllPlayers()) {
                    if (entrances.get(0).equals(existingPlayer.getPosition())) {
                        isBlocking = true;
                        break;
                    }
                }
                enterExitRoom.setEnabled(!isBlocking);

            } else {
                // other rooms have more than one exit. Check if they are all blocked.
                boolean allBlocked = true;
                for (Entrance exit : entrances) {
                    boolean isBlocked = false;
                    for (Player existingPlayer : gui.getAllPlayers()) {
                        if (exit.equals(existingPlayer.getPosition())) {
                            isBlocked = true;
                            break;
                        }
                        isBlocked = false;
                    }

                    if (!isBlocked) {
                        allBlocked = false;
                        break;
                    }
                }

                if (allBlocked) {
                    enterExitRoom.setEnabled(false);
                } else {
                    enterExitRoom.setEnabled(true);
                }
            }
        }

        // if the player is in a room, and there is a secret passage
        if (gui.getBoard().lookForSecPas(player) != null) {
            // in a room, have a secret passage
            SecPasButton.setEnabled(true);
        }

        // validate the roll dice button
        if (gui.getRemainingSteps(currentPlayer) == 0) {
            rollDiceButton.setEnabled(true);
        } else {
            rollDiceButton.setEnabled(false);
        }

        // enables the suggestion button only when the player is in a room
        if (gui.getPlayerPosition(currentPlayer) instanceof Room) {
            suggestionButton.setEnabled(true);
        } else {
            suggestionButton.setEnabled(false);
        }
    }

    /**
     * This method does all the work when the player clicked on up button. It move player
     * towards north by one tile, decrement remaining steps, end current player's turn if
     * necessary, and update the GUI.
     */
    public void clickOnUp() {
        Tile northTile = gui.getBoard()
                .lookNorth(gui.getPlayerByCharacter(currentPlayer));

        if (northTile != null) { // this shouldn't be necessary
            gui.movePlayer(currentPlayer, northTile);
        }

        // decrementing remaining steps
        remainingSteps--;
        gui.setRemainingSteps(currentPlayer, remainingSteps);
        if (remainingSteps == 0) {
            gui.currentPlayerEndTurn();
        }

        // update the GUI
        gui.update();
    }

    /**
     * This method does all the work when the player clicked on left button. It move
     * player towards west by one tile, decrement remaining steps, end current player's
     * turn if necessary, and update the GUI.
     */
    public void clickOnLeft() {
        Tile westTile = gui.getBoard().lookWest(gui.getPlayerByCharacter(currentPlayer));
        if (westTile != null) {
            gui.movePlayer(currentPlayer, westTile);
        }
        remainingSteps--;
        gui.setRemainingSteps(currentPlayer, remainingSteps);
        if (remainingSteps == 0) {
            gui.currentPlayerEndTurn();
        }
        gui.update();
    }

    /**
     * This method does all the work when the player clicked on down button. It move
     * player towards south by one tile, decrement remaining steps, end current player's
     * turn if necessary, and update the GUI.
     */
    public void clickOnDown() {
        Tile southTile = gui.getBoard()
                .lookSouth(gui.getPlayerByCharacter(currentPlayer));
        if (southTile != null) {
            gui.movePlayer(currentPlayer, southTile);
        }
        remainingSteps--;
        gui.setRemainingSteps(currentPlayer, remainingSteps);
        if (remainingSteps == 0) {
            gui.currentPlayerEndTurn();
        }
        gui.update();
    }

    /**
     * This method does all the work when the player clicked on right button. It move
     * player towards east by one tile, decrement remaining steps, end current player's
     * turn if necessary, and update the GUI.
     */
    public void clickOnRight() {
        Tile eastTile = gui.getBoard().lookEast(gui.getPlayerByCharacter(currentPlayer));
        if (eastTile != null) {
            gui.movePlayer(currentPlayer, eastTile);
        }
        remainingSteps--;
        gui.setRemainingSteps(currentPlayer, remainingSteps);
        if (remainingSteps == 0) {
            gui.currentPlayerEndTurn();
        }
        gui.update();
    }

    /**
     * This method does all the work when the player clicked on enter/exit button. It move
     * the player in or out of room, decrement remaining steps, end current player's turn
     * if necessary, pop up a suggestion dialog if necessary, and update the GUI.
     */
    public void clickOnEnterExitRoom() {

        if (enterExitRoom.getName().equals("ENTER")) {
            Room room = gui.getBoard()
                    .atEntranceTo(gui.getPlayerByCharacter(currentPlayer));
            if (room != null) {
                gui.movePlayer(currentPlayer, room);
            }
            remainingSteps = 0;
            gui.setRemainingSteps(currentPlayer, remainingSteps);
            gui.update();
            gui.popUpSuggestion();
            gui.currentPlayerEndTurn();
        } else {
            List<Entrance> entrances = gui.getBoard()
                    .lookForExit(gui.getPlayerByCharacter(currentPlayer));
            Location room = entrances.get(0).toRoom().getRoom();

            if (room == Location.Kitchen || room == Location.Conservatory
                    || room == Location.Study || room == Location.Lounge) {
                // these rooms has only one exit, just step out.
                if (entrances.get(0) != null) {
                    gui.movePlayer(currentPlayer, entrances.get(0));
                }
            } else {
                // pop up a dialog to choose which room to exit
                new ExitRoomDialog(gui, SwingUtilities.windowForComponent(this),
                        "Exit Room", room);
            }

            remainingSteps--;
            gui.setRemainingSteps(currentPlayer, remainingSteps);
            if (remainingSteps == 0) {
                gui.currentPlayerEndTurn();
            }
        }
        gui.update();
    }

    /**
     * This method does all the work when the player clicked on secret passage button. It
     * move the player to the room at the end of this room's secret passage, decrement
     * remaining steps, end current player's turn if necessary, pop up a suggestion dialog
     * if necessary, and update the GUI.
     */
    public void clickOnSecretPass() {
        Room secPasTo = gui.getBoard()
                .lookForSecPas(gui.getPlayerByCharacter(currentPlayer));
        if (secPasTo != null) {
            gui.movePlayer(currentPlayer, secPasTo);
        }

        remainingSteps = 0;
        gui.setRemainingSteps(currentPlayer, remainingSteps);
        gui.update();
        gui.popUpSuggestion();
        gui.currentPlayerEndTurn();
        gui.update();
    }

    /**
     * This method does all the work when the player clicked on roll dice button. It roll
     * the dices, get a total number, and update the GUI.
     */
    public void clickOnRollDice() {
        diceRolled = gui.rollDice(currentPlayer);
        for (int i = 0; i < diceLabels.length; i++) {
            if (diceRolled != null) {
                diceLabels[i].setIcon(DICE_IMG[diceRolled[i]]);
            }
        }
        remainingSteps = 0;
        for (int i : diceRolled) {
            remainingSteps += (i + 1);
        }
        gui.setRemainingSteps(currentPlayer, remainingSteps);
        rollDiceButton.setEnabled(false);
        update();
    }

    /**
     * This method does all the work when the player clicked on end turn button. It ends
     * current player's turn, and update the GUI.
     */
    public void clickOnEndTurn() {
        remainingSteps = 0;
        gui.setRemainingSteps(currentPlayer, 0);
        gui.currentPlayerEndTurn();
        gui.update();
    }

    /**
     * This method does all the work when the player clicked on suggestion button. It pops
     * up a dialog for making suggestion, and after the player has made suggestion, update
     * the GUI.
     */
    public void clickOnSuggestion() {
        remainingSteps = 0;
        gui.setRemainingSteps(currentPlayer, remainingSteps);
        gui.popUpSuggestion();
        gui.currentPlayerEndTurn();
        gui.update();
    }

    /**
     * This method does all the work when the player clicked on accusation button. It pops
     * up a dialog for making accusation, and after the player has made accusation, update
     * the GUI.
     */
    public void clickOnAccusation() {
        remainingSteps = 0;
        gui.setRemainingSteps(currentPlayer, remainingSteps);
        gui.popUpAccusation();
        gui.currentPlayerEndTurn();
        gui.update();
    }

    /**
     * This method exists to respond a short-cut key input for moving up. It simply check
     * whether up button is enabled. If yes, then click on it, if no, do nothing.
     */
    public void tryClickOnUp() {
        if (upButton.isEnabled()) {
            clickOnUp();
        }
    }

    /**
     * This method exists to respond a short-cut key input for moving left. It simply
     * check whether left button is enabled. If yes, then click on it, if no, do nothing.
     */
    public void tryClickOnLeft() {
        if (leftButton.isEnabled()) {
            clickOnLeft();
        }
    }

    /**
     * This method exists to respond a short-cut key input for moving down. It simply
     * check whether down button is enabled. If yes, then click on it, if no, do nothing.
     */
    public void tryClickOnDown() {
        if (downButton.isEnabled()) {
            clickOnDown();
        }
    }

    /**
     * This method exists to respond a short-cut key input for moving right. It simply
     * check whether right button is enabled. If yes, then click on it, if no, do nothing.
     */
    public void tryClickOnRight() {
        if (rightButton.isEnabled()) {
            clickOnRight();
        }
    }

    /**
     * This method exists to respond a short-cut key input for enter/exit room button. It
     * simply check whether enter/exit room button is enabled. If yes, then click on it,
     * if no, do nothing.
     */
    public void tryClickOnEnterExitRoom() {
        if (enterExitRoom.isEnabled()) {
            clickOnEnterExitRoom();
        }
    }

    /**
     * This method exists to respond a short-cut key input for secret passage button. It
     * simply check whether secret passage button is enabled. If yes, then click on it, if
     * no, do nothing.
     */
    public void tryClickOnSecretOass() {
        if (SecPasButton.isEnabled()) {
            clickOnSecretPass();
        }
    }

    /**
     * This method exists to respond a short-cut key input for roll dice button. It simply
     * check whether roll dice button is enabled. If yes, then click on it, if no, do
     * nothing.
     */
    public void tryClickOnRollDice() {
        if (rollDiceButton.isEnabled()) {
            clickOnRollDice();
        }
    }

    /**
     * This method exists to respond a short-cut key input for end turn button. It simply
     * check whether end turn button is enabled. If yes, then click on it, if no, do
     * nothing.
     */
    public void tryClickOnEndTurn() {
        if (endTurnButton.isEnabled()) {
            clickOnEndTurn();
        }
    }

    /**
     * A helper method to create a JButton object, and set its attributes to fit the GUI.
     * 
     * @param defaultIcon
     *            --- the image used as the default image
     * @param pressedIcon
     *            --- the image used as the "pressed" image
     * @param disabledIcon
     *            --- the image used as the disabled image
     * @param dimension
     *            --- the preferred size of this JButton
     * @return --- a JButton instance
     */
    private JButton createButton(ImageIcon defaultIcon, ImageIcon pressedIcon,
            ImageIcon disabledIcon, Dimension dimension) {
        JButton button = new JButton();
        button.setBackground(null);
        button.setPreferredSize(dimension);
        // make the button transparent
        button.setMargin(new Insets(0, 0, 0, 0));
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        // set Images
        button.setIcon(defaultIcon);
        button.setRolloverIcon(defaultIcon);
        button.setPressedIcon(pressedIcon);
        button.setDisabledIcon(disabledIcon);
        return button;
    }

    /**
     * A helper method to create a JLabel object for displaying the cards.
     * 
     * @param cardImg
     *            --- the image used to display the card
     * @param example
     *            --- a Character or a Weapon or a Location card, as example. This
     *            argument is used to decide the type of this card, and look for
     *            appropriate image accordingly
     * @return --- a JLabel object to represents the card
     */
    private static JLabel[] createCardLabel(ImageIcon[] cardImg, Card example) {
        JLabel[] cards = new JLabel[cardImg.length];
        for (int i = 0; i < cardImg.length; i++) {
            Card c;
            if (example instanceof Character) {
                c = Character.get(i);
            } else if (example instanceof Weapon) {
                c = Weapon.get(i);
            } else {
                c = Location.get(i);
            }
            cards[i] = new JLabel(cardImg[i]);
            cards[i].setBorder(null);
            cards[i].setToolTipText(c.toString());
        }
        return cards;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(PLAYER_PANEL, PADDING_LEFT, PADDING_TOP, WIDTH, HEIGHT, this);
    }

    // ============== Static Images ========================

    /**
     * The background image of player panel
     */
    private static final Image PLAYER_PANEL = GUIClient
            .loadImage("Player_Panel_Background.png");
    /**
     * Six profile images
     */
    public static final ImageIcon[] PROFILE_IMG = {
            new ImageIcon(loadImage("Profile_Miss_Scarlet.png")),
            new ImageIcon(loadImage("Profile_Colonel_Mustard.png")),
            new ImageIcon(loadImage("Profile_Mrs_White.png")),
            new ImageIcon(loadImage("Profile_The_Reverend_Green.png")),
            new ImageIcon(loadImage("Profile_Mrs_Peacock.png")),
            new ImageIcon(loadImage("Profile_Professor_Plum.png")) };
    /**
     * Images for displaying dices
     */
    private static final ImageIcon[] DICE_IMG = { new ImageIcon(loadImage("Dice_1.png")),
            new ImageIcon(loadImage("Dice_2.png")),
            new ImageIcon(loadImage("Dice_3.png")),
            new ImageIcon(loadImage("Dice_4.png")),
            new ImageIcon(loadImage("Dice_5.png")),
            new ImageIcon(loadImage("Dice_6.png")) };
    /**
     * Images for displaying Character cards
     */
    public static final ImageIcon[] CHARACTER_IMG = {
            new ImageIcon(loadImage("Character_Miss_Scarlet.png")),
            new ImageIcon(loadImage("Character_Colonel_Mustard.png")),
            new ImageIcon(loadImage("Character_Mrs_White.png")),
            new ImageIcon(loadImage("Character_The_Reverend_Green.png")),
            new ImageIcon(loadImage("Character_Mrs_Peacock.png")),
            new ImageIcon(loadImage("Character_Professor_Plum.png")) };
    /**
     * Images for displaying Weapon cards
     */
    public static final ImageIcon[] WEAPON_IMG = {
            new ImageIcon(loadImage("Weapon_Candlestick.png")),
            new ImageIcon(loadImage("Weapon_Dagger.png")),
            new ImageIcon(loadImage("Weapon_Lead_Pipe.png")),
            new ImageIcon(loadImage("Weapon_Revolver.png")),
            new ImageIcon(loadImage("Weapon_Rope.png")),
            new ImageIcon(loadImage("Weapon_Spanner.png")) };
    /**
     * Images for displaying Location cards
     */
    public static final ImageIcon[] LOCATION_IMG = {
            new ImageIcon(loadImage("Location_Kitchen.png")),
            new ImageIcon(loadImage("Location_Ball_room.png")),
            new ImageIcon(loadImage("Location_Conservatory.png")),
            new ImageIcon(loadImage("Location_Billard_Room.png")),
            new ImageIcon(loadImage("Location_Library.png")),
            new ImageIcon(loadImage("Location_Study.png")),
            new ImageIcon(loadImage("Location_Hall.png")),
            new ImageIcon(loadImage("Location_Lounge.png")),
            new ImageIcon(loadImage("Location_Dining_Room.png")) };
    /**
     * JLabel objects for displaying Character cards
     */
    private static final JLabel[] CHRACTER_LABELS = createCardLabel(CHARACTER_IMG,
            Character.get(0));
    /**
     * JLabel objects for displaying Weapon cards
     */
    private static final JLabel[] WEAPON_LABELS = createCardLabel(WEAPON_IMG,
            Weapon.get(0));
    /**
     * JLabel objects for displaying Location cards
     */
    private static final JLabel[] LOCATION_LABELS = createCardLabel(LOCATION_IMG,
            Location.get(0));
    /**
     * The preferred size of move buttons
     */
    private static final Dimension MOVE_BUTTON_SIZE = new Dimension(85, 55);
    /**
     * The preferred size of action buttons
     */
    private static final Dimension ACTION_BUTTON_SIZE = new Dimension(135, 55);
    /**
     * An image for displaying disabled action button
     */
    private static final ImageIcon ACTION_DISABLED_IMG = new ImageIcon(
            loadImage("Button_Action_Disabled.png"));
    /**
     * An image for displaying disabled move button
     */
    private static final ImageIcon MOVE_DISABLED_IMG = new ImageIcon(
            loadImage("Button_Movement_Disabled.png"));
    /**
     * An image for displaying default up button
     */
    private static final ImageIcon UP_DEFAULT_IMG = new ImageIcon(
            loadImage("Button_Up_Default.png"));
    /**
     * An image for displaying pressed up button
     */
    private static final ImageIcon UP_PRESSED_IMG = new ImageIcon(
            loadImage("Button_Up_Pressed.png"));
    /**
     * An image for displaying default down button
     */
    private static final ImageIcon DOWN_DEFAULT_IMG = new ImageIcon(
            loadImage("Button_Down_Default.png"));
    /**
     * An image for displaying pressed down button
     */
    private static final ImageIcon DOWN_PRESSED_IMG = new ImageIcon(
            loadImage("Button_Down_Pressed.png"));
    /**
     * An image for displaying default left button
     */
    private static final ImageIcon LEFT_DEFAULT_IMG = new ImageIcon(
            loadImage("Button_Left_Default.png"));
    /**
     * An image for displaying pressed left button
     */
    private static final ImageIcon LEFT_PRESSED_IMG = new ImageIcon(
            loadImage("Button_Left_Pressed.png"));
    /**
     * An image for displaying default right button
     */
    private static final ImageIcon RIGHT_DEFAULT_IMG = new ImageIcon(
            loadImage("Button_Right_Default.png"));
    /**
     * An image for displaying pressed right button
     */
    private static final ImageIcon RIGHT_PRESSED_IMG = new ImageIcon(
            loadImage("Button_Right_Pressed.png"));
    /**
     * An image for displaying default enter room button
     */
    private static final ImageIcon ENTER_DEFAULT_IMG = new ImageIcon(
            loadImage("Button_EnterRoom_Default.png"));
    /**
     * An image for displaying pressed enter room button
     */
    private static final ImageIcon ENTER_PRESSED_IMG = new ImageIcon(
            loadImage("Button_EnterRoom_Pressed.png"));
    /**
     * An image for displaying default exit room button
     */
    private static final ImageIcon EXIT_DEFAULT_IMG = new ImageIcon(
            loadImage("Button_ExitRoom_Default.png"));
    /**
     * An image for displaying pressed exit room button
     */
    private static final ImageIcon EXIT_PRESSED_IMG = new ImageIcon(
            loadImage("Button_ExitRoom_Pressed.png"));
    /**
     * An image for displaying default secret passage button
     */
    private static final ImageIcon SECPAS_DEFAULT_IMG = new ImageIcon(
            loadImage("Button_SecretPass_Default.png"));
    /**
     * An image for displaying pressed secret passage button
     */
    private static final ImageIcon SECPAS_PRESSED_IMG = new ImageIcon(
            loadImage("Button_SecretPass_Pressed.png"));
    /**
     * An image for displaying default roll dice button
     */
    private static final ImageIcon ROLLDICE_DEFAULT_IMG = new ImageIcon(
            loadImage("Button_RollDice_Default.png"));
    /**
     * An image for displaying pressed roll dice button
     */
    private static final ImageIcon ROLLDICE_PRESSED_IMG = new ImageIcon(
            loadImage("Button_RollDice_Pressed.png"));
    /**
     * An image for displaying default end turn button
     */
    private static final ImageIcon ENDTURN_DEFAULT_IMG = new ImageIcon(
            loadImage("Button_EndTuen_Default.png"));
    /**
     * An image for displaying pressed end turn button
     */
    private static final ImageIcon ENDTURN_PRESSED_IMG = new ImageIcon(
            loadImage("Button_EndTuen_Pressed.png"));
    /**
     * An image for displaying default suggestion button
     */
    private static final ImageIcon SUGGESTION_DEFAULT_IMG = new ImageIcon(
            loadImage("Button_Suggestion_Default.png"));
    /**
     * An image for displaying pressed suggestion button
     */
    private static final ImageIcon SUGGESTION_PRESSED_IMG = new ImageIcon(
            loadImage("Button_Suggestion_Pressed.png"));
    /**
     * An image for displaying default accusation button
     */
    private static final ImageIcon ACCUSATION_DEFAULT_IMG = new ImageIcon(
            loadImage("Button_Accusation_Default.png"));
    /**
     * An image for displaying pressed accusation button
     */
    private static final ImageIcon ACCUSATION_PRESSED_IMG = new ImageIcon(
            loadImage("Button_Accusation_Pressed.png"));

}