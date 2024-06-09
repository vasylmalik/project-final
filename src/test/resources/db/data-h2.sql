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
       ('linkedin', 'LinkedIn', 0),
       ('github', 'GitHub', 0),
-- PRIORITY
       ('critical', 'Critical', 7),
       ('high', 'High', 7),
       ('normal', 'Normal', 7),
       ('low', 'Low', 7),
       ('neutral', 'Neutral', 7);

insert into  REFERENCE (CODE, TITLE, REF_TYPE, AUX)
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

CREATE TABLE IF NOT EXISTS USERS (
                                     ID BIGINT AUTO_INCREMENT PRIMARY KEY,
                                     DISPLAY_NAME VARCHAR(32) NOT NULL UNIQUE,
                                     EMAIL VARCHAR(128) NOT NULL UNIQUE,
                                     FIRST_NAME VARCHAR(32) NOT NULL,
                                     LAST_NAME VARCHAR(32),
                                     PASSWORD VARCHAR(128) NOT NULL,
                                     ENDPOINT TIMESTAMP,
                                     STARTPOINT TIMESTAMP
);



insert into USERS (EMAIL, PASSWORD, FIRST_NAME, LAST_NAME, DISPLAY_NAME)
values ('user@gmail.com', 'testPassword', 'userFirstName', 'userLastName', 'userDisplayName'),
       ('admin@gmail.com', 'testPassword', 'adminFirstName', 'adminLastName', 'adminDisplayName'),
       ('guest@gmail.com', 'testPassword', 'guestFirstName', 'guestLastName', 'guestDisplayName'),
       ('manager@gmail.com', 'testPassword', 'managerFirstName', 'managerLastName', 'managerDisplayName'),
       ('taras@gmail.com', 'testPassword', 'Тарас', 'Шевченко', '@taras'),
       ('petlura@gmail.com', 'testPassword', 'Симон', 'Петлюра', '@epetl'),
       ('moroz_a@gmail.com', 'testPassword', 'Александр', 'Мороз', '@Moroz93'),
       ('antonio.nest@gmail.com', 'testPassword', 'Антон', 'Нестеров', '@antonio_nest'),
       ('i.franko@gmail.com', 'testPassword', 'Иван', 'Франко', '@ifranko'),
       ('g.skovoroda@gmail.com', 'testPassword', 'Григорий', 'Сковорода', '@Gregory24'),
       ('arsh.and@gmail.com', 'testPassword', 'Андрей', 'Арш', '@arsh01'),
       ('squirrel2011@gmail.com', 'testPassword', 'Леся', 'Иванюк', '@SmallSquirrel'),
       ('nikk24@gmail.com', 'testPassword', 'Николай', 'Никулин', '@nikk'),
       ('artem711@gmail.com', 'testPassword', 'Артем', 'Запорожец', '@Artt'),
       ('max.pain@gmail.com', 'testPassword', 'Максим', 'Дудник', '@MaxPain'),
       ('admin@aws.co', 'testPassword', 'test', 'admin', '@testAdmin');



CREATE TABLE IF NOT EXISTS PROFILE (
                                       ID BIGINT PRIMARY KEY,
                                       LAST_LOGIN TIMESTAMP,
                                       LAST_FAILED_LOGIN TIMESTAMP,
                                       MAIL_NOTIFICATIONS BIGINT,
                                       CONSTRAINT FK_PROFILE_USERS FOREIGN KEY (ID) REFERENCES USERS (ID) ON DELETE CASCADE
);

insert into PROFILE (ID, LAST_FAILED_LOGIN, LAST_LOGIN, MAIL_NOTIFICATIONS)
values (1, null, null, 49),
       (2, null, null, 14);

create table IF NOT EXISTS PROJECT
(
    ID bigint primary key,
    CODE        varchar(32)   not null
        constraint UK_PROJECT_CODE unique,
    TITLE       varchar(1024) not null,
    DESCRIPTION varchar(4096) not null,
    TYPE_CODE   varchar(32)   not null,
    STARTPOINT  timestamp,
    ENDPOINT    timestamp,
    PARENT_ID   bigint,
    constraint FK_PROJECT_PARENT foreign key (PARENT_ID) references PROJECT (ID) on delete cascade
);

