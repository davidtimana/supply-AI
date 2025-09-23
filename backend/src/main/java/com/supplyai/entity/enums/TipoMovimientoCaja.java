package com.supplyai.entity.enums;

/**
 * Enum para tipos de movimiento de caja en el sistema Supply AI
 */
public enum TipoMovimientoCaja {
    APERTURA("Apertura de caja"),
    CIERRE("Cierre de caja"),
    VENTA("Venta realizada"),
    RETIRO("Retiro de efectivo"),
    DEPOSITO("Depósito de efectivo"),
    PROPINA("Propina recibida"),
    AJUSTE("Ajuste de caja"),
    TRANSFERENCIA("Transferencia entre cajas"),
    DEVOLUCION("Devolución de venta");
    
    private final String descripcion;
    
    TipoMovimientoCaja(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
}
