package service;

import model.Movimiento;
import model.Producto;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class PersistenciaService {

    private static final String CARPETA = "datos";

    public PersistenciaService() {
        new File(CARPETA).mkdirs();
    }

    // ==================== PRODUCTOS ====================

    public void guardarProductos(int usuarioId, ArrayList<Producto> productos) {
        String ruta = CARPETA + "/productos_" + usuarioId + ".csv";
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ruta))) {
            bw.write("id,nombre,descripcion,cantidadActual,stockMinimo");
            bw.newLine();
            for (Producto p : productos) {
                bw.write(p.getId() + "," + p.getNombre() + "," + p.getDescripcion() + ","
                        + p.getCantidadActual() + "," + p.getStockMinimo());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error guardando productos: " + e.getMessage());
        }
    }

    public ArrayList<Producto> cargarProductos(int usuarioId) {
        ArrayList<Producto> productos = new ArrayList<>();
        String ruta = CARPETA + "/productos_" + usuarioId + ".csv";
        File archivo = new File(ruta);
        if (!archivo.exists()) return productos;

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            br.readLine(); // Salta el header
            while ((linea = br.readLine()) != null) {
                String[] p = linea.split(",");
                if (p.length == 5) {
                    productos.add(new Producto(
                        Integer.parseInt(p[0]),
                        p[1], p[2],
                        Integer.parseInt(p[3]),
                        Integer.parseInt(p[4])
                    ));
                }
            }
        } catch (IOException e) {
            System.out.println("Error cargando productos: " + e.getMessage());
        }
        return productos;
    }

    // ==================== MOVIMIENTOS ====================

    public void guardarMovimientos(int usuarioId, ArrayList<Movimiento> movimientos) {
        String ruta = CARPETA + "/movimientos_" + usuarioId + ".csv";
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ruta))) {
            bw.write("id,productoId,tipo,cantidad,fecha");
            bw.newLine();
            for (Movimiento m : movimientos) {
                bw.write(m.getId() + "," + m.getProductoId() + "," + m.getTipo() + ","
                        + m.getCantidad() + "," + m.getFecha());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error guardando movimientos: " + e.getMessage());
        }
    }

    public ArrayList<Movimiento> cargarMovimientos(int usuarioId) {
        ArrayList<Movimiento> movimientos = new ArrayList<>();
        String ruta = CARPETA + "/movimientos_" + usuarioId + ".csv";
        File archivo = new File(ruta);
        if (!archivo.exists()) return movimientos;

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            br.readLine(); // Salta el header
            while ((linea = br.readLine()) != null) {
                String[] m = linea.split(",");
                if (m.length == 5) {
                    movimientos.add(new Movimiento(
                        Integer.parseInt(m[0]),
                        Integer.parseInt(m[1]),
                        Movimiento.Tipo.valueOf(m[2]),
                        Integer.parseInt(m[3]),
                        LocalDateTime.parse(m[4])
                    ));
                }
            }
        } catch (IOException e) {
            System.out.println("Error cargando movimientos: " + e.getMessage());
        }
        return movimientos;
    }
}