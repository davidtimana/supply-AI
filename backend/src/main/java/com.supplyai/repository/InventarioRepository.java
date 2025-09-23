package com.supplyai.repository;

import com.supplyai.entity.Inventario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InventarioRepository extends JpaRepository<Inventario, Long> {

    Optional<Inventario> findByProductoIdAndSucursalId(Long productoId, Long sucursalId);

    Page<Inventario> findBySucursalId(Long sucursalId, Pageable pageable);

    @Query("SELECT i FROM Inventario i WHERE i.sucursal.id = :sucursalId AND i.stockActual <= i.stockMinimo")
    Page<Inventario> findByStockBajo(@Param("sucursalId") Long sucursalId, Pageable pageable);

    @Query("SELECT i FROM Inventario i WHERE i.sucursal.id = :sucursalId AND i.stockActual <= i.puntoReorden")
    Page<Inventario> findByPuntoReorden(@Param("sucursalId") Long sucursalId, Pageable pageable);
}
