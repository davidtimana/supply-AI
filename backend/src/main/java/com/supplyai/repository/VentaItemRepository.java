package com.supplyai.repository;

import com.supplyai.entity.VentaItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VentaItemRepository extends JpaRepository<VentaItem, Long> {

    List<VentaItem> findByVentaId(Long ventaId);

    List<VentaItem> findByProductoId(Long productoId);
}
