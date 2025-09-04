package com.supplyai.repository;

import com.supplyai.entity.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByKeycloakUserId(String keycloakUserId);

    Optional<Usuario> findByEmail(String email);

    Page<Usuario> findByOrganizacionId(Long organizacionId, Pageable pageable);
    
    Page<Usuario> findByOrganizacionIdAndActivo(Long organizacionId, boolean activo, Pageable pageable);
}
