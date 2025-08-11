package com.aluracursos.literalura.service;

import com.aluracursos.literalura.dto.DatosBusqueda;
import com.aluracursos.literalura.dto.DatosLibro;
import com.aluracursos.literalura.model.Autor;
import com.aluracursos.literalura.model.Libro;
import com.aluracursos.literalura.repository.AutorRepository;
import com.aluracursos.literalura.repository.LibroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.Scanner;

@Service
public class LibroService {

    private final String URL_BASE = "https://gutendex.com/books/";
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private IConvierteDatos conversor = new ConvierteDatos();
    private Scanner teclado = new Scanner(System.in);

    @Autowired
    private LibroRepository libroRepository;
    @Autowired
    private AutorRepository autorRepository;

    private String limpiarCadena(String cadena) {
        String cadenaNormalizada = Normalizer.normalize(cadena, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(cadenaNormalizada).replaceAll("").toLowerCase();
    }

    public void buscarYGuardarLibro(String tituloLibro) {
        String tituloBusqueda = tituloLibro.replace(" ", "%20");
        String urlBusqueda = URL_BASE + "?search=" + tituloBusqueda;

        var json = consumoAPI.obtenerDatos(urlBusqueda);
        DatosBusqueda datosBusqueda = conversor.obtenerDatos(json, DatosBusqueda.class);

        Optional<DatosLibro> libroEncontrado = Optional.empty();

        if (datosBusqueda != null && datosBusqueda.resultados() != null && !datosBusqueda.resultados().isEmpty()) {
            libroEncontrado = datosBusqueda.resultados().stream()
                    .filter(l -> limpiarCadena(l.titulo()).contains(limpiarCadena(tituloLibro)))
                    .findFirst();
        }

        if (libroEncontrado.isEmpty()) {
            String[] palabras = tituloLibro.split(" ");
            if (palabras.length > 0) {
                String ultimaPalabra = palabras[palabras.length - 1];
                urlBusqueda = URL_BASE + "?search=" + limpiarCadena(ultimaPalabra);

                json = consumoAPI.obtenerDatos(urlBusqueda);
                datosBusqueda = conversor.obtenerDatos(json, DatosBusqueda.class);

                if (datosBusqueda != null && datosBusqueda.resultados() != null) {
                    libroEncontrado = datosBusqueda.resultados().stream()
                            .filter(l -> limpiarCadena(l.titulo()).contains(limpiarCadena(tituloLibro)))
                            .findFirst();
                }
            }
        }

        if (libroEncontrado.isPresent()) {
            DatosLibro datosLibro = libroEncontrado.get();

            Autor autor = new Autor();
            if (datosLibro.autor() != null && !datosLibro.autor().isEmpty()) {
                autor.setNombre(datosLibro.autor().get(0).nombre());
                autor.setFechaNacimiento(datosLibro.autor().get(0).fechaNacimiento());
                autor.setFechaFallecimiento(datosLibro.autor().get(0).fechaFallecimiento());
            }

            Libro libro = new Libro();
            libro.setTitulo(datosLibro.titulo());
            libro.setIdioma(String.join(",", datosLibro.idiomas()));
            libro.setNumeroDeDescargas(datosLibro.numeroDeDescargas());
            libro.setAutor(autor);

            autorRepository.save(autor);
            libroRepository.save(libro);

            System.out.println("Libro guardado exitosamente: " + libro.getTitulo());
        } else {
            // Mensaje de error corregido
            System.out.println("\nNo se encontró ninguna coincidencia para el título '" + tituloLibro + "'.");
            System.out.println("Te recomendamos intentar la opción 6 para buscar por autor, o buscar un título más genérico.");
        }
    }

    public void listarLibrosRegistrados() {
        List<Libro> libros = libroRepository.findAll();
        libros.forEach(libro -> System.out.println(
                "Título: " + libro.getTitulo() +
                        ", Autor: " + libro.getAutor().getNombre() +
                        ", Idioma: " + libro.getIdioma()
        ));
    }

    public void listarAutoresRegistrados() {
        List<Autor> autores = autorRepository.findAll();
        autores.forEach(autor -> System.out.println(
                "Autor: " + autor.getNombre() +
                        ", Fecha de Nacimiento: " + autor.getFechaNacimiento() +
                        ", Fecha de Fallecimiento: " + autor.getFechaFallecimiento()
        ));
    }

    public void listarAutoresVivosPorAnio(Integer anio) {
        List<Autor> autoresVivos = autorRepository.buscarAutoresVivosPorAnio(anio);
        if (autoresVivos.isEmpty()) {
            System.out.println("No se encontraron autores vivos en ese año.");
        } else {
            autoresVivos.forEach(autor -> System.out.println("Autor: " + autor.getNombre()));
        }
    }

    public void buscarAutorYLibros(String nombreAutor) {
        String urlBusqueda = URL_BASE + "?search=" + nombreAutor.replace(" ", "%20");
        var json = consumoAPI.obtenerDatos(urlBusqueda);
        DatosBusqueda datosBusqueda = conversor.obtenerDatos(json, DatosBusqueda.class);

        if (datosBusqueda != null && datosBusqueda.resultados() != null && !datosBusqueda.resultados().isEmpty()) {
            System.out.println("Se encontraron los siguientes libros para el autor '" + nombreAutor + "':");
            datosBusqueda.resultados().forEach(libro -> System.out.println("Título: " + libro.titulo()));
        } else {
            System.out.println("No se encontraron libros para el autor '" + nombreAutor + "'.");
        }
    }

    public void listarLibrosPorIdioma() {
        System.out.println("Ingrese el código de idioma (ej. 'es', 'en', 'fr', 'pt'):");
        var idioma = teclado.nextLine();

        List<Libro> librosPorIdioma = libroRepository.findByIdiomaContaining(idioma);

        if (librosPorIdioma.isEmpty()) {
            System.out.println("No se encontraron libros en el idioma '" + idioma + "'.");
        } else {
            System.out.println("Libros encontrados en el idioma '" + idioma + "':");
            librosPorIdioma.forEach(libro -> System.out.println(
                    "Título: " + libro.getTitulo() +
                            ", Autor: " + libro.getAutor().getNombre()
            ));
        }
    }
}