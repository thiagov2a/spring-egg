package com.thiagov2a.biblioteca.controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.thiagov2a.biblioteca.entidades.Editorial;
import com.thiagov2a.biblioteca.excepciones.MiException;
import com.thiagov2a.biblioteca.servicios.EditorialServicio;
import java.util.List;

@Controller
@RequestMapping("/editorial")
public class EditorialControlador {

    @Autowired
    private EditorialServicio editorialServicio;

    @GetMapping("/registrar")
    public String registrar() {
        return "/editorial/editorial_form.html";
    }

    @PostMapping("/registro")
    public String registro(@RequestParam String nombre, ModelMap modelo) {
        try {
            editorialServicio.crearEditorial(nombre);

            modelo.put("exito", "La editorial fue creada exitosamente");

            return "index.html";
        } catch (MiException e) {
            modelo.put("error", e.getMessage());

            return "/editorial/editorial_form.html";
        }
    }

    @GetMapping("/listar")
    public String listar(ModelMap modelo) {
        List<Editorial> editoriales = editorialServicio.listarEditoriales();

        modelo.addAttribute("editoriales", editoriales);

        return "/editorial/editorial_list.html";
    }

    @GetMapping("/modificar/{id}")
    public String modificar(@PathVariable String id, ModelMap modelo) {
        Editorial editorial = editorialServicio.buscarEditorial(id);

        modelo.addAttribute("editorial", editorial);
        
        return "/editorial/editorial_modificar.html";
    }

    @PostMapping("/modificar/{id}")
    public String modificar(@PathVariable String id, String nombre, ModelMap modelo) {
        try {
            editorialServicio.modificarEditorial(id, nombre);

            modelo.put("exito", "La editorial fue modificada exitosamente");
            
            return "redirect:/editorial/listar";
        } catch (MiException e) {
            modelo.put("error", e.getMessage());

            Editorial editorial = editorialServicio.buscarEditorial(id);
            modelo.addAttribute("editorial", editorial);

            return "/editorial/editorial_modificar.html";
        }
    }

}
