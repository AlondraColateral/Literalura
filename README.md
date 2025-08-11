Literalura - Catálogo de Libros y Autores
📝 Descripción del Proyecto
Literalura es una aplicación de consola desarrollada con Spring Boot que permite a los usuarios interactuar con la API de Gutendex para buscar y gestionar información sobre libros y autores. La aplicación permite buscar libros por título, listar libros y autores registrados, buscar autores vivos en un año específico y listar libros por idioma.

✨ Funcionalidades
El menú interactivo de la aplicación ofrece las siguientes opciones:

Buscar libro por título: Busca un libro en la API Gutendex y lo guarda en la base de datos local.

Listar todos los libros: Muestra los libros que han sido registrados en la base de datos.

Listar autores: Muestra una lista de todos los autores de los libros registrados.

Listar autores vivos en un determinado año: Permite filtrar y mostrar los autores que estaban vivos en un año específico.

Listar libros por idioma: Muestra los libros registrados en la base de datos que coinciden con un código de idioma (ej. es, en, fr).

Buscar autor por nombre: Realiza una búsqueda más precisa en la API por el nombre del autor.

Salir: Cierra la aplicación.

🚀 Tecnologías Utilizadas
Java 17

Spring Boot 3.2.0

Maven

API Gutendex

Bases de Datos relacionales (configuradas a través de Spring Data JPA)

🛠️ Cómo Ejecutar el Proyecto
Requisitos
Java Development Kit (JDK) 17 o superior.

Maven (generalmente ya incluido en entornos de desarrollo como IntelliJ).



⚠️ Notas Importantes
La API de Gutendex puede ser inconsistente y a veces no devuelve resultados para títulos de libros muy conocidos (como "El Principito" o "Harry Potter"). Para estos casos, se recomienda utilizar la opción 6 - Buscar autor por nombre como una alternativa más confiable.
