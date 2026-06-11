-- =====================================================
-- ОЧИСТКА ТАБЛИЦ (с сохранением порядка FK)
-- =====================================================
DELETE FROM REFERENCE;
DELETE FROM USER_BELONG;
DELETE FROM ACTIVITY;
DELETE FROM TASK;
DELETE FROM SPRINT;
DELETE FROM PROJECT;
DELETE FROM CONTACT;
DELETE FROM PROFILE;
DELETE FROM USER_ROLE;
DELETE FROM USERS;

-- =====================================================
-- СБРОС АВТОИНКРЕМЕНТОВ
-- =====================================================
ALTER TABLE USERS ALTER COLUMN ID RESTART WITH 1;
ALTER TABLE PROJECT ALTER COLUMN ID RESTART WITH 1;
ALTER TABLE SPRINT ALTER COLUMN ID RESTART WITH 1;
ALTER TABLE TASK ALTER COLUMN ID RESTART WITH 1;
ALTER TABLE ACTIVITY ALTER COLUMN ID RESTART WITH 1;
ALTER TABLE USER_BELONG ALTER COLUMN ID RESTART WITH 1;
ALTER TABLE REFERENCE ALTER COLUMN ID RESTART WITH 1;

-- =====================================================
-- ПОЛЬЗОВАТЕЛИ
-- =====================================================
INSERT INTO USERS (EMAIL, PASSWORD, FIRST_NAME, LAST_NAME, DISPLAY_NAME)
VALUES ('user@gmail.com', '{noop}password', 'userFirstName', 'userLastName', 'userDisplayName'),
       ('admin@gmail.com', '{noop}admin', 'adminFirstName', 'adminLastName', 'adminDisplayName'),
       ('guest@gmail.com', '{noop}guest', 'guestFirstName', 'guestLastName', 'guestDisplayName'),
       ('manager@gmail.com', '{noop}manager', 'managerFirstName', 'managerLastName', 'managerDisplayName');

-- =====================================================
-- РОЛИ ПОЛЬЗОВАТЕЛЕЙ
-- =====================================================
INSERT INTO USER_ROLE (USER_ID, ROLE)
VALUES (1, 0),   -- user: DEV
       (2, 0),   -- admin: DEV
       (2, 1),   -- admin: ADMIN
       (4, 2);   -- manager: MANAGER

-- =====================================================
-- ПРОФИЛИ
-- =====================================================
INSERT INTO PROFILE (ID, LAST_FAILED_LOGIN, LAST_LOGIN, MAIL_NOTIFICATIONS)
VALUES (1, NULL, NULL, 49),
       (2, NULL, NULL, 14);

-- =====================================================
-- КОНТАКТЫ
-- =====================================================
INSERT INTO CONTACT (ID, CODE, "VALUE")
VALUES (1, 'skype', 'userSkype'),
       (1, 'mobile', '+01234567890'),
       (1, 'website', 'user.com'),
       (2, 'github', 'adminGitHub'),
       (2, 'tg', 'adminTg'),
       (2, 'vk', 'adminVk');

-- =====================================================
-- ПРОЕКТЫ
-- =====================================================
INSERT INTO PROJECT (CODE, TITLE, DESCRIPTION, TYPE_CODE, PARENT_ID)
VALUES ('PR1', 'PROJECT-1', 'test project 1', 'task_tracker', NULL),
       ('PR2', 'PROJECT-2', 'test project 2', 'task_tracker', 1);

-- =====================================================
-- СПРИНТЫ
-- =====================================================
INSERT INTO SPRINT (STATUS_CODE, STARTPOINT, ENDPOINT, CODE, PROJECT_ID)
VALUES ('finished', '2023-05-01 08:05:10', '2023-05-07 17:10:01', 'SP-1.001', 1),
       ('active', '2023-05-01 08:06:00', NULL, 'SP-1.002', 1),
       ('active', '2023-05-01 08:07:00', NULL, 'SP-1.003', 1),
       ('planning', '2023-05-01 08:08:00', NULL, 'SP-1.004', 1),
       ('active', '2023-05-10 08:06:00', NULL, 'SP-2.001', 2),
       ('planning', '2023-05-10 08:07:00', NULL, 'SP-2.002', 2),
       ('planning', '2023-05-10 08:08:00', NULL, 'SP-2.003', 2);

