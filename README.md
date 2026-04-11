馃殌 ERP de Inventarios - Distribuidora Andina SAC

Sistema web de gesti贸n de inventarios (ERP) desarrollado con Java + Spring Boot, orientado al control de productos, almacenes y movimientos de stock en tiempo real.

馃挕 Proyecto acad茅mico con enfoque profesional, aplicando buenas pr谩cticas de arquitectura backend.

馃搶 Descripci贸n

Este sistema permite a una empresa de distribuci贸n gestionar su inventario de manera eficiente, controlando:

Productos y categor铆as
Stock por almac茅n
Movimientos (entradas y salidas)
Control de acceso por roles

Incluye tanto una API REST como una interfaz web para administraci贸n.

馃 Funcionalidades principales
馃摝 Gesti贸n completa de productos y categor铆as
馃彫 Control de inventario por almac茅n
馃攧 Registro de movimientos de stock (entrada/salida)
馃攼 Autenticaci贸n y autorizaci贸n basada en roles
馃搳 Dashboard administrativo con Thymeleaf
馃敆 API REST para integraci贸n con otros sistemas
馃洜锔?Stack tecnol贸gico
Lenguaje: Java 17
Framework: Spring Boot 3
Persistencia: Spring Data JPA / Hibernate
Base de datos: MySQL 8
Mapeo: MapStruct
Frontend: Thymeleaf + HTML + CSS
Utilidades: Lombok, Jackson
Build tool: Maven
馃彈锔?Arquitectura

El proyecto sigue una arquitectura en capas:

controller 鈫?manejo de peticiones (REST y MVC)
service 鈫?l贸gica de negocio
repository 鈫?acceso a datos
entity 鈫?modelo de datos
mapper 鈫?conversi贸n DTO 鈫?entidad
exception 鈫?manejo global de errores
鈿欙笍 Ejecuci贸n del proyecto
馃敡 Requisitos
JDK 17 o superior
MySQL 8
Maven (o usar Maven Wrapper incluido)
馃殌 Pasos
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

馃摗 Endpoints principales
/api/productos
/api/almacenes
/api/inventarios
/api/movimientos

馃搧 Se incluye colecci贸n de Postman para pruebas.

馃摳 Capturas del sistema

馃憠 Agrega aqu铆 screenshots del sistema (muy importante para reclutadores)
Ejemplo:

Dashboard
Gesti贸n de productos
Inventario
Postman
馃幆 Aprendizajes clave
Desarrollo de aplicaciones empresariales con Spring Boot
Dise帽o de APIs REST
Manejo de bases de datos relacionales con JPA
Implementaci贸n de arquitectura en capas
Seguridad b谩sica con roles
Separaci贸n de responsabilidades (DTO, Mapper, Service)
馃搶 Estado del proyecto

鉁旓笍 Funcional en entorno local
馃攧 Posible mejora: despliegue en la nube (Render / Railway)

馃懆鈥嶐煉?Autor

Jason D谩vila Delgado
Estudiante de Computaci贸n e Inform谩tica

馃敆 GitHub: https://github.com/JasonDavD
馃敆 LinkedIn: https://www.linkedin.com/in/jasondavd/