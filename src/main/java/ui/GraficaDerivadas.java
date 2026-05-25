package ui;

import java.awt.*;
import java.util.ArrayList;
import javax.swing.JPanel;

public class GraficaDerivadas extends JPanel {

    private double[] derivadas;
    private ArrayList<Integer> historial;

    private static final int PADDING_IZQ = 55;
    private static final int PADDING_DER = 20;
    private static final int PADDING_ARR = 20;
    private static final int PADDING_ABA = 40;

    private static final Color COLOR_FONDO      = new Color(255, 255, 255);
    private static final Color COLOR_GRILLA     = new Color(235, 235, 235);
    private static final Color COLOR_EJE        = new Color(180, 180, 180);
    private static final Color COLOR_POSITIVO   = new Color(16,  185, 129);
    private static final Color COLOR_NEGATIVO   = new Color(239, 68,  68);
    private static final Color COLOR_CERO       = new Color(180, 180, 180);
    private static final Color COLOR_TEXTO_EJE  = new Color(120, 120, 120);
    private static final Color COLOR_LINEA_CERO = new Color(100, 100, 100);

    public GraficaDerivadas() {
        this.derivadas = new double[0];
        this.historial = new ArrayList<>();
        setBackground(COLOR_FONDO);
        setPreferredSize(new Dimension(0, 250));
    }

    public void setDatos(double[] derivadas, ArrayList<Integer> historial) {
        this.derivadas = derivadas;
        this.historial = historial;
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

        if (derivadas == null || derivadas.length < 2) {
            dibujarMensajeVacio(g2);
            return;
        }

        double valMax = 0;
        for (double d : derivadas) if (Math.abs(d) > valMax) valMax = Math.abs(d);
        if (valMax == 0) valMax = 1;

        dibujarGrilla(g2, ancho, alto);
        dibujarLineaCero(g2, ancho, alto);
        dibujarBarras(g2, ancho, alto, valMax);
        dibujarEjes(g2, ancho, alto, valMax);
    }

    // ==================== MENSAJE VACÍO ====================

    private void dibujarMensajeVacio(Graphics2D g2) {
        g2.setColor(new Color(180, 180, 180));
        g2.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        String msg = "Sin suficientes datos para analizar";
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
        for (int i = 0; i <= 4; i++) {
            int y = PADDING_ARR + (alto * i / 4);
            g2.drawLine(PADDING_IZQ, y, PADDING_IZQ + ancho, y);
        }
    }

    // ==================== LÍNEA DE CERO ====================

    private void dibujarLineaCero(Graphics2D g2, int ancho, int alto) {
        int yCero = PADDING_ARR + alto / 2;
        g2.setColor(COLOR_LINEA_CERO);
        g2.setStroke(new BasicStroke(1f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER,
            10f, new float[]{4f, 4f}, 0f));
        g2.drawLine(PADDING_IZQ, yCero, PADDING_IZQ + ancho, yCero);
    }

    // ==================== BARRAS ====================

    private void dibujarBarras(Graphics2D g2, int ancho, int alto, double valMax) {
        int n = derivadas.length;
        int anchoTotal = ancho / n;
        int anchoBarra = Math.max(4, (int)(anchoTotal * 0.55));
        int yCero = PADDING_ARR + alto / 2;

        for (int i = 0; i < n; i++) {
            int cx = PADDING_IZQ + (int)((i + 0.5) * ancho / n);
            int altura = (int)(Math.abs(derivadas[i]) / valMax * (alto / 2));

            Color color;
            if      (derivadas[i] > 0)  color = COLOR_POSITIVO;
            else if (derivadas[i] < 0)  color = COLOR_NEGATIVO;
            else                         color = COLOR_CERO;

            int x      = cx - anchoBarra / 2;
            int y      = derivadas[i] >= 0 ? yCero - altura : yCero;
            int hBarra = Math.max(2, altura);

            // Relleno
            g2.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 180));
            g2.fillRoundRect(x, y, anchoBarra, hBarra, 4, 4);

            // Borde
            g2.setColor(color);
            g2.setStroke(new BasicStroke(1f));
            g2.drawRoundRect(x, y, anchoBarra, hBarra, 4, 4);

            // Valor encima
            g2.setFont(new Font("Segoe UI", Font.PLAIN, 10));
            g2.setColor(COLOR_TEXTO_EJE);
            String val = String.format("%.1f", derivadas[i]);
            FontMetrics fm = g2.getFontMetrics();
            int valX = cx - fm.stringWidth(val) / 2;
            int valY = derivadas[i] >= 0
                ? y - 3
                : y + hBarra + fm.getAscent();
            g2.drawString(val, valX, valY);
        }
    }

    // ==================== EJES ====================

    private void dibujarEjes(Graphics2D g2, int ancho, int alto, double valMax) {
        g2.setColor(COLOR_EJE);
        g2.setStroke(new BasicStroke(1f));

        // Eje Y
        g2.drawLine(PADDING_IZQ, PADDING_ARR, PADDING_IZQ, PADDING_ARR + alto);
        // Eje X
        g2.drawLine(PADDING_IZQ, PADDING_ARR + alto, PADDING_IZQ + ancho, PADDING_ARR + alto);

        // Labels eje Y
        g2.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        g2.setColor(COLOR_TEXTO_EJE);
        FontMetrics fm = g2.getFontMetrics();

        String[] labelsY = {
            String.format("%.1f", valMax),
            String.format("%.1f", valMax / 2),
            "0",
            String.format("%.1f", -valMax / 2),
            String.format("%.1f", -valMax)
        };
        for (int i = 0; i < labelsY.length; i++) {
            int y = PADDING_ARR + (alto * i / 4);
            g2.drawString(labelsY[i], PADDING_IZQ - fm.stringWidth(labelsY[i]) - 5, y + fm.getAscent() / 2);
        }

        // Labels eje X
        int n = derivadas.length;
        for (int i = 0; i < n; i++) {
            int cx = PADDING_IZQ + (int)((i + 0.5) * ancho / n);
            String label = "t=" + i;
            g2.drawString(label, cx - fm.stringWidth(label) / 2, PADDING_ARR + alto + 18);
        }
    }
}