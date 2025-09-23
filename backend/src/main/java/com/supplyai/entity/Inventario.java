package com.supplyai.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entidad para inventarios en el sistema Supply AI
 * Incluye @Version para optimistic locking
 */
@Entity
@Table(name = "inventarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = {"organizacion", "sucursal", "producto"})
public class Inventario {
    
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
    
    @NotNull(message = "El stock actual es obligatorio")
    @DecimalMin(value = "0.0", inclusive = true, message = "El stock actual debe ser mayor o igual a 0")
    @Digits(integer = 12, fraction = 3, message = "El stock actual debe tener máximo 12 dígitos enteros y 3 decimales")
    @Column(name = "stock_actual", nullable = false, precision = 15, scale = 3)
    private BigDecimal stockActual;
    
    @NotNull(message = "El stock mínimo es obligatorio")
    @DecimalMin(value = "0.0", inclusive = true, message = "El stock mínimo debe ser mayor o igual a 0")
    @Digits(integer = 12, fraction = 3, message = "El stock mínimo debe tener máximo 12 dígitos enteros y 3 decimales")
    @Column(name = "stock_minimo", nullable = false, precision = 15, scale = 3)
    private BigDecimal stockMinimo;
    
    @NotNull(message = "El stock máximo es obligatorio")
    @DecimalMin(value = "0.0", inclusive = true, message = "El stock máximo debe ser mayor o igual a 0")
    @Digits(integer = 12, fraction = 3, message = "El stock máximo debe tener máximo 12 dígitos enteros y 3 decimales")
    @Column(name = "stock_maximo", nullable = false, precision = 15, scale = 3)
    private BigDecimal stockMaximo;
    
    @DecimalMin(value = "0.0", inclusive = true, message = "El stock de seguridad debe ser mayor o igual a 0")
    @Digits(integer = 12, fraction = 3, message = "El stock de seguridad debe tener máximo 12 dígitos enteros y 3 decimales")
    @Column(name = "stock_seguridad", precision = 15, scale = 3)
    private BigDecimal stockSeguridad;
    
    @DecimalMin(value = "0.0", inclusive = true, message = "El punto de reorden debe ser mayor o igual a 0")
    @Digits(integer = 12, fraction = 3, message = "El punto de reorden debe tener máximo 12 dígitos enteros y 3 decimales")
    @Column(name = "punto_reorden", precision = 15, scale = 3)
    private BigDecimal puntoReorden;
    
    @Size(max = 50, message = "La unidad de medida debe tener máximo 50 caracteres")
    @Column(name = "unidad_medida", length = 50)
    private String unidadMedida;
    
    @Size(max = 200, message = "La ubicación debe tener máximo 200 caracteres")
    @Column(name = "ubicacion", length = 200)
    private String ubicacion;
    
    @Size(max = 100, message = "El pasillo debe tener máximo 100 caracteres")
    @Column(name = "pasillo", length = 100)
    private String pasillo;
    
    @Size(max = 100, message = "El estante debe tener máximo 100 caracteres")
    @Column(name = "estante", length = 100)
    private String estante;
    
    @Size(max = 50, message = "El nivel debe tener máximo 50 caracteres")
    @Column(name = "nivel", length = 50)
    private String nivel;
    
    @Column(name = "activo", nullable = false)
    private Boolean activo;
    
    @CreationTimestamp
    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;
    
    @UpdateTimestamp
    @Column(name = "fecha_modificacion", nullable = false)
    private LocalDateTime fechaModificacion;
    
    @Column(name = "fecha_eliminacion")
    private LocalDateTime fechaEliminacion;
    
    @Column(name = "eliminado", nullable = false)
    private Boolean eliminado;
    
    // Optimistic locking para evitar conflictos de concurrencia
    @Version
    @Column(name = "version")
    private Long version;
    
    // Métodos de negocio
    @PrePersist
    protected void onCreate() {
        if (stockActual == null) stockActual = BigDecimal.ZERO;
        if (stockMinimo == null) stockMinimo = BigDecimal.ZERO;
        if (stockMaximo == null) stockMaximo = BigDecimal.valueOf(1000);
        if (stockSeguridad == null) stockSeguridad = BigDecimal.ZERO;
        if (puntoReorden == null) puntoReorden = BigDecimal.ZERO;
        if (unidadMedida == null) unidadMedida = "unidad";
        if (activo == null) activo = true;
        if (eliminado == null) eliminado = false;
        if (fechaCreacion == null) fechaCreacion = LocalDateTime.now();
        if (fechaModificacion == null) fechaModificacion = LocalDateTime.now();
        if (version == null) version = 0L;
    }
    
    @PreUpdate
    protected void onUpdate() {
        fechaModificacion = LocalDateTime.now();
    }
    
    public void softDelete() {
        this.eliminado = true;
        this.fechaEliminacion = LocalDateTime.now();
    }
    
    public void restore() {
        this.eliminado = false;
        this.fechaEliminacion = null;
    }
    
    public boolean necesitaReorden() {
        return stockActual.compareTo(puntoReorden) <= 0;
    }
    
    public boolean stockCritico() {
        return stockActual.compareTo(stockMinimo) <= 0;
    }
    
    public boolean stockExcesivo() {
        return stockActual.compareTo(stockMaximo) >= 0;
    }
    
    public void ajustarStock(BigDecimal cantidad) {
        this.stockActual = this.stockActual.add(cantidad);
        if (this.stockActual.compareTo(BigDecimal.ZERO) < 0) {
            this.stockActual = BigDecimal.ZERO;
        }
    }
    
    public BigDecimal calcularStockDisponible() {
        return stockActual.subtract(stockSeguridad);
    }
    
    public String getEstadoStock() {
        if (stockCritico()) {
            return "CRITICO";
        } else if (necesitaReorden()) {
            return "BAJO";
        } else {
            return "NORMAL";
        }
    }
    
    public boolean noEstaEliminado() {
        return getEliminado() == null || !getEliminado();
    }
    
    public boolean estaActivo() {
        return getActivo() != null && getActivo();
    }
}
