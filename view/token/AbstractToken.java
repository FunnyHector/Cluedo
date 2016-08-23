package view.token;

import static ui.GUIClient.loadImage;
import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToolTip;

import tile.RoomTile;

/**
 * This abstract class represents a token on board. It remembers where to draw tokens in
 * room (outside-room positions are not remembered here). Also, for GUI mode, each token
 * has a custom tooltip to show a better-looking tooltip in no-brainer mode.
 * 
 * @author Hector
 *
 */
@SuppressWarnings("serial")
public abstract class AbstractToken extends JLabel {

    /**
     * in which room
     */
    private RoomTile roomTile;
    /**
     * a boolean to decide which kind of tooltip to show
     */
    private boolean isEasyMode;
    /**
     * a boolean to decide which icon (known or unknown) to show in custom tooltip
     */
    private boolean isKnown;

    /**
     * Construct a token with an image and a given room tile.
     * 
     * @param img
     *            --- the image used to draw this token on board
     * @param roomTile
     *            --- which room tile is this token currently placed
     */
    public AbstractToken(ImageIcon img, RoomTile roomTile) {
        super(img);
        this.roomTile = roomTile;
        // It will show the normal tooltip on default
        this.isEasyMode = false;
        this.isKnown = false;
    }

    /**
     * This method set the token in specified room
     * 
     * @param roomTile
     *            --- the room to set in.
     */
    public void setRoomTile(RoomTile roomTile) {
        if (this.roomTile != null) {
            this.roomTile.setHoldingToken(false);
        }
        roomTile.setHoldingToken(true);
        this.roomTile = roomTile;
    }

    /**
     * which room is it in? Note that a character token may return null if it is not in a
     * room
     * 
     * @return --- the room it is located
     */
    public RoomTile getRoomTile() {
        return roomTile;
    }

    /**
     * Set tooltip display mode according to the on-off status of easy mode
     * 
     * @param isEasyMode
     *            --- whether it is Easy mode
     */
    public void setEasyMode(boolean isEasyMode) {
        this.isEasyMode = isEasyMode;
    }

    /**
     * Set the card (held by this token) is known or not to current player.
     * 
     * @param isKnown
     *            --- is it known?
     */
    public void setIsKnown(boolean isKnown) {
        this.isKnown = isKnown;
    }

    @Override
    public JToolTip createToolTip() {
        if (isEasyMode) {
            // use the custom tooltip
            JToolTip customTooltip = new CustomTooltip();
            customTooltip.setComponent(this);
            return customTooltip;
        } else {
            // use default tooltip
            JToolTip tip = new JToolTip();
            tip.setComponent(this);
            return tip;
        }
    }

    /**
     * A custom tooltip for easy mode
     * 
     * @author Hector
     */
    private class CustomTooltip extends JToolTip {

        // generated serial UID
        private static final long serialVersionUID = 6868701625611852907L;
        // text display
        private JLabel textLabel;
        // an icon to show whether this token is known to current player
        private JLabel iconLabel;
        // panel to hold things up
        private JPanel panel;

        /**
         * Construct a custom tooltip
         */
        public CustomTooltip() {
            super();
            textLabel = new JLabel(getToolTipText());
            textLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            ImageIcon icon;
            if (isKnown) {
                icon = CROSS_ICON;
            } else {
                icon = QUESTION_ICON;
            }
            iconLabel = new JLabel(icon);
            iconLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            panel = new JPanel(new BorderLayout(10, 10));
            panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            panel.add(BorderLayout.CENTER, textLabel);
            panel.add(BorderLayout.SOUTH, iconLabel);
            setLayout(new BorderLayout());
            add(panel);
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(300, 100);
        }

        @Override
        public void setTipText(String tipText) {
            if (tipText != null && !tipText.isEmpty()) {
                textLabel.setText(tipText);
            } else {
                super.setTipText(tipText);
            }
        }

    }

    /**
     * a cross icon to represents this token is known, i.e. not involved in crime
     */
    public static final ImageIcon CROSS_ICON = new ImageIcon(
            loadImage("EasyMode_Cross.png"));
    /**
     * a question mark icon to represents this token is unknown
     */
    public static final ImageIcon QUESTION_ICON = new ImageIcon(
            loadImage("EasyMode_Question.png"));
}
