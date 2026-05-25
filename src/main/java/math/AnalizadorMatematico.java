package math;

public class AnalizadorMatematico {

    private FuncionInventario funcion;

    public AnalizadorMatematico(FuncionInventario funcion) {
        this.funcion = funcion;
    }

    /*
     * Derivada numérica en el punto t.
     * Usa diferencia central: (I(t+1) - I(t-1)) / 2
     * En los extremos usa diferencia simple para no salirse del arreglo.
     */
    public double derivadaEn(int t) {
        int n = funcion.getTamanio();
        if (n < 2) {
            return 0;
        }

        if (t == 0) {
            return funcion.evaluar(1) - funcion.evaluar(0);
        } else if (t == n - 1) {
            return funcion.evaluar(n - 1) - funcion.evaluar(n - 2);
        } else {
            return (funcion.evaluar(t + 1) - funcion.evaluar(t - 1)) / 2.0;
        }
    }

    /*
     * Calcula la derivada en todos los puntos del historial.
     * Este arreglo es el que se grafica en la UI.
     */
    public double[] todasLasDerivadas() {
        int n = funcion.getTamanio();
        double[] derivadas = new double[n];
        for (int i = 0; i < n; i++) {
            derivadas[i] = derivadaEn(i);
        }
        return derivadas;
    }

    /*
     * Tasa de cambio promedio global.
     * Equivale a la pendiente entre el primer y último punto.
     * Si es negativa: el inventario cae en general.
     * Si es positiva: el inventario sube en general.
     */
    public double derivadaPromedio() {
        int n = funcion.getTamanio();
        if (n < 2) {
            return 0;
        }
        return (funcion.evaluar(n - 1) - funcion.evaluar(0)) / (n - 1.0);
    }

    /*
     * Límite de tendencia: proyecta hacia dónde va el stock.
     * Toma los últimos k puntos (máximo 5), aplica regresión lineal,
     * y evalúa la recta resultante en t + pasosAdelante.
     * 
     * Esto representa: si la tendencia reciente continúa,
     * ¿hacia qué valor tiende el inventario?
     */
    public double limiteTendencia(int pasosAdelante) {
        int n = funcion.getTamanio();
        if (n < 2) {
            return funcion.evaluar(0);
        }

        int k = Math.min(5, n);
        int inicio = n - k;

        double sumaX = 0, sumaY = 0, sumaXY = 0, sumaX2 = 0;

        for (int i = 0; i < k; i++) {
            double x = inicio + i;
            double y = funcion.evaluar(inicio + i);
            sumaX += x;
            sumaY += y;
            sumaXY += x * y;
            sumaX2 += x * x;
        }

        double denominador = k * sumaX2 - sumaX * sumaX;

        // Si el denominador es 0, el stock no cambió: tendencia plana
        if (denominador == 0) {
            return funcion.evaluar(n - 1);
        }

        double pendiente = (k * sumaXY - sumaX * sumaY) / denominador;
        double intercepto = (sumaY - pendiente * sumaX) / k;

        double tFuturo = (n - 1) + pasosAdelante;
        return pendiente * tFuturo + intercepto;
    }

    /*
     * Detecta riesgo de desabastecimiento.
     * Proyecta 3 movimientos hacia adelante y verifica
     * si el stock estimado cae al nivel del stock mínimo.
     */
    public boolean hayRiesgoDesabastecimiento(int stockMinimo) {
        int n = funcion.getTamanio();
        if (n == 0) {
            return false;
        }

        // Si el stock actual ya está por debajo del mínimo → alerta inmediata
        double stockActual = funcion.evaluar(n - 1);
        if (stockActual <= stockMinimo) {
            return true;
        }

        // Si la proyección a 3 movimientos cae al mínimo → alerta
        return limiteTendencia(3) <= stockMinimo;
    }

    // Retorna el índice t donde el stock fue más bajo
    public int puntoMinimoStock() {
        int n = funcion.getTamanio();
        int tMinimo = 0;
        double minValor = funcion.evaluar(0);
        for (int i = 1; i < n; i++) {
            if (funcion.evaluar(i) < minValor) {
                minValor = funcion.evaluar(i);
                tMinimo = i;
            }
        }
        return tMinimo;
    }

    // Retorna el índice t donde el stock fue más alto
    public int puntoMaximoStock() {
        int n = funcion.getTamanio();
        int tMaximo = 0;
        double maxValor = funcion.evaluar(0);
        for (int i = 1; i < n; i++) {
            if (funcion.evaluar(i) > maxValor) {
                maxValor = funcion.evaluar(i);
                tMaximo = i;
            }
        }
        return tMaximo;
    }
}
