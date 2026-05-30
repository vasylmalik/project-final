# JiraRush

Мини-проект в стиле JIRA на `Spring Boot 3`, `Java 17`, `PostgreSQL`, `Liquibase`, `Thymeleaf` и `Spring Security`.

## REST API

- Swagger UI через `nginx`: [http://localhost/doc](http://localhost/doc)
- Прямой backend: [http://localhost:8080/doc](http://localhost:8080/doc)

## Технологии

- Java 17
- Spring Boot 3
- Spring Security
- Spring Data JPA
- PostgreSQL
- H2 для тестов
- Liquibase
- Thymeleaf
- Docker
- Docker Compose
- Nginx

## Структура проекта

- `src/main/java` — основной Java-код
- `src/main/resources` — конфиги приложения, Liquibase, SQL
- `src/test/java` — тесты
- `src/test/resources` — тестовые данные и тестовый changelog
- `resources/view` — HTML-шаблоны
- `resources/static` — CSS, JS, шрифты, картинки
- `config` — дополнительные конфиги, включая `nginx.conf`

## Переменные окружения

Чувствительные данные вынесены из `application.yaml` в environment variables.

Для локальной работы нужен файл [`.env.example`](/C:/ProjectJavaRush/.env.example) как шаблон.
Локально нужно создать [`.env`](/C:/ProjectJavaRush/.env) и заполнить его значениями.

```env
DB_USERNAME=
DB_PASSWORD=

GITHUB_CLIENT_ID=
GITHUB_CLIENT_SECRET=

GOOGLE_CLIENT_ID=
GOOGLE_CLIENT_SECRET=

GITLAB_CLIENT_ID=
GITLAB_CLIENT_SECRET=

MAIL_USERNAME=
MAIL_PASSWORD=
```

## Запуск через Docker

Основной способ запуска проекта:

```powershell
docker compose up --build
```

После старта доступны:

- [http://localhost](http://localhost) — вход через `nginx`
- [http://localhost:8080](http://localhost:8080) — прямой доступ к Spring Boot
- [http://localhost/doc](http://localhost/doc) — Swagger через `nginx`

Остановка контейнеров:

```powershell
docker compose down
```

Если менялся `config/nginx.conf`, может понадобиться перезапуск контейнера `nginx`.

## Прогресс

### Выполнено

- Разобрался со структурой проекта.
- Удалил `vk` и `yandex` из OAuth-конфига и UI.
- Вынес чувствительную информацию в environment variables.
- Добавил `Dockerfile` для основного сервера.
- Добавил `docker-compose.yml` для запуска сервера, БД и `nginx`.
- Адаптировал `config/nginx.conf` под Docker Compose.
- Исправил запуск приложения через `RestAuthenticationEntryPoint`.
- Перевёл тесты на H2 вместо PostgreSQL.
- Отрефакторил `FileUtil#upload` на `java.nio.file`.
- Написал тесты для `ProfileRestController`.
- Реализовал теги для задач.

### Осталось сделать

- Разобраться с проблемами авторизации и регистрации.
- Проверить OAuth redirect URI для Google.
- Реализовать подсчёт времени по статусам.

## Проблемы и решения

- Локальный запуск в IntelliJ падал без `DB_USERNAME` и других env-переменных; для проверки я запускал приложение через Docker и отдельно вынес чувствительные значения в `.env`.
- H2 не принимал часть PostgreSQL-специфичных скриптов; для тестов я сделал отдельный `src/test/resources/db/changelog-test.sql` и упростил `src/test/resources/data.sql`.
- В `ProfileRestController` тесты пришлось подстроить под текущую реализацию: `GET` возвращает профиль с `id`, а `PUT` валидирует ID и контакты и отдаёт `422` на некорректные данные.
- Для тегов у `Task.tags` вылезал `LazyInitializationException`; я решил это через `LEFT JOIN FETCH t.tags` в `findFullById`, чтобы full-ответ задачи сразу содержал теги.

## Выполненные пункты задания

- Разобраться со структурой проекта.
- Удалить социальные сети: `vk`, `yandex`.
- Вынести чувствительную информацию в отдельные properties и читать её из переменных окружения.
- Перевести тесты на H2.
- Написать Dockerfile для основного сервера.
- Написать docker-compose для запуска сервера вместе с БД и `nginx`.
- Сделать рефакторинг `FileUtil#upload` на современный API файловой системы.
- Написать тесты для `ProfileRestController`.
- Реализовать теги для задач.

## Известные проблемы

- Google OAuth сейчас может падать с `redirect_uri_mismatch`.
- Блок авторизации и регистрации ещё нужно отдельно проверить после стабилизации инфраструктуры.
