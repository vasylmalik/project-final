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

Список выполненных задач:
- 1) изучение проекта - всегда можно разобраться еще чуть глубже=)
- 2) удаление соц.сетей: ломать не строить)
- 3) креденшиалы вынесены в переменные окружения. 
- 4) перенести тесты с Постгреса на Н2, самым читерским вариантом) зато VALUE не пришлось в скриптах исправлять=)
- 5) протестирован ProfileRestController
- 6) добавлена возможность добавлять теги к задачам, без реализации фронтенда, протестирован через Swagger
...