package com.supplyai.controller;

import com.supplyai.dto.InventarioDTO;
import com.supplyai.dto.request.AjusteInventarioRequest;
import com.supplyai.dto.response.ApiResponse;
import com.supplyai.service.InventarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST para la gestión de inventarios.
 * Proporciona endpoints para entradas, salidas y ajustes de stock.
 * 
 * @author David Timana
 * @version 1.0
 * @since 2025-01-01
 */
@RestController
@RequestMapping("/api/inventario")
@Tag(name = "Inventario", description = "API para la gestión de inventarios")
public class InventarioController {

    private final InventarioService inventarioService;

    public InventarioController(InventarioService inventarioService) {
        this.inventarioService = inventarioService;
    }

    /**
     * Registra una entrada de inventario (aumento de stock).
     * 
     * @param request DTO con la información del ajuste
     * @return Inventario actualizado
     */
    @PostMapping("/entrada")
    @Operation(
        summary = "Registrar entrada de inventario",
        description = "Registra una entrada de productos al inventario, aumentando el stock disponible"
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Entrada registrada exitosamente",
            content = @Content(schema = @Schema(implementation = InventarioDTO.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "Datos de entrada inválidos"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Inventario no encontrado"
        )
    })
    public ResponseEntity<ApiResponse<InventarioDTO>> registrarEntrada(
            @Valid @RequestBody AjusteInventarioRequest request) {
        // Validar que sea una entrada
        if (!"ENTRADA".equals(request.getTipoMovimiento())) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("El tipo de movimiento debe ser ENTRADA"));
        }
        
        InventarioDTO inventarioActualizado = inventarioService.ajustarInventario(
                request.getProductoId(),
                request.getSucursalId(),
                request.getCantidad(),
                request.getTipoMovimiento()
        );
        
        return ResponseEntity.ok(ApiResponse.success(inventarioActualizado, "Entrada registrada exitosamente"));
    }

    /**
     * Registra una salida de inventario (disminución de stock).
     * 
     * @param request DTO con la información del ajuste
     * @return Inventario actualizado
     */
    @PostMapping("/salida")
    @Operation(
        summary = "Registrar salida de inventario",
        description = "Registra una salida de productos del inventario, disminuyendo el stock disponible"
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Salida registrada exitosamente",
            content = @Content(schema = @Schema(implementation = InventarioDTO.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "Datos de salida inválidos"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Inventario no encontrado"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "409",
            description = "Stock insuficiente para la salida"
        )
    })
    public ResponseEntity<ApiResponse<InventarioDTO>> registrarSalida(
            @Valid @RequestBody AjusteInventarioRequest request) {
        // Validar que sea una salida
        if (!"SALIDA".equals(request.getTipoMovimiento())) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("El tipo de movimiento debe ser SALIDA"));
        }
        
        InventarioDTO inventarioActualizado = inventarioService.ajustarInventario(
                request.getProductoId(),
                request.getSucursalId(),
                request.getCantidad(),
                request.getTipoMovimiento()
        );
        
        return ResponseEntity.ok(ApiResponse.success(inventarioActualizado, "Salida registrada exitosamente"));
    }

    /**
     * Realiza un ajuste general de inventario.
     * 
     * @param request DTO con la información del ajuste
     * @return Inventario actualizado
     */
    @PostMapping("/ajuste")
    @Operation(
        summary = "Realizar ajuste de inventario",
        description = "Realiza un ajuste general del stock de inventario (establece un nuevo valor)"
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Ajuste realizado exitosamente",
            content = @Content(schema = @Schema(implementation = InventarioDTO.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "Datos de ajuste inválidos"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Inventario no encontrado"
        )
    })
    public ResponseEntity<ApiResponse<InventarioDTO>> realizarAjuste(
            @Valid @RequestBody AjusteInventarioRequest request) {
        // Validar que sea un ajuste
        if (!"AJUSTE".equals(request.getTipoMovimiento())) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("El tipo de movimiento debe ser AJUSTE"));
        }
        
        InventarioDTO inventarioActualizado = inventarioService.ajustarInventario(
                request.getProductoId(),
                request.getSucursalId(),
                request.getCantidad(),
                request.getTipoMovimiento()
        );
        
        return ResponseEntity.ok(ApiResponse.success(inventarioActualizado, "Ajuste realizado exitosamente"));
    }

    /**
     * Obtiene el inventario de un producto específico en una sucursal.
     * 
     * @param productoId ID del producto
     * @param sucursalId ID de la sucursal
     * @return Inventario del producto
     */
    @GetMapping("/producto/{productoId}/sucursal/{sucursalId}")
    @Operation(
        summary = "Obtener inventario por producto y sucursal",
        description = "Recupera la información de inventario de un producto específico en una sucursal"
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Inventario encontrado",
            content = @Content(schema = @Schema(implementation = InventarioDTO.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Inventario no encontrado"
        )
    })
    public ResponseEntity<ApiResponse<InventarioDTO>> obtenerInventario(
            @Parameter(description = "ID del producto") @PathVariable Long productoId,
            @Parameter(description = "ID de la sucursal") @PathVariable Long sucursalId) {
        InventarioDTO inventario = inventarioService.findByProductoIdAndSucursalId(productoId, sucursalId);
        return ResponseEntity.ok(ApiResponse.success(inventario, "Inventario encontrado"));
    }
}
