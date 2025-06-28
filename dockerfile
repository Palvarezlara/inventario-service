# Etapa de construcción
FROM maven:3.9.6-eclipse-temurin-17 AS builder
WORKDIR /app

# Copia el archivo de configuración y el código fuente
COPY pom.xml .
COPY src ./src

# Compila el proyecto y genera el .jar sin ejecutar los tests
RUN mvn clean package -DskipTests

# Etapa de ejecución
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app

# Copia el .jar generado desde la etapa de construcción
# Cambia el nombre exacto si es necesario
COPY --from=builder /app/target/inventario-0.0.1-SNAPSHOT.jar app.jar

# Expone el puerto en que correrá tu app
EXPOSE 8080

# Ejecuta la aplicación
ENTRYPOINT ["java", "-jar", "app.jar","--spring.profiles.active=dev"]

# Instrucciones para construir y ejecutar el contenedor
#docker build -t mi-app .
#docker run -p 8080:8080 mi-app
# Para acceder a la aplicación, abre tu navegador y ve a http://localhost:8080/doc/swagger-ui.html
#docker build --no-cache -t inventario-app . esto evita que Docker reutilice capas viejas con el .jar
