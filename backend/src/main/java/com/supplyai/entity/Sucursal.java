package com.supplyai.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad para sucursales en el sistema Supply AI
 */
@Entity
@Table(name = "sucursales")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = {"organizacion", "productos", "inventarios", "cajas", "ventas"})
public class Sucursal {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organizacion_id", nullable = false)
    @JsonIgnore
    private Organization organizacion;
    
    @NotBlank(message = "El nombre de la sucursal es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;
    
    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;
    
    @Column(name = "direccion", columnDefinition = "TEXT")
    private String direccion;
    
    @Size(max = 100, message = "La ciudad debe tener máximo 100 caracteres")
    @Column(name = "ciudad", length = 100)
    private String ciudad;
    
    @Size(max = 20, message = "El teléfono debe tener máximo 20 caracteres")
    @Column(name = "telefono", length = 20)
    private String telefono;
    
    @Email(message = "El email debe tener un formato válido")
    @Size(max = 100, message = "El email debe tener máximo 100 caracteres")
    @Column(name = "email", length = 100)
    private String email;
    
    @Column(name = "horario_apertura")
    private LocalTime horarioApertura;
    
    @Column(name = "horario_cierre")
    private LocalTime horarioCierre;
    
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
    
    // Relaciones
    @OneToMany(mappedBy = "sucursal", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Producto> productos = new ArrayList<>();
    
    @OneToMany(mappedBy = "sucursal", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Inventario> inventarios = new ArrayList<>();
    
    @OneToMany(mappedBy = "sucursal", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Caja> cajas = new ArrayList<>();
    
    @OneToMany(mappedBy = "sucursal", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Venta> ventas = new ArrayList<>();
    
    // Métodos de negocio
    @PrePersist
    protected void onCreate() {
        if (activa == null) activa = true;
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
    
    public boolean estaAbierta() {
        if (horarioApertura == null || horarioCierre == null) {
            return false;
        }
        
        LocalTime ahora = LocalTime.now();
        if (horarioApertura.isBefore(horarioCierre)) {
            // Horario normal (ej: 8:00 - 18:00)
            return ahora.isAfter(horarioApertura) && ahora.isBefore(horarioCierre);
        } else {
            // Horario que cruza la medianoche (ej: 22:00 - 6:00)
            return ahora.isAfter(horarioApertura) || ahora.isBefore(horarioCierre);
        }
    }
    
    public String getHorarioFormateado() {
        if (horarioApertura == null || horarioCierre == null) {
            return "No configurado";
        }
        return horarioApertura.toString() + " - " + horarioCierre.toString();
    }
    
    public boolean tieneHorarioConfigurado() {
        return horarioApertura != null && horarioCierre != null;
    }
    
    public boolean estaActiva() {
        return getActiva() != null && getActiva();
    }
    
    public boolean noEstaEliminada() {
        return getEliminado() == null || !getEliminado();
    }
}
