package com.supplyai.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.supplyai.entity.enums.MetodoPago;
import com.supplyai.entity.enums.TipoMovimientoCaja;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entidad para movimientos de caja en el sistema Supply AI
 */
@Entity
@Table(name = "movimientos_caja")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = {"organizacion", "sucursal", "caja"})
public class MovimientoCaja {
    
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
    
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false, length = 30)
    private TipoMovimientoCaja tipo;
    
    @NotNull(message = "El monto es obligatorio")
    @DecimalMin(value = "0.01", inclusive = true, message = "El monto debe ser mayor a 0")
    @Digits(integer = 13, fraction = 2, message = "El monto debe tener máximo 13 dígitos enteros y 2 decimales")
    @Column(name = "monto", nullable = false, precision = 15, scale = 2)
    private BigDecimal monto;
    
    @NotNull(message = "El saldo anterior es obligatorio")
    @DecimalMin(value = "0.0", inclusive = true, message = "El saldo anterior debe ser mayor o igual a 0")
    @Digits(integer = 13, fraction = 2, message = "El saldo anterior debe tener máximo 13 dígitos enteros y 2 decimales")
    @Column(name = "saldo_anterior", nullable = false, precision = 15, scale = 2)
    private BigDecimal saldoAnterior;
    
    @NotNull(message = "El saldo posterior es obligatorio")
    @DecimalMin(value = "0.0", inclusive = true, message = "El saldo posterior debe ser mayor o igual a 0")
    @Digits(integer = 13, fraction = 2, message = "El saldo posterior debe tener máximo 13 dígitos enteros y 2 decimales")
    @Column(name = "saldo_posterior", nullable = false, precision = 15, scale = 2)
    private BigDecimal saldoPosterior;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "metodo_pago", length = 20)
    private MetodoPago metodoPago;
    
    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;
    
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
    }
    
    public boolean esEntrada() {
        return TipoMovimientoCaja.APERTURA.equals(tipo) || 
               TipoMovimientoCaja.DEPOSITO.equals(tipo) || 
               TipoMovimientoCaja.VENTA.equals(tipo) || 
               TipoMovimientoCaja.PROPINA.equals(tipo);
    }
    
    public boolean esSalida() {
        return TipoMovimientoCaja.CIERRE.equals(tipo) || 
               TipoMovimientoCaja.RETIRO.equals(tipo) || 
               TipoMovimientoCaja.AJUSTE.equals(tipo);
    }
    
    public String getTipoFormateado() {
        if (tipo != null) {
            return tipo.getDescripcion();
        }
        return "Tipo no especificado";
    }
}
