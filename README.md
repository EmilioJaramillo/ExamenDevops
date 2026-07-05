# Logística Inventarios

Plataforma de gestión de inventarios cíclicos de bodega. Proyecto de la asignatura ISY1101 (Introducción a Herramientas DevOps), DuocUC.

## Arquitectura

```
┌─────────────┐      HTTP/JSON       ┌──────────────┐      JDBC       ┌─────────────┐
│  Frontend   │  ───────────────►    │   Backend    │  ───────────►   │  PostgreSQL │
│ React + Vite│                      │ Spring Boot  │                 │             │
│ (nginx)     │  ◄───────────────    │  (Java 17)   │  ◄───────────   │             │
└─────────────┘                      └──────────────┘                 └─────────────┘
```

- **Frontend**: React 18 + Vite, servido en producción por nginx. La URL del backend se inyecta en tiempo de ejecución (no en build) vía `docker-entrypoint.sh` + `env-config.js`, para poder usar la misma imagen en distintos entornos.
- **Backend**: Spring Boot 3.3 (Java 17), expone la API REST en `/api`. Endpoints principales:
  - `POST /api/clientes` — registrar cliente
  - `POST /api/inventarios/generar` — generar inventario cíclico del día
  - `GET /api/inventarios` — historial de los últimos 10 inventarios generados
  - `GET /actuator/health` — health check
- **Base de datos**: PostgreSQL 16, esquema gestionado por Hibernate (`ddl-auto: update`).

## Desarrollo local con Docker Compose

Requisitos: Docker y Docker Compose.

```bash
cp .env.example .env
docker compose up --build
```

- Frontend: http://localhost:5173
- Backend: http://localhost:8080/api
- Postgres: localhost:5432 (usuario/clave definidos en `.env`)

Para bajar el entorno: `docker compose down` (agregar `-v` para borrar también el volumen de datos).

## Despliegue en la nube

El despliegue en AWS (EKS) y el pipeline de CI/CD con GitHub Actions se documentan en detalle en el informe entregado junto con este repositorio (arquitectura de VPC, ECR, IAM, manejo de secretos y observabilidad).

## Estructura del repositorio

```
backend/    API REST (Spring Boot), Dockerfile multietapa
frontend/   SPA (React/Vite), Dockerfile multietapa servido con nginx
k8s/        Manifiestos de Kubernetes (Deployments, Services, Secrets)
.github/    Workflows de GitHub Actions (CI/CD)
docker-compose.yml   Orquestación local de los 3 servicios
```
