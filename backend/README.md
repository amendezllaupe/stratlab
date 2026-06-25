# StratLab Backend

Spring Boot 3.x REST API for the StratLab Valorant team analytics platform.

## Tech Stack

- Java 21
- Spring Boot 3.4.4
- Spring Web
- Spring Data JPA
- Spring Validation
- PostgreSQL 16
- Flyway (database migrations)
- Springdoc OpenAPI / Swagger
- JUnit 5 / Mockito / Spring Boot Test

## Prerequisites

- JDK 21
- Maven 3.9+
- Docker (for PostgreSQL)
- Docker Compose

## Quick Start

### 1. Start PostgreSQL

```bash
# From the project root
docker compose up -d postgres
```

### 2. Run the backend

```bash
cd backend
./mvnw spring-boot:run
```

The application starts at `http://localhost:8080`.

### 3. Verify it works

```bash
curl http://localhost:8080/api/health
```

Expected response:
```json
{
  "status": "UP",
  "service": "stratlab-backend"
}
```

## API Documentation

Once the backend is running, Swagger UI is available at:

```
http://localhost:8080/swagger-ui.html
```

OpenAPI JSON spec:

```
http://localhost:8080/api-docs
```

## Running Tests

```bash
cd backend
./mvnw test
```

## Database

The schema is managed with Flyway migrations located at:

```
src/main/resources/db/migration/
```

Migrations run automatically on startup.

### Database Configuration

Default connection (configurable in `application.yml`):

| Property     | Value            |
|-------------|------------------|
| URL         | `jdbc:postgresql://localhost:5432/stratlab` |
| Username    | `stratlab`       |
| Password    | `stratlab`       |

## Project Structure

```
backend/
├── src/
│   ├── main/
│   │   ├── java/com/stratlab/
│   │   │   ├── StratlabApplication.java
│   │   │   ├── config/
│   │   │   │   └── OpenApiConfig.java
│   │   │   └── health/
│   │   │       └── HealthController.java
│   │   └── resources/
│   │       ├── application.yml
│   │       └── db/migration/
│   │           └── V1__init_schema.sql
│   └── test/
│       └── java/com/stratlab/
│           └── health/
│               └── HealthControllerTest.java
├── pom.xml
└── README.md
```

## Build

```bash
./mvnw clean package
```

Produces `target/stratlab-backend-0.0.1-SNAPSHOT.jar`.

## Design Principles

- Controllers are thin — business logic belongs in services.
- DTOs are used for request and response models (entities are never exposed directly).
- Database schema is managed exclusively through Flyway migrations.
- Tests run with MockMvc for controller layer validation.
