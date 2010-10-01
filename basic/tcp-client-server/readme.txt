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
