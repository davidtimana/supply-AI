package com.supplyai.repository;

import com.supplyai.entity.MovimientoInventario;
import com.supplyai.entity.enums.TipoMovimiento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface MovimientoInventarioRepository extends JpaRepository<MovimientoInventario, Long> {

    Page<MovimientoInventario> findBySucursalId(Long sucursalId, Pageable pageable);

    Page<MovimientoInventario> findByProductoId(Long productoId, Pageable pageable);

    Page<MovimientoInventario> findByTipo(TipoMovimiento tipo, Pageable pageable);

    Page<MovimientoInventario> findByFechaMovimientoBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin, Pageable pageable);

    Page<MovimientoInventario> findBySucursalIdAndFechaMovimientoBetween(Long sucursalId, LocalDateTime fechaInicio, LocalDateTime fechaFin, Pageable pageable);
}
