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

public class CustomMenu extends JMenuBar {

    private static final String[] MENU_STRINGS = { "Menu", "New Game", "No Brainer Mode",
            "Help / Cluedo Rules", "Exit" };
    private static final String HELP_URL = "www.hasbro.com/common/instruct/Clue_(2002).pdf";

    private JCheckBoxMenuItem menuMenu_NoBrainerMode;

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
        menuMenu_NoBrainerMode = new JCheckBoxMenuItem(MENU_STRINGS[i++]);
        menuMenu_NoBrainerMode.setEnabled(false);
        menuMenu_NoBrainerMode.addChangeListener(e -> {
            parent.setNobrainerMode(((JCheckBoxMenuItem) e.getSource()).isSelected());
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
        menuMenu.add(menuMenu_NoBrainerMode);
        menuMenu.add(menuMenu_Help);
        menuMenu.add(menuMenu_Exit);
    }

    public void enableNoBrainerMenu() {
        menuMenu_NoBrainerMode.setEnabled(true);
    }
}