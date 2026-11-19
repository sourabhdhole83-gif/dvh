#DVH Devlog

## Daily log of what i built, what i learned and why

## Day 0 - Foundation: User, Vendor, exceptions, tests (23 Sep - 13 Nov 2026)

### What I did
**Project start and Vendor module (23-25 Sep)**
- Created the repo and the Spring Boot project
- Built the Vendor module: entity, repository, DTOs, MapStruct mapper, service, controller
- Added `GlobalExceptionHandler` for the Vendor module

**User module (25-26 Sep)**
- Built the User module with `POST` create user and `GET` get user by id
- Added `Role` and `UserStatus` enums, `UserRepository`
- Added `PasswordConfig` (BCrypt) and a basic `SecurityConfig`
- Extended the global exception handling and the common `APIResponse` / `ErrorResponse`
- Added custom exceptions (for example `VendorAlreadyExists`)

**Testing (30 Sep - 26 Oct)**
- Wrote `VendorServiceImplTest` (Mockito) for the vendor service
- Wrote `VendorControllerTest` for the controller layer
- Added validation tests for `@NotBlank` on `businessName` and `businessAddress`
- Added tests such as `shouldThrowVendorAlreadyExistsException` and `shouldLinkUserSaveAndReturnResponse`

**Setup and base work (12-13 Nov)**
- Ignored `.env` in git and added `.env.example`
- Upgraded the project to Java 21
- Moved DB credentials out of the code, first to environment variables, then loaded from `.env`
- Added a base entity and JPA auditing (`createdAt`, `updatedAt`)

### What I learned
- Layered structure: Controller takes the request, Service holds the business logic, Repository talks to the database. Each module has Entity, Repository, DTO, Mapper, Service, ServiceImpl and Controller.
- DTOs: the API should not expose entities directly. A DTO decides what goes in and out. MapStruct generates the entity-DTO mapping code at compile time.
- Validation: Jakarta annotations like `@NotBlank` check the request, and `@Valid` on the controller parameter turns it on.
- Exception handling: `@RestControllerAdvice` with `@ExceptionHandler` methods gives one place for error responses, with custom exceptions per case.
- Passwords are never stored as plain text. BCrypt hashes them, and `PasswordEncoder` is a bean in `PasswordConfig`.
- Entities: `@OneToOne` between Vendor and User, enums stored with `@Enumerated`, unique constraints for email and phone.
- Unit tests: Mockito mocks the repository, so the service is tested alone. A test has three parts, arrange, act, assert. Controller tests check status codes and responses.
- Secrets stay out of git. `.env` holds real values and `.env.example` shows which keys are needed.
- JPA auditing fills `createdAt` and `updatedAt` automatically through a base entity.

### Problems and fixes
- Nothing major recorded.

### Decisions (and why)
- Monolith first, microservices later only if needed
- One module per business area (user, vendor, vehicle, and so on), each with the same layers
- DTOs and MapStruct instead of returning entities
- Credentials from `.env`, not hardcoded

### Next
- Phase 0 cleanup (Day 1)

## Day 1 - Phase 0 cleanup (14 Nov 2026)

