# Supply AI - Base de Datos

## 📋 Descripción General

Base de datos multi-tenant para el sistema SaaS Supply AI, diseñada para gestión de inventario y POS (Point of Sale) con enfoque en escalabilidad y seguridad para 2025.

## 🏗️ Arquitectura

### Multi-Tenancy
- **Aislamiento**: Cada organización tiene sus datos completamente separados
- **Escalabilidad**: Preparado para miles de organizaciones
- **Seguridad**: Filtrado obligatorio por `organizacion_id`

### Estructura
- **18 tablas principales** + 2 tablas de auditoría
- **Vistas optimizadas** para reportes
- **Triggers de auditoría** para trazabilidad
- **Índices optimizados** para consultas multi-tenant

## 📊 Tablas Principales

### 1. Core del Sistema
| Tabla | Descripción | Registros Estimados |
|-------|-------------|-------------------|
| `organizaciones` | Multi-tenant principal | 1K - 10K |
| `sucursales` | Sucursales por organización | 5K - 100K |
| `usuarios` | Usuarios del sistema | 10K - 500K |

### 2. Gestión de Productos
| Tabla | Descripción | Registros Estimados |
|-------|-------------|-------------------|
| `categorias` | Categorías jerárquicas | 50K - 1M |
| `productos` | Catálogo de productos | 100K - 10M |
| `producto_componentes` | Productos compuestos | 50K - 5M |

### 3. Inventario
| Tabla | Descripción | Registros Estimados |
|-------|-------------|-------------------|
| `inventarios` | Stock por sucursal | 200K - 20M |
| `movimientos_inventario` | Historial de movimientos | 1M - 100M |

### 4. Ventas y POS
| Tabla | Descripción | Registros Estimados |
|-------|-------------|-------------------|
| `cajas` | Cajas registradoras | 5K - 50K |
| `ventas` | Transacciones de venta | 1M - 100M |
| `venta_items` | Items de cada venta | 5M - 500M |

### 5. Auditoría y Configuración
| Tabla | Descripción | Registros Estimados |
|-------|-------------|-------------------|
| `auditoria` | Log de cambios críticos | 10M - 1B |
| `configuraciones_organizacion` | Configs por tenant | 10K - 100K |

## 🔐 Seguridad y Auditoría

### Campos de Auditoría
- `creado_por`: Usuario que creó el registro
- `modificado_por`: Usuario que modificó por última vez
- `fecha_creacion`: Timestamp de creación
- `fecha_modificacion`: Timestamp de última modificación
- `fecha_eliminacion`: Timestamp de eliminación (soft delete)
- `eliminado`: Flag de soft delete

### Triggers de Auditoría
- **INSERT**: Registra creación de productos
- **UPDATE**: Registra modificaciones
- **SOFT_DELETE**: Registra eliminaciones lógicas

## 📈 Optimizaciones de Rendimiento

### Índices Clave
```sql
-- Multi-tenancy
idx_organizacion_id (en todas las tablas)

-- Consultas frecuentes
idx_producto_sku_organizacion
idx_inventario_stock
idx_venta_fecha_estado

-- Auditoría
idx_auditoria_fecha_accion
idx_auditoria_tabla_registro
```

### Particionamiento (Futuro)
- **movimientos_inventario**: Por año
- **ventas**: Por año
- **auditoria**: Por mes

### Vistas Optimizadas
- `v_inventario_critico`: Productos con stock bajo
- `v_ventas_periodo`: Análisis de ventas por período
- `v_productos_inventario`: Productos con estado de inventario

## 🚀 Escalabilidad

### Preparación para Crecimiento
- **Particionamiento**: Comentado para implementación futura
- **Índices compuestos**: Optimizados para consultas multi-tenant
- **Soft deletes**: Mantiene integridad referencial
- **JSON fields**: Flexibilidad para personalizaciones por tenant

### Límites Estimados
- **Organizaciones**: 10,000+
- **Productos por organización**: 100,000+
- **Ventas diarias**: 1,000,000+
- **Usuarios concurrentes**: 10,000+

## 🔧 Configuración

### Usuarios de Base de Datos
| Usuario | Permisos | Uso |
|---------|----------|-----|
| `supplyai_app` | CRUD + EXECUTE | Aplicación principal |
| `supplyai_reports` | SELECT | Reportes y analytics |
| `supplyai_admin` | ALL | Administración |

### Configuraciones de MySQL
- **Buffer Pool**: 1GB (ajustable según RAM)
- **Log Files**: 256MB
- **Connections**: 1,000 máximas
- **Query Cache**: 128MB

## 📋 Scripts de Mantenimiento

### Limpieza Automática
```sql
-- Limpiar datos antiguos (90 días)
CALL sp_limpiar_datos_antiguos(90);

-- Análisis de rendimiento
CALL sp_analisis_rendimiento();
```

### Eventos Automáticos
- **Limpieza**: Domingos 2:00 AM
- **Análisis**: Domingos 3:00 AM

## 🧪 Datos de Prueba

### Organización Demo
- **Nombre**: Supply AI Demo
- **RUC**: 20123456789
- **Email**: demo@supplyai.com

### Productos de Ejemplo
- Laptop HP Pavilion (S/. 2,500)
- Mouse Inalámbrico (S/. 25)
- Cuaderno A4 (S/. 8.50)
- Silla de Oficina (S/. 350)

## 📚 Consultas de Ejemplo

### Inventario Crítico
```sql
SELECT * FROM v_inventario_critico 
WHERE organizacion_nombre = 'Supply AI Demo';
```

### Ventas del Día
```sql
SELECT * FROM v_ventas_periodo 
WHERE fecha = CURDATE();
```

### Productos por Categoría
```sql
SELECT c.nombre as categoria, COUNT(p.id) as total_productos
FROM categorias c
LEFT JOIN productos p ON c.id = p.categoria_id
WHERE c.organizacion_id = 1 AND c.eliminado = FALSE
GROUP BY c.id, c.nombre;
```

## 🚨 Consideraciones Importantes

### Multi-Tenancy
- **SIEMPRE** filtrar por `organizacion_id` en todas las consultas
- Usar índices compuestos que incluyan `organizacion_id`
- Validar permisos de usuario antes de acceder a datos

### Rendimiento
- Monitorear consultas lentas con `slow_query_log`
- Reindexar tablas grandes periódicamente
- Usar vistas para consultas complejas frecuentes

### Seguridad
- No exponer IDs de organización en APIs públicas
- Validar permisos de usuario en cada operación
- Loggear todas las operaciones críticas

## 🔄 Migraciones Futuras

### Versión 3.0 (Planeada)
- Particionamiento automático por tenant
- Compresión de datos históricos
- Replicación read-only para reportes
- Sharding horizontal para organizaciones grandes

## 📞 Soporte

Para consultas sobre la base de datos:
- **Autor**: David Timana
- **Email**: davidorlandotimana@gmail.com
- **GitHub**: [@davidtimana](https://github.com/davidtimana)

---

*Última actualización: 2025-09-03*
*Versión del esquema: 2.0*
