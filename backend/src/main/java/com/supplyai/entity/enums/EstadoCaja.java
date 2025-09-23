package com.supplyai.entity.enums;

/**
 * Enum para estados de caja en el sistema Supply AI
 */
public enum EstadoCaja {
    ABIERTA("Caja abierta y operativa"),
    CERRADA("Caja cerrada"),
    EN_MANTENIMIENTO("Caja en mantenimiento"),
    BLOQUEADA("Caja bloqueada por seguridad");
    
    private final String descripcion;
    
    EstadoCaja(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
}