### What I did
- Removed the unused `A.java` and made `createVendor` transactional (PR #1)
- Excluded the password from `toString` and validated Indian mobile numbers
- Handled malformed JSON and data integrity violations in the exception handler
- Set the default user status and exposed the audit fields
- Went through the full flow: branch, commit, push, PR, friend review, merge (master is protected)

### What I learned
- `@Transactional` on `createVendor` means the user link and the vendor save together or not at all.
- Password hashes must not appear in `toString`, otherwise they can leak into logs.
- Malformed JSON and DB constraint errors should return a clean error response, not a 500 stack trace.
- Why changes go through a PR: review, history, and no accidental break of master.

### Problems and fixes
- Nothing major recorded.

### Decisions (and why)
- Master is protected, so all changes go through a PR and a friend's review
- Phase 0 is cleanup first, before building new features

### Next
- PR template, README, email fix

## Day 2 - PR template, README, email fix (15 Nov 2026)

### What I did
- Normalized email to lowercase before the duplicate check, with a test (PR #2)
- Added a pull request template (PR #3)
- Added the README (PR #4)

### What I learned
- `Test@mail.com` and `test@mail.com` are the same person, so the email must be normalized before checking for duplicates.
- A PR template (What, Why, Changes, Testing, Checklist) makes every PR easy to review.
- README is the front door of the repo. It tells a new person what the project is and how to run it.

### Problems and fixes
- Nothing major recorded.

### Decisions (and why)
- Keep each PR small and about one thing
- README and PR template live in the repo, so they are versioned with the code

### Next
- Profiles and Docker MySQL

## Day 3 - Profiles + Docker MySQL (15-16 Nov 2026)

### What I did
- Added `application-dev.yaml` and `application-prod.yaml` (PR #5)
- Added `docker-compose.yml` for MySQL 8.4 with a named volume and a healthcheck
- DB credentials come from `.env`

### What I learned
- `application.yaml` holds the common settings, and the active profile file is merged on top of it.
- A Docker volume keeps database data after the container is removed.
- A healthcheck tells Docker when MySQL is actually ready to accept connections.
- Port mapping `3307:3306` means host port 3307 goes to container port 3306.
- Secrets stay out of git: `.env` is ignored and `.env.example` shows the needed keys.

### Problems and fixes
- Nothing major recorded.

### Decisions (and why)
- MySQL runs on host port 3307, to avoid clashing with a local MySQL on 3306
- `dev` is the default profile, so a plain run just works locally
- `ddl-auto: validate` in both profiles, so Hibernate never changes the schema

### Next
- Flyway and Swagger

## Day 4 - Flyway + Swagger (18-19 Nov 2026)

### What I did
- Added Flyway migration `V1__init_schema.sql` and switched Hibernate to `ddl-auto: validate` (PR #7)
- Added springdoc OpenAPI / Swagger UI and `OpenApiConfig` (PR #8)
- Phase 0 closeout: restored the Flyway dependencies, fixed `SecurityConfig`, disabled Swagger in prod
- Tested dev and prod profiles on a fresh Docker MySQL
- Updated README and started this devlog

### What I learned
- Flyway owns the schema now. Migrations live in `db/migration` as `V1__...sql`, run in order, and are recorded in `flyway_schema_history`. Hibernate only validates that entities match the tables.
- Never edit a migration that has already been applied. A change means a new `V2__...` file.
- Flyway runs automatically once the dependency is on the classpath. The `enabled` and `locations` lines in the yml are just the defaults written out.
- With Spring Boot 4 the Flyway setup needs `spring-boot-starter-flyway` plus `flyway-mysql`.
- `docker compose down` keeps the data volume, `down -v` deletes it. `-v` is fine for local reset, never for prod.
- A profile is picked from `spring.profiles.default` (dev) unless I override it. With Maven the prod run is `-Dspring-boot.run.profiles=prod`, and the startup log shows which profile is active.
- Swagger lists every endpoint and request shape, so it is switched off in prod with `springdoc.api-docs.enabled` and `springdoc.swagger-ui.enabled`.
- In `SecurityConfig`, specific matchers go first and `anyRequest()` goes last, in a single `authorizeHttpRequests` block.
- Merge conflicts: read both sides, then decide. "Accept both" when both changes are needed.

### Problems and fixes
- Problem: Flyway dependencies kept disappearing from `pom.xml`
  Cause: while resolving the merge conflict in the Swagger PR I kept only one side, so the Flyway dependencies were dropped
  Fix: restored both, and now I check `git diff` before every commit
- Problem: prod run failed with "Port 5555 was already in use"
  Cause: the dev instance was still running
  Fix: stopped it (Ctrl+C, or `netstat -ano` and `taskkill`) and ran again
- Problem: Flyway needs an empty database or a baseline
  Cause: the tables were earlier created by Hibernate, so there was no Flyway history
  Fix: reset the local DB with `docker compose down -v` and let Flyway create everything

### Decisions (and why)
- Swagger is enabled only in dev, because it exposes the whole API structure
- Flyway creates the tables and Hibernate only validates, so the schema is versioned and reproducible
- Docs and closeout fixes go in separate small PRs

### Next
- Phase 1: JWT authentication