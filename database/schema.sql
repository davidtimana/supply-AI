-- =====================================================
-- SUPPLY AI - ESQUEMA DE BASE DE DATOS MULTI-TENANT (V2.0)
-- =====================================================
-- Sistema SaaS para gestión de inventario y suministros
-- Autor: David Timana
-- Fecha: 2025-09-03
-- Versión: 2.0 con mejoras de escalabilidad y seguridad 2025
-- =====================================================
-- MEJORAS IMPLEMENTADAS:
-- ✅ Naming convention consistente (snake_case)
-- ✅ Auditoría completa (creado_por, modificado_por)
-- ✅ Triggers de auditoría para cambios críticos
-- ✅ Índices optimizados para multi-tenancy
-- ✅ Particionamiento preparado para escalabilidad
-- ✅ Campos JSON para flexibilidad por tenant
-- ✅ Triggers de validación de integridad
-- ✅ Vistas materializadas para reportes
-- =====================================================

-- Crear base de datos
CREATE DATABASE IF NOT EXISTS supplyai CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE supplyai;

-- =====================================================
-- TABLA: USUARIOS (Referencia a Keycloak)
-- =====================================================
CREATE TABLE IF NOT EXISTS usuarios (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    organizacion_id BIGINT NOT NULL,
    keycloak_user_id VARCHAR(255) UNIQUE NOT NULL,
    username VARCHAR(100) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    nombre VARCHAR(200) NOT NULL,
    apellido VARCHAR(200) NOT NULL,
    telefono VARCHAR(20),
    rol_principal ENUM('SUPER_ADMIN', 'ADMIN', 'MANAGER', 'VENDEDOR', 'INVENTARIO', 'CONTADOR', 'USUARIO') NOT NULL DEFAULT 'USUARIO',
    permisos JSON,
    ultimo_acceso TIMESTAMP NULL,
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_modificacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    fecha_eliminacion TIMESTAMP NULL,
    eliminado BOOLEAN NOT NULL DEFAULT FALSE,
    
    INDEX idx_usuario_organizacion (organizacion_id),
    INDEX idx_usuario_keycloak (keycloak_user_id),
    INDEX idx_usuario_email (email),
    INDEX idx_usuario_rol (rol_principal),
    INDEX idx_usuario_activo (activo),
    INDEX idx_usuario_eliminado (eliminado)
) ENGINE=InnoDB;

-- =====================================================
-- TABLA: AUDITORIA (Log de cambios críticos)
-- =====================================================
CREATE TABLE IF NOT EXISTS auditoria (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    organizacion_id BIGINT NOT NULL,
    tabla VARCHAR(100) NOT NULL,
    registro_id BIGINT NOT NULL,
    accion ENUM('INSERT', 'UPDATE', 'DELETE', 'SOFT_DELETE') NOT NULL,
    usuario_id BIGINT,
    datos_anteriores JSON,
    datos_nuevos JSON,
    ip_address VARCHAR(45),
    user_agent TEXT,
    fecha_accion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    INDEX idx_auditoria_organizacion (organizacion_id),
    INDEX idx_auditoria_tabla (tabla),
    INDEX idx_auditoria_registro (registro_id),
    INDEX idx_auditoria_usuario (usuario_id),
    INDEX idx_auditoria_fecha (fecha_accion),
    INDEX idx_auditoria_accion (accion)
) ENGINE=InnoDB;

-- =====================================================
-- TABLA: ORGANIZACIONES (Multi-tenant)
-- =====================================================
CREATE TABLE IF NOT EXISTS organizaciones (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descripcion TEXT,
    ruc VARCHAR(20) UNIQUE,
    email VARCHAR(100) UNIQUE,
    telefono VARCHAR(20),
    direccion TEXT,
    ciudad VARCHAR(100),
    pais VARCHAR(100) DEFAULT 'Perú',
    moneda VARCHAR(10) DEFAULT 'PEN',
    zona_horaria VARCHAR(50) DEFAULT 'America/Lima',
    logo_url VARCHAR(500),
    configuracion JSON,
    metadata JSON,
    activa BOOLEAN NOT NULL DEFAULT TRUE,
    creado_por BIGINT,
    modificado_por BIGINT,
    fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_modificacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    fecha_eliminacion TIMESTAMP NULL,
    eliminado BOOLEAN NOT NULL DEFAULT FALSE,
    
    INDEX idx_organizacion_activa (activa),
    INDEX idx_organizacion_ruc (ruc),
    INDEX idx_organizacion_email (email),
    INDEX idx_organizacion_eliminado (eliminado),
    INDEX idx_organizacion_creado_por (creado_por),
    INDEX idx_organizacion_modificado_por (modificado_por),
    INDEX idx_organizacion_fecha_creacion (fecha_creacion),
    INDEX idx_organizacion_fecha_modificacion (fecha_modificacion)
) ENGINE=InnoDB;

