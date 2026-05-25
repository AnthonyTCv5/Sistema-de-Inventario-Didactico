package ui;

import controller.InventarioController;
import model.Movimiento;
import model.Producto;
import ui.components.RoundedPanel;
import ui.components.StyledTable;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.ArrayList;

public class PanelMovimientos extends JPanel {

    private InventarioController controller;
    private StyledTable tabla;
    private JComboBox<Producto> comboProductos;
    private Runnable onMovimientoRegistrado;

    public PanelMovimientos(InventarioController controller, Runnable onMovimientoRegistrado) {
        this.controller = controller;
        this.onMovimientoRegistrado = onMovimientoRegistrado;
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

        JLabel titulo = new JLabel("Movimientos");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titulo.setForeground(new Color(20, 20, 20));

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        botones.setBackground(Color.WHITE);

        JButton btnEntrada = crearBotonAccion("+ Entrada", new Color(16, 185, 129));
        JButton btnSalida  = crearBotonAccion("- Salida",  new Color(239, 68, 68));

        btnEntrada.addActionListener(evt -> abrirDialogoMovimiento(Movimiento.Tipo.ENTRADA));
        btnSalida.addActionListener(evt  -> abrirDialogoMovimiento(Movimiento.Tipo.SALIDA));

        botones.add(btnEntrada);
        botones.add(btnSalida);

        topbar.add(titulo,  BorderLayout.WEST);
        topbar.add(botones, BorderLayout.EAST);
        return topbar;
    }

    // ==================== CONTENIDO ====================

    private JPanel crearContenido() {
        JPanel contenido = new JPanel(new BorderLayout());
        contenido.setBackground(new Color(245, 245, 245));
        contenido.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        contenido.add(crearFiltro(), BorderLayout.NORTH);
        contenido.add(crearTabla(), BorderLayout.CENTER);
        return contenido;
    }

    // ==================== FILTRO ====================

    private JPanel crearFiltro() {
        RoundedPanel filtro = new RoundedPanel(10, new Color(220, 220, 220));
        filtro.setBackground(Color.WHITE);
        filtro.setLayout(new FlowLayout(FlowLayout.LEFT, 12, 10));
        filtro.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        filtro.setBorder(BorderFactory.createEmptyBorder(0, 0, 12, 0));

        JLabel labelFiltro = new JLabel("Filtrar por producto:");
        labelFiltro.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        labelFiltro.setForeground(new Color(100, 100, 100));

        comboProductos = new JComboBox<>();
        comboProductos.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        comboProductos.setPreferredSize(new Dimension(220, 32));
        comboProductos.addItem(null);
        for (Producto p : controller.getProductos()) comboProductos.addItem(p);
        comboProductos.addActionListener(evt -> actualizarTabla());

        filtro.add(labelFiltro);
        filtro.add(comboProductos);
        return filtro;
    }

    // ==================== TABLA ====================

