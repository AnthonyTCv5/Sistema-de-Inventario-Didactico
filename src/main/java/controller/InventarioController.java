package controller;

import math.AnalizadorMatematico;
import math.FuncionInventario;
import model.Inventario;
import model.Movimiento;
import model.Producto;
import model.Usuario;
import service.PersistenciaService;

import java.util.ArrayList;

public class InventarioController {

    private Inventario inventario;
    private PersistenciaService persistencia;
    private int usuarioId;

    public InventarioController(Usuario usuario) {
        this.usuarioId = usuario.getId();
        this.persistencia = new PersistenciaService();
        this.inventario = new Inventario();
        cargarDatos();
    }

    // Carga los datos del usuario desde los archivos CSV
    private void cargarDatos() {
        ArrayList<Producto> productos = persistencia.cargarProductos(usuarioId);
        ArrayList<Movimiento> movimientos = persistencia.cargarMovimientos(usuarioId);
        inventario.cargarDatos(productos, movimientos);
    }

    // Guarda todos los datos del usuario en sus archivos CSV
    private void guardarDatos() {
        persistencia.guardarProductos(usuarioId, inventario.getProductos());
        persistencia.guardarMovimientos(usuarioId, inventario.getMovimientos());
    }

    // ==================== PRODUCTOS ====================
    public void agregarProducto(String nombre, String descripcion, int cantidadInicial, int stockMinimo) {
        inventario.agregarProducto(nombre, descripcion, cantidadInicial, stockMinimo);
        guardarDatos();
    }

    public ArrayList<Producto> getProductos() {
        return inventario.getProductos();
    }

    public Producto getProductoPorId(int id) {
        return inventario.buscarProductoPorId(id);
    }

    public ArrayList<Producto> getProductosEnStockBajo() {
        return inventario.getProductosEnStockBajo();
    }

    // ==================== MOVIMIENTOS ====================
    public boolean registrarEntrada(int productoId, int cantidad) {
        boolean resultado = inventario.registrarMovimiento(productoId, Movimiento.Tipo.ENTRADA, cantidad);
        if (resultado) {
            guardarDatos();
        }
        return resultado;
    }

    public boolean registrarSalida(int productoId, int cantidad) {
        boolean resultado = inventario.registrarMovimiento(productoId, Movimiento.Tipo.SALIDA, cantidad);
        if (resultado) {
            guardarDatos();
        }
        return resultado;
    }

    public ArrayList<Movimiento> getMovimientosDeProducto(int productoId) {
        return inventario.getMovimientosDeProducto(productoId);
    }

    // ==================== ANÁLISIS MATEMÁTICO ====================
    private AnalizadorMatematico getAnalizador(int productoId) {
        ArrayList<Integer> historial = inventario.getHistorialStock(productoId);
        FuncionInventario funcion = new FuncionInventario(historial);
        return new AnalizadorMatematico(funcion);
    }

    public boolean eliminarProducto(int productoId) {
        boolean resultado = inventario.eliminarProducto(productoId);
        if (resultado) {
            guardarDatos();
        }
        return resultado;
    }

    public ArrayList<Integer> getHistorialStock(int productoId) {
        return inventario.getHistorialStock(productoId);
    }

    public double[] getDerivadas(int productoId) {
        return getAnalizador(productoId).todasLasDerivadas();
    }

    public double getDerivadaPromedio(int productoId) {
        return getAnalizador(productoId).derivadaPromedio();
    }

    public double getLimiteTendencia(int productoId, int pasosAdelante) {
        return getAnalizador(productoId).limiteTendencia(pasosAdelante);
    }

    public boolean hayRiesgoDesabastecimiento(int productoId) {
        Producto p = inventario.buscarProductoPorId(productoId);
        if (p == null) {
            return false;
        }
        return getAnalizador(productoId).hayRiesgoDesabastecimiento(p.getStockMinimo());
    }

    public int getPuntoMinimoStock(int productoId) {
        return getAnalizador(productoId).puntoMinimoStock();
    }

    public int getPuntoMaximoStock(int productoId) {
        return getAnalizador(productoId).puntoMaximoStock();
    }
}
