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

ALTER TABLE USERS ALTER COLUMN ID RESTART WITH 1;
ALTER TABLE PROJECT ALTER COLUMN ID RESTART WITH 1;
ALTER TABLE SPRINT ALTER COLUMN ID RESTART WITH 1;
ALTER TABLE TASK ALTER COLUMN ID RESTART WITH 1;
ALTER TABLE ACTIVITY ALTER COLUMN ID RESTART WITH 1;
ALTER TABLE USER_BELONG ALTER COLUMN ID RESTART WITH 1;
ALTER TABLE REFERENCE ALTER COLUMN ID RESTART WITH 1;

insert into USERS (EMAIL, PASSWORD, FIRST_NAME, LAST_NAME, DISPLAY_NAME)
values ('user@gmail.com', '{noop}password', 'userFirstName', 'userLastName', 'userDisplayName'),
       ('admin@gmail.com', '{noop}admin', 'adminFirstName', 'adminLastName', 'adminDisplayName'),
       ('guest@gmail.com', '{noop}guest', 'guestFirstName', 'guestLastName', 'guestDisplayName'),
       ('manager@gmail.com', '{noop}manager', 'managerFirstName', 'managerLastName', 'managerDisplayName');

-- 0 DEV
-- 1 ADMIN
-- 2 MANAGER

insert into USER_ROLE (USER_ID, ROLE)
values (1, 0),
       (2, 0),
       (2, 1),
       (4, 2);

insert into PROFILE (ID, LAST_FAILED_LOGIN, LAST_LOGIN, MAIL_NOTIFICATIONS)
values (1, null, null, 49),
       (2, null, null, 14);

insert into CONTACT (ID, CODE, "VALUE")
values (1, 'skype', 'userSkype'),
       (1, 'mobile', '+01234567890'),
       (1, 'website', 'user.com'),
       (2, 'github', 'adminGitHub'),
       (2, 'tg', 'adminTg'),
       (2, 'vk', 'adminVk');


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

insert into TASK (TITLE, TYPE_CODE, STATUS_CODE, PROJECT_ID, SPRINT_ID, STARTPOINT)
values ('Data', 'epic', 'in_progress', 1, 1, '2023-05-15 09:05:10'),
       ('Trees', 'epic', 'in_progress', 1, 1, '2023-05-15 12:05:10'),
       ('task-3', 'task', 'ready_for_test', 2, 5, '2023-06-14 09:28:10'),
       ('task-4', 'task', 'ready_for_review', 2, 5, '2023-06-14 09:28:10'),
       ('task-5', 'task', 'todo', 2, 5, '2023-06-14 09:28:10'),
       ('task-6', 'task', 'done', 2, 5, '2023-06-14 09:28:10'),
       ('task-7', 'task', 'canceled', 2, 5, '2023-06-14 09:28:10');


insert into ACTIVITY(AUTHOR_ID, TASK_ID, UPDATED, COMMENT, TITLE, DESCRIPTION, ESTIMATE, TYPE_CODE, STATUS_CODE,
                     PRIORITY_CODE)
values (1, 1, '2023-05-15 09:05:10', null, 'Data', null, 3, 'epic', 'in_progress', 'low'),
       (2, 1, '2023-05-15 12:25:10', null, 'Data', null, null, null, null, 'normal'),
       (1, 1, '2023-05-15 14:05:10', null, 'Data', null, 4, null, null, null),
       (1, 2, '2023-05-15 12:05:10', null, 'Trees', 'Trees desc', 4, 'epic', 'in_progress', 'normal');

insert into USER_BELONG (OBJECT_ID, OBJECT_TYPE, USER_ID, USER_TYPE_CODE, STARTPOINT, ENDPOINT)
values (1, 2, 2, 'task_developer', '2023-06-14 08:35:10', '2023-06-14 08:55:00'),
       (1, 2, 2, 'task_reviewer', '2023-06-14 09:35:10', null),
       (1, 2, 1, 'task_developer', '2023-06-12 11:40:00', '2023-06-12 12:35:00'),
       (1, 2, 1, 'task_developer', '2023-06-13 12:35:00', null),
       (1, 2, 1, 'task_tester', '2023-06-14 15:20:00', null),
       (2, 2, 2, 'task_developer', '2023-06-08 07:10:00', null),
       (2, 2, 1, 'task_developer', '2023-06-09 14:48:00', null),
       (2, 2, 1, 'task_tester', '2023-06-10 16:37:00', null);

