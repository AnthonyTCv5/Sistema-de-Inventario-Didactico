package ui;

import controller.InventarioController;
import model.Producto;
import ui.components.RoundedPanel;
import ui.components.StyledTable;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.ArrayList;

public class PanelProyeccion extends JPanel {

    private InventarioController controller;
    private JComboBox<Producto> comboProductos;
    private GraficaProyeccion graficaProyeccion;
    private StyledTable tabla;
    private JLabel lblTendenciaGeneral;
    private JLabel lblSubtendencia;

    private static final int PASOS_PROYECCION = 5;

    public PanelProyeccion(InventarioController controller) {
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

        JLabel titulo = new JLabel("Proyección del inventario");
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

        contenido.add(crearPanelGrafica(), BorderLayout.CENTER);
        contenido.add(crearPanelDerecha(), BorderLayout.EAST);

        return contenido;
    }

    // ==================== GRÁFICA ====================

    private RoundedPanel crearPanelGrafica() {
        RoundedPanel panel = new RoundedPanel(12, new Color(220, 220, 220));
        panel.setBackground(Color.WHITE);
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel tituloGrafica = new JLabel("Tendencia estimada — próximos " + PASOS_PROYECCION + " movimientos");
        tituloGrafica.setFont(new Font("Segoe UI", Font.BOLD, 13));
        tituloGrafica.setForeground(new Color(80, 80, 80));
        tituloGrafica.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        graficaProyeccion = new GraficaProyeccion();

        // Leyenda
        JPanel leyenda = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 6));
        leyenda.setBackground(Color.WHITE);
        leyenda.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(240, 240, 240)));

        JLabel leyReal = new JLabel("● Datos reales");
        leyReal.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        leyReal.setForeground(new Color(59, 130, 246));

        JLabel leyProy = new JLabel("● Proyección estimada");
        leyProy.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        leyProy.setForeground(new Color(245, 158, 11));

        leyenda.add(leyReal);
        leyenda.add(leyProy);

        panel.add(tituloGrafica,    BorderLayout.NORTH);
        panel.add(graficaProyeccion, BorderLayout.CENTER);
        panel.add(leyenda,           BorderLayout.SOUTH);

        return panel;
    }

    // ==================== PANEL DERECHO ====================

    private JPanel crearPanelDerecha() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(245, 245, 245));
        panel.setPreferredSize(new Dimension(360, 0));
        panel.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 0));

        panel.add(crearTabla(),   BorderLayout.CENTER);
        panel.add(crearResumen(), BorderLayout.SOUTH);

        return panel;
    }

    // ==================== TABLA ====================

    private RoundedPanel crearTabla() {
        RoundedPanel panel = new RoundedPanel(12, new Color(220, 220, 220));
        panel.setBackground(Color.WHITE);
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel tituloTabla = new JLabel("Valores proyectados");
        tituloTabla.setFont(new Font("Segoe UI", Font.BOLD, 13));
        tituloTabla.setForeground(new Color(20, 20, 20));
        tituloTabla.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        String[] columnas = {"Movimiento", "Stock estimado", "Variación"};
        tabla = new StyledTable(columnas);
        tabla.setAnchoColumna(0, 90);
        tabla.setAnchoColumna(1, 100);
        tabla.setAnchoColumna(2, 80);

        // Renderer columna Variación
        tabla.getTabla().getColumnModel().getColumn(2).setCellRenderer(
            new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable t, Object value,
                        boolean isSelected, boolean hasFocus, int row, int col) {
                    super.getTableCellRendererComponent(t, value, isSelected, hasFocus, row, col);
                    setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));
                    if (!isSelected) {
                        String val = value != null ? value.toString() : "";
                        setBackground(row % 2 == 0 ? Color.WHITE : new Color(249, 250, 251));
                        if (val.startsWith("+")) {
                            setForeground(new Color(21, 128, 61));
                            setFont(new Font("Segoe UI", Font.BOLD, 12));
                        } else if (val.startsWith("-")) {
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

        JLabel tituloResumen = new JLabel("Tendencia general");
        tituloResumen.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tituloResumen.setForeground(new Color(120, 120, 120));

        lblTendenciaGeneral = new JLabel("—");
        lblTendenciaGeneral.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTendenciaGeneral.setForeground(new Color(245, 158, 11));

        lblSubtendencia = new JLabel("—");
        lblSubtendencia.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblSubtendencia.setForeground(new Color(140, 140, 140));

        panel.add(tituloResumen);
        panel.add(Box.createRigidArea(new Dimension(0, 6)));
        panel.add(lblTendenciaGeneral);
        panel.add(Box.createRigidArea(new Dimension(0, 4)));
        panel.add(lblSubtendencia);

        return panel;
    }

    // ==================== ACTUALIZAR ANÁLISIS ====================

    public void actualizarAnalisis() {
        Producto seleccionado = (Producto) comboProductos.getSelectedItem();
        tabla.limpiar();

        if (seleccionado == null) {
            graficaProyeccion.setDatos(new ArrayList<>(), new ArrayList<>());
            lblTendenciaGeneral.setText("—");
            lblSubtendencia.setText("—");
            return;
        }

        int id = seleccionado.getId();
        ArrayList<Integer> historial = controller.getHistorialStock(id);

        if (historial.size() < 2) {
            graficaProyeccion.setDatos(new ArrayList<>(), new ArrayList<>());
            lblTendenciaGeneral.setText("Sin datos");
            lblSubtendencia.setText("Registra movimientos para proyectar");
            return;
        }

        // Calcular proyección para los próximos PASOS_PROYECCION movimientos
        ArrayList<Double> proyeccion = new ArrayList<>();
        for (int i = 1; i <= PASOS_PROYECCION; i++) {
            proyeccion.add(controller.getLimiteTendencia(id, i));
        }

        graficaProyeccion.setDatos(historial, proyeccion);

        // Llenar tabla — variación respecto al punto anterior
        double anterior = historial.get(historial.size() - 1);
        for (int i = 0; i < proyeccion.size(); i++) {
            double valor     = proyeccion.get(i);
            double variacion = valor - anterior;
            String signo     = variacion >= 0 ? "+" : "";
            String varTexto  = signo + String.format("%.1f", variacion);

            tabla.agregarFila(new Object[]{
                "t+" + (i + 1),
                String.format("%.0f", valor),
                varTexto
            });
            anterior = valor;
        }

        // Resumen tendencia general
        double tendenciaFinal = proyeccion.get(proyeccion.size() - 1);
        double stockActual    = historial.get(historial.size() - 1);
        double diferencia     = tendenciaFinal - stockActual;
        String signo          = diferencia >= 0 ? "+" : "";

        lblTendenciaGeneral.setText(signo + String.format("%.0f", diferencia) + " uds");
        lblTendenciaGeneral.setForeground(diferencia >= 0 ? new Color(16, 185, 129) : new Color(239, 68, 68));

        if (diferencia > 0) {
            lblSubtendencia.setText("El stock tenderá a aumentar");
        } else if (diferencia < 0) {
            lblSubtendencia.setText("El stock tenderá a disminuir");
        } else {
            lblSubtendencia.setText("El stock se mantendrá estable");
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