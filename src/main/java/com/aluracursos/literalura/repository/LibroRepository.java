package com.aluracursos.literalura.repository;

import com.aluracursos.literalura.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LibroRepository extends JpaRepository<Libro, Long> {
    Libro findByTituloContainsIgnoreCase(String titulo);
    List<Libro> findByIdioma(String idioma);

    List<Libro> findByIdiomaContaining(String idioma);
}
