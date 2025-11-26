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

# JiraRush Project: My Contributions & Enhancements

This document outlines my key contributions and improvements to the JiraRush project, covering aspects of security, testing, refactoring, new features, and deployment.

## My Contributions to the Project:

Here's a breakdown of the specific tasks and features I implemented:

1.  📘 **Project Structure Onboarding:** I successfully familiarized myself with the existing project structure and architecture, enabling effective subsequent modifications and new feature development.
2.  🚫 **Removal of Deprecated Social Integrations:** I removed outdated social media integrations (VK, Yandex) from the project, simplifying the codebase and mitigating potential security risks.
3.  🔑 **Externalization of Sensitive Information:** I moved sensitive data, such as database login credentials, OAuth registration/authorization identifiers, and email settings, into separate property files. These values are now securely loaded from machine environment variables upon server startup, significantly enhancing security and configuration flexibility.
4.  🧪 **Refactoring Tests for In-Memory H2 Database:** I adapted the test suite to utilize an in-memory H2 database instead of PostgreSQL. This involved defining two distinct Spring beans, with the active Spring profile dictating which database to use. Minor adjustments were also made to test data scripts to ensure compatibility with H2's features.
5.  ✅ **Comprehensive `ProfileRestController` Test Coverage:** I developed a robust set of unit and integration tests for all public methods of the `ProfileRestController`. These tests meticulously validate both successful and various unsuccessful execution paths to guarantee the API's reliability and resilience.
6.  🔄 **Refactoring `FileUtil#upload` for Modern File System API:** I refactored the `com.javarush.jira.bugtracking.attachment.FileUtil#upload` method to leverage modern Java file system APIs, enhancing its efficiency, robustness, and maintainability for file operations.
7.  🏷️ **Implementation of Task Tagging System:** I introduced a new feature allowing users to add tags to tasks. This includes developing the dedicated REST API endpoint and implementing the corresponding service-layer logic, utilizing the existing `task_tag` database table.
8.  ⏱️ **Adding Task Time Tracking Functionality:** I implemented two service-level methods to calculate the time a task spends in specific operational states:
  *   **Time in Work:** Calculated as the duration from `in_progress` to `ready_for_review` status.
  *   **Time in Testing:** Calculated as the duration from `ready_for_review` to `done` status.
      To support this, I appended three critical `ACTIVITY` entries (with `in_progress`, `ready_for_review`, and `done` statuses) to the `changelog.sql` database initialization script.
9.  🐳 **Dockerfile for Application Server:** I created a `Dockerfile` to containerize the main application server, ensuring easy and consistent deployment across different environments.
10. 🚀 **Docker Compose for Full Stack Deployment:** I developed a `docker-compose.yml` file to orchestrate the entire application stack. This setup includes the application server, a PostgreSQL database, and an Nginx reverse proxy, leveraging the `config/nginx.conf` file (which can be modified as needed) for efficient routing and load balancing.

---