package ui;

import java.awt.*;
import java.util.ArrayList;
import javax.swing.JPanel;

public class GraficaProyeccion extends JPanel {

    private ArrayList<Integer> historial;
    private ArrayList<Double>  proyeccion;

    private static final int PADDING_IZQ = 55;
    private static final int PADDING_DER = 20;
    private static final int PADDING_ARR = 20;
    private static final int PADDING_ABA = 40;

    private static final Color COLOR_FONDO      = new Color(255, 255, 255);
    private static final Color COLOR_GRILLA     = new Color(235, 235, 235);
    private static final Color COLOR_EJE        = new Color(180, 180, 180);
    private static final Color COLOR_REAL       = new Color(59,  130, 246);
    private static final Color COLOR_REAL_FILL  = new Color(59,  130, 246, 25);
    private static final Color COLOR_PROY       = new Color(245, 158, 11);
    private static final Color COLOR_PROY_FILL  = new Color(245, 158, 11, 20);
    private static final Color COLOR_PUNTO_REAL = new Color(59,  130, 246);
    private static final Color COLOR_PUNTO_PROY = new Color(245, 158, 11);
    private static final Color COLOR_TEXTO_EJE  = new Color(120, 120, 120);

    public GraficaProyeccion() {
        this.historial  = new ArrayList<>();
        this.proyeccion = new ArrayList<>();
        setBackground(COLOR_FONDO);
        setPreferredSize(new Dimension(0, 280));
    }

    public void setDatos(ArrayList<Integer> historial, ArrayList<Double> proyeccion) {
        this.historial  = historial;
        this.proyeccion = proyeccion;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,      RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        int ancho = getWidth()  - PADDING_IZQ - PADDING_DER;
        int alto  = getHeight() - PADDING_ARR  - PADDING_ABA;

        g2.setColor(COLOR_FONDO);
        g2.fillRect(0, 0, getWidth(), getHeight());

        if (historial == null || historial.size() < 2) {
            dibujarMensajeVacio(g2);
            return;
        }

        // Combinar todos los valores para escalar
        ArrayList<Double> todos = new ArrayList<>();
        for (int v : historial) todos.add((double) v);
        todos.addAll(proyeccion);

        double valMin = Double.MAX_VALUE;
        double valMax = Double.MIN_VALUE;
        for (double v : todos) {
            if (v < valMin) valMin = v;
            if (v > valMax) valMax = v;
        }
        if (valMin == valMax) { valMin -= 1; valMax += 1; }

        // Total de puntos = historial + proyeccion
        int totalPuntos = historial.size() + proyeccion.size();

        dibujarGrilla(g2, ancho, alto);
        dibujarSeparador(g2, ancho, alto, historial.size(), totalPuntos);
        dibujarRellenoReal(g2, ancho, alto, valMin, valMax, totalPuntos);
        dibujarRellenoProyeccion(g2, ancho, alto, valMin, valMax, totalPuntos);
        dibujarLineaReal(g2, ancho, alto, valMin, valMax, totalPuntos);
        dibujarLineaProyeccion(g2, ancho, alto, valMin, valMax, totalPuntos);
        dibujarPuntosReales(g2, ancho, alto, valMin, valMax, totalPuntos);
        dibujarPuntosProyeccion(g2, ancho, alto, valMin, valMax, totalPuntos);
        dibujarEjes(g2, ancho, alto, valMin, valMax, totalPuntos);
    }

    // ==================== MENSAJE VACÍO ====================

    private void dibujarMensajeVacio(Graphics2D g2) {
        g2.setColor(new Color(180, 180, 180));
        g2.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        String msg = "Sin suficientes datos para proyectar";
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString(msg,
            (getWidth()  - fm.stringWidth(msg)) / 2,
            (getHeight() + fm.getAscent())       / 2
        );
    }

    // ==================== GRILLA ====================

