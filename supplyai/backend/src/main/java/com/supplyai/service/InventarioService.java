package com.supplyai.service;

import com.supplyai.dto.InventarioDTO;
import java.math.BigDecimal;

/**
 * Servicio para la gestión de inventarios en el sistema Supply AI.
 * Proporciona operaciones para ajustar stock, verificar disponibilidad
 * y gestionar movimientos de inventario.
 * 
 * @author David Timana
 * @version 1.0
 * @since 2025-01-01
 */
public interface InventarioService {
    
    /**
     * Ajusta el inventario de un producto en una sucursal específica.
     * Registra el movimiento y actualiza el stock actual.
     * 
     * @param productoId ID del producto
     * @param sucursalId ID de la sucursal
     * @param cantidad Cantidad a ajustar (positiva para entrada, negativa para salida)
     * @param tipoMovimiento Tipo de movimiento: "ENTRADA", "SALIDA", "AJUSTE"
     * @return InventarioDTO con el inventario actualizado
     * @throws ResourceNotFoundException si el inventario no existe
     * @throws InsufficientStockException si no hay stock suficiente para salidas
     * @throws BadRequestException si el tipo de movimiento no es válido
     */
    InventarioDTO ajustarInventario(Long productoId, Long sucursalId, BigDecimal cantidad, String tipoMovimiento);
    
    /**
     * Busca el inventario de un producto específico en una sucursal.
     * 
     * @param productoId ID del producto
     * @param sucursalId ID de la sucursal
     * @return InventarioDTO con la información del inventario
     * @throws ResourceNotFoundException si el inventario no existe
     */
    InventarioDTO findByProductoIdAndSucursalId(Long productoId, Long sucursalId);
    
    /**
     * Verifica si hay stock suficiente para un producto en una sucursal.
     * 
     * @param productoId ID del producto
     * @param sucursalId ID de la sucursal
     * @param cantidadRequerida Cantidad de stock requerida
     * @throws ResourceNotFoundException si el inventario no existe
     * @throws InsufficientStockException si no hay stock suficiente
     */
    void checkStock(Long productoId, Long sucursalId, BigDecimal cantidadRequerida);
}
