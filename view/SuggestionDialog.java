package view;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import card.Character;
import card.Weapon;
import tile.Position;
import tile.Room;
import card.Location;
import ui.GUIClient;

public class SuggestionDialog extends JDialog {

    public static final Dimension CARD_DIMENSION = new Dimension(
            PlayerPanelCanvas.CHARACTER_IMG[0].getIconWidth(),
            PlayerPanelCanvas.CHARACTER_IMG[0].getIconHeight());

    private CardLayout cardLayout = new CardLayout();

    private GUIClient gui;
    private boolean isAccusation;
    private Character character;
    private Weapon weapon;
    private Location location;

    public SuggestionDialog(GUIClient parent, Window windowForComponent, String title,
            boolean isAccusation) {
        super(windowForComponent, title);
        this.gui = parent;
        this.isAccusation = isAccusation;

        JPanel mainPanel = new JPanel();

        // we use card layout
        mainPanel.setLayout(cardLayout);

        // let's make three panels
        addCharacterPanel(mainPanel);
        addWeaponPanel(mainPanel);
        addLocationPanel(mainPanel);

        // and show the dialog
        this.add(mainPanel);
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.setModal(true);
        this.setResizable(false);
        this.pack();
        this.setLocationRelativeTo(parent);
        this.setVisible(true);
    }

