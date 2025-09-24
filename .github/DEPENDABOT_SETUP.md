# Dependabot Setup - Para Habilitar Más Tarde

## Estado Actual
Dependabot está **deshabilitado** temporalmente en el pipeline de CI.

## Para Habilitar Dependabot

### 1. Descomentar configuración en `.github/dependabot.yml`
```yaml
version: 2
updates:
  # Backend dependencies
  - package-ecosystem: "gradle"
    directory: "/backend"
    schedule:
      interval: "weekly"
      day: "monday"
      time: "09:00"
    open-pull-requests-limit: 5
    reviewers:
      - "davidtimana"
    assignees:
      - "davidtimana"
    commit-message:
      prefix: "chore"
      include: "scope"
```

### 2. Habilitar workflow en `.github/workflows/dependabot.yml`
```yaml
on:
  pull_request:
    types: [opened, synchronize]
```

### 3. Actualizar README.md
- Quitar la nota de "deshabilitado temporalmente"
- Restaurar documentación de Dependabot

## Beneficios de Dependabot
- 🔄 **Actualizaciones automáticas** de dependencias
- 🛡️ **Parches de seguridad** automáticos
- 📊 **Reportes de vulnerabilidades** en GitHub Security tab
- 🚀 **Auto-merge** de PRs seguros

## Configuración Actual
- **Backend**: Gradle dependencies (Java/Spring Boot)
- **Frontend**: NPM dependencies (Node.js)
- **GitHub Actions**: Workflow dependencies
- **Docker**: Base image updates

## Notas
- Dependabot crea PRs automáticamente
- Auto-merge solo para actualizaciones seguras
- Revisión manual para cambios mayores
- Configurado para ejecutarse los lunes a las 9:00 AM
