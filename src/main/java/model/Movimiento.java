/*
Por Anthony Peñaloza Díaz
 */
package model;

import java.time.LocalDateTime;

public class Movimiento {

    public enum Tipo {
        ENTRADA, SALIDA
    }

    private int id;
    private int productoId;
    private Tipo tipo;
    private int cantidad;
    private LocalDateTime fecha;

    public Movimiento(int id, int productoId, Tipo tipo, int cantidad) {
        this.id = id;
        this.productoId = productoId;
        this.tipo = tipo;
        this.cantidad = cantidad;
        this.fecha = LocalDateTime.now();
    }

    public Movimiento(int id, int productoId, Tipo tipo, int cantidad, LocalDateTime fecha) {
        this.id = id;
        this.productoId = productoId;
        this.tipo = tipo;
        this.cantidad = cantidad;
        this.fecha = fecha;
    }

    public int getId() {
        return id;
    }

    public int getProductoId() {
        return productoId;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public int getCantidad() {
        return cantidad;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    @Override
    public String toString() {
        return tipo + " - " + cantidad + " unidades (" + fecha.toLocalDate() + ")";
    }
}