    private void addCharacterPanel(JPanel mainPanel) {

        JPanel characterPanel = new JPanel();
        characterPanel.setLayout(new BoxLayout(characterPanel, BoxLayout.Y_AXIS));
        characterPanel.setBorder(BorderFactory
                .createTitledBorder(
                        BorderFactory.createCompoundBorder(
                                BorderFactory.createSoftBevelBorder(BevelBorder.RAISED),
                                BorderFactory.createEtchedBorder(EtchedBorder.LOWERED)),
                        "Select Character:", TitledBorder.LEFT, TitledBorder.TOP));

        List<JRadioButton> rButtonList = new ArrayList<>();

        JPanel cardDisplay = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                for (JRadioButton b : rButtonList) {
                    if (b.isSelected()) {
                        g.drawImage(
                                PlayerPanelCanvas.CHARACTER_IMG[Integer
                                        .parseInt(b.getActionCommand())].getImage(),
                                0, 0, this);
                        break;
                    }
                }
            }
        };

        cardDisplay.setPreferredSize(CARD_DIMENSION);
        cardDisplay.setAlignmentY(TOP_ALIGNMENT);
        // this prevents a bug where the component won't be drawn until it is resized.
        cardDisplay.setVisible(true);

        // radio buttons
        ButtonGroup radioButtonGroup = new ButtonGroup();
        JPanel radioButtonsPanel = new JPanel();
        radioButtonsPanel.setLayout(new BoxLayout(radioButtonsPanel, BoxLayout.Y_AXIS));
        radioButtonsPanel.setAlignmentY(TOP_ALIGNMENT);

        // two buttons at bottom
        JButton confirm = new JButton("Next");
        confirm.setEnabled(false);
        JButton cancel = new JButton("Cancel");

        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardDisplay.repaint();
                if (!confirm.isEnabled()) {
                    confirm.setEnabled(true);
                }
            }
        };

        // add radio buttons
        for (int i = 0; i < Character.values().length; i++) {
            Character c = Character.get(i);
            JRadioButton rButton = new JRadioButton(c.toString(), false);
            rButton.setActionCommand(String.valueOf(i));
            rButton.addActionListener(al);
            rButton.setAlignmentX(LEFT_ALIGNMENT);
            radioButtonGroup.add(rButton);
            rButtonList.add(rButton);
            radioButtonsPanel.add(rButton);
            radioButtonsPanel.add(Box.createRigidArea(new Dimension(5, 5)));
        }

        // the middle panel to hold radio buttons and card display
        JPanel midPanel = new JPanel();
        midPanel.setLayout(new BoxLayout(midPanel, BoxLayout.X_AXIS));
        midPanel.add(radioButtonsPanel);
        midPanel.add(Box.createRigidArea(new Dimension(15, 15)));
        midPanel.add(cardDisplay);
        midPanel.add(Box.createRigidArea(new Dimension(5, 5)));

        // a text prompt
        JLabel text = new JLabel("Who?");
        JPanel textPane = new JPanel();
        textPane.setLayout(new BoxLayout(textPane, BoxLayout.X_AXIS));
        textPane.setAlignmentX(Component.CENTER_ALIGNMENT);
        textPane.add(text);

        // confirm button's listener
        confirm.addActionListener(e -> {
            for (JRadioButton b : rButtonList) {
                if (b.isSelected()) {
                    character = Character.get(Integer.parseInt(b.getActionCommand()));
                    break;
                }
            }
            cardLayout.next(mainPanel);
        });

        // cancel button's listener
        cancel.addActionListener(e -> SuggestionDialog.this.dispose());

        // bottom panel, which contains two buttons
        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.X_AXIS));
        buttonPane.setAlignmentX(Component.CENTER_ALIGNMENT);

        buttonPane.add(cancel);
        buttonPane.add(Box.createRigidArea(new Dimension(30, 20)));
        buttonPane.add(confirm);

        // put top, middle, bottom button together
        characterPanel.add(Box.createRigidArea(new Dimension(10, 10)));
        characterPanel.add(textPane);
        characterPanel.add(Box.createRigidArea(new Dimension(15, 15)));
        characterPanel.add(midPanel);
        characterPanel.add(Box.createRigidArea(new Dimension(15, 15)));
        characterPanel.add(buttonPane);

        mainPanel.add(characterPanel);
    }

    private void addWeaponPanel(JPanel mainPanel) {

        JPanel weaponPanel = new JPanel();

        weaponPanel.setLayout(new BoxLayout(weaponPanel, BoxLayout.Y_AXIS));
        weaponPanel.setBorder(BorderFactory
                .createTitledBorder(
                        BorderFactory.createCompoundBorder(
                                BorderFactory.createSoftBevelBorder(BevelBorder.RAISED),
                                BorderFactory.createEtchedBorder(EtchedBorder.LOWERED)),
                        "Select Weapon:", TitledBorder.CENTER, TitledBorder.TOP));

        List<JRadioButton> rButtonList = new ArrayList<>();

        JPanel cardDisplay = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                for (JRadioButton b : rButtonList) {
                    if (b.isSelected()) {
                        g.drawImage(
                                PlayerPanelCanvas.WEAPON_IMG[Integer
                                        .parseInt(b.getActionCommand())].getImage(),
                                0, 0, this);
                        break;
                    }
                }
            }
        };

        cardDisplay.setPreferredSize(CARD_DIMENSION);
        cardDisplay.setAlignmentY(TOP_ALIGNMENT);
        // this prevents a bug where the component won't be drawn until it is resized.
        cardDisplay.setVisible(true);

        // radio buttons
        ButtonGroup radioButtonGroup = new ButtonGroup();
        JPanel radioButtonsPanel = new JPanel();
        radioButtonsPanel.setLayout(new BoxLayout(radioButtonsPanel, BoxLayout.Y_AXIS));
        radioButtonsPanel.setAlignmentY(TOP_ALIGNMENT);

        // two buttons at bottom
        JButton confirm = new JButton("Next");
        confirm.setEnabled(false);
        JButton cancel = new JButton("Previous");
        cancel.setEnabled(true);

        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardDisplay.repaint();
                if (!confirm.isEnabled()) {
                    confirm.setEnabled(true);
                }
            }
        };

        // add radio buttons
        for (int i = 0; i < Weapon.values().length; i++) {
            Weapon w = Weapon.get(i);
            JRadioButton rButton = new JRadioButton(w.toString(), false);
            rButton.setActionCommand(String.valueOf(i));
            rButton.addActionListener(al);
            rButton.setAlignmentX(LEFT_ALIGNMENT);
            radioButtonGroup.add(rButton);
            rButtonList.add(rButton);
            radioButtonsPanel.add(rButton);
            radioButtonsPanel.add(Box.createRigidArea(new Dimension(5, 5)));
        }

        // the middle panel to hold radio buttons and card display
        JPanel midPanel = new JPanel();
        midPanel.setLayout(new BoxLayout(midPanel, BoxLayout.X_AXIS));
        midPanel.add(radioButtonsPanel);
        midPanel.add(Box.createRigidArea(new Dimension(40, 15)));
        midPanel.add(cardDisplay);
        midPanel.add(Box.createRigidArea(new Dimension(5, 5)));

        // a text prompt
        JLabel text = new JLabel("...commited crime with weapon?");
        JPanel textPane = new JPanel();
        textPane.setLayout(new BoxLayout(textPane, BoxLayout.X_AXIS));
        textPane.setAlignmentX(Component.CENTER_ALIGNMENT);
        textPane.add(text);

        // confirm button's listener
        confirm.addActionListener(e -> {
            for (JRadioButton b : rButtonList) {
                if (b.isSelected()) {
                    weapon = Weapon.get(Integer.parseInt(b.getActionCommand()));
                    break;
                }
            }
            cardLayout.next(mainPanel);
        });

        // confirm button at bottom
        cancel.addActionListener(e -> cardLayout.previous(mainPanel));

        // bottom panel, which contains two buttons
        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.X_AXIS));
        buttonPane.setAlignmentX(Component.CENTER_ALIGNMENT);

        buttonPane.add(cancel);
        buttonPane.add(Box.createRigidArea(new Dimension(30, 20)));
        buttonPane.add(confirm);

        // put top, middle, bottom button together
        weaponPanel.add(Box.createRigidArea(new Dimension(10, 10)));
        weaponPanel.add(textPane);
        weaponPanel.add(Box.createRigidArea(new Dimension(15, 15)));
        weaponPanel.add(midPanel);
        weaponPanel.add(Box.createRigidArea(new Dimension(15, 15)));
        weaponPanel.add(buttonPane);

        mainPanel.add(weaponPanel);
    }

    private void addLocationPanel(JPanel mainPanel) {

        JPanel locationPanel = new JPanel();

        locationPanel.setLayout(new BoxLayout(locationPanel, BoxLayout.Y_AXIS));
        locationPanel.setBorder(BorderFactory
                .createTitledBorder(
                        BorderFactory.createCompoundBorder(
                                BorderFactory.createSoftBevelBorder(BevelBorder.RAISED),
                                BorderFactory.createEtchedBorder(EtchedBorder.LOWERED)),
                        "Select Location:", TitledBorder.RIGHT, TitledBorder.TOP));

        List<JRadioButton> rButtonList = new ArrayList<>();

        JPanel cardDisplay = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                for (JRadioButton b : rButtonList) {
                    super.paintComponent(g);
                    if (b.isSelected()) {
                        g.drawImage(
                                PlayerPanelCanvas.LOCATION_IMG[Integer
                                        .parseInt(b.getActionCommand())].getImage(),
                                0, 0, this);
                        break;
                    }
                }
            }
        };

        cardDisplay.setPreferredSize(CARD_DIMENSION);
        cardDisplay.setAlignmentY(CENTER_ALIGNMENT);
        // this prevents a bug where the component won't be drawn until it is resized.
        cardDisplay.setVisible(true);

        // radio buttons
        ButtonGroup radioButtonGroup = new ButtonGroup();
        JPanel radioButtonsPanel = new JPanel();
        radioButtonsPanel.setLayout(new BoxLayout(radioButtonsPanel, BoxLayout.Y_AXIS));
        radioButtonsPanel.setAlignmentY(CENTER_ALIGNMENT);

        // two buttons at bottom
        JButton confirm;
        if (isAccusation) {
            confirm = new JButton("Make Accusation!");
        } else {
            confirm = new JButton("Make Suggestion!");
        }

        JButton cancel = new JButton("Previous");

        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardDisplay.repaint();
                if (!confirm.isEnabled()) {
                    confirm.setEnabled(true);
                }
            }
        };

        // add radio buttons
        for (int i = 0; i < Location.values().length; i++) {
            Location l = Location.get(i);

            JRadioButton rButton = new JRadioButton(l.toString(), false);
            rButton.setActionCommand(String.valueOf(i));
            rButton.addActionListener(al);
            rButton.setAlignmentX(LEFT_ALIGNMENT);
            radioButtonGroup.add(rButton);
            rButtonList.add(rButton);
            radioButtonsPanel.add(rButton);
            radioButtonsPanel.add(Box.createRigidArea(new Dimension(5, 5)));
        }

        // disable other rooms, only enable the current room
        if (!isAccusation) {
            Character currentPlayer = gui.getCurrentPlayer();
            Position pos = gui.getPlayerPosition(currentPlayer);
            if (pos instanceof Room) {
                Room room = (Room) pos;

                Location loc = room.getRoom();

                for (JRadioButton button : rButtonList) {
                    // disable other rooms, only current room is selectable
                    if (Integer.parseInt(button.getActionCommand()) == loc.ordinal()) {
                        button.setEnabled(true);
                        button.setSelected(true);
                    } else {
                        button.setEnabled(false);
                        button.setSelected(false);
                    }
                }
            }
        }

        // the middle panel to hold radio buttons and card display
        JPanel midPanel = new JPanel();
        midPanel.setLayout(new BoxLayout(midPanel, BoxLayout.X_AXIS));
        midPanel.add(radioButtonsPanel);
        midPanel.add(Box.createRigidArea(new Dimension(15, 15)));
        midPanel.add(cardDisplay);
        midPanel.add(Box.createRigidArea(new Dimension(5, 5)));

        // a text prompt
        JLabel text = new JLabel("...in which room?");
        JPanel textPane = new JPanel();
        textPane.setLayout(new BoxLayout(textPane, BoxLayout.X_AXIS));
        textPane.setAlignmentX(Component.CENTER_ALIGNMENT);
        textPane.add(text);

        // confirm button's listener
        confirm.addActionListener(e -> {
            for (JRadioButton b : rButtonList) {
                if (b.isSelected()) {
                    location = Location.get(Integer.parseInt(b.getActionCommand()));
                    break;
                }
            }
            // dispose dialog
            SuggestionDialog.this.dispose();

            if (isAccusation) {
                // now make an accusation
                gui.makeAccusation(character, weapon, location);
            } else {
                // now make a suggestion
                gui.makeSuggestion(character, weapon, location);
            }
        });

        // TODO if this is a suggestion, when the player press make suggestion
        // give the result of suggestion
        // and ask if the player want to make accusation right away.
        // if yes, then pop up a accusation dialog

        // confirm button at bottom
        cancel.addActionListener(e -> cardLayout.previous(mainPanel));

        // bottom panel, which contains two buttons
        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.X_AXIS));
        buttonPane.setAlignmentX(Component.CENTER_ALIGNMENT);

        buttonPane.add(cancel);
        buttonPane.add(Box.createRigidArea(new Dimension(30, 20)));
        buttonPane.add(confirm);

        // put top, middle, bottom button together
        locationPanel.add(Box.createRigidArea(new Dimension(10, 10)));
        locationPanel.add(textPane);
        locationPanel.add(Box.createRigidArea(new Dimension(15, 15)));
        locationPanel.add(midPanel);
        locationPanel.add(Box.createRigidArea(new Dimension(15, 15)));
        locationPanel.add(buttonPane);

        mainPanel.add(locationPanel);

    }

}
