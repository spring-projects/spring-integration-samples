Spring Integration - TCP-AMQP Sample
====================================

# Overview

This sample demonstrates basic functionality of bridging **Spring Integration TCP Adapters** with **Spring Integration AMQP Adapters**.

Once the application is started, you enter some text in a telnet session and the data is written to an AMQP queue, which is then consumed and the result echoed to a [netcat][] session.

    telnet->tcp-inbound-adapter->rabbit->tcp-outbound-adapter->netcat

telnet: https://en.wikipedia.org/wiki/Telnet
netcat: https://en.wikipedia.org/wiki/Netcat

> In order to run the example you will need a running  instance of RabbitMQ. A local installation with just the basic defaults will be sufficient. Please visit: [https://www.rabbitmq.com/install.html](https://www.rabbitmq.com/install.html) for detailed installation procedures.

# How to Run the Sample

## Start netcat

In a terminal window start [netcat][], listening on port *11112*:

    netcat -l -p 11112

## Start the Application

If you imported the example into your IDE, you can just run class **org.springframework.integration.samples.tcpamqp.Main**. For example in [SpringSource Tool Suite](https://www.springsource.com/developer/sts) (STS) do:

* Right-click on Main class --> Run As --> Java Application

Alternatively, you can start the sample from the command line 
([Gradle Application Plugin](https://www.gradle.org/docs/current/userguide/application_plugin.html)):

    $ gradlew :tcp-amqp:run

## Run Telnet

In another terminal window, telnet to localhost:11111

    telnet localhost 11111

Data typed into the telnet terminal will be echoed to the [netcat][] terminal, via the rabbit queue.

# Used Spring Integration components

### Spring Integration Modules (Maven dependencies)

* spring-integration-core
* spring-integration-amqp
* spring-integration-tcp

# Resources

For further help please take a look at the Spring Integration documentation:

* [https://docs.spring.io/spring-integration/reference/html/#amqp](https://docs.spring.io/spring-integration/reference/html/#amqp)

Some further resources:

* RabbitMQ -  [https://www.rabbitmq.com/](https://www.rabbitmq.com/)
* Spring AMQP - [https://www.springsource.org/spring-amqp](https://www.springsource.org/spring-amqp)

[netcat]: https://en.wikipedia.org/wiki/Netcat
