package com.supplyai.repository;

import com.supplyai.entity.MovimientoCaja;
import com.supplyai.entity.enums.TipoMovimientoCaja;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface MovimientoCajaRepository extends JpaRepository<MovimientoCaja, Long> {

    Page<MovimientoCaja> findByCajaId(Long cajaId, Pageable pageable);

    Page<MovimientoCaja> findBySucursalId(Long sucursalId, Pageable pageable);
    
    Page<MovimientoCaja> findByTipo(TipoMovimientoCaja tipo, Pageable pageable);

    Page<MovimientoCaja> findByFechaMovimientoBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin, Pageable pageable);

    Page<MovimientoCaja> findByCajaIdAndFechaMovimientoBetween(Long cajaId, LocalDateTime fechaInicio, LocalDateTime fechaFin, Pageable pageable);
}
