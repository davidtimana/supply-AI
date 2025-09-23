package com.supplyai.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.supplyai.entity.enums.EstadoVenta;
import com.supplyai.entity.enums.MetodoPago;
import com.supplyai.entity.enums.TipoVenta;
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
 * Entidad para ventas en el sistema Supply AI
 */
@Entity
@Table(name = "ventas")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = {"organizacion", "sucursal", "caja", "items"})
public class Venta {
    
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
    @JoinColumn(name = "caja_id", nullable = false)
    @JsonIgnore
    private Caja caja;
    
    @NotBlank(message = "El número de ticket es obligatorio")
    @Size(max = 100, message = "El número de ticket debe tener máximo 100 caracteres")
    @Column(name = "numero_ticket", nullable = false, length = 100)
    private String numeroTicket;
    
    @Size(max = 100, message = "El número de factura debe tener máximo 100 caracteres")
    @Column(name = "numero_factura", length = 100)
    private String numeroFactura;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_venta", nullable = false, length = 20)
    private TipoVenta tipoVenta;
    
    @NotNull(message = "El subtotal es obligatorio")
    @DecimalMin(value = "0.0", inclusive = true, message = "El subtotal debe ser mayor o igual a 0")
    @Digits(integer = 13, fraction = 2, message = "El subtotal debe tener máximo 13 dígitos enteros y 2 decimales")
    @Column(name = "subtotal", nullable = false, precision = 15, scale = 2)
    private BigDecimal subtotal;
    
    @NotNull(message = "El impuesto es obligatorio")
    @DecimalMin(value = "0.0", inclusive = true, message = "El impuesto debe ser mayor o igual a 0")
    @Digits(integer = 13, fraction = 2, message = "El impuesto debe tener máximo 13 dígitos enteros y 2 decimales")
    @Column(name = "impuesto", nullable = false, precision = 15, scale = 2)
    private BigDecimal impuesto;
    
    @NotNull(message = "El descuento es obligatorio")
    @DecimalMin(value = "0.0", inclusive = true, message = "El descuento debe ser mayor o igual a 0")
    @Digits(integer = 13, fraction = 2, message = "El descuento debe tener máximo 13 dígitos enteros y 2 decimales")
    @Column(name = "descuento", nullable = false, precision = 15, scale = 2)
    private BigDecimal descuento;
    
    @NotNull(message = "La propina es obligatoria")
    @DecimalMin(value = "0.0", inclusive = true, message = "La propina debe ser mayor o igual a 0")
    @Digits(integer = 13, fraction = 2, message = "La propina debe tener máximo 13 dígitos enteros y 2 decimales")
    @Column(name = "propina", nullable = false, precision = 15, scale = 2)
    private BigDecimal propina;
    
