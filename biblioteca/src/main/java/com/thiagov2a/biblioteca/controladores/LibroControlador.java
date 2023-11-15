package com.thiagov2a.biblioteca.controladores;

import com.thiagov2a.biblioteca.entidades.Autor;
import com.thiagov2a.biblioteca.entidades.Editorial;
import com.thiagov2a.biblioteca.entidades.Libro;
import com.thiagov2a.biblioteca.excepciones.MiException;
import com.thiagov2a.biblioteca.servicios.AutorServicio;
import com.thiagov2a.biblioteca.servicios.EditorialServicio;
import com.thiagov2a.biblioteca.servicios.LibroServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/libro")
public class LibroControlador {

    @Autowired
    private LibroServicio libroServicio;

    @Autowired
    private AutorServicio autorServicio;

    @Autowired
    private EditorialServicio editorialServicio;

    @GetMapping("/registrar")
    public String registrar(ModelMap modelo) {
        List<Autor> autores = autorServicio.listarAutores();
        List<Editorial> editoriales = editorialServicio.listarEditoriales();

        modelo.addAttribute("autores", autores);
        modelo.addAttribute("editoriales", editoriales);

        return "/libro/libro_form.html";
    }

    @PostMapping("/registro")
    public String registro(
            @RequestParam(required = false) Long isbn,
            @RequestParam(required = false) String titulo,
            @RequestParam(required = false) Integer ejemplares,
            @RequestParam(required = false) String idAutor,
            @RequestParam(required = false) String idEditorial,
            ModelMap modelo) {
        try {
            libroServicio.crearLibro(isbn, titulo, ejemplares, idAutor, idEditorial);

            modelo.put("exito", "El libro fue creado exitosamente");

            return "index.html";
        } catch (MiException e) {
            modelo.put("error", e.getMessage());

            List<Autor> autores = autorServicio.listarAutores();
            List<Editorial> editoriales = editorialServicio.listarEditoriales();

            modelo.addAttribute("autores", autores);
            modelo.addAttribute("editoriales", editoriales);

            return "/libro/libro_form.html";
        }
    }

    @GetMapping("/listar")
    public String listar(ModelMap modelo) {
        List<Libro> libros = libroServicio.listarLibros();

        modelo.addAttribute("libros", libros);

        return "/libro/libro_list.html";
    }

    @GetMapping("/modificar/{isbn}")
    public String modificar(@PathVariable Long isbn, ModelMap modelo) {
        Libro libro = libroServicio.buscarLibroPorIsbn(isbn);
        modelo.addAttribute("libro", libro);

        List<Autor> autores = autorServicio.listarAutores();
        List<Editorial> editoriales = editorialServicio.listarEditoriales();

        modelo.addAttribute("autores", autores);
        modelo.addAttribute("editoriales", editoriales);

        return "/libro/libro_modificar.html";
    }

    @PostMapping("/modificar/{isbn}")
    public String modificar(
            @PathVariable Long isbn,
            String titulo,
            Integer ejemplares,
            String idAutor,
            String idEditorial,
            ModelMap modelo,
            RedirectAttributes attributes) {
        try {
            List<Autor> autores = autorServicio.listarAutores();
            List<Editorial> editoriales = editorialServicio.listarEditoriales();

            modelo.addAttribute("autores", autores);
            modelo.addAttribute("editoriales", editoriales);

            libroServicio.modificarLibro(isbn, titulo, ejemplares, idAutor, idEditorial);

            attributes.addFlashAttribute("exito", "El libro fue modificado exitosamente");

            return "redirect:/libro/listar";
        } catch (MiException e) {
            modelo.put("error", e.getMessage());

            Libro libro = libroServicio.buscarLibroPorIsbn(isbn);
            modelo.addAttribute("libro", libro);

            List<Autor> autores = autorServicio.listarAutores();
            List<Editorial> editoriales = editorialServicio.listarEditoriales();

            modelo.addAttribute("autores", autores);
            modelo.addAttribute("editoriales", editoriales);

            return "/libro/libro_modificar.html";
        }
    }
}
