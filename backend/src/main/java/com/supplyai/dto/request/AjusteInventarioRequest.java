package com.supplyai.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

/**
 * DTO de request para ajustes de inventario.
 * Permite registrar entradas, salidas y ajustes de stock.
 * 
 * @author David Timana
 * @version 1.0
 * @since 2025-01-01
 */
@Data
public class AjusteInventarioRequest {

    /**
     * ID del producto a ajustar.
     * Campo obligatorio.
     */
    @NotNull(message = "El ID del producto es obligatorio")
    @Min(value = 1, message = "El ID del producto debe ser mayor a 0")
    private Long productoId;

    /**
     * ID de la sucursal donde se realiza el ajuste.
     * Campo obligatorio.
     */
    @NotNull(message = "El ID de la sucursal es obligatorio")
    @Min(value = 1, message = "El ID de la sucursal debe ser mayor a 0")
    private Long sucursalId;

    /**
     * Cantidad a ajustar.
     * Para entradas debe ser positiva, para salidas negativa.
     * Campo obligatorio.
     */
    @NotNull(message = "La cantidad es obligatoria")
    @DecimalMin(value = "0.001", inclusive = true, message = "La cantidad debe ser mayor a 0")
    @Digits(integer = 15, fraction = 3, message = "La cantidad debe tener máximo 15 dígitos enteros y 3 decimales")
    private BigDecimal cantidad;

    /**
     * Tipo de movimiento de inventario.
     * Valores válidos: ENTRADA, SALIDA, AJUSTE
     * Campo obligatorio.
     */
    @NotBlank(message = "El tipo de movimiento es obligatorio")
    @Pattern(regexp = "^(ENTRADA|SALIDA|AJUSTE)$", message = "El tipo de movimiento debe ser ENTRADA, SALIDA o AJUSTE")
    private String tipoMovimiento;

    /**
     * Precio unitario del producto (opcional).
     * Se utiliza para calcular el costo total del movimiento.
     */
    @DecimalMin(value = "0.0", inclusive = true, message = "El precio unitario debe ser mayor o igual a 0")
    @Digits(integer = 13, fraction = 2, message = "El precio unitario debe tener máximo 13 dígitos enteros y 2 decimales")
    private BigDecimal precioUnitario;

    /**
     * Referencia del movimiento (opcional).
     * Máximo 200 caracteres.
     */
    @Size(max = 200, message = "La referencia debe tener máximo 200 caracteres")
    private String referencia;

    /**
     * Documento asociado al movimiento (opcional).
     * Máximo 100 caracteres.
     */
    @Size(max = 100, message = "El documento debe tener máximo 100 caracteres")
    private String documento;

    /**
     * Observaciones adicionales del movimiento (opcional).
     */
    private String observaciones;
}