-- =====================================================
-- ЗАДАЧИ
-- =====================================================
INSERT INTO TASK (TITLE, TYPE_CODE, STATUS_CODE, PROJECT_ID, SPRINT_ID, STARTPOINT)
VALUES ('Data', 'epic', 'in_progress', 1, 1, '2023-05-15 09:05:10'),
       ('Trees', 'epic', 'in_progress', 1, 1, '2023-05-15 12:05:10'),
       ('task-3', 'task', 'ready_for_test', 2, 5, '2023-06-14 09:28:10'),
       ('task-4', 'task', 'ready_for_review', 2, 5, '2023-06-14 09:28:10'),
       ('task-5', 'task', 'todo', 2, 5, '2023-06-14 09:28:10'),
       ('task-6', 'task', 'done', 2, 5, '2023-06-14 09:28:10'),
       ('task-7', 'task', 'canceled', 2, 5, '2023-06-14 09:28:10');

-- =====================================================
-- АКТИВНОСТИ
-- =====================================================
INSERT INTO ACTIVITY (AUTHOR_ID, TASK_ID, UPDATED, COMMENT, TITLE, DESCRIPTION, ESTIMATE, TYPE_CODE, STATUS_CODE, PRIORITY_CODE)
VALUES (1, 1, '2023-05-15 09:05:10', NULL, 'Data', NULL, 3, 'epic', 'in_progress', 'low'),
       (2, 1, '2023-05-15 12:25:10', NULL, 'Data', NULL, NULL, NULL, NULL, 'normal'),
       (1, 1, '2023-05-15 14:05:10', NULL, 'Data', NULL, 4, NULL, NULL, NULL),
       (1, 2, '2023-05-15 12:05:10', NULL, 'Trees', 'Trees desc', 4, 'epic', 'in_progress', 'normal');

-- =====================================================
-- ПРИВЯЗКИ ПОЛЬЗОВАТЕЛЕЙ К ЗАДАЧАМ
-- =====================================================
INSERT INTO USER_BELONG (OBJECT_ID, OBJECT_TYPE, USER_ID, USER_TYPE_CODE, STARTPOINT, ENDPOINT)
VALUES (1, 2, 2, 'task_developer', '2023-06-14 08:35:10', '2023-06-14 08:55:00'),
       (1, 2, 2, 'task_reviewer', '2023-06-14 09:35:10', NULL),
       (1, 2, 1, 'task_developer', '2023-06-12 11:40:00', '2023-06-12 12:35:00'),
       (1, 2, 1, 'task_developer', '2023-06-13 12:35:00', NULL),
       (1, 2, 1, 'task_tester', '2023-06-14 15:20:00', NULL),
       (2, 2, 2, 'task_developer', '2023-06-08 07:10:00', NULL),
       (2, 2, 1, 'task_developer', '2023-06-09 14:48:00', NULL),
       (2, 2, 1, 'task_tester', '2023-06-10 16:37:00', NULL);

-- =====================================================
-- СПРАВОЧНИКИ (REFERENCE) — ВСЕ ТИПЫ
-- =====================================================

-- Типы контактов (REF_TYPE = 0)
INSERT INTO REFERENCE (CODE, TITLE, AUX, REF_TYPE, STARTPOINT, ENDPOINT)
VALUES ('skype', 'Skype', NULL, 0, NULL, NULL),
       ('tg', 'Telegram', NULL, 0, NULL, NULL),
       ('mobile', 'Mobile', NULL, 0, NULL, NULL),
       ('phone', 'Phone', NULL, 0, NULL, NULL),
       ('website', 'Website', NULL, 0, NULL, NULL),
       ('vk', 'VK', NULL, 0, NULL, NULL),
       ('linkedin', 'LinkedIn', NULL, 0, NULL, NULL),
       ('github', 'GitHub', NULL, 0, NULL, NULL);

