package com.supplyai.controller;

import com.supplyai.dto.CajaDTO;
import com.supplyai.dto.response.ApiResponse;
import com.supplyai.service.CajaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

/**
 * Controlador REST para la gestión de cajas registradoras.
 * Proporciona endpoints para abrir y cerrar cajas.
 * 
 * @author David Timana
 * @version 1.0
 * @since 2025-01-01
 */
@RestController
@RequestMapping("/api/caja")
@Tag(name = "Caja", description = "API para la gestión de cajas registradoras")
public class CajaController {

    private final CajaService cajaService;

    public CajaController(CajaService cajaService) {
        this.cajaService = cajaService;
    }

    /**
     * DTO para la apertura de caja.
     */
    @Data
    public static class AperturaCajaRequest {
        @NotNull(message = "El monto inicial es obligatorio")
        @DecimalMin(value = "0.01", message = "El monto inicial debe ser mayor a 0")
        private BigDecimal montoInicial;
    }

    /**
     * Abre una caja registradora con un monto inicial.
     * 
     * @param cajaId ID de la caja a abrir
     * @param request DTO con el monto inicial
     * @return Caja abierta
     */
    @PostMapping("/{cajaId}/apertura")
    @Operation(
        summary = "Abrir caja",
        description = "Abre una caja registradora con un monto inicial y cambia su estado a ABIERTA"
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Caja abierta exitosamente",
            content = @Content(schema = @Schema(implementation = CajaDTO.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "La caja ya está abierta o datos inválidos"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Caja no encontrada"
        )
    })
    public ResponseEntity<ApiResponse<CajaDTO>> abrirCaja(
            @Parameter(description = "ID de la caja") @PathVariable Long cajaId,
            @Valid @RequestBody AperturaCajaRequest request) {
        CajaDTO cajaAbierta = cajaService.abrirCaja(cajaId, request.getMontoInicial());
        return ResponseEntity.ok(ApiResponse.success(cajaAbierta, "Caja abierta exitosamente"));
    }

    /**
     * Cierra una caja registradora.
     * 
     * @param cajaId ID de la caja a cerrar
     * @return Caja cerrada
     */
    @PostMapping("/{cajaId}/cierre")
    @Operation(
        summary = "Cerrar caja",
        description = "Cierra una caja registradora, cambia su estado a CERRADA y registra el movimiento"
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Caja cerrada exitosamente",
            content = @Content(schema = @Schema(implementation = CajaDTO.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "La caja ya está cerrada"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Caja no encontrada"
        )
    })
    public ResponseEntity<ApiResponse<CajaDTO>> cerrarCaja(
            @Parameter(description = "ID de la caja") @PathVariable Long cajaId) {
        CajaDTO cajaCerrada = cajaService.cerrarCaja(cajaId);
        return ResponseEntity.ok(ApiResponse.success(cajaCerrada, "Caja cerrada exitosamente"));
    }

    /**
     * Obtiene una caja por su ID.
     * 
     * @param id ID de la caja
     * @return Caja encontrada
     */
    @GetMapping("/{id}")
    @Operation(
        summary = "Obtener caja por ID",
        description = "Recupera una caja específica por su identificador"
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Caja encontrada",
            content = @Content(schema = @Schema(implementation = CajaDTO.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Caja no encontrada"
        )
    })
    public ResponseEntity<ApiResponse<CajaDTO>> obtenerCaja(
            @Parameter(description = "ID de la caja") @PathVariable Long id) {
        CajaDTO caja = cajaService.findById(id);
        return ResponseEntity.ok(ApiResponse.success(caja, "Caja encontrada"));
    }
}
