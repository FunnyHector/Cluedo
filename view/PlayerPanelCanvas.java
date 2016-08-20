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

import configs.Configs;
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

    private Character currentPlayer;

    private List<Card> remainingCards;
    private List<Card> cardsInHand;

    JPanel remainingCardsPanel;
    private JLabel profileLabel;
    private JLabel[] diceButtons;
    private JLabel remainingSteps;
    JPanel cardsInHandPanel;

    public PlayerPanelCanvas(GUIClient guiClient) {

        this.gui = guiClient;
        remainingCards = gui.getRemainingCards();
        // cardsInHand = new ArrayList<>();

        // ================== BorderLayout =====================
        this.setLayout(new BorderLayout(5, 5));

        // =================== North, remaining cards =====================

        remainingCardsPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                // TODO paint the remaining cards one by one
                // see how many he has, and draw image accordingly
                // gaps can vary.

                // and remove the below cardImage lables

                // solution 2:
                // keep JLabels, make 21 cards as static JLabels
                // keep a list to iterate them.
                // set tooltip text on labels to show "???(blue)(fontsize bigger)" or
                // "cross(find a good cross character)(red)(fontsize bigger)"

            }
        };
        remainingCardsPanel.setBackground(null);
        remainingCardsPanel.setOpaque(false);
        remainingCardsPanel.setPreferredSize(new Dimension(WIDTH, SOUTH_PANEL_HEIGHT));

        // createEmptyBorder
        remainingCardsPanel
                .setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, Color.black));

        // these will be removed
        ImageIcon cardImage_2 = CHARACTER_IMG[2];
        remainingCardsPanel.add(new JLabel(cardImage_2));
        remainingCardsPanel.add(new JLabel(cardImage_2));
        remainingCardsPanel.add(new JLabel(cardImage_2));
        remainingCardsPanel.add(new JLabel(cardImage_2));

        // ============== west, a player's character pic ===============
        profileLabel = new JLabel();
        profileLabel.setOpaque(false);
        profileLabel
                .setPreferredSize(new Dimension(WEST_PANEL_WIDTH, CENTRE_PANEL_HEIGHT));

        // createEmptyBorder
        profileLabel.setBorder(BorderFactory.createMatteBorder(PADDING_LEFT, PADDING_LEFT,
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
        diceButtons = new JLabel[gui.getNumDices()];
        for (int i = 0; i < diceButtons.length; i++) {
            diceButtons[i] = new JLabel();
            diceButtons[i].setBorder(null);
            diceAddListener(diceButtons[i]);
            diceGroup.add(diceButtons[i], Component.CENTER_ALIGNMENT);

            // add gaps between dices. and do not add a gap after the last dice
            if (i != diceButtons.length - 1) {
                int gap = 0;
                if (diceButtons.length == 2) {
                    gap = 25;
                } else if (diceButtons.length == 3) {
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
        remainingSteps = new JLabel();
        remainingSteps.setBackground(null);
        remainingSteps.setOpaque(false);
        remainingSteps.setAlignmentX(Component.CENTER_ALIGNMENT);
        remainingSteps.setAlignmentY(Component.CENTER_ALIGNMENT);
        // createEmptyBorder
        remainingSteps.setBorder(BorderFactory.createMatteBorder(PADDING_LEFT,
                PADDING_LEFT, PADDING_LEFT, PADDING_LEFT, Color.RED));

        // second row, another grid layout to show four direction buttons.
        JPanel movePanel = new JPanel();
        movePanel.setBackground(null);
        movePanel.setOpaque(false);
        movePanel.setLayout(new GridLayout(2, 3));
        movePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        movePanel.setAlignmentY(Component.CENTER_ALIGNMENT);
        // createEmptyBorder
        movePanel.setBorder(BorderFactory.createMatteBorder(PADDING_LEFT, PADDING_LEFT,
                PADDING_LEFT, PADDING_LEFT, Color.RED));

        // add empty JLabels to hole the place
        movePanel.add(new JLabel());
        movePanel.add(makeButton("↑", BUTTON_COLOUR, ARROW_BUTTON_SIZE));
        movePanel.add(new JLabel());
        movePanel.add(makeButton("←", BUTTON_COLOUR, ARROW_BUTTON_SIZE));
        movePanel.add(makeButton("↓", BUTTON_COLOUR, ARROW_BUTTON_SIZE));
        movePanel.add(makeButton("→", BUTTON_COLOUR, ARROW_BUTTON_SIZE));

        // third row, another gridLayout
        JPanel actionPanel = new JPanel();
        actionPanel.setBackground(null);
        actionPanel.setOpaque(false);
        actionPanel.setLayout(new GridLayout(2, 2));
        actionPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        actionPanel.setAlignmentY(Component.CENTER_ALIGNMENT);
        // createEmptyBorder
        actionPanel.setBorder(BorderFactory.createMatteBorder(PADDING_LEFT, PADDING_LEFT,
                PADDING_LEFT, PADDING_LEFT, Color.RED));

        actionPanel.add(makeButton("Roll Dice", BUTTON_COLOUR, ACTION_BUTTON_SIZE));
        actionPanel.add(makeButton("End Turn", BUTTON_COLOUR, ACTION_BUTTON_SIZE));
        actionPanel.add(makeButton("Suggestion", BUTTON_COLOUR, ACTION_BUTTON_SIZE));
        actionPanel.add(makeButton("Accusation", BUTTON_COLOUR, ACTION_BUTTON_SIZE));

        // put them together
        buttonPanel.add(remainingSteps);
        buttonPanel.add(Box.createRigidArea(new Dimension(5, 5)));
        buttonPanel.add(movePanel);
        buttonPanel.add(Box.createRigidArea(new Dimension(5, 5)));
        buttonPanel.add(actionPanel);

        // ================= south, cards =================
        cardsInHandPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                // TODO paint the player's cards one by one
                // see how many he has, and draw image accordingly
                // gaps can vary.

                // and remove the below cardImage lables

            }
        };
        cardsInHandPanel.setBackground(null);
        cardsInHandPanel.setOpaque(false);
        cardsInHandPanel.setPreferredSize(new Dimension(WIDTH, SOUTH_PANEL_HEIGHT));

        // createEmptyBorder
        cardsInHandPanel
                .setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, Color.black));

        // these will be removed
        ImageIcon cardImage = CHARACTER_IMG[3];
        cardsInHandPanel.add(new JLabel(cardImage));
        cardsInHandPanel.add(new JLabel(cardImage));
        cardsInHandPanel.add(new JLabel(cardImage));
        cardsInHandPanel.add(new JLabel(cardImage));

        // ================ Adding stuff ===================
        this.add(remainingCardsPanel, BorderLayout.NORTH);
        this.add(dicePanel, BorderLayout.CENTER);
        this.add(profileLabel, BorderLayout.WEST);
        this.add(buttonPanel, BorderLayout.EAST);
        this.add(cardsInHandPanel, BorderLayout.SOUTH);

        this.setVisible(true);
    }

    public void update() {

        // TODO is it necessary to update five panels individually?

        // north: remainint cards

        // west: currentPlayer
        currentPlayer = gui.getCurrentPlayer();
        ImageIcon playerImage = PROFILE_IMG[currentPlayer.ordinal()];
        profileLabel.setIcon(playerImage);

        // centre: dices
        int[] diceRoll = gui.rollDice(currentPlayer);
        for (int i = 0; i < diceButtons.length; i++) {
            diceButtons[i].setIcon(createDiceImg(diceRoll[i]));
        }

        remainingSteps
                .setText("Remaining Steps: " + gui.getRemainingSteps(currentPlayer));

        // east: button panel, need to update remaining steps

        // south: cards in hands

        repaint();
    }

    private void diceAddListener(JLabel diceLabel) {
        diceLabel.addMouseListener(new MouseListener() {
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
                System.out.println("button pressed");
            }
        });
    }

    private JButton makeButton(String label, Color color, Dimension dimension) {
        JButton button = new JButton(label);
        button.setBackground(color);
        button.setPreferredSize(dimension);
        // createEmptyBorder
        button.setBorder(BorderFactory.createMatteBorder(PADDING_LEFT, PADDING_LEFT,
                PADDING_LEFT, PADDING_LEFT, Color.YELLOW));
        button.addActionListener(e -> {

            // TODO add button listeners

            if (button.getText().equals("↑")) {

            } else if (button.getText().equals("←")) {

            } else if (button.getText().equals("↓")) {

            } else if (button.getText().equals("→")) {

            } else if (button.getText().equals("Roll Dice")) {

            } else if (button.getText().equals("End Turn")) {
                update();

            } else if (button.getText().equals("Suggestion")) {

            } else if (button.getText().equals("Accusation")) {

            }
        });

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

    private ImageIcon createDiceImg(int i) {
        return DICE_IMG[i];
    }

    @Override
    public void paintComponent(Graphics g) {
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
