TCP Client-Server Multiplex Sample
==================================

If this is your first experience with the spring-integrtion-ip module, start with the **tcp-client-server** project in the basic folder.

That project uses outbound and inbound tcp gateways for communication. As discussed in the Spring Integration Reference Manual, this has some limitations for performance. If a shared socket (single-use="false") is used, only one message can be processed at a time (on the client side); we must wait for the response to the current request before we can send the next request. Otherwise, because only the payload is sent over tcp, the framework cannot correlate responses to requests.

An alternative is to use a new socket for each message, but this comes with a performance overhead. The solution is to use **Collaborating Channel Adapters** (see SI Reference Manual). In such a scenario, we can send multiple requests before a response is received. This is termed multiplexing.

This sample demonstrates how to configure collaborating channel adapters, on both the client and server sides, and one technique for correlating the responses to the corresponding request.