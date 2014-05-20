drop table COFFEE_BEVERAGES;
drop PROCEDURE FIND_COFFEE;
drop PROCEDURE FIND_ALL_COFFEE_BEVERAGES;

create table COFFEE_BEVERAGES(ID INTEGER NOT NULL CONSTRAINT COFFEE_BEVERAGES_PK PRIMARY KEY, COFFEE_NAME varchar(100), COFFEE_DESCRIPTION varchar(200));

INSERT INTO COFFEE_BEVERAGES (ID, COFFEE_NAME, COFFEE_DESCRIPTION) VALUES (1, 'Espresso',     'Espressos keep developers going in the morning. There are never enough of them.');
INSERT INTO COFFEE_BEVERAGES (ID, COFFEE_NAME, COFFEE_DESCRIPTION) VALUES (2, 'Cappuccino',   'For the finer moments. Wrap your espresso in a tasty layer of foam.');
INSERT INTO COFFEE_BEVERAGES (ID, COFFEE_NAME, COFFEE_DESCRIPTION) VALUES (3, 'Mocha',        'Mmmmh, chocolate.');
INSERT INTO COFFEE_BEVERAGES (ID, COFFEE_NAME, COFFEE_DESCRIPTION) VALUES (4, 'Latte',        'If you are more into milk than into foam.');

CREATE PROCEDURE FIND_COFFEE( IN COFFEE_NAME INTEGER, OUT COFFEE_DESCRIPTION VARCHAR(100)) PARAMETER STYLE JAVA LANGUAGE JAVA EXTERNAL NAME 'org.springframework.integration.samples.storedprocedure.jdbc.storedproc.derby.DerbyStoredProcedures.findCoffee';
CREATE PROCEDURE FIND_ALL_COFFEE_BEVERAGES() PARAMETER STYLE JAVA LANGUAGE JAVA MODIFIES SQL DATA DYNAMIC RESULT SETS 1 EXTERNAL NAME 'org.springframework.integration.samples.storedprocedure.jdbc.storedproc.derby.DerbyStoredProcedures.findAllCoffeeBeverages';

