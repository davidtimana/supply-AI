package com.supplyai.entity.enums;

/**
 * Enum para estados de suscripción en el sistema Supply AI
 */
public enum EstadoSuscripcion {
    ACTIVA("Suscripción activa y vigente"),
    SUSPENDIDA("Suscripción suspendida temporalmente"),
    CANCELADA("Suscripción cancelada"),
    VENCIDA("Suscripción vencida"),
    PENDIENTE("Suscripción pendiente de activación");
    
    private final String descripcion;
    
    EstadoSuscripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
}
