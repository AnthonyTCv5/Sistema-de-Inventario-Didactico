package ui;

import controller.InventarioController;
import model.Producto;
import ui.components.RoundedPanel;
import ui.components.StyledTable;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.ArrayList;

public class PanelProductos extends JPanel {

    private InventarioController controller;
    private StyledTable tabla;
    private Runnable onProductoAgregado;

    public PanelProductos(InventarioController controller, Runnable onProductoAgregado) {
        this.controller = controller;
        this.onProductoAgregado = onProductoAgregado;
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

        JLabel titulo = new JLabel("Productos");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titulo.setForeground(new Color(20, 20, 20));

        JButton btnEliminar = crearBotonAccion("✕ Eliminar producto", new Color(220, 38, 38));
        btnEliminar.addActionListener(evt -> eliminarProductoSeleccionado());

        JButton btnNuevo = crearBotonAccion("+ Nuevo producto", new Color(15, 15, 15));
        btnNuevo.addActionListener(evt -> abrirDialogoNuevoProducto());

        JPanel botonesTopbar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        botonesTopbar.setBackground(Color.WHITE);
        botonesTopbar.add(btnEliminar);
        botonesTopbar.add(btnNuevo);

        topbar.add(titulo,        BorderLayout.WEST);
        topbar.add(botonesTopbar, BorderLayout.EAST);
        return topbar;
    }

    // ==================== CONTENIDO ====================

    private JPanel crearContenido() {
        JPanel contenido = new JPanel(new BorderLayout());
        contenido.setBackground(new Color(245, 245, 245));
        contenido.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        contenido.add(crearTabla(), BorderLayout.CENTER);
        return contenido;
    }

    // ==================== TABLA ====================

