package com.supplyai.service.impl;

import com.supplyai.dto.CajaDTO;
import com.supplyai.entity.Caja;
import com.supplyai.entity.MovimientoCaja;
import com.supplyai.entity.enums.EstadoCaja;
import com.supplyai.entity.enums.TipoMovimientoCaja;
import com.supplyai.exception.BadRequestException;
import com.supplyai.exception.ResourceNotFoundException;
import com.supplyai.mapper.CajaMapper;
import com.supplyai.repository.CajaRepository;
import com.supplyai.repository.MovimientoCajaRepository;
import com.supplyai.service.CajaService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Implementación del servicio de gestión de cajas registradoras.
 * Proporciona la lógica de negocio para abrir y cerrar cajas,
 * gestionar estados y registrar movimientos para auditoría.
 * 
 * @author David Timana
 * @version 1.0
 * @since 2025-01-01
 * @see CajaService
 */
@Service
public class CajaServiceImpl implements CajaService {

    private final CajaRepository cajaRepository;
    private final MovimientoCajaRepository movimientoCajaRepository;
    private final CajaMapper cajaMapper;

    /**
     * Constructor que inyecta las dependencias necesarias.
     * 
     * @param cajaRepository Repositorio para operaciones de persistencia de cajas
     * @param movimientoCajaRepository Repositorio para operaciones de movimientos de caja
     * @param cajaMapper Mapper para conversión entre entidades y DTOs
     */
    public CajaServiceImpl(CajaRepository cajaRepository, MovimientoCajaRepository movimientoCajaRepository, CajaMapper cajaMapper) {
        this.cajaRepository = cajaRepository;
        this.movimientoCajaRepository = movimientoCajaRepository;
        this.cajaMapper = cajaMapper;
    }

    /**
     * {@inheritDoc}
     * 
     * @implSpec Este método implementa el proceso de apertura de caja:
     * 1. Verifica que la caja no esté ya abierta
     * 2. Cambia el estado a ABIERTA
     * 3. Establece el saldo inicial y actual
     * 4. Registra el movimiento de apertura para auditoría
     */
    @Override
    @Transactional
    public CajaDTO abrirCaja(Long cajaId, BigDecimal montoInicial) {
        Caja caja = cajaRepository.findById(cajaId)
                .orElseThrow(() -> new ResourceNotFoundException("Caja no encontrada con id: " + cajaId));

        if (caja.getEstado() == EstadoCaja.ABIERTA) {
            throw new BadRequestException("La caja ya se encuentra abierta.");
        }

        caja.setEstado(EstadoCaja.ABIERTA);
        caja.setSaldoInicial(montoInicial);
        caja.setSaldoActual(montoInicial);
        Caja cajaAbierta = cajaRepository.save(caja);

        registrarMovimiento(cajaAbierta, TipoMovimientoCaja.APERTURA, montoInicial, montoInicial, montoInicial);

        return cajaMapper.cajaToCajaDTO(cajaAbierta);
    }

    /**
     * {@inheritDoc}
     * 
     * @implSpec Este método implementa el proceso de cierre de caja:
     * 1. Verifica que la caja no esté ya cerrada
     * 2. Cambia el estado a CERRADA
     * 3. Registra el movimiento de cierre para auditoría
     * 4. Establece el saldo actual en cero
     */
    @Override
    @Transactional
    public CajaDTO cerrarCaja(Long cajaId) {
        Caja caja = cajaRepository.findById(cajaId)
                .orElseThrow(() -> new ResourceNotFoundException("Caja no encontrada con id: " + cajaId));

        if (caja.getEstado() == EstadoCaja.CERRADA) {
            throw new BadRequestException("La caja ya se encuentra cerrada.");
        }

        caja.setEstado(EstadoCaja.CERRADA);
        Caja cajaCerrada = cajaRepository.save(caja);

        registrarMovimiento(cajaCerrada, TipoMovimientoCaja.CIERRE, caja.getSaldoActual(), caja.getSaldoActual(), BigDecimal.ZERO);
        
        cajaCerrada.setSaldoActual(BigDecimal.ZERO);
        cajaRepository.save(cajaCerrada);
        

        return cajaMapper.cajaToCajaDTO(cajaCerrada);
    }

    /**
     * {@inheritDoc}
     * 
     * @implSpec Este método busca la caja por ID y la mapea a DTO.
     */
    @Override
    @Transactional(readOnly = true)
    public CajaDTO findById(Long id) {
        Caja caja = cajaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Caja no encontrada con id: " + id));
        return cajaMapper.cajaToCajaDTO(caja);
    }

    /**
     * Registra un movimiento de caja para auditoría.
     * Este método privado se utiliza internamente para mantener
     * un registro de todos los cambios en la caja.
     * 
     * @param caja Caja asociada al movimiento
     * @param tipo Tipo de movimiento realizado
     * @param monto Monto del movimiento
     * @param saldoAnterior Saldo antes del movimiento
     * @param saldoPosterior Saldo después del movimiento
     */
    private void registrarMovimiento(Caja caja, TipoMovimientoCaja tipo, BigDecimal monto, BigDecimal saldoAnterior, BigDecimal saldoPosterior) {
        MovimientoCaja movimiento = new MovimientoCaja();
        movimiento.setCaja(caja);
        movimiento.setSucursal(caja.getSucursal());
        movimiento.setOrganizacion(caja.getOrganizacion());
        movimiento.setTipo(tipo);
        movimiento.setMonto(monto);
        movimiento.setSaldoAnterior(saldoAnterior);
        movimiento.setSaldoPosterior(saldoPosterior);
        movimiento.setFechaMovimiento(LocalDateTime.now());
        movimientoCajaRepository.save(movimiento);
    }
}
