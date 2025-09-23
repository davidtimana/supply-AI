package com.supplyai.controller;

import com.supplyai.dto.ProductoDTO;
import com.supplyai.dto.response.ApiResponse;
import com.supplyai.service.ProductoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
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
 * Controlador REST para la gestión de productos.
 * Proporciona endpoints CRUD para operaciones con productos.
 * 
 * @author David Timana
 * @version 1.0
 * @since 2025-01-01
 */
@RestController
@RequestMapping("/api/v1/productos")
@Tag(name = "Productos", description = "API para la gestión de productos")
@Validated
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    /**
     * Crea un nuevo producto.
     * 
     * @param productoDTO DTO con la información del producto
     * @return Producto creado con Location header
     */
    @PostMapping
    @Operation(
        summary = "Crear producto",
        description = "Crea un nuevo producto en el sistema. Retorna 201 con Location header."
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "201",
            description = "Producto creado exitosamente",
            headers = {
                @io.swagger.v3.oas.annotations.headers.Header(
                    name = "Location",
                    description = "URL del producto creado",
                    schema = @Schema(type = "string")
                )
            },
            content = @Content(schema = @Schema(implementation = ProductoDTO.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "Datos del producto inválidos"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "409",
            description = "Conflicto: producto ya existe"
        )
    })
    public ResponseEntity<ApiResponse<ProductoDTO>> crearProducto(
            @Valid @RequestBody ProductoDTO productoDTO) {
        ProductoDTO productoCreado = productoService.save(productoDTO);
        
        URI location = URI.create("/api/v1/productos/" + productoCreado.getId());
        
        return ResponseEntity.created(location)
                .body(ApiResponse.success(productoCreado, "Producto creado exitosamente"));
    }

    /**
     * Actualiza un producto existente (reemplazo completo).
     * 
     * @param id ID del producto a actualizar
     * @param productoDTO DTO con la información actualizada
     * @return Producto actualizado
     */
    @PutMapping("/{id}")
    @Operation(
        summary = "Actualizar producto",
        description = "Actualiza un producto existente por su ID. Operación idempotente."
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Producto actualizado exitosamente",
            content = @Content(schema = @Schema(implementation = ProductoDTO.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Producto no encontrado"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "Datos del producto inválidos"
        )
    })
    public ResponseEntity<ApiResponse<ProductoDTO>> actualizarProducto(
            @Parameter(description = "ID del producto", required = true) 
            @PathVariable @Min(1) Long id,
            @Valid @RequestBody ProductoDTO productoDTO) {
        ProductoDTO productoActualizado = productoService.update(id, productoDTO);
        return ResponseEntity.ok(ApiResponse.success(productoActualizado, "Producto actualizado exitosamente"));
    }

    /**
     * Obtiene un producto por su ID.
     * 
     * @param id ID del producto
     * @return Producto encontrado
     */
    @GetMapping("/{id}")
    @Operation(
        summary = "Obtener producto por ID",
        description = "Recupera un producto específico por su identificador"
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Producto encontrado",
            content = @Content(schema = @Schema(implementation = ProductoDTO.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Producto no encontrado"
        )
    })
    public ResponseEntity<ApiResponse<ProductoDTO>> obtenerProducto(
            @Parameter(description = "ID del producto", required = true) 
            @PathVariable @Min(1) Long id) {
        ProductoDTO producto = productoService.findById(id);
        return ResponseEntity.ok(ApiResponse.success(producto, "Producto encontrado"));
    }

    /**
     * Obtiene todos los productos de una sucursal con paginación.
     * 
     * @param sucursalId ID de la sucursal
     * @param pageable Configuración de paginación
     * @return Página de productos
     */
    @GetMapping("/sucursal/{sucursalId}")
    @Operation(
        summary = "Listar productos por sucursal",
        description = "Recupera todos los productos de una sucursal específica con paginación. Paginación obligatoria."
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Productos recuperados exitosamente",
            content = @Content(schema = @Schema(implementation = Page.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "Parámetros de paginación inválidos"
        )
    })
    public ResponseEntity<ApiResponse<Page<ProductoDTO>>> listarProductosPorSucursal(
            @Parameter(description = "ID de la sucursal", required = true) 
            @PathVariable @Min(1) Long sucursalId,
            @Parameter(description = "Configuración de paginación") 
            @PageableDefault(size = 20, sort = "nombre") @Valid Pageable pageable) {
        
        // Validar límites de paginación
        if (pageable.getPageSize() > 100) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("El tamaño de página no puede exceder 100", "PAGINATION_LIMIT_EXCEEDED"));
        }
        
        Page<ProductoDTO> productos = productoService.findAllBySucursalId(sucursalId, pageable);
        return ResponseEntity.ok(ApiResponse.success(productos, "Productos recuperados exitosamente"));
    }

    /**
     * Elimina un producto por su ID.
     * 
     * @param id ID del producto a eliminar
     * @return Respuesta sin contenido (204)
     */
    @DeleteMapping("/{id}")
    @Operation(
        summary = "Eliminar producto",
        description = "Elimina un producto del sistema por su ID. Operación idempotente."
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "204",
            description = "Producto eliminado exitosamente"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Producto no encontrado"
        )
    })
    public ResponseEntity<Void> eliminarProducto(
            @Parameter(description = "ID del producto", required = true) 
            @PathVariable @Min(1) Long id) {
        productoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
