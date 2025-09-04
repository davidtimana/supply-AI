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
 * Entidad para componentes de productos compuestos en el sistema Supply AI
 */
@Entity
@Table(name = "producto_componentes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = {"productoPrincipal", "productoComponente"})
public class ProductoComponente {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_principal_id", nullable = false)
    @JsonIgnore
    private Producto productoPrincipal;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_componente_id", nullable = false)
    @JsonIgnore
    private Producto productoComponente;
    
    @NotNull(message = "La cantidad es obligatoria")
    @DecimalMin(value = "0.001", inclusive = true, message = "La cantidad debe ser mayor a 0")
    @Digits(integer = 12, fraction = 3, message = "La cantidad debe tener máximo 12 dígitos enteros y 3 decimales")
    @Column(name = "cantidad", nullable = false, precision = 15, scale = 3)
    private BigDecimal cantidad;
    
    @Size(max = 50, message = "La unidad de medida debe tener máximo 50 caracteres")
    @Column(name = "unidad_medida", length = 50)
    private String unidadMedida;
    
    @DecimalMin(value = "0.0", inclusive = true, message = "El costo unitario debe ser mayor o igual a 0")
    @Digits(integer = 13, fraction = 2, message = "El costo unitario debe tener máximo 13 dígitos enteros y 2 decimales")
    @Column(name = "costo_unitario", precision = 15, scale = 2)
    private BigDecimal costoUnitario;
    
    @Column(name = "activo", nullable = false)
    private Boolean activo;
    
    @CreationTimestamp
    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;
    
    @UpdateTimestamp
    @Column(name = "fecha_modificacion", nullable = false)
    private LocalDateTime fechaModificacion;
    
    // Métodos de negocio
    @PrePersist
    protected void onCreate() {
        if (cantidad == null) cantidad = BigDecimal.ONE;
        if (unidadMedida == null) unidadMedida = "unidad";
        if (costoUnitario == null) costoUnitario = BigDecimal.ZERO;
        if (activo == null) activo = true;
        if (fechaCreacion == null) fechaCreacion = LocalDateTime.now();
        if (fechaModificacion == null) fechaModificacion = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        fechaModificacion = LocalDateTime.now();
    }
    
    public BigDecimal calcularCostoTotal() {
        if (costoUnitario != null && cantidad != null) {
            return costoUnitario.multiply(cantidad);
        }
        return BigDecimal.ZERO;
    }
    
    public boolean esComponenteValido() {
        return productoComponente != null && 
               productoComponente.getActivo() && 
               !productoComponente.getEliminado() &&
               cantidad != null && 
               cantidad.compareTo(BigDecimal.ZERO) > 0;
    }
    
    public String getDescripcionComponente() {
        if (productoComponente != null) {
            return productoComponente.getNombre() + " (" + cantidad + " " + unidadMedida + ")";
        }
        return "Componente no válido";
    }
}
