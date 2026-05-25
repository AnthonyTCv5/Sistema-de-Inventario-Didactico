/*
Por Anthony Peñaloza Díaz
*/

package model;

public class Producto {

    private int id;
    private String nombre;
    private String descripcion;
    private int cantidadActual;
    private int stockMinimo;

    public Producto(int id, String nombre, String descripcion, int cantidadActual, int stockMinimo) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.cantidadActual = cantidadActual;
        this.stockMinimo = stockMinimo;
    }

    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public int getCantidadActual() { return cantidadActual; }
    public int getStockMinimo() { return stockMinimo; }

    public void setCantidadActual(int cantidadActual) {
        this.cantidadActual = cantidadActual;
    }

    @Override
    public String toString() {
        return nombre;
    }
}