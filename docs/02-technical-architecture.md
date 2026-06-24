# StratLab — Technical Architecture

## 1. Architecture Style

StratLab will start as a modular full stack application with a separated frontend and backend inside a monorepo.

Initial structure:

```txt
stratlab/
  backend/
  frontend/
  docs/
  docker-compose.yml
  README.md
```

The backend will expose REST APIs consumed by the Angular frontend.

The database schema will be managed using Flyway migrations.

---

## 2. Frontend Architecture

### Stack

- Angular
- TypeScript
- PrimeNG
- Angular Router
- Reactive Forms
- Charting library to be defined

### Suggested Structure

```txt
frontend/src/app/
  core/
    services/
    interceptors/
    models/
  layout/
    components/
  features/
    dashboard/
    players/
    matches/
    analytics/
    settings/
  shared/
    components/
    pipes/
    utils/
```

### Frontend Principles

- Use feature-based organization.
- Keep components small and readable.
- Keep API calls inside services.
- Do not hardcode API URLs inside components.
- Use environment configuration for backend URL.
- Prefer typed interfaces for API responses.
- Use PrimeNG for tables, forms, cards and layout components.
- Avoid premature state management libraries unless needed.

---

## 3. Backend Architecture

### Stack

- Java 21
- Spring Boot 3.x
- Spring Web
- Spring Data JPA
- Spring Validation
- PostgreSQL
- Flyway
- Springdoc OpenAPI / Swagger

### Suggested Package Structure

```txt
backend/src/main/java/com/stratlab/
  StratlabApplication.java
  config/
  common/
    exception/
    validation/
  team/
  player/
  agent/
  gamemap/
  match/
  stats/
  analytics/
```

Each domain package may contain:

```txt
controller/
service/
repository/
entity/
dto/
mapper/
```

### Backend Principles

- Controllers should be thin.
- Business logic belongs in services.
- Repositories handle persistence.
- Use DTOs for input and output.
- Do not expose JPA entities directly through REST APIs.
- Use validation annotations for request DTOs.
- Use meaningful exceptions and centralized error handling.
- Use transactions where needed.
- Keep the MVP simple and avoid over-engineering.

---

## 4. Initial Domain Model

### Team

Represents a Valorant team.

Basic fields:

- id
- name
- tag
- description
- createdAt
- updatedAt

### Player

Represents a player in a team.

Basic fields:

- id
- teamId
- nickname
- riotTag
- mainRole
- currentRank
- peakRank
- notes
- active
- createdAt
- updatedAt

### Agent

Represents a Valorant agent.

Basic fields:

- id
- name
- role
- active

### GameMap

Represents a Valorant map.

Basic fields:

- id
- name
- active

### Match

Represents a team match.

Basic fields:

- id
- teamId
- gameMapId
- matchDate
- mode
- result
- teamScore
- enemyScore
- notes
- createdAt
- updatedAt

### MatchPlayerStats

Represents a player's performance in a match.

Basic fields:

- id
- matchId
- playerId
- agentId
- kills
- deaths
- assists
- acs
- adr
- kast
- headshotPercentage
- firstKills
- firstDeaths

---

## 5. Database

### Database Engine

PostgreSQL.

### Migration Tool

Flyway.

### Initial Migration

```txt
backend/src/main/resources/db/migration/V1__init_schema.sql
```

### Database Principles

- Use clear table names.
- Use foreign keys for relationships.
- Use constraints where useful.
- Add indexes when queries require them.
- Avoid storing calculated values unless necessary.
- Use migrations for every schema change.

---

## 6. API Documentation

Swagger/OpenAPI should be enabled for backend APIs.

Suggested local path:

```txt
http://localhost:8080/swagger-ui.html
```

or Springdoc default path depending on configuration.

---

## 7. Testing Strategy

### Initial Backend Testing

- Unit tests for services.
- Controller tests with MockMvc.
- Repository tests later if needed.
- Integration tests later with Testcontainers.

### Frontend Testing

Frontend tests are optional for the first MVP, but the structure should not prevent adding them later.

---

## 8. Dev Environment

The local environment should be reproducible with Docker Compose.

Initial Docker Compose services:

- PostgreSQL database
- Backend later if useful
- Frontend later if useful

Initial priority:

- Run PostgreSQL through Docker.
- Run backend locally with Maven.
- Run frontend locally with Angular CLI.

---

## 9. AI Feature Strategy

AI features are not part of the initial MVP.

Potential future AI features:

- Match summary generation.
- Team weakness analysis.
- Improvement recommendations.
- Coach assistant.
- Natural language tactical report.

AI integration should be added only after the core data model, analytics and UI are stable.

---

## 10. Security Strategy

Authentication is not part of the first MVP.

Later phases may include:

- Spring Security.
- JWT authentication.
- Role-based access control.
- Admin, Coach, Player and Viewer roles.
