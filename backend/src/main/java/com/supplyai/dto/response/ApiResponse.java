package com.supplyai.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

/**
 * DTO de respuesta estandarizada para todas las operaciones de la API.
 * Proporciona una estructura consistente para respuestas exitosas y errores.
 * 
 * @author David Timana
 * @version 1.0
 * @since 2025-01-01
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    /**
     * Indica si la operación fue exitosa.
     */
    private boolean success;

    /**
     * Mensaje descriptivo de la operación.
     */
    private String message;

    /**
     * Datos de la respuesta (solo en caso de éxito).
     */
    private T data;

    /**
     * Código de error (solo en caso de error).
     */
    private String errorCode;

    /**
     * Timestamp de la respuesta.
     */
    private LocalDateTime timestamp;

    /**
     * Crea una respuesta exitosa con datos.
     * 
     * @param data Datos de la respuesta
     * @param message Mensaje de éxito
     * @param <T> Tipo de los datos
     * @return ApiResponse exitosa
     */
    public static <T> ApiResponse<T> success(T data, String message) {
        return ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }

    /**
     * Crea una respuesta exitosa sin datos.
     * 
     * @param message Mensaje de éxito
     * @param <T> Tipo de los datos
     * @return ApiResponse exitosa
     */
    public static <T> ApiResponse<T> success(String message) {
        return ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
    }

    /**
     * Crea una respuesta de error.
     * 
     * @param message Mensaje de error
     * @param errorCode Código de error
     * @param <T> Tipo de los datos
     * @return ApiResponse de error
     */
    public static <T> ApiResponse<T> error(String message, String errorCode) {
        return ApiResponse.<T>builder()
                .success(false)
                .message(message)
                .errorCode(errorCode)
                .timestamp(LocalDateTime.now())
                .build();
    }

    /**
     * Crea una respuesta de error sin código.
     * 
     * @param message Mensaje de error
     * @param <T> Tipo de los datos
     * @return ApiResponse de error
     */
    public static <T> ApiResponse<T> error(String message) {
        return ApiResponse.<T>builder()
                .success(false)
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
