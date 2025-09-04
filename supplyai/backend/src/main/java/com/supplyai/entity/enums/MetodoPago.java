package com.supplyai.entity.enums;

/**
 * Enum para métodos de pago en el sistema Supply AI
 */
public enum MetodoPago {
    EFECTIVO("Pago en efectivo"),
    TARJETA_CREDITO("Pago con tarjeta de crédito"),
    TARJETA_DEBITO("Pago con tarjeta de débito"),
    TRANSFERENCIA("Transferencia bancaria"),
    QR("Pago con código QR"),
    MIXTO("Pago con múltiples métodos"),
    OTRO("Otro método de pago");
    
    private final String descripcion;
    
    MetodoPago(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
}
