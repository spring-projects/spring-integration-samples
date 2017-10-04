Spring Integration - JPA Sample
================================

# Overview

This sample illustrates how the JPA Components can be used. The example presented covers the following use cases

* List all people from the database
* Create a new Person record in the database

The first example demonstrates the use of an JPA Outbound gateway to retrieve a list of people. The second example uses an JPA Outbound Gateway in order to create a new Person record and then return the newly created Person record.

Hibernate works out of the box and there are 2 options on how to execute the sample:

* running the "Main" class from within STS (Right-click on Main class --> Run As --> Java Application)
* or from the command line:
    $ gradlew :jpa:run

# Resources

For help please take a look at the Spring Integration documentation:

http://www.springsource.org/spring-integration

