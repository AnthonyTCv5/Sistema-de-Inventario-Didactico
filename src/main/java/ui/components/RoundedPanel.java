package ui.components;

import javax.swing.*;
import java.awt.*;

public class RoundedPanel extends JPanel {

    private int radio;
    private Color colorBorde;
    private boolean tieneBorde;

    public RoundedPanel(int radio) {
        this.radio       = radio;
        this.tieneBorde  = false;
        setOpaque(false);
    }

    public RoundedPanel(int radio, Color colorBorde) {
        this.radio       = radio;
        this.colorBorde  = colorBorde;
        this.tieneBorde  = true;
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), radio, radio);

        if (tieneBorde) {
            g2.setColor(colorBorde);
            g2.setStroke(new BasicStroke(1f));
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radio, radio);
        }

        g2.dispose();
    }

    @Override
    protected void paintChildren(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        super.paintChildren(g2);
        g2.dispose();
    }

    public void setRadio(int radio) {
        this.radio = radio;
        repaint();
    }
}