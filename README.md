# 📦 Distribuidora Andina SAC - Sistema ERP de Gestión de Inventarios

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.7-brightgreen)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-17-orange)](https://www.oracle.com/java/)
[![MySQL](https://img.shields.io/badge/MySQL-8.0-blue)](https://www.mysql.com/)

Este proyecto es una solución integral para la gestión de inventarios y distribución de productos (ERP), desarrollada con **Spring Boot 3**. Implementa una arquitectura robusta para el control de almacenes, stock, productos y movimientos de inventario.

## 🚀 Características del Sistema

- **Gestión de Inventario:** Control de stock mínimo, máximo y actual por almacén.
- **Movimientos:** Registro detallado de entradas y salidas de productos.
- **Catálogo ERP:** Administración de productos, categorías y unidades de medida.
- **Seguridad:** Control de acceso basado en sesiones y roles de usuario.
- **Dashboard Web:** Interfaz administrativa amigable desarrollada con Thymeleaf.
- **API REST:** Endpoints listos para ser consumidos por aplicaciones externas.

## 🛠️ Stack Tecnológico

- **Lenguaje:** Java 17.
- **Framework Principal:** Spring Boot 3.5.7.
- **Persistencia:** Spring Data JPA / Hibernate.
- **Base de Datos:** MySQL 8.
- **Mapeo DTO:** MapStruct (para una conversión eficiente entre Entidades y DTOs).
- **Vistas:** Thymeleaf + HTML5 + CSS3.
- **Utilidades:** Lombok, Jackson Annotations.
- **Gestor de Dependencias:** Maven.

## 📋 Requisitos Previos

- **Java Development Kit (JDK) 17** o superior.
- **MySQL Server 8.0** o superior.
- **Maven** (opcional, se incluye Maven Wrapper).

## 🔧 Configuración e Instalación

### 1. Preparar la Base de Datos
1. Abre tu gestor de MySQL (Workbench, CLI, etc.).
2. Crea la base de datos:
   ```sql
   CREATE DATABASE erp_productos;
   ```
3. Ejecuta el script de creación de tablas y datos iniciales ubicado en:
   `src/main/resources/db/Script.sql`

### 2. Configurar la Aplicación
Edita el archivo `src/main/resources/application.properties` con tus credenciales locales:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/erp_productos?useSSL=false&serverTimezone=America/Lima
spring.datasource.username=tu_usuario
spring.datasource.password=tu_password
```

### 3. Ejecutar el Proyecto
Desde la terminal en la carpeta raíz del proyecto:
```bash
# Si usas Windows
./mvnw.cmd spring-boot:run

# Si usas Linux/macOS
./mvnw spring-boot:run
```

## 📂 Estructura del Proyecto

```text
Distribuidora_Andina_sac/
├── src/main/java/pe/com/andinadistribuidora/
│   ├── api/             # Controladores (MVC y REST) y DTOs
│   ├── entity/          # Entidades JPA (Modelo de datos)
│   ├── service/         # Lógica de negocio (Interfaces e Impl)
│   ├── repository/      # Interfaces de acceso a datos (JPA)
│   ├── mapper/          # Mapeadores MapStruct
│   └── exception/       # Manejo global de errores
├── src/main/resources/
│   ├── db/              # Script de base de datos (Script.sql) ⬅️
│   ├── templates/       # Vistas HTML (Thymeleaf)
│   ├── static/          # Recursos estáticos (CSS, JS)
│   └── application.properties
├── Andina SAC.postman_collection.json # Pruebas de API
└── pom.xml              # Configuración de Maven
```

## 🧪 Pruebas de API
Se incluye el archivo `Andina SAC.postman_collection.json` en la raíz. Importalo en **Postman** para probar los endpoints de:
- `/api/almacenes`
- `/api/productos`
- `/api/inventarios`
- `/api/movimientos`

---
⌨️ Desarrollado para el curso de **Lenguaje de Programación II**
