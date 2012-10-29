TCP Sample
==========

This is a place to get started with the [Transmission Control Protocol][] (TCP). It demonstrates a simple message flow represented by the diagram below:

    Gateway -> Channel -> TcpOutboundGateway -> <===Socket===> -> TcpInboundGateway -> Channel -> ServiceActivator

The service returns a response which the *Inbound Gateway* sends back over the socket to the *Outbound Gateway* and the result is returned to the client that invoked the original **SimpleGateway** method. 

## Running the Sample

To run sample simply execute a test case in the **org.springframework.integration.samples.tcpclientserver** package.

Note that the test case includes an alternative configuration that uses the built-in *conversion service* and the channel *dataType* attribute, instead of explicit *Transformers*, to convert from byte arrays to Strings.

Simply change the *@ContextConfiguration* to switch between the two techniques. In addition, a simple telnet server is provided; see **TelnetServer** in *src/main/java*. Run this class as a Java application and then use `telnet` to connect to the service (**telnet localhost 11111**). 

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

>Note that the test case also demonstrates error handling on an inbound gateway using direct channels. If the payload is 'FAIL', the EchoService throws an exception. The gateway is configured >with an error-channel attribute. Messages sent to that channel are consumed by a transformer that concatenates the inbound message payload with the message text from the thrown exception, >returning **FAIL:Failure Demonstration** over the TCP socket.

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

A third option exists for converting a stream of bytes to a domain object or message payload. You can hook up different serializers/deserializers at the connection factory which will apply the conversions right when the stream comes in to the *Gateway* and right when it goes out.

See **TcpServerConnectionDeserializeTest** for using a simple (comes with spring) Stx/Etx serializer.

See **TcpServerCustomSerializerTest** for creating and using your own serializers

[Transmission Control Protocol]: http://en.wikipedia.org/wiki/Transmission_Control_Protocol