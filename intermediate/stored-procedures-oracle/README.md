Spring Integration - Stored Procedure Example - Oracle
================================================================================

# Overview

This example provides a simple example using the stored procedure outbound gateway
adapter. This example will call an Oracle Stored Procedure as well as an Oracle Function using the StoredProc Outbound Gateway.

This sample was tested against: **Oracle Database Express Edition 11g Release 2** (which can be downloaded and used for free).
Nevertheless, the example should work with other versions as well.

# Setup

##Pre-requisites

- Access to a Oracle or Oracle XE database instance
- Install Oracle JDBC Driver to your local Maven repository (~/.m2)

##JDBC Driver installation

- Go to [http://www.oracle.com/technetwork/indexes/downloads/index.html](http://www.oracle.com/technetwork/indexes/downloads/index.html)
- Under "JDBC Drivers", download the appropriate driver relevant to your Oracle and JDK version (This sample was tested using 
"Oracle Database 11g Release 2 JDBC Drivers")
- Once downloaded, install the driver to your local Maven repository:

        $ mvn install:install-file -DgroupId=com.oracle -DartifactId=ojdbc6 -Dversion=11.2.0.3 -Dpackaging=jar -Dfile=~/dev/ojdbc6.jar -DgeneratePom=true

- Now you can add the dependency to the Maven pom.xml file. Please check the pom.xml for this project and verify that it matches your installed Oracle JDBC driver

        <dependency>
            <groupId>com.oracle</groupId>
            <artifactId>ojdbc6</artifactId>
            <version>11.2.0.3</version>
        </dependency>

## Setting Up Oracle

### Create Tablespace

	CREATE TABLESPACE procedure_test
	DATAFILE 'c:/data/procedure_test.dbf'
	   SIZE 10M 
	   AUTOEXTEND ON NEXT 10M
	   MAXSIZE 100M;

### Create User

	CREATE USER storedproc
	       IDENTIFIED BY storedproc
	       DEFAULT TABLESPACE procedure_test
	       TEMPORARY TABLESPAC temp;  

### Grant Rights

	GRANT CREATE SESSION,CREATE TABLE,CREATE VIEW,CREATE SEQUENCE TO storedproc;
	ALTER USER storedproc QUOTA unlimited ON procedure_test;

### Creating the Stored Procedure

create or replace
PROCEDURE CAPITALIZE_STRING(inoutString IN OUT VARCHAR) IS
BEGIN
    SELECT upper(inoutString) INTO inoutString from dual ;
END;

### Creating the Function

create or replace
FUNCTION GET_COOL_NUMBER
   RETURN NUMBER 
   IS cool_number NUMBER(11,2);
BEGIN 
   cool_number := 12345;
   RETURN cool_number; 
END;

## Setting Up Oracle

You may have to update the Oracle DB properties in:

    /src/main/resources/META-INF/spring/integration/spring-integration-context.xml

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

# Run the Sample
        
* running the "Main" class from within STS (Right-click on Main class --> Run As --> Java Application)
* or from the command line:
    - mvn package
    - mvn exec:java

--------------------------------------------------------------------------------

For help please take a look at the Spring Integration documentation:

http://www.springsource.org/spring-integration

