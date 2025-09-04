package com.supplyai.controller;

import com.supplyai.dto.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Controlador de salud para monitoreo de la aplicación.
 * Proporciona endpoints para verificar el estado de la aplicación.
 * 
 * @author David Timana
 * @version 1.0
 * @since 2025-01-01
 */
@RestController
@RequestMapping("/api/v1/health")
@Tag(name = "Salud", description = "API para monitoreo de salud de la aplicación")
public class HealthController {

    /**
     * Endpoint básico de ping para verificar que la aplicación esté funcionando.
     * 
     * @return Respuesta de ping con timestamp
     */
    @GetMapping("/ping")
    @Operation(
        summary = "Ping de salud",
        description = "Verifica que la aplicación esté funcionando correctamente"
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Aplicación funcionando correctamente",
            content = @Content(schema = @Schema(implementation = Map.class))
        )
    })
    public ResponseEntity<ApiResponse<Map<String, Object>>> ping() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "OK");
        response.put("timestamp", LocalDateTime.now());
        response.put("message", "Supply AI API funcionando correctamente");
        
        return ResponseEntity.ok(ApiResponse.success(response, "Ping exitoso"));
    }

    /**
     * Endpoint de estado completo de la aplicación.
     * 
     * @return Estado detallado de la aplicación
     */
    @GetMapping("/status")
    @Operation(
        summary = "Estado de la aplicación",
        description = "Proporciona información detallada sobre el estado de la aplicación"
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Estado de la aplicación recuperado",
            content = @Content(schema = @Schema(implementation = Map.class))
        )
    })
    public ResponseEntity<ApiResponse<Map<String, Object>>> status() {
        Map<String, Object> status = new HashMap<>();
        status.put("application", "Supply AI Backend");
        status.put("version", "1.0.0");
        status.put("status", "RUNNING");
        status.put("timestamp", LocalDateTime.now());
        status.put("uptime", System.currentTimeMillis());
        
        // Información del sistema
        Map<String, Object> system = new HashMap<>();
        system.put("javaVersion", System.getProperty("java.version"));
        system.put("osName", System.getProperty("os.name"));
        system.put("osVersion", System.getProperty("os.version"));
        system.put("memory", Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory());
        system.put("maxMemory", Runtime.getRuntime().maxMemory());
        
        status.put("system", system);
        
        return ResponseEntity.ok(ApiResponse.success(status, "Estado de la aplicación recuperado"));
    }
}
