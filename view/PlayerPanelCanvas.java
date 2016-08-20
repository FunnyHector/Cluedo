package view;

import java.awt.BorderLayout;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GraphicsDevice;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import configs.Configs;
import game.Player;
import tile.Entrance;
import tile.Position;
import tile.Room;
import tile.Tile;
import ui.GUIClient;
import card.Card;
import card.Character;
import card.Location;
import card.Weapon;

import static ui.GUIClient.loadImage;

public class PlayerPanelCanvas extends JPanel {

    public static final Image PLAYER_PANEL = GUIClient
            .loadImage("Player_Panel_Background.png");

    public static final ImageIcon[] PROFILE_IMG = {
            new ImageIcon(loadImage("Profile_Miss_Scarlet.png")),
            new ImageIcon(loadImage("Profile_Colonel_Mustard.png")),
            new ImageIcon(loadImage("Profile_Mrs_White.png")),
            new ImageIcon(loadImage("Profile_The_Reverend_Green.png")),
            new ImageIcon(loadImage("Profile_Mrs_Peacock.png")),
            new ImageIcon(loadImage("Profile_Professor_Plum.png")) };

    public static final ImageIcon[] DICE_IMG = { new ImageIcon(loadImage("Dice_1.png")),
            new ImageIcon(loadImage("Dice_2.png")),
            new ImageIcon(loadImage("Dice_3.png")),
            new ImageIcon(loadImage("Dice_4.png")),
            new ImageIcon(loadImage("Dice_5.png")),
            new ImageIcon(loadImage("Dice_6.png")) };

    public static final ImageIcon[] CHARACTER_IMG = {
            new ImageIcon(loadImage("Character_Miss_Scarlet.png")),
            new ImageIcon(loadImage("Character_Colonel_Mustard.png")),
            new ImageIcon(loadImage("Character_Mrs_White.png")),
            new ImageIcon(loadImage("Character_The_Reverend_Green.png")),
            new ImageIcon(loadImage("Character_Mrs_Peacock.png")),
            new ImageIcon(loadImage("Character_Professor_Plum.png")) };

    public static final CardLabel[] CHRACTER_LABELS = createCardLabel(CHARACTER_IMG,
            Character.get(0));

    public static final ImageIcon[] WEAPON_IMG = {
            new ImageIcon(loadImage("Weapon_Candlestick.png")),
            new ImageIcon(loadImage("Weapon_Dagger.png")),
            new ImageIcon(loadImage("Weapon_Lead_Pipe.png")),
            new ImageIcon(loadImage("Weapon_Revolver.png")),
            new ImageIcon(loadImage("Weapon_Rope.png")),
            new ImageIcon(loadImage("Weapon_Spanner.png")) };

    public static final CardLabel[] WEAPON_LABELS = createCardLabel(WEAPON_IMG,
            Weapon.get(0));

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

    public static final CardLabel[] LOCATION_LABELS = createCardLabel(LOCATION_IMG,
            Location.get(0));

    public static final int WIDTH = 700;
    public static final int HEIGHT = BoardCanvas.BOARD_IMG_HEIGHT;

    public static final int SOUTH_PANEL_HEIGHT = 220;
    public static final int NORTH_PANEL_HEIGHT = SOUTH_PANEL_HEIGHT;
    public static final int CENTRE_PANEL_HEIGHT = HEIGHT - SOUTH_PANEL_HEIGHT
            - NORTH_PANEL_HEIGHT;

    public static final int WEST_PANEL_WIDTH = 230;
    public static final int EAST_PANEL_WIDTH = 320;
    public static final int CENTRE_PANEL_WIDTH = WIDTH - WEST_PANEL_WIDTH
            - EAST_PANEL_WIDTH;

    public static final int PADDING_LEFT = BoardCanvas.PADDING_LEFT;
    public static final int PADDING_RIGHT = BoardCanvas.PADDING_RIGHT;
    public static final int PADDING_TOP = BoardCanvas.PADDING_TOP;
    public static final int PADDING_DOWN = BoardCanvas.PADDING_DOWN;

    public static final Dimension ARROW_BUTTON_SIZE = new Dimension(80, 50);
    public static final Dimension ACTION_BUTTON_SIZE = new Dimension(120, 50);

