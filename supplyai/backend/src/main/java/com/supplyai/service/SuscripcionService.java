package com.supplyai.service;

import com.supplyai.dto.SuscripcionDTO;

/**
 * Servicio para la gestión de suscripciones en el sistema Supply AI.
 * Proporciona operaciones para crear suscripciones, cambiar planes
 * y gestionar el estado de las mismas.
 * 
 * @author David Timana
 * @version 1.0
 * @since 2025-01-01
 */
public interface SuscripcionService {
    
    /**
     * Crea una nueva suscripción para una organización.
     * 
     * @param suscripcionDTO DTO con la información de la suscripción a crear
     * @return SuscripcionDTO de la suscripción creada con su ID asignado
     * @throws IllegalArgumentException si los datos de la suscripción son inválidos
     */
    SuscripcionDTO crearSuscripcion(SuscripcionDTO suscripcionDTO);
    
    /**
     * Cambia el plan de suscripción de una organización.
     * Este método:
     * 1. Busca la suscripción activa de la organización
     * 2. Actualiza el plan de suscripción
     * 3. Ajusta automáticamente precios y límites según el nuevo plan
     * 
     * @param organizacionId ID de la organización
     * @param nuevoPlan Nuevo plan de suscripción (GRATUITO, BASICO, PRO, ENTERPRISE, CUSTOM)
     * @return SuscripcionDTO de la suscripción actualizada
     * @throws ResourceNotFoundException si la organización no tiene suscripción activa
     * @throws IllegalArgumentException si el nuevo plan no es válido
     */
    SuscripcionDTO cambiarPlan(Long organizacionId, String nuevoPlan);
    
    /**
     * Busca la suscripción activa de una organización.
     * 
     * @param organizacionId ID de la organización
     * @return SuscripcionDTO de la suscripción encontrada
     * @throws ResourceNotFoundException si la organización no tiene suscripción activa
     */
    SuscripcionDTO findByOrganizacionId(Long organizacionId);
}
