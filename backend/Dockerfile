# Usar OpenJDK 21 como imagen base
FROM openjdk:21-jdk-slim

# Establecer directorio de trabajo
WORKDIR /app

# Copiar todos los archivos de Gradle de una vez
COPY gradle/ gradle/
COPY gradlew .
COPY build.gradle .
COPY settings.gradle .

# Hacer gradlew ejecutable
RUN chmod +x gradlew

# Verificar que el wrapper esté presente
RUN ls -la gradle/wrapper/ || echo "Wrapper directory not found"
RUN ls -la gradle/wrapper/gradle-wrapper.jar || echo "gradle-wrapper.jar not found"

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
