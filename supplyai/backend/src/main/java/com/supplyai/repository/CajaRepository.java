package com.supplyai.repository;

import com.supplyai.entity.Caja;
import com.supplyai.entity.enums.EstadoCaja;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CajaRepository extends JpaRepository<Caja, Long> {

    Optional<Caja> findByNombreAndSucursalId(String nombre, Long sucursalId);

    List<Caja> findBySucursalId(Long sucursalId);

    Page<Caja> findBySucursalIdAndEstado(Long sucursalId, EstadoCaja estado, Pageable pageable);
}
