# StratLab

**StratLab** is a full stack Valorant team analytics platform designed to help amateur and semi-competitive teams analyze players, matches, maps, agents, roles and performance trends.

The project is built as a professional portfolio application to demonstrate full stack engineering skills using **Angular**, **Spring Boot**, **PostgreSQL**, **Flyway**, **REST APIs**, automated testing and CI/CD practices.

> This project uses a fictional domain and synthetic/manual data. It does not expose private professional work or confidential information.

---

## Project Goals

StratLab aims to provide a structured way to:

- Register teams and players.
- Track Valorant matches and player statistics.
- Import audited historical season snapshots exported by the team.
- Analyze team performance by map, agent, role and match history.
- Identify strengths, weaknesses and improvement opportunities.
- Provide tactical recommendations based on match data.
- Later integrate AI-assisted match summaries and coaching insights.

---

## Tech Stack

### Frontend

- Angular
- TypeScript
- PrimeNG
- Reactive Forms
- Angular Router
- Charting library to be defined

### Backend

- Java 21
- Spring Boot 3.x
- Spring Web
- Spring Data JPA
- Spring Validation
- Springdoc OpenAPI / Swagger

### Database

- PostgreSQL
- Flyway migrations

### DevOps / Tooling

- Docker Compose
- GitHub Actions
- Maven
- Git / GitHub

### Testing

- JUnit
- Mockito
- Spring Boot Test
- MockMvc
- Testcontainers later if needed

---

## Repository Structure

```txt
stratlab/
  backend/
  frontend/
  docs/
    01-product-definition.md
    02-technical-architecture.md
    03-development-roadmap.md
    04-ai-agent-guidelines.md
  docker-compose.yml
  README.md
```

---

## MVP Scope

The first version of StratLab will focus on:

- Backend setup with Spring Boot.
- PostgreSQL schema managed with Flyway.
- Basic entities and REST APIs.
- Angular dashboard layout.
- Player management.
- Match management.
- Basic team analytics.
- API documentation with Swagger.
- Backend automated tests.
- Local execution with Docker Compose.

AI features are intentionally excluded from the initial MVP and will be added in a later phase.

---

## Planned Core Modules

- Teams
- Players
- Agents
- Maps
- Matches
- Match player statistics
- Dashboard analytics
- Recommendations
- AI-assisted analysis, later phase

## Historical Data Boundary

Historical Tracker exports remain in the separate `valorant-analisis` handbook. StratLab imports supported snapshots through a documented API and stores normalized records plus provenance; it does not scrape Tracker.gg. See [`docs/05-historical-data-import.md`](docs/05-historical-data-import.md).

---

## Development Status

Current phase: **Project definition and initial setup**.

Next milestone:

> Run a local full stack application where a user can create Valorant players from Angular, store them through a Spring Boot REST API and persist them in PostgreSQL.

---

## Contact

Created by [Alfredo Méndez Llaupe](https://github.com/amendezllaupe).
