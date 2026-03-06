# E-commerce Spring Boot Microservices

## Architecture

Microservices architecture built with Spring Boot 4.0.3 and Spring Cloud 2025.x. The system follows an event-driven design where services communicate asynchronously through Apache Kafka using Spring Cloud Stream. All incoming traffic is routed through an API Gateway that handles authentication via OAuth2 and Keycloak, rate limiting, circuit breaking, and service discovery through Eureka.

```
                          +------------------+
                          |    Keycloak      |
                          | (Auth Server)    |
                          | Port: 8084       |
                          +--------+---------+
                                   |
                                   | JWT Validation
                                   |
Client ----> [ API Gateway :8080 ] ----> [ Eureka Server :8761 ]
                  |                             |
                  | Routes to                   | Service Discovery
                  |                             |
       +----------+----------+-----------+
       |          |          |           |
  [ User    [ Product  [ Order      [ Notification
   :8081 ]   :8082 ]    :8083 ]       :8085 ]
   (MongoDB) (PostgreSQL)(PostgreSQL)   |
       |          |          |           |
       |          |          +--- Kafka --+
       |          |       (Producer)   (Consumer)
       |          |
       +----------+----------+-----------+
                  |
          [ Config Server :8888 ]
```

## How It Works

1. A client sends a request to the API Gateway (port 8080).
2. The Gateway validates the JWT token against Keycloak. If the token is missing or invalid, the request is rejected with 401 Unauthorized.
3. Once authenticated, the Gateway routes the request to the appropriate microservice using Eureka service discovery with load balancing.
4. Each microservice fetches its configuration from the Config Server on startup.
5. When an order is created, the Order Service publishes an OrderCreatedEvent to a Kafka topic using Spring Cloud Stream.
6. The Notification Service consumes the event from Kafka and processes it (logging, sending notifications, etc.).

## Authentication and Authorization

### OAuth2 with Keycloak

The API Gateway acts as an OAuth2 Resource Server. All requests must include a valid JWT Bearer token issued by Keycloak.

- Keycloak Realm: ecom-app
- Issuer URI: http://localhost:8084/realms/ecom-app
- Token Endpoint: http://localhost:8084/realms/ecom-app/protocol/openid-connect/token

### How to Authenticate

1. Start Keycloak on port 8084.
2. Create a realm named `ecom-app`.
3. Create a client for your application.
4. Create users and assign roles as needed.
5. Obtain an access token from the Keycloak token endpoint.
6. Include the token in your API requests:

```
Authorization: Bearer <access_token>
```

### Gateway Security Configuration

- All endpoints require authentication.
- CSRF is disabled for API usage.
- JWT tokens are validated against the Keycloak issuer URI.

## Services

### Config Server
- Port: 8888
- Centralized configuration management using Spring Cloud Config Server with native profile. All services pull their configuration from this server on startup.

### Eureka Server
- Port: 8761
- Service discovery server. All microservices register themselves here, and the API Gateway uses it to dynamically discover and route to services.

### API Gateway
- Port: 8080
- Single entry point for all client requests. Handles:
  - OAuth2 JWT authentication via Keycloak
  - Request routing and load balancing via Eureka
  - Rate limiting via Redis (10 requests per second, burst of 20)
  - Circuit breaker with Resilience4j
  - Path rewriting from public paths to internal API paths
  - Fallback responses when services are unavailable

### Gateway Routes

