# E-commerce Spring Boot Microservices

## Architecture
Microservices architecture with Spring Boot 4.0.3 and Spring Cloud 2025.1.0

## Services

### Config Server
Port: 8888
Centralized configuration management using Spring Cloud Config Server with native profile

### User Service
Port: 8080
Database: MongoDB
Features: User management with MongoDB persistence

### Product Service
Port: 8081
Database: PostgreSQL (ecommerce_product)
Features: Product management with JPA and Hibernate

### Order Service
Port: 8082
Database: PostgreSQL
Features: Order management with JPA and Hibernate

## Technology Stack

### Core
- Java 25
- Spring Boot 4.0.3
- Spring Cloud 2025.1.0

### Frameworks
- Spring Web MVC
- Spring Data JPA
- Spring Data MongoDB
- Spring Cloud Config
- Spring Cloud Bus
- Spring Boot Actuator
- Hibernate 7.2.4.Final

### Messaging
- RabbitMQ 4 Management
- Spring AMQP

### Database
- PostgreSQL 42.7.10
- MongoDB

### Tools
- Lombok
- Maven

## Docker Setup

### Start all services
```bash
docker-compose up -d
```

### Stop all services
```bash
docker-compose down
```

### Stop and remove volumes
```bash
docker-compose down -v
```

## Docker Services

### PostgreSQL
- Port: 5433 (host) -> 5432 (container)
- Username: postgres
- Password: arpon007
- Database: ecommerce_product

### PgAdmin
- URL: http://localhost:5050
- Email: admin@admin.com
- Password: admin
- PostgreSQL connection host: postgres (port 5432 inside docker network)

### MongoDB
- Port: 27018 (host) -> 27017 (container)
- Username: admin
- Password: admin123

### RabbitMQ
- AMQP Port: 5672
- Management UI: http://localhost:15672
- Username: guest
- Password: guest

## Environment Variables

### For Docker Setup

#### Order Service
- DB_URL: jdbc:postgresql://localhost:5433/ecommerce_product
- DB_USERNAME: postgres
- DB_PASS: arpon007

#### Product Service
- DB_URL: jdbc:postgresql://localhost:5433/ecommerce_product
- DB_USERNAME: postgres
- DB_PASS: arpon007

#### User Service
- MONGO_URI: mongodb://admin:admin123@localhost:27018
- DB_NAME: ecommerce_user

### For Local Setup (without Docker)

#### Order Service
- DB_URL: PostgreSQL connection URL
- DB_USERNAME: Database username
- DB_PASS: Database password

#### Product Service
- DB_URL: jdbc:postgresql://localhost:5432/ecommerce_product (default)
- DB_USERNAME: postgres (default)
- DB_PASS: arpon007 (default)

#### User Service
- MONGO_URI: mongodb://localhost:27017 (default)
- DB_NAME: Database name

## Running Services

### Using Docker
1. Run `docker-compose up -d` to start all infrastructure services
2. Start Config Server on port 8888
3. Start User Service on port 8080
4. Start Product Service on port 8081
5. Start Order Service on port 8082

### Without Docker
1. Start RabbitMQ manually
2. Start PostgreSQL and MongoDB manually
3. Start Config Server on port 8888
4. Start User Service on port 8080
5. Start Product Service on port 8081
6. Start Order Service on port 8082

## Endpoints

Management endpoints exposed via Spring Boot Actuator on all services

## RabbitMQ Configuration
- Host: localhost
- Port: 5672
- Username: guest
- Password: guest
