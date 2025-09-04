package com.supplyai.entity.enums;

/**
 * Enum para tipos de venta en el sistema Supply AI
 */
public enum TipoVenta {
    CONTADO("Venta al contado"),
    CREDITO("Venta a crédito"),
    CONSIGNACION("Venta en consignación");
    
    private final String descripcion;
    
    TipoVenta(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
}
