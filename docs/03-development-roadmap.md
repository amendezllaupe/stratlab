# StratLab — Development Roadmap

## Phase 0 — Project Foundation

Goal: create a clean and understandable project base.

Tasks:

- Create GitHub repository.
- Create monorepo structure.
- Add README.
- Add project documentation.
- Add initial Docker Compose file.
- Define architecture and MVP scope.

Exit criteria:

- Repository has a professional README.
- Documentation exists in `/docs`.
- Project scope is clear.
- AI agents have clear guidelines.

---

## Phase 1 — Backend Setup

Goal: create the initial Spring Boot backend.

Tasks:

- Create Spring Boot project inside `/backend`.
- Use Java 21 and Spring Boot 3.x.
- Add dependencies:
  - Spring Web
  - Spring Data JPA
  - Spring Validation
  - PostgreSQL Driver
  - Flyway
  - Springdoc OpenAPI
  - Testing dependencies
- Configure PostgreSQL connection.
- Configure Flyway.
- Add a health endpoint.
- Add Swagger/OpenAPI.
- Add initial test setup.

Exit criteria:

- Backend runs locally.
- Backend connects to PostgreSQL.
- Flyway migration runs successfully.
- Swagger is available.
- Tests run with Maven.

---

## Phase 2 — Core Domain Backend

Goal: implement the first backend domain modules.

Tasks:

- Create entities:
  - Team
  - Player
  - Agent
  - GameMap
  - Match
  - MatchPlayerStats
- Create Flyway migration `V1__init_schema.sql`.
- Implement Player CRUD.
- Implement Match CRUD.
- Add DTOs and mappers.
- Add validation.
- Add centralized exception handling.
- Add service tests.
- Add controller tests.

Exit criteria:

- Player API works.
- Match API works.
- Data persists in PostgreSQL.
- Backend exposes documented APIs.
- Core backend tests pass.

---

## Phase 3 — Frontend Setup

Goal: create the initial Angular + PrimeNG frontend.

Tasks:

- Create Angular project inside `/frontend`.
- Configure PrimeNG.
- Add routing.
- Create dashboard layout.
- Create sidebar navigation.
- Add environment configuration.
- Create API service layer.
- Create placeholder pages:
  - Dashboard
  - Players
  - Matches
  - Match Detail
  - Settings

Exit criteria:

- Frontend runs locally.
- PrimeNG is configured.
- Routing works.
- Layout is usable.
- API base URL is configurable.

---

## Phase 4 — Frontend Core Features

Goal: connect the frontend to the backend.

Tasks:

- Implement Players page.
- Implement player creation form.
- Implement player update/edit flow.
- Implement player table.
- Implement Matches page.
- Implement Match Detail page.
- Connect frontend services to backend APIs.
- Add loading and error states.

Exit criteria:

- User can create players from the frontend.
- User can view players from PostgreSQL through the backend API.
- User can create and view matches.
- UI is presentable enough for screenshots.

---

## Phase 5 — Analytics

Goal: make the project more than a CRUD.

Tasks:

- Add backend analytics endpoints:
  - Total matches
  - Win rate
  - Average ACS
  - Average ADR
  - Best map
  - Worst map
  - Recent performance
- Add dashboard cards.
- Add charts.
- Add player performance summary.
- Add map performance summary.
- Add deterministic recommendation rules.

Exit criteria:

- Dashboard shows real aggregated data.
- At least one chart exists.
- Recommendations are generated from match data.
- Project demonstrates analytical value.

---

## Phase 6 — CI/CD and Deployment

Goal: make the project portfolio-ready.

Tasks:

- Add GitHub Actions for backend tests.
- Add GitHub Actions for frontend build.
- Add Docker Compose improvements.
- Add production-ready environment documentation.
- Deploy frontend.
- Deploy backend.
- Deploy or connect cloud PostgreSQL database.
- Add screenshots to README.

Exit criteria:

- CI pipeline runs.
- Application has a public demo or documented local demo.
- README includes setup, screenshots and API docs.
- Project is ready to be shared on LinkedIn.

---

## Phase 7 — AI-Assisted Features

Goal: add AI features after the core product is stable.

Possible tasks:

- Generate match summaries from stats and notes.
- Generate team improvement recommendations.
- Generate map-specific tactical summaries.
- Create a coach assistant page.
- Store AI-generated reports.
- Add prompt templates and safeguards.

Exit criteria:

- AI features are optional and do not break core functionality.
- AI output is based on structured match data.
- The feature is documented as AI-assisted analysis, not as a replacement for human coaching.

---

## Current Next Step

Start with **Phase 0**.

After Phase 0 is committed, use an AI agent to generate the initial backend setup based on the documentation.
