-- =========================================================================================
-- КРОК 1: Одноразове, безпечне каскадне очищення ВСІХ таблиць та скидання лічильників ID
-- =========================================================================================
TRUNCATE TABLE
    user_role, contact, profile, activity, task, sprint,
    project, users, user_belong, attachment, mail_case
RESTART IDENTITY CASCADE;

-- =========================================================================================
-- КРОК 2: Наповнення таблиць свіжими даними (без внутрішніх DELETE / ALTER)
-- =========================================================================================

-- populate users
insert into USERS (EMAIL, PASSWORD, FIRST_NAME, LAST_NAME, DISPLAY_NAME)
values ('user@gmail.com', '{noop}password', 'userFirstName', 'userLastName', 'userDisplayName'),
       ('admin@gmail.com', '{noop}admin', 'adminFirstName', 'adminLastName', 'adminDisplayName'),
       ('guest@gmail.com', '{noop}guest', 'guestFirstName', 'guestLastName', 'guestDisplayName'),
       ('manager@gmail.com', '{noop}manager', 'managerFirstName', 'managerLastName', 'managerDisplayName'),
       ('taras@gmail.com', '{noop}password', 'Тарас', 'Шевченко', '@taras'),
       ('petlura@gmail.com', '{noop}password', 'Симон', 'Петлюра', '@epetl'),
       ('moroz_a@gmail.com', '{noop}password', 'Александр', 'Мороз', '@Moroz93'),
       ('antonio.nest@gmail.com', '{noop}password', 'Антон', 'Нестеров', '@antonio_nest'),
       ('i.franko@gmail.com', '{noop}password', 'Иван', 'Франко', '@ifranko'),
       ('g.skovoroda@gmail.com', '{noop}password', 'Григорий', 'Сковорода', '@Gregory24'),
       ('arsh.and@gmail.com', '{noop}password', 'Андрей', 'Арш', '@arsh01'),
       ('squirrel2011@gmail.com', '{noop}password', 'Леся', 'Иванюк', '@SmallSquirrel'),
       ('nikk24@gmail.com', '{noop}password', 'Николай', 'Никулин', '@nikk'),
       ('artem711@gmail.com', '{noop}password', 'Артем', 'Запорожец', '@Artt'),
       ('max.pain@gmail.com', '{noop}password', 'Максим', 'Дудник', '@MaxPain'),
       ('admin@aws.co', '{noop}password', 'test', 'admin', '@testAdmin');

-- 0 DEV
-- 1 ADMIN
-- 2 MANAGER
insert into USER_ROLE (USER_ID, ROLE)
values (1, 0),
       (2, 0),
       (2, 1),
       (4, 2),
       (5, 0),
       (6, 0),
       (7, 0),
       (8, 0),
       (9, 0),
       (10, 0),
       (11, 0),
       (12, 0),
       (13, 0),
       (14, 0),
       (15, 1),
       (16, 1);

insert into PROFILE (ID, LAST_FAILED_LOGIN, LAST_LOGIN, MAIL_NOTIFICATIONS)
values (1, null, null, 49),
       (2, null, null, 14);

insert into CONTACT (ID, CODE, VALUE)
values (1, 'skype', 'userSkype'),
       (1, 'mobile', '+01234567890'),
       (1, 'website', 'user.com'),
       (2, 'github', 'adminGitHub'),
       (2, 'tg', 'adminTg'),
       (2, 'vk', 'adminVk');

