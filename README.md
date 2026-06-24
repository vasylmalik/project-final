## [REST API](http://localhost:8080/doc)

## Концепция:

- Spring Modulith
    - [Spring Modulith: достигли ли мы зрелости модульности](https://habr.com/ru/post/701984/)
    - [Introducing Spring Modulith](https://spring.io/blog/2022/10/21/introducing-spring-modulith)
    - [Spring Modulith - Reference documentation](https://docs.spring.io/spring-modulith/docs/current-SNAPSHOT/reference/html/)

```
  url: jdbc:postgresql://localhost:5432/jira
  username: jira
  password: JiraRush
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
## Команды загрузки
```bash
docker run -p 5432:5432 --name postgres-db -e POSTGRES_USER=jira -e POSTGRES_PASSWORD=JiraRush -e POSTGRES_DB=jira -e PGDATA=/var/lib/postgresql/data/pgdata -v $(pwd)/pgdata:/var/lib/postgresql/data -d postgres
```
```bash
docker run -p 5433:5432 --name postgres-db-test -e POSTGRES_USER=jira -e POSTGRES_PASSWORD=JiraRush -e POSTGRES_DB=jira-test -e PGDATA=/var/lib/postgresql/data/pgdata -v $(pwd)/pgdata-test:/var/lib/postgresql/data -d postgres
```

1.  Разобраться со структурой проекта (onboarding).✅

2.  Удалить социальные сети: vk, yandex. `Easy task`✅

3.  Вынести чувствительную информацию в отдельный проперти файл: ✅
  - логин
  - пароль БД
  - идентификаторы для OAuth регистрации/авторизации
  - настройки почты

    Значения этих проперти должны считываться при старте сервера из переменных окружения машины. `Easy task`
  - ```
    добавлен .env файл в проект 
    написан тест
    проект учебный .env не добавлен в gitignore
    ```

4.  Переделать тесты так, чтоб во время тестов использовалась **in memory БД (H2)**, а не PostgreSQL. Для этого нужно определить 2 бина, и выборка какой из них использовать должно определяться активным профилем Spring. H2 не поддерживает все фичи, которые есть у PostgreSQL, поэтому тебе прийдется немного упростить скрипты с тестовыми данными.

5.  Написать тесты для всех публичных методов контроллера `ProfileRestController`. Хоть методов только 2, но тестовых методов должно быть больше, т.к. нужно проверить success and unsuccess path. ✅

6.  Сделать рефакторинг метода `com.javarush.jira.bugtracking.attachment.FileUtil#upload` чтоб он использовал современный подход для работы с файловой системмой. `Easy task` ✅

7.  Добавить новый функционал: добавления тегов к задаче (REST API + реализация на сервисе). Фронт делать необязательно. Таблица `task_tag` уже создана.

8.  Добавить подсчет времени сколько задача находилась в работе и тестировании. Написать 2 метода на уровне сервиса, которые параметром принимают задачу и возвращают затраченное время: ✅
  - Сколько задача находилась в работе (ready_for_review минус in_progress).
  - Сколько задача находилась на тестировании (done минус ready_for_review).

    Для написания этого задания, нужно добавить в конец скрипта инициализации базы данных `changelog.sql` **3 записи** в таблицу `ACTIVITY`:
    ```
    insert into ACTIVITY ( ID, AUTHOR_ID, TASK_ID, UPDATED, STATUS_CODE ) values ...
    ```
Со статусами:
- время начала работы над задачей – **in_progress**
- время окончания разработки - **ready_for_review**
- время конца тестирования - **done**

9.  Написать `Dockerfile` для основного сервера ✅
```
добавлен Dockerfile и docker-compose.yml
docker build --no-cache --progress=plain -t project-final-app .

```
```bash
docker build --no-cache --progress=plain -t project-final-app .
```
```bash
docker-compose up -d
```

10. Написать `docker-compose` файл для запуска контейнера сервера вместе с БД и [nginx](https://nginx.org/ru/docs/). Для nginx используй конфиг-файл `config/nginx.conf`. При необходимости файл конфига можно редактировать. `Hard task` ✅
```bash
docker-compose down -v
```
```bash
docker-compose up -d --build
```
```bash

```
11. Добавить локализацию минимум на двух языках для шаблонов писем (mails) и стартовой страницы `index.html`.

12. Переделать механизм распознавания «свой-чужой» между фронтом и беком с `JSESSIONID` на `JWT`. Из сложностей – тебе придётся переделать отправку форм с фронта, чтоб добавлять хедер аутентификации. `Extra-hard task`
...