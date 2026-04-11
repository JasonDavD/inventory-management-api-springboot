🚀 ERP de Inventarios - Distribuidora Andina SAC

Sistema web de gestión de inventarios (ERP) desarrollado con Java + Spring Boot, orientado al control de productos, almacenes y movimientos de stock en tiempo real.

💡 Proyecto académico con enfoque profesional, aplicando buenas prácticas de arquitectura backend.

📌 Descripción

Este sistema permite a una empresa de distribución gestionar su inventario de manera eficiente, controlando:

Productos y categorías
Stock por almacén
Movimientos (entradas y salidas)
Control de acceso por roles

Incluye tanto una API REST como una interfaz web para administración.

🧠 Funcionalidades principales
📦 Gestión completa de productos y categorías
🏬 Control de inventario por almacén
🔄 Registro de movimientos de stock (entrada/salida)
🔐 Autenticación y autorización basada en roles
📊 Dashboard administrativo con Thymeleaf
🔗 API REST para integración con otros sistemas
🛠️ Stack tecnológico
Lenguaje: Java 17
Framework: Spring Boot 3
Persistencia: Spring Data JPA / Hibernate
Base de datos: MySQL 8
Mapeo: MapStruct
Frontend: Thymeleaf + HTML + CSS
Utilidades: Lombok, Jackson
Build tool: Maven
🏗️ Arquitectura

El proyecto sigue una arquitectura en capas:

controller → manejo de peticiones (REST y MVC)
service → lógica de negocio
repository → acceso a datos
entity → modelo de datos
mapper → conversión DTO ↔ entidad
exception → manejo global de errores
⚙️ Ejecución del proyecto
🔧 Requisitos
JDK 17 o superior
MySQL 8
Maven (o usar Maven Wrapper incluido)
🚀 Pasos
Crear base de datos en MySQL:
CREATE DATABASE erp_productos;
Ejecutar script SQL:
src/main/resources/db/Script.sql
Configurar credenciales en:
src/main/resources/application.properties

Ejemplo:

spring.datasource.url=jdbc:mysql://localhost:3306/erp_productos?useSSL=false&serverTimezone=America/Lima
spring.datasource.username=tu_usuario
spring.datasource.password=tu_password
Ejecutar el proyecto:
./mvnw spring-boot:run

(En Windows usar mvnw.cmd)

📡 Endpoints principales
/api/productos
/api/almacenes
/api/inventarios
/api/movimientos

📁 Se incluye colección de Postman para pruebas.

📸 Capturas del sistema

👉 Agrega aquí screenshots del sistema (muy importante para reclutadores)
Ejemplo:

Dashboard
Gestión de productos
Inventario
Postman
🎯 Aprendizajes clave
Desarrollo de aplicaciones empresariales con Spring Boot
Diseño de APIs REST
Manejo de bases de datos relacionales con JPA
Implementación de arquitectura en capas
Seguridad básica con roles
Separación de responsabilidades (DTO, Mapper, Service)
📌 Estado del proyecto

✔️ Funcional en entorno local
🔄 Posible mejora: despliegue en la nube (Render / Railway)

👨‍💻 Autor

Jason Dávila Delgado
Estudiante de Computación e Informática

🔗 GitHub: https://github.com/JasonDavD
🔗 LinkedIn: https://www.linkedin.com/in/jasondavd/
