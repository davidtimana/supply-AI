package com.supplyai.service;

import com.supplyai.dto.VentaDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Servicio para la gestión de ventas en el sistema Supply AI.
 * Proporciona operaciones para crear ventas, verificar stock
 * y gestionar el proceso de venta completo.
 * 
 * @author David Timana
 * @version 1.0
 * @since 2025-01-01
 */
public interface VentaService {
    
    /**
     * Crea una nueva venta en el sistema.
     * Este método realiza las siguientes operaciones:
     * 1. Valida el stock disponible para cada producto
     * 2. Crea la venta y sus items
     * 3. Actualiza el inventario restando las cantidades vendidas
     * 4. Calcula automáticamente los totales
     * 
     * @param ventaDTO DTO con la información de la venta y sus items
     * @return VentaDTO de la venta creada con su ID asignado
     * @throws InsufficientStockException si no hay stock suficiente para algún producto
     * @throws IllegalArgumentException si los datos de la venta son inválidos
     */
    VentaDTO crearVenta(VentaDTO ventaDTO);
    
    /**
     * Busca una venta por su ID.
     * 
     * @param id ID de la venta a buscar
     * @return VentaDTO de la venta encontrada
     * @throws ResourceNotFoundException si la venta no existe
     */
    VentaDTO findById(Long id);
    
    /**
     * Busca todas las ventas de una sucursal específica con paginación.
     * 
     * @param sucursalId ID de la sucursal
     * @param pageable Configuración de paginación y ordenamiento
     * @return Página de VentaDTO con las ventas de la sucursal
     */
    Page<VentaDTO> findAllBySucursalId(Long sucursalId, Pageable pageable);
}
