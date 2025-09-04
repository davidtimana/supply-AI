package com.supplyai.repository;

import com.supplyai.entity.Venta;
import com.supplyai.entity.enums.EstadoVenta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface VentaRepository extends JpaRepository<Venta, Long> {

    Optional<Venta> findByNumeroTicketAndSucursalId(String numeroTicket, Long sucursalId);

    Page<Venta> findBySucursalId(Long sucursalId, Pageable pageable);

    Page<Venta> findByCajaId(Long cajaId, Pageable pageable);

    Page<Venta> findByEstado(EstadoVenta estado, Pageable pageable);

    Page<Venta> findByFechaVentaBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin, Pageable pageable);
    
    Page<Venta> findBySucursalIdAndFechaVentaBetween(Long sucursalId, LocalDateTime fechaInicio, LocalDateTime fechaFin, Pageable pageable);
}
