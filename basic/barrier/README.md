Barrier Sample
==============

This example demonstrates the use of a process barrier component to suspend a thread until some asynchronous operation 
completes. It uses an **HTTP Inbound Gateway**, splits the request, sends the splits to rabbitmq and then waits for
the publisher confirms. Finally, the results are returned to the caller.

The sample is a Spring Boot application that loads 2 contexts:

* Client - Sends **A,B,C** to the server
* Server - Web application

The server context has 3 integration flows:

```
http -> splitter -> amqp
     |-> barrier

amqp(Acks) -> aggregator -> barrier (release)

qmqp(inbound) -> nullChannel
```

The last flow drains the messages and allows the auto-delete queue to be removed when the application is closed.

## Running the sample


    $ gradlew :barrier:run

This will package the application and run it using the [Gradle Application Plugin](http://www.gradle.org/docs/current/userguide/application_plugin.html)

#### Using an IDE such as SpringSource Tool Suite™ (STS)

In STS (Eclipse), go to package **org.springframework.integration.samples.barrier**, right-click **Application** and select **Run as** --> **Java Application** (or Spring Boot Application).

### Output
  
The gateway (**client**) initiates a simple request posting "A,B,C" to the **server** and the **server** responds with the results.
You should see the following output from the server:
   
    ++++++++++++ Replied with: Result: A: ack=true, B: ack=true, C: ack=true ++++++++++++