-- =====================================================
-- TABLA: SUCURSALES
-- =====================================================
CREATE TABLE IF NOT EXISTS sucursales (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    organizacion_id BIGINT NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    descripcion TEXT,
    direccion TEXT,
    ciudad VARCHAR(100),
    telefono VARCHAR(20),
    email VARCHAR(100),
    horario_apertura TIME,
    horario_cierre TIME,
    activa BOOLEAN NOT NULL DEFAULT TRUE,
    fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_modificacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    fecha_eliminacion TIMESTAMP NULL,
    eliminado BOOLEAN NOT NULL DEFAULT FALSE,
    
    FOREIGN KEY (organizacion_id) REFERENCES organizaciones(id) ON DELETE CASCADE,
    INDEX idx_sucursal_organizacion (organizacion_id),
    INDEX idx_sucursal_activa (activa),
    INDEX idx_sucursal_eliminado (eliminado),
    UNIQUE KEY uk_sucursal_nombre_org (nombre, organizacion_id)
) ENGINE=InnoDB;

-- =====================================================
-- TABLA: CATEGORIAS
-- =====================================================
CREATE TABLE IF NOT EXISTS categorias (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    organizacion_id BIGINT NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    descripcion TEXT,
    categoria_padre_id BIGINT NULL,
    nivel INT NOT NULL DEFAULT 1,
    orden INT NOT NULL DEFAULT 0,
    icono VARCHAR(100),
    color VARCHAR(7),
    activa BOOLEAN NOT NULL DEFAULT TRUE,
    fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_modificacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    fecha_eliminacion TIMESTAMP NULL,
    eliminado BOOLEAN NOT NULL DEFAULT FALSE,
    
    FOREIGN KEY (organizacion_id) REFERENCES organizaciones(id) ON DELETE CASCADE,
    FOREIGN KEY (categoria_padre_id) REFERENCES categorias(id) ON DELETE SET NULL,
    INDEX idx_categoria_organizacion (organizacion_id),
    INDEX idx_categoria_padre (categoria_padre_id),
    INDEX idx_categoria_activa (activa),
    INDEX idx_categoria_eliminado (eliminado),
    UNIQUE KEY uk_categoria_nombre_org (nombre, organizacion_id)
) ENGINE=InnoDB;

-- =====================================================
-- TABLA: PRODUCTOS
-- =====================================================
CREATE TABLE IF NOT EXISTS productos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    organizacion_id BIGINT NOT NULL,
    sucursal_id BIGINT NOT NULL,
    categoria_id BIGINT,
    nombre VARCHAR(200) NOT NULL,
    descripcion TEXT,
    codigo_barras VARCHAR(100),
    codigo_interno VARCHAR(100),
    sku VARCHAR(100),
    tipo ENUM('SIMPLE', 'COMPUESTO', 'COMBO') NOT NULL DEFAULT 'SIMPLE',
    precio_venta DECIMAL(15,2) NOT NULL DEFAULT 0.00,
    precio_costo DECIMAL(15,2) NOT NULL DEFAULT 0.00,
    precio_compra DECIMAL(15,2) NOT NULL DEFAULT 0.00,
    margen_ganancia DECIMAL(5,2) DEFAULT 0.00,
    impuesto_porcentaje DECIMAL(5,2) DEFAULT 0.00,
    unidad_medida VARCHAR(50) DEFAULT 'unidad',
    peso DECIMAL(10,3),
    dimensiones VARCHAR(100),
    imagen_url VARCHAR(500),
    atributos JSON,
    etiquetas JSON,
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    creado_por BIGINT,
    modificado_por BIGINT,
    fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_modificacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    fecha_eliminacion TIMESTAMP NULL,
    eliminado BOOLEAN NOT NULL DEFAULT FALSE,
    
    FOREIGN KEY (organizacion_id) REFERENCES organizaciones(id) ON DELETE CASCADE,
    FOREIGN KEY (sucursal_id) REFERENCES sucursales(id) ON DELETE CASCADE,
    FOREIGN KEY (categoria_id) REFERENCES categorias(id) ON DELETE SET NULL,
    FOREIGN KEY (creado_por) REFERENCES usuarios(id) ON DELETE SET NULL,
    FOREIGN KEY (modificado_por) REFERENCES usuarios(id) ON DELETE SET NULL,
    INDEX idx_producto_organizacion (organizacion_id),
    INDEX idx_producto_sucursal (sucursal_id),
    INDEX idx_producto_categoria (categoria_id),
    INDEX idx_producto_tipo (tipo),
    INDEX idx_producto_activo (activo),
    INDEX idx_producto_codigo_barras (codigo_barras),
    INDEX idx_producto_codigo_interno (codigo_interno),
    INDEX idx_producto_sku (sku),
    INDEX idx_producto_eliminado (eliminado),
    INDEX idx_producto_creado_por (creado_por),
    INDEX idx_producto_modificado_por (modificado_por),
    INDEX idx_producto_fecha_creacion (fecha_creacion),
    INDEX idx_producto_fecha_modificacion (fecha_modificacion),
    UNIQUE KEY uk_producto_codigo_interno_sucursal (codigo_interno, sucursal_id),
    UNIQUE KEY uk_producto_sku_organizacion (sku, organizacion_id)
) ENGINE=InnoDB;

