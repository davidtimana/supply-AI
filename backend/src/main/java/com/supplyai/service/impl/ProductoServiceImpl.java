package com.supplyai.service.impl;

import com.supplyai.dto.ProductoDTO;
import com.supplyai.entity.Producto;
import com.supplyai.exception.ResourceNotFoundException;
import com.supplyai.mapper.ProductoMapper;
import com.supplyai.repository.ProductoRepository;
import com.supplyai.service.ProductoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementación del servicio de gestión de productos.
 * Proporciona la lógica de negocio para operaciones CRUD de productos,
 * incluyendo validaciones y manejo de transacciones.
 * 
 * @author David Timana
 * @version 1.0
 * @since 2025-01-01
 * @see ProductoService
 */
@Service
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;
    private final ProductoMapper productoMapper;

    /**
     * Constructor que inyecta las dependencias necesarias.
     * 
     * @param productoRepository Repositorio para operaciones de persistencia de productos
     * @param productoMapper Mapper para conversión entre entidades y DTOs
     */
    public ProductoServiceImpl(ProductoRepository productoRepository, ProductoMapper productoMapper) {
        this.productoRepository = productoRepository;
        this.productoMapper = productoMapper;
    }

    /**
     * {@inheritDoc}
     * 
     * @implSpec Este método mapea el DTO a entidad, persiste la entidad
     * y retorna el DTO mapeado con el ID asignado.
     */
    @Override
    @Transactional
    public ProductoDTO save(ProductoDTO productoDTO) {
        Producto producto = productoMapper.productoDTOToProducto(productoDTO);
        Producto savedProducto = productoRepository.save(producto);
        return productoMapper.productoToProductoDTO(savedProducto);
    }

    /**
     * {@inheritDoc}
     * 
     * @implSpec Este método busca el producto existente, actualiza sus campos
     * con la información del DTO y persiste los cambios.
     */
    @Override
    @Transactional
    public ProductoDTO update(Long id, ProductoDTO productoDTO) {
        Producto existingProducto = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + id));

        // Actualizar campos
        existingProducto.setNombre(productoDTO.getNombre());
        existingProducto.setDescripcion(productoDTO.getDescripcion());
        existingProducto.setPrecioVenta(productoDTO.getPrecioVenta());
        // ... otros campos que se puedan actualizar

        Producto updatedProducto = productoRepository.save(existingProducto);
        return productoMapper.productoToProductoDTO(updatedProducto);
    }

    /**
     * {@inheritDoc}
     * 
     * @implSpec Este método busca el producto por ID y lanza una excepción
     * si no se encuentra.
     */
    @Override
    @Transactional(readOnly = true)
    public ProductoDTO findById(Long id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + id));
        return productoMapper.productoToProductoDTO(producto);
    }

    /**
     * {@inheritDoc}
     * 
     * @implSpec Este método utiliza el repositorio para buscar productos por sucursal
     * y mapea cada resultado a DTO usando el mapper.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ProductoDTO> findAllBySucursalId(Long sucursalId, Pageable pageable) {
        return productoRepository.findBySucursalId(sucursalId, pageable)
                .map(productoMapper::productoToProductoDTO);
    }

    /**
     * {@inheritDoc}
     * 
     * @implSpec Este método busca el producto por ID antes de eliminarlo
     * para asegurar que existe.
     */
    @Override
    @Transactional
    public void delete(Long id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + id));
        productoRepository.delete(producto);
    }
}
