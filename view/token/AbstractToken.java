package view.token;

import static ui.GUIClient.loadImage;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Paint;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToolTip;

public abstract class AbstractToken extends JLabel {

    public static final ImageIcon CROSS_ICON = new ImageIcon(
            loadImage("NoBrainer_Cross.png"));
    public static final ImageIcon QUESTION_ICON = new ImageIcon(
            loadImage("NoBrainer_Question.png"));

    private int x;
    private int y;
    private boolean isNoBrainer;
    private boolean isKnown;

    // private CustomTooltip customTooltip;

    public AbstractToken(ImageIcon img, int x, int y) {
        super(img);
        this.x = x;
        this.y = y;
        this.isNoBrainer = false;
        this.isKnown = false;

    }

    public void setNoBrainer(boolean isNoBrainer) {
        this.isNoBrainer = isNoBrainer;
    }

    public void setIsKnown(boolean isKnown) {
        this.isKnown = isKnown;
    }

    protected void moveTo(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public JToolTip createToolTip() {
        if (isNoBrainer) {
            // if (customTooltip == null) {
            JToolTip customTooltip = new CustomTooltip();
            customTooltip.setComponent(this);
            // }
            return customTooltip;
        } else {
            JToolTip tip = new JToolTip();
            tip.setComponent(this);
            return tip;
        }
    }

    private class CustomTooltip extends JToolTip {

        private JLabel textLabel;
        private JLabel iconLabel;
        private JPanel panel;

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

}
