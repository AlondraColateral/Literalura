package com.aluracursos.literalura.principal;

import com.aluracursos.literalura.service.LibroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.InputMismatchException;
import java.util.Scanner;

@Component
public class Principal {
    private Scanner teclado = new Scanner(System.in);
    @Autowired
    private LibroService libroService;

    public void muestraElMenu() {
        var opcion = -1;
        while (opcion != 0) {
            var menu = """
                    1 - Buscar libro por titulo
                    2 - Listar todos los libros
                    3 - Listar autores
                    4 - Listar autores vivos en un determinado año
                    5 - Listar libros por idioma
                    6 - Buscar autor por nombre
                    0 - Salir
                    """;
            System.out.println(menu);
            try {
                opcion = teclado.nextInt();
                teclado.nextLine();
            } catch (InputMismatchException e) {
                System.out.println("Opción inválida. Por favor, ingrese un número.");
                teclado.nextLine();
                continue;
            }

            switch (opcion) {
                case 1:
                    System.out.println("Escribe el nombre del libro que deseas buscar");
                    var tituloLibro = teclado.nextLine();
                    libroService.buscarYGuardarLibro(tituloLibro);
                    break;
                case 2:
                    libroService.listarLibrosRegistrados();
                    break;
                case 3:
                    libroService.listarAutoresRegistrados();
                    break;
                case 4:
                    System.out.println("Ingrese el año para buscar autores vivos");
                    var anio = teclado.nextInt();
                    teclado.nextLine();
                    libroService.listarAutoresVivosPorAnio(anio);
                    break;
                case 5:
                    libroService.listarLibrosPorIdioma();
                    break;
                case 6:
                    System.out.println("Escribe el nombre del autor que deseas buscar");
                    var nombreAutor = teclado.nextLine();
                    libroService.buscarAutorYLibros(nombreAutor);
                    break;
                case 0:
                    System.out.println("Cerrando la aplicación...");
                    break;
                default:
                    System.out.println("Opción inválida.");
                    break;
            }
        }
    }
}
