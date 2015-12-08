Barrier Sample
==============

This example demonstrates the use of a process barrier component to suspend a thread until some asynchronous operation
completes. The first example uses an **HTTP Inbound Gateway**, splits the request, sends the splits to rabbitmq and then
waits for
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

### Error Handling

The second example uses a simple gateway to launch some asynchronous tasks and waits for those tasks to complete.

It shows how you might return an exception to the caller if one or more of those tasks fail.

An aggregator is used to aggregate the results; if there are no errors, the results are returned; if one or more
errors occurred, an exception is sent to release the barrier; this is thrown to the caller and has all the consolidated
results in a property.

You can run this example from an IDE, such as STS using the technique above; in this case, the class is
**ErrorHandlingApplication** in the **org.springframework.integration.samples.barrier** package.

It sends a list of integers to the flow:

    [2, 0, 2, 0, 2]

The zeros should fail and in stderr you should see the results:

    ConsolidatedResultsException
    [results=
    [5
    org.springframework.integration.transformer.MessageTransformationException:
      Failed to transform Message; nested exception is org.springframework.messaging.MessageHandlingException:
      Expression evaluation failed: 10 / payload; nested exception is java.lang.ArithmeticException: / by zero
    5
    org.springframework.integration.transformer.MessageTransformationException:
      Failed to transform Message; nested exception is org.springframework.messaging.MessageHandlingException:
      Expression evaluation failed: 10 / payload; nested exception is java.lang.ArithmeticException: / by zero
    5]
    ]