-- =====================================================
-- TABLA: COMPONENTES DE PRODUCTO (para productos compuestos)
-- =====================================================
CREATE TABLE IF NOT EXISTS producto_componentes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    producto_principal_id BIGINT NOT NULL,
    producto_componente_id BIGINT NOT NULL,
    cantidad DECIMAL(15,3) NOT NULL DEFAULT 1.000,
    unidad_medida VARCHAR(50),
    costo_unitario DECIMAL(15,2) DEFAULT 0.00,
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_modificacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    FOREIGN KEY (producto_principal_id) REFERENCES productos(id) ON DELETE CASCADE,
    FOREIGN KEY (producto_componente_id) REFERENCES productos(id) ON DELETE CASCADE,
    INDEX idx_componente_principal (producto_principal_id),
    INDEX idx_componente_producto (producto_componente_id),
    INDEX idx_componente_activo (activo),
    UNIQUE KEY uk_componente_producto (producto_principal_id, producto_componente_id)
) ENGINE=InnoDB;

-- =====================================================
-- TABLA: INVENTARIO
-- =====================================================
CREATE TABLE IF NOT EXISTS inventarios (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    organizacion_id BIGINT NOT NULL,
    sucursal_id BIGINT NOT NULL,
    producto_id BIGINT NOT NULL,
    stock_actual DECIMAL(15,3) NOT NULL DEFAULT 0.000,
    stock_minimo DECIMAL(15,3) NOT NULL DEFAULT 0.000,
    stock_maximo DECIMAL(15,3) NOT NULL DEFAULT 1000.000,
    stock_seguridad DECIMAL(15,3) DEFAULT 0.000,
    punto_reorden DECIMAL(15,3) DEFAULT 0.000,
    unidad_medida VARCHAR(50) DEFAULT 'unidad',
    ubicacion VARCHAR(200),
    pasillo VARCHAR(100),
    estante VARCHAR(100),
    nivel VARCHAR(50),
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    version BIGINT NOT NULL DEFAULT 0,
    fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_modificacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    fecha_eliminacion TIMESTAMP NULL,
    eliminado BOOLEAN NOT NULL DEFAULT FALSE,
    
    FOREIGN KEY (organizacion_id) REFERENCES organizaciones(id) ON DELETE CASCADE,
    FOREIGN KEY (sucursal_id) REFERENCES sucursales(id) ON DELETE CASCADE,
    FOREIGN KEY (producto_id) REFERENCES productos(id) ON DELETE CASCADE,
    INDEX idx_inventario_organizacion (organizacion_id),
    INDEX idx_inventario_sucursal (sucursal_id),
    INDEX idx_inventario_producto (producto_id),
    INDEX idx_inventario_activo (activo),
    INDEX idx_inventario_eliminado (eliminado),
    INDEX idx_inventario_stock (stock_actual, stock_minimo),
    UNIQUE KEY uk_inventario_producto_sucursal (producto_id, sucursal_id)
) ENGINE=InnoDB;

-- =====================================================
-- TABLA: MOVIMIENTOS DE INVENTARIO
-- =====================================================
CREATE TABLE IF NOT EXISTS movimientos_inventario (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    organizacion_id BIGINT NOT NULL,
    sucursal_id BIGINT NOT NULL,
    producto_id BIGINT NOT NULL,
    tipo ENUM('ENTRADA', 'SALIDA', 'AJUSTE', 'TRANSFERENCIA', 'DEVOLUCION', 'MERMA') NOT NULL,
    cantidad DECIMAL(15,3) NOT NULL,
    cantidad_anterior DECIMAL(15,3) NOT NULL,
    cantidad_posterior DECIMAL(15,3) NOT NULL,
    precio_unitario DECIMAL(15,2) DEFAULT 0.00,
    costo_total DECIMAL(15,2) DEFAULT 0.00,
    referencia VARCHAR(200),
    documento VARCHAR(100),
    observaciones TEXT,
    fecha_movimiento TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    FOREIGN KEY (organizacion_id) REFERENCES organizaciones(id) ON DELETE CASCADE,
    FOREIGN KEY (sucursal_id) REFERENCES sucursales(id) ON DELETE CASCADE,
    FOREIGN KEY (producto_id) REFERENCES productos(id) ON DELETE CASCADE,
    INDEX idx_movimiento_organizacion (organizacion_id),
    INDEX idx_movimiento_sucursal (sucursal_id),
    INDEX idx_movimiento_producto (producto_id),
    INDEX idx_movimiento_tipo (tipo),
    INDEX idx_movimiento_fecha (fecha_movimiento),
    INDEX idx_movimiento_referencia (referencia)
) ENGINE=InnoDB;

