# StratLab — AI Agent Guidelines

This project may use AI coding agents such as OpenCode to accelerate development.

The AI agent must follow these guidelines to keep the project clean, consistent and portfolio-ready.

---

## 1. General Rules

- Do not rewrite large parts of the project without explicit permission.
- Do not add new dependencies without explaining why.
- Do not bypass the existing architecture.
- Do not duplicate DTOs, services, components or utilities.
- Prefer small, incremental changes.
- Keep code readable over clever.
- Use professional naming.
- Ask for clarification when the task is ambiguous.
- Update documentation when architecture changes.

---

## 2. Backend Rules

- Use Java 21 and Spring Boot 3.x.
- Keep controllers thin.
- Put business logic in services.
- Use repositories for persistence.
- Use DTOs for request and response models.
- Do not expose JPA entities directly through controllers.
- Use validation annotations on request DTOs.
- Use centralized exception handling.
- Use Flyway for database schema changes.
- Do not modify old migrations after they are committed; create new migrations instead.
- Add or update tests when backend logic changes.
- Avoid over-engineering the MVP.

---

## 3. Frontend Rules

- Use Angular and TypeScript.
- Use PrimeNG for UI components.
- Use routing for feature pages.
- Keep API calls inside services.
- Do not hardcode backend URLs in components.
- Use environment configuration for API URLs.
- Keep components small and readable.
- Prefer typed interfaces for API models.
- Avoid unnecessary state management libraries unless the project requires them later.
- Add loading and error states for API-based screens.

---

## 4. Database Rules

- Use PostgreSQL.
- Use clear table names.
- Use primary keys and foreign keys.
- Add constraints where useful.
- Use indexes when a query pattern requires them.
- Avoid unnecessary denormalization in the MVP.
- Use Flyway migrations for every schema change.

---

## 5. Testing Rules

- Backend changes should include or update tests when possible.
- Service logic should have unit tests.
- Controller endpoints should have MockMvc tests when relevant.
- Do not remove tests to make the build pass.
- If a test fails, explain why before changing the implementation.

---

## 6. AI Feature Rules

AI product features are not part of the initial MVP.

Do not implement:

- OpenAI integration.
- Prompt templates.
- AI-generated summaries.
- AI recommendation endpoints.
- AI coach assistant.

These features belong to a later roadmap phase.

AI may be used to help write code, documentation and tests, but not as a runtime feature until Phase 7.

---

## 7. Output Format for AI Agent Tasks

When completing a task, provide:

1. Summary of what changed.
2. Files created or modified.
3. How to run or test the change.
4. Assumptions made.
5. Pending tasks or technical debt.

---

## 8. Definition of Done

A task is done when:

- The implementation matches the requested scope.
- The code is readable.
- The project still builds.
- Relevant tests pass or are documented.
- No unrelated files were modified.
- Documentation is updated if needed.
