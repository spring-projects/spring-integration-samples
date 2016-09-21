Spring Integration Samples
==========================

# Note

This (master) branch requires Spring Integration 4.1 or above. For samples running against earlier versions of Spring Integration, use the __SI4.0.x__ and __SI3.0.x__ branches.

# Introduction

Welcome to the **Spring Integration Samples** repository which provides **50+ samples** to help you learn [Spring Integration][]. To simplify your experience, the *Spring Integration* samples are split into 4 distinct categories:

* Basic
* Intermediate
* Advanced
* Applications
* DSL

Inside of each category you'll find a **README.md** file, which will contain a more detailed description of that category. Each sample also comes with its own **README.md** file explaining further details, e.g. how to run the respective sample.

| For additional samples, please also checkout the [Spring Integration Extensions][] project as it also provides numerous samples.

*Happy Integration!*

## Related GitHub projects

* [Spring Integration][]
* [Spring Integration Extensions][]
* [Spring Integration Templates][]
* [Spring Integration Java Dsl][]
* [Spring Integration Dsl Groovy][]
* [Spring Integration Dsl Scala][]
* [Spring Integration Pattern Catalog][]

## Community Sample Projects

* [Xavier Padró][]

# Categories

Below is a short description of each category.

## DSL

This directory holds demos/samples for Spring Integration 4.0 Java Configuration as well as the Java DSL Extension.

## Basic

This is a good place to get started. The samples here are technically motivated and demonstrate the bare minimum with regard to configuration and code to help you to get introduced to the basic concepts, API and configuration of Spring Integration. For example, if you are looking for an answer on how to wire a **Service Activator**  to a **Channel** or how to apply a **Gateway** to your message exchange or how to get started with using the **MAIL** or **XML** module, this would be the right place to find a relevant sample. The bottom line is that this is a good starting point.