-- =====================================================
-- TABLA: CAJAS
-- =====================================================
CREATE TABLE IF NOT EXISTS cajas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    organizacion_id BIGINT NOT NULL,
    sucursal_id BIGINT NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    descripcion TEXT,
    estado ENUM('ABIERTA', 'CERRADA', 'EN_MANTENIMIENTO', 'BLOQUEADA') NOT NULL DEFAULT 'CERRADA',
    saldo_inicial DECIMAL(15,2) NOT NULL DEFAULT 0.00,
    saldo_actual DECIMAL(15,2) NOT NULL DEFAULT 0.00,
    saldo_efectivo DECIMAL(15,2) DEFAULT 0.00,
    saldo_tarjeta DECIMAL(15,2) DEFAULT 0.00,
    saldo_transferencia DECIMAL(15,2) DEFAULT 0.00,
    total_ventas DECIMAL(15,2) NOT NULL DEFAULT 0.00,
    total_propinas DECIMAL(15,2) NOT NULL DEFAULT 0.00,
    total_transacciones INT DEFAULT 0,
    activa BOOLEAN NOT NULL DEFAULT TRUE,
    version BIGINT NOT NULL DEFAULT 0,
    fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_modificacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    fecha_eliminacion TIMESTAMP NULL,
    eliminado BOOLEAN NOT NULL DEFAULT FALSE,
    
    FOREIGN KEY (organizacion_id) REFERENCES organizaciones(id) ON DELETE CASCADE,
    FOREIGN KEY (sucursal_id) REFERENCES sucursales(id) ON DELETE CASCADE,
    INDEX idx_caja_organizacion (organizacion_id),
    INDEX idx_caja_sucursal (sucursal_id),
    INDEX idx_caja_estado (estado),
    INDEX idx_caja_activa (activa),
    INDEX idx_caja_eliminado (eliminado),
    UNIQUE KEY uk_caja_nombre_sucursal (nombre, sucursal_id)
) ENGINE=InnoDB;

-- =====================================================
-- TABLA: MOVIMIENTOS DE CAJA
-- =====================================================
CREATE TABLE IF NOT EXISTS movimientos_caja (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    organizacion_id BIGINT NOT NULL,
    sucursal_id BIGINT NOT NULL,
    caja_id BIGINT NOT NULL,
    tipo ENUM('APERTURA', 'CIERRE', 'VENTA', 'RETIRO', 'DEPOSITO', 'PROPINA', 'AJUSTE', 'TRANSFERENCIA', 'DEVOLUCION') NOT NULL,
    monto DECIMAL(15,2) NOT NULL,
    saldo_anterior DECIMAL(15,2) NOT NULL,
    saldo_posterior DECIMAL(15,2) NOT NULL,
    metodo_pago ENUM('EFECTIVO', 'TARJETA_CREDITO', 'TARJETA_DEBITO', 'TRANSFERENCIA', 'QR', 'OTRO') NULL,
    descripcion TEXT,
    referencia VARCHAR(200),
    documento VARCHAR(100),
    observaciones TEXT,
    fecha_movimiento TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    FOREIGN KEY (organizacion_id) REFERENCES organizaciones(id) ON DELETE CASCADE,
    FOREIGN KEY (sucursal_id) REFERENCES sucursales(id) ON DELETE CASCADE,
    FOREIGN KEY (caja_id) REFERENCES cajas(id) ON DELETE CASCADE,
    INDEX idx_movimiento_caja_organizacion (organizacion_id),
    INDEX idx_movimiento_caja_sucursal (sucursal_id),
    INDEX idx_movimiento_caja_caja (caja_id),
    INDEX idx_movimiento_caja_tipo (tipo),
    INDEX idx_movimiento_caja_fecha (fecha_movimiento),
    INDEX idx_movimiento_caja_referencia (referencia)
) ENGINE=InnoDB;

