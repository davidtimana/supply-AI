package com.supplyai.repository;

import com.supplyai.entity.Organization;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization, Long> {

    Optional<Organization> findByRuc(String ruc);

    Optional<Organization> findByEmail(String email);

    Page<Organization> findByActiva(boolean activa, Pageable pageable);
}
