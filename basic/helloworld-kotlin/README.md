Hello World Sample
==================

This is the Kotlin version of the helloworld Java sample using Kotlin DSL. This sample project contains 2 basic sample applications:

* Hello World
* Poller Application

## Hello World

The Hello World application demonstrates a simple message flow represented by the diagram below:

    Message -> Channel -> ServiceActivator -> QueueChannel

To run the sample simply execute **HelloWorldApp** in package **org.springframework.integration.samples.helloworld**.
You can also execute that class using the [Gradle](https://www.gradle.org):

    $ gradlew :helloworld-kotlin:runHelloWorldApp

You should see the following output:

    INFO : org.springframework.integration.samples.helloworld.HelloWorldApp - ==> HelloWorldDemo: Hello World

## Poller Application

This simple application will print out the current system time twice every 20 seconds.

More specifically, an **Inbound Channel Adapter** polls for the current system time 2 times every 20 seconds (20000 milliseconds). The resulting message contains as payload the time in milliseconds and the message is sent to a **Logging Channel Adapter**, which will print the time to the command prompt.

To run the sample simply execute **PollerApp** in package **org.springframework.integration.samples.helloworld**.
You can also execute that class using the [Gradle](https://www.gradle.org):

    $ gradlew :helloworld-kotlin:runPollerApp

You should see output like the following:

[task-scheduler-1][org.springframework.integration.samples.helloworld] GenericMessage [payload=1763478785243, headers={id=8f93b18a-063a-5e9f-4708-2ed1d04a1566, timestamp=1763478785244}]
[task-scheduler-1][org.springframework.integration.samples.helloworld] GenericMessage [payload=1763478785248, headers={id=aa37e9c4-95d1-538c-a6cd-d400bb1474bf, timestamp=1763478785248}]
