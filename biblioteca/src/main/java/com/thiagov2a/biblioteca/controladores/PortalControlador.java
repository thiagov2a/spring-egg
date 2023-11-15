package com.thiagov2a.biblioteca.controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.thiagov2a.biblioteca.entidades.Usuario;
import com.thiagov2a.biblioteca.excepciones.MiException;
import com.thiagov2a.biblioteca.servicios.UsuarioServicio;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/")
public class PortalControlador {

    @Autowired
    UsuarioServicio usuarioServicio;

    @GetMapping("/")
    public String index(HttpSession session) {

        Usuario usuario = (Usuario) session.getAttribute("usuariosession");

        if (usuario != null && usuario.getRol().toString().equals("ADMIN")) {
            return "redirect:/admin/dashboard";
        }

        return "index.html";
    }

    @GetMapping("/registrar")
    public String registrar() {
        return "registro.html";
    }

    @PostMapping("/registro")
    public String registrar(@RequestParam String nombre, @RequestParam String email, @RequestParam String password,
            String password2, ModelMap modelo) {
        try {
            usuarioServicio.registrar(nombre, email, password, password2);

            modelo.put("exito", "Te has registrado correctamente");

            return "index.html";
        } catch (MiException e) {
            modelo.put("error", e.getMessage());

            modelo.put("nombre", nombre);
            modelo.put("email", email);

            return "registro.html";
        }
    }

    @GetMapping("/login")
    public String login(@RequestParam(required = false) String error, ModelMap modelo) {
        if (error != null) {
            modelo.put("error", "Usuario o contraseña incorrectos");
        }
        return "login.html";
    }

}