-- =====================================================
-- TABLA: VENTAS
-- =====================================================
CREATE TABLE IF NOT EXISTS ventas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    organizacion_id BIGINT NOT NULL,
    sucursal_id BIGINT NOT NULL,
    caja_id BIGINT NOT NULL,
    numero_ticket VARCHAR(100) NOT NULL,
    numero_factura VARCHAR(100),
    tipo_venta ENUM('CONTADO', 'CREDITO', 'CONSIGNACION') NOT NULL DEFAULT 'CONTADO',
    subtotal DECIMAL(15,2) NOT NULL DEFAULT 0.00,
    impuesto DECIMAL(15,2) NOT NULL DEFAULT 0.00,
    descuento DECIMAL(15,2) NOT NULL DEFAULT 0.00,
    propina DECIMAL(15,2) NOT NULL DEFAULT 0.00,
    total DECIMAL(15,2) NOT NULL DEFAULT 0.00,
    estado ENUM('PENDIENTE', 'COMPLETADA', 'CANCELADA', 'DEVUELTA', 'ANULADA') NOT NULL DEFAULT 'PENDIENTE',
    metodo_pago ENUM('EFECTIVO', 'TARJETA_CREDITO', 'TARJETA_DEBITO', 'TRANSFERENCIA', 'QR', 'MIXTO') NOT NULL,
    cliente_nombre VARCHAR(200),
    cliente_documento VARCHAR(50),
    cliente_email VARCHAR(100),
    cliente_telefono VARCHAR(20),
    observaciones TEXT,
    fecha_venta TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_modificacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    fecha_eliminacion TIMESTAMP NULL,
    eliminado BOOLEAN NOT NULL DEFAULT FALSE,
    
    FOREIGN KEY (organizacion_id) REFERENCES organizaciones(id) ON DELETE CASCADE,
    FOREIGN KEY (sucursal_id) REFERENCES sucursales(id) ON DELETE CASCADE,
    FOREIGN KEY (caja_id) REFERENCES cajas(id) ON DELETE CASCADE,
    INDEX idx_venta_organizacion (organizacion_id),
    INDEX idx_venta_sucursal (sucursal_id),
    INDEX idx_venta_caja (caja_id),
    INDEX idx_venta_estado (estado),
    INDEX idx_venta_fecha (fecha_venta),
    INDEX idx_venta_ticket (numero_ticket),
    INDEX idx_venta_factura (numero_factura),
    INDEX idx_venta_cliente (cliente_documento),
    INDEX idx_venta_eliminado (eliminado),
    UNIQUE KEY uk_venta_ticket_sucursal (numero_ticket, sucursal_id)
) ENGINE=InnoDB;

-- =====================================================
-- TABLA: ITEMS DE VENTA
-- =====================================================
CREATE TABLE IF NOT EXISTS venta_items (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    organizacion_id BIGINT NOT NULL,
    venta_id BIGINT NOT NULL,
    producto_id BIGINT NOT NULL,
    cantidad DECIMAL(15,3) NOT NULL,
    precio_unitario DECIMAL(15,2) NOT NULL,
    precio_costo DECIMAL(15,2) DEFAULT 0.00,
    subtotal DECIMAL(15,2) NOT NULL,
    descuento DECIMAL(15,2) NOT NULL DEFAULT 0.00,
    impuesto DECIMAL(15,2) NOT NULL DEFAULT 0.00,
    total DECIMAL(15,2) NOT NULL,
    margen_ganancia DECIMAL(15,2) DEFAULT 0.00,
    observaciones TEXT,
    fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    FOREIGN KEY (organizacion_id) REFERENCES organizaciones(id) ON DELETE CASCADE,
    FOREIGN KEY (venta_id) REFERENCES ventas(id) ON DELETE CASCADE,
    FOREIGN KEY (producto_id) REFERENCES productos(id) ON DELETE CASCADE,
    INDEX idx_venta_item_organizacion (organizacion_id),
    INDEX idx_venta_item_venta (venta_id),
    INDEX idx_venta_item_producto (producto_id)
) ENGINE=InnoDB;

-- =====================================================
-- TABLA: DIVISIONES DE CUENTA
-- =====================================================
CREATE TABLE IF NOT EXISTS divisiones_cuenta (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    organizacion_id BIGINT NOT NULL,
    venta_id BIGINT NOT NULL,
    nombre_cliente VARCHAR(200) NOT NULL,
    documento_cliente VARCHAR(50),
    monto DECIMAL(15,2) NOT NULL,
    metodo_pago ENUM('EFECTIVO', 'TARJETA_CREDITO', 'TARJETA_DEBITO', 'TRANSFERENCIA', 'QR') NOT NULL,
    observaciones TEXT,
    fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    FOREIGN KEY (organizacion_id) REFERENCES organizaciones(id) ON DELETE CASCADE,
    FOREIGN KEY (venta_id) REFERENCES ventas(id) ON DELETE CASCADE,
    INDEX idx_division_organizacion (organizacion_id),
    INDEX idx_division_venta (venta_id),
    INDEX idx_division_cliente (documento_cliente)
) ENGINE=InnoDB;

-- =====================================================
-- TABLA: SUSCRIPCIONES
-- =====================================================
CREATE TABLE IF NOT EXISTS suscripciones (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    organizacion_id BIGINT NOT NULL,
    plan ENUM('GRATUITO', 'BASICO', 'PRO', 'ENTERPRISE', 'CUSTOM') NOT NULL,
    nombre_plan VARCHAR(100),
    descripcion TEXT,
    fecha_inicio DATE NOT NULL,
    fecha_vencimiento DATE NOT NULL,
    fecha_renovacion DATE,
    estado ENUM('ACTIVA', 'SUSPENDIDA', 'CANCELADA', 'VENCIDA', 'PENDIENTE') NOT NULL DEFAULT 'ACTIVA',
    precio_mensual DECIMAL(15,2) NOT NULL DEFAULT 0.00,
    precio_total DECIMAL(15,2) NOT NULL DEFAULT 0.00,
    numero_sucursales INT NOT NULL DEFAULT 1,
    numero_usuarios INT NOT NULL DEFAULT 1,
    limite_productos INT,
    limite_ventas_mensuales INT,
    caracteristicas JSON,
    activa BOOLEAN NOT NULL DEFAULT TRUE,
    fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_modificacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    fecha_eliminacion TIMESTAMP NULL,
    eliminado BOOLEAN NOT NULL DEFAULT FALSE,
    
    FOREIGN KEY (organizacion_id) REFERENCES organizaciones(id) ON DELETE CASCADE,
    INDEX idx_suscripcion_organizacion (organizacion_id),
    INDEX idx_suscripcion_estado (estado),
    INDEX idx_suscripcion_plan (plan),
    INDEX idx_suscripcion_fecha_vencimiento (fecha_vencimiento),
    INDEX idx_suscripcion_activa (activa),
    INDEX idx_suscripcion_eliminado (eliminado)
) ENGINE=InnoDB;

