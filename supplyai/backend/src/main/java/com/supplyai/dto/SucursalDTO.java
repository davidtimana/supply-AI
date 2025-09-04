package com.supplyai.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
public class SucursalDTO {
    private Long id;
    private Long organizacionId;
    private String nombre;
    private String descripcion;
    private String direccion;
    private String ciudad;
    private String telefono;
    private String email;
    private LocalTime horarioApertura;
    private LocalTime horarioCierre;
    private boolean activa;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaModificacion;
}
