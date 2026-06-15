--------- Очищення таблиць перед кожним тестом ----------------------
TRUNCATE TABLE user_role, contact, profile, activity, task, sprint, project, users, user_belong, reference RESTART IDENTITY CASCADE;

--------- Користувачі та Ролі ----------------------
insert into USERS (EMAIL, PASSWORD, FIRST_NAME, LAST_NAME, DISPLAY_NAME)
values ('user@gmail.com', '{noop}password', 'userFirstName', 'userLastName', 'userDisplayName'),
       ('admin@gmail.com', '{noop}admin', 'adminFirstName', 'adminLastName', 'adminDisplayName'),
       ('guest@gmail.com', '{noop}guest', 'guestFirstName', 'guestLastName', 'guestDisplayName'),
       ('manager@gmail.com', '{noop}manager', 'managerFirstName', 'managerLastName', 'managerDisplayName');

-- 0 DEV, 1 ADMIN, 2 MANAGER
insert into USER_ROLE (USER_ID, ROLE)
values (1, 0),
       (2, 0),
       (2, 1),
       (4, 2);

insert into PROFILE (ID, LAST_FAILED_LOGIN, LAST_LOGIN, MAIL_NOTIFICATIONS)
values (1, null, null, 0),   -- 49
       (2, null, null, 0);  --14

insert into CONTACT (ID, CODE, VALUE)
values (1, 'skype', 'userSkype'),
       (1, 'mobile', '+01234567890'),
       (1, 'website', 'user.com'),
       (2, 'github', 'adminGitHub'),
       (2, 'tg', 'adminTg');

--------- Довідники (REFERENCE) ----------------------
-- Важливо: поля мають збігатися з Hibernate-мапінгом. id генерується автоматично.
insert into reference (id, ref_type, code, title, endpoint, startpoint, aux)
values
    -- CONTACT (ref_type = 0)
    (1, 0, 'skype', 'Skype', null, null, null),
    (2, 0, 'mobile', 'Mobile', null, null, null),
    (3, 0, 'website', 'Website', null, null, null),
    (4, 0, 'github', 'GitHub', null, null, null),
    (5, 0, 'tg', 'Telegram', null, null, null),

    -- TASK (ref_type = 2)
    (6, 2, 'task', 'Task', null, null, null),
    (7, 2, 'bug', 'Bug', null, null, null),
    (8, 2, 'epic', 'Epic', null, null, null),

    -- TASK_STATUS (ref_type = 3)
    -- Поля aux містять чисті рядки ролей, які одразу зчитає getAux()
    (9, 3, 'todo', 'To Do', null, null, 'task_developer'),
    (10, 3, 'in_progress', 'In Progress', null, null, 'task_developer'),
    (11, 3, 'ready_for_review', 'Ready for Review', null, null, 'task_reviewer'),
    (12, 3, 'ready_for_test', 'Ready for Test', null, null, 'task_tester'),
    (13, 3, 'done', 'Done', null, null, null),
    (14, 3, 'canceled', 'Canceled', null, null, null),

    -- SPRINT_STATUS (ref_type = 4)
    (15, 4, 'planning', 'Planning', null, null, null),
    (16, 4, 'active', 'Active', null, null, null),
    (17, 4, 'finished', 'Finished', null, null, null),

    -- USER_TYPE (ref_type = 5)
    (18, 5, 'task_developer', 'Developer', null, null, null),
    (19, 5, 'task_reviewer', 'Reviewer', null, null, null),
    (20, 5, 'task_tester', 'Tester', null, null, null),

    -- PROJECT_TYPE (PROJECT = 1)
    (21, 1, 'task_tracker', 'Task Tracker', null, null, null),

    -- MAIL_NOTIFICATION (ref_type = 6) -> ВЕРХНІЙ РЕГІСТР
    (22, 6, 'ONE_DAY_BEFORE_DEADLINE', 'Deadline 1 day', null, null, null),
    (23, 6, 'TWO_DAYS_BEFORE_DEADLINE', 'Deadline 2 days', null, null, null),
    (24, 6, 'THREE_DAYS_BEFORE_DEADLINE', 'Deadline 3 days', null, null, null),
    (25, 6, 'ASSIGNED', 'Assigned', null, null, null),
    (26, 6, 'DEADLINE_CHANGE', 'Deadline change', null, null, null),
    (27, 6, 'STATUS_CHANGE', 'Status change', null, null, null),

    -- PRIORITY (ref_type = 7) -> ВЕРХНІЙ РЕГІСТР
    (28, 7, 'LOW', 'Low', null, null, null),
    (29, 7, 'NORMAL', 'Normal', null, null, null),
    (30, 7, 'HIGH', 'High', null, null, null),
    (31, 7, 'CRITICAL', 'Critical', null, null, null);

