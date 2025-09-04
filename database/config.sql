-- =====================================================
-- SUPPLY AI - CONFIGURACION DE BASE DE DATOS
-- =====================================================
-- Configuraciones y optimizaciones para MySQL 8.0+
-- Autor: David Timana
-- Fecha: 2025-09-03
-- =====================================================

-- Configuraciones de rendimiento para multi-tenancy
SET GLOBAL innodb_buffer_pool_size = 1073741824; -- 1GB
SET GLOBAL innodb_log_file_size = 268435456; -- 256MB
SET GLOBAL innodb_log_buffer_size = 16777216; -- 16MB
SET GLOBAL innodb_flush_log_at_trx_commit = 2;
SET GLOBAL innodb_flush_method = 'O_DIRECT';

-- Configuraciones de conexiones
SET GLOBAL max_connections = 1000;
SET GLOBAL max_connect_errors = 1000000;

-- Configuraciones de consultas
SET GLOBAL query_cache_size = 134217728; -- 128MB
SET GLOBAL query_cache_type = 1;
SET GLOBAL sort_buffer_size = 2097152; -- 2MB
SET GLOBAL read_buffer_size = 1048576; -- 1MB
SET GLOBAL read_rnd_buffer_size = 2097152; -- 2MB

-- Configuraciones de timeouts
SET GLOBAL wait_timeout = 28800; -- 8 horas
SET GLOBAL interactive_timeout = 28800; -- 8 horas
SET GLOBAL lock_wait_timeout = 50;

-- Configuraciones de logs
SET GLOBAL slow_query_log = 1;
SET GLOBAL long_query_time = 2;
SET GLOBAL log_queries_not_using_indexes = 1;

-- Crear usuario específico para la aplicación
CREATE USER IF NOT EXISTS 'supplyai_app'@'localhost' IDENTIFIED BY 'SupplyAI2025!';
GRANT SELECT, INSERT, UPDATE, DELETE ON supplyai.* TO 'supplyai_app'@'localhost';
GRANT EXECUTE ON supplyai.* TO 'supplyai_app'@'localhost';

-- Crear usuario para reportes (solo lectura)
CREATE USER IF NOT EXISTS 'supplyai_reports'@'localhost' IDENTIFIED BY 'Reports2025!';
GRANT SELECT ON supplyai.* TO 'supplyai_reports'@'localhost';

-- Crear usuario para administración
CREATE USER IF NOT EXISTS 'supplyai_admin'@'localhost' IDENTIFIED BY 'Admin2025!';
GRANT ALL PRIVILEGES ON supplyai.* TO 'supplyai_admin'@'localhost';

-- Aplicar privilegios
FLUSH PRIVILEGES;

-- Configuraciones específicas de la base de datos
USE supplyai;

-- Configurar particionamiento para tablas grandes (preparación futura)
-- ALTER TABLE movimientos_inventario PARTITION BY RANGE (YEAR(fecha_movimiento)) (
--     PARTITION p2024 VALUES LESS THAN (2025),
--     PARTITION p2025 VALUES LESS THAN (2026),
--     PARTITION p2026 VALUES LESS THAN (2027),
--     PARTITION p_future VALUES LESS THAN MAXVALUE
-- );

-- Configurar particionamiento para ventas por año
-- ALTER TABLE ventas PARTITION BY RANGE (YEAR(fecha_venta)) (
--     PARTITION p2024 VALUES LESS THAN (2025),
--     PARTITION p2025 VALUES LESS THAN (2026),
--     PARTITION p2026 VALUES LESS THAN (2027),
--     PARTITION p_future VALUES LESS THAN MAXVALUE
-- );

-- Crear índices adicionales para optimización
CREATE INDEX IF NOT EXISTS idx_movimientos_inventario_fecha_producto 
ON movimientos_inventario (fecha_movimiento, producto_id, organizacion_id);

CREATE INDEX IF NOT EXISTS idx_ventas_fecha_estado 
ON ventas (fecha_venta, estado, organizacion_id);

CREATE INDEX IF NOT EXISTS idx_productos_activo_fecha 
ON productos (activo, fecha_creacion, organizacion_id);

-- Configurar variables de sesión para la aplicación
SET SESSION sql_mode = 'STRICT_TRANS_TABLES,NO_ZERO_DATE,NO_ZERO_IN_DATE,ERROR_FOR_DIVISION_BY_ZERO';
SET SESSION time_zone = '+00:00';
SET SESSION autocommit = 1;

-- Crear procedimiento para limpieza de datos antiguos
DELIMITER //
CREATE PROCEDURE sp_limpiar_datos_antiguos(IN dias_antiguedad INT)
BEGIN
    DECLARE fecha_limite DATE;
    SET fecha_limite = DATE_SUB(CURDATE(), INTERVAL dias_antiguedad DAY);
    
    -- Marcar como eliminados registros antiguos de auditoría
    UPDATE auditoria 
    SET eliminado = TRUE, 
        fecha_eliminacion = NOW() 
    WHERE fecha_accion < fecha_limite 
        AND eliminado = FALSE;
    
    -- Limpiar logs de movimientos antiguos (mantener solo 2 años)
    UPDATE movimientos_inventario 
    SET eliminado = TRUE, 
        fecha_eliminacion = NOW() 
    WHERE fecha_movimiento < DATE_SUB(CURDATE(), INTERVAL 2 YEAR) 
        AND eliminado = FALSE;
    
    SELECT ROW_COUNT() as registros_limpiados;
END//
DELIMITER ;

-- Crear procedimiento para análisis de rendimiento
DELIMITER //
CREATE PROCEDURE sp_analisis_rendimiento()
BEGIN
    -- Análisis de índices
    SELECT 
        table_name,
        index_name,
        cardinality,
        sub_part,
        packed,
        null,
        index_type
    FROM information_schema.statistics 
    WHERE table_schema = 'supplyai' 
    ORDER BY table_name, index_name;
    
    -- Análisis de tamaño de tablas
    SELECT 
        table_name,
        ROUND(((data_length + index_length) / 1024 / 1024), 2) AS 'Size (MB)',
        table_rows
    FROM information_schema.tables 
    WHERE table_schema = 'supplyai' 
    ORDER BY (data_length + index_length) DESC;
    
    -- Análisis de consultas lentas
    SELECT 
        start_time,
        query_time,
        lock_time,
        rows_sent,
        rows_examined,
        sql_text
    FROM mysql.slow_log 
    WHERE start_time > DATE_SUB(NOW(), INTERVAL 1 DAY)
    ORDER BY query_time DESC 
    LIMIT 10;
END//
DELIMITER ;

-- Configurar eventos automáticos
SET GLOBAL event_scheduler = ON;

-- Evento para limpieza automática de datos antiguos (ejecutar cada domingo a las 2 AM)
CREATE EVENT IF NOT EXISTS ev_limpieza_automatica
ON SCHEDULE EVERY 1 WEEK
STARTS CURRENT_TIMESTAMP + INTERVAL (7 - WEEKDAY(CURRENT_TIMESTAMP)) DAY + INTERVAL 2 HOUR
DO CALL sp_limpiar_datos_antiguos(90);

-- Evento para análisis de rendimiento semanal
CREATE EVENT IF NOT EXISTS ev_analisis_rendimiento
ON SCHEDULE EVERY 1 WEEK
STARTS CURRENT_TIMESTAMP + INTERVAL (7 - WEEKDAY(CURRENT_TIMESTAMP)) DAY + INTERVAL 3 HOUR
DO CALL sp_analisis_rendimiento();

-- =====================================================
-- FIN DE CONFIGURACION
-- =====================================================