* **amqp** - Demonstrates the functionality of the various **AMQP Adapters**
* **barrier** - Shows how to suspend a thread until some asynchronous event occurs
* **control-bus** - Demonstrates the functionality of the **Control Bus**
* **enricher** - This sample demonstrates how the Enricher components can be used
* **feed** - Demonstrates the functionality of the **Feed Adapter** (RSS/ATOM)
* **file** - Demonstrates aspects of the various File Adapters (e.g. **File Inbound/Outbound Channel Adapters**, file **polling**)
* **ftp** - Demonstrates the **FTP support** available with Spring Integration
* **helloworld** - Very simple starting example illustrating a basic message flow (using **Channel**, **ServiceActivator**, **QueueChannel**)
* **http** - Demonstrates request/reply communication when using a pair of **HTTP Inbound/Outbound gateways**
* **jdbc** - Illustrates the usage of the Jdbc Adapters, including object persistence and retrieval
* **jms** - Demonstrates **JMS** support available with Spring Integration
* **jmx** - Demonstrates **JMX** support using a **JMX Attribute Polling Channel** and **JMX Operation Invoking Channel Adapter**
* **jpa** - Shows the usage of the JPA Components can be used
* **mail** - Example showing **IMAP** and **POP3** support
* **mqtt** - Demonstrates the functionality of inbound and outbound **MQTT Adapters**
* **mongodb** - Shows how to persist a Message payload to a **MongoDb** document store and how to read documents from **MongoDb**
* **oddeven** - Example combining the functionality of **Inbound Channel Adapter**, **Filter**, **Router** and **Poller**
* **jpa** - This sample illustrates how the JPA Components can be used
* **quote** - Example demoing core EIP support using **Channel Adapter (Inbound and Stdout)**, **Poller** with Interval Trigers, **Service Activator**
* **sftp** - Demonstrating SFTP support using **SFTP Inbound / Outbound Channel Adapters**
* **tcp-amqp** - Demonstrates basic functionality of bridging the **Spring Integration TCP Adapters** with **Spring Integration AMQP Adapters**
* **tcp-client-server** - Demonstrates socket communication using **TcpOutboundGateway**, **TcpInboundGateway** and also uses a **Gateway** and a **Service Activator**
* **testing-examples** - A series of test cases that show techniques to **test** Spring Integration applications.
* **twitter** - Illustrates Twitter support using the **Twitter Inbound Channel Adapter**, **Twitter Inbound Search Channel Adapter**, **Twitter Outbound Channel Adapter**
* **ws-inbound-gateway** - Example showing basic functionality of the **Web Service Gateway**
* **ws-outbound-gateway** - Shows outbound web services support using the **Web Service Outbound Gateway**, **Content Enricher**, Composed Message Processor (**Chain**)
* **xml** - Example demonstrates various aspects of the **Xml** support using an **XPath Splitter**, **XPath Router**, **XSLT Transformer** as well as **XPath Expression** support
* **xmpp** - Show the support for [**XMPP**](http://en.wikipedia.org/wiki/Extensible_Messaging_and_Presence_Protocol) (formerly known as Jabber) using e.g. GoogleTalk

## Intermediate

This category targets developers who are already more familiar with the Spring Integration framework (past getting started), but need some more guidance while resolving more advanced technical problems that you have to deal with when switching to a Messaging architecture. For example, if you are looking for an answer on how to handle errors in various scenarios, or how to properly configure an **Aggregator** for the situations where some messages might not ever arrive for aggregation, or any other issue that goes beyond a basic understanding and configuration of a particular component to address "what else you can do?" types of problems, this would be the right place to find relevant examples.

* **async-gateway** - Usage example of an asynchronous **Gateway**
* **dynamic-poller** - Example shows usage of a **Poller** with a custom **Trigger** to change polling periods at runtime
* **async-gateway** - Example shows usage of an **Asynchronous Gateway**
* **errorhandling** - Demonstrates basic **Error Handling** capabilities of Spring Integration
* **file-processing** - Sample demonstrates how to wire a message flow to process files either sequentially (maintain the order) or concurrently (no order).
* **mail-attachments** - Demonstrates the processing of email attachments
* **monitoring** The project used in the *[Spring Integration Management and Monitoring Webinar](http://www.springsource.org/node/3598)* Also available on the *[SpringSourceDev YouTube Channel](http://www.youtube.com/SpringSourceDev)*
* **multipart-http** - Demonstrates the sending of HTTP multipart requests using Spring's **RestTemplate** and a Spring Integration **Http Outbound Gateway**
* **rest-http** - This sample demonstrates how to send an HTTP request to a Spring Integration's HTTP service while utilizing Spring Integration's new HTTP Path usage. This sample also uses Spring Security for HTTP Basic authentication. With HTTP Path facility, the client program can send requests with URL Variables.
* **retry-and-more** Provides samples showing the application of MessageHandler Advice Chains to endpoints - retry, circuit breaker, expression evaluating
* **splitter-aggregator-reaper** A demonstration of implementing the Splitter and Aggregator *Enterprise Integration Patterns* (EIP) together. This sample also provides a concrete example of a [message store reaper][] in action.
* **stored-procedures-derby**  Provides an example of the stored procedure Outbound Gateway using *[Apache Derby](http://db.apache.org/derby/)*
* **stored-procedures-ms** Provides an example of the stored procedure Outbound Gateway using *Microsoft SQL Server*
* **stored-procedures-oracle** Provides an example of the stored procedure Outbound Gateway using *ORACLE XE*
* **stored-procedures-postgresql** Provides an example of the stored procedure Outbound Gateway using *[PostgreSQL](http://www.postgresql.org/)*
* **tcp-client-server-multiplex** - Demonstrates the use of *Collaborating Channel Adapters*
* **travel** - More sophisticated example showing the retrieval of weather (SOAP Web Service) and traffic (HTTP Service) reports using real services
* **tx-synch** Provides a sample demonstrating the use of transaction synchronization, renaming an input file to a different filename, depending on whether the transaction commits, or rolls back.

## Advanced

This category targets advanced developers who are quite familiar with Spring Integration but are looking to address a specific custom need by extending the Spring Integration public API. For example, if you are looking for samples showing how to implement a custom **Channel** or **Consumer** (event-based or polling-based), or you are trying to figure out what is the most appropriate way to implement a custom **BeanParser** on top of the Spring Integration BeanParser hierarchy when implementing a custom namespace, this would be the right place to look. Here you can also find samples that will help you with adapter development. Spring Integration comes with an extensive library of adapters that allow you to connect remote systems with the Spring Integration messaging framework. However you might have a need to integrate with a system for which the core framework does not provide an adapter, so you have to implement your own. This category would include samples showing you how to implement various adapters.

* **advanced-testing-examples** - Example test cases that show advanced techniques to test Spring Integration applications
* **dynamic-ftp** - Demonstrates one technique for sending files to dynamic destinations.
* **dynamic-tcp-client** - Demonstrates a technique for dynamically creating TCP clients.

## Applications

This category targets developers and architects who have a good understanding of Message-Driven architecture and Enterprise Integration Patterns, and have an above average understanding of Spring and Spring integration and who are looking for samples that address a particular business problem. In other words, the emphasis of samples in this category is '**business use cases**' and how they can be solved via a Messaging architecture and Spring Integration in particular. For example, if you are interested to see how a Loan Broker process or Travel Agent process could be implemented and automated via Spring Integration, this would be the right place to find these types of samples.

* **cafe** - Emulates a simple operation of a coffee shop combining various Spring Integration adapters (Including **Router** and **Splitter**) see [Appendix A of the reference documentation](http://static.springsource.org/spring-integration/docs/latest-ga/reference/html/samples.html) for more details. Implementations are provided for:
  - AMQP
  - JMS
  - In memory channels
* **cafe-scripted** - Scripted implementation of the classic **cafe** sample application. Supports **JavaScript**, **Groovy**, **Ruby**, and **Python**.
* **loan-broker** - Simulates a simple banking application (Uses **Gateway**, **Chain**, **Header Enricher**, **Recipient List Router**, **Aggregator**) see [Appendix A of the reference documentation](http://docs.spring.io/spring-integration/reference/html/samples.html) for more details
* **loanshark** This extension to the loan broker sample shows how to exchange messages between Spring Integration applications (and other technologies) using **UDP**.
  **file-split-ftp** - Reads a file; splits into 3 based on contents; sends files over ftp; sends email with results.

#Contributing

See the [Spring Integration Contributor Guidelines](https://github.com/spring-projects/spring-integration/blob/master/CONTRIBUTING.adoc) for information about how to contribute to this repository.

#Resources

For more information, please visit the Spring Integration website at: [http://projects.spring.io/spring-integration/](http://projects.spring.io/spring-integration/)

[Spring Integration]: https://github.com/spring-projects/spring-integration
[Spring Integration Extensions]: https://github.com/spring-projects/spring-integration-extensions
[Spring Integration Templates]: https://github.com/spring-projects/spring-integration-templates/tree/master/si-sts-templates
[Spring Integration Java Dsl]: https://github.com/spring-projects/spring-integration-java-dsl
[Spring Integration Dsl Groovy]: https://github.com/spring-projects/spring-integration-dsl-groovy
[Spring Integration Dsl Scala]: https://github.com/spring-projects/spring-integration-dsl-scala
[Spring Integration Pattern Catalog]: https://github.com/spring-projects/spring-integration-pattern-catalog

[message store reaper]: http://static.springsource.org/spring-integration/api/org/springframework/integration/store/MessageGroupStoreReaper.html

[Xavier Padró]: https://github.com/xpadro/spring-integration
