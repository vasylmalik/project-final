## [REST API](http://localhost:8080/doc)

## Концепция:

- Spring Modulith
    - [Spring Modulith: достигли ли мы зрелости модульности](https://habr.com/ru/post/701984/)
    - [Introducing Spring Modulith](https://spring.io/blog/2022/10/21/introducing-spring-modulith)
    - [Spring Modulith - Reference documentation](https://docs.spring.io/spring-modulith/docs/current-SNAPSHOT/reference/html/)

## Быстрый запуск (bash)

1. Убедитесь, что установлены **Docker**, **Maven** и **Git Bash** (или WSL).
2. Выполните в корне проекта:

```bash
mvn clean package -DskipTests
```
```bash
docker build -t jira-rush .
```
```bash
docker run -d -p 8080:8080 -e DB_USERNAME=jira -e DB_PASSWORD=JiraRush -e DB_URL=jdbc:postgresql://host.docker.internal:5432/jira --name jira-rush-app jira-rush
```
## Запуск БД
```
  url: jdbc:postgresql://localhost:5432/jira
  username: jira
  password: JiraRush
```

### Prod
```bash 
docker run -p 5432:5432 --name postgres-db -e POSTGRES_USER=jira -e POSTGRES_PASSWORD=JiraRush -e POSTGRES_DB=jira -e PGDATA=/var/lib/postgresql/data/pgdata -v ./pgdata:/var/lib/postgresql/data -d postgres
```
### test
```bash 
docker run -p 5433:5432 --name postgres-db-test -e POSTGRES_USER=jira -e POSTGRES_PASSWORD=JiraRush -e POSTGRES_DB=jira-test -e PGDATA=/var/lib/postgresql/data/pgdata -v ./pgdata-test:/var/lib/postgresql/data -d postgres
```
- Есть 2 общие таблицы, на которых не fk
    - _Reference_ - справочник. Связь делаем по _code_ (по id нельзя, тк id привязано к окружению-конкретной базе)
    - _UserBelong_ - привязка юзеров с типом (owner, lead, ...) к объекту (таска, проект, спринт, ...). FK вручную будем
      проверять

## Аналоги

- https://java-source.net/open-source/issue-trackers

## Тестирование

- https://habr.com/ru/articles/259055/

Список выполненных задач:
1. Удалить социальные сети: vk, yandex. 18.06.2026
2. Вынести чувствительную информацию в отдельный проперти файл. 21.06.2026
3. Переделать тесты так, чтоб во время тестов использовалась in memory БД (H2), а не PostgreSQL. 21.06.2026
4. Написать тесты для всех публичных методов контроллера ProfileRestController. 22.06.2026
5. Сделать рефакторинг метода com.javarush.jira.bugtracking.attachment.FileUtil#upload. 22.06.2026
6. Добавить новый функционал: добавления тегов к задаче (REST API + реализация на сервисе) 22.06.2026
7. Добавить подсчет времени сколько задача находилась в работе и тестировании. 23.06.2026
8. Написать Dockerfile для основного сервера. 24.06.2026
9. Написать docker-compose файл для запуска контейнера сервера вместе с БД и nginx. 24.06.2026