insert into ATTACHMENT (name, file_link, object_id, object_type, user_id, date_time)
values ('Снимок экрана 1.png', './attachments/project/1_Снимок экрана 1.png', 2, 0, 4, '2023-05-04 22:28:50.215429'),
       ('Снимок экрана 2.png', './attachments/project/2_Снимок экрана 2.png', 2, 0, 4, '2023-05-04 22:28:53.687600'),
       ('Ежедневный-чеклист.xlsx', './attachments/project/3_Ежедневный-чеклист.xlsx', 2, 0, 4, '2023-05-04 22:31:15.166547'),
       ('Снимок экрана 1.png', './attachments/task/1_Снимок экрана 1.png', 41, 2, 4, '2023-05-04 22:28:53.687600'),
       ('Снимок экрана 2.png', './attachments/task/2_Снимок экрана 2.png', 41, 2, 4, '2023-05-04 22:28:50.215429'),
       ('Ежедневный-чеклист.xlsx', './attachments/task/3_Ежедневный-чеклист.xlsx', 38, 2, 4, '2023-05-04 22:28:50.215429');

-- Встановлюємо стартове значення для нових автозгенерованих вкладень, як було задумано
alter sequence ATTACHMENT_ID_SEQ restart with 1000;

insert into PROJECT (code, title, description, type_code, parent_id)
values ('JiraRush', 'JiraRush', '«Mini-JIRA» app : project management system tutorial app', 'task_tracker', null),
       ('Test_Project', 'Test Project', 'Just test project', 'task_tracker', null),
       ('Test_Project_2', 'Test Project 2', 'Just test project 2', 'task_tracker', null),
       ('JiraRush sub', 'JiraRush subproject', 'subproject', 'task_tracker', 1);

alter sequence PROJECT_ID_SEQ restart with 1000;

insert into SPRINT (status_code, startpoint, endpoint, code, project_id)
values ('active', null, null, 'Sprint-2', 1),
       ('finished', '2023-04-09 08:05:10', '2023-04-29 16:48:34', 'Sprint-1', 2),
       ('finished', '2023-04-03 12:14:11', '2023-04-18 17:03:41', 'Sprint-2', 2),
       ('active', '2023-04-05 14:25:43', '2023-06-10 13:00:00', 'Sprint-3', 2),
       ('active', null, null, 'Sprint-1', 4);

alter sequence SPRINT_ID_SEQ restart with 1000;

---- project 1 -------------
INSERT INTO TASK (TITLE, TYPE_CODE, STATUS_CODE, PROJECT_ID, SPRINT_ID, STARTPOINT)
values ('Data', 'epic', 'in_progress', 1, 1, now()),
       ('Trees', 'epic', 'in_progress', 1, 1, now()),
       ('UI', 'epic', 'in_progress', 1, 1, now()),
       ('Sprint', 'epic', 'in_progress', 1, 1, now()),
       ('Project', 'epic', 'in_progress', 1, 1, now()),
       ('Task', 'epic', 'in_progress', 1, 1, now()),
       ('Attachments', 'story', 'in_progress', 1, 1, now()),
       ('Dashboard', 'epic', 'in_progress', 1, 1, now()),
       ('Report of Sprint (UI)', 'story', 'in_progress', 1, 1, now()),
       ('Organizational-architectural', 'epic', 'in_progress', 1, 1, now()),
---- project 2 -------------
       ('Title', 'task', 'todo', 2, 2, now()),
       ('Title', 'task', 'todo', 2, 2, now()),
       ('Title', 'task', 'in_progress', 2, 2, now()),
       ('Title', 'task', 'in_progress', 2, 2, now()),
       ('Title', 'task', 'in_progress', 2, 2, now()),
       ('Title', 'task', 'test', 2, 2, now()),
       ('Title', 'task', 'done', 2, 2, now()),
       ('Title', 'task', 'done', 2, 2, now()),
       ('Title', 'task', 'canceled', 2, 2, now()),
       ('Title', 'task', 'ready_for_test', 2, 2, now()),
       ('Title', 'task', 'in_progress', 2, 3, now()),
       ('Title', 'task', 'in_progress', 2, 3, now()),
       ('Title', 'task', 'in_progress', 2, 3, now()),
       ('Title', 'task', 'test', 2, 3, now()),
       ('Title', 'task', 'test', 2, 3, now()),
       ('Title', 'task', 'test', 2, 3, now()),
       ('Title', 'task', 'done', 2, 3, now()),
       ('Title', 'task', 'done', 2, 3, now());