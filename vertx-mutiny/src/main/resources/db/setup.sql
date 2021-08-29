CREATE TABLE users
(
  id   SERIAL PRIMARY KEY,
  name VARCHAR
);

insert into users(id, name) values (1, 'Alice');
insert into users(id, name) values (2, 'Bob');
insert into users(id, name) values (3, 'Charlie');