--------- Проєкти та Спринти ----------------------
insert into PROJECT (code, title, description, type_code, parent_id)
values ('PR1', 'PROJECT-1', 'test project 1', 'task_tracker', null),
       ('PR2', 'PROJECT-2', 'test project 2', 'task_tracker', 1);

insert into SPRINT (status_code, startpoint, endpoint, code, project_id)
values ('finished', '2023-05-01 08:05:10', '2023-05-07 17:10:01', 'SP-1.001', 1),
       ('active', '2023-05-01 08:06:00', null, 'SP-1.002', 1),
       ('active', '2023-05-01 08:07:00', null, 'SP-1.003', 1),
       ('planning', '2023-05-01 08:08:00', null, 'SP-1.004', 1),
       ('active', '2023-05-10 08:06:00', null, 'SP-2.001', 2),
       ('planning', '2023-05-10 08:07:00', null, 'SP-2.002', 2),
       ('planning', '2023-05-10 08:08:00', null, 'SP-2.003', 2);

--------- Завдання (TASK) ----------------------
insert into TASK (TITLE, TYPE_CODE, STATUS_CODE, PROJECT_ID, SPRINT_ID, STARTPOINT)
values ('Data', 'epic', 'in_progress', 1, 1, '2023-05-15 09:05:10'),
       ('Trees', 'epic', 'in_progress', 1, 1, '2023-05-15 12:05:10'),
       ('task-3', 'task', 'ready_for_test', 2, 5, '2023-06-14 09:28:10'),
       ('task-4', 'task', 'ready_for_review', 2, 5, '2023-06-14 09:28:10'),
       ('task-5', 'task', 'todo', 2, 5, '2023-06-14 09:28:10'),
       ('task-6', 'task', 'done', 2, 5, '2023-06-14 09:28:10'),
       ('task-7', 'task', 'canceled', 2, 5, '2023-06-14 09:28:10');

--------- Активності та Логи ----------------------
insert into ACTIVITY(AUTHOR_ID, TASK_ID, UPDATED, COMMENT, TITLE, DESCRIPTION, ESTIMATE, TYPE_CODE, STATUS_CODE, PRIORITY_CODE)
values (1, 1, '2023-05-15 09:05:10', null, 'Data', null, 3, 'epic', 'in_progress', 'low'),
       (2, 1, '2023-05-15 12:25:10', null, 'Data', null, null, null, 'in_progress', 'normal'), -- ВИПРАВЛЕНО
       (1, 1, '2023-05-15 14:05:10', null, 'Data', null, 4, null, null, null),
       (1, 2, '2023-05-15 12:05:10', null, 'Trees', 'Trees desc', 4, 'epic', 'in_progress', 'normal');

--------- Зв'язки Користувачів із Завданнями (USER_BELONG) ----------------------
insert into USER_BELONG (OBJECT_ID, OBJECT_TYPE, USER_ID, USER_TYPE_CODE, STARTPOINT, ENDPOINT)
values (1, 2, 2, 'task_developer', '2023-06-14 08:35:10', null), -- ЗАЛИШАЄМО NULL (активний розробник для успішного unassign)
       (1, 2, 2, 'task_reviewer', '2023-06-14 09:35:10', null),
       (1, 2, 1, 'task_developer', '2023-06-12 11:40:00', '2023-06-12 12:35:00'),
       (1, 2, 1, 'task_developer', '2023-06-13 12:35:00', null),
       (1, 2, 1, 'task_tester', '2023-06-14 15:20:00', null),
       (2, 2, 2, 'task_developer', '2023-06-08 07:10:00', null),
       (2, 2, 1, 'task_developer', '2023-06-09 14:48:00', null),
       (2, 2, 1, 'task_tester', '2023-06-10 16:37:00', null);
