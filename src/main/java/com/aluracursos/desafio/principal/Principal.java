package com.aluracursos.desafio.principal;

import com.aluracursos.desafio.model.Autor;
import com.aluracursos.desafio.model.Datos;
import com.aluracursos.desafio.model.DatosAutor;
import com.aluracursos.desafio.model.DatosLibros;
import com.aluracursos.desafio.model.Libro;
import com.aluracursos.desafio.repository.AutorRepository;
import com.aluracursos.desafio.repository.LibroRepository;
import com.aluracursos.desafio.service.ConsumoAPI;
import com.aluracursos.desafio.service.ConvierteDatos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

@Component
public class Principal {
    private static final String URL_BASE = "https://gutendex.com/books/";

    @Autowired
    private ConsumoAPI consumoAPI;

    @Autowired
    private ConvierteDatos conversor;

    @Autowired
    private AutorRepository autorRepository;

    @Autowired
    private LibroRepository libroRepository;

    private final Scanner teclado = new Scanner(System.in);
    private boolean ejecutando = true;

    public void muestraElMenu() {
        while (ejecutando) {
            try {
                mostrarOpciones();
                procesarOpcion(leerOpcion());
            } catch (Exception e) {
                System.out.println("Error al procesar la opción: " + e.getMessage());
                teclado.nextLine(); // Limpiar el buffer
            }
        }
    }

    private void mostrarOpciones() {
        System.out.println("\n╔══════════════════════════════════════════════╗");
        System.out.println("║        📚 SISTEMA DE GESTIÓN DE LIBROS 📚        ║");
        System.out.println("╠══════════════════════════════════════════════╣");
        System.out.println("║  1️⃣  ➤ Buscar libro por título                 ║");
        System.out.println("║  2️⃣  ➤ Listar libros registrados               ║");
        System.out.println("║  3️⃣  ➤ Listar autores registrados              ║");
        System.out.println("║  4️⃣  ➤ Listar autores vivos en un determinado año ║");
        System.out.println("║  5️⃣  ➤ Listar libros por idioma                ║");
        System.out.println("║  0️⃣  ➤ Salir                                   ║");
        System.out.println("╚══════════════════════════════════════════════╝");
        System.out.print("👉 Por favor, seleccione una opción: ");
    }


    private int leerOpcion() {
        while (!teclado.hasNextInt()) {
            System.out.print("Por favor, ingrese un número válido: ");
            teclado.next();
        }
        int opcion = teclado.nextInt();
        teclado.nextLine(); // Consumir el salto de línea
        return opcion;
    }

    private void procesarOpcion(int opcion) {
        switch (opcion) {
            case 1 -> buscarLibroPorTitulo();
            case 2 -> listarLibrosRegistrados();
            case 3 -> listarAutoresRegistrados();
            case 4 -> listarAutoresVivosPorAnio();
            case 5 -> listarLibrosPorIdioma();
            case 0 -> salir();
            default -> System.out.println("Opción no válida. Por favor, intente nuevamente.");
        }
    }

    private void listarAutoresRegistrados() {
        System.out.println("\n📚 ╔════════════════ AUTORES REGISTRADOS ════════════════╗");
        List<Autor> autores = autorRepository.findAutoresConLibros();

        if (autores.isEmpty()) {
            System.out.println("   ║ ❌ No hay autores registrados en la base de datos.       ║");
            System.out.println("📚 ╚═══════════════════════════════════════════════════════╝");
            return;
        }

        autores.forEach(autor -> {
            System.out.println("   ║ ✍️ Autor: " + autor.getNombre());
            System.out.println("   ║ 🗓️ Nacimiento: " + valorODesconocido(autor.getFechaDeNacimiento()));
            System.out.println("   ║ ⚰️ Fallecimiento: " + valorODesconocido(autor.getFechaFallecimiento()));
            System.out.println("   ║ 📖 Libros:");

            autor.getLibros().forEach(libro -> {
                System.out.printf("   ║    ➤ %s (🌐 Idioma: %s, 📥 Descargas: %.0f)\n",
                        libro.getTitulo(), libro.getIdioma(), libro.getNumeroDeDescargas());
            });

            System.out.println("   ║ -----------------------------------------------------");
        });
        System.out.println("📚 ╚═══════════════════════════════════════════════════════╝");
    }


    private void buscarLibroPorTitulo() {
        System.out.println("\n=== BÚSQUEDA DE LIBRO ===");
        System.out.print("Ingrese el título del libro: ");
        String tituloLibro = teclado.nextLine().trim();

        if (tituloLibro.isEmpty()) {
            System.out.println("El título no puede estar vacío.");
            return;
        }

        libroRepository.findByTituloContainingIgnoreCase(tituloLibro)
                .ifPresentOrElse(
                        libro -> {
                            System.out.println("\nLibro encontrado en la base de datos:");
                            System.out.println(libro);
                        },
                        () -> buscarEnApiExterna(tituloLibro)
                );
    }