    private RoundedPanel crearTabla() {
        RoundedPanel panel = new RoundedPanel(12, new Color(220, 220, 220));
        panel.setBackground(Color.WHITE);
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel tituloTabla = new JLabel("Historial de movimientos");
        tituloTabla.setFont(new Font("Segoe UI", Font.BOLD, 14));
        tituloTabla.setForeground(new Color(20, 20, 20));
        tituloTabla.setBorder(BorderFactory.createEmptyBorder(0, 0, 12, 0));

        String[] columnas = {"ID", "Producto", "Tipo", "Cantidad", "Fecha"};
        tabla = new StyledTable(columnas);

        tabla.setAnchoColumna(0, 40);
        tabla.setAnchoColumna(1, 160);
        tabla.setAnchoColumna(2, 110);
        tabla.setAnchoColumna(3, 90);
        tabla.setAnchoColumna(4, 200);

        // Renderer colores columna Tipo
        tabla.getTabla().getColumnModel().getColumn(2).setCellRenderer(
            new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable t, Object value,
                        boolean isSelected, boolean hasFocus, int row, int col) {
                    super.getTableCellRendererComponent(t, value, isSelected, hasFocus, row, col);
                    setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
                    if (!isSelected) {
                        String val = value != null ? value.toString() : "";
                        setBackground(row % 2 == 0 ? Color.WHITE : new Color(249, 250, 251));
                        if (val.contains("Entrada")) {
                            setForeground(new Color(21, 128, 61));
                        } else {
                            setForeground(new Color(185, 28, 28));
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
        Producto seleccionado = (Producto) comboProductos.getSelectedItem();

        ArrayList<Movimiento> movimientos = new ArrayList<>();
        if (seleccionado == null) {
            for (Producto p : controller.getProductos()) {
                movimientos.addAll(controller.getMovimientosDeProducto(p.getId()));
            }
        } else {
            movimientos = controller.getMovimientosDeProducto(seleccionado.getId());
        }

        for (Movimiento m : movimientos) {
            Producto p = controller.getProductoPorId(m.getProductoId());
            String nombreProducto = p != null ? p.getNombre() : "Desconocido";
            String tipo = m.getTipo() == Movimiento.Tipo.ENTRADA ? "📥 Entrada" : "📤 Salida";
            tabla.agregarFila(new Object[]{
                m.getId(),
                nombreProducto,
                tipo,
                m.getCantidad(),
                m.getFecha().toLocalDate() + " " + m.getFecha().toLocalTime().withNano(0)
            });
        }
    }

    // ==================== DIÁLOGO MOVIMIENTO ====================

    private void abrirDialogoMovimiento(Movimiento.Tipo tipo) {
        ArrayList<Producto> productos = controller.getProductos();
        if (productos.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Primero registra un producto.", "Sin productos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this),
            tipo == Movimiento.Tipo.ENTRADA ? "Registrar entrada" : "Registrar salida", true);
        dialog.setSize(380, 300);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());
        dialog.getContentPane().setBackground(Color.WHITE);

        // Header
        Color colorTipo = tipo == Movimiento.Tipo.ENTRADA ? new Color(16, 185, 129) : new Color(239, 68, 68);
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(colorTipo);
        header.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        JLabel tituloDialog = new JLabel(tipo == Movimiento.Tipo.ENTRADA ? "📥 Registrar entrada" : "📤 Registrar salida");
        tituloDialog.setFont(new Font("Segoe UI", Font.BOLD, 15));
        tituloDialog.setForeground(Color.WHITE);
        header.add(tituloDialog, BorderLayout.WEST);

        // Formulario
        JPanel formulario = new JPanel(new GridBagLayout());
        formulario.setBackground(Color.WHITE);
        formulario.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill    = GridBagConstraints.HORIZONTAL;
        gbc.insets  = new Insets(5, 0, 5, 0);
        gbc.weightx = 1;
        gbc.gridx   = 0;

        JComboBox<Producto> comboDialog = new JComboBox<>();
        comboDialog.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        for (Producto p : productos) comboDialog.addItem(p);
        if (comboProductos.getSelectedItem() != null) {
            comboDialog.setSelectedItem(comboProductos.getSelectedItem());
        }

        JTextField campoCantidad = new JTextField();
        campoCantidad.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        campoCantidad.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(210, 210, 210)),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));

        gbc.gridy = 0; formulario.add(crearLabel("Producto"),  gbc);
        gbc.gridy = 1; formulario.add(comboDialog,             gbc);
        gbc.gridy = 2; formulario.add(crearLabel("Cantidad"),  gbc);
        gbc.gridy = 3; formulario.add(campoCantidad,           gbc);

        // Footer
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 15));
        footer.setBackground(Color.WHITE);
        footer.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(220, 220, 220)));

        JButton btnCancelar  = crearBotonAccion("Cancelar",   new Color(245, 245, 245));
        btnCancelar.setForeground(new Color(80, 80, 80));
        btnCancelar.addActionListener(evt -> dialog.dispose());

        JButton btnConfirmar = crearBotonAccion("Confirmar", colorTipo);
        btnConfirmar.addActionListener(evt -> {
            Producto productoSeleccionado = (Producto) comboDialog.getSelectedItem();
            String cantidadStr = campoCantidad.getText().trim();

            if (cantidadStr.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Ingresa una cantidad.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                int cantidad = Integer.parseInt(cantidadStr);
                if (cantidad <= 0) {
                    JOptionPane.showMessageDialog(dialog, "La cantidad debe ser mayor a 0.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                boolean exito = tipo == Movimiento.Tipo.ENTRADA
                    ? controller.registrarEntrada(productoSeleccionado.getId(), cantidad)
                    : controller.registrarSalida(productoSeleccionado.getId(), cantidad);

                if (exito) {
                    actualizarTabla();
                    onMovimientoRegistrado.run();
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Stock insuficiente.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(dialog, "La cantidad debe ser un número.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        footer.add(btnCancelar);
        footer.add(btnConfirmar);

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

    public void refrescarProductos() {
        comboProductos.removeAllItems();
        comboProductos.addItem(null);
        for (Producto p : controller.getProductos()) comboProductos.addItem(p);
        actualizarTabla();
    }
}