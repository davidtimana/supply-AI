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

## Tecnologías

- **Backend**: Spring Boot, Java 17+
- **Frontend**: React/JavaScript
- **Base de Datos**: PostgreSQL
- **Autenticación**: Keycloak
- **IA/ML**: Python, TensorFlow/PyTorch
- **DevOps**: Docker, Kubernetes

## Instalación

### Prerrequisitos

- Java 17+
- Node.js 18+
- Docker
- PostgreSQL

### Pasos de instalación

1. Clonar el repositorio:
```bash
git clone https://github.com/davidtimana/supply-AI.git
cd supply-AI
```

2. Configurar variables de entorno:
```bash
cp .env.example .env
# Editar .env con tus configuraciones
```

3. Ejecutar con Docker:
```bash
docker-compose up -d
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

1. Acceder a la aplicación web: `http://localhost:3000`
2. Configurar tu organización y productos
3. Importar datos históricos
4. Configurar modelos de IA para predicciones

## Contribución

1. Fork el proyecto
2. Crear una rama para tu feature (`git checkout -b feature/AmazingFeature`)
3. Commit tus cambios (`git commit -m 'Add some AmazingFeature'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abrir un Pull Request

## Licencia

Este proyecto está bajo la Licencia MIT - ver el archivo [LICENSE](LICENSE) para más detalles.

## Contacto

David Timana - [@davidtimana](https://github.com/davidtimana)

Link del proyecto: [https://github.com/davidtimana/supply-AI](https://github.com/davidtimana/supply-AI)
