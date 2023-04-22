--liquibase formatted sql

--changeset kriffer:add_dashboard

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
