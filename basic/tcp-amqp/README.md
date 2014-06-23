Spring Integration - TCP-AMQP Sample
====================================

# Overview

This sample demonstrates basic functionality of bridging **Spring Integration TCP Adapters** with **Spring Integration AMQP Adapters**.

Once the application is started, you enter some text in a telnet session and the data is written to an AMQP queue, which is then consumed and the result echoed to a [netcat][] session.

    telnet->tcp-inbound-adapter->rabbit->tcp-outbound-adapter->netcat

telnet: http://en.wikipedia.org/wiki/Telnet
netcat: http://en.wikipedia.org/wiki/Netcat

> In order to run the example you will need a running  instance of RabbitMQ. A local installation with just the basic defaults will be sufficient. Please visit: [http://www.rabbitmq.com/install.html](http://www.rabbitmq.com/install.html) for detailed installation procedures.

# How to Run the Sample

## Start netcat

In a terminal window start [netcat][], listening on port *11112*:

    netcat -l -p 11112

## Start the Application

If you imported the example into your IDE, you can just run class **org.springframework.integration.samples.tcpamqp.Main**. For example in [SpringSource Tool Suite](http://www.springsource.com/developer/sts) (STS) do:

* Right-click on Main class --> Run As --> Java Application

Alternatively, you can start the sample from the command line 
([Gradle Application Plugin](http://www.gradle.org/docs/current/userguide/application_plugin.html)):

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

* [http://static.springsource.org/spring-integration/reference/htmlsingle/#amqp](http://static.springsource.org/spring-integration/reference/htmlsingle/#amqp)

Some further resources:

* RabbitMQ -  [http://www.rabbitmq.com/](http://www.rabbitmq.com/)
* Spring AMQP - [http://www.springsource.org/spring-amqp](http://www.springsource.org/spring-amqp)

[netcat]: http://en.wikipedia.org/wiki/Netcat
