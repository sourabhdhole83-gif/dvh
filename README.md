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

DB_URL=jdbc:mysql://localhost:3306/dvh?createDatabaseIfNotExist=true
DB_USERNAME=root
DB_PASSWORD=your_password

3. Run
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

## Roadmap
JWT auth, vendor and admin flows, vehicles, bookings, KYC and payments,
notifications, Docker, React frontend, deployment.

## Contributing
Work on a feature branch and open a PR to `master` (protected branch).