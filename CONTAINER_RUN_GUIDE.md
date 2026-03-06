# Container Run Guide

This setup runs the full e-commerce stack with one command, including:
- Core infrastructure (PostgreSQL, MongoDB, Redis, Kafka, Zookeeper, Keycloak)
- Spring services (Config Server, Eureka, User, Product, Order, Notification, Gateway)
- Observability from `Additional/evaluate-prometheus` (Loki, Alloy, Prometheus, Grafana, Zipkin)

## 1) Prerequisites
- Docker Desktop running
- Docker Compose v2 (`docker compose`)

## 2) Start Everything
From the repository root (`ecommerce-spring-boot-micro-service`):

```powershell
docker compose up -d --build
```

## 3) Stop Everything

```powershell
docker compose down
```

To also remove named volumes:

```powershell
docker compose down -v
```

## 4) Main Access URLs
- API Gateway: `http://localhost:8080`
- Keycloak: `http://localhost:8084`
- Eureka: `http://localhost:8761`
- Config Server: `http://localhost:8888`
- Grafana: `http://localhost:3000`
- Prometheus: `http://localhost:9090`
- Zipkin: `http://localhost:9411`
- Loki Gateway: `http://localhost:3100`

## 5) Database Host Ports (default +1)
- PostgreSQL: `localhost:5433` (container 5432)
- MongoDB: `localhost:27018` (container 27017)
- Redis: `localhost:6380` (container 6379)

These are configured in root `.env`.

## 6) Internal Container Communication
All services are on network `ecom-net` and use service DNS names, for example:
- `postgres:5432`
- `mongodb:27017`
- `kafka:29092`
- `keycloak:8080`
- `configserver:8888`
- `eureka:8761`

## 7) Kafka / Zookeeper / Keycloak
- Kafka host access: `localhost:9092`
- Zookeeper host access: `localhost:2181`
- Keycloak host access: `localhost:8084`

## 8) View Container Logs From PC
Zookeeper logs:

```powershell
docker logs -f ecom-zookeeper
```

Kafka logs:

```powershell
docker logs -f ecom-kafka
```

Gateway logs:

```powershell
docker logs -f ecom-gateway
```

List all containers:

```powershell
docker compose ps
```

## 9) Quick Health Checks

```powershell
curl http://localhost:8080/actuator/health
curl http://localhost:8761
curl http://localhost:8084
curl http://localhost:9090/-/ready
```

## 10) Notes
- Root `docker-compose.yml` already includes observability dependencies from `Additional/evaluate-prometheus`.
- Spring services are containerized with per-service Dockerfiles.
- PostgreSQL databases `ecommerce_product` and `ecommerce_order` are auto-created if missing by `ecom-postgres-db-init` during `docker compose up`.
- If Keycloak realm/client/roles are not pre-imported, configure them once in Keycloak UI.

