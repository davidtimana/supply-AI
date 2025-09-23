package com.supplyai.repository;

import com.supplyai.entity.Producto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    Page<Producto> findByOrganizacionId(Long organizacionId, Pageable pageable);

    Page<Producto> findBySucursalId(Long sucursalId, Pageable pageable);
    
    Page<Producto> findByCategoriaId(Long categoriaId, Pageable pageable);

    Optional<Producto> findBySkuAndOrganizacionId(String sku, Long organizacionId);
    
    Optional<Producto> findByCodigoInternoAndSucursalId(String codigoInterno, Long sucursalId);
}
