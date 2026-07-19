# Task Manager

Современное REST API-приложение для управления задачами, разработанное на **Spring Boot 3**.

Проект демонстрирует современные подходы к разработке backend-приложений с использованием Spring Security, JWT, PostgreSQL, Docker, GitHub Actions и полноценного тестирования.

---

## Возможности

- JWT-аутентификация
- Refresh Token
- Авторизация по ролям (ADMIN / USER)
- Управление пользователями
- Управление задачами
- Управление комментариями
- PostgreSQL
- Liquibase
- Swagger/OpenAPI
- Spring Boot Actuator
- Docker и Docker Compose
- Unit-тесты
- Интеграционные тесты
- GitHub Actions CI

---

## Используемые технологии

| Технология | Версия |
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
| Swagger | ✓ |
| Docker | ✓ |
| Docker Compose | ✓ |
| Spring Boot Actuator | ✓ |
| GitHub Actions | ✓ |
| JUnit 5 | ✓ |
| Mockito | ✓ |
| H2 Database | ✓ |

---

## Архитектура

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

## Структура проекта

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

### Аутентификация

```
POST /auth/register
POST /auth/login
POST /auth/refresh
POST /auth/logout
```

### Пользователи

```
GET
GET/{id}
POST
PUT
DELETE
```

### Задачи

```
GET
GET/{id}
POST
PUT
DELETE
```

### Комментарии

```
GET /comments/task/{taskId}
POST
PUT
DELETE
```

---

## Запуск проекта

### Через Docker

```bash
docker compose up --build
```

---

### Локальный запуск

Необходимо установить:

- Java 21
- PostgreSQL

Настроить подключение к базе данных через:

```
application.yaml
```

или переменные окружения

```
SPRING_DATASOURCE_URL

SPRING_DATASOURCE_USERNAME

SPRING_DATASOURCE_PASSWORD
```

После этого выполнить

```bash
mvn spring-boot:run
```

---

## Swagger

```
http://localhost:8080/swagger-ui/index.html
```

---

## Actuator

```
http://localhost:8080/actuator
```

Проверка состояния приложения

```
http://localhost:8080/actuator/health
```

---

## Запуск тестов

```bash
mvn clean test
```

или

```bash
mvn clean verify
```

---

## CI/CD

При каждом Push и Pull Request GitHub Actions автоматически:

- собирает проект;
- запускает unit-тесты;
- запускает интеграционные тесты.

---

## Возможные улучшения

- Пагинация
- Сортировка
- Фильтрация задач
- Поиск
- Email-уведомления
- Автоматическая публикация Docker-образов
- Автоматический деплой
- Frontend-клиент

---

## Автор

**Андрей Лазарев**

Java Backend Developer

GitHub:

https://github.com/AndreyLazareff