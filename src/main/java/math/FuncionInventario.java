/*
Por Anthony Peñaloza Díaz
*/

package math;

import java.util.ArrayList;

public class FuncionInventario {

    private double[] valores;

    /*
     * Recibe el historial de stock del Inventario.
     * Cada posición representa I(t):
     * t=0 → stock inicial
     * t=1 → stock después del primer movimiento
     * t=n → stock después del n-ésimo movimiento
     */
    
    public FuncionInventario(ArrayList<Integer> historial) {
        this.valores = new double[historial.size()];
        for (int i = 0; i < historial.size(); i++) {
            this.valores[i] = historial.get(i);
        }
    }

    // Evalúa I(t) en un punto dado
    public double evaluar(int t) {
        if (t < 0 || t >= valores.length) return -1;
        return valores[t];
    }

    // Retorna todos los valores de I(t)
    public double[] getValores() {
        return valores;
    }

    // Cantidad de puntos en la función
    public int getTamanio() {
        return valores.length;
    }
}