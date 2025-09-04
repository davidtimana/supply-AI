package com.supplyai.repository;

import com.supplyai.entity.ProductoComponente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoComponenteRepository extends JpaRepository<ProductoComponente, Long> {

    List<ProductoComponente> findByProductoPrincipalId(Long productoPrincipalId);
    
}
