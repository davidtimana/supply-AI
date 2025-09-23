package com.supplyai.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.supplyai.entity.enums.EstadoCaja;
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
 * Entidad para cajas registradoras en el sistema Supply AI
 * Incluye @Version para optimistic locking
 */
@Entity
@Table(name = "cajas")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = {"organizacion", "sucursal", "movimientosCaja", "ventas"})
public class Caja {
    
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
    
    @NotBlank(message = "El nombre de la caja es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;
    
    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 20)
    private EstadoCaja estado;
    
    @NotNull(message = "El saldo inicial es obligatorio")
    @DecimalMin(value = "0.0", inclusive = true, message = "El saldo inicial debe ser mayor o igual a 0")
    @Digits(integer = 13, fraction = 2, message = "El saldo inicial debe tener máximo 13 dígitos enteros y 2 decimales")
    @Column(name = "saldo_inicial", nullable = false, precision = 15, scale = 2)
    private BigDecimal saldoInicial;
    
    @NotNull(message = "El saldo actual es obligatorio")
    @DecimalMin(value = "0.0", inclusive = true, message = "El saldo actual debe ser mayor o igual a 0")
    @Digits(integer = 13, fraction = 2, message = "El saldo actual debe tener máximo 13 dígitos enteros y 2 decimales")
    @Column(name = "saldo_actual", nullable = false, precision = 15, scale = 2)
    private BigDecimal saldoActual;
    
    @DecimalMin(value = "0.0", inclusive = true, message = "El saldo en efectivo debe ser mayor o igual a 0")
    @Digits(integer = 13, fraction = 2, message = "El saldo en efectivo debe tener máximo 13 dígitos enteros y 2 decimales")
    @Column(name = "saldo_efectivo", precision = 15, scale = 2)
    private BigDecimal saldoEfectivo;
    
    @DecimalMin(value = "0.0", inclusive = true, message = "El saldo en tarjeta debe ser mayor o igual a 0")
    @Digits(integer = 13, fraction = 2, message = "El saldo en tarjeta debe tener máximo 13 dígitos enteros y 2 decimales")
    @Column(name = "saldo_tarjeta", precision = 15, scale = 2)
    private BigDecimal saldoTarjeta;
    
    @DecimalMin(value = "0.0", inclusive = true, message = "El saldo en transferencia debe ser mayor o igual a 0")
    @Digits(integer = 13, fraction = 2, message = "El saldo en transferencia debe tener máximo 13 dígitos enteros y 2 decimales")
    @Column(name = "saldo_transferencia", precision = 15, scale = 2)
    private BigDecimal saldoTransferencia;
    
    @NotNull(message = "El total de ventas es obligatorio")
    @DecimalMin(value = "0.0", inclusive = true, message = "El total de ventas debe ser mayor o igual a 0")
    @Digits(integer = 13, fraction = 2, message = "El total de ventas debe tener máximo 13 dígitos enteros y 2 decimales")
    @Column(name = "total_ventas", nullable = false, precision = 15, scale = 2)
    private BigDecimal totalVentas;
    
    @NotNull(message = "El total de propinas es obligatorio")
    @DecimalMin(value = "0.0", inclusive = true, message = "El total de propinas debe ser mayor o igual a 0")
    @Digits(integer = 13, fraction = 2, message = "El total de propinas debe tener máximo 13 dígitos enteros y 2 decimales")
    @Column(name = "total_propinas", nullable = false, precision = 15, scale = 2)
    private BigDecimal totalPropinas;
    
    @Min(value = 0, message = "El total de transacciones debe ser mayor o igual a 0")
    @Column(name = "total_transacciones")
    private Integer totalTransacciones;
    
    @Column(name = "activa", nullable = false)
    private Boolean activa;
    
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
    
    // Relaciones
    @OneToMany(mappedBy = "caja", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<MovimientoCaja> movimientosCaja = new ArrayList<>();
    
    @OneToMany(mappedBy = "caja", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Venta> ventas = new ArrayList<>();
    
    // Métodos de negocio
    @PrePersist
    protected void onCreate() {
        if (estado == null) estado = EstadoCaja.CERRADA;
        if (saldoInicial == null) saldoInicial = BigDecimal.ZERO;
        if (saldoActual == null) saldoActual = saldoInicial;
        if (saldoEfectivo == null) saldoEfectivo = BigDecimal.ZERO;
        if (saldoTarjeta == null) saldoTarjeta = BigDecimal.ZERO;
        if (saldoTransferencia == null) saldoTransferencia = BigDecimal.ZERO;
        if (totalVentas == null) totalVentas = BigDecimal.ZERO;
        if (totalPropinas == null) totalPropinas = BigDecimal.ZERO;
        if (totalTransacciones == null) totalTransacciones = 0;
        if (activa == null) activa = true;
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
    
    public void abrirCaja() {
        this.estado = EstadoCaja.ABIERTA;
        this.saldoActual = this.saldoInicial;
        this.saldoEfectivo = this.saldoInicial;
        this.saldoTarjeta = BigDecimal.ZERO;
        this.saldoTransferencia = BigDecimal.ZERO;
    }
    
    public void cerrarCaja() {
        this.estado = EstadoCaja.CERRADA;
        // Aquí se podría implementar la lógica de cierre de caja
    }
    
    public void bloquearCaja() {
        this.estado = EstadoCaja.BLOQUEADA;
    }
    
    public void agregarVenta(BigDecimal monto) {
        this.totalVentas = this.totalVentas.add(monto);
        this.totalTransacciones++;
        this.saldoActual = this.saldoActual.add(monto);
    }
    
    public void agregarPropina(BigDecimal monto) {
        this.totalPropinas = this.totalPropinas.add(monto);
        this.saldoActual = this.saldoActual.add(monto);
    }
    
    public BigDecimal getDiferencia() {
        return saldoActual.subtract(saldoInicial);
    }
    
    public boolean estaAbierta() {
        return EstadoCaja.ABIERTA.equals(this.estado);
    }
    
    public boolean estaCerrada() {
        return EstadoCaja.CERRADA.equals(this.estado);
    }
    
    public boolean estaBloqueada() {
        return EstadoCaja.BLOQUEADA.equals(this.estado);
    }
    
    public boolean estaActiva() {
        return getActiva() != null && getActiva();
    }
    
    public boolean noEstaEliminada() {
        return getEliminado() == null || !getEliminado();
    }
}
