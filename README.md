# Ledger Service

This project is a minimal FinTech-style payments system (Ledger Service) built with Spring Boot and PostgreSQL. It supports internal wallet transfers, account management, and an immutable ledger, with a focus on idempotency and atomicity.

## Features
- Create and fetch accounts
- Apply transfers between accounts (with idempotency)
- Atomic updates and optimistic locking
- RESTful API (OpenAPI/Swagger enabled)
- PostgreSQL persistence (Dockerized)
- Minimal CI (GitHub Actions)
- Static code analysis (Qodana)

## Prerequisites
- Java 21
- Maven 3.8+
- Docker & Docker Compose

## Build & Test

```
mvn clean verify
```

## Run Locally (with Docker Compose)

1. **Build the JAR:**
   ```
   mvn clean package -DskipTests
   ```
2. **Start services (Postgres + Ledger Service):**
   
   **Note:** This project uses a shared `docker-compose.yml` located in the base project root (not in this directory).
   
   From the base project root, run:
   ```
   docker-compose up --build
   ```
   This will:
   - Start a PostgreSQL instance (with schema/init.sql)
   - Start the Ledger Service (on port 8081 by default)

3. **API Docs:**
   - Visit [http://localhost:8081/swagger-ui.html](http://localhost:8081/swagger-ui.html) for OpenAPI docs.

## Configuration
- Application config: `src/main/resources/application.yml` or `application-dev.yml`
- Database config: see `db/init.sql` and Docker Compose file in the base project root
- Ports: Ledger Service (8081), Postgres (see compose file)

## Running Tests

```
mvn test
```

## Static Code Analysis (Qodana)

Qodana runs automatically on push/PR via GitHub Actions. To run locally:

```
docker run -it --rm -v $(pwd):/data/project -p 8082:8082 jetbrains/qodana-jvm
```

## API Endpoints
- `POST /accounts` – Create account
- `GET /accounts/{id}` – Fetch account
- `POST /ledger/transfer` – Apply transfer
- `GET /health` – Health check

## Notes
- No authentication required by default, but code is structured for easy future integration (see SOLUTION.md).
- For troubleshooting, check logs in the Docker containers.
- **Docker Compose is shared:** This service relies on the `docker-compose.yml` in the base project root. Make sure to run Docker Compose commands from the base directory, not from within `ledger-service`.

---

**For more details, see SOLUTION.md.**
