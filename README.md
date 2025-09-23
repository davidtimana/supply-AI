# Supply AI

Sistema inteligente de gestión de inventario y suministros.

## Descripción

Supply AI es una plataforma que utiliza inteligencia artificial para optimizar la gestión de inventarios, predicción de demanda y automatización de procesos de suministro.

## Características

- 🧠 **Inteligencia Artificial**: Predicción de demanda y optimización de inventarios
- 📊 **Analytics**: Dashboard con métricas en tiempo real
- 🔄 **Automatización**: Procesos automáticos de reabastecimiento
- 📱 **Multiplataforma**: Web y aplicaciones móviles
- 🔒 **Seguridad**: Autenticación robusta y encriptación de datos

## Estructura del Proyecto

```
supply-ai/
├── backend/                    # 🎯 Código Java del backend
│   ├── src/                    # Código fuente Java
│   ├── build/                  # Archivos compilados
│   ├── build.gradle            # Configuración de Gradle
│   ├── settings.gradle         # Configuración de Gradle
│   ├── gradlew                 # Wrapper de Gradle
│   └── gradle/                 # Configuración de Gradle
├── frontend/                   # 🎯 Código React/JavaScript
│   ├── src/                    # Código fuente del frontend
│   ├── App.js                  # Componente principal
│   └── package.json            # Dependencias del frontend
├── database/                   # 🎯 Scripts de base de datos
│   ├── config.sql
│   ├── schema.sql
│   └── README.md
├── documentation/              # 🎯 Documentación del proyecto
│   └── api-spec.yaml
├── docker-compose.yml          # 🐳 Configuración de Docker
├── README.md                   # 📖 Documentación principal
└── LICENSE                     # 🎯 Licencia del proyecto
```

### Servicios Docker

- **Backend**: Spring Boot en puerto 8082
- **MySQL**: Base de datos en puerto 3306
- **Red**: Red interna `supplyai-network`
- **Volúmenes**: Persistencia de datos MySQL

## Tecnologías

- **Backend**: Spring Boot, Java 21
- **Base de Datos**: MySQL 8.0
- **DevOps**: Docker, Docker Compose
- **Documentación**: Swagger/OpenAPI 3
- **Testing**: JUnit 5, Mockito

## Instalación

### Prerrequisitos

- Docker
- Docker Compose

### Instalación con Docker (Recomendado)

1. Clonar el repositorio:
```bash
git clone https://github.com/davidtimana/supply-AI.git
cd supply-AI
```

2. Ejecutar con Docker Compose:
```bash
docker-compose up -d
```

3. Verificar que los servicios estén funcionando:
```bash
docker-compose ps
```

### Desarrollo Local

#### Backend (Spring Boot)
```bash
cd backend
./gradlew bootRun
```

#### Frontend (React)
```bash
cd frontend
npm install
npm start
```

## Uso

### Endpoints Disponibles

Una vez que la aplicación esté ejecutándose, puedes acceder a:

- **API Backend**: `http://localhost:8082`
- **Health Check**: `http://localhost:8082/api/v1/health/ping`
- **Status**: `http://localhost:8082/api/v1/health/status`
- **Swagger UI**: `http://localhost:8082/swagger-ui.html`

### Base de Datos

La base de datos MySQL está disponible externamente en:
- **Host**: `localhost`
- **Puerto**: `3306`
- **Base de datos**: `supplyai`
- **Usuario**: `supplyai`
- **Contraseña**: `supplyai123`

### Comandos Útiles

```bash
# Ver logs de la aplicación
docker-compose logs backend

# Ver logs de la base de datos
docker-compose logs mysql

# Reiniciar servicios
docker-compose restart

# Detener servicios
docker-compose down

# Detener y eliminar volúmenes
docker-compose down --volumes
```

## Troubleshooting

### Problemas Comunes

**Puerto 8080 ocupado:**
```bash
# Cambiar puerto en docker-compose.yml
ports:
  - "8082:8080"  # Usar puerto 8082 en lugar de 8080
```

**Error de conexión a MySQL:**
```bash
# Verificar que MySQL esté ejecutándose
docker-compose logs mysql

# Reiniciar MySQL
docker-compose restart mysql
```

**Backend no inicia:**
```bash
# Ver logs del backend
docker-compose logs backend

# Reconstruir imagen
docker-compose up --build backend
```

### Verificación de Salud

```bash
# Verificar estado de contenedores
docker-compose ps

# Probar endpoint de health
curl http://localhost:8082/api/v1/health/ping

# Probar conexión a MySQL
docker exec supplyai-mysql mysql -u supplyai -psupplyai123 -e "SELECT 1;"
```

## CI/CD

El proyecto incluye pipelines automatizados de CI/CD:

### 🔄 Workflows Automáticos

- **CI Pipeline**: Se ejecuta en cada push y PR
  - Tests unitarios e integración
  - Build de la aplicación
  - Análisis de seguridad con CodeQL
  - Escaneo de vulnerabilidades con Trivy

- **CD Pipeline**: Se ejecuta en merge a `main`
  - Build y push de imagen Docker
  - Deploy automático a producción
  - Health checks post-deploy

### 📊 Estados de Build

[![CI](https://github.com/davidtimana/supply-AI/workflows/CI%2FCD%20Pipeline/badge.svg)](https://github.com/davidtimana/supply-AI/actions)
[![Security](https://github.com/davidtimana/supply-AI/workflows/CodeQL%20Security%20Analysis/badge.svg)](https://github.com/davidtimana/supply-AI/security)

### 🛠️ Configuración Local

Para ejecutar los mismos checks localmente:

```bash
# Ejecutar tests
cd backend && ./gradlew test

# Ejecutar análisis de seguridad
docker run --rm -v $(pwd):/app aquasec/trivy fs /app

# Build de la aplicación
cd backend && ./gradlew build
```

## Contribución

1. Fork el proyecto
2. Crear una rama para tu feature (`git checkout -b feature/AmazingFeature`)
3. Commit tus cambios (`git commit -m 'Add some AmazingFeature'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abrir un Pull Request

### 📋 Checklist para PRs

- [ ] Tests pasan localmente
- [ ] Código sigue las guías de estilo
- [ ] Documentación actualizada
- [ ] No hay conflictos de merge

## Licencia

Este proyecto está bajo la Licencia MIT - ver el archivo [LICENSE](LICENSE) para más detalles.

## Contacto

David Timana - [@davidtimana](https://github.com/davidtimana)

Link del proyecto: [https://github.com/davidtimana/supply-AI](https://github.com/davidtimana/supply-AI)
