package com.supplyai.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.supplyai.entity.enums.TipoProducto;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad para productos en el sistema Supply AI
 */
@Entity
@Table(name = "productos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = {"organizacion", "sucursal", "categoria", "inventarios", "ventaItems"})
public class Producto {
    
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
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;
    
    @NotBlank(message = "El nombre del producto es obligatorio")
    @Size(min = 2, max = 200, message = "El nombre debe tener entre 2 y 200 caracteres")
    @Column(name = "nombre", nullable = false, length = 200)
    private String nombre;
    
    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;
    
    @Size(max = 100, message = "El código de barras debe tener máximo 100 caracteres")
    @Column(name = "codigo_barras", length = 100)
    private String codigoBarras;
    
    @Size(max = 100, message = "El código interno debe tener máximo 100 caracteres")
    @Column(name = "codigo_interno", length = 100)
    private String codigoInterno;
    
    @Size(max = 100, message = "El SKU debe tener máximo 100 caracteres")
    @Column(name = "sku", length = 100)
    private String sku;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false, length = 20)
    private TipoProducto tipo;
    
    @NotNull(message = "El precio de venta es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El precio de venta debe ser mayor a 0")
    @Digits(integer = 13, fraction = 2, message = "El precio de venta debe tener máximo 13 dígitos enteros y 2 decimales")
    @Column(name = "precio_venta", nullable = false, precision = 15, scale = 2)
    private BigDecimal precioVenta;
    
    @NotNull(message = "El precio de costo es obligatorio")
    @DecimalMin(value = "0.0", inclusive = true, message = "El precio de costo debe ser mayor o igual a 0")
    @Digits(integer = 13, fraction = 2, message = "El precio de costo debe tener máximo 13 dígitos enteros y 2 decimales")
    @Column(name = "precio_costo", nullable = false, precision = 15, scale = 2)
    private BigDecimal precioCosto;
    
    @NotNull(message = "El precio de compra es obligatorio")
    @DecimalMin(value = "0.0", inclusive = true, message = "El precio de compra debe ser mayor o igual a 0")
    @Digits(integer = 13, fraction = 2, message = "El precio de compra debe tener máximo 13 dígitos enteros y 2 decimales")
    @Column(name = "precio_compra", nullable = false, precision = 15, scale = 2)
    private BigDecimal precioCompra;
    
    @DecimalMin(value = "0.0", inclusive = true, message = "El margen de ganancia debe ser mayor o igual a 0")
    @DecimalMax(value = "100.0", inclusive = true, message = "El margen de ganancia debe ser menor o igual a 100")
    @Digits(integer = 3, fraction = 2, message = "El margen de ganancia debe tener máximo 3 dígitos enteros y 2 decimales")
    @Column(name = "margen_ganancia", precision = 5, scale = 2)
    private BigDecimal margenGanancia;
    
    @DecimalMin(value = "0.0", inclusive = true, message = "El porcentaje de impuesto debe ser mayor o igual a 0")
    @DecimalMax(value = "100.0", inclusive = true, message = "El porcentaje de impuesto debe ser menor o igual a 100")
    @Digits(integer = 3, fraction = 2, message = "El porcentaje de impuesto debe tener máximo 3 dígitos enteros y 2 decimales")
    @Column(name = "impuesto_porcentaje", precision = 5, scale = 2)
    private BigDecimal impuestoPorcentaje;
    
    @Size(max = 50, message = "La unidad de medida debe tener máximo 50 caracteres")
    @Column(name = "unidad_medida", length = 50)
    private String unidadMedida;
    
    @DecimalMin(value = "0.0", inclusive = true, message = "El peso debe ser mayor o igual a 0")
    @Digits(integer = 7, fraction = 3, message = "El peso debe tener máximo 7 dígitos enteros y 3 decimales")
    @Column(name = "peso", precision = 10, scale = 3)
    private BigDecimal peso;
    
    @Size(max = 100, message = "Las dimensiones deben tener máximo 100 caracteres")
    @Column(name = "dimensiones", length = 100)
    private String dimensiones;
    
    @Size(max = 500, message = "La URL de la imagen debe tener máximo 500 caracteres")
    @Column(name = "imagen_url", length = 500)
    private String imagenUrl;
    
    @Column(name = "atributos", columnDefinition = "JSON")
    private String atributos;
    
    @Column(name = "etiquetas", columnDefinition = "JSON")
    private String etiquetas;
    
    @Column(name = "activo", nullable = false)
    private Boolean activo;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creado_por")
    private Usuario creadoPor;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "modificado_por")
    private Usuario modificadoPor;
    
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
    
    // Relaciones
    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Inventario> inventarios = new ArrayList<>();
    
    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<VentaItem> ventaItems = new ArrayList<>();
    
    // Métodos de negocio
    @PrePersist
    protected void onCreate() {
        if (tipo == null) tipo = TipoProducto.SIMPLE;
        if (precioVenta == null) precioVenta = BigDecimal.ZERO;
        if (precioCosto == null) precioCosto = BigDecimal.ZERO;
        if (precioCompra == null) precioCompra = BigDecimal.ZERO;
        if (margenGanancia == null) margenGanancia = BigDecimal.ZERO;
        if (impuestoPorcentaje == null) impuestoPorcentaje = BigDecimal.ZERO;
        if (unidadMedida == null) unidadMedida = "unidad";
        if (activo == null) activo = true;
        if (eliminado == null) eliminado = false;
        if (fechaCreacion == null) fechaCreacion = LocalDateTime.now();
        if (fechaModificacion == null) fechaModificacion = LocalDateTime.now();
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
    
    public BigDecimal calcularPrecioConImpuesto() {
        if (impuestoPorcentaje == null || impuestoPorcentaje.equals(BigDecimal.ZERO)) {
            return precioVenta;
        }
        return precioVenta.multiply(BigDecimal.ONE.add(impuestoPorcentaje.divide(BigDecimal.valueOf(100), 2, BigDecimal.ROUND_HALF_UP)));
    }
    
    public BigDecimal calcularMargenGanancia() {
        if (precioCosto == null || precioCosto.equals(BigDecimal.ZERO)) {
            return BigDecimal.ZERO;
        }
        return precioVenta.subtract(precioCosto).divide(precioCosto, 4, BigDecimal.ROUND_HALF_UP)
                .multiply(BigDecimal.valueOf(100));
    }
    
    public boolean noEstaEliminado() {
        return getEliminado() == null || !getEliminado();
    }
    
    public boolean estaActivo() {
        return getActivo() != null && getActivo();
    }
}
