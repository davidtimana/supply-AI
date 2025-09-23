package com.supplyai.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entidad para items de venta en el sistema Supply AI
 */
@Entity
@Table(name = "venta_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = {"organizacion", "venta", "producto"})
public class VentaItem {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organizacion_id", nullable = false)
    @JsonIgnore
    private Organization organizacion;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "venta_id", nullable = false)
    @JsonIgnore
    private Venta venta;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", nullable = false)
    @JsonIgnore
    private Producto producto;
    
    @NotNull(message = "La cantidad es obligatoria")
    @DecimalMin(value = "0.001", inclusive = true, message = "La cantidad debe ser mayor a 0")
    @Digits(integer = 12, fraction = 3, message = "La cantidad debe tener máximo 12 dígitos enteros y 3 decimales")
    @Column(name = "cantidad", nullable = false, precision = 15, scale = 3)
    private BigDecimal cantidad;
    
    @NotNull(message = "El precio unitario es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El precio unitario debe ser mayor a 0")
    @Digits(integer = 13, fraction = 2, message = "El precio unitario debe tener máximo 13 dígitos enteros y 2 decimales")
    @Column(name = "precio_unitario", nullable = false, precision = 15, scale = 2)
    private BigDecimal precioUnitario;
    
    @DecimalMin(value = "0.0", inclusive = true, message = "El precio de costo debe ser mayor o igual a 0")
    @Digits(integer = 13, fraction = 2, message = "El precio de costo debe tener máximo 13 dígitos enteros y 2 decimales")
    @Column(name = "precio_costo", precision = 15, scale = 2)
    private BigDecimal precioCosto;
    
    @NotNull(message = "El subtotal es obligatorio")
    @DecimalMin(value = "0.0", inclusive = true, message = "El subtotal debe ser mayor o igual a 0")
    @Digits(integer = 13, fraction = 2, message = "El subtotal debe tener máximo 13 dígitos enteros y 2 decimales")
    @Column(name = "subtotal", nullable = false, precision = 15, scale = 2)
    private BigDecimal subtotal;
    
    @DecimalMin(value = "0.0", inclusive = true, message = "El descuento debe ser mayor o igual a 0")
    @Digits(integer = 13, fraction = 2, message = "El descuento debe tener máximo 13 dígitos enteros y 2 decimales")
    @Column(name = "descuento", precision = 15, scale = 2)
    private BigDecimal descuento;
    
    @DecimalMin(value = "0.0", inclusive = true, message = "El impuesto debe ser mayor o igual a 0")
    @Digits(integer = 13, fraction = 2, message = "El impuesto debe tener máximo 13 dígitos enteros y 2 decimales")
    @Column(name = "impuesto", precision = 15, scale = 2)
    private BigDecimal impuesto;
    
    @NotNull(message = "El total es obligatorio")
    @DecimalMin(value = "0.0", inclusive = true, message = "El total debe ser mayor o igual a 0")
    @Digits(integer = 13, fraction = 2, message = "El total debe tener máximo 13 dígitos enteros y 2 decimales")
    @Column(name = "total", nullable = false, precision = 15, scale = 2)
    private BigDecimal total;
    
    @DecimalMin(value = "0.0", inclusive = true, message = "El margen de ganancia debe ser mayor o igual a 0")
    @Digits(integer = 13, fraction = 2, message = "El margen de ganancia debe tener máximo 13 dígitos enteros y 2 decimales")
    @Column(name = "margen_ganancia", precision = 15, scale = 2)
    private BigDecimal margenGanancia;
    
    @Column(name = "observaciones", columnDefinition = "TEXT")
    private String observaciones;
    
    @CreationTimestamp
    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;
    
    // Métodos de negocio
    @PrePersist
    protected void onCreate() {
        if (cantidad == null) cantidad = BigDecimal.ONE;
        if (precioUnitario == null) precioUnitario = BigDecimal.ZERO;
        if (precioCosto == null) precioCosto = BigDecimal.ZERO;
        if (subtotal == null) subtotal = BigDecimal.ZERO;
        if (descuento == null) descuento = BigDecimal.ZERO;
        if (impuesto == null) impuesto = BigDecimal.ZERO;
        if (total == null) total = BigDecimal.ZERO;
        if (margenGanancia == null) margenGanancia = BigDecimal.ZERO;
        if (fechaCreacion == null) fechaCreacion = LocalDateTime.now();
        
        // Calcular valores automáticamente
        calcularValores();
    }
    
    public void calcularValores() {
        // Calcular subtotal
        this.subtotal = this.cantidad.multiply(this.precioUnitario);
        
        // Calcular total (subtotal + impuesto - descuento)
        this.total = this.subtotal.add(this.impuesto).subtract(this.descuento);
        
        // Calcular margen de ganancia si hay precio de costo
        if (this.precioCosto != null && this.precioCosto.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal ganancia = this.precioUnitario.subtract(this.precioCosto);
            if (ganancia.compareTo(BigDecimal.ZERO) > 0) {
                this.margenGanancia = ganancia.multiply(this.cantidad);
            } else {
                this.margenGanancia = BigDecimal.ZERO;
            }
        }
    }
    
    public BigDecimal calcularSubtotal() {
        return this.cantidad.multiply(this.precioUnitario);
    }
    
    public BigDecimal calcularTotal() {
        return this.subtotal.add(this.impuesto).subtract(this.descuento);
    }
    
    public BigDecimal calcularMargenGanancia() {
        if (this.precioCosto == null || this.precioCosto.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO;
        }
        
        BigDecimal ganancia = this.precioUnitario.subtract(this.precioCosto);
        if (ganancia.compareTo(BigDecimal.ZERO) > 0) {
            return ganancia.multiply(this.cantidad);
        }
        return BigDecimal.ZERO;
    }
    
    public void aplicarDescuento(BigDecimal porcentajeDescuento) {
        if (porcentajeDescuento != null && porcentajeDescuento.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal descuentoCalculado = this.subtotal.multiply(porcentajeDescuento)
                    .divide(BigDecimal.valueOf(100), 2, BigDecimal.ROUND_HALF_UP);
            this.descuento = descuentoCalculado;
            calcularValores();
        }
    }
    
    public void aplicarImpuesto(BigDecimal porcentajeImpuesto) {
        if (porcentajeImpuesto != null && porcentajeImpuesto.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal impuestoCalculado = this.subtotal.multiply(porcentajeImpuesto)
                    .divide(BigDecimal.valueOf(100), 2, BigDecimal.ROUND_HALF_UP);
            this.impuesto = impuestoCalculado;
            calcularValores();
        }
    }
}
