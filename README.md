# Task Manager

A modern RESTful Task Manager application built with **Spring Boot 3**, demonstrating best practices of backend development including JWT authentication, Docker, CI/CD, testing, Liquibase, and PostgreSQL.

---

## Features

- JWT Authentication
- Refresh Token support
- Role-based Authorization (ADMIN / USER)
- User Management
- Task Management
- Comment Management
- PostgreSQL database
- Liquibase database migrations
- Swagger/OpenAPI documentation
- Spring Boot Actuator monitoring
- Docker & Docker Compose support
- Unit Tests (JUnit 5 + Mockito)
- Integration Tests (H2 Database)
- GitHub Actions CI

---

## Tech Stack

| Technology | Version |
|------------|---------|
| Java | 21 |
| Spring Boot | 3.5 |
| Spring Security | ✓ |
| Spring Data JPA | ✓ |
| PostgreSQL | ✓ |
| Liquibase | ✓ |
| JWT | jjwt |
| MapStruct | ✓ |
| Lombok | ✓ |
| Swagger/OpenAPI | ✓ |
| Docker | ✓ |
| Docker Compose | ✓ |
| Spring Boot Actuator | ✓ |
| GitHub Actions | ✓ |
| JUnit 5 | ✓ |
| Mockito | ✓ |
| H2 Database | ✓ |

---

## Architecture

```
Controller
    ↓
Service
    ↓
Repository
    ↓
PostgreSQL
```

---

## Project Structure

```
src
 ├── config
 ├── controller
 ├── dto
 ├── entity
 ├── enums
 ├── exception
 ├── mapper
 ├── repository
 ├── security
 ├── service
 └── resources
```

---

## REST API

### Authentication

```
POST /auth/register
POST /auth/login
POST /auth/refresh
POST /auth/logout
```

### Users

```
GET    /users
GET    /users/{id}
POST   /users
PUT    /users/{id}
DELETE /users/{id}
```

### Tasks

```
GET    /tasks
GET    /tasks/{id}
POST   /tasks
PUT    /tasks/{id}
DELETE /tasks/{id}
```

### Comments

```
GET    /comments/task/{taskId}
POST   /comments
PUT    /comments/{id}
DELETE /comments/{id}
```

---

## Running the Project

### Clone repository

```bash
git clone https://github.com/AndreyLazareff/project-final.git

cd project-final
```

---

### Run with Docker

```bash
docker compose up --build
```

---

### Run locally

Requirements:

- Java 21
- PostgreSQL

Configure database credentials inside `application.yaml` or using environment variables.

Run:

```bash
mvn spring-boot:run
```

---

## Environment Variables

```
SPRING_DATASOURCE_URL

SPRING_DATASOURCE_USERNAME

SPRING_DATASOURCE_PASSWORD
```

---

## Swagger

```
http://localhost:8080/swagger-ui/index.html
```

---

## Spring Boot Actuator

```
http://localhost:8080/actuator
```

Health endpoint

```
http://localhost:8080/actuator/health
```

---

## Running Tests

Run all tests

```bash
mvn clean test
```

or

```bash
mvn clean verify
```

---

## Continuous Integration

GitHub Actions automatically:

- Build project
- Run unit tests
- Run integration tests

on every push and pull request.

---

## Future Improvements

- Pagination
- Sorting
- Task filtering
- Search API
- Email notifications
- Docker image publishing
- Deployment pipeline
- Frontend client

---

## Author

**Andrew Lazareff**

Backend Java Developer

GitHub:

https://github.com/AndreyLazareff