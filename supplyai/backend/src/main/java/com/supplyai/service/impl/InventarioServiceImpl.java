package com.supplyai.service.impl;

import com.supplyai.dto.InventarioDTO;
import com.supplyai.entity.Inventario;
import com.supplyai.entity.MovimientoInventario;
import com.supplyai.entity.enums.TipoMovimiento;
import com.supplyai.exception.BadRequestException;
import com.supplyai.exception.InsufficientStockException;
import com.supplyai.exception.ResourceNotFoundException;
import com.supplyai.mapper.InventarioMapper;
import com.supplyai.repository.InventarioRepository;
import com.supplyai.repository.MovimientoInventarioRepository;
import com.supplyai.service.InventarioService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Implementación del servicio de gestión de inventarios.
 * Proporciona la lógica de negocio para ajustes de stock,
 * validaciones de disponibilidad y registro de movimientos.
 * 
 * @author David Timana
 * @version 1.0
 * @since 2025-01-01
 * @see InventarioService
 */
@Service
public class InventarioServiceImpl implements InventarioService {

    private final InventarioRepository inventarioRepository;
    private final MovimientoInventarioRepository movimientoInventarioRepository;
    private final InventarioMapper inventarioMapper;

    /**
     * Constructor que inyecta las dependencias necesarias.
     * 
     * @param inventarioRepository Repositorio para operaciones de persistencia de inventarios
     * @param movimientoInventarioRepository Repositorio para operaciones de movimientos de inventario
     * @param inventarioMapper Mapper para conversión entre entidades y DTOs
     */
    public InventarioServiceImpl(InventarioRepository inventarioRepository,
                                 MovimientoInventarioRepository movimientoInventarioRepository,
                                 InventarioMapper inventarioMapper) {
        this.inventarioRepository = inventarioRepository;
        this.movimientoInventarioRepository = movimientoInventarioRepository;
        this.inventarioMapper = inventarioMapper;
    }

    /**
     * {@inheritDoc}
     * 
     * @implSpec Este método implementa la lógica completa de ajuste de inventario:
     * 1. Busca el inventario existente
     * 2. Valida el tipo de movimiento
     * 3. Calcula el nuevo stock según el tipo
     * 4. Actualiza el inventario
     * 5. Registra el movimiento para auditoría
     */
    @Override
    @Transactional
    public InventarioDTO ajustarInventario(Long productoId, Long sucursalId, BigDecimal cantidad, String tipoMovimientoStr) {
        Inventario inventario = inventarioRepository.findByProductoIdAndSucursalId(productoId, sucursalId)
                .orElseThrow(() -> new ResourceNotFoundException("Inventario no encontrado para el producto " + productoId + " en la sucursal " + sucursalId));

        BigDecimal stockAnterior = inventario.getStockActual();
        BigDecimal nuevoStock;

        TipoMovimiento tipoMovimiento = TipoMovimiento.valueOf(tipoMovimientoStr.toUpperCase());

        switch (tipoMovimiento) {
            case ENTRADA:
                nuevoStock = stockAnterior.add(cantidad);
                break;
            case SALIDA:
                if (stockAnterior.compareTo(cantidad) < 0) {
                    throw new InsufficientStockException("Stock insuficiente para el producto " + productoId);
                }
                nuevoStock = stockAnterior.subtract(cantidad);
                break;
            case AJUSTE:
                nuevoStock = cantidad; // El ajuste establece un nuevo valor de stock
                break;
            default:
                throw new BadRequestException("Tipo de movimiento no válido: " + tipoMovimientoStr);
        }

        inventario.setStockActual(nuevoStock);
        Inventario inventarioActualizado = inventarioRepository.save(inventario);

        // Registrar movimiento
        MovimientoInventario movimiento = new MovimientoInventario();
        movimiento.setProducto(inventario.getProducto());
        movimiento.setSucursal(inventario.getSucursal());
        movimiento.setOrganizacion(inventario.getOrganizacion());
        movimiento.setTipo(tipoMovimiento);
        movimiento.setCantidad(cantidad);
        movimiento.setCantidadAnterior(stockAnterior);
        movimiento.setCantidadPosterior(nuevoStock);
        movimiento.setFechaMovimiento(LocalDateTime.now());
        movimientoInventarioRepository.save(movimiento);

        return inventarioMapper.inventarioToInventarioDTO(inventarioActualizado);
    }

    /**
     * {@inheritDoc}
     * 
     * @implSpec Este método busca el inventario por producto y sucursal,
     * mapeando el resultado a DTO.
     */
    @Override
    @Transactional(readOnly = true)
    public InventarioDTO findByProductoIdAndSucursalId(Long productoId, Long sucursalId) {
        Inventario inventario = inventarioRepository.findByProductoIdAndSucursalId(productoId, sucursalId)
                .orElseThrow(() -> new ResourceNotFoundException("Inventario no encontrado"));
        return inventarioMapper.inventarioToInventarioDTO(inventario);
    }

    /**
     * {@inheritDoc}
     * 
     * @implSpec Este método verifica que haya stock suficiente antes de
     * permitir una operación que requiera stock.
     */
    @Override
    @Transactional(readOnly = true)
    public void checkStock(Long productoId, Long sucursalId, BigDecimal cantidadRequerida) {
        Inventario inventario = inventarioRepository.findByProductoIdAndSucursalId(productoId, sucursalId)
                .orElseThrow(() -> new ResourceNotFoundException("Inventario no encontrado"));
        if (inventario.getStockActual().compareTo(cantidadRequerida) < 0) {
            throw new InsufficientStockException("Stock insuficiente para el producto " + productoId);
        }
    }
}
