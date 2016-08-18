package view;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ui.GUIClient;

public class NumberSetupDialog extends JDialog {

    /**
     * TODO Generated serial version UID
     */

    public NumberSetupDialog(GUIClient parent, Window windowForComponent, String string) {
        super(windowForComponent, string);
        this.setModal(true);

        JPanel numSetup = new JPanel();
        numSetup.setLayout(new BoxLayout(numSetup, BoxLayout.Y_AXIS));
        numSetup.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // ======== first row ========

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
        numSetup.add(firstRow);

        numSetup.add(Box.createRigidArea(new Dimension(15, 20)));

        // ======== second row ========

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
        numSetup.add(secondRow);

        numSetup.add(Box.createRigidArea(new Dimension(15, 20)));

        // ======== OK and Cancel buttons ========

        JPanel thirdRow = new JPanel();
        thirdRow.setLayout(new BoxLayout(thirdRow, BoxLayout.X_AXIS));
        JButton confirm = new JButton("OK");
        confirm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int numPlayers = comboBox_1.getItemAt(comboBox_1.getSelectedIndex());
                int numDices = comboBox_2.getItemAt(comboBox_2.getSelectedIndex());
                NumberSetupDialog.this.dispose();
                parent.setNumPlayers(numPlayers);
                parent.setNumDices(numDices);
                parent.setupPlayers();
            }
        });
        thirdRow.add(confirm);
        thirdRow.add(Box.createRigidArea(new Dimension(20, 15)));
        JButton cancel = new JButton("Cancel");
        thirdRow.add(cancel);
        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                NumberSetupDialog.this.dispose();
            }
        });
        thirdRow.setAlignmentX(Component.CENTER_ALIGNMENT);
        numSetup.add(thirdRow);

        // show it
        this.add(numSetup);

        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setResizable(false);
        this.pack();
        this.setLocationRelativeTo(parent);
        this.setVisible(true);
    }

}
