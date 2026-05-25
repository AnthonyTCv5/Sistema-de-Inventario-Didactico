package ui;

import controller.InventarioController;
import model.Producto;
import ui.components.MetricCard;
import ui.components.RoundedPanel;
import ui.components.StyledTable;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.ArrayList;

public class PanelDashboard extends JPanel {

    private InventarioController controller;

    public PanelDashboard(InventarioController controller) {
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

        JLabel titulo = new JLabel("Dashboard");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titulo.setForeground(new Color(20, 20, 20));

        // Alertas en topbar
        ArrayList<Producto> stockBajo = controller.getProductosEnStockBajo();
        JPanel topbarDerecha = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        topbarDerecha.setBackground(Color.WHITE);

        if (!stockBajo.isEmpty()) {
            RoundedPanel badgeAlertas = new RoundedPanel(40, new Color(239, 68, 68));
            badgeAlertas.setBackground(new Color(254, 226, 226));
            badgeAlertas.setLayout(new FlowLayout(FlowLayout.CENTER, 8, 4));

            JLabel lblAlerta = new JLabel("⚠ " + stockBajo.size() + " alerta(s) de stock");
            lblAlerta.setFont(new Font("Segoe UI", Font.BOLD, 12));
            lblAlerta.setForeground(new Color(185, 28, 28));
            badgeAlertas.add(lblAlerta);
            topbarDerecha.add(badgeAlertas);
        }

        topbar.add(titulo, BorderLayout.WEST);
        topbar.add(topbarDerecha, BorderLayout.EAST);
        return topbar;
    }

    // ==================== CONTENIDO ====================
    private JPanel crearContenido() {
        JPanel contenido = new JPanel();
        contenido.setBackground(new Color(245, 245, 245));
        contenido.setLayout(new BoxLayout(contenido, BoxLayout.Y_AXIS));
        contenido.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        contenido.add(crearTarjetas());
        contenido.add(Box.createRigidArea(new Dimension(0, 20)));
        contenido.add(crearTablaProductos());

        return contenido;
    }

    // ==================== TARJETAS DE MÉTRICAS ====================
    private JPanel crearTarjetas() {
        JPanel panel = new JPanel(new GridLayout(1, 4, 15, 0));
        panel.setBackground(new Color(245, 245, 245));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 115));

        ArrayList<Producto> productos = controller.getProductos();

        int totalStock = 0;
        for (Producto p : productos) {
            totalStock += p.getCantidadActual();
        }

        // Proyección promedio de todos los productos
        double proyeccion = 0;
        if (!productos.isEmpty()) {
            for (Producto p : productos) {
                ArrayList<Integer> historial = controller.getHistorialStock(p.getId());
                if (historial.size() >= 2) {
                    proyeccion += controller.getLimiteTendencia(p.getId(), 3);
                } else {
                    proyeccion += p.getCantidadActual();
                }
            }
            proyeccion = proyeccion / productos.size();
        }

        String proyeccionTexto = productos.isEmpty() ? "—" : String.format("%.0f", proyeccion) + " uds";

        panel.add(new MetricCard(
                "Productos",
                String.valueOf(productos.size()),
                "registrados",
                new Color(59, 130, 246)
        ));
        panel.add(new MetricCard(
                "Stock total",
                String.valueOf(totalStock),
                "unidades",
                new Color(16, 185, 129)
        ));
        panel.add(new MetricCard(
                "Proyección estimada",
                proyeccionTexto,
                "promedio en 3 movimientos",
                new Color(139, 92, 246)
        ));
        panel.add(new MetricCard(
                "Movimientos",
                String.valueOf(totalMovimientos()),
                "registrados",
                new Color(245, 158, 11)
        ));

        return panel;
    }

    // ==================== TABLA DE PRODUCTOS ====================
    private JPanel crearTablaProductos() {
        RoundedPanel panel = new RoundedPanel(12, new Color(220, 220, 220));
        panel.setBackground(Color.WHITE);
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

        JLabel titulo = new JLabel("Estado de productos");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 14));
        titulo.setForeground(new Color(20, 20, 20));
        titulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 12, 0));

        String[] columnas = {"ID", "Producto", "Stock actual", "Stock mínimo", "Estado"};
        StyledTable tabla = new StyledTable(columnas);

        tabla.setAnchoColumna(0, 40);
        tabla.setAnchoColumna(1, 180);
        tabla.setAnchoColumna(2, 100);
        tabla.setAnchoColumna(3, 100);
        tabla.setAnchoColumna(4, 120);

        // Renderer con colores para columna Estado
        tabla.getTabla().getColumnModel().getColumn(4).setCellRenderer(
                new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object value,
                    boolean isSelected, boolean hasFocus, int row, int col) {
                super.getTableCellRendererComponent(t, value, isSelected, hasFocus, row, col);
                setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
                if (!isSelected) {
                    String val = value != null ? value.toString() : "";
                    setBackground(row % 2 == 0 ? Color.WHITE : new Color(249, 250, 251));
                    if (val.contains("Crítico")) {
                        setForeground(new Color(185, 28, 28));
                        setFont(new Font("Segoe UI", Font.BOLD, 13));
                    } else if (val.contains("Bajo")) {
                        setForeground(new Color(180, 100, 0));
                        setFont(new Font("Segoe UI", Font.PLAIN, 13));
                    } else {
                        setForeground(new Color(21, 128, 61));
                        setFont(new Font("Segoe UI", Font.PLAIN, 13));
                    }
                }
                return this;
            }
        }
        );

        for (Producto p : controller.getProductos()) {
            String estado = calcularEstado(p);
            tabla.agregarFila(new Object[]{
                p.getId(),
                p.getNombre(),
                p.getCantidadActual(),
                p.getStockMinimo(),
                estado
            });
        }

        panel.add(titulo, BorderLayout.NORTH);
        panel.add(tabla, BorderLayout.CENTER);

        return panel;
    }

    // ==================== UTILS ====================
    private int totalMovimientos() {
        int total = 0;
        for (Producto p : controller.getProductos()) {
            total += controller.getMovimientosDeProducto(p.getId()).size();
        }
        return total;
    }

    public void refresh() {
        removeAll();
        initComponents();
        revalidate();
        repaint();
    }

    private String calcularEstado(Producto p) {
        if (p.getCantidadActual() <= 0 || p.getCantidadActual() <= p.getStockMinimo() / 2) {
            return "✕ Crítico";
        } else if (p.getCantidadActual() <= p.getStockMinimo()) {
            return "⚠ Bajo";
        } else {
            return "✓ Normal";
        }
    }

}
