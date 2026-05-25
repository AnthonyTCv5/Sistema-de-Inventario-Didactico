package service;

import model.Usuario;
import java.io.*;
import java.util.ArrayList;

public class UsuarioService {

    private static final String ARCHIVO = "usuarios.csv";
    private ArrayList<Usuario> usuarios;
    private int contadorId;

    public UsuarioService() {
        this.usuarios = new ArrayList<>();
        this.contadorId = 1;
        cargarDesdeArchivo();
    }

    // Registra un usuario nuevo. Retorna null si el usuario ya existe
    public Usuario registrar(String nombreUsuario, String contrasena) {
        if (buscarPorNombre(nombreUsuario) != null) return null;
        Usuario nuevo = new Usuario(contadorId++, nombreUsuario, contrasena);
        usuarios.add(nuevo);
        guardarEnArchivo();
        return nuevo;
    }

    // Valida credenciales. Retorna el Usuario si es correcto, null si no
    public Usuario login(String nombreUsuario, String contrasena) {
        Usuario u = buscarPorNombre(nombreUsuario);
        if (u == null) return null;
        if (!u.getContrasena().equals(contrasena)) return null;
        return u;
    }

    private Usuario buscarPorNombre(String nombreUsuario) {
        for (Usuario u : usuarios) {
            if (u.getNombreUsuario().equals(nombreUsuario)) return u;
        }
        return null;
    }

    private void guardarEnArchivo() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ARCHIVO))) {
            bw.write("id,nombreUsuario,contrasena");
            bw.newLine();
            for (Usuario u : usuarios) {
                bw.write(u.getId() + "," + u.getNombreUsuario() + "," + u.getContrasena());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error guardando usuarios: " + e.getMessage());
        }
    }

    private void cargarDesdeArchivo() {
        File archivo = new File(ARCHIVO);
        if (!archivo.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            br.readLine(); // Salta el header
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(",");
                if (partes.length == 3) {
                    int id = Integer.parseInt(partes[0]);
                    usuarios.add(new Usuario(id, partes[1], partes[2]));
                    if (id >= contadorId) contadorId = id + 1;
                }
            }
        } catch (IOException e) {
            System.out.println("Error cargando usuarios: " + e.getMessage());
        }
    }
}