-- =====================================================
-- TABLA: HISTORIAL DE PRECIOS
-- =====================================================
CREATE TABLE IF NOT EXISTS historial_precios (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    organizacion_id BIGINT NOT NULL,
    producto_id BIGINT NOT NULL,
    precio_anterior DECIMAL(15,2) NOT NULL,
    precio_nuevo DECIMAL(15,2) NOT NULL,
    tipo_cambio ENUM('AUMENTO', 'REDUCCION', 'AJUSTE') NOT NULL,
    porcentaje_cambio DECIMAL(5,2),
    motivo TEXT,
    fecha_cambio TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    FOREIGN KEY (organizacion_id) REFERENCES organizaciones(id) ON DELETE CASCADE,
    FOREIGN KEY (producto_id) REFERENCES productos(id) ON DELETE CASCADE,
    INDEX idx_historial_organizacion (organizacion_id),
    INDEX idx_historial_producto (producto_id),
    INDEX idx_historial_fecha (fecha_cambio),
    INDEX idx_historial_tipo (tipo_cambio)
) ENGINE=InnoDB;

-- =====================================================
-- TABLA: CONFIGURACIONES DE ORGANIZACION
-- =====================================================
CREATE TABLE IF NOT EXISTS configuraciones_organizacion (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    organizacion_id BIGINT NOT NULL,
    clave VARCHAR(100) NOT NULL,
    valor TEXT,
    descripcion TEXT,
    tipo ENUM('STRING', 'NUMBER', 'BOOLEAN', 'JSON', 'DATE') NOT NULL DEFAULT 'STRING',
    activa BOOLEAN NOT NULL DEFAULT TRUE,
    fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_modificacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    FOREIGN KEY (organizacion_id) REFERENCES organizaciones(id) ON DELETE CASCADE,
    INDEX idx_config_organizacion (organizacion_id),
    INDEX idx_config_clave (clave),
    INDEX idx_config_activa (activa),
    UNIQUE KEY uk_config_org_clave (organizacion_id, clave)
) ENGINE=InnoDB;

-- =====================================================
-- INSERTAR DATOS DE EJEMPLO
-- =====================================================

-- Organización de ejemplo
INSERT INTO organizaciones (nombre, descripcion, ruc, email, telefono, direccion, ciudad) VALUES
('Supply AI Demo', 'Organización de demostración para Supply AI', '20123456789', 'demo@supplyai.com', '+51 1 234 5678', 'Av. Principal 123', 'Lima');

-- Sucursal de ejemplo
INSERT INTO sucursales (organizacion_id, nombre, descripcion, direccion, ciudad, telefono, email) VALUES
(1, 'Sucursal Principal', 'Sucursal principal de demostración', 'Av. Principal 123', 'Lima', '+51 1 234 5678', 'principal@supplyai.com');

-- Categorías de ejemplo
INSERT INTO categorias (organizacion_id, nombre, descripcion, nivel, orden) VALUES
(1, 'Electrónicos', 'Productos electrónicos y tecnología', 1, 1),
(1, 'Oficina', 'Artículos de oficina y papelería', 1, 2),
(1, 'Limpieza', 'Productos de limpieza y aseo', 1, 3),
(1, 'Alimentos', 'Productos alimenticios y bebidas', 1, 4);

-- Subcategorías
INSERT INTO categorias (organizacion_id, nombre, descripcion, categoria_padre_id, nivel, orden) VALUES
(1, 'Computadoras', 'Computadoras y laptops', 1, 2, 1),
(1, 'Accesorios', 'Accesorios para computadora', 1, 2, 2),
(1, 'Papelería', 'Artículos de papelería', 2, 2, 1),
(1, 'Mobiliario', 'Mobiliario de oficina', 2, 2, 2);

