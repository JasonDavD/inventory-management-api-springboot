# GEMINI.md - Distribuidora Andina SAC

## Project Overview
**Distribuidora Andina SAC** is a Spring Boot application designed for inventory management and product distribution (ERP). It provides both a web interface using **Thymeleaf** templates and a RESTful API for programmatic access.

### Main Technologies
- **Java 17**
- **Spring Boot 3.5.7** (Spring Web, Spring Data JPA, Spring Validation)
- **Database:** MySQL
- **Template Engine:** Thymeleaf
- **Mapping:** MapStruct (Entity <-> DTO)
- **Boilerplate:** Lombok
- **Documentation:** Postman collection included (`Andina SAC.postman_collection.json`)

## Architecture & Conventions
The project follows a standard N-tier architecture:
1.  **Controller Layer (`pe.com.andinadistribuidora.api`):** 
    - `Controller`: Returns Thymeleaf views (MVC).
    - `RestController`: Returns JSON (REST API).
    - `dto`, `request`, `response`: Data Transfer Objects for isolation.
2.  **Service Layer (`pe.com.andinadistribuidora.service`):** 
    - Interfaces define the business contract.
    - `impl` package contains the concrete logic.
3.  **Data Access Layer (`pe.com.andinadistribuidora.repository`):** Spring Data JPA repositories.
4.  **Domain Layer (`pe.com.andinadistribuidora.entity`):** JPA entities representing the database schema.
5.  **Mapping Layer (`pe.com.andinadistribuidora.mapper`):** MapStruct interfaces for efficient object mapping.

### Key Conventions
- **Authentication:** Custom session-based check in MVC controllers (`session.getAttribute("usuarioLogueado")`).
- **Data Transfer:** Requests and responses are encapsulated in specific DTOs (e.g., `AlmacenRequestDto`, `AlmacenResponseDto`).
- **Validation:** Uses standard `@Valid` and Bean Validation annotations.

## Building and Running

### Commands
- **Build the project:**
  ```powershell
  ./mvnw clean install
  ```
- **Run the application:**
  ```powershell
  ./mvnw spring-boot:run
  ```
- **Run tests:**
  ```powershell
  ./mvnw test
  ```

### Database Configuration
The application connects to a MySQL database named `erp_productos`. Configuration can be found in `src/main/resources/application.properties`.
- **SQL Script:** `src/main/resources/db/Script.sql`
- **Default URL:** `jdbc:mysql://localhost:3306/erp_productos`
- **Default Username:** `root`
- **Default Password:** `mysql`

## Project Structure
- `src/main/java`: Java source files organized by package.
- `src/main/resources/templates`: Thymeleaf HTML views.
- `src/main/resources/static`: Static assets (CSS, JS, images).
- `HELP.md`: Spring Boot generated help file.
- `Andina SAC.postman_collection.json`: API testing collection.
