This example demonstrates the following aspects of the JMX support available with Spring Integration:
1. JMX Attribute Polling Channel
2. JMX Operation Invoking Channel Adapter
3. Control Bus

StopWatch is a Managed Bean. It is bootstraped and deployed using annotation support (@Component, @ManagedResource) 
and component scanning functionality provided by Spring JMX. Internally StopWatch simply runs a task that increments the 
value of its 'seconds' attribute by 1 every second.

JMX Attribute Polling Channel Adapter simply polls a managed attribute 'Seconds' of the  StopWatch MBean identified by the 'org.springframework.integration.samples.jmx:type=StopWatch,name=stopWatch' name and sends its value to a 'seconds' channel. 
The interesting this is that 'seconds' channel is a publish-subscribe-channel and has two subscribers; 
- Stdout Channel Adapter which prints the value of the polled attribute to the console;
- Filter which essentially waits till payload value is 10; 
Once the payload value is 10 filter sends the Message to a 'reset' channel which is represented as JMX Operation Invoking Channel Adapter 
which simply invokes 'reset' operation on the same StopWatch MBean resetting 'Seconds' attribute value back to 1 and the process repeats.

Another example is a Control Bus which uses SpEL to send a Control Message to start/stop and inbound adapter


To run JMX Adapter sample simple execute JmxAdapterDemoTest

You will see the output similar to this which will loop for ~ 20 sec:
1
2
3
4
5
6
7
8
9
10
1
2
3
. . .

To run Control Bus sample simple execute ControlBusDemoTest

You will see the output similar to this

INFO : org.springframework.integration.samples.jmx.ControlBusDemo - Received before adapter started: null
INFO : org.springframework.integration.samples.jmx.ControlBusDemo - Received before adapter started: [Payload=Hello][Headers={timestamp=1294950897714, id=240e72fb-93b0-4d38-8fe8-b701cf7e9a5d}]
INFO : org.springframework.integration.samples.jmx.ControlBusDemo - Received after adapter stopped: null

