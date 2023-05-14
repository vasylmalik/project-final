DELETE FROM contact;
DELETE FROM profile;
DELETE FROM user_role cascase;
DELETE FROM user_belong;
DELETE FROM users;
DELETE FROM task;
DELETE FROM task_tag;
DELETE FROM sprint;
DELETE FROM project;
DELETE FROM activity;
DELETE FROM attachment;
DELETE FROM mail_case;
DELETE FROM reference;

insert into users (EMAIL, PASSWORD, FIRST_NAME, LAST_NAME, DISPLAY_NAME, STARTPOINT)
values ('user@gmail.com', '{noop}password', 'userFirstName', 'userLastName', 'userDisplayName', current_timestamp),
       ('admin@gmail.com', '{noop}admin', 'adminFirstName', 'adminLastName', 'adminDisplayName', current_timestamp),
       ('guest@gmail.com', '{noop}guest', 'guestFirstName', 'guestLastName', 'guestDisplayName', current_timestamp);

-- 0 DEV
-- 1 ADMIN
insert into USER_ROLE (ROLE, USER_ID)
values (0, 1),
       (1, 2),
       (0, 2);

DELETE FROM reference;
--============ References =================
insert into reference (CODE, TITLE, REF_TYPE, STARTPOINT)
-- TASK
values ('task', 'Task', 2, current_timestamp),
       ('story', 'Story', 2, current_timestamp),
       ('bug', 'Bug', 2, current_timestamp),
       ('epic', 'Epic', 2, current_timestamp),
-- TASK_STATUS
       ('icebox', 'Icebox', 3, current_timestamp),
       ('backlog', 'Backlog', 3, current_timestamp),
       ('ready', 'Ready', 3, current_timestamp),
       ('in progress', 'In progress', 3, current_timestamp),
       ('done', 'Done', 3, current_timestamp),
-- SPRINT_STATUS
       ('planning', 'Planning', 4, current_timestamp),
       ('implementation', 'Implementation', 4, current_timestamp),
       ('review', 'Review', 4, current_timestamp),
       ('retrospective', 'Retrospective', 4, current_timestamp),
-- USER_TYPE
       ('admin', 'Admin', 5, current_timestamp),
       ('user', 'User', 5, current_timestamp),
-- PROJECT
       ('scrum', 'Scrum', 1, current_timestamp),
       ('task tracker', 'Task tracker', 1, current_timestamp),
-- CONTACT
       ('skype', 'Skype', 0, current_timestamp),
       ('tg', 'Telegram', 0, current_timestamp),
       ('mobile', 'Mobile', 0, current_timestamp),
       ('phone', 'Phone', 0, current_timestamp),
       ('website', 'Website', 0, current_timestamp),
       ('vk', 'VK', 0, current_timestamp),
       ('linkedin', 'LinkedIn', 0, current_timestamp),
       ('github', 'GitHub', 0, current_timestamp),
-- PRIORITY
       ('critical', 'Critical', 7, current_timestamp),
       ('high', 'High', 7, current_timestamp),
       ('normal', 'Normal', 7, current_timestamp),
       ('low', 'Low', 7, current_timestamp),
       ('neutral', 'Neutral', 7, current_timestamp);

insert into reference (CODE, TITLE, REF_TYPE, AUX, STARTPOINT)
-- MAIL_NOTIFICATION
values ('assigned', 'Assigned', 6, '1', current_timestamp),
       ('three_days_before_deadline', 'Three days before deadline', 6, '2', current_timestamp),
       ('two_days_before_deadline', 'Two days before deadline', 6, '4', current_timestamp),
       ('one_day_before_deadline', 'One day before deadline', 6, '8', current_timestamp),
       ('deadline', 'Deadline', 6, '16', current_timestamp),
       ('overdue', 'Overdue', 6, '32', current_timestamp);

insert into profile (ID, LAST_FAILED_LOGIN, LAST_LOGIN, MAIL_NOTIFICATIONS)
values (1, null, null, 49),
       (2, null, null, 14);

DELETE FROM contact;
insert into contact (ID, CODE, "value")
values (1, 'skype', 'userSkype'),
       (1, 'mobile', '+01234567890'),
       (1, 'website', 'user.com'),
       (2, 'github', 'adminGitHub'),
       (2, 'tg', 'adminTg'),
       (2, 'vk', 'adminVk');

-- bugtracking
INSERT INTO project (id, code, title, description, type_code, startpoint, endpoint, parent_id) VALUES (2, 'task tracker', 'PROJECT-1', 'test project', 'task tracker', current_timestamp, null, null);

INSERT INTO sprint (id, status_code, startpoint, endpoint, title, project_id) VALUES (1, 'planning', '2023-04-09 23:05:05.000000', '2023-04-12 23:05:12.000000', 'Sprint-1', 2);

INSERT INTO task (id, title, description, type_code, status_code, priority_code, estimate, updated, project_id, sprint_id, parent_id, startpoint, endpoint) VALUES (2, 'Task-1', 'short test task', 'task', 'in progress', 'high', null, null, 2, 1, null, current_timestamp, null);
INSERT INTO task (id, title, description, type_code, status_code, priority_code, estimate, updated, project_id, sprint_id, parent_id, startpoint, endpoint) VALUES (3, 'Task-2', 'test 2 task', 'bug', 'ready', 'normal', null, null, 2, 1, null, current_timestamp, null);
INSERT INTO task (id, title, description, type_code, status_code, priority_code, estimate, updated, project_id, sprint_id, parent_id, startpoint, endpoint) VALUES (5, 'Task-4', 'test 4', 'bug', 'in progress', 'normal', null, null, 2, 1, null, current_timestamp, null);
INSERT INTO task (id, title, description, type_code, status_code, priority_code, estimate, updated, project_id, sprint_id, parent_id, startpoint, endpoint) VALUES (4, 'Task-3', 'test 3 descr', 'task', 'done', 'low', null, null, 2, 1, null, current_timestamp, null);

INSERT INTO user_belong (id, object_id, object_type, user_id, user_type_code, startpoint, endpoint) VALUES (3, 2, 2, 2, 'admin', current_timestamp, null);
INSERT INTO user_belong (id, object_id, object_type, user_id, user_type_code, startpoint, endpoint) VALUES (4, 3, 2, 2, 'admin', current_timestamp, null);
INSERT INTO user_belong (id, object_id, object_type, user_id, user_type_code, startpoint, endpoint) VALUES (5, 4, 2, 2, 'admin', current_timestamp, null);
INSERT INTO user_belong (id, object_id, object_type, user_id, user_type_code, startpoint, endpoint) VALUES (6, 5, 2, 2, 'admin', current_timestamp, null);

