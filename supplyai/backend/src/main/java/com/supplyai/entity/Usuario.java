package com.supplyai.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * Entidad para usuarios del sistema Supply AI
 */
@Entity
@Table(name = "usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = {"organizacion"})
public class Usuario {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organizacion_id", nullable = false)
    @JsonIgnore
    private Organization organizacion;
    
    @NotBlank(message = "El ID de Keycloak es obligatorio")
    @Size(max = 255, message = "El ID de Keycloak debe tener máximo 255 caracteres")
    @Column(name = "keycloak_user_id", nullable = false, unique = true, length = 255)
    private String keycloakUserId;
    
    @NotBlank(message = "El nombre de usuario es obligatorio")
    @Size(min = 3, max = 100, message = "El nombre de usuario debe tener entre 3 y 100 caracteres")
    @Column(name = "username", nullable = false, length = 100)
    private String username;
    
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email debe tener un formato válido")
    @Size(max = 255, message = "El email debe tener máximo 255 caracteres")
    @Column(name = "email", nullable = false, unique = true, length = 255)
    private String email;
    
    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 200, message = "El nombre debe tener entre 2 y 200 caracteres")
    @Column(name = "nombre", nullable = false, length = 200)
    private String nombre;
    
    @NotBlank(message = "El apellido es obligatorio")
    @Size(min = 2, max = 200, message = "El apellido debe tener entre 2 y 200 caracteres")
    @Column(name = "apellido", nullable = false, length = 200)
    private String apellido;
    
    @Size(max = 20, message = "El teléfono debe tener máximo 20 caracteres")
    @Column(name = "telefono", length = 20)
    private String telefono;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "rol_principal", nullable = false, length = 20)
    private RolUsuario rolPrincipal;
    
    @Column(name = "permisos", columnDefinition = "JSON")
    private String permisos;
    
    @Column(name = "ultimo_acceso")
    private LocalDateTime ultimoAcceso;
    
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
    
    // Métodos de negocio
    @PrePersist
    protected void onCreate() {
        if (rolPrincipal == null) rolPrincipal = RolUsuario.USUARIO;
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
    
    public String getNombreCompleto() {
        return nombre + " " + apellido;
    }
    
    public void registrarAcceso() {
        this.ultimoAcceso = LocalDateTime.now();
    }
    
    public boolean noEstaEliminado() {
        return getEliminado() == null || !getEliminado();
    }
    
    public boolean estaActivo() {
        return getActivo() != null && getActivo();
    }
}

/**
 * Enum para roles de usuario
 */
enum RolUsuario {
    SUPER_ADMIN, ADMIN, MANAGER, VENDEDOR, INVENTARIO, CONTADOR, USUARIO
}
