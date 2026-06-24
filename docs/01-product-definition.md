# StratLab — Product Definition

## 1. Product Overview

**StratLab** is a Valorant team analytics platform for small amateur and semi-competitive teams.

The platform helps teams track matches, players, agents, maps and performance statistics in order to understand how the team is performing and where it can improve.

This is a public portfolio project designed to demonstrate real-world full stack engineering skills while using a fictional domain and safe data.

---

## 2. Problem Statement

Amateur Valorant teams often track their performance informally through screenshots, Discord messages, spreadsheets or external trackers.

This makes it difficult to answer questions such as:

- Which maps are our strongest and weakest?
- Which player is most consistent?
- Which roles are underperforming?
- Which agents are we using most often?
- Are we losing because of poor composition, low survivability or low impact?
- What should we improve before the next session?

StratLab provides a structured platform for storing match data and generating useful insights.

---

## 3. Target Users

### Team Coach

A user who reviews performance, identifies weaknesses and creates improvement plans.

### Team Captain

A player who manages team members, registers matches and reviews tactical trends.

### Player

A user who reviews personal statistics, agent performance and team context.

### Viewer

A read-only user who can inspect dashboards and reports.

Authentication and role-based access control will be implemented after the initial MVP.

---

## 4. MVP Features

The initial MVP should include:

### Player Management

- Create, update, list and delete players.
- Store nickname, tag, role, rank, peak rank and notes.
- Associate players with preferred agents later.

### Match Management

- Register matches.
- Store map, date, mode, result, score and notes.
- Associate player stats with each match.

### Match Player Statistics

- Store kills, deaths, assists, ACS, ADR, KAST, headshot percentage, first kills and first deaths.
- Use the statistics to generate aggregated analytics.

### Dashboard

- Display basic team statistics:
  - Total matches.
  - Win rate.
  - Average ACS.
  - Average ADR.
  - Best map.
  - Worst map.
  - Recent performance trend.

### Basic Recommendations

Use deterministic rules at first.

Examples:

- Low KAST may indicate poor trading or survivability.
- High ACS with low KAST may indicate aggressive impact but poor round consistency.
- Low win rate on a specific map may indicate map-specific weakness.
- Missing controller/sentinel roles may indicate poor team composition balance.

---

## 5. Out of Scope for MVP

The following features are intentionally excluded from the first MVP:

- Real Tracker.gg integration.
- Riot API integration.
- User authentication.
- AI-generated summaries.
- Team invitations.
- Real-time collaboration.
- Payment features.
- Advanced VOD review.
- Complex round-by-round analysis.

These may be added in later phases.

---

## 6. Success Criteria

The MVP is successful when:

- The project can run locally with clear instructions.
- The backend exposes documented REST APIs.
- The frontend can create and display players and matches.
- PostgreSQL stores the application data.
- Flyway manages the schema.
- At least one meaningful dashboard view exists.
- Backend tests run through Maven.
- The README is clear enough for recruiters and developers to understand the project.

---

## 7. Portfolio Value

StratLab should demonstrate:

- Full stack product thinking.
- Angular frontend development.
- Spring Boot backend development.
- PostgreSQL relational modeling.
- REST API design.
- Clean code practices.
- Automated testing.
- Documentation.
- CI/CD and deployment readiness.
- Later: AI-assisted software features.
