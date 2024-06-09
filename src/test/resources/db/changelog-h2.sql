--liquibase formatted sql

--changeset kmpk:init
CREATE TABLE IF NOT EXISTS PROJECT (
                                       ID BIGINT AUTO_INCREMENT PRIMARY KEY,
                                       CODE VARCHAR(32) NOT NULL UNIQUE,
                                       TITLE VARCHAR(1024) NOT NULL,
                                       DESCRIPTION VARCHAR(4096) NOT NULL,
                                       TYPE_CODE VARCHAR(32) NOT NULL,
                                       STARTPOINT TIMESTAMP,
                                       ENDPOINT TIMESTAMP,
                                       PARENT_ID BIGINT,
                                       CONSTRAINT FK_PROJECT_PARENT FOREIGN KEY (PARENT_ID) REFERENCES PROJECT(ID) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS MAIL_CASE (
                                         ID BIGINT AUTO_INCREMENT PRIMARY KEY,
                                         EMAIL VARCHAR(255) NOT NULL,
                                         NAME VARCHAR(255) NOT NULL,
                                         DATE_TIME TIMESTAMP NOT NULL,
                                         RESULT VARCHAR(255) NOT NULL,
                                         TEMPLATE VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS SPRINT (
                                      ID BIGINT AUTO_INCREMENT PRIMARY KEY,
                                      STATUS_CODE VARCHAR(32) NOT NULL,
                                      STARTPOINT TIMESTAMP,
                                      ENDPOINT TIMESTAMP,
                                      CODE varchar (512) not null,
                                      TITLE VARCHAR(1024) NOT NULL,
                                      PROJECT_ID BIGINT NOT NULL,
                                      CONSTRAINT FK_SPRINT_PROJECT FOREIGN KEY (PROJECT_ID) REFERENCES PROJECT(ID) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS REFERENCE (
                                         ID BIGINT AUTO_INCREMENT PRIMARY KEY,
                                         CODE VARCHAR(32) NOT NULL,
                                         REF_TYPE SMALLINT NOT NULL,
                                         ENDPOINT TIMESTAMP,
                                         STARTPOINT TIMESTAMP,
                                         TITLE VARCHAR(1024) NOT NULL,
                                         AUX VARCHAR,
                                         CONSTRAINT UK_REFERENCE_REF_TYPE_CODE UNIQUE (REF_TYPE, CODE)
);

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

CREATE TABLE IF NOT EXISTS PROFILE (
                                       ID BIGINT PRIMARY KEY,
                                       LAST_LOGIN TIMESTAMP,
                                       LAST_FAILED_LOGIN TIMESTAMP,
                                       MAIL_NOTIFICATIONS BIGINT,
                                       CONSTRAINT FK_PROFILE_USERS FOREIGN KEY (ID) REFERENCES USERS (ID) ON DELETE CASCADE
);
--
-- CREATE TABLE IF NOT EXISTS CONTACT (
--                                        ID BIGINT NOT NULL,
--                                        CODE VARCHAR(32) NOT NULL,
--                                        VALUE VARCHAR(256) NOT NULL,
--                                        PRIMARY KEY (ID, CODE),
--                                        CONSTRAINT FK_CONTACT_PROFILE FOREIGN KEY (ID) REFERENCES PROFILE (ID) ON DELETE CASCADE
-- );

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

CREATE TABLE IF NOT EXISTS ACTIVITY (
                                        ID BIGINT AUTO_INCREMENT PRIMARY KEY,
                                        AUTHOR_ID BIGINT NOT NULL,
                                        TASK_ID BIGINT NOT NULL,
                                        UPDATED TIMESTAMP,
                                        COMMENT VARCHAR(4096),
                                        TITLE VARCHAR(1024),
                                        DESCRIPTION VARCHAR(4096),
                                        ESTIMATE INTEGER,
                                        TYPE_CODE VARCHAR(32),
                                        STATUS_CODE VARCHAR(32),
                                        PRIORITY_CODE VARCHAR(32),
                                        CONSTRAINT FK_ACTIVITY_USERS FOREIGN KEY (AUTHOR_ID) REFERENCES USERS (ID),
                                        CONSTRAINT FK_ACTIVITY_TASK FOREIGN KEY (TASK_ID) REFERENCES TASK (ID) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS TASK_TAG (
                                        TASK_ID BIGINT NOT NULL,
                                        TAG VARCHAR(32) NOT NULL,
                                        CONSTRAINT UK_TASK_TAG UNIQUE (TASK_ID, TAG),
                                        CONSTRAINT FK_TASK_TAG FOREIGN KEY (TASK_ID) REFERENCES TASK (ID) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS USER_BELONG (
                                           ID BIGINT AUTO_INCREMENT PRIMARY KEY,
                                           OBJECT_ID BIGINT NOT NULL,
                                           OBJECT_TYPE SMALLINT NOT NULL,
                                           USER_ID BIGINT NOT NULL,
                                           USER_TYPE_CODE VARCHAR(32) NOT NULL,
                                           STARTPOINT TIMESTAMP,
                                           ENDPOINT TIMESTAMP,
                                           CONSTRAINT FK_USER_BELONG FOREIGN KEY (USER_ID) REFERENCES USERS (ID)
);

CREATE TABLE IF NOT EXISTS ATTACHMENT (
                                          ID BIGINT AUTO_INCREMENT PRIMARY KEY,
                                          NAME VARCHAR(128) NOT NULL,
                                          FILE_LINK VARCHAR(2048) NOT NULL,
                                          OBJECT_ID BIGINT NOT NULL,
                                          OBJECT_TYPE SMALLINT NOT NULL,
                                          USER_ID BIGINT NOT NULL,
                                          DATE_TIME TIMESTAMP,
                                          CONSTRAINT FK_ATTACHMENT FOREIGN KEY (USER_ID) REFERENCES USERS (ID)
);

CREATE TABLE IF NOT EXISTS USER_ROLE (
                                         USER_ID BIGINT NOT NULL,
                                         ROLE SMALLINT NOT NULL,
                                         CONSTRAINT UK_USER_ROLE UNIQUE (USER_ID, ROLE),
                                         CONSTRAINT FK_USER_ROLE FOREIGN KEY (USER_ID) REFERENCES USERS (ID) ON DELETE CASCADE
);