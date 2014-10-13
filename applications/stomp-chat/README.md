WebSockets Stomp Chat Sample
==============

This application demonstrates the Web chat based on STOMP WebSocket sub-protocol with Spring Integration Adapters.
## Server

The server is presented only with a single `org.springframework.integration.samples.websocket.standard.server.Application`
class, which is based on the Spring Boot AutoConfiguration and Spring Integration xml configuration `@ImportResource`.
It is a `main` and starts an embedded Tomcat server on the default `8080` port. 
The WebSocket endpoint is mapped to the `/chat` path.

The server also can be run from Gradle `gradlew :stomp-chat:run`

The server application demonstrates how Spring Integration can be used as a STOMP Broker. 
 
 1. `webSocketSessionStore` - the `SimpleMetadataStore` to keep track of `WebSocketSession` and its `user`.
 2. `chatMessagesStore` - the `SimpleMessageStore` to store messages for chat rooms.
 3. `chatRoomSessions` - the `Map<String, Tuple2<String, String>>` to keep track of `WebSocketSession` `subscriptions`
 to the concrete chat room - STOMP `destination`.
 4. `<int-event:inbound-channel-adapter channel="routeStompEvents">` is subscribed to the `AbstractSubProtocolEvent` 
 type to handle STOMP sub-protocol events.
 5. `<int:payload-type-router input-channel="routeStompEvents">` is mapped to the appropriate `AbstractSubProtocolEvent`
 type to provide the specific integration flow for each event type.
 6. `<int-websocket:inbound-channel-adapter>` receives STOMP messages, store them to the appropriate `messageGroup` 
 (according to the STOMP `destination`) and forward to the `<int-websocket:outbound-channel-adapter>` to send to 
 each `WebSocketSession` subscribed to that STOMP `destination` - chat room. 

## Client
   
The `index.html` in the `src/main/resources/static` directory of this project demonstrates a JavaScript `STOMP` client
over `SockJS` client.

This application covers classical STOMP scenario:

- `connect` - requirement to enter the `user name` - chat member;
- `subscribe` - the `Join` operation on the one of chat rooms and receiving messages to that subscription for the 
destination;
- `send` and `receive` - just chat messages;
- `unsubscribe` - the `Leave` operation on the chat room: the current web socket session stops receiving messages for 
the destination;
- `disconnect` - close current web socket session and unsubscribe from all its subscriptions.

To get real chat interaction it's just enough to open several tabs in browser. 
When the user joins to the chat room, his subscription receives all messages, sent by other users to that room, 
immediately.

## Test Case

The `org.springframework.integration.samples.chat.stomp.server.ApplicationTests` demonstrates the Spring Boot test 
framework and just starts Server on the random port to be sure that this application is run correctly.
