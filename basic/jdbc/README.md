Spring Integration - JDBC Sample
================================

# Overview

This sample provides example of how the Jdbc Adapters can be used.
The example presented covers the following two use cases

* Find a User detail from the database based on the name provided
* Create a new Person record in the table

The first example demonstrates the use of outbound gateway to search for a user record using the
spring integration's jdbc outbound gateway

The second example on other hand demonstrates how the jdbc outbound gateway be used to create a new 
Person record and then return the newly created Person record. 
This example demonstrates how to make use of the sql parameter source factory to extract 
the required values to be inserted/updated/selected in the query provided.

# Getting Started

You can run the application by either

* running the "Main" class from within STS (Right-click on Main class --> Run As --> Java Application)
* or from the command line:
    - mvn package
    - mvn exec:java

Make an appropriate choice for searching a User or creating a Person

For selecting the User, on the command prompt you can enter the following valid values and get a response back:

* 'a'
* 'b'
* 'foo'

For creating the person record, select the appropriate steps as prompted by the application

#Some details about the sample "Person Outbound Gateway"

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

