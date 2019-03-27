Spring Integration - AMQP Sample
================================

# Overview

This sample demonstrates basic functionality of the **Spring Integration AMQP Adapter**, which uses the [Advanced Message Queuing Protocol](https://www.amqp.org/) (AMQP) to send and retrieve messages. As AMQP Broker implementation the sample uses [RabbitMQ](https://www.rabbitmq.com/).

Once the application is started, you enter some text on the command prompt and a message containing that entered text is dispatched to the AMQP queue. In return that message is retrieved by Spring Integration and then printed to the console. 

> In order to run the example you will need a running  instance of RabbitMQ. A local installation with just the basic defaults will be sufficient. Please visit: [https://www.rabbitmq.com/install.html](https://www.rabbitmq.com/install.html) for detailed installation procedures.

# How to Run the Sample

If you imported the example into your IDE, you can just run class **org.springframework.integration.samples.amqp.SampleSimple**. For example in [SpringSource Tool Suite](https://www.springsource.com/developer/sts) (STS) do:

* Right-click on SampleSimple class --> Run As --> Java Application

Alternatively, you can start the sample from the command line:

* ./gradlew :amqp:runSimple

Enter some data (e.g. 'foo') on the console; you will see a [tapInbound] log and 'Received: foo'.

Ctrl-C to terminate.

The __SamplePubConfirmsReturns__ class is similar, but demonstrates publisher confirms and returns.

* Right-click on SamplePubConfirmsReturns class --> Run As --> Java Application

Or:

* ./gradlew :amqp:runPubConfirmsReturns

When you enter a message in the console you will see the message received, together with a send confirmation:

````
foo
Received: foo
foo sent ok
````

When you enter 'fail', the message is sent with a bad routing key; you will see the message is sent ok, but returned because it is not routable:

````
fail
fail returned:NO_ROUTE
fail sent ok
````

When you enter 'nack', the message is sent to a non-existent exchange; the broker reacts to this by closing the channel with an error and Spring AMQP generates a Nack:

````
nack
nack send failed (nack)
11:54:00.818 ERROR [pool-1-thread-1][org.springframework.amqp.rabbit.connection.CachingConnectionFactory] Channel shutdown: channel error; protocol method: #method<channel.close>(reply-code=404, reply-text=NOT_FOUND - no exchange 'badExchange' in vhost '/', class-id=60, method-id=40)
````

Ctrl-C to terminate.

# Used Spring Integration components

### Spring Integration Modules (Maven/Gradle dependencies)

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

* [https://docs.spring.io/spring-integration/reference/html/#amqp](https://docs.spring.io/spring-integration/reference/html/#amqp)

Some further resources:

* RabbitMQ -  [https://www.rabbitmq.com/](https://www.rabbitmq.com/)
* Spring AMQP - [https://www.springsource.org/spring-amqp](https://www.springsource.org/spring-amqp)
