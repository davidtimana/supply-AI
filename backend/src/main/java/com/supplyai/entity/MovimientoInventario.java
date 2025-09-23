package com.supplyai.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.supplyai.entity.enums.TipoMovimiento;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entidad para movimientos de inventario en el sistema Supply AI
 */
@Entity
@Table(name = "movimientos_inventario")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = {"organizacion", "sucursal", "producto"})
public class MovimientoInventario {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organizacion_id", nullable = false)
    @JsonIgnore
    private Organization organizacion;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sucursal_id", nullable = false)
    @JsonIgnore
    private Sucursal sucursal;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", nullable = false)
    @JsonIgnore
    private Producto producto;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false, length = 20)
    private TipoMovimiento tipo;
    
    @NotNull(message = "La cantidad es obligatoria")
    @DecimalMin(value = "0.001", inclusive = true, message = "La cantidad debe ser mayor a 0")
    @Digits(integer = 12, fraction = 3, message = "La cantidad debe tener máximo 12 dígitos enteros y 3 decimales")
    @Column(name = "cantidad", nullable = false, precision = 15, scale = 3)
    private BigDecimal cantidad;
    
    @NotNull(message = "La cantidad anterior es obligatoria")
    @DecimalMin(value = "0.0", inclusive = true, message = "La cantidad anterior debe ser mayor o igual a 0")
    @Digits(integer = 12, fraction = 3, message = "La cantidad anterior debe tener máximo 12 dígitos enteros y 3 decimales")
    @Column(name = "cantidad_anterior", nullable = false, precision = 15, scale = 3)
    private BigDecimal cantidadAnterior;
    
    @NotNull(message = "La cantidad posterior es obligatoria")
    @DecimalMin(value = "0.0", inclusive = true, message = "La cantidad posterior debe ser mayor o igual a 0")
    @Digits(integer = 12, fraction = 3, message = "La cantidad posterior debe tener máximo 12 dígitos enteros y 3 decimales")
    @Column(name = "cantidad_posterior", nullable = false, precision = 15, scale = 3)
    private BigDecimal cantidadPosterior;
    
    @DecimalMin(value = "0.0", inclusive = true, message = "El precio unitario debe ser mayor o igual a 0")
    @Digits(integer = 13, fraction = 2, message = "El precio unitario debe tener máximo 13 dígitos enteros y 2 decimales")
    @Column(name = "precio_unitario", precision = 15, scale = 2)
    private BigDecimal precioUnitario;
    
    @DecimalMin(value = "0.0", inclusive = true, message = "El costo total debe ser mayor o igual a 0")
    @Digits(integer = 13, fraction = 2, message = "El costo total debe tener máximo 13 dígitos enteros y 2 decimales")
    @Column(name = "costo_total", precision = 15, scale = 2)
    private BigDecimal costoTotal;
    
    @Size(max = 200, message = "La referencia debe tener máximo 200 caracteres")
    @Column(name = "referencia", length = 200)
    private String referencia;
    
    @Size(max = 100, message = "El documento debe tener máximo 100 caracteres")
    @Column(name = "documento", length = 100)
    private String documento;
    
    @Column(name = "observaciones", columnDefinition = "TEXT")
    private String observaciones;
    
    @Column(name = "fecha_movimiento", nullable = false)
    private LocalDateTime fechaMovimiento;
    
    @CreationTimestamp
    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;
    
    // Métodos de negocio
    @PrePersist
    protected void onCreate() {
        if (fechaMovimiento == null) fechaMovimiento = LocalDateTime.now();
        if (fechaCreacion == null) fechaCreacion = LocalDateTime.now();
        
        // Calcular costo total si no se especifica
        if (costoTotal == null && precioUnitario != null && cantidad != null) {
            this.costoTotal = precioUnitario.multiply(cantidad);
        }
    }
    
    public boolean esEntrada() {
        return TipoMovimiento.ENTRADA.equals(tipo) || 
               TipoMovimiento.DEVOLUCION.equals(tipo) || 
               TipoMovimiento.TRANSFERENCIA.equals(tipo);
    }
    
    public boolean esSalida() {
        return TipoMovimiento.SALIDA.equals(tipo) || 
               TipoMovimiento.MERMA.equals(tipo);
    }
    
    public boolean esAjuste() {
        return TipoMovimiento.AJUSTE.equals(tipo);
    }
    
    public BigDecimal calcularCostoTotal() {
        if (precioUnitario != null && cantidad != null) {
            return precioUnitario.multiply(cantidad);
        }
        return BigDecimal.ZERO;
    }
    
    public String getTipoFormateado() {
        switch (tipo) {
            case ENTRADA: return "Entrada";
            case SALIDA: return "Salida";
            case AJUSTE: return "Ajuste";
            case TRANSFERENCIA: return "Transferencia";
            case DEVOLUCION: return "Devolución";
            case MERMA: return "Merma";
            default: return tipo.toString();
        }
    }
    
    public boolean afectaStock() {
        return !TipoMovimiento.AJUSTE.equals(tipo);
    }
}
