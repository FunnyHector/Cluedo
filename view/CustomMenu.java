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
            "Help / Cluedo Rules", "Exit" };

    /**
     * A URL address to open a Cluedo manual
     */
    private static final String HELP_URL = "www.hasbro.com/common/instruct/Clue_(2002).pdf";

    /**
     * a check box menu to enable/disable no brainer mode
     */
    private JCheckBoxMenuItem menuMenu_EasyMode;

    /**
     * Construct a Menu for Cluedo game
     * 
     * @param parent
     *            --- the Main GUI of this game
     */
    public CustomMenu(GUIClient parent) {

        int i = 0;
        JMenu menuMenu = new JMenu(MENU_STRINGS[i++]);
        this.add(menuMenu);

        // new game
        JMenuItem menuMenu_NewGame = new JMenuItem(MENU_STRINGS[i++]);
        menuMenu_NewGame.addActionListener(e -> {
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

        // a checkbox menu to enable no brainer mode
        menuMenu_EasyMode = new JCheckBoxMenuItem(MENU_STRINGS[i++]);
        menuMenu_EasyMode.setEnabled(false);
        menuMenu_EasyMode.addChangeListener(e -> {
            parent.setEasyMode(((JCheckBoxMenuItem) e.getSource()).isSelected());
            parent.update();
        });

        // a help link to open Cluedo Rule
        JMenuItem menuMenu_Help = new JMenuItem(MENU_STRINGS[i++]);
        menuMenu_Help.addActionListener(e -> {
            if (Desktop.isDesktopSupported()) {
                try {
                    URI uri = URI.create(HELP_URL);
                    Desktop desktop = java.awt.Desktop.getDesktop();
                    if (desktop.isSupported(Desktop.Action.BROWSE)) {
                        desktop.browse(uri);
                    }
                } catch (IOException e1) {
                    System.err.println("No browser supported");
                }
            }
        });

        // exit
        JMenuItem menuMenu_Exit = new JMenuItem(MENU_STRINGS[i++]);
        menuMenu_Exit.addActionListener(e -> {

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

        menuMenu.add(menuMenu_NewGame);
        menuMenu.add(menuMenu_EasyMode);
        menuMenu.add(menuMenu_Help);
        menuMenu.add(menuMenu_Exit);
    }

    /**
     * This method enables easy mode menuItem in menu.
     */
    public void enableEasyModeMenu() {
        menuMenu_EasyMode.setEnabled(true);
    }
}