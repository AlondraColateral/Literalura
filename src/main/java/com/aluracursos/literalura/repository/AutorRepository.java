package com.aluracursos.literalura.repository;

import com.aluracursos.literalura.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AutorRepository extends JpaRepository<Autor, Long> {

    @Query("SELECT a FROM Autor a WHERE a.fechaNacimiento <= :anio AND a.fechaFallecimiento >= :anio")
    List<Autor> buscarAutoresVivosPorAnio(Integer anio);

    // Nuevo método para buscar autores que contengan un nombre, sin importar mayúsculas
    List<Autor> findByNombreContainingIgnoreCase(String nombreAutor);
}
