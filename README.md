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

## Build the service
```sh
cd ledger-service
mvn clean package -DskipTests
```

### Start All Services (from project root)
```sh
docker-compose up --build
```
This will start:
- `transfer-service` (on port 8080)
- `ledger-service` (on port 8081)
- Separate Postgres databases for each service
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


### Development Notes
- The shared `docker-compose.yml` is at the project root and manages both services and their databases.
- Each service has its own database and does not access the other's tables.
- For local development, you can build and test each service independently with Maven.
- No authentication required by default, but code is structured for easy future integration (see SOLUTION.md).
- For troubleshooting, check logs in the Docker containers.
- **Docker Compose is shared:** This service relies on the `docker-compose.yml` in the base project root. Make sure to run Docker Compose commands from the base directory, not from within `ledger-service`.

### CI
A minimal GitHub Actions workflow is provided in `.github/workflows/ci.yml` to build and test the service on push/PR.