    private void dibujarGrilla(Graphics2D g2, int ancho, int alto) {
        g2.setColor(COLOR_GRILLA);
        g2.setStroke(new BasicStroke(0.5f));
        for (int i = 0; i <= 5; i++) {
            int y = PADDING_ARR + (alto * i / 5);
            g2.drawLine(PADDING_IZQ, y, PADDING_IZQ + ancho, y);
        }
    }

    // ==================== SEPARADOR REAL/PROYECCIÓN ====================

    private void dibujarSeparador(Graphics2D g2, int ancho, int alto, int nReal, int totalPuntos) {
        int xSep = PADDING_IZQ + ancho * (nReal - 1) / (totalPuntos - 1);
        g2.setColor(new Color(200, 200, 200));
        g2.setStroke(new BasicStroke(1f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER,
            10f, new float[]{5f, 5f}, 0f));
        g2.drawLine(xSep, PADDING_ARR, xSep, PADDING_ARR + alto);

        g2.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        g2.setColor(new Color(160, 160, 160));
        g2.drawString("Proyección →", xSep + 4, PADDING_ARR + 12);
    }

    // ==================== RELLENO REAL ====================

    private void dibujarRellenoReal(Graphics2D g2, int ancho, int alto,
            double valMin, double valMax, int totalPuntos) {
        int n = historial.size();
        int[] xs = new int[n + 2];
        int[] ys = new int[n + 2];
        for (int i = 0; i < n; i++) {
            xs[i] = xPos(i, ancho, totalPuntos);
            ys[i] = yPos(historial.get(i), alto, valMin, valMax);
        }
        xs[n]     = xs[n - 1];
        ys[n]     = PADDING_ARR + alto;
        xs[n + 1] = xs[0];
        ys[n + 1] = PADDING_ARR + alto;
        g2.setColor(COLOR_REAL_FILL);
        g2.fillPolygon(xs, ys, n + 2);
    }

    // ==================== RELLENO PROYECCIÓN ====================

    private void dibujarRellenoProyeccion(Graphics2D g2, int ancho, int alto,
            double valMin, double valMax, int totalPuntos) {
        int nReal = historial.size();
        int nProy = proyeccion.size();
        int n     = nProy + 1;
        int[] xs  = new int[n + 2];
        int[] ys  = new int[n + 2];

        xs[0] = xPos(nReal - 1, ancho, totalPuntos);
        ys[0] = yPos(historial.get(nReal - 1), alto, valMin, valMax);

        for (int i = 0; i < nProy; i++) {
            xs[i + 1] = xPos(nReal + i, ancho, totalPuntos);
            ys[i + 1] = yPos(proyeccion.get(i), alto, valMin, valMax);
        }
        xs[n]     = xs[n - 1];
        ys[n]     = PADDING_ARR + alto;
        xs[n + 1] = xs[0];
        ys[n + 1] = PADDING_ARR + alto;
        g2.setColor(COLOR_PROY_FILL);
        g2.fillPolygon(xs, ys, n + 2);
    }

    // ==================== LÍNEA REAL ====================

