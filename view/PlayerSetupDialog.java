package view;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import card.Character;
import ui.GUIClient;

/**
 * This class is a custom dialog for players to choose which character he / she want to
 * play as one by one.
 * 
 * @author Hector
 *
 */
@SuppressWarnings("serial")
public class PlayerSetupDialog extends JDialog {

    /*
     * A dimension used to set the size of player profile picture
     */
    private static final Dimension PROFILE_DIMENSION = new Dimension(
            PlayerPanelCanvas.PROFILE_IMG[0].getIconWidth(),
            PlayerPanelCanvas.PROFILE_IMG[0].getIconHeight());

    /**
     * Construct a dialog, let players choose which character he / she want to play as one
     * by one.
     * 
     * @param parent
     *            --- the Main GUI of this game
     * @param windowForComponent
     *            --- the owner component
     * @param string
     *            --- the tile of this dialog
     */
    public PlayerSetupDialog(GUIClient parent, Window windowForComponent, String string) {
        super(windowForComponent, string);

        /*
         * a list used to remember the order of chosen characters in case someone want to
         * re-select another character
         */
        Map<Character, String> selectedCharacters = new HashMap<>();
        // a map used to maintain a [character = player name] pair
        List<Integer> characterOder = new ArrayList<>();
        // a bunch of JRadioButtons
        List<JRadioButton> rButtonList = new ArrayList<>();

        JPanel cardDisplay = new JPanel() {
            protected void paintComponent(Graphics g) {
                for (JRadioButton b : rButtonList) {
                    if (b.isSelected()) {
                        // paint the profile picture according to radio buttons
                        g.drawImage(
                                PlayerPanelCanvas.PROFILE_IMG[Integer
                                        .parseInt(b.getActionCommand())].getImage(),
                                0, 0, this);
                        break;
                    }
                }
            }
        };
        cardDisplay.setPreferredSize(PROFILE_DIMENSION);
        // this prevents a bug where the component won't be drawn until it is resized.
        cardDisplay.setVisible(true);

        // two buttons at bottom
        JButton confirm = new JButton("Next");
        JButton cancel = new JButton("Previous");
        confirm.setEnabled(false);
        cancel.setEnabled(false);

        // A ButtonGroup used to ensure that only one is selected.
        ButtonGroup radioButtonGroup = new ButtonGroup();
        JPanel radioButtonsPanel = new JPanel();
        radioButtonsPanel.setLayout(new BoxLayout(radioButtonsPanel, BoxLayout.Y_AXIS));

        // a text field to input player's name (TOTALLY UNNECESSARY!! ONLY FOR ASSIGNMENT)
        JTextField nameInput = new JTextField(8);
        nameInput.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                tryUnlockNext(selectedCharacters, rButtonList, confirm, nameInput);
            }
        });

        // a listener for radio buttons
        ActionListener al = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // update the profile picture
                cardDisplay.repaint();
                tryUnlockNext(selectedCharacters, rButtonList, confirm, nameInput);
            }
        };

        // add radio buttons
        for (int i = 0; i < Character.values().length; i++) {
            Character c = Character.get(i);

            JRadioButton rButton = new JRadioButton(c.toString(), false);
            // use the index as the button's action command
            rButton.setActionCommand(String.valueOf(i));
            rButton.addActionListener(al);
            radioButtonGroup.add(rButton);
            rButtonList.add(rButton);
            radioButtonsPanel.add(rButton);
            radioButtonsPanel.add(Box.createRigidArea(new Dimension(15, 15)));
        }

        // the middle panel to hold radio buttons and card display
        JPanel midPanel = new JPanel();
        midPanel.setLayout(new BoxLayout(midPanel, BoxLayout.X_AXIS));
        midPanel.add(radioButtonsPanel);
        midPanel.add(Box.createRigidArea(new Dimension(15, 20)));
        midPanel.add(cardDisplay);

        JPanel playerSetupPane = new JPanel();
        playerSetupPane.setLayout(new BoxLayout(playerSetupPane, BoxLayout.Y_AXIS));
        playerSetupPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JLabel text = new JLabel("What's your name and your character?");
        text.setAlignmentX(Component.CENTER_ALIGNMENT);
        JPanel textPane = new JPanel();
        textPane.setLayout(new BoxLayout(textPane, BoxLayout.Y_AXIS));
        textPane.setAlignmentX(Component.CENTER_ALIGNMENT);
        textPane.add(text);
        textPane.add(Box.createRigidArea(new Dimension(15, 20)));
        textPane.add(nameInput);

        // what "Next" button will do
        confirm.addActionListener(e -> {
            String name = nameInput.getText();
            // clear name input field
            nameInput.setText("");

            if (selectedCharacters.size() == 0) {
                // No you can't "previous" any more
                cancel.setEnabled(true);
            }

            for (JRadioButton b : rButtonList) {
                if (b.isSelected()) {
                    int buttonIndex = Integer.parseInt(b.getActionCommand());
                    // update these memory collections
                    selectedCharacters.put(Character.get(buttonIndex), name);
                    characterOder.add(buttonIndex);

                    if (confirm.getText().equals("Game On!")) {
                        // dispose this dialog
                        PlayerSetupDialog.this.dispose();
                        // join players
                        for (Character c : selectedCharacters.keySet()) {
                            parent.joinPlayer(c, selectedCharacters.get(c));
                        }
                        // start the game
                        parent.startGame();
                    }

                    b.setSelected(false);
                    b.setEnabled(false);
                    confirm.setEnabled(false);

                    if (selectedCharacters.size() == parent.getNumPlayers() - 1) {
                        confirm.setText("Game On!");
                    }
                    break;
                }
            }
        });

        // what "Previous" button will do
        cancel.addActionListener(e -> {
            // clear the name input
            nameInput.setText("");
            // update memory collections
            int canceledIndex = characterOder.remove(characterOder.size() - 1);
            selectedCharacters.remove(Character.get(canceledIndex));

            for (JRadioButton button : rButtonList) {
                if (Integer.parseInt(button.getActionCommand()) == canceledIndex) {
                    button.setEnabled(true);
                }
            }

            if (selectedCharacters.size() == 0) {
                cancel.setEnabled(false);
            } else if (selectedCharacters.size() < parent.getNumPlayers() - 1) {
                confirm.setText("Next");
            }
        });

        // put stuff together
        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.X_AXIS));
        buttonPane.setAlignmentX(Component.CENTER_ALIGNMENT);

        buttonPane.add(cancel);
        buttonPane.add(Box.createRigidArea(new Dimension(30, 20)));
        buttonPane.add(confirm);

        playerSetupPane.add(textPane);
        playerSetupPane.add(Box.createRigidArea(new Dimension(15, 15)));
        playerSetupPane.add(midPanel);
        playerSetupPane.add(Box.createRigidArea(new Dimension(15, 15)));
        playerSetupPane.add(buttonPane);

        // pack and show
        this.add(playerSetupPane);
        this.setModal(true);
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.setResizable(false);
        this.pack();
        this.setLocationRelativeTo(parent);
        this.setVisible(true);
    }

    /**
     * This helper method check whether the "Next" button can be unlocked. It can be
     * unlocked only when a name has been inputed, and the name inputed is not duplicate
     * with previous ones, and a different character is selected. Otherwise the "Next"
     * button would be disabled.
     * 
     * @param selectedCharacters
     *            --- a map used to maintain a [character = player name] pair
     * @param rButtonList
     *            --- a list of radio button group
     * @param confirm
     *            --- the "Next" button
     * @param nameInput
     *            --- the text field for inputting player names
     */
    private void tryUnlockNext(Map<Character, String> selectedCharacters,
            List<JRadioButton> rButtonList, JButton confirm, JTextField nameInput) {

        // three booleans to represent three critical conditions
        boolean hasName = false;
        boolean nameUnique = true;
        boolean radioSelected = false;

        String name = nameInput.getText();
        if (name != null && !name.equals("")) {
            hasName = true;
            for (String s : selectedCharacters.values()) {
                if (s.equals(name)) {
                    nameUnique = false;
                    break;
                }
            }
        }

        for (JRadioButton button : rButtonList) {
            if (button.isSelected() && button.isEnabled()) {
                radioSelected = true;
                break;
            }
        }

        // all of them have to be true to unlock the next chapter ** sorry button
        if (hasName && radioSelected && nameUnique) {
            confirm.setEnabled(true);
        } else {
            confirm.setEnabled(false);
        }
    }
}
