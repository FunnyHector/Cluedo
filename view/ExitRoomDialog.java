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

/**
 * This class is a custom dialog for player to choose which door he / she can take to exit
 * current room.
 * 
 * @author Hector
 *
 */
@SuppressWarnings("serial")
public class ExitRoomDialog extends JDialog {

    /**
     * an array holding images to display exit
     */
    private ImageIcon[] images;
    /**
     * which exit is selected by the player
     */
    private Entrance selectedExit;

    /**
     * Construct a dialog, let the player choose through which door to exit current room.
     * 
     * @param parent
     *            --- the Main GUI of this game
     * @param windowForComponent
     *            --- the owner component
     * @param string
     *            --- the tile of this dialog
     * @param room
     *            --- current room
     */
    public ExitRoomDialog(GUIClient parent, Window windowForComponent, String string,
            Location room) {

        super(windowForComponent, string);
        images = null;
        selectedExit = null;
        List<Entrance> exits = parent.getBoard()
                .lookForExit(parent.getPlayerByCharacter(parent.getCurrentPlayer()));

        // choose which image array to use
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
        JPanel exitDisplay = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                // draw images to show each exits accordingly
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

        // a listener for radio buttons
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

            // disable exits blocked by other players
            for (Player existingPlayer : parent.getAllPlayers()) {
                if (exits.get(i).equals(existingPlayer.getPosition())) {
                    rButton.setEnabled(false);
                    break;
                }
            }
        }

        // the middle panel to hold radio buttons and card display
        JPanel midPanel = new JPanel();
        midPanel.setLayout(new BoxLayout(midPanel, BoxLayout.X_AXIS));
        midPanel.add(radioButtonsPanel);
        midPanel.add(Box.createRigidArea(new Dimension(15, 20)));
        midPanel.add(exitDisplay);

        // the main panel
        JPanel dialogPane = new JPanel();
        dialogPane.setLayout(new BoxLayout(dialogPane, BoxLayout.Y_AXIS));
        dialogPane.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel text = new JLabel("Select an exit:");
        JPanel textPane = new JPanel();
        textPane.setLayout(new BoxLayout(textPane, BoxLayout.X_AXIS));
        textPane.setAlignmentX(Component.CENTER_ALIGNMENT);
        textPane.add(text);

        // what confirm button does
        confirm.addActionListener(e -> {
            for (JRadioButton b : rButtonList) {
                if (b.isSelected()) {
                    selectedExit = exits.get(Integer.parseInt(b.getActionCommand()));
                    ExitRoomDialog.this.dispose();

                    if (selectedExit != null) {
                        parent.movePlayer(parent.getCurrentPlayer(), selectedExit);
                    }
                    break;
                }
            }
        });

        // put stuff together
        dialogPane.add(textPane);
        dialogPane.add(Box.createRigidArea(new Dimension(15, 15)));
        dialogPane.add(midPanel);
        dialogPane.add(Box.createRigidArea(new Dimension(15, 15)));
        dialogPane.add(confirm);

        // pack and show
        this.add(dialogPane);
        this.setModal(true);
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.setResizable(false);
        this.pack();
        this.setLocationRelativeTo(parent);
        this.setVisible(true);
    }

    /**
     * Images for displaying exits of ball room
     */
    private static final ImageIcon[] BALL_ROOM_EXITS = {
            new ImageIcon(loadImage("Exit_Ball_Room_1.png")),
            new ImageIcon(loadImage("Exit_Ball_Room_2.png")),
            new ImageIcon(loadImage("Exit_Ball_Room_3.png")),
            new ImageIcon(loadImage("Exit_Ball_Room_4.png")) };
    /**
     * Images for displaying exits of billard room
     */
    private static final ImageIcon[] BILLARD_ROOM_EXITS = {
            new ImageIcon(loadImage("Exit_Billard_Room_1.png")),
            new ImageIcon(loadImage("Exit_Billard_Room_2.png")) };
    /**
     * Images for displaying exits of dining room
     */
    private static final ImageIcon[] DINING_ROOM_EXITS = {
            new ImageIcon(loadImage("Exit_Dining_Room_1.png")),
            new ImageIcon(loadImage("Exit_Dining_Room_2.png")) };
    /**
     * Images for displaying exits of hall
     */
    private static final ImageIcon[] HALL_EXITS = {
            new ImageIcon(loadImage("Exit_Hall_1.png")),
            new ImageIcon(loadImage("Exit_Hall_2.png")),
            new ImageIcon(loadImage("Exit_Hall_3.png")) };
    /**
     * Images for displaying exits of library
     */
    private static final ImageIcon[] LIBRARY_EXITS = {
            new ImageIcon(loadImage("Exit_Library_1.png")),
            new ImageIcon(loadImage("Exit_Library_2.png")) };

    /**
     * a preferred dimension for displaying exit images
     */
    private static final Dimension EXIT_DISPLAY_DIMENSION = new Dimension(320, 288);

}
