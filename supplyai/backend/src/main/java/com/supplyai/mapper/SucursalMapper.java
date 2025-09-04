package com.supplyai.mapper;

import com.supplyai.dto.SucursalDTO;
import com.supplyai.entity.Sucursal;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface SucursalMapper {

    SucursalMapper INSTANCE = Mappers.getMapper(SucursalMapper.class);

    @Mapping(source = "organizacion.id", target = "organizacionId")
    SucursalDTO sucursalToSucursalDTO(Sucursal sucursal);

    @Mapping(source = "organizacionId", target = "organizacion.id")
    Sucursal sucursalDTOToSucursal(SucursalDTO sucursalDTO);
}
