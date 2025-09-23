package com.supplyai.dto;

import com.supplyai.entity.enums.TipoProducto;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductoDTO {
    private Long id;
    private Long organizacionId;
    private Long sucursalId;
    private Long categoriaId;
    private String nombre;
    private String descripcion;
    private String codigoBarras;
    private String codigoInterno;
    private String sku;
    private TipoProducto tipo;
    private BigDecimal precioVenta;
    private BigDecimal precioCosto;
    private BigDecimal precioCompra;
    private String unidadMedida;
    private boolean activo;
}
