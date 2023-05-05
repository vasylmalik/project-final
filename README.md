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
  - _UserBelong_ - привязка юзеров с типом (owner, lead, ...) к объекту (таска, проект, спринт, ...). FK вручную будем проверять

## Аналоги
- https://java-source.net/open-source/issue-trackers

## Тестирование
- https://habr.com/ru/articles/259055/

## Список выполненных задач:
1. Разобраться со структурой проекта (onboarding).
2. Удалены социальные сети: vk, yandex
3. Вынесена чувствительная информация (логин, пароль БД, идентификаторы для OAuth регистрации/авторизации, настройки 
почты) в отдельный проперти файл.
4. Тесты запускаются in memory БД (H2), а не PostgreSQL. Но без участия Liquibase.
5. Написаны тесты для всех публичных методов контроллера ProfileRestController.
6. Добавлен функционал: добавления тегов к задаче.
7. Добавлена возможность подписываться на задачи, которые не назначены на текущего пользователя. (Изначально делал так, 
как я это видел – в таблице user_belong есть записи отношений пользователей к типам задач. В реализации я получаю 
список задач, которые не назначены на текущего пользователя, выбираю задачу на которую хочу подписаться и 
подписываюсь. В результате в табл. user_belong создаётся новая запись, в которой тип пользователя указан subscriber.
Для этого, данным полем c типом пользователя, был расширен enum Role. На одной из последних лекций был разобран, 
как этот механизм работает в Джире, правильная реализация этой фичи взята на карандаш :)
9. Написан Dockerfile для основного сервера.
10. Написан docker-compose файл для запуска контейнера сервера вместе с БД и nginx. Объединил их всех в общую сеть, 
порты для бд и сервера не видны извне. Вся работа с приложением ведётся через nginx. Почему-то не работает 
переадресация запросов через nginx только в браузере Chrome. 
11. Добавлена локализация на двух языках (Ukrainian, English) для шаблонов писем и стартовой страницы index.html, 
header.html. В хедер добавлены кнопки по смене языка.

