package com.supplyai.entity.enums;

/**
 * Enum para estados de venta en el sistema Supply AI
 */
public enum EstadoVenta {
    PENDIENTE("Venta pendiente de procesamiento"),
    COMPLETADA("Venta completada exitosamente"),
    CANCELADA("Venta cancelada"),
    DEVUELTA("Venta devuelta"),
    ANULADA("Venta anulada");
    
    private final String descripcion;
    
    EstadoVenta(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
}
