package view;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import ui.GUIClient;

/**
 * This class is a custom menu for the main Frame in Cluedo GUI.
 * 
 * @author Hector
 *
 */
@SuppressWarnings("serial")
public class CustomMenu extends JMenuBar {

    /**
     * An array holding all Strings to make the menu
     */
    private static final String[] MENU_STRINGS = { "Menu", "New Game", "Easy Mode",
            "Help", "Cluedo Manual", "Exit" };

    /**
     * A URL address to open a Cluedo manual
     */
    private static final String HELP_URL = "www.hasbro.com/common/instruct/Clue_(2002).pdf";

    /**
     * a check box menu to enable/disable easy mode
     */
    private JCheckBoxMenuItem easyMode;

    /**
     * Construct a Menu for Cluedo game
     * 
     * @param parent
     *            --- the Main GUI of this game
     */
    public CustomMenu(GUIClient parent) {

        int i = 0;
        JMenu jMenu = new JMenu(MENU_STRINGS[i++]);

        // new game
        JMenuItem newGame = new JMenuItem(MENU_STRINGS[i++]);
        newGame.addActionListener(e -> {
            if (parent.isGameRunning()) {
                int choice = JOptionPane.showConfirmDialog(parent,
                        "Game is still running, are you sure to start a new game?",
                        "Confirm new game?", JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE);
                if (choice == JOptionPane.YES_OPTION) {
                    parent.setupNumPlayers();
                }
            } else {
                parent.setupNumPlayers();
            }
        });

        // a checkbox menu to enable easy mode
        easyMode = new JCheckBoxMenuItem(MENU_STRINGS[i++]);
        easyMode.setEnabled(false);
        easyMode.addChangeListener(e -> {
            parent.setEasyMode(((JCheckBoxMenuItem) e.getSource()).isSelected());
            parent.update();
        });

        // a Pop up help dialog
        JMenuItem help = new JMenuItem(MENU_STRINGS[i++]);
        help.addActionListener(e -> parent.popUpHelp());

        // a link to open a browser for Cluedo Manual
        JMenuItem manual = new JMenuItem(MENU_STRINGS[i++]);
        manual.addActionListener(e -> {
            if (Desktop.isDesktopSupported()) {
                try {
                    URI uri = URI.create(HELP_URL);
                    Desktop desktop = Desktop.getDesktop();
                    if (desktop.isSupported(Desktop.Action.BROWSE)) {
                        desktop.browse(uri);
                    }
                } catch (IOException e1) {
                    System.err.println("No browser supported");
                }
            }
        });

        // exit
        JMenuItem exit = new JMenuItem(MENU_STRINGS[i++]);
        exit.addActionListener(e -> {

            if (parent.isGameRunning()) {
                int choice = JOptionPane.showConfirmDialog(parent,
                        "Game is still running, are you sure to quit?",
                        "Confirm quiting?", JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE);
                if (choice == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            } else {
                System.exit(0);
            }
        });

        jMenu.add(newGame);
        jMenu.add(easyMode);
        jMenu.add(help);
        jMenu.add(manual);
        jMenu.add(exit);
        this.add(jMenu);
    }

    /**
     * This method enables easy mode menuItem in menu.
     */
    public void enableEasyModeMenu() {
        easyMode.setEnabled(true);
    }
}