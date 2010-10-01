This is a place to get started with tcp. It demonstrates a simple message flow represented by the diagram below:

Gateway -> Channel -> TcpOutboundGateway -> <===Socket===> -> TcpInboundGateway -> Channel -> ServiceActivator

The service returns a response which the inbound gateway sends back over the socket to the outbound gateway
and the result is returned to the client that invoked the original SimpleGateway method. 

To run sample simply execute a test case in org.springframework.integration.samples.tcpclientservice package