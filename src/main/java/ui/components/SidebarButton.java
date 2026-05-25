package ui.components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SidebarButton extends JButton {

    private boolean activo;
    private Color colorActivo    = new Color(59, 130, 246);
    private Color colorHover     = new Color(35, 35, 35);
    private Color colorNormal    = new Color(15, 15, 15);
    private Color colorTexto     = new Color(180, 180, 180);
    private Color colorTextoActivo = Color.WHITE;

    public SidebarButton(String texto) {
        super(texto);
        this.activo = false;
        configurar();
    }

    private void configurar() {
        setFont(new Font("Segoe UI", Font.PLAIN, 14));
        setForeground(colorTexto);
        setBackground(colorNormal);
        setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 15));
        setHorizontalAlignment(SwingConstants.LEFT);
        setMaximumSize(new Dimension(200, 46));
        setMinimumSize(new Dimension(200, 46));
        setPreferredSize(new Dimension(200, 46));
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setFocusPainted(false);
        setContentAreaFilled(false);
        setOpaque(true);

        addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                if (!activo) setBackground(colorHover);
            }
            public void mouseExited(MouseEvent e) {
                if (!activo) setBackground(colorNormal);
            }
        });
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
        if (activo) {
            setBackground(colorActivo);
            setForeground(colorTextoActivo);
            setFont(new Font("Segoe UI", Font.BOLD, 14));
        } else {
            setBackground(colorNormal);
            setForeground(colorTexto);
            setFont(new Font("Segoe UI", Font.PLAIN, 14));
        }
        repaint();
    }

    public boolean isActivo() { return activo; }
}