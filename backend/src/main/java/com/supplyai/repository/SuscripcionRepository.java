package com.supplyai.repository;

import com.supplyai.entity.Suscripcion;
import com.supplyai.entity.enums.EstadoSuscripcion;
import com.supplyai.entity.enums.PlanSuscripcion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface SuscripcionRepository extends JpaRepository<Suscripcion, Long> {

    Optional<Suscripcion> findByOrganizacionId(Long organizacionId);
    
    Page<Suscripcion> findByEstado(EstadoSuscripcion estado, Pageable pageable);

    Page<Suscripcion> findByPlan(PlanSuscripcion plan, Pageable pageable);

    List<Suscripcion> findByFechaVencimientoBefore(LocalDate fecha);
}
