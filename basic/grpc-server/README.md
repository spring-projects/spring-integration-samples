gRPC Server Sample
==================

This sample demonstrates how to use Spring Integration's gRPC support with an **Inbound Gateway** to handle gRPC requests.

The sample implements a gRPC server that exposes a `HelloWorldService` with multiple RPC methods demonstrating different communication patterns:

* **SayHello** - Unary RPC (single request, single response)
* **StreamSayHello** - Server streaming RPC (single request, multiple responses)

## Running the Sample

Start the gRPC server using Gradle:

    $ gradlew :grpc-server:bootRun

The server will start on port 9090 by default (configured in `application.properties`).

#### Using an IDE such as SpringSource Tool Suite™ (STS)

In STS (Eclipse), go to package **org.springframework.integration.samples.grpc**, right-click **Application** and select **Run as** --> **Java Application** (or Spring Boot Application).

## Testing the Server

You can test the server using the gRPC client sample or tools like `grpcurl`:

```bash
# List available services
grpcurl -plaintext localhost:9090 list

# Call the SayHello method
grpcurl -plaintext -d '{"name": "World"}' localhost:9090 integration.grpc.hello.HelloWorldService/SayHello

# Call the StreamSayHello method (server streaming)
grpcurl -plaintext -d '{"name": "World"}' localhost:9090 integration.grpc.hello.HelloWorldService/StreamSayHello
```

## Resources

* [Spring Integration gRPC Documentation](https://docs.spring.io/spring-integration/reference/grpc.html)
