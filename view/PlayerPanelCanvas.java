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

public class PlayerPanelCanvas extends JPanel {

    public static final Image PLAYER_PANEL = GUIClient
            .loadImage("Player_Panel_Background.png");

    public static final ImageIcon[] PROFILE_IMG = {
            new ImageIcon(GUIClient.loadImage("Profile_Miss_Scarlet.png")),
            new ImageIcon(GUIClient.loadImage("Profile_Colonel_Mustard.png")),
            new ImageIcon(GUIClient.loadImage("Profile_Mrs_White.png")),
            new ImageIcon(GUIClient.loadImage("Profile_The_Reverend_Green.png")),
            new ImageIcon(GUIClient.loadImage("Profile_Mrs_Peacock.png")),
            new ImageIcon(GUIClient.loadImage("Profile_Professor_Plum.png")) };

    public static final ImageIcon[] DICE_IMG = {
            new ImageIcon(GUIClient.loadImage("Dice_1.png")),
            new ImageIcon(GUIClient.loadImage("Dice_2.png")),
            new ImageIcon(GUIClient.loadImage("Dice_3.png")),
            new ImageIcon(GUIClient.loadImage("Dice_4.png")),
            new ImageIcon(GUIClient.loadImage("Dice_5.png")),
            new ImageIcon(GUIClient.loadImage("Dice_6.png")) };

    public static final Image[] CHARACTER_IMG = {
            GUIClient.loadImage("Character_Miss_Scarlet.png"),
            GUIClient.loadImage("Character_Colonel_Mustard.png"),
            GUIClient.loadImage("Character_Mrs_White.png"),
            GUIClient.loadImage("Character_The_Reverend_Green.png"),
            GUIClient.loadImage("Character_Mrs_Peacock.png"),
            GUIClient.loadImage("Character_Professor_Plum.png") };

    public static final Image[] Weapon_IMAGES = {
            GUIClient.loadImage("Weapon_Candlestick.png"),
            GUIClient.loadImage("Weapon_Dagger.png"),
            GUIClient.loadImage("Weapon_Lead_Pipe.png"),
            GUIClient.loadImage("Weapon_Revolver.png"),
            GUIClient.loadImage("Weapon_Rope.png"),
            GUIClient.loadImage("Weapon_Spanner.png") };

    public static final Image[] Location_IMAGES = {
            GUIClient.loadImage("Location_Kitchen.png"),
            GUIClient.loadImage("Location_Ball_room.png"),
            GUIClient.loadImage("Location_Conservatory.png"),
            GUIClient.loadImage("Location_Billard_Room.png"),
            GUIClient.loadImage("Location_Library.png"),
            GUIClient.loadImage("Location_Study.png"),
            GUIClient.loadImage("Location_Hall.png"),
            GUIClient.loadImage("Location_Lounge.png"),
            GUIClient.loadImage("Location_Dining_Room.png") };

    public static final int WIDTH = 700;
    public static final int HEIGHT = 600;
    public static final int CARD_PANEL_HEIGHT = 280;
    public static final int UPPER_PANEL_HEIGHT = 280;
    public static final int PROFILE_PANEL_WIDTH = 230;
    public static final int DICE_PANEL_WIDTH = 150;
    public static final int BUTTON_PANEL_WIDTH = 320;

    public static final int PADDING_LEFT = 10;
    public static final int PADDING_RIGHT = PADDING_LEFT;
    public static final int PADDING_TOP = 20;
    public static final int PADDING_DOWN = PADDING_LEFT;

    public static final Dimension ARROW_BUTTON_SIZE = new Dimension(80, 50);
    public static final Dimension ACTION_BUTTON_SIZE = new Dimension(120, 50);

    public static final Color BUTTON_COLOUR = new Color(68, 170, 58);

    private String textPrompt = "Test";

    private GUIClient gui;

    public PlayerPanelCanvas(GUIClient guiClient) {

        this.gui = guiClient;

        // ================== BorderLayout =====================
        this.setLayout(new BorderLayout(5, 5));

        // ============== west, a player's character pic ===============
        ImageIcon playerImage = PROFILE_IMG[1];
        JLabel playerLabel = new JLabel(playerImage);
        playerLabel.setOpaque(false);
        playerLabel
                .setPreferredSize(new Dimension(PROFILE_PANEL_WIDTH, UPPER_PANEL_HEIGHT));

        // createEmptyBorder
        playerLabel.setBorder(BorderFactory.createMatteBorder(PADDING_LEFT, PADDING_LEFT,
                PADDING_LEFT, PADDING_LEFT, Color.black));

        // ============== centre, dice or dices ====================
        MouseListener diceListener = createDiceListener();

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
        JLabel[] diceButtons = new JLabel[gui.getNumDices()];
        for (int i = 0; i < diceButtons.length; i++) {
            diceButtons[i] = new JLabel(randomDiceNum(6));
            diceButtons[i].setBorder(null);
            diceButtons[i].addMouseListener(diceListener);
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
                .setPreferredSize(new Dimension(BUTTON_PANEL_WIDTH, UPPER_PANEL_HEIGHT));

        // createEmptyBorder
        buttonPanel.setBorder(BorderFactory.createMatteBorder(PADDING_LEFT, PADDING_LEFT,
                PADDING_LEFT, PADDING_LEFT, Color.black));
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));

        // first row
        JLabel remainingSteps = new JLabel("Remaining Steps: ");
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
        movePanel.add(makeButton("←", BUTTON_COLOUR, ARROW_BUTTON_SIZE));

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
        JPanel cardPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                // TODO paint the player's cards one by one
                // see how many he has, and draw image accordingly
                // gaps can vary.

                // and remove the below cardImage lables

            }
        };
        cardPanel.setBackground(null);
        cardPanel.setOpaque(false);
        cardPanel.setPreferredSize(new Dimension(WIDTH, CARD_PANEL_HEIGHT));

        // createEmptyBorder
        cardPanel.setBorder(BorderFactory.createMatteBorder(PADDING_LEFT, PADDING_LEFT,
                PADDING_LEFT, PADDING_LEFT, Color.black));

        // these will be removed
        ImageIcon cardImage = new ImageIcon(CHARACTER_IMG[3]);
        cardPanel.add(new JLabel(cardImage));
        cardPanel.add(new JLabel(cardImage));
        cardPanel.add(new JLabel(cardImage));
        cardPanel.add(new JLabel(cardImage));

        // ================ Adding stuff ===================

        this.add(dicePanel, BorderLayout.CENTER);
        this.add(playerLabel, BorderLayout.WEST);
        this.add(buttonPanel, BorderLayout.EAST);
        this.add(cardPanel, BorderLayout.SOUTH);

        this.setVisible(true);
    }

    private MouseListener createDiceListener() {
        return new MouseListener() {
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
        };
    }

    private JButton makeButton(String label, Color color, Dimension dimension) {
        JButton button = new JButton(label);
        button.setBackground(color);
        button.setPreferredSize(dimension);
        // createEmptyBorder
        button.setBorder(BorderFactory.createMatteBorder(PADDING_LEFT, PADDING_LEFT,
                PADDING_LEFT, PADDING_LEFT, Color.YELLOW));
        return button;
    }

    public ImageIcon randomDiceNum(int diceRoll) {
        return DICE_IMG[diceRoll - 1];
    }

    @Override
    public void paintComponent(Graphics g) {
        g.drawImage(PLAYER_PANEL, PADDING_LEFT, PADDING_TOP, WIDTH, HEIGHT, this);
    }
}
