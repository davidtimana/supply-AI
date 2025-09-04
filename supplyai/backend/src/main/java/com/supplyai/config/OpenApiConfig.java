package com.supplyai.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Configuración de OpenAPI para la documentación de la API.
 * Define la información general, contactos y servidores disponibles.
 * 
 * @author David Timana
 * @version 1.0
 * @since 2025-01-01
 */
@Configuration
public class OpenApiConfig {

    /**
     * Configura la información general de la API.
     * 
     * @return Configuración de OpenAPI con metadatos de la aplicación
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Supply AI API")
                        .description("""
                            API REST para el sistema de gestión de inventarios y suministros Supply AI.
                            
                            ## Características
                            - **Versionado**: API v1 con soporte para futuras versiones
                            - **Multi-tenancy**: Soporte para múltiples organizaciones y sucursales
                            - **Validación**: Bean Validation en todos los endpoints
                            - **Documentación**: OpenAPI 3.0 con ejemplos y esquemas
                            
                            ## Autenticación
                            La API utiliza autenticación basada en JWT tokens (implementación futura con Keycloak).
                            
                            ## Multi-tenancy
                            Todos los endpoints requieren filtrado por organización y sucursal para garantizar el aislamiento de datos.
                            
                            ## Códigos de Estado
                            - `200` - Operación exitosa
                            - `201` - Recurso creado exitosamente
                            - `204` - Operación exitosa sin contenido
                            - `400` - Solicitud incorrecta
                            - `401` - No autenticado
                            - `403` - No autorizado
                            - `404` - Recurso no encontrado
                            - `409` - Conflicto
                            - `422` - Entidad no procesable
                            - `500` - Error interno del servidor
                            """)
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("David Timana")
                                .email("davidorlandotimana@gmail.com")
                                .url("https://github.com/davidtimana"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server().url("http://localhost:8080").description("Servidor de desarrollo"),
                        new Server().url("https://api.supplyai.com").description("Servidor de producción")
                ))
                .tags(List.of(
                        new Tag().name("Salud").description("Endpoints para monitoreo de salud de la aplicación"),
                        new Tag().name("Productos").description("API para la gestión de productos"),
                        new Tag().name("Ventas").description("API para la gestión de ventas"),
                        new Tag().name("Inventario").description("API para la gestión de inventarios"),
                        new Tag().name("Caja").description("API para la gestión de cajas registradoras"),
                        new Tag().name("Suscripciones").description("API para la gestión de suscripciones")
                ));
    }
}
