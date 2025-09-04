package com.supplyai.mapper;

import com.supplyai.dto.SuscripcionDTO;
import com.supplyai.entity.Suscripcion;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface SuscripcionMapper {
    SuscripcionMapper INSTANCE = Mappers.getMapper(SuscripcionMapper.class);

    @Mapping(source = "organizacion.id", target = "organizacionId")
    SuscripcionDTO suscripcionToSuscripcionDTO(Suscripcion suscripcion);

    @Mapping(source = "organizacionId", target = "organizacion.id")
    Suscripcion suscripcionDTOToSuscripcion(SuscripcionDTO suscripcionDTO);
}