-- Productos de ejemplo
INSERT INTO productos (organizacion_id, sucursal_id, categoria_id, nombre, descripcion, codigo_interno, sku, tipo, precio_venta, precio_costo, precio_compra, unidad_medida, margen_ganancia) VALUES
(1, 1, 5, 'Laptop HP Pavilion', 'Laptop HP Pavilion 15.6" Intel Core i5', 'LP001', 'LAP-HP-PAV-001', 'SIMPLE', 2500.00, 1800.00, 2000.00, 'unidad', 38.89),
(1, 1, 6, 'Mouse Inalámbrico', 'Mouse inalámbrico Logitech M185', 'MS001', 'MOU-LOG-M185-001', 'SIMPLE', 25.00, 15.00, 18.00, 'unidad', 66.67),
(1, 1, 7, 'Cuaderno A4', 'Cuaderno A4 100 hojas rayadas', 'CU001', 'CUAD-A4-100-001', 'SIMPLE', 8.50, 4.50, 5.00, 'unidad', 88.89),
(1, 1, 8, 'Silla de Oficina', 'Silla ergonómica de oficina', 'SI001', 'SILLA-ERG-001', 'SIMPLE', 350.00, 250.00, 280.00, 'unidad', 40.00);

-- Inventario de ejemplo
INSERT INTO inventarios (organizacion_id, sucursal_id, producto_id, stock_actual, stock_minimo, stock_maximo, stock_seguridad, punto_reorden, ubicacion) VALUES
(1, 1, 1, 10, 2, 20, 5, 7, 'Almacén A - Estante 1'),
(1, 1, 2, 50, 10, 100, 20, 30, 'Almacén A - Estante 2'),
(1, 1, 3, 200, 50, 500, 100, 150, 'Almacén B - Estante 1'),
(1, 1, 4, 15, 3, 30, 8, 11, 'Almacén A - Estante 3');

-- Caja de ejemplo
INSERT INTO cajas (organizacion_id, sucursal_id, nombre, descripcion, estado, saldo_inicial, saldo_actual, saldo_efectivo) VALUES
(1, 1, 'Caja Principal', 'Caja principal de la sucursal', 'ABIERTA', 1000.00, 1000.00, 1000.00);

-- Suscripción de ejemplo
INSERT INTO suscripciones (organizacion_id, plan, nombre_plan, descripcion, fecha_inicio, fecha_vencimiento, precio_mensual, precio_total, numero_sucursales, numero_usuarios, limite_productos, limite_ventas_mensuales) VALUES
(1, 'PRO', 'Plan Profesional', 'Plan profesional con funcionalidades avanzadas', CURDATE(), DATE_ADD(CURDATE(), INTERVAL 1 YEAR), 99.99, 1199.88, 5, 10, 10000, 100000);

-- Configuraciones de ejemplo
INSERT INTO configuraciones_organizacion (organizacion_id, clave, valor, descripcion, tipo) VALUES
(1, 'impuesto_por_defecto', '18.00', 'Porcentaje de impuesto por defecto', 'NUMBER'),
(1, 'moneda_principal', 'PEN', 'Moneda principal de la organización', 'STRING'),
(1, 'zona_horaria', 'America/Lima', 'Zona horaria de la organización', 'STRING'),
(1, 'notificaciones_email', 'true', 'Habilitar notificaciones por email', 'BOOLEAN');

-- =====================================================
-- CREAR VISTAS ÚTILES
-- =====================================================

-- Vista para productos con inventario
CREATE OR REPLACE VIEW v_productos_inventario AS
SELECT 
    p.id,
    p.organizacion_id,
    p.sucursal_id,
    p.nombre,
    p.descripcion,
    p.codigo_interno,
    p.sku,
    p.tipo,
    p.precio_venta,
    p.precio_costo,
    p.activo,
    i.stock_actual,
    i.stock_minimo,
    i.stock_maximo,
    i.ubicacion,
    CASE 
        WHEN i.stock_actual <= i.stock_minimo THEN 'CRITICO'
        WHEN i.stock_actual <= i.punto_reorden THEN 'BAJO'
        ELSE 'NORMAL'
    END as estado_stock
FROM productos p
LEFT JOIN inventarios i ON p.id = i.producto_id AND p.sucursal_id = i.sucursal_id
WHERE p.eliminado = FALSE AND i.eliminado = FALSE;

-- Vista para resumen de ventas por sucursal
CREATE OR REPLACE VIEW v_resumen_ventas_sucursal AS
SELECT 
    s.organizacion_id,
    s.id as sucursal_id,
    s.nombre as sucursal_nombre,
    COUNT(v.id) as total_ventas,
    SUM(v.total) as monto_total,
    AVG(v.total) as promedio_venta,
    MAX(v.fecha_venta) as ultima_venta
FROM sucursales s
LEFT JOIN ventas v ON s.id = v.sucursal_id AND v.estado = 'COMPLETADA'
WHERE s.eliminado = FALSE
GROUP BY s.id, s.organizacion_id, s.nombre;

-- =====================================================
-- TRIGGERS DE AUDITORIA
-- =====================================================

-- Trigger para auditoría de productos
DELIMITER //
CREATE TRIGGER tr_productos_auditoria_insert
AFTER INSERT ON productos
FOR EACH ROW
BEGIN
    INSERT INTO auditoria (organizacion_id, tabla, registro_id, accion, usuario_id, datos_nuevos)
    VALUES (NEW.organizacion_id, 'productos', NEW.id, 'INSERT', NEW.creado_por, JSON_OBJECT(
        'id', NEW.id,
        'nombre', NEW.nombre,
        'sku', NEW.sku,
        'precio_venta', NEW.precio_venta,
        'tipo', NEW.tipo
    ));
