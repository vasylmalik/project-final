DELETE FROM profile;
DELETE FROM user_role;
DELETE FROM user_belong;
DELETE FROM users;
DELETE FROM task;
DELETE FROM sprint;
DELETE FROM project;
ALTER SEQUENCE users_id_seq RESTART WITH 1;

insert into users (EMAIL, PASSWORD, FIRST_NAME, LAST_NAME, DISPLAY_NAME)
values ('user@gmail.com', '{noop}password', 'userFirstName', 'userLastName', 'userDisplayName'),
       ('admin@gmail.com', '{noop}admin', 'adminFirstName', 'adminLastName', 'adminDisplayName'),
       ('guest@gmail.com', '{noop}guest', 'guestFirstName', 'guestLastName', 'guestDisplayName');

-- 0 DEV
-- 1 ADMIN
insert into USER_ROLE (ROLE, USER_ID)
values (0, 1),
       (1, 2),
       (0, 2);

DELETE FROM reference;
ALTER SEQUENCE reference_id_seq RESTART WITH 1;
--============ References =================
insert into reference (CODE, TITLE, REF_TYPE)
-- TASK
values ('task', 'Task', 2),
       ('story', 'Story', 2),
       ('bug', 'Bug', 2),
       ('epic', 'Epic', 2),
-- TASK_STATUS
       ('icebox', 'Icebox', 3),
       ('backlog', 'Backlog', 3),
       ('ready', 'Ready', 3),
       ('in progress', 'In progress', 3),
       ('done', 'Done', 3),
-- SPRINT_STATUS
       ('planning', 'Planning', 4),
       ('implementation', 'Implementation', 4),
       ('review', 'Review', 4),
       ('retrospective', 'Retrospective', 4),
-- USER_TYPE
       ('admin', 'Admin', 5),
       ('user', 'User', 5),
-- PROJECT
       ('scrum', 'Scrum', 1),
       ('task tracker', 'Task tracker', 1),
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

insert into reference (CODE, TITLE, REF_TYPE, AUX)
-- MAIL_NOTIFICATION
values ('assigned', 'Assigned', 6, '1'),
       ('three_days_before_deadline', 'Three days before deadline', 6, '2'),
       ('two_days_before_deadline', 'Two days before deadline', 6, '4'),
       ('one_day_before_deadline', 'One day before deadline', 6, '8'),
       ('deadline', 'Deadline', 6, '16'),
       ('overdue', 'Overdue', 6, '32');

insert into profile (ID, LAST_FAILED_LOGIN, LAST_LOGIN, MAIL_NOTIFICATIONS)
values (1, null, null, 49),
       (2, null, null, 14);

DELETE FROM contact;
insert into contact (ID, CODE, VALUE)
values (1, 'skype', 'userSkype'),
       (1, 'mobile', '+01234567890'),
       (1, 'website', 'user.com'),
       (2, 'github', 'adminGitHub'),
       (2, 'tg', 'adminTg'),
       (2, 'vk', 'adminVk');

-- bugtracking
INSERT INTO project (id, code, title, description, type_code, startpoint, endpoint, parent_id) VALUES (2, 'task tracker', 'PROJECT-1', 'test project', 'task tracker', null, null, null);

INSERT INTO sprint (id, status_code, startpoint, endpoint, title, project_id) VALUES (1, 'planning', '2023-04-09 23:05:05.000000', '2023-04-12 23:05:12.000000', 'Sprint-1', 2);

INSERT INTO task (id, title, description, type_code, status_code, priority_code, estimate, updated, project_id, sprint_id, parent_id, startpoint, endpoint) VALUES (2, 'Task-1', 'short test task', 'task', 'in progress', 'high', null, null, 2, 1, null, null, null);
INSERT INTO task (id, title, description, type_code, status_code, priority_code, estimate, updated, project_id, sprint_id, parent_id, startpoint, endpoint) VALUES (3, 'Task-2', 'test 2 task', 'bug', 'ready', 'normal', null, null, 2, 1, null, null, null);
INSERT INTO task (id, title, description, type_code, status_code, priority_code, estimate, updated, project_id, sprint_id, parent_id, startpoint, endpoint) VALUES (5, 'Task-4', 'test 4', 'bug', 'in progress', 'normal', null, null, 2, 1, null, null, null);
INSERT INTO task (id, title, description, type_code, status_code, priority_code, estimate, updated, project_id, sprint_id, parent_id, startpoint, endpoint) VALUES (4, 'Task-3', 'test 3 descr', 'task', 'done', 'low', null, null, 2, 1, null, null, null);

INSERT INTO user_belong (id, object_id, object_type, user_id, user_type_code, startpoint, endpoint) VALUES (3, 2, 2, 2, 'admin', null, null);
INSERT INTO user_belong (id, object_id, object_type, user_id, user_type_code, startpoint, endpoint) VALUES (4, 3, 2, 2, 'admin', null, null);
INSERT INTO user_belong (id, object_id, object_type, user_id, user_type_code, startpoint, endpoint) VALUES (5, 4, 2, 2, 'admin', null, null);
INSERT INTO user_belong (id, object_id, object_type, user_id, user_type_code, startpoint, endpoint) VALUES (6, 5, 2, 2, 'admin', null, null);

