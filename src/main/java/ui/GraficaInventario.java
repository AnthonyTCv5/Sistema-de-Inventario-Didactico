package ui;

import java.awt.*;
import java.util.ArrayList;
import javax.swing.JPanel;

public class GraficaInventario extends JPanel {

    private ArrayList<Integer> historial;
    private int puntoMinimo;
    private int puntoMaximo;

    private static final int PADDING_IZQ  = 55;
    private static final int PADDING_DER  = 20;
    private static final int PADDING_ARR  = 20;
    private static final int PADDING_ABA  = 40;

    private static final Color COLOR_FONDO     = new Color(255, 255, 255);
    private static final Color COLOR_GRILLA    = new Color(235, 235, 235);
    private static final Color COLOR_EJE       = new Color(180, 180, 180);
    private static final Color COLOR_LINEA     = new Color(59, 130, 246);
    private static final Color COLOR_RELLENO   = new Color(59, 130, 246, 30);
    private static final Color COLOR_PUNTO     = new Color(59, 130, 246);
    private static final Color COLOR_MINIMO    = new Color(239, 68, 68);
    private static final Color COLOR_MAXIMO    = new Color(16, 185, 129);
    private static final Color COLOR_TEXTO_EJE = new Color(120, 120, 120);

    public GraficaInventario() {
        this.historial = new ArrayList<>();
        setBackground(COLOR_FONDO);
        setPreferredSize(new Dimension(0, 280));
    }

    public void setDatos(ArrayList<Integer> historial, int puntoMinimo, int puntoMaximo) {
        this.historial  = historial;
        this.puntoMinimo = puntoMinimo;
        this.puntoMaximo = puntoMaximo;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        int ancho  = getWidth()  - PADDING_IZQ - PADDING_DER;
        int alto   = getHeight() - PADDING_ARR  - PADDING_ABA;

        dibujarFondo(g2, ancho, alto);

        if (historial == null || historial.size() < 2) {
            dibujarMensajeVacio(g2);
            return;
        }

        int valMin = Integer.MAX_VALUE;
        int valMax = Integer.MIN_VALUE;
        for (int v : historial) {
            if (v < valMin) valMin = v;
            if (v > valMax) valMax = v;
        }
        if (valMin == valMax) { valMin -= 1; valMax += 1; }

        dibujarGrilla(g2, ancho, alto, valMin, valMax);
        dibujarEjes(g2,   ancho, alto, valMin, valMax);
        dibujarRelleno(g2, ancho, alto, valMin, valMax);
        dibujarLinea(g2,   ancho, alto, valMin, valMax);
        dibujarPuntos(g2,  ancho, alto, valMin, valMax);
    }

    // ==================== FONDO ====================

    private void dibujarFondo(Graphics2D g2, int ancho, int alto) {
        g2.setColor(COLOR_FONDO);
        g2.fillRect(0, 0, getWidth(), getHeight());
    }

    // ==================== MENSAJE VACÍO ====================

    private void dibujarMensajeVacio(Graphics2D g2) {
        g2.setColor(new Color(180, 180, 180));
        g2.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        String msg = "Sin suficientes datos para graficar";
        FontMetrics fm = g2.getFontMetrics();
        int x = (getWidth()  - fm.stringWidth(msg)) / 2;
        int y = (getHeight() + fm.getAscent())       / 2;
        g2.drawString(msg, x, y);
    }

    // ==================== GRILLA ====================

    private void dibujarGrilla(Graphics2D g2, int ancho, int alto, int valMin, int valMax) {
        g2.setColor(COLOR_GRILLA);
        g2.setStroke(new BasicStroke(0.5f));
        int filas = 5;
        for (int i = 0; i <= filas; i++) {
            int y = PADDING_ARR + (alto * i / filas);
            g2.drawLine(PADDING_IZQ, y, PADDING_IZQ + ancho, y);
        }
        int n = historial.size();
        for (int i = 0; i < n; i++) {
            int x = PADDING_IZQ + (n == 1 ? ancho / 2 : ancho * i / (n - 1));
            g2.drawLine(x, PADDING_ARR, x, PADDING_ARR + alto);
        }
    }

    // ==================== EJES ====================

