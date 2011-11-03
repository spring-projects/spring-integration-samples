create table IF NOT EXISTS USERS(USERNAME varchar(100),PASSWORD varchar(100), EMAIL varchar(100));
create table IF NOT EXISTS DUMMY(DUMMY_VALUE varchar(10));
INSERT INTO USERS(USERNAME, PASSWORD, EMAIL) VALUES ('a', 'secret', 'spring-integration@awesome.com');
INSERT INTO USERS(USERNAME, PASSWORD, EMAIL) VALUES ('b', 's3cr3t', 'spring@rocks.com');
INSERT INTO USERS(USERNAME, PASSWORD, EMAIL) VALUES ('foo', 'bar', 'foo@bar.de');