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
 * Entidad para categorías en el sistema Supply AI
 */
@Entity
@Table(name = "categorias")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = {"organizacion", "categoriaPadre", "subcategorias", "productos"})
public class Categoria {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organizacion_id", nullable = false)
    @JsonIgnore
    private Organization organizacion;
    
    @NotBlank(message = "El nombre de la categoría es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;
    
    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_padre_id")
    @JsonIgnore
    private Categoria categoriaPadre;
    
    @Min(value = 1, message = "El nivel debe ser mayor o igual a 1")
    @Column(name = "nivel", nullable = false)
    private Integer nivel;
    
    @Min(value = 0, message = "El orden debe ser mayor o igual a 0")
    @Column(name = "orden", nullable = false)
    private Integer orden;
    
    @Size(max = 100, message = "El icono debe tener máximo 100 caracteres")
    @Column(name = "icono", length = 100)
    private String icono;
    
    @Pattern(regexp = "^#[0-9A-Fa-f]{6}$", message = "El color debe tener formato hexadecimal válido (ej: #FF0000)")
    @Size(max = 7, message = "El color debe tener máximo 7 caracteres")
    @Column(name = "color", length = 7)
    private String color;
    
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
    @OneToMany(mappedBy = "categoriaPadre", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Categoria> subcategorias = new ArrayList<>();
    
    @OneToMany(mappedBy = "categoria", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Producto> productos = new ArrayList<>();
    
    // Métodos de negocio
    @PrePersist
    protected void onCreate() {
        if (nivel == null) nivel = 1;
        if (orden == null) orden = 0;
        if (activa == null) activa = true;
        if (eliminado == null) eliminado = false;
        if (fechaCreacion == null) fechaCreacion = LocalDateTime.now();
        if (fechaModificacion == null) fechaModificacion = LocalDateTime.now();
        
        // Calcular nivel automáticamente si no se especifica
        if (categoriaPadre != null && nivel == 1) {
            this.nivel = categoriaPadre.getNivel() + 1;
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
    
    public boolean esCategoriaRaiz() {
        return categoriaPadre == null;
    }
    
    public boolean esSubcategoria() {
        return categoriaPadre != null;
    }
    
    public boolean tieneSubcategorias() {
        return !subcategorias.isEmpty();
    }
    
    public boolean tieneProductos() {
        return !productos.isEmpty();
    }
    
    public String getRutaCompleta() {
        if (esCategoriaRaiz()) {
            return nombre;
        }
        return categoriaPadre.getRutaCompleta() + " > " + nombre;
    }
    
    public int getTotalProductos() {
        int total = productos.size();
        for (Categoria subcategoria : subcategorias) {
            total += subcategoria.getTotalProductos();
        }
        return total;
    }
    
    public List<Categoria> getCategoriasHijas() {
        List<Categoria> hijas = new ArrayList<>();
        for (Categoria subcategoria : subcategorias) {
            if (subcategoria.getActiva() && !subcategoria.getEliminado()) {
                hijas.add(subcategoria);
            }
        }
        return hijas;
    }
    
    public List<Producto> getProductosActivos() {
        return productos.stream()
                .filter(p -> p.getActivo() && !p.getEliminado())
                .toList();
    }
}
