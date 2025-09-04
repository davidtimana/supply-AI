package com.supplyai.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.supplyai.entity.enums.PlanSuscripcion;
import com.supplyai.entity.enums.EstadoSuscripcion;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Entidad para suscripciones en el sistema Supply AI
 */
@Entity
@Table(name = "suscripciones")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = {"organizacion"})
public class Suscripcion {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organizacion_id", nullable = false)
    @JsonIgnore
    private Organization organizacion;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "plan", nullable = false, length = 20)
    private PlanSuscripcion plan;
    
    @Size(max = 100, message = "El nombre del plan debe tener máximo 100 caracteres")
    @Column(name = "nombre_plan", length = 100)
    private String nombrePlan;
    
    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;
    
    @NotNull(message = "La fecha de inicio es obligatoria")
    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicio;
    
    @NotNull(message = "La fecha de vencimiento es obligatoria")
    @Column(name = "fecha_vencimiento", nullable = false)
    private LocalDate fechaVencimiento;
    
    @Column(name = "fecha_renovacion")
    private LocalDate fechaRenovacion;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 20)
    private EstadoSuscripcion estado;
    
    @NotNull(message = "El precio mensual es obligatorio")
    @DecimalMin(value = "0.0", inclusive = true, message = "El precio mensual debe ser mayor o igual a 0")
    @Digits(integer = 13, fraction = 2, message = "El precio mensual debe tener máximo 13 dígitos enteros y 2 decimales")
    @Column(name = "precio_mensual", nullable = false, precision = 15, scale = 2)
    private BigDecimal precioMensual;
    
    @NotNull(message = "El precio total es obligatorio")
    @DecimalMin(value = "0.0", inclusive = true, message = "El precio total debe ser mayor o igual a 0")
    @Digits(integer = 13, fraction = 2, message = "El precio total debe tener máximo 13 dígitos enteros y 2 decimales")
    @Column(name = "precio_total", nullable = false, precision = 15, scale = 2)
    private BigDecimal precioTotal;
    
    @Min(value = 1, message = "El número de sucursales debe ser mayor o igual a 1")
    @Column(name = "numero_sucursales", nullable = false)
    private Integer numeroSucursales;
    
    @Min(value = 1, message = "El número de usuarios debe ser mayor o igual a 1")
    @Column(name = "numero_usuarios", nullable = false)
    private Integer numeroUsuarios;
    
    @Min(value = 0, message = "El límite de productos debe ser mayor o igual a 0")
    @Column(name = "limite_productos")
    private Integer limiteProductos;
    
    @Min(value = 0, message = "El límite de ventas mensuales debe ser mayor o igual a 0")
    @Column(name = "limite_ventas_mensuales")
    private Integer limiteVentasMensuales;
    
    @Column(name = "caracteristicas", columnDefinition = "JSON")
    private String caracteristicas;
    
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
    
    // Métodos de negocio
    @PrePersist
    protected void onCreate() {
        if (estado == null) estado = EstadoSuscripcion.ACTIVA;
        if (precioMensual == null) precioMensual = BigDecimal.ZERO;
        if (precioTotal == null) precioTotal = BigDecimal.ZERO;
        if (numeroSucursales == null) numeroSucursales = 1;
        if (numeroUsuarios == null) numeroUsuarios = 1;
        if (limiteProductos == null) limiteProductos = 0;
        if (limiteVentasMensuales == null) limiteVentasMensuales = 0;
        if (activa == null) activa = true;
        if (eliminado == null) eliminado = false;
        if (fechaCreacion == null) fechaCreacion = LocalDateTime.now();
        if (fechaModificacion == null) fechaModificacion = LocalDateTime.now();
        
        // Calcular precio total si no se especifica
        if (precioTotal.equals(BigDecimal.ZERO) && precioMensual.compareTo(BigDecimal.ZERO) > 0) {
            calcularPrecioTotal();
        }
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
    
    public void calcularPrecioTotal() {
        if (fechaInicio != null && fechaVencimiento != null && precioMensual.compareTo(BigDecimal.ZERO) > 0) {
            long meses = java.time.temporal.ChronoUnit.MONTHS.between(fechaInicio, fechaVencimiento);
            this.precioTotal = precioMensual.multiply(BigDecimal.valueOf(meses));
        }
    }
    
    public boolean estaVencida() {
        if (fechaVencimiento == null) return false;
        return LocalDate.now().isAfter(fechaVencimiento);
    }
    
    public boolean estaPorVencer() {
        if (fechaVencimiento == null) return false;
        LocalDate hoy = LocalDate.now();
        LocalDate proximoMes = hoy.plusMonths(1);
        return fechaVencimiento.isBefore(proximoMes) && fechaVencimiento.isAfter(hoy);
    }
    
    public boolean necesitaRenovacion() {
        if (fechaVencimiento == null) return false;
        LocalDate hoy = LocalDate.now();
        LocalDate proximaSemana = hoy.plusWeeks(1);
        return fechaVencimiento.isBefore(proximaSemana) && fechaVencimiento.isAfter(hoy);
    }
    
    public int getDiasRestantes() {
        if (fechaVencimiento == null) return 0;
        return (int) java.time.temporal.ChronoUnit.DAYS.between(LocalDate.now(), fechaVencimiento);
    }
    
    public boolean puedeAgregarSucursal() {
        if (limiteProductos == null || limiteProductos == 0) return true;
        return numeroSucursales < limiteProductos;
    }
    
    public boolean puedeAgregarUsuario() {
        if (limiteProductos == null || limiteProductos == 0) return true;
        return numeroUsuarios < limiteProductos;
    }
    
    public void renovarSuscripcion(int meses) {
        if (fechaVencimiento != null) {
            this.fechaRenovacion = this.fechaVencimiento;
            this.fechaVencimiento = this.fechaVencimiento.plusMonths(meses);
            this.fechaInicio = LocalDate.now();
            calcularPrecioTotal();
        }
    }
    
    public void suspenderSuscripcion() {
        this.estado = EstadoSuscripcion.SUSPENDIDA;
    }
    
    public void cancelarSuscripcion() {
        this.estado = EstadoSuscripcion.CANCELADA;
        this.activa = false;
    }
    
    public void activarSuscripcion() {
        this.estado = EstadoSuscripcion.ACTIVA;
        this.activa = true;
    }
    
    public boolean noEstaEliminada() {
        return getEliminado() == null || !getEliminado();
    }
    
    public boolean estaActiva() {
        return getActiva() != null && getActiva();
    }
}