    private void buscarEnApiExterna(String tituloLibro) {
        try {
            System.out.println("Buscando en la API externa...");
            String urlBusqueda = URL_BASE + "?search=" + URLEncoder.encode(tituloLibro, StandardCharsets.UTF_8);
            String json = consumoAPI.obtenerDatos(urlBusqueda);

            if (json == null || json.isEmpty()) {
                System.out.println("No se recibió respuesta de la API");
                return;
            }

            Datos datosBusqueda = conversor.obtenerDatos(json, Datos.class);
            List<DatosLibros> resultados = datosBusqueda.resultados();

            if (resultados == null || resultados.isEmpty()) {
                System.out.println("No se encontraron resultados para: " + tituloLibro);
                return;
            }

            resultados.stream()
                    .filter(libro -> libro.titulo().equalsIgnoreCase(tituloLibro))
                    .findFirst()
                    .ifPresentOrElse(this::guardarLibroYAutor,
                            () -> System.out.println("No se encontró ningún libro que coincida exactamente con: " + tituloLibro));
        } catch (Exception e) {
            System.out.println("Error durante la búsqueda: " + e.getMessage());
        }
    }

    private void guardarLibroYAutor(DatosLibros datosLibro) {
        if (datosLibro.autor() == null || datosLibro.autor().isEmpty()) {
            System.out.println("El libro no tiene autor registrado.");
            return;
        }

        DatosAutor datosAutor = datosLibro.autor().get(0);
        Autor autor = autorRepository.findByNombre(datosAutor.nombre())
                .orElseGet(() -> autorRepository.save(new Autor(datosAutor)));

        libroRepository.findByTituloContainingIgnoreCase(datosLibro.titulo())
                .ifPresentOrElse(
                        libro -> System.out.println("El libro ya existe en la base de datos."),
                        () -> {
                            Libro libro = libroRepository.save(new Libro(datosLibro, autor));
                            System.out.println("\nLibro guardado exitosamente:");
                            System.out.println(libro);
                        }
                );
    }

    private void listarLibrosRegistrados() {
        System.out.println("\n=== LIBROS REGISTRADOS ===");
        List<Libro> libros = libroRepository.findAll();

        if (libros.isEmpty()) {
            System.out.println("No hay libros registrados en la base de datos.");
            return;
        }

        libros.forEach(System.out::println);
    }

    private void listarAutoresVivosPorAnio() {
        System.out.println("\n=== AUTORES VIVOS POR AÑO ===");
        System.out.print("Ingrese el año para consultar: ");

        try {
            int anio = Integer.parseInt(teclado.nextLine().trim());

            if (anio < 0 || anio > 2024) {
                System.out.println("Por favor, ingrese un año válido.");
                return;
            }

            List<Autor> autoresVivos = autorRepository.findAll().stream()
                    .filter(autor -> estaVivoEnAnio(autor, anio))
                    .collect(Collectors.toList());

            if (autoresVivos.isEmpty()) {
                System.out.println("No se encontraron autores vivos para el año especificado.");
                return;
            }

            System.out.println("Autores vivos en " + anio + ":");
            autoresVivos.forEach(autor ->
                    System.out.printf("- %s (Nacimiento: %s)%n", autor.getNombre(), autor.getFechaDeNacimiento()));

        } catch (NumberFormatException e) {
            System.out.println("Por favor, ingrese un año válido en formato numérico.");
        }
    }

    private boolean estaVivoEnAnio(Autor autor, int anio) {
        try {
            int anioNacimiento = Integer.parseInt(autor.getFechaDeNacimiento());
            int anioFallecimiento = autor.getFechaFallecimiento() == null ? Integer.MAX_VALUE : Integer.parseInt(autor.getFechaFallecimiento());

            return anioNacimiento <= anio && anioFallecimiento >= anio;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void listarLibrosPorIdioma() {
        System.out.println("\n=== LIBROS POR IDIOMA ===");
        System.out.print("Ingrese el código del idioma (ES, EN, FR, PT, etc.): ");
        String codigoIdioma = teclado.nextLine().trim().toUpperCase();

        if (codigoIdioma.isEmpty()) {
            System.out.println("El código del idioma no puede estar vacío.");
            return;
        }

        List<Libro> librosPorIdioma = libroRepository.findByIdioma(codigoIdioma);

        if (librosPorIdioma.isEmpty()) {
            System.out.println("No se encontraron libros registrados en el idioma: " + codigoIdioma);
            return;
        }

        System.out.println("Libros en idioma " + codigoIdioma + ":");
        librosPorIdioma.forEach(libro ->
                System.out.printf("- %s (Autor: %s, Descargas: %.0f)%n",
                        libro.getTitulo(), libro.getAutor().getNombre(), libro.getNumeroDeDescargas()));
    }

    private void salir() {
        System.out.println("Saliendo del sistema...");
        ejecutando = false;
    }

    private String valorODesconocido(String valor) {
        return valor == null || valor.isEmpty() ? "Desconocido" : valor;
    }
}