    public static final Color BUTTON_COLOUR = new Color(68, 170, 58);

    private GUIClient gui;

    private List<Card> remainingCards;
    private JPanel remainingCardsPanel;
    private List<Card> cardsInHand;
    private JPanel cardsInHandPanel;

    private JLabel profileLabel;
    private JLabel[] diceLabels;
    private JLabel remainingStepLabel;

    private JButton EnterExitRoom;
    private JButton upButton;
    private JButton SecretPass;
    private JButton leftButton;
    private JButton downButton;
    private JButton rightButton;

    private JButton rollDiceButton;
    private JButton endTurnButton;
    private JButton suggestionButton;
    private JButton accusationButton;

    private Character currentPlayer;
    private int[] diceRolled = null;
    private int remainingSteps;

    public PlayerPanelCanvas(GUIClient guiClient) {

        this.gui = guiClient;
        remainingCards = gui.getRemainingCards();
        // cardsInHand = new ArrayList<>();

        // ================== BorderLayout =====================
        this.setLayout(new BorderLayout(5, 5));

        // =================== North, remaining cards =====================

        remainingCardsPanel = new JPanel();
        remainingCardsPanel.setBackground(null);
        remainingCardsPanel.setOpaque(false);
        remainingCardsPanel.setPreferredSize(new Dimension(WIDTH, NORTH_PANEL_HEIGHT));
        remainingCardsPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        // createEmptyBorder
        remainingCardsPanel
                .setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, Color.black));

        JLabel text = new JLabel(new ImageIcon(loadImage("Remaining_Cards.png")),
                SwingConstants.CENTER);

        remainingCardsPanel.add(text);

        // display remaining cards.
        remainingCards = gui.getRemainingCards();

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

        // ============== west, a player's character pic ===============
        profileLabel = new JLabel();
        profileLabel.setOpaque(false);
        profileLabel
                .setPreferredSize(new Dimension(WEST_PANEL_WIDTH, CENTRE_PANEL_HEIGHT));

        // createEmptyBorder
        profileLabel.setBorder(BorderFactory.createMatteBorder(PADDING_LEFT, PADDING_TOP,
                PADDING_LEFT, PADDING_LEFT, Color.black));

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

        // use JLabel as buttons
        diceLabels = new JLabel[gui.getNumDices()];
        for (int i = 0; i < diceLabels.length; i++) {
            diceLabels[i] = new JLabel();
            diceLabels[i].setBorder(null);
            diceGroup.add(diceLabels[i], Component.CENTER_ALIGNMENT);

            // add gaps between dices. and do not add a gap after the last dice
            if (i != diceLabels.length - 1) {
                int gap = 0;
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
        diceGroup.setBorder(BorderFactory.createMatteBorder(5, 5, 5, 15, Color.YELLOW));
        dicePanel.add(diceGroup);

        // createEmptyBorder
        dicePanel.setBorder(BorderFactory.createMatteBorder(PADDING_LEFT, PADDING_LEFT,
                PADDING_LEFT, PADDING_LEFT, Color.RED));

        // ============ east, buttons ===================
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(null);
        buttonPanel.setOpaque(false);
        buttonPanel
                .setPreferredSize(new Dimension(EAST_PANEL_WIDTH, CENTRE_PANEL_HEIGHT));

        // createEmptyBorder
        buttonPanel.setBorder(BorderFactory.createMatteBorder(PADDING_LEFT, PADDING_LEFT,
                PADDING_LEFT, PADDING_LEFT, Color.black));
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));

        // first row
        remainingStepLabel = new JLabel();
        remainingStepLabel.setBackground(null);
        remainingStepLabel.setOpaque(false);
        remainingStepLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // createEmptyBorder
        remainingStepLabel.setBorder(BorderFactory.createMatteBorder(PADDING_LEFT,
                PADDING_LEFT, PADDING_LEFT, PADDING_LEFT, Color.RED));

        // second row, a grid layout to show four direction buttons.
        JPanel movePanel = new JPanel();
        movePanel.setBackground(null);
        movePanel.setOpaque(false);
        movePanel.setLayout(new GridLayout(2, 3));
        movePanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // createEmptyBorder
        movePanel.setBorder(BorderFactory.createMatteBorder(PADDING_LEFT, PADDING_LEFT,
                PADDING_LEFT, PADDING_LEFT, Color.RED));

        // four direction buttons
        EnterExitRoom = createButton("Enter Room", ARROW_BUTTON_SIZE);
        EnterExitRoom.setEnabled(false);
        upButton = createButton("↑", ARROW_BUTTON_SIZE);
        SecretPass = createButton("Secret Pass", ARROW_BUTTON_SIZE);
        SecretPass.setEnabled(false);
        leftButton = createButton("←", ARROW_BUTTON_SIZE);
        downButton = createButton("↓", ARROW_BUTTON_SIZE);
        rightButton = createButton("→", ARROW_BUTTON_SIZE);

        // add listener on them
        EnterExitRoom.addActionListener(e -> {

            // TODO only interact with room
            // whenever this button is enabled, the player is standing at entrance (button
            // text as enter room)
            // or in a room, (button text as exit room, and pop a selection panel to
            // choose which exit to take)
            if (EnterExitRoom.getText().equals("Enter Room")) {

                // TODO add some stuff

                remainingSteps = 0;
                gui.setRemainingSteps(currentPlayer, remainingSteps);

                // TODO this make suggestion can extract as a method
                // can make suggestion now
                // and then pop up do you want to make accusation
                // if yes, pop up make accusation
                // if no end turn
                // gui.currentPlayerEndTurn();

                gui.currentPlayerEndTurn();
            } else {

                List<Entrance> entrances = gui.getBoard()
                        .lookForExit(gui.getPlayerByCharacter(currentPlayer));

                // TODO pop up a dialog to choose which room to exit

                remainingSteps--;
                gui.setRemainingSteps(currentPlayer, remainingSteps);
                if (remainingSteps == 0) {
                    gui.currentPlayerEndTurn();
                }
            }

            gui.update();
        });

        upButton.addActionListener(e -> {

            // TODO move up

            remainingSteps--;
            gui.setRemainingSteps(currentPlayer, remainingSteps);
            if (remainingSteps == 0) {
                gui.currentPlayerEndTurn();
            }
            gui.update();
        });

        SecretPass.addActionListener(e -> {

            // TODO take the secret passage
            // move player to there
            // brings a suggestion

            remainingSteps = 0;
            gui.setRemainingSteps(currentPlayer, remainingSteps);

            gui.currentPlayerEndTurn();

            gui.update();
        });

        leftButton.addActionListener(e -> {

            // TODO move left

            remainingSteps--;
            gui.setRemainingSteps(currentPlayer, remainingSteps);
            if (remainingSteps == 0) {
                gui.currentPlayerEndTurn();
            }
            gui.update();
        });

        downButton.addActionListener(e -> {

            // TODO move down

            remainingSteps--;
            gui.setRemainingSteps(currentPlayer, remainingSteps);
            if (remainingSteps == 0) {
                gui.currentPlayerEndTurn();
            }
            gui.update();
        });

        rightButton.addActionListener(e -> {

            // TODO move right

            remainingSteps--;
            gui.setRemainingSteps(currentPlayer, remainingSteps);
            if (remainingSteps == 0) {
                gui.currentPlayerEndTurn();
            }
            gui.update();
        });

        movePanel.add(EnterExitRoom);
        movePanel.add(upButton);
        movePanel.add(SecretPass);
        movePanel.add(leftButton);
        movePanel.add(downButton);
        movePanel.add(rightButton);

        // third row, another gridLayout
        JPanel actionPanel = new JPanel();
        actionPanel.setBackground(null);
        actionPanel.setOpaque(false);
        actionPanel.setLayout(new GridLayout(2, 2));
        actionPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // createEmptyBorder
        actionPanel.setBorder(BorderFactory.createMatteBorder(PADDING_LEFT, PADDING_LEFT,
                PADDING_LEFT, PADDING_LEFT, Color.RED));

        rollDiceButton = createButton("Roll Dice", ACTION_BUTTON_SIZE);
        rollDiceButton.setEnabled(false);
        endTurnButton = createButton("End Turn", ACTION_BUTTON_SIZE);
        suggestionButton = createButton("Suggestion", ACTION_BUTTON_SIZE);
        suggestionButton.setEnabled(false);
        accusationButton = createButton("Accusation", ACTION_BUTTON_SIZE);

        // add listeners
        rollDiceButton.addActionListener(e -> {
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
            gui.update();
        });

        endTurnButton.addActionListener(e -> {

            remainingSteps = 0;
            gui.setRemainingSteps(currentPlayer, 0);
            gui.currentPlayerEndTurn();
            gui.update();
        });

        suggestionButton.addActionListener(e -> {

            remainingSteps = 0;
            gui.setRemainingSteps(currentPlayer, remainingSteps);
            gui.popUpSuggestion();
            // TODO popup do you want to make accusation
            // if yes, pop up make accusation
            // if no end turn
            // gui.currentPlayerEndTurn();

            gui.update();
        });

        accusationButton.addActionListener(e -> {
            remainingSteps = 0;
            gui.setRemainingSteps(currentPlayer, remainingSteps);
            gui.popUpAccusation();
            gui.currentPlayerEndTurn();
            gui.update();
        });

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
        // createEmptyBorder
        cardsInHandPanel
                .setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, Color.black));

        currentPlayer = gui.getCurrentPlayer();
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

        // ================ Adding stuff together ===================
        this.add(remainingCardsPanel, BorderLayout.NORTH);
        this.add(profileLabel, BorderLayout.WEST);
        this.add(dicePanel, BorderLayout.CENTER);
        this.add(buttonPanel, BorderLayout.EAST);
        this.add(cardsInHandPanel, BorderLayout.SOUTH);

        this.setVisible(true);

        update();
    }

    // check the player's movable positions, if it contains north, enable it
    // otherwise disable it

    // if (button.getText().equals("Suggestion")) {
    // this should not be necessary
    // if currentPlayer is not in room, disable this button
    // Character currentPlayer = gui.getCurrentPlayer();
    // Position pos = gui.getPlayerPosition(currentPlayer);
    // if (pos instanceof Room)

    public void update() {

        // ===== North, remaining cards, No need to update during the game =======

        // ============== west, a player's character pic ===============
        currentPlayer = gui.getCurrentPlayer();
        profileLabel.setIcon(PROFILE_IMG[currentPlayer.ordinal()]);

        // ============== centre, dice or dices ====================
        if (remainingSteps == 0) {
            for (int i = 0; i < diceLabels.length; i++) {
                diceLabels[i].setIcon(null);
            }
        }

        // ============ east, buttons panel ===================
        valuateButtons();

        // ================= south, cards in hand =================
        cardsInHandPanel = new JPanel();
        cardsInHandPanel.setBackground(null);
        cardsInHandPanel.setOpaque(false);
        cardsInHandPanel.setPreferredSize(new Dimension(WIDTH, SOUTH_PANEL_HEIGHT));
        cardsInHandPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        // createEmptyBorder
        cardsInHandPanel
                .setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, Color.black));
        // cardsInHandPanel.setBorder(null);

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
        
        this.add(cardsInHandPanel, BorderLayout.SOUTH);

        // ================ Adding stuff ===================

        repaint();
    }

    private void valuateButtons() {
        // the text label for displaying remaining steps
        remainingStepLabel
                .setText("Remaining Steps: " + gui.getRemainingSteps(currentPlayer));

        // first if the player hasn't rolled dices, disable all buttons and return
        if (remainingSteps == 0) {
            EnterExitRoom.setEnabled(false);
            upButton.setEnabled(false);
            SecretPass.setEnabled(false);
            leftButton.setEnabled(false);
            downButton.setEnabled(false);
            rightButton.setEnabled(false);
            suggestionButton.setEnabled(false);
            accusationButton.setEnabled(false);
            endTurnButton.setEnabled(false);
            rollDiceButton.setEnabled(true);
            return;
        }

        // first let's disable all
        EnterExitRoom.setEnabled(false);
        upButton.setEnabled(false);
        SecretPass.setEnabled(false);
        leftButton.setEnabled(false);
        downButton.setEnabled(false);
        rightButton.setEnabled(false);
        suggestionButton.setEnabled(false);
        accusationButton.setEnabled(true);
        endTurnButton.setEnabled(true);

        Player player = gui.getPlayerByCharacter(currentPlayer);

        // if there are tiles in four directions
        if (gui.getBoard().lookNorth(player) != null) {
            // check if any other player standing there, then it's not an option
            boolean isBlocking = false;
            Tile tile = gui.getBoard().lookNorth(player);
            for (Player existingPlayer : gui.getPlayers()) {
                if (tile.equals(existingPlayer.getPosition())) {
                    isBlocking = true;
                }
            }
            upButton.setEnabled(!isBlocking);
        }

        if (gui.getBoard().lookEast(player) != null) {
            // check if any other player standing there, then it's not an option
            boolean isBlocking = false;
            Tile tile = gui.getBoard().lookEast(player);
            for (Player existingPlayer : gui.getPlayers()) {
                if (tile.equals(existingPlayer.getPosition())) {
                    isBlocking = true;
                }
            }
            rightButton.setEnabled(!isBlocking);
        }

        if (gui.getBoard().lookSouth(player) != null) {
            // check if any other player standing there, then it's not an option
            boolean isBlocking = false;
            Tile tile = gui.getBoard().lookSouth(player);
            for (Player existingPlayer : gui.getPlayers()) {
                if (tile.equals(existingPlayer.getPosition())) {
                    isBlocking = true;
                }
            }
            downButton.setEnabled(!isBlocking);
        }

        if (gui.getBoard().lookWest(player) != null) {
            // check if any other player standing there, then it's not an option
            boolean isBlocking = false;
            Tile tile = gui.getBoard().lookWest(player);
            for (Player existingPlayer : gui.getPlayers()) {
                if (tile.equals(existingPlayer.getPosition())) {
                    isBlocking = true;
                }
            }
            leftButton.setEnabled(!isBlocking);
        }

        // if the player is standing at an entrance to a room
        if (gui.getBoard().atEntranceTo(player) != null) {
            EnterExitRoom.setText("Enter Room");
            EnterExitRoom.setEnabled(true);
        }

        // if the player is in a room, get the exits
        List<Entrance> entrances = gui.getBoard().lookForExit(player);
        if (entrances != null && !entrances.isEmpty()) {
            EnterExitRoom.setText("Exit Room");
            EnterExitRoom.setEnabled(true);
        }

        // if the player is in a room, and there is a secret passage
        if (gui.getBoard().lookForSecPas(player) != null) {
            // in a room, have a secret passage
            SecretPass.setEnabled(true);
        }

        if (gui.getRemainingSteps(currentPlayer) == 0) {
            rollDiceButton.setEnabled(true);
        } else {
            rollDiceButton.setEnabled(false);
        }

        if (gui.getPlayerPosition(currentPlayer) instanceof Room) {
            suggestionButton.setEnabled(true);
        } else {
            suggestionButton.setEnabled(false);
        }
    }

    private JButton createButton(String label, Dimension dimension) {
        JButton button = new JButton(label);
        button.setBackground(BUTTON_COLOUR);
        button.setPreferredSize(dimension);
        // createEmptyBorder
        button.setBorder(BorderFactory.createMatteBorder(PADDING_LEFT, PADDING_LEFT,
                PADDING_LEFT, PADDING_LEFT, Color.YELLOW));
        return button;
    }

    private static CardLabel[] createCardLabel(ImageIcon[] cardImg, Card example) {
        CardLabel[] cards = new CardLabel[cardImg.length];
        for (int i = 0; i < cardImg.length; i++) {
            Card c;
            if (example instanceof Character) {
                c = Character.get(i);
            } else if (example instanceof Weapon) {
                c = Weapon.get(i);
            } else {
                c = Location.get(i);
            }

            cards[i] = new CardLabel(cardImg[i], c);
            cards[i].setBorder(null);
            cards[i].setToolTipText(c.toString());
            addMouseListenerOnCardLabel(cards[i]);
        }
        return cards;
    }

    private static void addMouseListenerOnCardLabel(CardLabel cardLabel) {
        cardLabel.addMouseListener(new MouseListener() {

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

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(PLAYER_PANEL, PADDING_LEFT, PADDING_TOP, WIDTH, HEIGHT, this);
    }

}

class CardLabel extends JLabel {

    private Card card;

    public CardLabel(ImageIcon img, Card card) {
        super(img);
        this.card = card;
    }

    public Card getCard() {
        return card;
    }
}
