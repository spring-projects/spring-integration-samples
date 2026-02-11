gRPC Client Sample
==================

This sample demonstrates how to use Spring Integration's gRPC support with an **Outbound Gateway** to make gRPC requests to a server.

The sample implements a gRPC client that calls a `HelloWorldService` demonstrating different communication patterns:

* **SayHello** - Unary RPC (single request, single response)
* **StreamSayHello** - Server streaming RPC (single request, multiple responses)

The client automatically executes both examples on startup using `ApplicationRunner` beans.

## Running the Sample

**Important:** Start the gRPC server first (see the grpc-server sample) and it must have the same gRPC proto files as the client.

Then start the gRPC client using Gradle:

    $ gradlew :grpc-client:bootRun

#### Using an IDE such as SpringSource Tool Suite™ (STS)

In STS (Eclipse), go to package **org.springframework.integration.samples.grpc**, right-click **Application** and select **Run as** --> **Java Application** (or Spring Boot Application).

### Output

The client will automatically send requests to the server and display the responses:

```
Single response reply: message: "Hello Jack"

Stream received reply: Hello Jack
Stream received reply: Hello again!
```

## Configuration

The gRPC server connection is configured in `application.properties`:

```properties
grpc.server.host=localhost
grpc.server.port=9090
```

## Resources

* [Spring Integration gRPC Documentation](https://docs.spring.io/spring-integration/reference/grpc.html)
