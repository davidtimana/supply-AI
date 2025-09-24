# CodeQL Setup - Para Habilitar Más Tarde

## Estado Actual
CodeQL está **deshabilitado** temporalmente en el pipeline de CI.

## Para Habilitar CodeQL

### 1. Descomentar triggers en `.github/workflows/codeql.yml`
```yaml
on:
  push:
    branches: [ main, develop ]
  pull_request:
    branches: [ main, develop ]
  schedule:
    - cron: '0 2 * * 1'  # Weekly on Monday at 2 AM
```

### 2. Actualizar README.md
- Descomentar el badge de seguridad
- Quitar la nota de "deshabilitado temporalmente"

### 3. Verificar Configuración
- Asegurarse de que el repositorio tenga habilitado GitHub Advanced Security
- Verificar que CodeQL esté disponible en la organización

## Beneficios de CodeQL
- 🔍 **Análisis estático** de código
- 🛡️ **Detección de vulnerabilidades** automática
- 📊 **Reportes de seguridad** en GitHub Security tab
- 🔄 **Escaneo continuo** en cada PR

## Notas
- CodeQL puede ser lento en repositorios grandes
- Requiere permisos de GitHub Advanced Security
- Los resultados aparecen en la pestaña "Security" del repositorio