insert into ACTIVITY ( AUTHOR_ID, TASK_ID, UPDATED, STATUS_CODE )
values (2, 6, '2026-06-01 09:00:10', 'in_progress'),
       (2, 6, '2026-06-10 10:00:10', 'ready_for_review'),
       (2, 6, '2026-06-15 23:05:10', 'done');

insert into REFERENCE (CODE, TITLE, REF_TYPE)
-- TASK
values ('task', 'Task', 2),
       ('story', 'Story', 2),
       ('bug', 'Bug', 2),
       ('epic', 'Epic', 2),
-- SPRINT_STATUS
       ('planning', 'Planning', 4),
       ('active', 'Active', 4),
       ('finished', 'Finished', 4),
-- USER_TYPE
       ('author', 'Author', 5),
       ('developer', 'Developer', 5),
       ('reviewer', 'Reviewer', 5),
       ('tester', 'Tester', 5),
-- PROJECT
       ('scrum', 'Scrum', 1),
       ('task_tracker', 'Task tracker', 1),
-- CONTACT
       ('skype', 'Skype', 0),
       ('tg', 'Telegram', 0),
       ('mobile', 'Mobile', 0),
       ('phone', 'Phone', 0),
       ('website', 'Website', 0),
       ('vk', 'VK', 0),
       ('linkedin', 'LinkedIn', 0),
       ('github', 'GitHub', 0),
-- PRIORITY
       ('critical', 'Critical', 7),
       ('high', 'High', 7),
       ('normal', 'Normal', 7),
       ('low', 'Low', 7),
       ('neutral', 'Neutral', 7);

insert into REFERENCE (CODE, TITLE, REF_TYPE, AUX)
-- MAIL_NOTIFICATION
values ('assigned', 'Assigned', 6, '1'),
       ('three_days_before_deadline', 'Three days before deadline', 6, '2'),
       ('two_days_before_deadline', 'Two days before deadline', 6, '4'),
       ('one_day_before_deadline', 'One day before deadline', 6, '8'),
       ('deadline', 'Deadline', 6, '16'),
       ('overdue', 'Overdue', 6, '32'),
-- TASK_STATUS
       ('todo', 'ToDo', 3, 'in_progress,canceled'),
       ('in_progress', 'In progress', 3, 'ready_for_review,canceled'),
       ('ready_for_review', 'Ready for review', 3, 'review,canceled'),
       ('review', 'Review', 3, 'in_progress,ready_for_test,canceled'),
       ('ready_for_test', 'Ready for test', 3, 'test,canceled'),
       ('test', 'Test', 3, 'done,in_progress,canceled'),
       ('done', 'Done', 3, 'canceled'),
       ('canceled', 'Canceled', 3, null);

-- TASK_TYPE
delete
from REFERENCE
where REF_TYPE = 3;
insert into REFERENCE (CODE, TITLE, REF_TYPE, AUX)
values ('todo', 'ToDo', 3, 'in_progress,canceled|'),
       ('in_progress', 'In progress', 3, 'ready_for_review,canceled|task_developer'),
       ('ready_for_review', 'Ready for review', 3, 'in_progress,review,canceled|'),
       ('review', 'Review', 3, 'in_progress,ready_for_test,canceled|task_reviewer'),
       ('ready_for_test', 'Ready for test', 3, 'review,test,canceled|'),
       ('test', 'Test', 3, 'done,in_progress,canceled|task_tester'),
       ('done', 'Done', 3, 'canceled|'),
       ('canceled', 'Canceled', 3, null);

delete
from REFERENCE
where REF_TYPE = 5;
insert into REFERENCE (CODE, TITLE, REF_TYPE)
-- USER_TYPE
values ('project_author', 'Author', 5),
       ('project_manager', 'Manager', 5),
       ('sprint_author', 'Author', 5),
       ('sprint_manager', 'Manager', 5),
       ('task_author', 'Author', 5),
       ('task_developer', 'Developer', 5),
       ('task_reviewer', 'Reviewer', 5),
       ('task_tester', 'Tester', 5);

