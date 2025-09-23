package com.supplyai.entity.enums;

/**
 * Enum para tipos de movimiento en el sistema Supply AI
 */
public enum TipoMovimiento {
    ENTRADA("Entrada de productos al inventario"),
    SALIDA("Salida de productos del inventario"),
    AJUSTE("Ajuste de inventario"),
    TRANSFERENCIA("Transferencia entre sucursales"),
    DEVOLUCION("Devolución de productos"),
    MERMA("Pérdida o merma de productos");
    
    private final String descripcion;
    
    TipoMovimiento(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
}
