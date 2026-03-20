gRPC Server Sample
==================

This example demonstrates the use of **gRPC Inbound Gateway** with Spring Integration.

It uses Java configuration and showcases various gRPC communication patterns including:

* Unary RPC - client sends a single request and receives a single response
* Server streaming RPC - client sends a single request and receives a stream of responses

The sample uses a simple greeting service defined in a Protocol Buffers (protobuf) file that demonstrates how Spring Integration can handle gRPC service implementations through the `GrpcInboundGateway`.

## Running the sample

### Command Line Using Gradle

    $ gradlew :grpc-server:bootRun

This will start the gRPC server application using Spring Boot.

## Testing the sample

The sample includes JUnit tests that demonstrate the various gRPC communication patterns:

    $ gradlew :grpc-server:test

### Output

The tests use an in-process gRPC server for testing, configured via `spring.grpc.server.inprocess.name` and `spring.grpc.client.default-channel.address`. When running the unary test, a request with name "Jack" returns:

    Hello Jack

When running the server streaming test, a request with name "Jane" returns multiple responses:

    Hello Stream Jane
    Hello again!
