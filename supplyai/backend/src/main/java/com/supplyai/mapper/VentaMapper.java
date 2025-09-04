package com.supplyai.mapper;

import com.supplyai.dto.VentaDTO;
import com.supplyai.dto.VentaItemDTO;
import com.supplyai.entity.Venta;
import com.supplyai.entity.VentaItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface VentaMapper {
    VentaMapper INSTANCE = Mappers.getMapper(VentaMapper.class);

    @Mapping(source = "organizacion.id", target = "organizacionId")
    @Mapping(source = "sucursal.id", target = "sucursalId")
    @Mapping(source = "caja.id", target = "cajaId")
    VentaDTO ventaToVentaDTO(Venta venta);

    @Mapping(source = "organizacionId", target = "organizacion.id")
    @Mapping(source = "sucursalId", target = "sucursal.id")
    @Mapping(source = "cajaId", target = "caja.id")
    Venta ventaDTOToVenta(VentaDTO ventaDTO);

    @Mapping(source = "venta.id", target = "ventaId")
    @Mapping(source = "producto.id", target = "productoId")
    @Mapping(source = "producto.nombre", target = "productoNombre")
    VentaItemDTO ventaItemToVentaItemDTO(VentaItem ventaItem);

    @Mapping(source = "ventaId", target = "venta.id")
    @Mapping(source = "productoId", target = "producto.id")
    VentaItem ventaItemDTOToVentaItem(VentaItemDTO ventaItemDTO);

    List<VentaItemDTO> ventaItemsToVentaItemDTOs(List<VentaItem> ventaItems);
}
