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

Task 1. Анализ структуры проекта (onboarding).

Task 2. Удалены социальные сети: vk, yandex.

Task 3. Чувствительная информация вынесена в отдельный проперти файл:
     логин,
     пароль БД,
     идентификаторы для OAuth регистрации/авторизации,
     настройки почты.
Основной вариант properties.env; альтернативный - environment-vars.yaml.

Task 5. Написаны тесты для публичных методов контроллера ProfileRestController.

Task s.n. Рефакторинг сокращения TO -> DTO в классах и методах. Лучше читается и не путается с "to".

Task 6. Рефакторинг метода com.javarush.jira.bugtracking.attachment.FileUtil#upload,
чтобы он использовал современный подход для работы с файловой системмой.

Task 7. Новый функционал: добавление тегов к задаче (REST API + реализация на сервисе). Также добавлены тесты.

Task 11. Локализация на двух языках для шаблонов писем (mails) и стартовой страницы index.html.

Task s.n. + 9-10. Dockerfile для основного сервера. Docker-compose файл для запуска контейнера сервера 
вместе с БД и nginx. Организация автоматизированного развертывания бд: sql -> changelog.xml.