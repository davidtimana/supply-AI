package com.supplyai.entity.enums;

/**
 * Enum para tipos de producto en el sistema Supply AI
 */
public enum TipoProducto {
    SIMPLE("Producto simple"),
    COMPUESTO("Producto compuesto por otros productos"),
    COMBO("Conjunto de productos vendidos juntos");
    
    private final String descripcion;
    
    TipoProducto(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
}
