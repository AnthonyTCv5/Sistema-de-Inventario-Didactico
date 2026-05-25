package model;

import java.util.ArrayList;

public class Inventario {

    private ArrayList<Producto> productos;
    private ArrayList<Movimiento> movimientos;
    private int contadorProductos;
    private int contadorMovimientos;

    public Inventario() {
        this.productos = new ArrayList<>();
        this.movimientos = new ArrayList<>();
        this.contadorProductos = 1;
        this.contadorMovimientos = 1;
    }

    // Agrega un producto nuevo al inventario
    public void agregarProducto(String nombre, String descripcion, int cantidadInicial, int stockMinimo) {
        Producto p = new Producto(contadorProductos++, nombre, descripcion, cantidadInicial, stockMinimo);
        productos.add(p);
    }

    // Registra una entrada o salida. Retorna false si la operación no es válida
    public boolean registrarMovimiento(int productoId, Movimiento.Tipo tipo, int cantidad) {
        Producto producto = buscarProductoPorId(productoId);
        if (producto == null) {
            return false;
        }

        if (tipo == Movimiento.Tipo.SALIDA && producto.getCantidadActual() < cantidad) {
            return false; // No hay stock suficiente
        }

        Movimiento m = new Movimiento(contadorMovimientos++, productoId, tipo, cantidad);
        movimientos.add(m);

        if (tipo == Movimiento.Tipo.ENTRADA) {
            producto.setCantidadActual(producto.getCantidadActual() + cantidad);
        } else {
            producto.setCantidadActual(producto.getCantidadActual() - cantidad);
        }

        return true;
    }

    // Busca un producto por su ID
    public Producto buscarProductoPorId(int id) {
        for (Producto p : productos) {
            if (p.getId() == id) {
                return p;
            }
        }
        return null;
    }

    // Retorna todos los movimientos de un producto específico
    public ArrayList<Movimiento> getMovimientosDeProducto(int productoId) {
        ArrayList<Movimiento> resultado = new ArrayList<>();
        for (Movimiento m : movimientos) {
            if (m.getProductoId() == productoId) {
                resultado.add(m);
            }
        }
        return resultado;
    }

    /*
     * Reconstruye el historial de stock de un producto a lo largo del tiempo.
     * Este método es el que alimenta el módulo matemático.
     * Retorna una lista donde cada posición representa el stock
     * después de cada movimiento registrado.
     */
    public ArrayList<Integer> getHistorialStock(int productoId) {
        Producto producto = buscarProductoPorId(productoId);
        if (producto == null) {
            return new ArrayList<>();
        }

        ArrayList<Movimiento> movs = getMovimientosDeProducto(productoId);
        ArrayList<Integer> historial = new ArrayList<>();

        // Reconstruye el stock inicial desde el estado actual
        int totalEntradas = 0;
        int totalSalidas = 0;
        for (Movimiento m : movs) {
            if (m.getTipo() == Movimiento.Tipo.ENTRADA) {
                totalEntradas += m.getCantidad();
            } else {
                totalSalidas += m.getCantidad();
            }
        }
        int stockInicial = producto.getCantidadActual() + totalSalidas - totalEntradas;
        historial.add(stockInicial);

        // Reproduce cada movimiento para construir el historial completo
        int stockAcumulado = stockInicial;
        for (Movimiento m : movs) {
            if (m.getTipo() == Movimiento.Tipo.ENTRADA) {
                stockAcumulado += m.getCantidad();
            } else {
                stockAcumulado -= m.getCantidad();
            }
            historial.add(stockAcumulado);
        }

        return historial;
    }

    // Retorna productos que están por debajo del stock mínimo
    public ArrayList<Producto> getProductosEnStockBajo() {
        ArrayList<Producto> resultado = new ArrayList<>();
        for (Producto p : productos) {
            if (p.getCantidadActual() <= p.getStockMinimo()) {
                resultado.add(p);
            }
        }
        return resultado;
    }

    public ArrayList<Producto> getProductos() {
        return productos;
    }

    public ArrayList<Movimiento> getMovimientos() {
        return movimientos;
    }

    public void cargarDatos(ArrayList<Producto> productos, ArrayList<Movimiento> movimientos) {
        this.productos = productos;
        this.movimientos = movimientos;

        // Recalcula los contadores para evitar IDs duplicados
        for (Producto p : productos) {
            if (p.getId() >= contadorProductos) {
                contadorProductos = p.getId() + 1;
            }
        }
        for (Movimiento m : movimientos) {
            if (m.getId() >= contadorMovimientos) {
                contadorMovimientos = m.getId() + 1;
            }
        }

    }

    public boolean eliminarProducto(int productoId) {
        Producto producto = buscarProductoPorId(productoId);
        if (producto == null) {
            return false;
        }

        // Elimina el producto
        productos.removeIf(p -> p.getId() == productoId);

        // Elimina todos sus movimientos
        movimientos.removeIf(m -> m.getProductoId() == productoId);

        return true;
    }

}
