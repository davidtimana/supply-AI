package com.supplyai.dto;

import com.supplyai.entity.enums.EstadoVenta;
import com.supplyai.entity.enums.MetodoPago;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class VentaDTO {
    private Long id;
    private Long organizacionId;
    private Long sucursalId;
    private Long cajaId;
    private String numeroTicket;
    private BigDecimal subtotal;
    private BigDecimal impuesto;
    private BigDecimal descuento;
    private BigDecimal total;
    private EstadoVenta estado;
    private MetodoPago metodoPago;
    private LocalDateTime fechaVenta;
    private List<VentaItemDTO> items;
}
