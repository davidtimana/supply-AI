package com.supplyai.controller;

import com.supplyai.dto.SuscripcionDTO;
import com.supplyai.dto.response.ApiResponse;
import com.supplyai.service.SuscripcionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST para la gestión de suscripciones.
 * Proporciona endpoints para crear, actualizar y consultar suscripciones.
 * 
 * @author David Timana
 * @version 1.0
 * @since 2025-01-01
 */
@RestController
@RequestMapping("/api/suscripciones")
@Tag(name = "Suscripciones", description = "API para la gestión de suscripciones")
public class SuscripcionController {

    private final SuscripcionService suscripcionService;

    public SuscripcionController(SuscripcionService suscripcionService) {
        this.suscripcionService = suscripcionService;
    }

    /**
     * DTO para cambio de plan de suscripción.
     */
    @Data
    public static class CambioPlanRequest {
        @NotBlank(message = "El nuevo plan es obligatorio")
        private String nuevoPlan;
    }

    /**
     * Crea una nueva suscripción.
     * 
     * @param suscripcionDTO DTO con la información de la suscripción
     * @return Suscripción creada
     */
    @PostMapping
    @Operation(
        summary = "Crear suscripción",
        description = "Crea una nueva suscripción para una organización"
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "201",
            description = "Suscripción creada exitosamente",
            content = @Content(schema = @Schema(implementation = SuscripcionDTO.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "Datos de la suscripción inválidos"
        )
    })
    public ResponseEntity<ApiResponse<SuscripcionDTO>> crearSuscripcion(
            @Valid @RequestBody SuscripcionDTO suscripcionDTO) {
        SuscripcionDTO suscripcionCreada = suscripcionService.crearSuscripcion(suscripcionDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(suscripcionCreada, "Suscripción creada exitosamente"));
    }

    /**
     * Cambia el plan de una suscripción.
     * 
     * @param organizacionId ID de la organización
     * @param request DTO con el nuevo plan
     * @return Suscripción actualizada
     */
    @PatchMapping("/{organizacionId}/plan")
    @Operation(
        summary = "Cambiar plan de suscripción",
        description = "Cambia el plan de suscripción de una organización"
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Plan cambiado exitosamente",
            content = @Content(schema = @Schema(implementation = SuscripcionDTO.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "Nuevo plan inválido"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Suscripción no encontrada"
        )
    })
    public ResponseEntity<ApiResponse<SuscripcionDTO>> cambiarPlan(
            @Parameter(description = "ID de la organización") @PathVariable Long organizacionId,
            @Valid @RequestBody CambioPlanRequest request) {
        SuscripcionDTO suscripcionActualizada = suscripcionService.cambiarPlan(organizacionId, request.getNuevoPlan());
        return ResponseEntity.ok(ApiResponse.success(suscripcionActualizada, "Plan cambiado exitosamente"));
    }

    /**
     * Obtiene la suscripción de una organización.
     * 
     * @param organizacionId ID de la organización
     * @return Suscripción encontrada
     */
    @GetMapping("/{organizacionId}")
    @Operation(
        summary = "Obtener suscripción por organización",
        description = "Recupera la suscripción activa de una organización específica"
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Suscripción encontrada",
            content = @Content(schema = @Schema(implementation = SuscripcionDTO.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Suscripción no encontrada"
        )
    })
    public ResponseEntity<ApiResponse<SuscripcionDTO>> obtenerSuscripcion(
            @Parameter(description = "ID de la organización") @PathVariable Long organizacionId) {
        SuscripcionDTO suscripcion = suscripcionService.findByOrganizacionId(organizacionId);
        return ResponseEntity.ok(ApiResponse.success(suscripcion, "Suscripción encontrada"));
    }
}
