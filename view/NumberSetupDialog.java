package view;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Window;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ui.GUIClient;

/**
 * This class is a custom dialog for players to choose how many players and how many dices
 * are used in game.
 * 
 * @author Hector
 *
 */
@SuppressWarnings("serial")
public class NumberSetupDialog extends JDialog {

    /**
     * Construct a dialog to choose how many players and how many dices are used in game.
     * 
     * @param parent
     *            --- the Main GUI of this game
     * @param windowForComponent
     *            --- the owner component
     * @param string
     *            --- the tile of this dialog
     */
    public NumberSetupDialog(GUIClient parent, Window windowForComponent, String string) {
        super(windowForComponent, string);
        // initialise the main panel, and set a vertical BoxLayout
        JPanel numSetup = new JPanel();
        numSetup.setLayout(new BoxLayout(numSetup, BoxLayout.Y_AXIS));
        numSetup.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // ===== first row, choose how many players with a JComboBox =====

        JPanel firstRow = new JPanel();
        firstRow.setLayout(new BoxLayout(firstRow, BoxLayout.X_AXIS));
        JLabel label1 = new JLabel("Number of Players: ");
        firstRow.add(label1);
        firstRow.add(Box.createRigidArea(new Dimension(20, 15)));
        Integer[] numOptions_1 = new Integer[] { 3, 4, 5, 6 };
        JComboBox<Integer> comboBox_1 = new JComboBox<Integer>(numOptions_1);
        comboBox_1.setSelectedIndex(3);
        firstRow.add(comboBox_1);
        firstRow.setAlignmentX(Component.CENTER_ALIGNMENT);

        // ===== second row, choose how many dicess with a JComboBox =====

        JPanel secondRow = new JPanel();
        secondRow.setLayout(new BoxLayout(secondRow, BoxLayout.X_AXIS));
        JLabel label2 = new JLabel("Number of Dices:     ");
        secondRow.add(label2);
        secondRow.add(Box.createRigidArea(new Dimension(20, 15)));
        Integer[] numOptions_2 = new Integer[] { 1, 2, 3 };
        JComboBox<Integer> comboBox_2 = new JComboBox<Integer>(numOptions_2);
        comboBox_2.setSelectedIndex(1);
        secondRow.add(comboBox_2);
        secondRow.setAlignmentX(Component.CENTER_ALIGNMENT);


        // ===== last row, OK and Cancel buttons =====

        JPanel thirdRow = new JPanel();
        thirdRow.setLayout(new BoxLayout(thirdRow, BoxLayout.X_AXIS));
        JButton confirm = new JButton("OK");
        confirm.addActionListener(e -> {
            // if the player click OK, then we have a new game!
            int numPlayers = comboBox_1.getItemAt(comboBox_1.getSelectedIndex());
            int numDices = comboBox_2.getItemAt(comboBox_2.getSelectedIndex());
            NumberSetupDialog.this.dispose();
            parent.createNewGame(numPlayers, numDices);
            parent.setupPlayers();
        });
        JButton cancel = new JButton("Cancel");
        cancel.addActionListener(e -> NumberSetupDialog.this.dispose());
        
        thirdRow.add(cancel);
        thirdRow.add(Box.createRigidArea(new Dimension(20, 15)));
        thirdRow.add(confirm);
        thirdRow.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        numSetup.add(firstRow);
        numSetup.add(Box.createRigidArea(new Dimension(15, 20)));
        numSetup.add(secondRow);
        numSetup.add(Box.createRigidArea(new Dimension(15, 20)));
        numSetup.add(thirdRow);

        // pack and show it
        this.add(numSetup);
        this.setModal(true);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setResizable(false);
        this.pack();
        this.setLocationRelativeTo(parent);
        this.setVisible(true);
    }

}