    private RoundedPanel crearTabla() {
        RoundedPanel panel = new RoundedPanel(12, new Color(220, 220, 220));
        panel.setBackground(Color.WHITE);
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel tituloTabla = new JLabel("Listado de productos");
        tituloTabla.setFont(new Font("Segoe UI", Font.BOLD, 14));
        tituloTabla.setForeground(new Color(20, 20, 20));
        tituloTabla.setBorder(BorderFactory.createEmptyBorder(0, 0, 12, 0));

        String[] columnas = {"ID", "Nombre", "Descripción", "Stock actual", "Stock mínimo", "Estado"};
        tabla = new StyledTable(columnas);

        tabla.setAnchoColumna(0, 40);
        tabla.setAnchoColumna(1, 150);
        tabla.setAnchoColumna(2, 250);
        tabla.setAnchoColumna(3, 100);
        tabla.setAnchoColumna(4, 100);
        tabla.setAnchoColumna(5, 110);

        tabla.getTabla().getColumnModel().getColumn(5).setCellRenderer(
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

        panel.add(tituloTabla, BorderLayout.NORTH);
        panel.add(tabla,       BorderLayout.CENTER);

        actualizarTabla();
        return panel;
    }

    // ==================== ACTUALIZAR TABLA ====================

    public void actualizarTabla() {
        tabla.limpiar();
        for (Producto p : controller.getProductos()) {
            tabla.agregarFila(new Object[]{
                p.getId(),
                p.getNombre(),
                p.getDescripcion(),
                p.getCantidadActual(),
                p.getStockMinimo(),
                calcularEstado(p)
            });
        }
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

    // ==================== ELIMINAR PRODUCTO ====================

    private void eliminarProductoSeleccionado() {
        int fila = tabla.getFilaSeleccionada();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this,
                "Selecciona un producto de la tabla primero.",
                "Sin selección", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id       = (int) tabla.getValorCelda(fila, 0);
        String nombre = (String) tabla.getValorCelda(fila, 1);

        int confirmacion = JOptionPane.showConfirmDialog(this,
            "¿Estás seguro de eliminar \"" + nombre + "\"?\n" +
            "Se eliminarán también todos sus movimientos registrados.",
            "Confirmar eliminación",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );

        if (confirmacion == JOptionPane.YES_OPTION) {
            boolean exito = controller.eliminarProducto(id);
            if (exito) {
                actualizarTabla();
                onProductoAgregado.run();
            } else {
                JOptionPane.showMessageDialog(this,
                    "No se pudo eliminar el producto.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // ==================== DIÁLOGO NUEVO PRODUCTO ====================

    private void abrirDialogoNuevoProducto() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Nuevo producto", true);
        dialog.setSize(440, 460);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());
        dialog.getContentPane().setBackground(Color.WHITE);

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 220, 220)),
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
        JLabel tituloDialog = new JLabel("Registrar nuevo producto");
        tituloDialog.setFont(new Font("Segoe UI", Font.BOLD, 15));
        tituloDialog.setForeground(new Color(20, 20, 20));
        header.add(tituloDialog, BorderLayout.WEST);

        JPanel formulario = new JPanel(new GridBagLayout());
        formulario.setBackground(Color.WHITE);
        formulario.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill    = GridBagConstraints.HORIZONTAL;
        gbc.insets  = new Insets(5, 0, 5, 0);
        gbc.weightx = 1;
        gbc.gridx   = 0;

        JTextField campoNombre      = crearCampo();
        JTextField campoDescripcion = crearCampo();
        JTextField campoCantidad    = crearCampo();
        JTextField campoStockMin    = crearCampo();

        gbc.gridy = 0; formulario.add(crearLabel("Nombre del producto"), gbc);
        gbc.gridy = 1; formulario.add(campoNombre, gbc);
        gbc.gridy = 2; formulario.add(crearLabel("Descripción"), gbc);
        gbc.gridy = 3; formulario.add(campoDescripcion, gbc);
        gbc.gridy = 4; formulario.add(crearLabel("Cantidad inicial"), gbc);
        gbc.gridy = 5; formulario.add(campoCantidad, gbc);
        gbc.gridy = 6; formulario.add(crearLabel("Stock mínimo"), gbc);
        gbc.gridy = 7; formulario.add(campoStockMin, gbc);

        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 15));
        footer.setBackground(Color.WHITE);
        footer.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(220, 220, 220)));

        JButton btnCancelar = crearBotonAccion("Cancelar", new Color(245, 245, 245));
        btnCancelar.setForeground(new Color(80, 80, 80));
        btnCancelar.addActionListener(evt -> dialog.dispose());

        JButton btnGuardar = crearBotonAccion("Guardar producto", new Color(15, 15, 15));
        btnGuardar.addActionListener(evt -> {
            String nombre      = campoNombre.getText().trim();
            String descripcion = campoDescripcion.getText().trim();
            String cantidadStr = campoCantidad.getText().trim();
            String stockMinStr = campoStockMin.getText().trim();

            if (nombre.isEmpty() || descripcion.isEmpty() || cantidadStr.isEmpty() || stockMinStr.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Completa todos los campos.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                int cantidad = Integer.parseInt(cantidadStr);
                int stockMin = Integer.parseInt(stockMinStr);
                if (cantidad < 0 || stockMin < 0) {
                    JOptionPane.showMessageDialog(dialog, "Las cantidades no pueden ser negativas.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                controller.agregarProducto(nombre, descripcion, cantidad, stockMin);
                actualizarTabla();
                onProductoAgregado.run();
                dialog.dispose();
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(dialog, "Cantidad y stock mínimo deben ser números.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        footer.add(btnCancelar);
        footer.add(btnGuardar);

        dialog.add(header,     BorderLayout.NORTH);
        dialog.add(formulario, BorderLayout.CENTER);
        dialog.add(footer,     BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    // ==================== UTILS ====================

    private JLabel crearLabel(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        label.setForeground(new Color(100, 100, 100));
        return label;
    }

    private JTextField crearCampo() {
        JTextField campo = new JTextField();
        campo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        campo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(210, 210, 210)),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        return campo;
    }

    private JButton crearBotonAccion(String texto, Color bgColor) {
        JButton btn = new JButton(texto);
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btn.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setFocusPainted(false);
        return btn;
    }
}