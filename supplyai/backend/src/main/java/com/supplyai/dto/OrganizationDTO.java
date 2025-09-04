package com.supplyai.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrganizationDTO {

    private Long id;
    private String nombre;
    private String descripcion;
    private String ruc;
    private String email;
    private String telefono;
    private String direccion;
    private String ciudad;
    private String pais;
    private String moneda;
    private String zonaHoraria;
    private String logoUrl;
    private boolean activa;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaModificacion;
}
