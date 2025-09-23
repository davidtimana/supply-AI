package com.supplyai.mapper;

import com.supplyai.dto.InventarioDTO;
import com.supplyai.entity.Inventario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface InventarioMapper {
    InventarioMapper INSTANCE = Mappers.getMapper(InventarioMapper.class);

    @Mapping(source = "organizacion.id", target = "organizacionId")
    @Mapping(source = "sucursal.id", target = "sucursalId")
    @Mapping(source = "producto.id", target = "productoId")
    InventarioDTO inventarioToInventarioDTO(Inventario inventario);

    @Mapping(source = "organizacionId", target = "organizacion.id")
    @Mapping(source = "sucursalId", target = "sucursal.id")
    @Mapping(source = "productoId", target = "producto.id")
    Inventario inventarioDTOToInventario(InventarioDTO inventarioDTO);
}
