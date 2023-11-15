package com.thiagov2a.biblioteca.controladores;

import com.thiagov2a.biblioteca.entidades.Autor;
import com.thiagov2a.biblioteca.excepciones.MiException;
import com.thiagov2a.biblioteca.servicios.AutorServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequestMapping("/autor")
public class AutorControlador {

    @Autowired
    private AutorServicio autorServicio;

    @GetMapping("/registrar")
    public String registrar() {
        return "/autor/autor_form.html";
    }

    @PostMapping("/registro")
    public String registro(@RequestParam String nombre, ModelMap modelo) {
        try {
            autorServicio.crearAutor(nombre);

            modelo.put("exito", "El autor fue creado exitosamente");

            return "index.html";
        } catch (MiException e) {
            modelo.put("error", e.getMessage());

            return "/autor/autor_form.html";
        }
    }

    @GetMapping("/listar")
    public String listar(ModelMap modelo) {
        List<Autor> autores = autorServicio.listarAutores();

        modelo.addAttribute("autores", autores);

        return "/autor/autor_list.html";
    }

    @GetMapping("/modificar/{id}")
    public String modificar(@PathVariable String id, ModelMap modelo) {
        Autor autor = autorServicio.buscarAutor(id);

        modelo.addAttribute("autor", autor);

        return "/autor/autor_modificar.html";
    }

    @PostMapping("/modificar/{id}")
    public String modificar(@PathVariable String id, String nombre, ModelMap modelo) {
        try {
            autorServicio.modificarAutor(id, nombre);

            modelo.put("exito", "El autor fue modificado exitosamente");
            
            return "redirect:/autor/listar";
        } catch (MiException e) {
            modelo.put("error", e.getMessage());

            Autor autor = autorServicio.buscarAutor(id);
            modelo.addAttribute("autor", autor);

            return "/autor/autor_modificar.html";
        }
    }
}
