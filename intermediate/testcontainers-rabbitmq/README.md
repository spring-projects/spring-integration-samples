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

## Execute the tests

The Maven Wrapper is provided to execute the tests. 

```bash
$ ./mvnw clean test
```