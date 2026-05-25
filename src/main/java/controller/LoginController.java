package controller;

import model.Usuario;
import service.UsuarioService;

public class LoginController {

    private UsuarioService usuarioService;

    public LoginController() {
        this.usuarioService = new UsuarioService();
    }

    // Retorna el Usuario si el login es exitoso, null si no
    public Usuario login(String nombreUsuario, String contrasena) {
        if (nombreUsuario.isEmpty() || contrasena.isEmpty()) return null;
        return usuarioService.login(nombreUsuario, contrasena);
    }

    // Retorna el Usuario recién creado si el registro es exitoso, null si ya existe
    public Usuario registrar(String nombreUsuario, String contrasena) {
        if (nombreUsuario.isEmpty() || contrasena.isEmpty()) return null;
        return usuarioService.registrar(nombreUsuario, contrasena);
    }
}