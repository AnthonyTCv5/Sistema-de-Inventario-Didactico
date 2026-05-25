package ui;

import controller.InventarioController;
import model.Producto;
import ui.components.RoundedPanel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class PanelAlertas extends JPanel {

    private InventarioController controller;
    private JPanel panelTarjetas;

    public PanelAlertas(InventarioController controller) {
        this.controller = controller;
        setBackground(new Color(245, 245, 245));
        setLayout(new BorderLayout());
        initComponents();
    }

    private void initComponents() {
        add(crearTopbar(), BorderLayout.NORTH);
        add(crearContenido(), BorderLayout.CENTER);
    }

    // ==================== TOPBAR ====================
    private JPanel crearTopbar() {
        JPanel topbar = new JPanel(new BorderLayout());
        topbar.setBackground(Color.WHITE);
        topbar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 220, 220)),
                BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));

        JLabel titulo = new JLabel("Alertas de reposición");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titulo.setForeground(new Color(20, 20, 20));

        topbar.add(titulo, BorderLayout.WEST);
        return topbar;
    }

    // ==================== CONTENIDO ====================
    private JPanel crearContenido() {
        JScrollPane scroll = new JScrollPane(crearPanelTarjetas());
        scroll.setBorder(null);
        scroll.getViewport().setBackground(new Color(245, 245, 245));
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        JPanel contenido = new JPanel(new BorderLayout());
        contenido.setBackground(new Color(245, 245, 245));
        contenido.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        contenido.add(scroll, BorderLayout.CENTER);
        return contenido;
    }

    private JPanel crearPanelTarjetas() {
        panelTarjetas = new JPanel();
        panelTarjetas.setBackground(new Color(245, 245, 245));
        panelTarjetas.setLayout(new BoxLayout(panelTarjetas, BoxLayout.Y_AXIS));
        actualizarAlertas();
        return panelTarjetas;
    }

    // ==================== ACTUALIZAR ALERTAS ====================
    public void actualizarAlertas() {
        panelTarjetas.removeAll();

        ArrayList<Producto> enStockBajo = controller.getProductosEnStockBajo();

        if (enStockBajo.isEmpty()) {
            panelTarjetas.add(crearEstadoVacio());
        } else {
            JLabel subtitulo = new JLabel(enStockBajo.size() + " producto(s) requieren atención");
            subtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            subtitulo.setForeground(new Color(120, 120, 120));
            subtitulo.setHorizontalAlignment(SwingConstants.RIGHT);
            subtitulo.setAlignmentX(Component.RIGHT_ALIGNMENT);
            subtitulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
            panelTarjetas.add(subtitulo);

            for (Producto p : enStockBajo) {
                panelTarjetas.add(crearTarjetaAlerta(p));
                panelTarjetas.add(Box.createRigidArea(new Dimension(0, 12)));
            }
        }

        panelTarjetas.revalidate();
        panelTarjetas.repaint();
    }

    // ==================== TARJETA DE ALERTA ====================
    private RoundedPanel crearTarjetaAlerta(Producto p) {
        String estado = calcularEstado(p);

        Color colorEstado = estado.contains("Crítico") ? new Color(185, 28, 28) : new Color(180, 100, 0);

        RoundedPanel tarjeta = new RoundedPanel(12, colorEstado);
        tarjeta.setBackground(Color.WHITE);
        tarjeta.setLayout(new BorderLayout());
        tarjeta.setMaximumSize(new Dimension(Integer.MAX_VALUE, 140));
        tarjeta.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        // Indicador lateral
        JPanel indicador = new JPanel();
        indicador.setBackground(colorEstado);
        indicador.setPreferredSize(new Dimension(5, 0));

        // Cuerpo principal
        JPanel cuerpo = new JPanel(new BorderLayout());
        cuerpo.setBackground(Color.WHITE);
        cuerpo.setBorder(BorderFactory.createEmptyBorder(14, 16, 14, 16));

        // Parte superior — nombre y descripción
        JPanel infoPanel = new JPanel();
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));

        JLabel nombreLabel = new JLabel(p.getNombre());
        nombreLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        nombreLabel.setForeground(new Color(20, 20, 20));

        JLabel descripcionLabel = new JLabel(p.getDescripcion());
        descripcionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        descripcionLabel.setForeground(new Color(140, 140, 140));

        infoPanel.add(nombreLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 3)));
        infoPanel.add(descripcionLabel);

        // Parte inferior — badges
        JPanel badgesPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        badgesPanel.setBackground(Color.WHITE);
        badgesPanel.setBorder(BorderFactory.createEmptyBorder(45, -5, 0, 0));

        Color badgeBg = estado.contains("Crítico") ? new Color(254, 226, 226) : new Color(254, 243, 199);
        Color badgeText = estado.contains("Crítico") ? new Color(185, 28, 28) : new Color(146, 64, 14);

        badgesPanel.add(crearBadge("Stock actual: " + p.getCantidadActual(), badgeBg, badgeText));
        badgesPanel.add(crearBadge("Mínimo: " + p.getStockMinimo(), new Color(243, 244, 246), new Color(75, 85, 99)));
        badgesPanel.add(crearBadge(estado, badgeBg, badgeText));

        JPanel izquierda = new JPanel(new BorderLayout());
        izquierda.setBackground(Color.WHITE);
        izquierda.add(infoPanel, BorderLayout.NORTH);
        izquierda.add(badgesPanel, BorderLayout.CENTER);

        // Parte derecha — tendencia
        JPanel derecha = new JPanel();
        derecha.setBackground(Color.WHITE);
        derecha.setLayout(new BoxLayout(derecha, BoxLayout.Y_AXIS));

        ArrayList<Integer> historial = controller.getHistorialStock(p.getId());
        String tendenciaTexto;
        String sugerenciaTexto;
        Color sugerenciaColor;

        if (historial.size() >= 2) {
            double tendencia = controller.getLimiteTendencia(p.getId(), 3);
            double velocidad = controller.getDerivadaPromedio(p.getId());
            tendenciaTexto = "Tendencia: " + String.format("%.0f", tendencia) + " uds en 3 mov";
            if (velocidad < 0) {
                sugerenciaTexto = "⚠ Reponer pronto";
                sugerenciaColor = new Color(185, 28, 28);
            } else {
                sugerenciaTexto = "→ Monitorear";
                sugerenciaColor = new Color(180, 100, 0);
            }
        } else {
            tendenciaTexto = "Sin suficientes datos";
            sugerenciaTexto = "Registra movimientos";
            sugerenciaColor = new Color(140, 140, 140);
        }

        JLabel lblTendencia = new JLabel(tendenciaTexto);
        lblTendencia.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblTendencia.setForeground(new Color(120, 120, 120));
        lblTendencia.setAlignmentX(Component.RIGHT_ALIGNMENT);

        JLabel lblSugerencia = new JLabel(sugerenciaTexto);
        lblSugerencia.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblSugerencia.setForeground(sugerenciaColor);
        lblSugerencia.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
        lblSugerencia.setAlignmentX(Component.RIGHT_ALIGNMENT);

        derecha.add(Box.createVerticalGlue());
        derecha.add(lblTendencia);
        derecha.add(lblSugerencia);
        derecha.add(Box.createVerticalGlue());

        cuerpo.add(izquierda, BorderLayout.CENTER);
        cuerpo.add(derecha, BorderLayout.EAST);

        tarjeta.add(indicador, BorderLayout.WEST);
        tarjeta.add(cuerpo, BorderLayout.CENTER);

        return tarjeta;
    }

    // ==================== ESTADO ====================
    private String calcularEstado(Producto p) {
        if (p.getCantidadActual() <= 0 || p.getCantidadActual() <= p.getStockMinimo() / 2) {
            return "✕ Crítico";
        } else if (p.getCantidadActual() <= p.getStockMinimo()) {
            return "⚠ Bajo";
        } else {
            return "✓ Normal";
        }
    }

    // ==================== ESTADO VACÍO ====================
    private RoundedPanel crearEstadoVacio() {
        RoundedPanel panel = new RoundedPanel(12, new Color(220, 220, 220));
        panel.setBackground(Color.WHITE);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 130));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 20, 30, 20));

        JLabel icono = new JLabel("✓");
        icono.setFont(new Font("Segoe UI", Font.BOLD, 32));
        icono.setForeground(new Color(16, 185, 129));
        icono.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel mensaje = new JLabel("Todos los productos tienen stock suficiente");
        mensaje.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        mensaje.setForeground(new Color(100, 100, 100));
        mensaje.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel submensaje = new JLabel("No hay alertas activas en este momento");
        submensaje.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        submensaje.setForeground(new Color(160, 160, 160));
        submensaje.setAlignmentX(Component.CENTER_ALIGNMENT);
        submensaje.setBorder(BorderFactory.createEmptyBorder(4, 0, 0, 0));

        panel.add(icono);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(mensaje);
        panel.add(submensaje);

        return panel;
    }

    // ==================== BADGE ====================
    private JLabel crearBadge(String texto, Color bgColor, Color textColor) {
        JLabel badge = new JLabel(texto);
        badge.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        badge.setForeground(textColor);
        badge.setBackground(bgColor);
        badge.setOpaque(true);
        badge.setBorder(BorderFactory.createEmptyBorder(3, 8, 3, 8));
        return badge;
    }
}
