package view;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.URI;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import ui.GUIClient;

public class CustomMenu extends JMenuBar {

    public CustomMenu(GUIClient parent) {

        JMenu menuMenu = new JMenu("Menu");
        this.add(menuMenu);

        // new game
        JMenuItem menuMenu_NewGame = new JMenuItem("New Game");
        menuMenu_NewGame.addActionListener(e -> parent.setupNumPlayers());
        menuMenu.add(menuMenu_NewGame);

        // a checkbox menu to enable no brainer mode
        JCheckBoxMenuItem menuMenu_NoBrainerMode = new JCheckBoxMenuItem(
                "No Brainer Mode");
        menuMenu_NoBrainerMode.addChangeListener(e -> parent
                .setNobrainerMode(((JCheckBoxMenuItem) e.getSource()).isSelected()));
        menuMenu.add(menuMenu_NoBrainerMode);

        // a help menu to open Cluedo Rule
        JMenuItem menuMenu_Help = new JMenuItem("Help");
        menuMenu_Help.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (Desktop.isDesktopSupported()) {
                    try {
                        URI uri = URI
                                .create("www.hasbro.com/common/instruct/Clue_(2002).pdf");
                        Desktop desktop = java.awt.Desktop.getDesktop();
                        if (desktop.isSupported(Desktop.Action.BROWSE)) {
                            desktop.browse(uri);
                        }
                    } catch (IOException e1) {
                        System.err.println("No browser supported");
                    }
                }
            }
        });
        menuMenu.add(menuMenu_Help);

        // exit
        JMenuItem menuMenu_Exit = new JMenuItem("Exit");
        menuMenu_Exit.addActionListener(e -> System.exit(0));
        menuMenu.add(menuMenu_Exit);

        // actions menus
        JMenu menuActions = new JMenu("Actions");
        this.add(menuActions);
        JMenu menuActions_MoveTo = new JMenu("Move to:");
        menuActions.add(menuActions_MoveTo);

        // move to north
        JMenuItem menuActions_MoveTo_North = new JMenuItem("North");
        menuActions_MoveTo_North.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, 0));
        menuActions_MoveTo_North.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub

            }
        });
        menuActions_MoveTo.add(menuActions_MoveTo_North);

        // move to east
        JMenuItem menuActions_MoveTo_East = new JMenuItem("East");
        menuActions_MoveTo_East.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0));
        menuActions_MoveTo_East.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub

            }
        });
        menuActions_MoveTo.add(menuActions_MoveTo_East);

        // move to south
        JMenuItem menuActions_MoveTo_South = new JMenuItem("South");
        menuActions_MoveTo_South.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0));
        menuActions_MoveTo_South.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub

            }
        });
        menuActions_MoveTo.add(menuActions_MoveTo_South);

        // move to west
        JMenuItem menuActions_MoveTo_West = new JMenuItem("West");
        menuActions_MoveTo_West.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0));
        menuActions_MoveTo_West.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub

            }
        });
        menuActions_MoveTo.add(menuActions_MoveTo_West);

        // make suggestions
        JMenuItem menuActions_MakeSuggestion = new JMenuItem("Make Suggestion");
        menuActions_MakeSuggestion.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub

            }
        });
        menuActions.add(menuActions_MakeSuggestion);

        // make accusation
        JMenuItem menuActions_MakeAccusation = new JMenuItem("Make Accusation");
        menuActions_MakeAccusation.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub

            }
        });
        menuActions.add(menuActions_MakeAccusation);

        // end turn
        JMenuItem menuActions_EndTurn = new JMenuItem("End Turn");
        menuActions_EndTurn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub

            }
        });
        menuActions.add(menuActions_EndTurn);

    }

}
