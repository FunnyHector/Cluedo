package view;

import static ui.GUIClient.loadImage;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import card.Location;
import game.Player;
import tile.Entrance;
import ui.GUIClient;

public class ExitRoomDialog extends JDialog {

    public static final ImageIcon[] BALL_ROOM_EXITS = {
            new ImageIcon(loadImage("Exit_Ball_Room_1.png")),
            new ImageIcon(loadImage("Exit_Ball_Room_2.png")),
            new ImageIcon(loadImage("Exit_Ball_Room_3.png")),
            new ImageIcon(loadImage("Exit_Ball_Room_4.png")) };

    public static final ImageIcon[] BILLARD_ROOM_EXITS = {
            new ImageIcon(loadImage("Exit_Billard_Room_1.png")),
            new ImageIcon(loadImage("Exit_Billard_Room_2.png")) };

    public static final ImageIcon[] DINING_ROOM_EXITS = {
            new ImageIcon(loadImage("Exit_Dining_Room_1.png")),
            new ImageIcon(loadImage("Exit_Dining_Room_2.png")) };

    public static final ImageIcon[] HALL_EXITS = {
            new ImageIcon(loadImage("Exit_Hall_1.png")),
            new ImageIcon(loadImage("Exit_Hall_2.png")),
            new ImageIcon(loadImage("Exit_Hall_3.png")) };

    public static final ImageIcon[] LIBRARY_EXITS = {
            new ImageIcon(loadImage("Exit_Library_1.png")),
            new ImageIcon(loadImage("Exit_Library_2.png")) };

    public static final Dimension EXIT_DISPLAY_DIMENSION = new Dimension(320, 288);

    private ImageIcon[] images;
    private Entrance selectedExit;

    public ExitRoomDialog(GUIClient parent, Window windowForComponent, String string,
            Location room) {

        super(windowForComponent, string);

        images = null;
        selectedExit = null;

        List<Entrance> exits = parent.getBoard()
                .lookForExit(parent.getPlayerByCharacter(parent.getCurrentPlayer()));

        // choose images to use
        if (room == Location.Ball_room) {
            images = BALL_ROOM_EXITS;
        } else if (room == Location.Billard_Room) {
            images = BILLARD_ROOM_EXITS;
        } else if (room == Location.Dining_Room) {
            images = DINING_ROOM_EXITS;
        } else if (room == Location.Hall) {
            images = HALL_EXITS;
        } else if (room == Location.Library) {
            images = LIBRARY_EXITS;
        }

        JRadioButton[] rButtonList = new JRadioButton[exits.size()];

        // panel used to display exits
        @SuppressWarnings("serial")
        JPanel exitDisplay = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                for (JRadioButton b : rButtonList) {
                    if (b.isSelected()) {

                        Image image = images[Integer.parseInt(b.getActionCommand())]
                                .getImage();

                        int x = ((int) EXIT_DISPLAY_DIMENSION.getWidth()
                                - image.getWidth(this)) / 2;

                        int y = ((int) EXIT_DISPLAY_DIMENSION.getHeight()
                                - image.getHeight(this)) / 2;

                        g.drawImage(image, x, y, this);
                        break;
                    }
                }
            }
        };
        exitDisplay.setPreferredSize(EXIT_DISPLAY_DIMENSION);
        // this prevents a bug where the component won't be drawn until it is resized.
        exitDisplay.setVisible(true);

        // button at bottom
        JButton confirm = new JButton("Exit Room");
        confirm.setEnabled(false);

        // radio buttons
        ButtonGroup radioButtonGroup = new ButtonGroup();
        JPanel radioButtonsPanel = new JPanel();
        radioButtonsPanel.setLayout(new BoxLayout(radioButtonsPanel, BoxLayout.Y_AXIS));

        // a listener for radiobuttons
        ActionListener al = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                exitDisplay.repaint();
                if (!confirm.isEnabled()) {
                    confirm.setEnabled(true);
                }
            }
        };

        // add radio buttons
        for (int i = 0; i < exits.size(); i++) {
            JRadioButton rButton = new JRadioButton(room.toString() + " Exit " + (i + 1),
                    false);
            rButton.setActionCommand(String.valueOf(i));
            rButton.addActionListener(al);
            radioButtonGroup.add(rButton);
            rButtonList[i] = rButton;
            radioButtonsPanel.add(rButton);
            radioButtonsPanel.add(Box.createRigidArea(new Dimension(15, 15)));

            boolean isBlocking = false;
            for (Player existingPlayer : parent.getAllPlayers()) {
                if (exits.get(i).equals(existingPlayer.getPosition())) {
                    isBlocking = true;
                    break;
                }
            }

            if (isBlocking) {
                rButton.setEnabled(false);
            }
        }

        // the middle panel to hold radio buttons and card display
        JPanel midPanel = new JPanel();
        midPanel.setLayout(new BoxLayout(midPanel, BoxLayout.X_AXIS));
        midPanel.add(radioButtonsPanel);
        midPanel.add(Box.createRigidArea(new Dimension(15, 20)));
        midPanel.add(exitDisplay);

        JPanel dialogPane = new JPanel();
        dialogPane.setLayout(new BoxLayout(dialogPane, BoxLayout.Y_AXIS));
        dialogPane.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel text = new JLabel("Select an exit:");
        JPanel textPane = new JPanel();
        textPane.setLayout(new BoxLayout(textPane, BoxLayout.X_AXIS));
        textPane.setAlignmentX(Component.CENTER_ALIGNMENT);
        textPane.add(text);

        confirm.addActionListener(e -> {

            for (JRadioButton b : rButtonList) {
                if (b.isSelected()) {
                    selectedExit = exits.get(Integer.parseInt(b.getActionCommand()));
                    ExitRoomDialog.this.dispose();
                    parent.movePlayer(parent.getCurrentPlayer(), selectedExit);
                    break;
                }
            }
        });

        dialogPane.add(textPane);
        dialogPane.add(Box.createRigidArea(new Dimension(15, 15)));
        dialogPane.add(midPanel);
        dialogPane.add(Box.createRigidArea(new Dimension(15, 15)));
        dialogPane.add(confirm);

        this.add(dialogPane);
        this.setModal(true);
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.setResizable(false);
        this.pack();
        this.setLocationRelativeTo(parent);
        this.setVisible(true);
    }

}
