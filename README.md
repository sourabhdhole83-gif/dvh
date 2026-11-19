# DVH - Dhole Vehicle Hub

Self-drive vehicle (cars/bikes) rental platform built with Spring Boot.
Roles: `ADMIN`, `CUSTOMER`, `VENDOR`. Minimum booking duration: 12 hours.

> Status: Phase 1 (JWT authentication) in progress.

## Tech Stack
- Java 21, Spring Boot 4.1.1
- Spring Web MVC, Spring Data JPA (Hibernate), Spring Security
- MySQL 8.4 (Docker), Flyway migrations
- springdoc-openapi (Swagger UI)
- MapStruct, Lombok, Jakarta Validation
- JUnit 5, Mockito, AssertJ

## Modules
| Module  | Status |
|---------|--------|
| user    | Registration, get by id |
| vendor  | Create vendor |
| vehicle | Entity and repository only |
| booking, payment, KYC, notifications, admin | Planned |

## Getting Started

### Prerequisites
- JDK 21
- Docker Desktop

### Setup
1. Clone the repo
   ```bash
   git clone https://github.com/sourabhdhole83-gif/dvh.git
   cd dvh
   ```
2. Copy `.env.example` to `.env` and fill the values:
   ```
   DB_URL=jdbc:mysql://localhost:3307/dvh?createDatabaseIfNotExist=true
   DB_USERNAME=root
   DB_PASSWORD=your_password
   ```
3. Start MySQL:
   ```bash
   docker compose up -d
   ```
4. Run the app:
   ```bash
   ./mvnw spring-boot:run
   ```
   The app starts on `http://localhost:5555`.

### Run tests
```bash
./mvnw test
```

## API (current)
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/v1/users` | Register a user |
| GET | `/api/v1/users/{id}` | Get user by id |

## Profiles
| Profile | How to run | Swagger | SQL logs |
|---------|-----------|---------|----------|
| dev (default) | `./mvnw spring-boot:run` | enabled | on |
| prod | `./mvnw spring-boot:run "-Dspring-boot.run.profiles=prod"` | disabled | off |

## Database migrations
Schema is managed by Flyway. Files live in `src/main/resources/db/migration`
and are named `V1__description.sql`. Never edit a migration that has been
applied; add a new `V2__...` file instead. Hibernate only validates the schema.

To reset the local database:
```bash
docker compose down -v
docker compose up -d
```

## API docs
Swagger UI (dev profile only): http://localhost:5555/swagger-ui.html

## Development log
See [docs/DEVLOG.md](docs/DEVLOG.md).

## Roadmap
1. JWT authentication
2. Vendor and admin flows
3. Vehicles
4. Bookings
5. KYC and payments
6. Notifications
7. Production-grade setup
8. React frontend
9. Deployment

## Contributing
Work on a feature branch and open a PR to `master` (protected branch).