package com.supplyai.service;

import com.supplyai.dto.ProductoDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Servicio para la gestión de productos en el sistema Supply AI.
 * Proporciona operaciones CRUD y lógica de negocio relacionada con productos.
 * 
 * @author David Timana
 * @version 1.0
 * @since 2025-01-01
 */
public interface ProductoService {
    
    /**
     * Crea un nuevo producto en el sistema.
     * 
     * @param productoDTO DTO con la información del producto a crear
     * @return ProductoDTO del producto creado con su ID asignado
     * @throws IllegalArgumentException si los datos del producto son inválidos
     */
    ProductoDTO save(ProductoDTO productoDTO);
    
    /**
     * Actualiza un producto existente en el sistema.
     * 
     * @param id ID del producto a actualizar
     * @param productoDTO DTO con la información actualizada del producto
     * @return ProductoDTO del producto actualizado
     * @throws ResourceNotFoundException si el producto no existe
     * @throws IllegalArgumentException si los datos del producto son inválidos
     */
    ProductoDTO update(Long id, ProductoDTO productoDTO);
    
    /**
     * Busca un producto por su ID.
     * 
     * @param id ID del producto a buscar
     * @return ProductoDTO del producto encontrado
     * @throws ResourceNotFoundException si el producto no existe
     */
    ProductoDTO findById(Long id);
    
    /**
     * Busca todos los productos de una sucursal específica con paginación.
     * 
     * @param sucursalId ID de la sucursal
     * @param pageable Configuración de paginación y ordenamiento
     * @return Página de ProductoDTO con los productos de la sucursal
     */
    Page<ProductoDTO> findAllBySucursalId(Long sucursalId, Pageable pageable);
    
    /**
     * Elimina un producto del sistema.
     * 
     * @param id ID del producto a eliminar
     * @throws ResourceNotFoundException si el producto no existe
     */
    void delete(Long id);
}
