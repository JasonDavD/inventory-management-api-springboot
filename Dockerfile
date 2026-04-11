# =====================================================
# STAGE 1: Build - Compilar el proyecto con Maven
# =====================================================
FROM maven:3.9.6-eclipse-temurin-17 AS builder

WORKDIR /app

# Copiar primero el pom.xml para aprovechar cache de dependencias
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copiar el resto del código fuente y compilar
COPY src ./src
RUN mvn clean package -DskipTests -B

# =====================================================
# STAGE 2: Runtime - Imagen final ligera
# =====================================================
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Instalar utilidades necesarias (wait-for-it para esperar a MySQL)
RUN apk add --no-cache bash curl

# Copiar el JAR compilado desde el stage anterior
COPY --from=builder /app/target/*.jar app.jar

# Copiar script de espera para MySQL
COPY wait-for-mysql.sh wait-for-mysql.sh
RUN chmod +x wait-for-mysql.sh

# Puerto de la aplicación Spring Boot
EXPOSE 8080

# Comando de inicio: esperar MySQL y luego arrancar la app
ENTRYPOINT ["./wait-for-mysql.sh", "mysql", "3306", "--", "java", "-jar", "app.jar"]
