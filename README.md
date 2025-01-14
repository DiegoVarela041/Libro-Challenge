# Sistema de Gestión de Libros

Este proyecto es un sistema de gestión de libros desarrollado en **Java** utilizando **Spring Framework**. Permite realizar búsquedas de libros, listar autores y libros registrados, consultar autores vivos en un año determinado, y mucho más. Además, integra una API externa para buscar información adicional sobre libros.

## Características

- **Búsqueda de libros**: Busca libros por título en la base de datos local o en una API externa.
- **Gestión de autores**: Lista autores registrados junto con sus libros y permite consultar autores vivos en un año específico.
- **Gestión de libros**: Lista libros registrados en la base de datos por título o idioma.
- **Integración con API externa**: Consulta información de libros utilizando [Gutendex API](https://gutendex.com/).
- **Persistencia**: Los datos de autores y libros se almacenan en un repositorio mediante JPA.

## Requisitos

Antes de ejecutar el proyecto, asegúrate de tener instalados los siguientes componentes:

- **Java**: JDK 17 o superior.
- **Maven**: Para gestionar las dependencias y compilar el proyecto.
- **Base de datos**: Configurada para la persistencia (por ejemplo, MySQL, H2, etc.).
- **Spring Framework**: Configuración de Spring Boot incluida en el proyecto.
- ## Instalación

1. Clona el repositorio y accede al directorio del proyecto.
2. Configura tu archivo `application.properties` con los datos de tu base de datos.
3. Compila el proyecto utilizando Maven.
4. Ejecuta la aplicación con el comando `mvn spring-boot:run`.
## Futuras Mejoras

- Implementar una interfaz gráfica o una API REST para el sistema.
- Añadir estadísticas, como libros más descargados o autores más destacados.
- Validaciones más robustas para entradas de usuario.
- Soporte para filtros avanzados en las búsquedas.

## Contribuciones

Las contribuciones son bienvenidas. Si deseas mejorar este proyecto, realiza un fork, realiza tus cambios y envía un pull request.

## Licencia

Este proyecto está bajo la licencia MIT. Consulta el archivo `LICENSE` para más detalles.
