package com.supplyai.repository;

import com.supplyai.entity.Categoria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    List<Categoria> findByOrganizacionId(Long organizacionId);

    Page<Categoria> findByOrganizacionIdAndActiva(Long organizacionId, boolean activa, Pageable pageable);

    List<Categoria> findByOrganizacionIdAndCategoriaPadreId(Long organizacionId, Long categoriaPadreId);
}