    private void dibujarLineaReal(Graphics2D g2, int ancho, int alto,
            double valMin, double valMax, int totalPuntos) {
        int n = historial.size();
        g2.setColor(COLOR_REAL);
        g2.setStroke(new BasicStroke(2.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        for (int i = 0; i < n - 1; i++) {
            g2.drawLine(
                xPos(i,     ancho, totalPuntos), yPos(historial.get(i),     alto, valMin, valMax),
                xPos(i + 1, ancho, totalPuntos), yPos(historial.get(i + 1), alto, valMin, valMax)
            );
        }
    }

    // ==================== LÍNEA PROYECCIÓN ====================

    private void dibujarLineaProyeccion(Graphics2D g2, int ancho, int alto,
            double valMin, double valMax, int totalPuntos) {
        int nReal = historial.size();
        int nProy = proyeccion.size();

        g2.setColor(COLOR_PROY);
        g2.setStroke(new BasicStroke(2.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
            10f, new float[]{8f, 5f}, 0f));

        // Conecta último punto real con primer punto proyectado
        g2.drawLine(
            xPos(nReal - 1, ancho, totalPuntos), yPos(historial.get(nReal - 1), alto, valMin, valMax),
            xPos(nReal,     ancho, totalPuntos), yPos(proyeccion.get(0),         alto, valMin, valMax)
        );

        for (int i = 0; i < nProy - 1; i++) {
            g2.drawLine(
                xPos(nReal + i,     ancho, totalPuntos), yPos(proyeccion.get(i),     alto, valMin, valMax),
                xPos(nReal + i + 1, ancho, totalPuntos), yPos(proyeccion.get(i + 1), alto, valMin, valMax)
            );
        }
    }

    // ==================== PUNTOS REALES ====================

    private void dibujarPuntosReales(Graphics2D g2, int ancho, int alto,
            double valMin, double valMax, int totalPuntos) {
        int n = historial.size();
        for (int i = 0; i < n; i++) {
            int x = xPos(i, ancho, totalPuntos);
            int y = yPos(historial.get(i), alto, valMin, valMax);
            g2.setColor(COLOR_PUNTO_REAL);
            g2.fillOval(x - 4, y - 4, 8, 8);
            g2.setColor(Color.WHITE);
            g2.fillOval(x - 2, y - 2, 4, 4);
        }
    }

    // ==================== PUNTOS PROYECCIÓN ====================

    private void dibujarPuntosProyeccion(Graphics2D g2, int ancho, int alto,
            double valMin, double valMax, int totalPuntos) {
        int nReal = historial.size();
        int nProy = proyeccion.size();
        for (int i = 0; i < nProy; i++) {
            int x = xPos(nReal + i, ancho, totalPuntos);
            int y = yPos(proyeccion.get(i), alto, valMin, valMax);
            g2.setColor(COLOR_PUNTO_PROY);
            g2.fillOval(x - 4, y - 4, 8, 8);
            g2.setColor(Color.WHITE);
            g2.fillOval(x - 2, y - 2, 4, 4);
        }
    }

    // ==================== EJES ====================

    private void dibujarEjes(Graphics2D g2, int ancho, int alto,
            double valMin, double valMax, int totalPuntos) {
        g2.setColor(COLOR_EJE);
        g2.setStroke(new BasicStroke(1f));
        g2.drawLine(PADDING_IZQ, PADDING_ARR, PADDING_IZQ, PADDING_ARR + alto);
        g2.drawLine(PADDING_IZQ, PADDING_ARR + alto, PADDING_IZQ + ancho, PADDING_ARR + alto);

        g2.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        g2.setColor(COLOR_TEXTO_EJE);
        FontMetrics fm = g2.getFontMetrics();

        // Labels eje Y
        for (int i = 0; i <= 5; i++) {
            int y     = PADDING_ARR + (alto * i / 5);
            double val = valMax - (valMax - valMin) * i / 5;
            String label = String.valueOf((int) val);
            g2.drawString(label, PADDING_IZQ - fm.stringWidth(label) - 5, y + fm.getAscent() / 2);
        }

        // Labels eje X
        int nReal = historial.size();
        int nProy = proyeccion.size();
        for (int i = 0; i < totalPuntos; i++) {
            int x = xPos(i, ancho, totalPuntos);
            String label = i < nReal ? "t=" + i : "t+" + (i - nReal + 1);
            g2.drawString(label, x - fm.stringWidth(label) / 2, PADDING_ARR + alto + 18);
        }
    }

    // ==================== UTILS ====================

    private int xPos(int i, int ancho, int totalPuntos) {
        return PADDING_IZQ + ancho * i / (totalPuntos - 1);
    }

    private int yPos(double valor, int alto, double valMin, double valMax) {
        return PADDING_ARR + alto - (int)((valor - valMin) / (valMax - valMin) * alto);
    }
}