package com.supplyai.mapper;

import com.supplyai.dto.ProductoDTO;
import com.supplyai.entity.Producto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ProductoMapper {

    ProductoMapper INSTANCE = Mappers.getMapper(ProductoMapper.class);

    @Mapping(source = "organizacion.id", target = "organizacionId")
    @Mapping(source = "sucursal.id", target = "sucursalId")
    @Mapping(source = "categoria.id", target = "categoriaId")
    ProductoDTO productoToProductoDTO(Producto producto);

    @Mapping(source = "organizacionId", target = "organizacion.id")
    @Mapping(source = "sucursalId", target = "sucursal.id")
    @Mapping(source = "categoriaId", target = "categoria.id")
    Producto productoDTOToProducto(ProductoDTO productoDTO);
}
