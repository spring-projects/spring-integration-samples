This is a place to get started with tcp. It demonstrates a simple message flow represented by the diagram below:

Gateway -> Channel -> TcpOutboundGateway -> <===Socket===> -> TcpInboundGateway -> Channel -> ServiceActivator

The service returns a response which the inbound gateway sends back over the socket to the outbound gateway
and the result is returned to the client that invoked the original SimpleGateway method. 

To run sample simply execute a test case in org.springframework.integration.samples.tcpclientservice package.


In addition, a simple telnet server is provided; see TelnetServer in src/main/java. Run this class as a
java application and then use telnet to connect to the service ('telnet localhost 11111'). 

Messages sent will be returned, preceded by 'echo:'.


$ telnet localhost 11111
Trying 127.0.0.1...
Connected to localhost.
Escape character is '^]'.
Hello world!
echo:Hello world!
Test
echo:Test
^]

telnet> quit
Connection closed.


Note that the test case also demonstrates error handling on an inbound gateway using direct channels.
If the payload is 'FAIL', the EchoService throws an exception. The gateway is configured
with an error-channel attribute. Messages sent to that channel are consumed by a transformer
that concatenates the inbound message payload with the message text from the thrown 
exception, returning 'FAIL:Failure Demonstration' over the TCP socket.

This can also be demonstrated with the telnet client thus...

$ telnet localhost 11111
Trying 127.0.0.1...
Connected to localhost.
Escape character is '^]'.
Hello world!
echo:Hello world!
FAIL
FAIL:Failure Demonstration
Hello
echo:Hello
^]

telnet> quit
Connection closed.


