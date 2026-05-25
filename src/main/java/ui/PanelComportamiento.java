package ui;

import controller.InventarioController;
import model.Producto;
import ui.components.MetricCard;
import ui.components.RoundedPanel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class PanelComportamiento extends JPanel {

    private InventarioController controller;
    private JComboBox<Producto> comboProductos;
    private GraficaInventario grafica;
    private MetricCard cardAlerta;
    private MetricCard cardStockAlto;
    private MetricCard cardStockBajo;
    private JLabel lblLeyendaMax;
    private JLabel lblLeyendaMin;

    public PanelComportamiento(InventarioController controller) {
        this.controller = controller;
        setBackground(new Color(245, 245, 245));
        setLayout(new BorderLayout());
        initComponents();
    }

    private void initComponents() {
        add(crearTopbar(),    BorderLayout.NORTH);
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

        JLabel titulo = new JLabel("Comportamiento del inventario");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titulo.setForeground(new Color(20, 20, 20));

        JPanel selectorPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        selectorPanel.setBackground(Color.WHITE);

        JLabel labelSelector = new JLabel("Producto:");
        labelSelector.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        labelSelector.setForeground(new Color(100, 100, 100));

        comboProductos = new JComboBox<>();
        comboProductos.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        comboProductos.setPreferredSize(new Dimension(200, 32));
        for (Producto p : controller.getProductos()) comboProductos.addItem(p);
        comboProductos.addActionListener(evt -> actualizarAnalisis());

        selectorPanel.add(labelSelector);
        selectorPanel.add(comboProductos);

        topbar.add(titulo,        BorderLayout.WEST);
        topbar.add(selectorPanel, BorderLayout.EAST);
        return topbar;
    }

    // ==================== CONTENIDO ====================

    private JPanel crearContenido() {
        JPanel contenido = new JPanel(new BorderLayout());
        contenido.setBackground(new Color(245, 245, 245));
        contenido.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        contenido.add(crearPanelGrafica(),  BorderLayout.CENTER);
        contenido.add(crearPanelMetricas(), BorderLayout.SOUTH);

        return contenido;
    }

    // ==================== GRÁFICA ====================

    private RoundedPanel crearPanelGrafica() {
        RoundedPanel panel = new RoundedPanel(12, new Color(220, 220, 220));
        panel.setBackground(Color.WHITE);
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel tituloGrafica = new JLabel("Evolución del stock en el tiempo");
        tituloGrafica.setFont(new Font("Segoe UI", Font.BOLD, 13));
        tituloGrafica.setForeground(new Color(80, 80, 80));
        tituloGrafica.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        grafica = new GraficaInventario();

        JPanel leyenda = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 6));
        leyenda.setBackground(Color.WHITE);
        leyenda.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(240, 240, 240)));

        lblLeyendaMax = new JLabel("● Stock más alto");
        lblLeyendaMax.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblLeyendaMax.setForeground(new Color(16, 185, 129));

        lblLeyendaMin = new JLabel("● Stock más bajo");
        lblLeyendaMin.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblLeyendaMin.setForeground(new Color(239, 68, 68));

        leyenda.add(lblLeyendaMax);
        leyenda.add(lblLeyendaMin);

        panel.add(tituloGrafica, BorderLayout.NORTH);
        panel.add(grafica,       BorderLayout.CENTER);
        panel.add(leyenda,       BorderLayout.SOUTH);

        return panel;
    }

    // ==================== MÉTRICAS ====================

    private JPanel crearPanelMetricas() {
        JPanel panel = new JPanel(new GridLayout(1, 3, 12, 0));
        panel.setBackground(new Color(245, 245, 245));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        panel.setPreferredSize(new Dimension(0, 120));

        cardAlerta    = new MetricCard("Alerta de reposición", "—", "estado actual", new Color(239, 68,  68));
        cardStockAlto = new MetricCard("Stock más alto",       "—", "registrado",    new Color(16,  185, 129));
        cardStockBajo = new MetricCard("Stock más bajo",       "—", "registrado",    new Color(245, 158, 11));

        panel.add(cardAlerta);
        panel.add(cardStockAlto);
        panel.add(cardStockBajo);

        actualizarAnalisis();
        return panel;
    }

    // ==================== ACTUALIZAR ANÁLISIS ====================

    public void actualizarAnalisis() {
        Producto seleccionado = (Producto) comboProductos.getSelectedItem();

        if (seleccionado == null) {
            grafica.setDatos(new ArrayList<>(), 0, 0);
            lblLeyendaMax.setText("● Stock más alto");
            lblLeyendaMin.setText("● Stock más bajo");
            cardAlerta.setValor("—");
            cardStockAlto.setValor("—");
            cardStockBajo.setValor("—");
            return;
        }

        int id = seleccionado.getId();
        ArrayList<Integer> historial = controller.getHistorialStock(id);

        if (historial.size() < 2) {
            grafica.setDatos(new ArrayList<>(), 0, 0);
            lblLeyendaMax.setText("● Stock más alto");
            lblLeyendaMin.setText("● Stock más bajo");
            cardAlerta.setValor("Sin datos");
            cardStockAlto.setValor("Sin datos");
            cardStockBajo.setValor("Sin datos");
            return;
        }

        int tMinimo = controller.getPuntoMinimoStock(id);
        int tMaximo = controller.getPuntoMaximoStock(id);

        grafica.setDatos(historial, tMinimo, tMaximo);

        lblLeyendaMax.setText("● Stock más alto (t=" + tMaximo + ")");
        lblLeyendaMin.setText("● Stock más bajo (t=" + tMinimo + ")");

        boolean alerta    = controller.hayRiesgoDesabastecimiento(id);
        int     stockAlto = historial.get(tMaximo);
        int     stockBajo = historial.get(tMinimo);

        cardAlerta.setValor(
            alerta ? "⚠ Reponer" : "✓ Estable",
            alerta ? new Color(239, 68, 68) : new Color(16, 185, 129)
        );
        cardStockAlto.setValor(stockAlto + " uds");
        cardStockBajo.setValor(stockBajo + " uds");
    }

    public void refrescarProductos() {
        Producto actual = (Producto) comboProductos.getSelectedItem();
        comboProductos.removeAllItems();
        for (Producto p : controller.getProductos()) comboProductos.addItem(p);
        if (actual != null) comboProductos.setSelectedItem(actual);
        actualizarAnalisis();
    }
}