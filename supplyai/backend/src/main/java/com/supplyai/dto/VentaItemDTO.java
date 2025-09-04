package com.supplyai.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class VentaItemDTO {
    private Long id;
    private Long ventaId;
    private Long productoId;
    private String productoNombre;
    private BigDecimal cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal subtotal;
    private BigDecimal descuento;
    private BigDecimal impuesto;
    private BigDecimal total;
}
