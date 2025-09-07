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
- Видалити соціальні мережі: vk, yandex;
- Винести чутливу інформацію до окремого проперті файлу;
- Написати тести для всіх публічних методів контролера ProfileRestController;
- Зробити рефакторинг методу com.javarush.jira.bugtracking.attachment.FileUtil#upload, щоб він використовував сучасний підхід для роботи з файловою системою;
- Додати новий функціонал: додавання тегів до завдання (REST API + реалізація на сервісі);
- Додати підрахунок часу: скільки завдання перебувало у роботі та тестуванні;
- Написати Dockerfile для основного сервера;
- Додати локалізацію мінімум двома мовами для шаблонів листів (mails) та стартовою сторінки index.html.