# Usar OpenJDK 21 como imagen base
FROM openjdk:21-jdk-slim

# Establecer directorio de trabajo
WORKDIR /app

# Copiar archivos de configuración de Gradle
COPY gradle/ gradle/
COPY gradlew .
COPY build.gradle .
COPY settings.gradle .

# Hacer gradlew ejecutable
RUN chmod +x gradlew

# Descargar dependencias (esto se cachea si no cambian los archivos de configuración)
RUN ./gradlew dependencies --no-daemon

# Copiar código fuente
COPY src/ src/

# Construir la aplicación
RUN ./gradlew build -x test --no-daemon

# Exponer puerto
EXPOSE 8080

# Comando para ejecutar la aplicación
CMD ["java", "-jar", "build/libs/supplyai-backend-0.0.1-SNAPSHOT.jar"]
