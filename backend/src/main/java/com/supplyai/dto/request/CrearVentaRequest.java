package com.supplyai.dto.request;

import com.supplyai.dto.VentaItemDTO;
import com.supplyai.entity.enums.MetodoPago;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.List;

/**
 * DTO de request para crear una nueva venta.
 * Incluye validaciones para asegurar la integridad de los datos.
 * 
 * @author David Timana
 * @version 1.0
 * @since 2025-01-01
 */
@Data
public class CrearVentaRequest {

    /**
     * ID de la sucursal donde se realiza la venta.
     * Campo obligatorio.
     */
    @NotNull(message = "El ID de la sucursal es obligatorio")
    @Min(value = 1, message = "El ID de la sucursal debe ser mayor a 0")
    private Long sucursalId;

    /**
     * ID de la caja donde se procesa la venta.
     * Campo obligatorio.
     */
    @NotNull(message = "El ID de la caja es obligatorio")
    @Min(value = 1, message = "El ID de la caja debe ser mayor a 0")
    private Long cajaId;

    /**
     * Método de pago utilizado para la venta.
     * Campo obligatorio.
     */
    @NotNull(message = "El método de pago es obligatorio")
    private MetodoPago metodoPago;

    /**
     * Nombre del cliente (opcional).
     * Máximo 200 caracteres.
     */
    @Size(max = 200, message = "El nombre del cliente debe tener máximo 200 caracteres")
    private String clienteNombre;

    /**
     * Documento de identidad del cliente (opcional).
     * Máximo 50 caracteres.
     */
    @Size(max = 50, message = "El documento del cliente debe tener máximo 50 caracteres")
    private String clienteDocumento;

    /**
     * Email del cliente (opcional).
     * Debe tener formato válido si se proporciona.
     */
    @Email(message = "El email del cliente debe tener un formato válido")
    @Size(max = 100, message = "El email del cliente debe tener máximo 100 caracteres")
    private String clienteEmail;

    /**
     * Teléfono del cliente (opcional).
     * Máximo 20 caracteres.
     */
    @Size(max = 20, message = "El teléfono del cliente debe tener máximo 20 caracteres")
    private String clienteTelefono;

    /**
     * Observaciones adicionales de la venta (opcional).
     */
    private String observaciones;

    /**
     * Lista de items de la venta.
     * Debe contener al menos un item y cada item debe ser válido.
     */
    @NotEmpty(message = "La venta debe tener al menos un item")
    @Valid
    private List<VentaItemDTO> items;
}
