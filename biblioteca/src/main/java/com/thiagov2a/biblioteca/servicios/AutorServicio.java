package com.thiagov2a.biblioteca.servicios;

import com.thiagov2a.biblioteca.entidades.Autor;
import com.thiagov2a.biblioteca.excepciones.MiException;
import com.thiagov2a.biblioteca.repositorios.AutorRepositorio;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AutorServicio {

    @Autowired
    private AutorRepositorio autorRepositorio;

    @Transactional
    public void crearAutor(String nombre) throws MiException {

        validar(nombre);
        validarExistencia(nombre);

        Autor autor = new Autor();
        autor.setNombre(nombre);
        autorRepositorio.save(autor);
    }

    public List<Autor> listarAutores() {

        List<Autor> autores = new ArrayList<>();
        autores = autorRepositorio.findAll();
        return autores;
    }

    public Autor buscarAutor(String id) {

        Optional<Autor> respuesta = autorRepositorio.findById(id);

        if (respuesta.isPresent()) {
            return respuesta.get();
        } else {
            return null;
        }
    }

    @Transactional
    public void modificarAutor(String id, String nombre) throws MiException {

        validar(nombre);

        Optional<Autor> respuesta = autorRepositorio.findById(id);

        if (respuesta.isPresent()) {
            Autor autor = respuesta.get();
            autor.setNombre(nombre);
            autorRepositorio.save(autor);
        }
    }

    private void validar(String nombre) throws MiException {

        if (nombre == null || nombre.trim().isEmpty()) {
            throw new MiException("El nombre del autor no puede ser nulo o estar vacío");
        }
    }

    private void validarExistencia(String nombre) throws MiException {

        Autor respuestaAutor = autorRepositorio.buscarPorNombre(nombre.trim());

        if (respuestaAutor != null) {
            throw new MiException("Ya existe un autor con ese nombre");
        }
    }

}
