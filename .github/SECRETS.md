# GitHub Secrets Configuration

Este documento describe los secrets necesarios para que los workflows de CI/CD funcionen correctamente.

## Secrets Requeridos

### Para Docker Hub (Opcional)
Si quieres usar Docker Hub en lugar de GitHub Container Registry:

```
DOCKER_USERNAME=tu_usuario_dockerhub
DOCKER_PASSWORD=tu_token_dockerhub
```

### Para GitHub Container Registry (Recomendado)
No se requieren secrets adicionales. GitHub Actions usa automáticamente `GITHUB_TOKEN`.

## Configuración de Secrets

1. Ve a tu repositorio en GitHub
2. Haz clic en **Settings** → **Secrets and variables** → **Actions**
3. Haz clic en **New repository secret**
4. Añade cada secret con su valor correspondiente

## Secrets para Deploy (Opcional)

Si tienes un servidor de producción, puedes añadir:

```
PRODUCTION_HOST=tu_servidor.com
PRODUCTION_USER=usuario_servidor
PRODUCTION_SSH_KEY=tu_clave_privada_ssh
```

## Variables de Entorno

Los workflows usan las siguientes variables de entorno:

- `JAVA_VERSION`: '21'
- `GRADLE_VERSION`: '8.5'
- `REGISTRY`: 'ghcr.io'
- `IMAGE_NAME`: Nombre del repositorio

## Notas de Seguridad

- ⚠️ **Nunca** commits secrets en el código
- 🔒 Usa siempre GitHub Secrets para información sensible
- 🔄 Rota los tokens regularmente
- 📝 Documenta todos los secrets necesarios
