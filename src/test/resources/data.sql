-- Отключаем проверку внешних ключей на время очистки базы в H2
SET REFERENTIAL_INTEGRITY FALSE;

---------  Очистка данных ----------------------
DELETE FROM USERS;
DELETE FROM USER_ROLE;
DELETE FROM PROFILE;
DELETE FROM CONTACT;
DELETE FROM ATTACHMENT;
DELETE FROM TASK;
DELETE FROM SPRINT;
DELETE FROM PROJECT;
DELETE FROM ACTIVITY;

-- Исправлен синтаксис (все alter sequence теперь записаны в одну строку)
-- ALTER SEQUENCE USERS_ID_SEQ RESTART WITH 1;
-- ALTER SEQUENCE ATTACHMENT_ID_SEQ RESTART WITH 1;
-- ALTER SEQUENCE TASK_ID_SEQ RESTART WITH 1;
-- ALTER SEQUENCE SPRINT_ID_SEQ RESTART WITH 1;
-- ALTER SEQUENCE PROJECT_ID_SEQ RESTART WITH 1;
-- ALTER SEQUENCE ACTIVITY_ID_SEQ RESTART WITH 1;

-- Включаем проверки обратно
SET REFERENTIAL_INTEGRITY TRUE;

---------  Заполнение данных ----------------------

-- populate users
INSERT INTO USERS (EMAIL, PASSWORD, FIRST_NAME, LAST_NAME, DISPLAY_NAME, STARTPOINT) VALUES ('user@gmail.com', '{noop}password', 'userFirstName', 'userLastName', 'userDisplayName', CURRENT_TIMESTAMP);
INSERT INTO USERS (EMAIL, PASSWORD, FIRST_NAME, LAST_NAME, DISPLAY_NAME, STARTPOINT) VALUES ('admin@gmail.com', '{noop}admin', 'adminFirstName', 'adminLastName', 'adminDisplayName', CURRENT_TIMESTAMP);
INSERT INTO USERS (EMAIL, PASSWORD, FIRST_NAME, LAST_NAME, DISPLAY_NAME, STARTPOINT) VALUES ('guest@gmail.com', '{noop}guest', 'guestFirstName', 'guestLastName', 'guestDisplayName', CURRENT_TIMESTAMP);
INSERT INTO USERS (EMAIL, PASSWORD, FIRST_NAME, LAST_NAME, DISPLAY_NAME, STARTPOINT) VALUES ('manager@gmail.com', '{noop}manager', 'managerFirstName', 'managerLastName', 'managerDisplayName', CURRENT_TIMESTAMP);
INSERT INTO USERS (EMAIL, PASSWORD, FIRST_NAME, LAST_NAME, DISPLAY_NAME, STARTPOINT) VALUES ('taras@gmail.com', '{noop}password', 'Тарас', 'Шевченко', '@taras', CURRENT_TIMESTAMP);
INSERT INTO USERS (EMAIL, PASSWORD, FIRST_NAME, LAST_NAME, DISPLAY_NAME, STARTPOINT) VALUES ('petlura@gmail.com', '{noop}password', 'Симон', 'Петлюра', '@epetl', CURRENT_TIMESTAMP);
INSERT INTO USERS (EMAIL, PASSWORD, FIRST_NAME, LAST_NAME, DISPLAY_NAME, STARTPOINT) VALUES ('moroz_a@gmail.com', '{noop}password', 'Александр', 'Мороз', '@Moroz93', CURRENT_TIMESTAMP);
INSERT INTO USERS (EMAIL, PASSWORD, FIRST_NAME, LAST_NAME, DISPLAY_NAME, STARTPOINT) VALUES ('antonio.nest@gmail.com', '{noop}password', 'Антон', 'Нестеров', '@antonio_nest', CURRENT_TIMESTAMP);
INSERT INTO USERS (EMAIL, PASSWORD, FIRST_NAME, LAST_NAME, DISPLAY_NAME, STARTPOINT) VALUES ('i.franko@gmail.com', '{noop}password', 'Иван', 'Франко', '@ifranko', CURRENT_TIMESTAMP);
INSERT INTO USERS (EMAIL, PASSWORD, FIRST_NAME, LAST_NAME, DISPLAY_NAME, STARTPOINT) VALUES ('g.skovoroda@gmail.com', '{noop}password', 'Григорий', 'Сковорода', '@Gregory24', CURRENT_TIMESTAMP);
INSERT INTO USERS (EMAIL, PASSWORD, FIRST_NAME, LAST_NAME, DISPLAY_NAME, STARTPOINT) VALUES ('arsh.and@gmail.com', '{noop}password', 'Андрей', 'Арш', '@arsh01', CURRENT_TIMESTAMP);
INSERT INTO USERS (EMAIL, PASSWORD, FIRST_NAME, LAST_NAME, DISPLAY_NAME, STARTPOINT) VALUES ('squirrel2011@gmail.com', '{noop}password', 'Леся', 'Иванюк', '@SmallSquirrel', CURRENT_TIMESTAMP);
INSERT INTO USERS (EMAIL, PASSWORD, FIRST_NAME, LAST_NAME, DISPLAY_NAME, STARTPOINT) VALUES ('nikk24@gmail.com', '{noop}password', 'Николай', 'Никулин', '@nikk', CURRENT_TIMESTAMP);
INSERT INTO USERS (EMAIL, PASSWORD, FIRST_NAME, LAST_NAME, DISPLAY_NAME, STARTPOINT) VALUES ('artem711@gmail.com', '{noop}password', 'Артем', 'Запорожец', '@Artt', CURRENT_TIMESTAMP);
INSERT INTO USERS (EMAIL, PASSWORD, FIRST_NAME, LAST_NAME, DISPLAY_NAME, STARTPOINT) VALUES ('max.pain@gmail.com', '{noop}password', 'Максим', 'Дудник', '@MaxPain', CURRENT_TIMESTAMP);
INSERT INTO USERS (EMAIL, PASSWORD, FIRST_NAME, LAST_NAME, DISPLAY_NAME, STARTPOINT) VALUES ('admin@aws.co', '{noop}password', 'test', 'admin', '@testAdmin', CURRENT_TIMESTAMP);

