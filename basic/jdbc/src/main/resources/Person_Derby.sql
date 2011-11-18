create table Person (
	id integer primary key not null generated always as identity (start with 1, increment by 1), 
	name varchar(100) not null,
	gender varchar(1) not null,
	dateOfBirth date
);