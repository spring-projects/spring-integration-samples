Spring Integration - Stored Procedure Example - Oracle
======================================================

## Overview

This sample application illustrates the usage of the *Spring Integration* Stored Procedure components using an Oracle™ database as a backend.
Actually 2 samples are provided:

* **Sample 1** Capitalizes Strings
* **Sample 2** Retrieves Coffee Data
  - This is sample is similar to the [Stored Procedure Sample for PostgreSql][]

## Running the Sample

### Pre-requisites

This sample was tested against: **Oracle Database Express Edition 11g Release 2** (which can be downloaded and used for free).
Nevertheless, the example should work with other versions as well.

- Access to a Oracle or Oracle XE database instance
- Install Oracle JDBC Driver to your local Maven repository (~/.m2)

### JDBC Driver Installation for Oracle

- Go to [http://www.oracle.com/technetwork/indexes/downloads/index.html](http://www.oracle.com/technetwork/indexes/downloads/index.html)
- Under "JDBC Drivers", download the appropriate driver relevant to your Oracle and JDK version (This sample was tested using "Oracle Database 11g Release 2 JDBC Drivers")
- Once downloaded, install the driver to your local Maven repository:

        $ mvn install:install-file -DgroupId=com.oracle -DartifactId=ojdbc6 -Dversion=11.2.0.3 -Dpackaging=jar -Dfile=~/dev/ojdbc6.jar -DgeneratePom=true

- Now you can uncomment the `ojdbc6` dependency in the build.gradle file for `stored-procedures-oracle` project.
 
After that you can run the sample application using [Gradle Application Plugin](http://www.gradle.org/docs/current/userguide/application_plugin.html):

    $ gradlew :stored-procedures-oracle:run

### Common Oracle Setup

#### Create Tablespace

```SQL
	CREATE TABLESPACE procedure_test
	DATAFILE 'c:/data/procedure_test.dbf'
	   SIZE 10M
	   AUTOEXTEND ON NEXT 10M
	   MAXSIZE 100M;
```

#### Create User

```SQL
	CREATE USER storedproc
	       IDENTIFIED BY storedproc
	       DEFAULT TABLESPACE procedure_test
	       TEMPORARY TABLESPACE temp;
```

#### Grant Rights

```SQL
	GRANT CREATE SESSION, CREATE TABLE, CREATE VIEW, CREATE SEQUENCE, CREATE PROCEDURE TO storedproc;
	ALTER USER storedproc QUOTA unlimited ON procedure_test;
```

### Setting up the Spring Application Context

You may have to update the Oracle DB properties in:

    /src/main/resources/META-INF/spring/integration/spring-integration-context.xml

```XML
    <bean id="dataSource" class="oracle.jdbc.pool.OracleDataSource" destroy-method="close">
	   <property name="connectionCachingEnabled" value="true" />
	   <property name="URL" value="jdbc:oracle:thin:@//localhost:1521/XE" />
	   <property name="password" value="storedproc" />
	   <property name="user"     value="storedproc" />
	   <property name="connectionCacheProperties">
	      <props merge="default">
	         <prop key="MinLimit">3</prop>
	         <prop key="MaxLimit">20</prop>
	      </props>
	   </property>
	</bean>
```

## Running Sample 1 - Capitalizes Strings

This example provides a simple example using the stored procedure outbound gateway
adapter. This example will call an Oracle Stored Procedure as well as an Oracle Function using the StoredProc Outbound Gateway.

### Creating the Stored Procedure

```SQL
create or replace
PROCEDURE CAPITALIZE_STRING(inoutString IN OUT VARCHAR) IS
BEGIN
    SELECT upper(inoutString) INTO inoutString from dual ;
END;
```

### Creating the Stored Function

```SQL
create or replace
FUNCTION GET_COOL_NUMBER
   RETURN NUMBER
   IS cool_number NUMBER(11,2);
BEGIN
   cool_number := 12345;
   RETURN cool_number;
END;
```

### Execute the Sample

* running the "Main" class from within STS (Right-click on Main class --> Run As --> Java Application)
* or from the command line:
    
    $ gradlew :stored-procedures-oracle:run

You should see the following output:

	16:05:19.556 INFO  [main][org.springframework.integration.samples.storedprocedure.Main]
	=========================================================

	          Welcome to Spring Integration's
	     Stored Procedure/Function Sample for Oracle

	    For more information please visit:
	    http://www.springsource.org/spring-integration

	=========================================================
	Please enter a choice and press <enter>:
		1. Execute Sample 1 (Capitalize String)
		2. Execute Sample 2 (Coffee Service)
		q. Quit the application

Select **Opion 1**.

	=========================================================

	    Please press 'q + Enter' to quit the application.

	=========================================================
	Please enter a string and press <enter>: hello world
	Converting String to Uppcase using Stored Procedure...
	Retrieving Numeric value via Sql Function...
	Converted 'hello world' - End Result: 'HELLO WORLD_12345'.

When you enter a text, the text will be converted into upper-case using the Oracle Stored Procedure named `CAPITALIZE_STRING`. Afterwards, the String is concatenated with the result from calling the Oracle Stored Function `GET_COOL_NUMBER`.

## Running Sample 2 - Coffee Service

### Create Table COFFEE_BEVERAGES

```SQL
CREATE TABLE "COFFEE_BEVERAGES"(
  "ID"                 NUMBER(10,0)       NOT NULL,
  "COFFEE_NAME"        VARCHAR2(50 CHAR)  NOT NULL,
  "COFFEE_DESCRIPTION" VARCHAR2(500 CHAR) NOT NULL,
  CONSTRAINT "COFFEE_BEVERAGES_PK" PRIMARY KEY ("ID"));
```
### Add Sample Data to	 Table COFFEE_BEVERAGES

```SQL
REM INSERTING into COFFEE_BEVERAGES
SET DEFINE OFF;
Insert into COFFEE_BEVERAGES (ID,COFFEE_NAME,COFFEE_DESCRIPTION) values (1,'Espresso','Espressos keep developers going in the morning. There are never enough of them.');
Insert into COFFEE_BEVERAGES (ID,COFFEE_NAME,COFFEE_DESCRIPTION) values (2,'Cappuccino','For the finer moments. Wrap your espresso in a tasty layer of foam.');
Insert into COFFEE_BEVERAGES (ID,COFFEE_NAME,COFFEE_DESCRIPTION) values (3,'Mocha','Mmmmh, chocolate.');
Insert into COFFEE_BEVERAGES (ID,COFFEE_NAME,COFFEE_DESCRIPTION) values (4,'Latte','If you are more into milk than into foam.');
```

### Creating the Stored Functions

Please create the following Stored Functions:

#### Find All Coffee Beverages

```SQL
create or replace
package types
as
    type cursorType is ref cursor;
end;
```

```SQL
create or replace
FUNCTION find_all_coffee_beverages return types.cursortype
AS
  l_cursor types.cursorType;
BEGIN
  OPEN l_cursor FOR SELECT "ID", "COFFEE_NAME", "COFFEE_DESCRIPTION" FROM "COFFEE_BEVERAGES";
  RETURN l_cursor;
END;
```
#### Find Specific Coffee Beverage

```SQL
create or replace
FUNCTION find_coffee(coffee_id IN integer)
  RETURN VARCHAR2 is description VARCHAR2(1000);
begin
    SELECT COFFEE_DESCRIPTION into description from COFFEE_BEVERAGES where ID=coffee_id;
    return description;
end;
```
### Execute the Sample

* running the "Main" class from within STS (Right-click on Main class --> Run As --> Java Application)
* or from the command line:
    
    $ gradlew :stored-procedures-oracle:run

You should see the following output:

	16:05:19.556 INFO  [main][org.springframework.integration.samples.storedprocedure.Main]
	=========================================================

	          Welcome to Spring Integration's
	     Stored Procedure/Function Sample for Oracle

	    For more information please visit:
	    http://www.springsource.org/spring-integration

	=========================================================
	Please enter a choice and press <enter>:
		1. Execute Sample 1 (Capitalize String)
		2. Execute Sample 2 (Coffee Service)
		q. Quit the application

Select **Opion 2**.

This should result in the following output:

	* Please enter 'list' and press <enter> to get a list of coffees.
	* Enter a coffee id, e.g. '1' and press <enter> to get a description.
	* Please press 'q + Enter' to quit the application.

This sample also periodically polls the Oracle database using a **Stored Procedure Inbound Channel Adapter**:

	16:06:46.669 INFO  [task-scheduler-1][org.springframework.integration.handler.LoggingHandler] [Payload=[CoffeeBeverage [id=1,...

--------------------------------------------------------------------------------

For help please take a look at the Spring Integration documentation:

http://www.springsource.org/spring-integration

[Stored Procedure Sample for PostgreSql]: https://github.com/ghillert/spring-integration-samples/tree/master/intermediate/stored-procedures-postgresql
