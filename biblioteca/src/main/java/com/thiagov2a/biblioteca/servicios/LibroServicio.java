package com.thiagov2a.biblioteca.servicios;

import com.thiagov2a.biblioteca.entidades.Autor;
import com.thiagov2a.biblioteca.entidades.Editorial;
import com.thiagov2a.biblioteca.entidades.Libro;
import com.thiagov2a.biblioteca.excepciones.MiException;
import com.thiagov2a.biblioteca.repositorios.AutorRepositorio;
import com.thiagov2a.biblioteca.repositorios.EditorialRepositorio;
import com.thiagov2a.biblioteca.repositorios.LibroRepositorio;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LibroServicio {

    @Autowired
    private LibroRepositorio libroRepositorio;

    @Autowired
    private AutorRepositorio autorRepositorio;

    @Autowired
    private EditorialRepositorio editorialRepositorio;

    @Transactional
    public void crearLibro(
            Long isbn,
            String titulo,
            Integer ejemplares,
            String idAutor,
            String idEditorial)
            throws MiException {

        validar(isbn, titulo, ejemplares, idAutor, idEditorial);
        validarExistencia(isbn, titulo, idAutor, idEditorial);

        Autor autor = autorRepositorio.findById(idAutor).get();
        Editorial editorial = editorialRepositorio.findById(idEditorial).get();

        Libro libro = new Libro();

        libro.setIsbn(isbn);
        libro.setTitulo(titulo);
        libro.setEjemplares(ejemplares);
        libro.setAlta(new Date());
        libro.setAutor(autor);
        libro.setEditorial(editorial);

        libroRepositorio.save(libro);
    }

    public List<Libro> listarLibros() {

        List<Libro> libros = new ArrayList<>();
        libros = libroRepositorio.findAll();
        return libros;
    }

    public Libro buscarLibroPorIsbn(Long isbn) {

        Optional<Libro> respuesta = libroRepositorio.findById(isbn);

        if (respuesta.isPresent()) {
            return respuesta.get();
        } else {
            return null;
        }
    }

    @Transactional
    public void modificarLibro(
            Long isbn,
            String titulo,
            Integer ejemplares,
            String idAutor,
            String idEditorial)
            throws MiException {

        validar(isbn, titulo, ejemplares, idAutor, idEditorial);

        Optional<Libro> respuesta = libroRepositorio.findById(isbn);
        Optional<Autor> respuestaAutor = autorRepositorio.findById(idAutor);
        Optional<Editorial> respuestaEditorial = editorialRepositorio.findById(idEditorial);

        Autor autor = new Autor();
        Editorial editorial = new Editorial();

        if (respuestaAutor.isPresent()) {
            autor = respuestaAutor.get();
        }

        if (respuestaEditorial.isPresent()) {
            editorial = respuestaEditorial.get();
        }

        if (respuesta.isPresent()) {
            Libro libro = respuesta.get();

            libro.setTitulo(titulo);
            libro.setEjemplares(ejemplares);
            libro.setAutor(autor);
            libro.setEditorial(editorial);

            libroRepositorio.save(libro);
        }
    }

    private void validar(Long isbn, String titulo, Integer ejemplares, String idAutor, String idEditorial)
            throws MiException {

        if (isbn == null) {
            throw new MiException("El ISBN no puede ser nulo");
        }

        if (titulo == null || titulo.trim().isEmpty()) {
            throw new MiException("El título no puede ser nulo o estar vacío");
        }

        if (ejemplares == null) {
            throw new MiException("Los ejemplares no puede ser nulo");
        }

        if (idAutor == null || idAutor.trim().isEmpty()) {
            throw new MiException("El Autor no puede ser nulo o estar vacío");
        }

        if (idEditorial == null || idEditorial.trim().isEmpty()) {
            throw new MiException("La Editorial no puede ser nula o estar vacía");
        }
    }

    private void validarExistencia(Long isbn, String titulo, String idAutor, String idEditorial)
            throws MiException {

        Libro respuestaLibro = libroRepositorio.buscarExistencia(titulo.trim(), idAutor.trim(), idEditorial.trim());
        if (respuestaLibro != null) {
            throw new MiException("Ya existe un libro con ese título, autor y editorial");
        }

        Libro respuestaLibro2 = libroRepositorio.buscarPorTitulo(titulo.trim());
        if (respuestaLibro2 != null) {
            throw new MiException("Ya existe un libro con ese título");
        }

        Optional<Libro> respuestaLibro3 = libroRepositorio.findById(isbn);
        if (respuestaLibro3.isPresent()) {
            throw new MiException("Ya existe un libro con ese ISBN");
        }
    }

}
