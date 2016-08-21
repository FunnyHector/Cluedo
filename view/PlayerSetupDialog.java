package view;

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
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import card.Character;
import ui.GUIClient;

public class PlayerSetupDialog extends JDialog {

    public static final Dimension PROFILE_DIMENSION = new Dimension(
            PlayerPanelCanvas.PROFILE_IMG[0].getIconWidth(),
            PlayerPanelCanvas.PROFILE_IMG[0].getIconHeight());

    public PlayerSetupDialog(GUIClient parent, Window windowForComponent, String string) {
        super(windowForComponent, string);

        List<Integer> selectedCharacters = new ArrayList<>();
        List<JRadioButton> rButtonList = new ArrayList<>();

        @SuppressWarnings("serial")
        JPanel cardDisplay = new JPanel() {
            protected void paintComponent(Graphics g) {
                for (JRadioButton b : rButtonList) {
                    if (b.isSelected()) {
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

        // radio buttons
        ButtonGroup radioButtonGroup = new ButtonGroup();
        JPanel radioButtonsPanel = new JPanel();
        radioButtonsPanel.setLayout(new BoxLayout(radioButtonsPanel, BoxLayout.Y_AXIS));

        // a listener for radiobuttons
        ActionListener al = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cardDisplay.repaint();
                if (!confirm.isEnabled()) {
                    confirm.setEnabled(true);
                }
            }
        };

        // add radio buttons
        for (int i = 0; i < Character.size(); i++) {
            Character c = Character.get(i);

            JRadioButton rButton = new JRadioButton(c.toString(), false);
            rButton.setActionCommand(String.valueOf(i));
            rButton.addActionListener(al);
            radioButtonGroup.add(rButton);
            rButtonList.add(rButton);
            radioButtonsPanel.add(rButton);
            radioButtonsPanel.add(Box.createRigidArea(new Dimension(15, 15)));

            if (selectedCharacters.contains(i)) {
                rButton.setEnabled(false);
            }
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

        JLabel text = new JLabel("Select Character:");
        JPanel textPane = new JPanel();
        textPane.setLayout(new BoxLayout(textPane, BoxLayout.X_AXIS));
        textPane.setAlignmentX(Component.CENTER_ALIGNMENT);
        textPane.add(text);

        confirm.addActionListener(e -> {

            if (selectedCharacters.size() == 0) {
                cancel.setEnabled(true);
            }

            for (JRadioButton b : rButtonList) {
                if (b.isSelected()) {
                    selectedCharacters.add(Integer.parseInt(b.getActionCommand()));

                    if (confirm.getText().equals("Start")) {
                        PlayerSetupDialog.this.dispose();

                        // join players
                        for (Integer i : selectedCharacters) {
                            parent.joinPlayer(Character.get(i));
                        }

                        // start the game
                        parent.setPlayerMoveFirst();
                        parent.creatSolutionAndDealCards();
                        parent.startGame();
                    }

                    b.setSelected(false);
                    b.setEnabled(false);
                    confirm.setEnabled(false);

                    if (selectedCharacters.size() == parent.getNumPlayers() - 1) {
                        confirm.setText("Start");
                    }
                    break;
                }
            }
        });

        cancel.addActionListener(e -> {
            int i = selectedCharacters.remove(selectedCharacters.size() - 1);
            if (selectedCharacters.size() == 0) {
                cancel.setEnabled(false);
            } else if (selectedCharacters.size() < parent.getNumPlayers() - 1) {
                confirm.setText("Next");
            }

            for (JRadioButton b : rButtonList) {
                if (Integer.parseInt(b.getActionCommand()) == i) {
                    b.setEnabled(true);
                    if (!confirm.isEnabled()) {
                        confirm.setEnabled(true);
                    }
                    break;
                }
            }
        });

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

        this.add(playerSetupPane);
        this.setModal(true);
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.setResizable(false);
        this.pack();
        this.setLocationRelativeTo(parent);
        this.setVisible(true);
    }
}