    @NotNull(message = "El total es obligatorio")
    @DecimalMin(value = "0.0", inclusive = true, message = "El total debe ser mayor o igual a 0")
    @Digits(integer = 13, fraction = 2, message = "El total debe tener máximo 13 dígitos enteros y 2 decimales")
    @Column(name = "total", nullable = false, precision = 15, scale = 2)
    private BigDecimal total;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 20)
    private EstadoVenta estado;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "metodo_pago", nullable = false, length = 20)
    private MetodoPago metodoPago;
    
    @Size(max = 200, message = "El nombre del cliente debe tener máximo 200 caracteres")
    @Column(name = "cliente_nombre", length = 200)
    private String clienteNombre;
    
    @Size(max = 50, message = "El documento del cliente debe tener máximo 50 caracteres")
    @Column(name = "cliente_documento", length = 50)
    private String clienteDocumento;
    
    @Email(message = "El email del cliente debe tener un formato válido")
    @Size(max = 100, message = "El email del cliente debe tener máximo 100 caracteres")
    @Column(name = "cliente_email", length = 100)
    private String clienteEmail;
    
    @Size(max = 20, message = "El teléfono del cliente debe tener máximo 20 caracteres")
    @Column(name = "cliente_telefono", length = 20)
    private String clienteTelefono;
    
    @Column(name = "observaciones", columnDefinition = "TEXT")
    private String observaciones;
    
    @Column(name = "fecha_venta", nullable = false)
    private LocalDateTime fechaVenta;
    
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
    @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<VentaItem> items = new ArrayList<>();
    
    // Métodos de negocio
    @PrePersist
    protected void onCreate() {
        if (tipoVenta == null) tipoVenta = TipoVenta.CONTADO;
        if (subtotal == null) subtotal = BigDecimal.ZERO;
        if (impuesto == null) impuesto = BigDecimal.ZERO;
        if (descuento == null) descuento = BigDecimal.ZERO;
        if (propina == null) propina = BigDecimal.ZERO;
        if (total == null) total = BigDecimal.ZERO;
        if (estado == null) estado = EstadoVenta.PENDIENTE;
        if (metodoPago == null) metodoPago = MetodoPago.EFECTIVO;
        if (fechaVenta == null) fechaVenta = LocalDateTime.now();
        if (eliminado == null) eliminado = false;
        if (fechaCreacion == null) fechaCreacion = LocalDateTime.now();
        if (fechaModificacion == null) fechaModificacion = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        fechaModificacion = LocalDateTime.now();
    }
    
    public void addItem(VentaItem item) {
        items.add(item);
        item.setVenta(this);
    }

    public void removeItem(VentaItem item) {
        items.remove(item);
        item.setVenta(null);
    }

    public void calcularTotales() {
        BigDecimal subtotalCalculado = BigDecimal.ZERO;
        BigDecimal impuestoCalculado = BigDecimal.ZERO;
        for (VentaItem item : this.items) {
            item.calcularSubtotal();
            subtotalCalculado = subtotalCalculado.add(item.getSubtotal());
            impuestoCalculado = impuestoCalculado.add(item.getImpuesto());
        }
        this.subtotal = subtotalCalculado;
        this.impuesto = impuestoCalculado;

        if (this.descuento == null) {
            this.descuento = BigDecimal.ZERO;
        }
        if (this.propina == null) {
            this.propina = BigDecimal.ZERO;
        }

        this.total = this.subtotal.add(this.impuesto).subtract(this.descuento).add(this.propina);
    }

    public void softDelete() {
        this.eliminado = true;
        this.fechaEliminacion = LocalDateTime.now();
        for (VentaItem item : this.items) {
            // Opcional: Marcar items como eliminados si tienen soft delete
        }
    }
    
    public void restore() {
        this.eliminado = false;
        this.fechaEliminacion = null;
    }
    
    public void completarVenta() {
        this.estado = EstadoVenta.COMPLETADA;
        this.fechaVenta = LocalDateTime.now();
    }
    
    public void cancelarVenta() {
        this.estado = EstadoVenta.CANCELADA;
    }
    
    public void devolverVenta() {
        this.estado = EstadoVenta.DEVUELTA;
    }
    
    public void anularVenta() {
        this.estado = EstadoVenta.ANULADA;
    }
    
    public BigDecimal calcularTotal() {
        return subtotal.add(impuesto).add(propina).subtract(descuento);
    }
    
    public boolean estaCompletada() {
        return EstadoVenta.COMPLETADA.equals(this.estado);
    }
    
    public boolean estaPendiente() {
        return EstadoVenta.PENDIENTE.equals(this.estado);
    }
    
    public boolean estaCancelada() {
        return EstadoVenta.CANCELADA.equals(this.estado);
    }
    
    public boolean estaDevuelta() {
        return EstadoVenta.DEVUELTA.equals(this.estado);
    }
    
    public boolean estaAnulada() {
        return EstadoVenta.ANULADA.equals(this.estado);
    }
    
    public boolean noEstaEliminada() {
        return getEliminado() == null || !getEliminado();
    }
}
