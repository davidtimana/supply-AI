package com.supplyai.dto;

import com.supplyai.entity.enums.EstadoCaja;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CajaDTO {
    private Long id;
    private Long organizacionId;
    private Long sucursalId;
    private String nombre;
    private String descripcion;
    private EstadoCaja estado;
    private BigDecimal saldoInicial;
    private BigDecimal saldoActual;
    private boolean activa;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaModificacion;
}
