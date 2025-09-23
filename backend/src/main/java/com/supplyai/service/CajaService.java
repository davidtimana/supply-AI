package com.supplyai.service;

import com.supplyai.dto.CajaDTO;
import java.math.BigDecimal;

/**
 * Servicio para la gestión de cajas registradoras en el sistema Supply AI.
 * Proporciona operaciones para abrir y cerrar cajas, así como
 * gestionar el estado y saldos de las mismas.
 * 
 * @author David Timana
 * @version 1.0
 * @since 2025-01-01
 */
public interface CajaService {
    
    /**
     * Abre una caja registradora con un monto inicial.
     * Este método:
     * 1. Verifica que la caja no esté ya abierta
     * 2. Cambia el estado a ABIERTA
     * 3. Establece el saldo inicial y actual
     * 4. Registra el movimiento de apertura
     * 
     * @param cajaId ID de la caja a abrir
     * @param montoInicial Monto inicial con el que se abre la caja
     * @return CajaDTO de la caja abierta
     * @throws ResourceNotFoundException si la caja no existe
     * @throws BadRequestException si la caja ya está abierta
     */
    CajaDTO abrirCaja(Long cajaId, BigDecimal montoInicial);
    
    /**
     * Cierra una caja registradora.
     * Este método:
     * 1. Verifica que la caja no esté ya cerrada
     * 2. Cambia el estado a CERRADA
     * 3. Registra el movimiento de cierre
     * 4. Establece el saldo actual en cero
     * 
     * @param cajaId ID de la caja a cerrar
     * @return CajaDTO de la caja cerrada
     * @throws ResourceNotFoundException si la caja no existe
     * @throws BadRequestException si la caja ya está cerrada
     */
    CajaDTO cerrarCaja(Long cajaId);
    
    /**
     * Busca una caja por su ID.
     * 
     * @param id ID de la caja a buscar
     * @return CajaDTO de la caja encontrada
     * @throws ResourceNotFoundException si la caja no existe
     */
    CajaDTO findById(Long id);
}
