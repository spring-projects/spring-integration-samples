Testcontainers - RabbitMQ Sample
==================================

Testcontainers is a Java library that supports JUnit tests, providing lightweight, throwaway instances of common databases, Selenium web browsers, or anything else that can run in a Docker container.

[Testcontainers](https://www.testcontainers.org/)

This sample demonstrates how to setup and configure the embedded RabbitMQ Docker container for use in testing Spring Integration projects that require RabbitMQ.

A simple `IntegrationFlow` is setup to establish a message is published to an `OutboundGateway` and handled by some _downstream_ process.
It expects a response to come back on some reply-to channel established by the `RabbitTemplate`.

In the real world scenario, the Topic Exchange and Queues would have already been established by the _downstream_ application. 
To aid in testing, when the RabbitMQ Testcontainer comes up, the correct Topic Exchange and Queues are created and provide simple message and handling and responses.

**Note**: These tests take a bit longer run to allow time for the Docker image to spin up and tear down.

## Embedded RabbitMQ

The project dependency adds Spring Boot Autoconfiguration for test containers that automatically sets up and configures the embedded docker image

```groovy
testCompile "com.playtika.testcontainers:embedded-rabbitmq:1.42"
```

Configuration is performed in `src/test/resources/application.yml` to point the Spring Boot RabbitMQ Autoconfiguration to the properties exposed by the library

```yml
spring:
  rabbitmq:
    host: ${embedded.rabbitmq.host}
    port: ${embedded.rabbitmq.port}
    username: ${embedded.rabbitmq.user}
    password: ${embedded.rabbitmq.password}
    virtual-host: ${embedded.rabbitmq.vhost}
```

[testcontainers-spring-boot](https://github.com/testcontainers/testcontainers-spring-boot)

## Architecture

The `IntegrationFlow` is setup to publish a message to a RabbitMQ `TopicExchange`. A `RoutingKey` is used to direct this message to an appropriate `Queue`.
The result is sent back to the _flow_ on a separate reply-to `Queue`. 

It is deliberately setup this way to model a real-world scenario we ran into. The calling application is a Spring Boot application and the downstream application is a Python ML application.

## Execute the tests

The Gradle Wrapper is provided to execute the tests. 

```bash
$ ./gradlew :testcontainers-rabbitmq:test
```