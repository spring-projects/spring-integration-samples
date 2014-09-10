WebSockets Sample
==============

This example demonstrates Standard WebSocket protocol (without any sub-protocols) with Spring Integration Adapters.
It just sends current time from the server to all connected clients.

## Server

The server is presented only with single `org.springframework.integration.samples.websocket.standard.server.Application`
class, which is based on the Spring Boot AutoConfiguration and Spring Integration Java & Annotation configuration.
It is a `main` and starts embedded Tomcat on default `8080` port. The WebSocket endpoint is mapped to the `/time` path.

## Java Client

The `org.springframework.integration.samples.websocket.standard.client.Application` represents simple Java application,
 which starts an integration flow (`client-context.xml`), connects to the WebSocket server and prints `Message`s to the 
 logs, which are received over WebSocket.
   
## Browser Client
   
The `index.html` in the `resource` directory of this project demonstrates a JavaScript `SockJS` client, which connects
to our server and just prints its messages in the middle of page.
 
## Test Case

The `org.springframework.integration.samples.websocket.standard.ApplicationTests` demonstrates the Spring Boot test 
framework and starts Server & Client to check, that the last one receives correct data.
