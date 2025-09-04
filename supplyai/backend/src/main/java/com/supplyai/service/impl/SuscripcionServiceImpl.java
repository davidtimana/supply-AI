package com.supplyai.service.impl;

import com.supplyai.dto.SuscripcionDTO;
import com.supplyai.entity.Suscripcion;
import com.supplyai.entity.enums.PlanSuscripcion;
import com.supplyai.exception.ResourceNotFoundException;
import com.supplyai.mapper.SuscripcionMapper;
import com.supplyai.repository.SuscripcionRepository;
import com.supplyai.service.SuscripcionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementación del servicio de gestión de suscripciones.
 * Proporciona la lógica de negocio para crear suscripciones,
 * cambiar planes y gestionar el estado de las mismas.
 * 
 * @author David Timana
 * @version 1.0
 * @since 2025-01-01
 * @see SuscripcionService
 */
@Service
public class SuscripcionServiceImpl implements SuscripcionService {

    private final SuscripcionRepository suscripcionRepository;
    private final SuscripcionMapper suscripcionMapper;

    /**
     * Constructor que inyecta las dependencias necesarias.
     * 
     * @param suscripcionRepository Repositorio para operaciones de persistencia de suscripciones
     * @param suscripcionMapper Mapper para conversión entre entidades y DTOs
     */
    public SuscripcionServiceImpl(SuscripcionRepository suscripcionRepository, SuscripcionMapper suscripcionMapper) {
        this.suscripcionRepository = suscripcionRepository;
        this.suscripcionMapper = suscripcionMapper;
    }

    /**
     * {@inheritDoc}
     * 
     * @implSpec Este método mapea el DTO a entidad, persiste la entidad
     * y retorna el DTO mapeado con el ID asignado.
     */
    @Override
    @Transactional
    public SuscripcionDTO crearSuscripcion(SuscripcionDTO suscripcionDTO) {
        Suscripcion suscripcion = suscripcionMapper.suscripcionDTOToSuscripcion(suscripcionDTO);
        Suscripcion savedSuscripcion = suscripcionRepository.save(suscripcion);
        return suscripcionMapper.suscripcionToSuscripcionDTO(savedSuscripcion);
    }

    /**
     * {@inheritDoc}
     * 
     * @implSpec Este método implementa el cambio de plan de suscripción:
     * 1. Busca la suscripción activa de la organización
     * 2. Actualiza el plan de suscripción
     * 3. Persiste los cambios
     * 
     * Nota: En una implementación completa, aquí se incluiría lógica
     * para ajustar precios, fechas y límites según el nuevo plan.
     */
    @Override
    @Transactional
    public SuscripcionDTO cambiarPlan(Long organizacionId, String nuevoPlanStr) {
        Suscripcion suscripcion = suscripcionRepository.findByOrganizacionId(organizacionId)
                .orElseThrow(() -> new ResourceNotFoundException("Suscripción no encontrada para la organización: " + organizacionId));

        PlanSuscripcion nuevoPlan = PlanSuscripcion.valueOf(nuevoPlanStr.toUpperCase());
        suscripcion.setPlan(nuevoPlan);
        // Aquí iría la lógica para ajustar precios, fechas, etc.

        Suscripcion updatedSuscripcion = suscripcionRepository.save(suscripcion);
        return suscripcionMapper.suscripcionToSuscripcionDTO(updatedSuscripcion);
    }

    /**
     * {@inheritDoc}
     * 
     * @implSpec Este método busca la suscripción por organización y la mapea a DTO.
     */
    @Override
    @Transactional(readOnly = true)
    public SuscripcionDTO findByOrganizacionId(Long organizacionId) {
        Suscripcion suscripcion = suscripcionRepository.findByOrganizacionId(organizacionId)
                .orElseThrow(() -> new ResourceNotFoundException("Suscripción no encontrada para la organización: " + organizacionId));
        return suscripcionMapper.suscripcionToSuscripcionDTO(suscripcion);
    }
}
