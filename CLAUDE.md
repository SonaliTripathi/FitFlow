# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project state

FitFlow is a Spring Boot 3.5.16 / Java 21 backend, currently at initial-scaffold stage: no
controllers, services, entities, or repositories exist yet beyond the generated
`FitflowApplication` entry point. There is no source control (`git init` has not been run) — check
with the user before assuming git commands will work.

## Commands

Database (required before running the app or any test that touches JPA/datasource):
```bash
docker-compose up -d
```

Run the app:
```bash
./mvnw spring-boot:run
```

Build:
```bash
./mvnw clean install
```

Run all tests:
```bash
./mvnw test
```

Run a single test class:
```bash
./mvnw test -Dtest=FitflowApplicationTests
```

Run a single test method:
```bash
./mvnw test -Dtest=FitflowApplicationTests#contextLoads
```

## Architecture

- Base package: `com.fitflow.fitflow`
- Persistence: Spring Data JPA against PostgreSQL. `docker-compose.yml` defines the local Postgres
  instance (db `fitflow`, user/password `fitflow`, port 5432) matching the datasource config in
  `src/main/resources/application.properties`.
- `spring.jpa.hibernate.ddl-auto=update` — schema is derived from JPA entities automatically in
  this environment; there is no separate migration tool (e.g. Flyway/Liquibase) configured.
- Dependencies present: `spring-boot-starter-web`, `spring-boot-starter-data-jpa`,
  `spring-boot-starter-validation`, `postgresql` driver. No security, no messaging, no caching
  layer configured yet.