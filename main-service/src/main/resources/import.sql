INSERT INTO roles(name) VALUES ('ROLE_USER');
INSERT INTO roles(name) VALUES ('ROLE_ADMIN');

INSERT INTO categories(name) VALUES ('Выставки');
INSERT INTO categories(name) VALUES ('Концерты');
INSERT INTO categories(name) VALUES ('Театр');
INSERT INTO categories(name) VALUES ('Обучение');
INSERT INTO categories(name) VALUES ('Кино');
INSERT INTO categories(name) VALUES ('Фестивали');
INSERT INTO categories(name) VALUES ('Экскурсии');
INSERT INTO categories(name) VALUES ('Стендапы');
INSERT INTO categories(name) VALUES ('Квесты');

INSERT INTO users(name, username, password, email) VALUES ('admin', 'admin', 'admin', 'admin@admin.ru');

INSERT INTO user_roles(user_id, role_id) VALUES (1, 1);
INSERT INTO user_roles(user_id, role_id) VALUES (1, 2);