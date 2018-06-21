DROP TABLE IF EXISTS customer;

DROP TABLE IF EXISTS employee;

CREATE TABLE customer (
	id serial not null,
	first_name varchar(100) NULL,
	last_name varchar(100) NULL,
	CONSTRAINT customer_pk PRIMARY KEY (id)
);

CREATE TABLE employee (
	id serial not null ,
	first_name varchar(100) NULL,
	last_name varchar(100) NULL,
	CONSTRAINT employee_pk PRIMARY KEY (id)
);