package ui;

import controller.InventarioController;
import model.Producto;
import ui.components.RoundedPanel;
import ui.components.StyledTable;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.ArrayList;

public class PanelTasaCambio extends JPanel {

    private InventarioController controller;
    private JComboBox<Producto> comboProductos;
    private GraficaDerivadas graficaDerivadas;
    private StyledTable tabla;
    private JLabel lblPromedio;
    private JLabel lblInterpretacion;

    public PanelTasaCambio(InventarioController controller) {
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

        JLabel titulo = new JLabel("Tasa de cambio del stock");
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

        // Panel izquierdo — gráfica
        RoundedPanel panelGrafica = new RoundedPanel(12, new Color(220, 220, 220));
        panelGrafica.setBackground(Color.WHITE);
        panelGrafica.setLayout(new BorderLayout());
        panelGrafica.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel tituloGrafica = new JLabel("Variación por movimiento — I'(t)");
        tituloGrafica.setFont(new Font("Segoe UI", Font.BOLD, 13));
        tituloGrafica.setForeground(new Color(80, 80, 80));
        tituloGrafica.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        graficaDerivadas = new GraficaDerivadas();

        // Leyenda
        JPanel leyenda = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 6));
        leyenda.setBackground(Color.WHITE);
        leyenda.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(240, 240, 240)));

        JLabel leyPos = new JLabel("● Stock subiendo");
        leyPos.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        leyPos.setForeground(new Color(16, 185, 129));

        JLabel leyNeg = new JLabel("● Stock bajando");
        leyNeg.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        leyNeg.setForeground(new Color(239, 68, 68));

        leyenda.add(leyPos);
        leyenda.add(leyNeg);

        panelGrafica.add(tituloGrafica, BorderLayout.NORTH);
        panelGrafica.add(graficaDerivadas, BorderLayout.CENTER);
        panelGrafica.add(leyenda, BorderLayout.SOUTH);

        // Panel derecho — tabla + resumen
        JPanel panelDerecha = new JPanel(new BorderLayout());
        panelDerecha.setBackground(new Color(245, 245, 245));
        panelDerecha.setPreferredSize(new Dimension(300, 0));
        panelDerecha.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 0));

        panelDerecha.add(crearTabla(),   BorderLayout.CENTER);
        panelDerecha.add(crearResumen(), BorderLayout.SOUTH);

        contenido.add(panelGrafica,  BorderLayout.CENTER);
        contenido.add(panelDerecha,  BorderLayout.EAST);

        return contenido;
    }

    // ==================== TABLA ====================

    private RoundedPanel crearTabla() {
        RoundedPanel panel = new RoundedPanel(12, new Color(220, 220, 220));
        panel.setBackground(Color.WHITE);
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel tituloTabla = new JLabel("Detalle por movimiento");
        tituloTabla.setFont(new Font("Segoe UI", Font.BOLD, 13));
        tituloTabla.setForeground(new Color(20, 20, 20));
        tituloTabla.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        String[] columnas = {"t", "I(t)", "I'(t)", "Estado"};
        tabla = new StyledTable(columnas);
        tabla.setAnchoColumna(0, 50);
        tabla.setAnchoColumna(1, 60);
        tabla.setAnchoColumna(2, 60);
        tabla.setAnchoColumna(3, 110);

        // Renderer columna Estado
        tabla.getTabla().getColumnModel().getColumn(3).setCellRenderer(
            new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable t, Object value,
                        boolean isSelected, boolean hasFocus, int row, int col) {
                    super.getTableCellRendererComponent(t, value, isSelected, hasFocus, row, col);
                    setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));
                    if (!isSelected) {
                        String val = value != null ? value.toString() : "";
                        setBackground(row % 2 == 0 ? Color.WHITE : new Color(249, 250, 251));
                        if (val.contains("Subiendo")) {
                            setForeground(new Color(21, 128, 61));
                            setFont(new Font("Segoe UI", Font.BOLD, 12));
                        } else if (val.contains("Bajando")) {
                            setForeground(new Color(185, 28, 28));
                            setFont(new Font("Segoe UI", Font.BOLD, 12));
                        } else {
                            setForeground(new Color(120, 120, 120));
                            setFont(new Font("Segoe UI", Font.PLAIN, 12));
                        }
                    }
                    return this;
                }
            }
        );

        panel.add(tituloTabla, BorderLayout.NORTH);
        panel.add(tabla,       BorderLayout.CENTER);
        return panel;
    }

    // ==================== RESUMEN ====================

    private RoundedPanel crearResumen() {
        RoundedPanel panel = new RoundedPanel(12, new Color(220, 220, 220));
        panel.setBackground(Color.WHITE);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(14, 16, 14, 16));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));

        JLabel tituloResumen = new JLabel("Tasa de cambio promedio");
        tituloResumen.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tituloResumen.setForeground(new Color(120, 120, 120));

        lblPromedio = new JLabel("—");
        lblPromedio.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblPromedio.setForeground(new Color(59, 130, 246));

        lblInterpretacion = new JLabel("—");
        lblInterpretacion.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblInterpretacion.setForeground(new Color(140, 140, 140));

        panel.add(tituloResumen);
        panel.add(Box.createRigidArea(new Dimension(0, 6)));
        panel.add(lblPromedio);
        panel.add(Box.createRigidArea(new Dimension(0, 4)));
        panel.add(lblInterpretacion);

        return panel;
    }

    // ==================== ACTUALIZAR ANÁLISIS ====================

    public void actualizarAnalisis() {
        Producto seleccionado = (Producto) comboProductos.getSelectedItem();
        tabla.limpiar();

        if (seleccionado == null) {
            graficaDerivadas.setDatos(new double[0], new ArrayList<>());
            lblPromedio.setText("—");
            lblInterpretacion.setText("—");
            return;
        }

        int id = seleccionado.getId();
        ArrayList<Integer> historial = controller.getHistorialStock(id);

        if (historial.size() < 2) {
            graficaDerivadas.setDatos(new double[0], new ArrayList<>());
            lblPromedio.setText("Sin datos");
            lblInterpretacion.setText("Registra movimientos para ver el análisis");
            return;
        }

        double[] derivadas = controller.getDerivadas(id);
        graficaDerivadas.setDatos(derivadas, historial);

        // Llenar tabla
        for (int i = 0; i < historial.size(); i++) {
            String estado;
            if      (derivadas[i] > 0)  estado = "↑ Subiendo";
            else if (derivadas[i] < 0)  estado = "↓ Bajando";
            else                         estado = "→ Sin cambio";

            tabla.agregarFila(new Object[]{
                "t=" + i,
                historial.get(i),
                String.format("%.1f", derivadas[i]),
                estado
            });
        }

        // Resumen
        double promedio = controller.getDerivadaPromedio(id);
        String signo    = promedio >= 0 ? "+" : "";
        lblPromedio.setText(signo + String.format("%.1f", promedio) + " uds/mov");
        lblPromedio.setForeground(promedio >= 0 ? new Color(16, 185, 129) : new Color(239, 68, 68));

        if (promedio > 0) {
            lblInterpretacion.setText("El stock crece en promedio cada movimiento");
        } else if (promedio < 0) {
            lblInterpretacion.setText("El stock disminuye en promedio cada movimiento");
        } else {
            lblInterpretacion.setText("El stock se mantiene estable en promedio");
        }
    }

    public void refrescarProductos() {
        Producto actual = (Producto) comboProductos.getSelectedItem();
        comboProductos.removeAllItems();
        for (Producto p : controller.getProductos()) comboProductos.addItem(p);
        if (actual != null) comboProductos.setSelectedItem(actual);
        actualizarAnalisis();
    }
}