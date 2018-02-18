Hello World Sample
==================

This is an obvious place to get started. This sample project contains 2 basic sample applications:

* Hello World
* Poller Application

## Hello World

The Hello World application demonstrates a simple message flow represented by the diagram below:

    Message -> Channel -> ServiceActivator -> QueueChannel 

To run the sample simply execute **HelloWorldApp** in package **org.springframework.integration.samples.helloworld**. 
You can also execute that class using the [Gradle](http://www.gradle.org):

    $ gradlew :helloworld:runHelloWorldApp

You should see the following output:

    INFO : org.springframework.integration.samples.helloworld.HelloWorldApp - ==> HelloWorldDemo: Hello World

## Poller Application

This simple application will print out the current system time twice every 20 seconds.

More specifically, an **Inbound Channel Adapter** polls for the current system time 2 times every 20 seconds (20000 milliseconds). The resulting message contains as payload the time in milliseconds and the message is sent to a **Logging Channel Adapter**, which will print the time to the command prompt.

To run the sample simply execute **PollerApp** in package **org.springframework.integration.samples.helloworld**. 
You can also execute that class using the [Gradle](http://www.gradle.org):

    $ gradlew :helloworld:runPollerApp

You should see output like the following:

    INFO : org.springframework.integration.samples.helloworld - 1328892135471
    INFO : org.springframework.integration.samples.helloworld - 1328892135524

