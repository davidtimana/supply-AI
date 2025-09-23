package com.supplyai.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class InventarioDTO {
    private Long id;
    private Long organizacionId;
    private Long sucursalId;
    private Long productoId;
    private BigDecimal stockActual;
    private BigDecimal stockMinimo;
    private BigDecimal stockMaximo;
    private String ubicacion;
    private boolean activo;
}
