package com.supplyai.mapper;

import com.supplyai.dto.CajaDTO;
import com.supplyai.entity.Caja;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CajaMapper {
    CajaMapper INSTANCE = Mappers.getMapper(CajaMapper.class);

    @Mapping(source = "organizacion.id", target = "organizacionId")
    @Mapping(source = "sucursal.id", target = "sucursalId")
    CajaDTO cajaToCajaDTO(Caja caja);

    @Mapping(source = "organizacionId", target = "organizacion.id")
    @Mapping(source = "sucursalId", target = "sucursal.id")
    Caja cajaDTOToCaja(CajaDTO cajaDTO);
}
