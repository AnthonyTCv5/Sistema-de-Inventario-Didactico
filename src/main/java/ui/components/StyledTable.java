package ui.components;

import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class StyledTable extends JScrollPane {

    private JTable tabla;
    private DefaultTableModel modelo;

    public StyledTable(String[] columnas) {
        modelo = new DefaultTableModel(columnas, 0) {
            public boolean isCellEditable(int row, int column) { return false; }
        };

        tabla = new JTable(modelo);
        configurarTabla();
        configurarHeader();
        configurarScrollbar();

        setBorder(BorderFactory.createEmptyBorder());
        setViewportBorder(BorderFactory.createEmptyBorder());
        getViewport().setBackground(Color.WHITE);
        setBackground(Color.WHITE);
        setViewportView(tabla);
    }

    private void configurarTabla() {
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tabla.setRowHeight(36);
        tabla.setShowGrid(true);
        tabla.setGridColor(new Color(240, 240, 240));
        tabla.setSelectionBackground(new Color(235, 245, 255));
        tabla.setSelectionForeground(new Color(20, 20, 20));
        tabla.setFocusable(false);
        tabla.setIntercellSpacing(new Dimension(0, 0));
        tabla.setFillsViewportHeight(true);

        tabla.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(249, 250, 251));
                }
                setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
                return c;
            }
        });
    }

    private void configurarHeader() {
        tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tabla.getTableHeader().setBackground(new Color(245, 245, 245));
        tabla.getTableHeader().setForeground(new Color(80, 80, 80));
        tabla.getTableHeader().setPreferredSize(new Dimension(0, 38));
        tabla.getTableHeader().setBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 220, 220))
        );
        tabla.getTableHeader().setReorderingAllowed(false);

        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setBackground(new Color(245, 245, 245));
                setForeground(new Color(80, 80, 80));
                setFont(new Font("Segoe UI", Font.BOLD, 12));
                setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 220, 220)),
                    BorderFactory.createEmptyBorder(0, 10, 0, 10)
                ));
                return this;
            }
        };

        for (int i = 0; i < tabla.getColumnCount(); i++) {
            tabla.getColumnModel().getColumn(i).setHeaderRenderer(headerRenderer);
        }
    }

    private void configurarScrollbar() {
        getVerticalScrollBar().setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                thumbColor        = new Color(200, 200, 200);
                thumbHighlightColor = new Color(180, 180, 180);
                trackColor        = new Color(245, 245, 245);
            }

            @Override
            protected JButton createDecreaseButton(int orientation) {
                return crearBotonVacio();
            }

            @Override
            protected JButton createIncreaseButton(int orientation) {
                return crearBotonVacio();
            }

            @Override
            protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
                if (thumbBounds.isEmpty()) return;
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(isDragging ? new Color(160, 160, 160) : new Color(200, 200, 200));
                g2.fillRoundRect(
                    thumbBounds.x + 3,
                    thumbBounds.y + 2,
                    thumbBounds.width - 6,
                    thumbBounds.height - 4,
                    8, 8
                );
                g2.dispose();
            }

            @Override
            protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(new Color(245, 245, 245));
                g2.fillRect(trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height);
                g2.dispose();
            }
        });

        getVerticalScrollBar().setPreferredSize(new Dimension(8, 0));
        getVerticalScrollBar().setOpaque(false);
        getHorizontalScrollBar().setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                thumbColor  = new Color(200, 200, 200);
                trackColor  = new Color(245, 245, 245);
            }

            @Override
            protected JButton createDecreaseButton(int orientation) {
                return crearBotonVacio();
            }

            @Override
            protected JButton createIncreaseButton(int orientation) {
                return crearBotonVacio();
            }

            @Override
            protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
                if (thumbBounds.isEmpty()) return;
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(200, 200, 200));
                g2.fillRoundRect(
                    thumbBounds.x + 2,
                    thumbBounds.y + 3,
                    thumbBounds.width - 4,
                    thumbBounds.height - 6,
                    8, 8
                );
                g2.dispose();
            }

            @Override
            protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(new Color(245, 245, 245));
                g2.fillRect(trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height);
                g2.dispose();
            }
        });

        getHorizontalScrollBar().setPreferredSize(new Dimension(0, 8));
    }

    private JButton crearBotonVacio() {
        JButton btn = new JButton();
        btn.setPreferredSize(new Dimension(0, 0));
        btn.setMinimumSize(new Dimension(0, 0));
        btn.setMaximumSize(new Dimension(0, 0));
        return btn;
    }

    public void agregarFila(Object[] fila) { modelo.addRow(fila); }
    public void limpiar() { modelo.setRowCount(0); }

    public void setAnchoColumna(int columna, int ancho) {
        tabla.getColumnModel().getColumn(columna).setPreferredWidth(ancho);
    }

    public int getFilaSeleccionada() { return tabla.getSelectedRow(); }
    public Object getValorCelda(int fila, int columna) { return modelo.getValueAt(fila, columna); }
    public JTable getTabla() { return tabla; }
    public DefaultTableModel getModelo() { return modelo; }
}