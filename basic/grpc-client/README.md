gRPC Client Sample
==================

This example demonstrates the use of Spring Integration's **gRPC Outbound Gateway** for client-side communication.

It uses Java configuration and the Spring Integration DSL to configure the gRPC client adapters.

The sample demonstrates two gRPC communication patterns:

1. **Single Response** - Simple request/reply pattern where the client sends a request and receives a single response
2. **Streaming Response** - The client sends a request and receives a stream of responses

## Prerequisites

This sample requires a gRPC server to be running. You can use any gRPC server that implements the `Simple` service defined in `hello.proto`, or run the test which uses an in-process gRPC server.

## Running the sample

### Command Line Using Gradle

To run the sample using Gradle, execute:

    $ gradlew :grpc-client:bootRun

This will start the Spring Boot application using the [Spring Boot Gradle Plugin](https://docs.spring.io/spring-boot/docs/current/gradle-plugin/reference/html/).

## Configuration

The application expects a gRPC server to be available. By default, it connects to `localhost:9090`. You can configure the server address in `application.properties`:

```properties
spring.grpc.client.channels.spring-integration.address=static://localhost:9090
```

## Output

The application demonstrates two patterns:

### Single Response Pattern

The client sends a `HelloRequest` with name "Jack" and receives a single `HelloReply`:

    Single response reply: message: Hello Jack

### Streaming Response Pattern

The client sends a `HelloRequest` with name "Jane" and receives multiple `HelloReply` messages:

    Stream received reply: Hello Jane
    Stream received reply: Hello Again

## Running the Tests

The test uses an in-process gRPC server and does not require an external server:

    $ gradlew :grpc-client:test

The test validates both single-response and streaming-response patterns.
