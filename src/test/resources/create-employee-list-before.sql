delete from employee;

ALTER SEQUENCE employee_id_seq RESTART WITH 10;

INSERT INTO employee (id, first_name, last_name, email, photo)
VALUES (1, 'Andrey', 'Savin', 'savin@gmail.com', 'photo1.jpg'),
       (2, 'Emma', 'Baumgarten', 'emma@gmail.com', 'photo2.jpg'),
       (3, 'Avani', 'Gupta', 'avani@gmail.com', 'photo3.jpg'),
       (4, 'Yuri', 'Petrov', 'yuri@gmail.com', 'photo4.jpg');