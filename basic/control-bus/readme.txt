This example demonstrates the functionality of the Control Bus component available with Spring Integration:

Control Bus  uses SpEL to send a Control Message to start/stop and inbound adapter

To run Control Bus sample simple execute ControlBusDemoTest

You will see the output similar to this

INFO : org.springframework.integration.samples.jmx.ControlBusDemo - Received before adapter started: null
INFO : org.springframework.integration.samples.jmx.ControlBusDemo - Received before adapter started: [Payload=Hello][Headers={timestamp=1294950897714, id=240e72fb-93b0-4d38-8fe8-b701cf7e9a5d}]
INFO : org.springframework.integration.samples.jmx.ControlBusDemo - Received after adapter stopped: null