| Public Path     | Internal Service | Internal Path     |
|-----------------|------------------|-------------------|
| /products/**    | PRODUCT          | /api/products/**  |
| /users/**       | USER             | /api/users/**     |
| /orders/**      | ORDER-SERVICE    | /api/orders/**    |
| /cart/**        | ORDER-SERVICE    | /api/cart/**      |
| /eureka/main    | Eureka Dashboard | /                 |
| /eureka/**      | Eureka Static    | /eureka/**        |

### User Service
- Port: 8081
- Database: MongoDB
- Manages user accounts, profiles, and addresses.

### Product Service
- Port: 8082
- Database: PostgreSQL (ecommerce_product)
- Manages product catalog including creation, retrieval, and inventory tracking using JPA and Hibernate.

### Order Service
- Port: 8083
- Database: PostgreSQL (ecommerce_order)
- Manages shopping cart and order processing. When an order is created, it:
  1. Validates cart items for the user.
  2. Validates product availability via the Product Service (with retry and circuit breaker).
  3. Validates user existence via the User Service.
  4. Calculates total price.
  5. Saves the order to the database.
  6. Clears the cart.
  7. Publishes an OrderCreatedEvent to Kafka via Spring Cloud Stream.

### Notification Service
- Port: 8085
- Consumes OrderCreatedEvent messages from Kafka via Spring Cloud Stream and processes them (logging order details, triggering notifications).

## Technology Stack

### Core
- Java 25
- Spring Boot 4.0.3
- Spring Cloud 2025.x

### Frameworks and Libraries
- Spring Web MVC
- Spring WebFlux (Gateway)
- Spring Data JPA
- Spring Data MongoDB
- Spring Cloud Config
- Spring Cloud Gateway
- Spring Cloud Netflix Eureka (Client and Server)
- Spring Cloud Stream
- Spring Cloud Circuit Breaker (Resilience4j)
- Spring Boot Actuator
- Spring Security OAuth2 Resource Server
- Hibernate

### Messaging
- Apache Kafka (via Spring Cloud Stream with Kafka Binder)
- Zookeeper (Kafka dependency)

### Authentication
- Keycloak (OAuth2 / OpenID Connect)
- JWT (JSON Web Tokens)

### Databases
- PostgreSQL (Product and Order services)
- MongoDB (User service)
- Redis (Gateway rate limiting)

### Observability
- Micrometer Tracing (Brave)
- Zipkin (Distributed tracing)
- Prometheus (Metrics export)
- Spring Boot Actuator (Health checks, metrics)

### Tools
- Lombok
- Maven
- Docker / Docker Compose

## Docker Setup

### Start infrastructure services (databases, PgAdmin)

```bash
docker-compose up -d
```

### Start Kafka and Zookeeper

```bash
cd Additional
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

### Kafka
- Port: 9092
- Broker ID: 1
- Zookeeper: localhost:2181

### Zookeeper
- Port: 2181

### Keycloak
- Port: 8084
- Realm: ecom-app
- Admin Console: http://localhost:8084

## Environment Variables

### Order Service (.env)

| Variable               | Description                  |
|------------------------|------------------------------|
| DB_URL                 | PostgreSQL connection URL    |
| DB_USERNAME            | Database username            |
| DB_PASSWORD            | Database password            |

### Product Service (.env)

| Variable               | Description                  |
|------------------------|------------------------------|
| DB_URL                 | PostgreSQL connection URL    |
| DB_USERNAME            | Database username            |
| DB_PASSWORD            | Database password            |

### User Service (.env)

| Variable               | Description                  |
|------------------------|------------------------------|
| MONGO_URI              | MongoDB connection URI       |

## Running Services

### Prerequisites
1. Docker and Docker Compose installed.
2. Java 25 installed.
3. Keycloak running on port 8084 with realm `ecom-app` configured.

### Startup Order

1. Start infrastructure with Docker Compose:
   ```bash
   docker-compose up -d
   cd Additional && docker-compose up -d
   ```
2. Start Keycloak (port 8084) and configure the `ecom-app` realm.
3. Start Config Server (port 8888).
4. Start Eureka Server (port 8761).
5. Start API Gateway (port 8080).
6. Start microservices in any order:
   - User Service (port 8081)
   - Product Service (port 8082)
   - Order Service (port 8083)
   - Notification Service (port 8085)

### Verify Services

- Eureka Dashboard: http://localhost:8761
- Gateway Actuator: http://localhost:8080/actuator/health
- PgAdmin: http://localhost:5050

## API Endpoints

All requests go through the API Gateway on port 8080 and require a valid JWT token in the Authorization header.

### Users

| Method | Endpoint         | Description      |
|--------|------------------|------------------|
| GET    | /users           | List all users   |
| POST   | /users           | Create a user    |
| GET    | /users/{id}      | Get user by ID   |

### Products

| Method | Endpoint           | Description         |
|--------|--------------------|---------------------|
| GET    | /products          | List all products   |
| POST   | /products          | Create a product    |
| GET    | /products/{id}     | Get product by ID   |

### Cart

| Method | Endpoint               | Description              | Headers   |
|--------|------------------------|--------------------------|-----------|
| GET    | /cart                  | Get cart items           | X-User-ID |
| POST   | /cart                  | Add item to cart         | X-User-ID |
| DELETE | /cart/items/{productId}| Remove item from cart    | X-User-ID |

### Orders

| Method | Endpoint   | Description    | Headers   |
|--------|------------|----------------|-----------|
| POST   | /orders    | Create order   | X-User-ID |

## Kafka Configuration

### Topic
- Destination: order.exchange
- Content Type: application/json
- Broker: localhost:9092

### Producer (Order Service)
- Uses Spring Cloud Stream with Kafka Binder.
- Binding: createOrder-out-0 -> order.exchange
- Publishes OrderCreatedEvent when an order is confirmed.

### Consumer (Notification Service)
- Uses Spring Cloud Stream with Kafka Binder.
- Binding: orderCreated-in-0 -> order.exchange
- Consumer Group: notification-group
- Processes OrderCreatedEvent messages.

## Circuit Breaker Configuration

### Gateway (ecomApp)
- Sliding window size: 10
- Minimum number of calls: 5
- Failure rate threshold: 50%
- Wait duration in open state: 10s
- Permitted calls in half-open state: 3
- Fallback: /fallback/products

### Order Service (product)
- Sliding window size: 10
- Minimum number of calls: 5
- Failure rate threshold: 50%
- Wait duration in open state: 10s
- Retry: 3 attempts, 500ms wait

### Rate Limiter (Order Service)
- Limit: 2 requests per 4 seconds
- Timeout: 0s

### Rate Limiter (Gateway - Redis)
- Replenish rate: 10 requests/second
- Burst capacity: 20

## Distributed Tracing

- Zipkin URL: http://localhost:9411
- Sampling probability: 1.0 (100% of requests traced)
- All services propagate trace IDs across HTTP calls and Kafka messages.