-- Типы проектов (REF_TYPE = 1)
INSERT INTO REFERENCE (CODE, TITLE, AUX, REF_TYPE, STARTPOINT, ENDPOINT)
VALUES ('scrum', 'Scrum', NULL, 1, NULL, NULL),
       ('task_tracker', 'Task tracker', NULL, 1, NULL, NULL);

-- Типы задач (REF_TYPE = 2)
INSERT INTO REFERENCE (CODE, TITLE, AUX, REF_TYPE, STARTPOINT, ENDPOINT)
VALUES ('task', 'Task', NULL, 2, NULL, NULL),
       ('story', 'Story', NULL, 2, NULL, NULL),
       ('bug', 'Bug', NULL, 2, NULL, NULL),
       ('epic', 'Epic', NULL, 2, NULL, NULL);

-- Статусы задач (REF_TYPE = 3) с правилами переходов и ролями
INSERT INTO REFERENCE (CODE, TITLE, AUX, REF_TYPE, STARTPOINT, ENDPOINT)
VALUES ('todo', 'ToDo', 'in_progress,canceled|', 3, NULL, NULL),
       ('in_progress', 'In progress', 'ready_for_review,canceled|task_developer', 3, NULL, NULL),
       ('ready_for_review', 'Ready for review', 'in_progress,review,canceled|', 3, NULL, NULL),
       ('review', 'Review', 'in_progress,ready_for_test,canceled|task_reviewer', 3, NULL, NULL),
       ('ready_for_test', 'Ready for test', 'review,test,canceled|', 3, NULL, NULL),
       ('test', 'Test', 'done,in_progress,canceled|task_tester', 3, NULL, NULL),
       ('done', 'Done', 'canceled|', 3, NULL, NULL),
       ('canceled', 'Canceled', NULL, 3, NULL, NULL);

-- Статусы спринтов (REF_TYPE = 4)
INSERT INTO REFERENCE (CODE, TITLE, AUX, REF_TYPE, STARTPOINT, ENDPOINT)
VALUES ('planning', 'Planning', NULL, 4, NULL, NULL),
       ('active', 'Active', NULL, 4, NULL, NULL),
       ('finished', 'Finished', NULL, 4, NULL, NULL);

-- Типы участников (REF_TYPE = 5)
INSERT INTO REFERENCE (CODE, TITLE, AUX, REF_TYPE, STARTPOINT, ENDPOINT)
VALUES ('project_author', 'Author', NULL, 5, NULL, NULL),
       ('project_manager', 'Manager', NULL, 5, NULL, NULL),
       ('sprint_author', 'Author', NULL, 5, NULL, NULL),
       ('sprint_manager', 'Manager', NULL, 5, NULL, NULL),
       ('task_author', 'Author', NULL, 5, NULL, NULL),
       ('task_developer', 'Developer', NULL, 5, NULL, NULL),
       ('task_reviewer', 'Reviewer', NULL, 5, NULL, NULL),
       ('task_tester', 'Tester', NULL, 5, NULL, NULL);

-- Типы уведомлений (REF_TYPE = 6)
INSERT INTO REFERENCE (CODE, TITLE, AUX, REF_TYPE, STARTPOINT, ENDPOINT)
VALUES ('assigned', 'Assigned', '1', 6, NULL, NULL),
       ('three_days_before_deadline', 'Three days before deadline', '2', 6, NULL, NULL),
       ('two_days_before_deadline', 'Two days before deadline', '4', 6, NULL, NULL),
       ('one_day_before_deadline', 'One day before deadline', '8', 6, NULL, NULL),
       ('deadline', 'Deadline', '16', 6, NULL, NULL),
       ('overdue', 'Overdue', '32', 6, NULL, NULL);

-- Приоритеты (REF_TYPE = 7)
INSERT INTO REFERENCE (CODE, TITLE, AUX, REF_TYPE, STARTPOINT, ENDPOINT)
VALUES ('critical', 'Critical', NULL, 7, NULL, NULL),
       ('high', 'High', NULL, 7, NULL, NULL),
       ('normal', 'Normal', NULL, 7, NULL, NULL),
       ('low', 'Low', NULL, 7, NULL, NULL),
       ('neutral', 'Neutral', NULL, 7, NULL, NULL);