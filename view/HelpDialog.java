package view;

import static ui.GUIClient.loadImage;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Window;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ui.GUIClient;

/**
 * This class is a custom dialog for displaying a help.
 * 
 * @author Hector
 *
 */
@SuppressWarnings("serial")
public class HelpDialog extends JDialog {
    /**
     * Help images
     */
    private static final ImageIcon[] HELP_IMAGES = {
            new ImageIcon(loadImage("Help_1.png")),
            new ImageIcon(loadImage("Help_2.png")) };
    /**
     * The preferred dimension for help image
     */
    private static final Dimension IMAGE_DIMENSION = new Dimension(
            HELP_IMAGES[0].getIconWidth(), HELP_IMAGES[0].getIconHeight());
    /**
     * A CardLayout to manage the panel switch
     */
    private CardLayout cardLayout = new CardLayout();

    /**
     * Construct a dialog, let players choose character, weapon, location respectively,
     * and make the suggestion/accusation.
     * 
     * @param parent
     *            --- the Main GUI of this game
     * @param windowForComponent
     *            --- the owner component
     * @param title
     *            --- the tile of this dialog
     */
    public HelpDialog(GUIClient parent, Window windowForComponent, String title) {
        super(windowForComponent, title);

        JPanel mainPanel = new JPanel();

        // we use card layout
        mainPanel.setLayout(cardLayout);

        // add help images
        for (int i = 0; i < HELP_IMAGES.length; i++) {
            addSlides(mainPanel, HELP_IMAGES[i], i);
        }

        // and show the dialog
        this.add(mainPanel);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setModal(true);
        this.setResizable(false);
        this.pack();
        this.setLocationRelativeTo(parent);
        this.setVisible(true);
    }

    /**
     * This method adds an image into the CardLayout (as its card).
     * 
     * @param mainPanel
     *            --- the parent Panel with CardLayout
     * @param image
     *            --- the image
     * @param index
     *            --- the index of this image
     */
    private void addSlides(JPanel mainPanel, ImageIcon image, int index) {
        // main panel
        JPanel slidePanel = new JPanel();
        slidePanel.setLayout(new BorderLayout());
        // image area
        JLabel imageDisplay = new JLabel(image);
        imageDisplay.setPreferredSize(IMAGE_DIMENSION);
        imageDisplay.setVisible(true);
        // a button
        JButton confirm = new JButton("Next");
        confirm.addActionListener(e -> {
            if (confirm.getText().equals("Next")) {
                cardLayout.next(mainPanel);
            } else {
                HelpDialog.this.dispose();
            }
        });

        // need to adapt when it's the last slide
        if (index == HELP_IMAGES.length - 1) {
            confirm.setText("Got it!");
        }

        // put stuff together
        slidePanel.add(imageDisplay, BorderLayout.NORTH);
        slidePanel.add(confirm, BorderLayout.SOUTH);

        // finally, add this panel into CardLayout
        mainPanel.add(slidePanel);
    }
}