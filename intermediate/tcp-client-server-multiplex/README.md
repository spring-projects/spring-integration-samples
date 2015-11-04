TCP Client-Server Multiplex Sample
==================================

If this is your first experience with the spring-integration-ip module, start with the **tcp-client-server** project in the basic folder.

That project uses outbound and inbound tcp gateways for communication. As discussed in the Spring Integration Reference Manual, this has some limitations for performance. If a shared socket (single-use="false") is used, only one message can be processed at a time (on the client side); we must wait for the response to the current request before we can send the next request. Otherwise, because only the payload is sent over tcp, the framework cannot correlate responses to requests.

An alternative is to use a new socket for each message, but this comes with a performance overhead. The solution is to use **Collaborating Channel Adapters** (see SI Reference Manual). In such a scenario, we can send multiple requests before a response is received. This is termed multiplexing.

This sample demonstrates how to configure collaborating channel adapters, on both the client and server sides, and one
technique for correlating the responses to the corresponding request.

````
gateway -> outbound-channel-adapter
        |-> aggregator

inbound-channel-adapter->aggregator->transformer
````

When the aggregator receives the reply, the group is released and transformed to just the reply which is then returned
to the gateway.

Unlike when using TCP gateways, there is no way to communicate an IO error to the waiting thread, which is sitting in
the initial `<gateway/>` waiting for a reply - it "knows" nothing about the downstream flow, such as a read timeout
on the socket.

This sample now shows how to use the `group-timeout` on the aggregator to release the group under this condition.
Further, it routes the discarded message to a service activator which return a `MessagingTimeoutException` which
is routed to the waiting thread and thrown to the caller.

````
gateway -> outbound-channel-adapter
        |-> aggregator

aggregator(group-timeout discard)->service-activator
````

A service activator is used here instead of a transformer because you may wish to take some other action when the
timeout condition occurs.

There are two test cases, one throws an exception; the other returns one as the message payload.

When the payload of the reply messsage is a `Throwable` normal gateway processing detects that and throws it to the
caller.

Similarly, when the async flow throws an exception, it is wrapped in an `ErrorMessage` and routed to the caller.

Thus, this shows both techniques for returning an exception to a gateway caller, even when the messaging is entirely
asynchronous.