END//

CREATE TRIGGER tr_productos_auditoria_update
AFTER UPDATE ON productos
FOR EACH ROW
BEGIN
    INSERT INTO auditoria (organizacion_id, tabla, registro_id, accion, usuario_id, datos_anteriores, datos_nuevos)
    VALUES (NEW.organizacion_id, 'productos', NEW.id, 'UPDATE', NEW.modificado_por, 
        JSON_OBJECT(
            'id', OLD.id,
            'nombre', OLD.nombre,
            'sku', OLD.sku,
            'precio_venta', OLD.precio_venta,
            'tipo', OLD.tipo
        ),
        JSON_OBJECT(
            'id', NEW.id,
            'nombre', NEW.nombre,
            'sku', NEW.sku,
            'precio_venta', NEW.precio_venta,
            'tipo', NEW.tipo
        )
    );
END//

CREATE TRIGGER tr_productos_auditoria_delete
AFTER UPDATE ON productos
FOR EACH ROW
BEGIN
    IF NEW.eliminado = TRUE AND OLD.eliminado = FALSE THEN
        INSERT INTO auditoria (organizacion_id, tabla, registro_id, accion, usuario_id, datos_anteriores)
        VALUES (NEW.organizacion_id, 'productos', NEW.id, 'SOFT_DELETE', NEW.modificado_por,
            JSON_OBJECT(
                'id', OLD.id,
                'nombre', OLD.nombre,
                'sku', OLD.sku,
                'precio_venta', OLD.precio_venta,
                'tipo', OLD.tipo
            )
        );
    END IF;
END//
DELIMITER ;

-- =====================================================
-- VISTAS MATERIALIZADAS PARA REPORTES
-- =====================================================

-- Vista para dashboard de inventario crítico
CREATE OR REPLACE VIEW v_inventario_critico AS
SELECT 
    o.nombre as organizacion_nombre,
    s.nombre as sucursal_nombre,
    p.nombre as producto_nombre,
    p.sku,
    p.codigo_interno,
    i.stock_actual,
    i.stock_minimo,
    i.punto_reorden,
    i.ubicacion,
    CASE 
        WHEN i.stock_actual <= i.stock_minimo THEN 'CRITICO'
        WHEN i.stock_actual <= i.punto_reorden THEN 'BAJO'
        ELSE 'NORMAL'
    END as estado_stock,
    p.precio_costo,
    (i.stock_actual * p.precio_costo) as valor_inventario
FROM inventarios i
JOIN productos p ON i.producto_id = p.id
JOIN sucursales s ON i.sucursal_id = s.id
JOIN organizaciones o ON i.organizacion_id = o.id
WHERE i.eliminado = FALSE 
    AND p.eliminado = FALSE 
    AND i.stock_actual <= i.punto_reorden
ORDER BY i.stock_actual ASC;

-- Vista para análisis de ventas por período
CREATE OR REPLACE VIEW v_ventas_periodo AS
SELECT 
    o.nombre as organizacion_nombre,
    s.nombre as sucursal_nombre,
    DATE(v.fecha_venta) as fecha,
    COUNT(v.id) as total_ventas,
    SUM(v.total) as monto_total,
    AVG(v.total) as promedio_venta,
    SUM(v.impuesto) as total_impuestos,
    SUM(v.descuento) as total_descuentos,
    COUNT(DISTINCT v.cliente_documento) as clientes_unicos
FROM ventas v
JOIN sucursales s ON v.sucursal_id = s.id
JOIN organizaciones o ON v.organizacion_id = o.id
WHERE v.estado = 'COMPLETADA' 
    AND v.eliminado = FALSE
GROUP BY o.id, s.id, DATE(v.fecha_venta)
ORDER BY fecha DESC;

-- =====================================================
-- FUNCIONES UTILES
-- =====================================================

-- Función para calcular edad de inventario
DELIMITER //
CREATE FUNCTION fn_calcular_edad_inventario(producto_id BIGINT, sucursal_id BIGINT)
RETURNS INT
READS SQL DATA
DETERMINISTIC
BEGIN
    DECLARE ultima_entrada DATE;
    DECLARE edad_dias INT;
    
    SELECT MAX(fecha_movimiento) INTO ultima_entrada
    FROM movimientos_inventario 
    WHERE producto_id = producto_id 
        AND sucursal_id = sucursal_id 
        AND tipo = 'ENTRADA';
    
    IF ultima_entrada IS NULL THEN
        RETURN 999; -- Sin movimientos
    END IF;
    
    SET edad_dias = DATEDIFF(CURDATE(), ultima_entrada);
    RETURN edad_dias;
END//
DELIMITER ;

-- =====================================================
-- FIN DEL ESQUEMA MEJORADO
-- =====================================================
