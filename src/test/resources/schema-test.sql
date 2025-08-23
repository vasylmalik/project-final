CREATE TABLE users (
                       id BIGINT PRIMARY KEY,
                       email VARCHAR(255),
                       password VARCHAR(255),
                       endpoint TIMESTAMP,
                       enabled BOOLEAN
);
CREATE TABLE profile (
                         id BIGINT PRIMARY KEY,
                         user_id BIGINT NOT NULL,
                         mail_notifications INTEGER,
                         FOREIGN KEY (user_id) REFERENCES users(id)
);
INSERT INTO users (id, email, password, endpoint, enabled)
VALUES (1, 'testuser@example.com', 'password', CURRENT_TIMESTAMP, true);

INSERT INTO profile (id, user_id, mail_notifications)
VALUES (1, 1, 1);