-- DROP schema IF EXISTS minicrmschema cascade ;
-- create schema minicrmschema;
-- set search_path to minicrmschema;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS users cascade ;

CREATE SEQUENCE users_seq;

CREATE TABLE users
(
    id         int     NOT NULL DEFAULT NEXTVAL ('users_seq'),
    username   varchar(50) NOT NULL,
    password   varchar(80) NOT NULL,
    first_name varchar(50) NOT NULL,
    last_name  varchar(50) NOT NULL,
    email      varchar(50) NOT NULL,
    PRIMARY KEY (id)
)
;

ALTER SEQUENCE users_seq RESTART WITH 1;

--
-- Dumping data for table `users`
--
-- NOTE: The passwords are encrypted using BCrypt
--
-- !!!!Default passwords here are: pass123
--

INSERT INTO users (username, password, first_name, last_name, email)
VALUES ('andrey', '$2a$10$/K.WdZlnm6GJni3i2ahWiueIovcoUoTHMHIDYD.JzqDL7RcMG8Q.y', 'Andrey', 'Savin', 'andrey@crm.com'),
       ('boris', '$2a$10$/K.WdZlnm6GJni3i2ahWiueIovcoUoTHMHIDYD.JzqDL7RcMG8Q.y', 'Boris', 'Borisov',
        'boris@crm.com'),
       ('alena', '$2a$10$/K.WdZlnm6GJni3i2ahWiueIovcoUoTHMHIDYD.JzqDL7RcMG8Q.y', 'Alena', 'Alenova',
        'alena@crm.com');


--
-- Table structure for table `roles`
--

DROP TABLE IF EXISTS roles cascade ;

CREATE SEQUENCE roles_seq;

CREATE TABLE roles
(
    id   int NOT NULL DEFAULT NEXTVAL ('roles_seq'),
    name varchar(50) DEFAULT NULL,
    PRIMARY KEY (id)
)
;

ALTER SEQUENCE roles_seq RESTART WITH 1;

--
-- Dumping data for table `roles`
--

INSERT INTO roles (name)
VALUES ('ROLE_EMPLOYEE'),
       ('ROLE_MANAGER'),
       ('ROLE_ADMIN');

--
-- Table structure for table `users_roles`
--

DROP TABLE IF EXISTS users_roles cascade ;

CREATE TABLE users_roles
(
    user_id int NOT NULL,
    role_id int NOT NULL,

    PRIMARY KEY (user_id, role_id),

    CONSTRAINT FK_USER FOREIGN KEY (user_id)
        REFERENCES users (id)
        ON DELETE NO ACTION ON UPDATE NO ACTION,

    CONSTRAINT FK_ROLE FOREIGN KEY (role_id)
        REFERENCES roles (id)
        ON DELETE NO ACTION ON UPDATE NO ACTION
);

CREATE INDEX FK_ROLE_idx ON users_roles (role_id);

--
-- Dumping data for table `users_roles`
--

INSERT INTO users_roles (user_id, role_id)
VALUES (1, 1),
       (2, 1),
       (2, 2),
       (3, 1),
       (3, 2),
       (3, 3);

--
-- Table structure for table `employee`
--

DROP TABLE IF EXISTS employee cascade ;

CREATE SEQUENCE employee_seq;

CREATE TABLE employee
(
    id         int NOT NULL DEFAULT NEXTVAL ('employee_seq'),
    first_name varchar(45) DEFAULT NULL,
    last_name  varchar(45) DEFAULT NULL,
    email      varchar(45) DEFAULT NULL,
    photo      varchar(64) DEFAULT NULL,
    PRIMARY KEY (id)

)
;

ALTER SEQUENCE employee_seq RESTART WITH 1;

--
-- Data for table `employee`
--

INSERT INTO employee (first_name, last_name, email, photo)
VALUES ('Andrey', 'Savin', 'savin@gmail.com', 'Savin-photo.jpg'),
       ('Emma', 'Baumgarten', 'emma@gmail.com', 'Savin-photo.jpg'),
       ('Avani', 'Gupta', 'avani@gmail.com', 'Savin-photo.jpg'),
       ('Yuri', 'Petrov', 'yuri@gmail.com', 'Savin-photo.jpg'),
       ('Juan', 'Vega', 'juan@gmail.com', 'Savin-photo.jpg'),
       ('Andre', 'Vega', 'andre@gmail.com', 'Savin-photo.jpg'),
       ('Sergey', 'Vega', 'sergey@gmail.com', 'Savin-photo.jpg');