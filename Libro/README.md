Sistema de Gestión de Libros

Este proyecto es una aplicación de consola en Java, desarrollada con Spring Boot, para la gestión de libros y autores. Permite buscar, registrar y listar libros y autores mediante una base de datos local o una API externa.

Ejecución de la Aplicación

La ejecución de la aplicación se inicia en la clase DesafioApplication, que implementa la interfaz CommandLineRunner para ejecutar el programa al inicio. El flujo comienza llamando al método muestraElMenu() de la clase Principal, que presenta un menú interactivo para acceder a las distintas funcionalidades del sistema.

Para ejecutar el proyecto:

1.Clona o descarga el repositorio.

2.Asegúrate de tener configurado Java y Spring Boot en tu entorno.

3.Ejecuta la aplicación desde la clase DesafioApplication.

@SpringBootApplication
public class DesafioApplication implements CommandLineRunner {

	private final Principal principal;

	// Constructor que inyecta `Principal` con `@Lazy`
	public DesafioApplication(@Lazy Principal principal) {
		this.principal = principal;
	}

	public static void main(String[] args) {
		SpringApplication.run(DesafioApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		principal.muestraElMenu();
	}
}

Funcionalidades del Sistema

Menú Principal

Desde el menú, se pueden realizar las siguientes operaciones:

Buscar libro por título: Permite buscar en la base de datos y, si no está registrado, buscar y registrar el libro desde la API externa.

Listar libros registrados: Muestra todos los libros almacenados en la base de datos.

Listar autores registrados: Presenta los autores y los libros asociados en la base de datos.

Listar autores vivos en un año específico: Permite consultar autores vivos en un año específico.

Listar libros por idioma: Filtra los libros en la base de datos según el idioma especificado (ES, EN, FR, PT).

Salir: Termina la aplicación.

Estructura de Paquetes

com.aluracursos.desafio.principal: Contiene la lógica principal y el menú interactivo (Principal).

com.aluracursos.desafio.model: Clases de modelo Autor y Libro.

com.aluracursos.desafio.repository: Repositorios para interactuar con la base de datos (AutorRepository y LibroRepository).

com.aluracursos.desafio.service: Servicios auxiliares para consumir la API y convertir datos (ConsumoAPI y ConvierteDatos).

Dependencias y Configuración

La clase DesafioApplication define beans para los servicios ConsumoAPI y ConvierteDatos, que son usados por la aplicación para la búsqueda y el procesamiento de datos obtenidos de la API.

@Bean
public ConsumoAPI consumoAPI() {
	return new ConsumoAPI();
}

@Bean
public ConvierteDatos conversor() {
	return new ConvierteDatos();
}


Ejemplo de Uso

=== SISTEMA DE GESTIÓN DE LIBROS ===

1- Buscar libro por título

2- Listar libros registrados

3- Listar autores registrados

4- Listar autores vivos en un determinado año

5- Listar libros por idioma

0- Salir

Seleccione una opción:




