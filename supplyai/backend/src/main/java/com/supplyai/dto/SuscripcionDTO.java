package com.supplyai.dto;

import com.supplyai.entity.enums.EstadoSuscripcion;
import com.supplyai.entity.enums.PlanSuscripcion;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class SuscripcionDTO {
    private Long id;
    private Long organizacionId;
    private PlanSuscripcion plan;
    private LocalDate fechaInicio;
    private LocalDate fechaVencimiento;
    private EstadoSuscripcion estado;
    private BigDecimal precioMensual;
    private int numeroSucursales;
    private int numeroUsuarios;
    private boolean activa;
}
