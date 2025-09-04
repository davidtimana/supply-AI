package com.supplyai.mapper;

import com.supplyai.dto.CategoriaDTO;
import com.supplyai.entity.Categoria;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CategoriaMapper {
    CategoriaMapper INSTANCE = Mappers.getMapper(CategoriaMapper.class);

    @Mapping(source = "organizacion.id", target = "organizacionId")
    @Mapping(source = "categoriaPadre.id", target = "categoriaPadreId")
    CategoriaDTO categoriaToCategoriaDTO(Categoria categoria);

    @Mapping(source = "organizacionId", target = "organizacion.id")
    @Mapping(source = "categoriaPadreId", target = "categoriaPadre.id")
    Categoria categoriaDTOToCategoria(CategoriaDTO categoriaDTO);
}
