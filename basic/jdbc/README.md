Spring Integration - JDBC Sample
================================

# Overview

This sample provides example of how the Jdbc Adapters can be used.

# Getting Started

You can run the application by either

* running the "Main" class from within STS (Right-click on Main class --> Run As --> Java Application)
* or from the command line:
    - mvn package
    - mvn exec:java

Currently one example exists. On the command prompt you can enter the following valid values and get a response back:

* 'a'
* 'b'
* 'foo'

#Sample "Person Outbound Gateway"

We use the outbound gateway to insert records in a Person table based on the values contained
in the message payload that is received over the channel to the adapter.

The following are used to configure the gateway
* The request and reply channels
* The data source for the database
* The the update/insert statement to be executed.
* Optional request SQL Parameter source factory
* The select query to be executed after the insert is done
* Optional reply SQL Parameter source factory
* RowMapper if you intend to map the ResultSet to your custom object

The following sequence of events happen when we invoke the createPerson method on the gateway
* The parameter of type Person is sent as a payload of a message over the reply-channel
* The outbound gateway reads this message and extracts the payload
* It then executes the given insert statement, the values to be inserted are derived from the payload 
	request-sql-parameter-source-factory provided is used to generate a parameter source.
* Since we expect an identity column to be generated, keys-generated is set to true
* The result of the insert is then use to create a reply sql parameter source, 
	the factory reply-sql-parameter-source-factory generates the required parameter source
* The select query provided is executed
* The ResultMapper is used to convert this ResultSet to a Person object
* The Person object is then sent as a payload of the Message over the reply channel.
* The Person payload is extracted from the Message and returned to the calling application.

For executing the program and see the results, execute the junit test case
org.springframework.integration.samples.jdbc.OutboundGatewayTest

# Resources

For help please take a look at the Spring Integration documentation:

http://www.springsource.org/spring-integration

