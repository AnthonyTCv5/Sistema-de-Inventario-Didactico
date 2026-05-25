package ui.components;

import javax.swing.*;
import java.awt.*;

public class MetricCard extends RoundedPanel {

    private JLabel lblTitulo;
    private JLabel lblValor;
    private JLabel lblSubtitulo;
    private Color colorValor;

    public MetricCard(String titulo, String valor, String subtitulo, Color colorValor) {
        super(12, new Color(220, 220, 220));
        this.colorValor = colorValor;
        setBackground(Color.WHITE);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(16, 18, 16, 18));
        initComponents(titulo, valor, subtitulo);
    }

    private void initComponents(String titulo, String valor, String subtitulo) {
        lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblTitulo.setForeground(new Color(120, 120, 120));

        lblValor = new JLabel(valor);
        lblValor.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblValor.setForeground(colorValor);

        lblSubtitulo = new JLabel(subtitulo);
        lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblSubtitulo.setForeground(new Color(150, 150, 150));

        add(lblTitulo);
        add(Box.createRigidArea(new Dimension(0, 6)));
        add(lblValor);
        add(Box.createRigidArea(new Dimension(0, 4)));
        add(lblSubtitulo);
    }

    // Actualiza el valor dinámicamente
    public void setValor(String valor) {
        lblValor.setText(valor);
        repaint();
    }

    // Actualiza el valor y su color dinámicamente
    public void setValor(String valor, Color color) {
        lblValor.setText(valor);
        lblValor.setForeground(color);
        repaint();
    }

    public void setSubtitulo(String subtitulo) {
        lblSubtitulo.setText(subtitulo);
        repaint();
    }
}