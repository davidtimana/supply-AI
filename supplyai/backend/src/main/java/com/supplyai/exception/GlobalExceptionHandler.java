package com.supplyai.exception;

import com.supplyai.dto.response.ApiResponse;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Manejador global de excepciones para toda la aplicación.
 * Proporciona manejo consistente de errores siguiendo estándares REST.
 * 
 * @author David Timana
 * @version 1.0
 * @since 2025-01-01
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Maneja errores de validación de argumentos de método.
     * 
     * @param ex Excepción de validación
     * @return Respuesta de error con detalles de validación
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        
        String traceId = generateTraceId();
        logger.warn("Validation error - TraceId: {}, Details: {}", traceId, ex.getMessage());
        
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        
        return ResponseEntity.badRequest()
                .body(ApiResponse.error("Datos de entrada inválidos", "VALIDATION_ERROR"));
    }

    /**
     * Maneja errores de validación de restricciones.
     * 
     * @param ex Excepción de violación de restricciones
     * @return Respuesta de error
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiResponse<String>> handleConstraintViolation(
            ConstraintViolationException ex) {
        
        String traceId = generateTraceId();
        logger.warn("Constraint violation - TraceId: {}, Details: {}", traceId, ex.getMessage());
        
        return ResponseEntity.badRequest()
                .body(ApiResponse.error("Restricción de validación violada", "CONSTRAINT_VIOLATION"));
    }

    /**
     * Maneja errores de tipo de argumento incorrecto.
     * 
     * @param ex Excepción de tipo de argumento
     * @return Respuesta de error
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiResponse<String>> handleTypeMismatch(
            MethodArgumentTypeMismatchException ex) {
        
        String traceId = generateTraceId();
        logger.warn("Type mismatch - TraceId: {}, Details: {}", traceId, ex.getMessage());
        
        return ResponseEntity.badRequest()
                .body(ApiResponse.error("Tipo de parámetro incorrecto", "TYPE_MISMATCH"));
    }

    /**
     * Maneja errores de mensaje HTTP no legible.
     * 
     * @param ex Excepción de mensaje no legible
     * @return Respuesta de error
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiResponse<String>> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex) {
        
        String traceId = generateTraceId();
        logger.warn("HTTP message not readable - TraceId: {}, Details: {}", traceId, ex.getMessage());
        
        return ResponseEntity.badRequest()
                .body(ApiResponse.error("Cuerpo de la petición no es legible", "INVALID_REQUEST_BODY"));
    }

    /**
     * Maneja errores de recursos no encontrados.
     * 
     * @param ex Excepción de recurso no encontrado
     * @return Respuesta de error 404
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ApiResponse<String>> handleResourceNotFound(
            ResourceNotFoundException ex) {
        
        String traceId = generateTraceId();
        logger.warn("Resource not found - TraceId: {}, Details: {}", traceId, ex.getMessage());
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(ex.getMessage(), "RESOURCE_NOT_FOUND"));
    }

    /**
     * Maneja errores de solicitudes incorrectas.
     * 
     * @param ex Excepción de solicitud incorrecta
     * @return Respuesta de error 400
     */
    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiResponse<String>> handleBadRequest(
            BadRequestException ex) {
        
        String traceId = generateTraceId();
        logger.warn("Bad request - TraceId: {}, Details: {}", traceId, ex.getMessage());
        
        return ResponseEntity.badRequest()
                .body(ApiResponse.error(ex.getMessage(), "BAD_REQUEST"));
    }

    /**
     * Maneja errores de stock insuficiente.
     * 
     * @param ex Excepción de stock insuficiente
     * @return Respuesta de error 409
     */
    @ExceptionHandler(InsufficientStockException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<ApiResponse<String>> handleInsufficientStock(
            InsufficientStockException ex) {
        
        String traceId = generateTraceId();
        logger.warn("Insufficient stock - TraceId: {}, Details: {}", traceId, ex.getMessage());
        
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ApiResponse.error(ex.getMessage(), "INSUFFICIENT_STOCK"));
    }

    /**
     * Maneja errores de integridad de datos.
     * 
     * @param ex Excepción de integridad de datos
     * @return Respuesta de error 409
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<ApiResponse<String>> handleDataIntegrityViolation(
            DataIntegrityViolationException ex) {
        
        String traceId = generateTraceId();
        logger.warn("Data integrity violation - TraceId: {}, Details: {}", traceId, ex.getMessage());
        
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ApiResponse.error("Conflicto de integridad de datos", "DATA_INTEGRITY_VIOLATION"));
    }

    /**
     * Maneja errores de estado ilegal.
     * 
     * @param ex Excepción de estado ilegal
     * @return Respuesta de error 422
     */
    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ResponseEntity<ApiResponse<String>> handleIllegalState(
            IllegalStateException ex) {
        
        String traceId = generateTraceId();
        logger.warn("Illegal state - TraceId: {}, Details: {}", traceId, ex.getMessage());
        
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(ApiResponse.error(ex.getMessage(), "ILLEGAL_STATE"));
    }

    /**
     * Maneja errores de argumento ilegal.
     * 
     * @param ex Excepción de argumento ilegal
     * @return Respuesta de error 422
     */
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ResponseEntity<ApiResponse<String>> handleIllegalArgument(
            IllegalArgumentException ex) {
        
        String traceId = generateTraceId();
        logger.warn("Illegal argument - TraceId: {}, Details: {}", traceId, ex.getMessage());
        
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(ApiResponse.error(ex.getMessage(), "ILLEGAL_ARGUMENT"));
    }

    /**
     * Manejador genérico para excepciones no manejadas.
     * 
     * @param ex Excepción genérica
     * @return Respuesta de error 500
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ApiResponse<String>> handleGenericException(Exception ex) {
        
        String traceId = generateTraceId();
        logger.error("Unhandled exception - TraceId: {}, Details: {}", traceId, ex.getMessage(), ex);
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Error interno del servidor", "INTERNAL_SERVER_ERROR"));
    }

    /**
     * Genera un ID de trazabilidad único para cada error.
     * 
     * @return ID de trazabilidad
     */
    private String generateTraceId() {
        return UUID.randomUUID().toString().substring(0, 8);
    }
}
