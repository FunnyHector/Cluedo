package view;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
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

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import configs.Configs;
import ui.GUIClient;

public class PlayerPanelCanvas extends JPanel {

    private GUIClient gui;

    public static final Image PLAYER_PANEL = GUIClient
            .loadImage("Player_Panel_Background.png");

    public static final Image[] PROFILE_IMG = {
            GUIClient.loadImage("Profile_Miss_Scarlet.png"),
            GUIClient.loadImage("Profile_Colonel_Mustard.png"),
            GUIClient.loadImage("Profile_Mrs_White.png"),
            GUIClient.loadImage("Profile_The_Reverend_Green.png"),
            GUIClient.loadImage("Profile_Mrs_Peacock.png"),
            GUIClient.loadImage("Profile_Professor_Plum.png") };

    public static final Image[] DICE_IMG = { GUIClient.loadImage("Dice_1.png"),
            GUIClient.loadImage("Dice_2.png"), GUIClient.loadImage("Dice_3.png"),
            GUIClient.loadImage("Dice_4.png"), GUIClient.loadImage("Dice_5.png"),
            GUIClient.loadImage("Dice_6.png") };

    public static final int WIDTH = 700;
    public static final int HEIGHT = 600;

    public static final int PADDING_LEFT = 10;
    public static final int PADDING_RIGHT = PADDING_LEFT;
    public static final int PADDING_TOP = 20;
    public static final int PADDING_DOWN = PADDING_LEFT;

    private String textPrompt = "Test";

    public PlayerPanelCanvas(GUIClient guiClient) {

        this.gui = guiClient;

        // ================== BorderLayout =====================
        this.setLayout(new BorderLayout(5, 5));

        // ============== west, a player's character pic ===============
        ImageIcon playerImage = new ImageIcon(PROFILE_IMG[1]);
        JLabel playerLabel = new JLabel(playerImage);
        playerLabel.setBorder(null);
        playerLabel.setOpaque(false);

        // ============== centre, dice or dices ====================

        ImageIcon diceIcon = new ImageIcon(DICE_IMG[1]);
        JLabel diceButton = new JLabel(diceIcon);
        diceButton.setBorder(null);
        MouseListener diceListener = new MouseListener() {
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
        diceButton.addMouseListener(diceListener);

        // ============ east, buttons ===================
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(null);
        buttonPanel.setOpaque(false);

        buttonPanel.setLayout(new GridLayout(3, 1));

        // first row
        JLabel remainingSteps = new JLabel();
        remainingSteps.setBackground(null);
        remainingSteps.setOpaque(false);
        remainingSteps.setText(null);

        // second row, another grid layout to show four direction buttons.
        JPanel movePanel = new JPanel();
        movePanel.setBackground(null);
        movePanel.setOpaque(false);
        movePanel.setLayout(new GridLayout(2, 3));

        JButton up = new JButton("↑");
        JButton right = new JButton("→");
        JButton down = new JButton("↓");
        JButton left = new JButton("←");

        up.setBackground(new Color(68, 170, 58));
        right.setBackground(new Color(68, 170, 58));
        down.setBackground(new Color(68, 170, 58));
        left.setBackground(new Color(68, 170, 58));

        movePanel.add(new JLabel());
        movePanel.add(up);
        movePanel.add(new JLabel());
        movePanel.add(left);
        movePanel.add(down);
        movePanel.add(right);

        // third row, another gridLayout
        JPanel actionPanel = new JPanel();
        actionPanel.setBackground(null);
        actionPanel.setOpaque(false);
        actionPanel.setLayout(new GridLayout(2, 2));

        JButton rollDice = new JButton("Roll Dice");
        JButton endTurn = new JButton("End Turn");
        JButton suggestion = new JButton("suggestion");
        JButton accusation = new JButton("accusation");

        rollDice.setBackground(new Color(68, 170, 58));
        endTurn.setBackground(new Color(68, 170, 58));
        suggestion.setBackground(new Color(68, 170, 58));
        accusation.setBackground(new Color(68, 170, 58));

        actionPanel.add(rollDice);
        actionPanel.add(endTurn);
        actionPanel.add(suggestion);
        actionPanel.add(accusation);

        // put them together
        buttonPanel.add(remainingSteps);
        buttonPanel.add(movePanel);
        buttonPanel.add(actionPanel);

        // ================= south, cards =================
        JPanel cardPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                // TODO paint the player's cards one by one
                // see how many he has, and draw image accordingly
                // gaps can vary.

            }
        };
        cardPanel.setBackground(null);
        cardPanel.setOpaque(false);

        ImageIcon cardImage = new ImageIcon(GUIClient.CHARACTER_IMG[3]);
        
        cardPanel.add(new JLabel(cardImage));
        cardPanel.add(new JLabel(cardImage));
        cardPanel.add(new JLabel(cardImage));
        cardPanel.add(new JLabel(cardImage));

        // ================ Adding stuff ===================
        this.add(diceButton, BorderLayout.CENTER);
        this.add(playerLabel, BorderLayout.WEST);
        this.add(buttonPanel, BorderLayout.EAST);
        this.add(cardPanel, BorderLayout.SOUTH);

        this.setVisible(true);

    }

    @Override
    public void paintComponent(Graphics g) {
        g.drawImage(PLAYER_PANEL, PADDING_LEFT, PADDING_TOP, WIDTH, HEIGHT, this);
    }
}
