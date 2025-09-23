package com.supplyai.service.impl;

import com.supplyai.dto.VentaDTO;
import com.supplyai.dto.VentaItemDTO;
import com.supplyai.entity.Venta;
import com.supplyai.entity.VentaItem;
import com.supplyai.exception.ResourceNotFoundException;
import com.supplyai.mapper.VentaMapper;
import com.supplyai.repository.VentaRepository;
import com.supplyai.service.InventarioService;
import com.supplyai.service.VentaService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

/**
 * Implementación del servicio de gestión de ventas.
 * Proporciona la lógica de negocio para crear ventas, validar stock
 * y gestionar el proceso completo de venta con actualización de inventario.
 * 
 * @author David Timana
 * @version 1.0
 * @since 2025-01-01
 * @see VentaService
 */
@Service
public class VentaServiceImpl implements VentaService {

    private final VentaRepository ventaRepository;
    private final VentaMapper ventaMapper;
    private final InventarioService inventarioService;

    /**
     * Constructor que inyecta las dependencias necesarias.
     * 
     * @param ventaRepository Repositorio para operaciones de persistencia de ventas
     * @param ventaMapper Mapper para conversión entre entidades y DTOs
     * @param inventarioService Servicio para operaciones de inventario
     */
    public VentaServiceImpl(VentaRepository ventaRepository, VentaMapper ventaMapper, InventarioService inventarioService) {
        this.ventaRepository = ventaRepository;
        this.ventaMapper = ventaMapper;
        this.inventarioService = inventarioService;
    }

    /**
     * {@inheritDoc}
     * 
     * @implSpec Este método implementa el proceso completo de creación de venta:
     * 1. Valida el stock disponible para cada producto
     * 2. Mapea el DTO a entidad y establece relaciones bidireccionales
     * 3. Calcula automáticamente los totales de la venta
     * 4. Persiste la venta y sus items
     * 5. Actualiza el inventario restando las cantidades vendidas
     */
    @Override
    @Transactional
    public VentaDTO crearVenta(VentaDTO ventaDTO) {
        // 1. Validar stock para cada item
        ventaDTO.getItems().forEach(itemDTO -> 
            inventarioService.checkStock(itemDTO.getProductoId(), ventaDTO.getSucursalId(), itemDTO.getCantidad())
        );

        // 2. Mapear DTO a Entidad
        Venta venta = ventaMapper.ventaDTOToVenta(ventaDTO);
        venta.getItems().forEach(item -> item.setVenta(venta)); // Establecer la relación bidireccional

        // 3. Calcular totales
        venta.calcularTotales();

        // 4. Guardar la venta y los items
        Venta savedVenta = ventaRepository.save(venta);

        // 5. Actualizar el stock
        savedVenta.getItems().forEach(item -> 
            inventarioService.ajustarInventario(
                item.getProducto().getId(), 
                savedVenta.getSucursal().getId(), 
                item.getCantidad(), 
                "SALIDA"
            )
        );

        return ventaMapper.ventaToVentaDTO(savedVenta);
    }

    /**
     * {@inheritDoc}
     * 
     * @implSpec Este método busca la venta por ID y la mapea a DTO.
     */
    @Override
    @Transactional(readOnly = true)
    public VentaDTO findById(Long id) {
        Venta venta = ventaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Venta no encontrada con id: " + id));
        return ventaMapper.ventaToVentaDTO(venta);
    }

    /**
     * {@inheritDoc}
     * 
     * @implSpec Este método utiliza el repositorio para buscar ventas por sucursal
     * y mapea cada resultado a DTO usando el mapper.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<VentaDTO> findAllBySucursalId(Long sucursalId, Pageable pageable) {
        return ventaRepository.findBySucursalId(sucursalId, pageable)
                .map(ventaMapper::ventaToVentaDTO);
    }
}
