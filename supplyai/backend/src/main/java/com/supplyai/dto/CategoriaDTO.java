package com.supplyai.dto;

import lombok.Data;

@Data
public class CategoriaDTO {
    private Long id;
    private Long organizacionId;
    private String nombre;
    private String descripcion;
    private Long categoriaPadreId;
    private boolean activa;
}
