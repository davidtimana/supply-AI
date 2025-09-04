package com.supplyai.repository;

import com.supplyai.entity.Sucursal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SucursalRepository extends JpaRepository<Sucursal, Long> {

    List<Sucursal> findByOrganizacionId(Long organizacionId);

    Page<Sucursal> findByOrganizacionIdAndActiva(Long organizacionId, boolean activa, Pageable pageable);
}