-- 0 DEV, 1 ADMIN, 2 MANAGER
-- Экранируем зарезервированные слова кавычками
INSERT INTO USER_ROLE (USER_ID, ROLE)
VALUES (1, 0), (2, 0), (2, 1), (4, 2), (5, 0), (6, 0), (7, 0), (8, 0),
       (9, 0), (10, 0), (11, 0), (12, 0), (13, 0), (14, 0), (15, 1), (16, 1);

INSERT INTO PROFILE (ID, LAST_FAILED_LOGIN, LAST_LOGIN, MAIL_NOTIFICATIONS)
VALUES (1, null, null, 49),
       (2, null, null, 14);

INSERT INTO CONTACT (ID, CODE, VALUE)
VALUES (1, 'skype', 'userSkype'),
       (1, 'mobile', '+01234567890'),
       (1, 'website', 'user.com'),
       (2, 'github', 'adminGitHub'),
       (2, 'tg', 'adminTg'),
       (2, 'vk', 'adminVk');

INSERT INTO ATTACHMENT (name, file_link, object_id, object_type, user_id, date_time)
VALUES ('Снимок экрана 1.png', './attachments/project/1_Снимок экрана 1.png', 2, 0, 4, '2023-05-04 22:28:50.215429'),
       ('Снимок экрана 2.png', './attachments/project/2_Снимок экрана 2.png', 2, 0, 4, '2023-05-04 22:28:53.687600'),
       ('Ежедневный-чеклист.xlsx', './attachments/project/3_Ежедневный-чеклист.xlsx', 2, 0, 4, '2023-05-04 22:31:15.166547'),
       ('Снимок экрана 1.png', './attachments/task/1_Снимок экрана 1.png', 41, 2, 4, '2023-05-04 22:28:53.687600'),
       ('Снимок экрана 2.png', './attachments/task/2_Снимок экрана 2.png', 41, 2, 4, '2023-05-04 22:28:50.215429'),
       ('Ежедневный-чеклист.xlsx', './attachments/task/3_Ежедневный-чеклист.xlsx', 38, 2, 4, '2023-05-04 22:28:50.215429');

-- ALTER SEQUENCE ATTACHMENT_ID_SEQ RESTART WITH 1000;

