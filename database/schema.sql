-- Supply AI Database Schema
-- MySQL 8.0+

CREATE DATABASE IF NOT EXISTS supplyai;
USE supplyai;

-- Tabla de organizaciones
CREATE TABLE IF NOT EXISTS organizaciones (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    ruc VARCHAR(20) UNIQUE,
    direccion TEXT,
    telefono VARCHAR(20),
    email VARCHAR(255),
    estado ENUM('ACTIVO', 'INACTIVO') DEFAULT 'ACTIVO',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Tabla de usuarios
CREATE TABLE IF NOT EXISTS usuarios (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    organizacion_id BIGINT,
    username VARCHAR(100) UNIQUE NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    nombre VARCHAR(255) NOT NULL,
    apellido VARCHAR(255) NOT NULL,
    rol ENUM('ADMIN', 'MANAGER', 'USER') DEFAULT 'USER',
    estado ENUM('ACTIVO', 'INACTIVO') DEFAULT 'ACTIVO',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (organizacion_id) REFERENCES organizaciones(id)
);

-- Tabla de categorías de productos
CREATE TABLE IF NOT EXISTS categorias (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    organizacion_id BIGINT,
    nombre VARCHAR(255) NOT NULL,
    descripcion TEXT,
    estado ENUM('ACTIVO', 'INACTIVO') DEFAULT 'ACTIVO',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (organizacion_id) REFERENCES organizaciones(id)
);

-- Tabla de productos
CREATE TABLE IF NOT EXISTS productos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    organizacion_id BIGINT,
    categoria_id BIGINT,
    codigo VARCHAR(100) UNIQUE NOT NULL,
    nombre VARCHAR(255) NOT NULL,
    descripcion TEXT,
    precio_unitario DECIMAL(10,2) DEFAULT 0.00,
    stock_minimo INT DEFAULT 0,
    stock_actual INT DEFAULT 0,
    unidad_medida VARCHAR(50),
    estado ENUM('ACTIVO', 'INACTIVO') DEFAULT 'ACTIVO',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (organizacion_id) REFERENCES organizaciones(id),
    FOREIGN KEY (categoria_id) REFERENCES categorias(id)
);

-- Tabla de inventario
CREATE TABLE IF NOT EXISTS inventario (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    producto_id BIGINT,
    tipo_movimiento ENUM('ENTRADA', 'SALIDA', 'AJUSTE') NOT NULL,
    cantidad INT NOT NULL,
    precio_unitario DECIMAL(10,2),
    fecha_movimiento TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    usuario_id BIGINT,
    observaciones TEXT,
    FOREIGN KEY (producto_id) REFERENCES productos(id),
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
);

-- Insertar datos de ejemplo
INSERT INTO organizaciones (nombre, ruc, direccion, telefono, email) VALUES
('Supply AI Demo', '12345678901', 'Av. Principal 123, Lima', '+51 1 123-4567', 'demo@supplyai.com');

INSERT INTO usuarios (organizacion_id, username, email, nombre, apellido, rol) VALUES
(1, 'admin', 'admin@supplyai.com', 'Administrador', 'Sistema', 'ADMIN');

INSERT INTO categorias (organizacion_id, nombre, descripcion) VALUES
(1, 'Electrónicos', 'Productos electrónicos y tecnología'),
(1, 'Oficina', 'Artículos de oficina y papelería'),
(1, 'Limpieza', 'Productos de limpieza y aseo');
