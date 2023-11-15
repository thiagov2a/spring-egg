package com.thiagov2a.biblioteca.repositorios;

import com.thiagov2a.biblioteca.entidades.Libro;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LibroRepositorio extends JpaRepository<Libro, Long> {

    @Query("SELECT l FROM Libro l WHERE l.titulo = :titulo")
    public Libro buscarPorTitulo(@Param("titulo") String titulo);

    @Query("SELECT l FROM Libro l WHERE l.autor.nombre = :nombre")
    public List<Libro> buscarPorAutor(@Param("nombre") String nombre);

    @Query("SELECT l FROM Libro l WHERE l.editorial.nombre = :nombre")
    public List<Libro> buscarPorEditorial(@Param("nombre") String nombre);

    @Query("SELECT l FROM Libro l WHERE l.titulo = :titulo AND l.autor.id = :idAutor AND l.editorial.id = :idEditorial")
    public Libro buscarExistencia(@Param("titulo") String titulo, @Param("idAutor") String idAutor,
            @Param("idEditorial") String idEditorial);

}
