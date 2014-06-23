Spring Integration - JPA Sample
================================

# Overview

This sample illustrates how the JPA Components can be used. The example presented covers the following use cases

* List all people from the database
* Create a new Person record in the database

The first example demonstrates the use of an JPA Outbound gateway to retrieve a list of people. The second example uses an JPA Outbound Gateway in order to create a new Person record and then return the newly created Person record. 

You have the option to choose between the following 3 persistence providers:

* [Hibernate](http://www.hibernate.org/)
* [OpenJPA](http://openjpa.apache.org/)
* [EclipseLink](http://www.eclipse.org/eclipselink/)

# Getting Started

Hibernate works out of the box and there are 2 options on how to execute the sample: 

* running the "Main" class from within STS (Right-click on Main class --> Run As --> Java Application)
* or from the command line:
    $ gradlew :jpa:run

For **OpenJPA** and **EclipseLink** to work, you must provide a [Java Agent](http://docs.oracle.com/javase/6/docs/api/java/lang/instrument/package-summary.html). 
When using the Gradle Application Plugin, this is taken care of for you behind the scenes automatically. However, 
when running the sample from within STS start the Main class with the following JVM flags:

    -javaagent:/path/to/.m2/repository/org/springframework/spring-instrument/3.1.1.RELEASE/spring-instrument-3.1.1.RELEASE.jar
    -javaagent:/path/to/.m2/repository/org/apache/openjpa/openjpa/2.2.0/openjpa-2.2.0.jar

With these flags you will be able to use all 3 persistence providers at once.

# Resources

For help please take a look at the Spring Integration documentation:

http://www.springsource.org/spring-integration