    private void dibujarEjes(Graphics2D g2, int ancho, int alto, int valMin, int valMax) {
        g2.setColor(COLOR_EJE);
        g2.setStroke(new BasicStroke(1f));

        // Eje Y
        g2.drawLine(PADDING_IZQ, PADDING_ARR, PADDING_IZQ, PADDING_ARR + alto);
        // Eje X
        g2.drawLine(PADDING_IZQ, PADDING_ARR + alto, PADDING_IZQ + ancho, PADDING_ARR + alto);

        // Labels eje Y
        g2.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        g2.setColor(COLOR_TEXTO_EJE);
        int filas = 5;
        for (int i = 0; i <= filas; i++) {
            int y = PADDING_ARR + (alto * i / filas);
            int valor = valMax - (valMax - valMin) * i / filas;
            String label = String.valueOf(valor);
            FontMetrics fm = g2.getFontMetrics();
            g2.drawString(label, PADDING_IZQ - fm.stringWidth(label) - 6, y + fm.getAscent() / 2);
        }

        // Labels eje X
        int n = historial.size();
        for (int i = 0; i < n; i++) {
            int x = PADDING_IZQ + (n == 1 ? ancho / 2 : ancho * i / (n - 1));
            String label = "t=" + i;
            FontMetrics fm = g2.getFontMetrics();
            g2.drawString(label, x - fm.stringWidth(label) / 2, PADDING_ARR + alto + 18);
        }
    }

    // ==================== RELLENO BAJO LA CURVA ====================

    private void dibujarRelleno(Graphics2D g2, int ancho, int alto, int valMin, int valMax) {
        int n = historial.size();
        int[] xs = new int[n + 2];
        int[] ys = new int[n + 2];

        for (int i = 0; i < n; i++) {
            xs[i] = PADDING_IZQ + (n == 1 ? ancho / 2 : ancho * i / (n - 1));
            ys[i] = PADDING_ARR + alto - (int) ((double)(historial.get(i) - valMin) / (valMax - valMin) * alto);
        }
        xs[n]     = xs[n - 1];
        ys[n]     = PADDING_ARR + alto;
        xs[n + 1] = xs[0];
        ys[n + 1] = PADDING_ARR + alto;

        g2.setColor(COLOR_RELLENO);
        g2.fillPolygon(xs, ys, n + 2);
    }

    // ==================== LÍNEA ====================

    private void dibujarLinea(Graphics2D g2, int ancho, int alto, int valMin, int valMax) {
        int n = historial.size();
        g2.setColor(COLOR_LINEA);
        g2.setStroke(new BasicStroke(2.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

        for (int i = 0; i < n - 1; i++) {
            int x1 = PADDING_IZQ + ancho * i       / (n - 1);
            int y1 = PADDING_ARR + alto - (int)((double)(historial.get(i)     - valMin) / (valMax - valMin) * alto);
            int x2 = PADDING_IZQ + ancho * (i + 1) / (n - 1);
            int y2 = PADDING_ARR + alto - (int)((double)(historial.get(i + 1) - valMin) / (valMax - valMin) * alto);
            g2.drawLine(x1, y1, x2, y2);
        }
    }

    // ==================== PUNTOS ====================

    private void dibujarPuntos(Graphics2D g2, int ancho, int alto, int valMin, int valMax) {
        int n = historial.size();
        for (int i = 0; i < n; i++) {
            int x = PADDING_IZQ + (n == 1 ? ancho / 2 : ancho * i / (n - 1));
            int y = PADDING_ARR + alto - (int)((double)(historial.get(i) - valMin) / (valMax - valMin) * alto);

            // Color especial para mínimo y máximo
            if (i == puntoMinimo) {
                g2.setColor(COLOR_MINIMO);
                g2.fillOval(x - 6, y - 6, 12, 12);
                g2.setColor(Color.WHITE);
                g2.fillOval(x - 3, y - 3, 6, 6);
            } else if (i == puntoMaximo) {
                g2.setColor(COLOR_MAXIMO);
                g2.fillOval(x - 6, y - 6, 12, 12);
                g2.setColor(Color.WHITE);
                g2.fillOval(x - 3, y - 3, 6, 6);
            } else {
                g2.setColor(COLOR_PUNTO);
                g2.fillOval(x - 4, y - 4, 8, 8);
                g2.setColor(Color.WHITE);
                g2.fillOval(x - 2, y - 2, 4, 4);
            }
        }
        
    }
}