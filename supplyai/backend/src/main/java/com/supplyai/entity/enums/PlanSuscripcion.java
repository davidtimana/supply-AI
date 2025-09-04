package com.supplyai.entity.enums;

/**
 * Enum para planes de suscripción en el sistema Supply AI
 */
public enum PlanSuscripcion {
    GRATUITO("Plan gratuito con funcionalidades básicas"),
    BASICO("Plan básico con funcionalidades estándar"),
    PRO("Plan profesional con funcionalidades avanzadas"),
    ENTERPRISE("Plan empresarial con funcionalidades premium"),
    CUSTOM("Plan personalizado según necesidades");
    
    private final String descripcion;
    
    PlanSuscripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
}
