# DVH - Dhole Vehicle Hub

Self-drive vehicle (cars/bikes) rental platform built with Spring Boot.
Roles: `ADMIN`, `CUSTOMER`, `VENDOR`. Minimum booking duration: 12 hours.

> Status: Work in progress (Phase 0: foundation and cleanup).

## Tech Stack
- Java 21, Spring Boot 4.1.1
- Spring Web MVC, Spring Data JPA (Hibernate), Spring Security
- MySQL
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
- MySQL running on `localhost:3306`

### Setup
1. Clone the repo
   ```bash
   git clone https://github.com/sourabhdhole83-gif/dvh.git
   cd dvh
   ```
2. Copy `.env.example` to `.env` and fill the values

For Docker Compose, MySQL is exposed on port `3307`; use the following database URL:

```properties
DB_URL=jdbc:mysql://localhost:3307/dvh?createDatabaseIfNotExist=true
DB_USERNAME=root
DB_PASSWORD=your_password
```

For a locally installed MySQL server on port `3306`, use that port in `DB_URL` instead.

3. Run
   ```bash
   ./mvnw spring-boot:run
   ```
   Flyway applies pending SQL migrations from `src/main/resources/db/migration` before Hibernate validates the schema. The app starts on `http://localhost:5555`.

   The development profile automatically baselines a non-empty existing database at version 1 to preserve its data. Hibernate then validates the existing schema against the entities. New databases run the V1 migration.

### API documentation
Open Swagger UI at `http://localhost:5555/swagger-ui/index.html`. The OpenAPI document is available at `http://localhost:5555/v3/api-docs`.

### Run tests
```bash
./mvnw test
```

## API (current)
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/v1/users` | Register a user |
| GET | `/api/v1/users/{id}` | Get user by id |

## Roadmap
JWT auth, vendor and admin flows, vehicles, bookings, KYC and payments,
notifications, Docker, React frontend, deployment.

## Contributing
Work on a feature branch and open a PR to `master` (protected branch).