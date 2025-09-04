package com.supplyai.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad para organizaciones (multi-tenant) en el sistema Supply AI
 */
@Entity
@Table(name = "organizaciones")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = {"sucursales", "usuarios", "productos", "categorias"})
public class Organization {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;
    
    @NotBlank(message = "El nombre de la organización es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;
    
    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;
    
    @Size(max = 20, message = "El RUC debe tener máximo 20 caracteres")
    @Column(name = "ruc", unique = true, length = 20)
    private String ruc;
    
    @Email(message = "El email debe tener un formato válido")
    @Size(max = 100, message = "El email debe tener máximo 100 caracteres")
    @Column(name = "email", unique = true, length = 100)
    private String email;
    
    @Size(max = 20, message = "El teléfono debe tener máximo 20 caracteres")
    @Column(name = "telefono", length = 20)
    private String telefono;
    
    @Column(name = "direccion", columnDefinition = "TEXT")
    private String direccion;
    
    @Size(max = 100, message = "La ciudad debe tener máximo 100 caracteres")
    @Column(name = "ciudad", length = 100)
    private String ciudad;
    
    @Size(max = 100, message = "El país debe tener máximo 100 caracteres")
    @Column(name = "pais", length = 100)
    private String pais;
    
    @Size(max = 10, message = "La moneda debe tener máximo 10 caracteres")
    @Column(name = "moneda", length = 10)
    private String moneda;
    
    @Size(max = 50, message = "La zona horaria debe tener máximo 50 caracteres")
    @Column(name = "zona_horaria", length = 50)
    private String zonaHoraria;
    
    @Size(max = 500, message = "La URL del logo debe tener máximo 500 caracteres")
    @Column(name = "logo_url", length = 500)
    private String logoUrl;
    
    @Column(name = "configuracion", columnDefinition = "JSON")
    private String configuracion;
    
    @Column(name = "metadata", columnDefinition = "JSON")
    private String metadata;
    
    @Column(name = "activa", nullable = false)
    private Boolean activa;
    
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
    @OneToMany(mappedBy = "organizacion", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Sucursal> sucursales = new ArrayList<>();
    
    @OneToMany(mappedBy = "organizacion", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Usuario> usuarios = new ArrayList<>();
    
    @OneToMany(mappedBy = "organizacion", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Producto> productos = new ArrayList<>();
    
    @OneToMany(mappedBy = "organizacion", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Categoria> categorias = new ArrayList<>();
    
    // Métodos de negocio
    @PrePersist
    protected void onCreate() {
        if (activa == null) activa = true;
        if (eliminado == null) eliminado = false;
        if (pais == null) pais = "Perú";
        if (moneda == null) moneda = "PEN";
        if (zonaHoraria == null) zonaHoraria = "America/Lima";
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
    
    public boolean noEstaEliminada() {
        return getEliminado() == null || !getEliminado();
    }
    
    public boolean estaActiva() {
        return getActiva() != null && getActiva();
    }
}
