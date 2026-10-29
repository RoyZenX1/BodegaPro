# ========================================================
# ETAPA 1: Compilación del proyecto (Build)
# ========================================================
FROM maven:3.9-eclipse-temurin-21-alpine AS build
WORKDIR /app

# 1. Copiar el descriptor del proyecto para descargar dependencias en caché
COPY pom.xml ./

# 2. Descargar las dependencias en modo offline (optimiza re-compilaciones)
RUN mvn dependency:go-offline -B

# 3. Copiar el código fuente y compilar omitiendo las pruebas unitarias
COPY src ./src
RUN mvn clean package -DskipTests

# ========================================================
# ETAPA 2: Entorno de ejecución en producción (Runtime)
# ========================================================
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# 1. Copiar únicamente el .jar generado en la etapa de compilación
COPY --from=build /app/target/*.jar app.jar

# 2. Exponer el puerto estándar de Spring Boot (documentación)
EXPOSE 8080

# 3. Asignar el puerto predeterminado por si no se especifica uno externo
ENV PORT=8080

# 4. Comando final para iniciar la aplicación bindeando el puerto de Render
ENTRYPOINT ["sh", "-c", "java -jar app.jar --server.port=${PORT}"]