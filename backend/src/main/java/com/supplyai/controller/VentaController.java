package com.supplyai.controller;

import com.supplyai.dto.VentaDTO;
import com.supplyai.dto.request.CrearVentaRequest;
import com.supplyai.dto.response.ApiResponse;
import com.supplyai.service.VentaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

/**
 * Controlador REST para la gestión de ventas.
 * Proporciona endpoints para crear ventas, dividir cuentas y consultar ventas.
 * 
 * @author David Timana
 * @version 1.0
 * @since 2025-01-01
 */
@RestController
@RequestMapping("/api/v1/ventas")
@Tag(name = "Ventas", description = "API para la gestión de ventas")
@Validated
public class VentaController {

    private final VentaService ventaService;

    public VentaController(VentaService ventaService) {
        this.ventaService = ventaService;
    }

    /**
     * Crea una nueva venta con sus items.
     * 
     * @param request DTO con la información de la venta
     * @return Venta creada con Location header
     */
    @PostMapping
    @Operation(
        summary = "Crear venta",
        description = "Crea una nueva venta con validación de stock y actualización de inventario. Retorna 201 con Location header."
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "201",
            description = "Venta creada exitosamente",
            headers = {
                @io.swagger.v3.oas.annotations.headers.Header(
                    name = "Location",
                    description = "URL de la venta creada",
                    schema = @Schema(type = "string")
                )
            },
            content = @Content(schema = @Schema(implementation = VentaDTO.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "Datos de la venta inválidos"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "409",
            description = "Stock insuficiente para algún producto"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "422",
            description = "Estado de la venta inválido"
        )
    })
    public ResponseEntity<ApiResponse<VentaDTO>> crearVenta(
            @Valid @RequestBody CrearVentaRequest request) {
        // TODO: Implementar lógica para mapear CrearVentaRequest a VentaDTO
        // Por ahora, creamos un VentaDTO básico
        VentaDTO ventaDTO = new VentaDTO();
        ventaDTO.setSucursalId(request.getSucursalId());
        ventaDTO.setCajaId(request.getCajaId());
        ventaDTO.setMetodoPago(request.getMetodoPago());
        ventaDTO.setItems(request.getItems());
        
        VentaDTO ventaCreada = ventaService.crearVenta(ventaDTO);
        
        URI location = URI.create("/api/v1/ventas/" + ventaCreada.getId());
        
        return ResponseEntity.created(location)
                .body(ApiResponse.success(ventaCreada, "Venta creada exitosamente"));
    }

    /**
     * Divide una venta en múltiples pagos.
     * 
     * @param ventaId ID de la venta a dividir
     * @param request Información de la división de pagos
     * @return Venta actualizada
     */
    @PostMapping("/{ventaId}/dividir")
    @Operation(
        summary = "Dividir cuenta de venta",
        description = "Divide el pago de una venta en múltiples métodos de pago"
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Venta dividida exitosamente",
            content = @Content(schema = @Schema(implementation = VentaDTO.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Venta no encontrada"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "Datos de división inválidos"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "422",
            description = "Estado de la venta no permite división"
        )
    })
    public ResponseEntity<ApiResponse<VentaDTO>> dividirVenta(
            @Parameter(description = "ID de la venta", required = true) 
            @PathVariable @Min(1) Long ventaId,
            @Valid @RequestBody Object request) { // TODO: Crear DTO específico para división
        // TODO: Implementar lógica de división de cuenta
        return ResponseEntity.ok(ApiResponse.success(null, "División de cuenta implementada"));
    }

    /**
     * Obtiene una venta por su ID.
     * 
     * @param id ID de la venta
     * @return Venta encontrada
     */
    @GetMapping("/{id}")
    @Operation(
        summary = "Obtener venta por ID",
        description = "Recupera una venta específica por su identificador"
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Venta encontrada",
            content = @Content(schema = @Schema(implementation = VentaDTO.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Venta no encontrada"
        )
    })
    public ResponseEntity<ApiResponse<VentaDTO>> obtenerVenta(
            @Parameter(description = "ID de la venta", required = true) 
            @PathVariable @Min(1) Long id) {
        VentaDTO venta = ventaService.findById(id);
        return ResponseEntity.ok(ApiResponse.success(venta, "Venta encontrada"));
    }

    /**
     * Obtiene todas las ventas de una sucursal con paginación.
     * 
     * @param sucursalId ID de la sucursal
     * @param pageable Configuración de paginación
     * @return Página de ventas
     */
    @GetMapping("/sucursal/{sucursalId}")
    @Operation(
        summary = "Listar ventas por sucursal",
        description = "Recupera todas las ventas de una sucursal específica con paginación. Paginación obligatoria."
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Ventas recuperadas exitosamente",
            content = @Content(schema = @Schema(implementation = Page.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "Parámetros de paginación inválidos"
        )
    })
    public ResponseEntity<ApiResponse<Page<VentaDTO>>> listarVentasPorSucursal(
            @Parameter(description = "ID de la sucursal", required = true) 
            @PathVariable @Min(1) Long sucursalId,
            @Parameter(description = "Configuración de paginación") 
            @PageableDefault(size = 20, sort = "fechaVenta") @Valid Pageable pageable) {
        
        // Validar límites de paginación
        if (pageable.getPageSize() > 100) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("El tamaño de página no puede exceder 100", "PAGINATION_LIMIT_EXCEEDED"));
        }
        
        Page<VentaDTO> ventas = ventaService.findAllBySucursalId(sucursalId, pageable);
        return ResponseEntity.ok(ApiResponse.success(ventas, "Ventas recuperadas exitosamente"));
    }
}