INSERT INTO PROJECT (code, title, description, type_code, parent_id, STARTPOINT)
VALUES ('JiraRush', 'JiraRush', '«Mini-JIRA» app : project management system tutorial app', 'task_tracker', null, CURRENT_TIMESTAMP),
       ('Test_Project', 'Test Project', 'Just test project', 'task_tracker', null, CURRENT_TIMESTAMP),
       ('Test_Project_2', 'Test Project 2', 'Just test project 2', 'task_tracker', null, CURRENT_TIMESTAMP),
       ('JiraRush sub', 'JiraRush subproject', 'subproject', 'task_tracker', 1, CURRENT_TIMESTAMP);

-- ALTER SEQUENCE PROJECT_ID_SEQ RESTART WITH 1000;

INSERT INTO SPRINT (status_code, startpoint, endpoint, code, project_id)
VALUES ('active', CURRENT_TIMESTAMP, null, 'Sprint-2', 1), -- заменили null на CURRENT_TIMESTAMP
       ('finished', '2023-04-09 08:05:10', '2023-04-29 16:48:34', 'Sprint-1', 2),
       ('finished', '2023-04-03 12:14:11', '2023-04-18 17:03:41', 'Sprint-2', 2),
       ('active', '2023-04-05 14:25:43', '2023-06-10 13:00:00', 'Sprint-3', 2),
       ('active', CURRENT_TIMESTAMP, null, 'Sprint-1', 4); -- заменили null на CURRENT_TIMESTAMP

-- ALTER SEQUENCE SPRINT_ID_SEQ RESTART WITH 1000;

---- Упрощенный блок генерации времени (now() + random() заменен на CURRENT_TIMESTAMP) -------------
INSERT INTO TASK (TITLE, TYPE_CODE, STATUS_CODE, PROJECT_ID, SPRINT_ID, STARTPOINT)
VALUES ('Data', 'epic', 'in_progress', 1, 1, CURRENT_TIMESTAMP),
       ('Trees', 'epic', 'in_progress', 1, 1, CURRENT_TIMESTAMP),
       ('UI', 'epic', 'in_progress', 1, 1, CURRENT_TIMESTAMP),
       ('Sprint', 'epic', 'in_progress', 1, 1, CURRENT_TIMESTAMP),
       ('Project', 'epic', 'in_progress', 1, 1, CURRENT_TIMESTAMP),
       ('Task', 'epic', 'in_progress', 1, 1, CURRENT_TIMESTAMP),
       ('Attachments', 'story', 'in_progress', 1, 1, CURRENT_TIMESTAMP),
       ('Dashboard', 'epic', 'in_progress', 1, 1, CURRENT_TIMESTAMP),
       ('Report of Sprint (UI)', 'story', 'in_progress', 1, 1, CURRENT_TIMESTAMP),
       ('Organizational-architectural', 'epic', 'in_progress', 1, 1, CURRENT_TIMESTAMP),
       ('Title', 'task', 'todo', 2, 2, CURRENT_TIMESTAMP),
       ('Title', 'task', 'todo', 2, 2, CURRENT_TIMESTAMP),
       ('Title', 'task', 'in_progress', 2, 2, CURRENT_TIMESTAMP),
       ('Title', 'task', 'in_progress', 2, 2, CURRENT_TIMESTAMP),
       ('Title', 'task', 'in_progress', 2, 2, CURRENT_TIMESTAMP),
       ('Title', 'task', 'test', 2, 2, CURRENT_TIMESTAMP),
       ('Title', 'task', 'done', 2, 2, CURRENT_TIMESTAMP),
       ('Title', 'task', 'done', 2, 2, CURRENT_TIMESTAMP),
       ('Title', 'task', 'canceled', 2, 2, CURRENT_TIMESTAMP),
       ('Title', 'task', 'ready_for_test', 2, 2, CURRENT_TIMESTAMP),
       ('Title', 'task', 'in_progress', 2, 3, CURRENT_TIMESTAMP),
       ('Title', 'task', 'in_progress', 2, 3, CURRENT_TIMESTAMP),
       ('Title', 'task', 'in_progress', 2, 3, CURRENT_TIMESTAMP),
       ('Title', 'task', 'test', 2, 3, CURRENT_TIMESTAMP),
       ('Title', 'task', 'test', 2, 3, CURRENT_TIMESTAMP),
       ('Title', 'task', 'test', 2, 3, CURRENT_TIMESTAMP),
       ('Title', 'task', 'done', 2, 3, CURRENT_TIMESTAMP),
       ('Title', 'task', 'done', 2, 3, CURRENT_TIMESTAMP);
