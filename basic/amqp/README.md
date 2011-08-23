Spring Integration - AMQP Sample
================================

# Overview

This sample demonstrates basic functionality of the **Spring Integration AMQP Adapter**, which uses the [Advanced Message Queuing Protocol](http://www.amqp.org/) (AMQP) to send and retrieve messages. As AMQP Broker implementation the sample uses [RabbitMQ](http://www.rabbitmq.com/).

Once the application is started, you enter some text on the command prompt and a message containing that entered text is dispatched to the AMQP queue. In return that message is retrieved by Spring Integration and then printed to the console. 

> In order to run the example you will need a running  instance of RabbitMQ. A local installation with just the basic defaults will be sufficient. Please visit: [http://www.rabbitmq.com/install.html](http://www.rabbitmq.com/install.html) for detailed installation procedures.

# How to Run the Sample

If you imported the example into your IDE, you can just run class **org.springframework.integration.samples.amqp.Main**. For example in [SpringSource Tool Suite](http://www.springsource.com/developer/sts) (STS) do:

* Right-click on Main class --> Run As --> Java Application

Alternatively, you can start the sample from the command line ([Maven](http://maven.apache.org/) required):

* mvn package
* mvn exec:java

# Used Spring Integration components

### Spring Integration Modules (Maven dependencies)

* spring-integration-core
* spring-integration-amqp
* spring-integration-stream

### Spring Integration Adapters

* int-stream:stdin-channel-adapter
* **int-amqp:outbound-channel-adapter**
* **int-amqp:inbound-channel-adapter**
* int-stream:stdout-channel-adapter
* int:poller
* int:channel
* int:interceptors
* int:wire-tap
* logging-channel-adapter

# Resources

For further help please take a look at the Spring Integration documentation:

* [http://static.springsource.org/spring-integration/reference/htmlsingle/#amqp](http://static.springsource.org/spring-integration/reference/htmlsingle/#amqp)

Some further resources:

* RabbitMQ -  [http://www.rabbitmq.com/](http://www.rabbitmq.com/)
* Spring AMQP - [http://www.springsource.org/spring-amqp](http://www.springsource.org/spring-amqp)