insert into PROJECT (code, title, description, type_code, parent_id)
values ('JiraRush', 'JiraRush', '«Mini-JIRA» app : project management system tutorial app', 'task_tracker', null),
       ('Test_Project', 'Test Project', 'Just test project', 'task_tracker', null),
       ('Test_Project_2', 'Test Project 2', 'Just test project 2', 'task_tracker', null),
       ('JiraRush sub', 'JiraRush subproject', 'subproject', 'task_tracker', 1);

create table IF NOT EXISTS SPRINT
(
    ID bigint primary key,
    STATUS_CODE varchar(32)   not null,
    STARTPOINT  timestamp,
    ENDPOINT    timestamp,
    CODE        varchar(512) not null,
    TITLE       varchar(1024) not null,
    PROJECT_ID  bigint        not null,
    constraint FK_SPRINT_PROJECT foreign key (PROJECT_ID) references PROJECT (ID) on delete cascade


);

insert into SPRINT (status_code, startpoint, endpoint, code, TITLE,  project_id)
values ('active', null, null, 'code',  'Sprint-2', 1),
       ('finished', '2023-04-09 08:05:10', '2023-04-29 16:48:34', 'code', 'Sprint-1', 2),
       ('finished', '2023-04-03 12:14:11', '2023-04-18 17:03:41', 'code', 'Sprint-2', 2),
       ('active', '2023-04-05 14:25:43', '2023-06-10 13:00:00', 'code', 'Sprint-3', 2),
       ('active', null, null, 'code', 'Sprint-1', 4);

CREATE TABLE IF NOT EXISTS TASK (
                                    ID BIGINT AUTO_INCREMENT PRIMARY KEY,
                                    TITLE VARCHAR(1024) NOT NULL,
                                    DESCRIPTION VARCHAR(4096) NOT NULL,
                                    TYPE_CODE VARCHAR(32) NOT NULL,
                                    STATUS_CODE VARCHAR(32) NOT NULL,
                                    PRIORITY_CODE VARCHAR(32) NOT NULL,
                                    ESTIMATE INTEGER,
                                    UPDATED TIMESTAMP,
                                    PROJECT_ID BIGINT NOT NULL,
                                    SPRINT_ID BIGINT,
                                    PARENT_ID BIGINT,
                                    STARTPOINT TIMESTAMP,
                                    ENDPOINT TIMESTAMP,
                                    CONSTRAINT FK_TASK_SPRINT FOREIGN KEY (SPRINT_ID) REFERENCES SPRINT (ID) ON DELETE SET NULL,
                                    CONSTRAINT FK_TASK_PROJECT FOREIGN KEY (PROJECT_ID) REFERENCES PROJECT (ID) ON DELETE CASCADE,
                                    CONSTRAINT FK_TASK_PARENT_TASK FOREIGN KEY (PARENT_ID) REFERENCES TASK (ID) ON DELETE CASCADE
);


INSERT INTO TASK (TITLE, TYPE_CODE, DESCRIPTION, STATUS_CODE, PRIORITY_CODE, PROJECT_ID, SPRINT_ID, STARTPOINT)
values ('Data', 'epic', 'desc', 'in_progress', 1, 1, 1,
        TIMESTAMPADD(SECOND, CAST((RANDOM() * 5 * 60) AS INT) + CAST((RANDOM() * 20) AS INT), CURRENT_TIMESTAMP));

create table if not exists ACTIVITY
(
    ID bigint primary key,
    AUTHOR_ID     bigint not null,
    TASK_ID       bigint not null,
    UPDATED       timestamp,
    COMMENT       varchar(4096),
    TITLE         varchar(1024),
    DESCRIPTION   varchar(4096),
    ESTIMATE      integer,
    TYPE_CODE     varchar(32),
    STATUS_CODE   varchar(32),
    PRIORITY_CODE varchar(32),
    constraint FK_ACTIVITY_USERS foreign key (AUTHOR_ID) references USERS (ID),
    constraint FK_ACTIVITY_TASK foreign key (TASK_ID) references TASK (ID) on delete cascade
);


INSERT INTO ACTIVITY(AUTHOR_ID, TASK_ID, UPDATED, COMMENT, TITLE, DESCRIPTION, ESTIMATE, TYPE_CODE, STATUS_CODE,
                     PRIORITY_CODE)
values (6, 1, '2023-05-15 09:05:10', null, 'Data',
        null, 3, 'epic', 'in_progress', 'low'),
       (5, 1, '2023-05-15 12:25:10', null, 'Data',
        null, null, null, 'ready_for_review', 'normal'),
       (6, 1, '2023-05-15 14:05:10', null, 'Data',
        null, 4, null, 'done', null);

