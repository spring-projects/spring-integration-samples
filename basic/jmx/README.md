JMX Sample
==========

This example demonstrates the following aspects of the JMX support available with Spring Integration:

1. JMX Attribute Polling Channel
2. JMX Operation Invoking Channel Adapter

**StopWatch** is a Managed Bean. It is bootstraped and deployed using annotation support (*@Component*, *@ManagedResource*) and component scanning functionality provided by Spring JMX. Internally StopWatch simply runs a task that increments the value of its **seconds** attribute by 1 every second.

The **JMX Attribute Polling Channel Adapter** simply polls a managed attribute **seconds** of the  **StopWatch** MBean identified by the name:

	org.springframework.integration.samples.jmx:type=StopWatch,name=stopWatch

It sends its value to a **seconds** channel. The interesting part is that the **seconds** channel is a *publish-subscribe-channel* and has two subscribers:

- **Stdout Channel Adapter** which prints the value of the polled attribute to the console
- **Filter** which essentially waits till the payload value is 10

Once the payload value is 10, the filter sends the Message to a **reset** channel, which is represented as **JMX Operation Invoking Channel Adapter**. That adapter simply invokes the **reset** operation on the same **StopWatch** MBean resetting the **seconds** attribute value back to 1 and the process repeats.

To run the JMX Adapter sample simply execute **JmxAdapterDemoTest**. You will see output similar to this, which will loop for ~20 seconds:

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

