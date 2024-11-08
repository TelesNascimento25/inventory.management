INSERT INTO roles (name) VALUES ('USER'), ('ADMIN');

INSERT INTO users (username, password) VALUES ('admin', 'admin');

INSERT INTO user_roles (user_id, role_id)
VALUES (
           (SELECT id FROM users WHERE username = 'admin'),
           (SELECT id FROM roles WHERE name = 'ADMIN')
       );
