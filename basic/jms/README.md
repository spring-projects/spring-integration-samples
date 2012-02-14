JMS Sample
==========

This example demonstrates the following aspects of the JMS support available with Spring Integration:

1. JMS Message-driven Channel Adapter
2. JMS Inbound Gateway
3. JMS Outbound Gateway

It also uses the following components:

1. Poller
2. Stdout Channel Adapter (from Stream support Module)
3. Stdin Channel Adapter (from Stream support Module) 

The Stdout and Stdin Channel Adapters will allow you to interact with JMS via the console. It uses an embedded ActiveMQ broker.

To run the samples, simply execute the **GatewayDemo** and **ChannelAdapterDemo** classes located in the *org.springframework.integration.samples.jms* package.

When running demos you will see the following prompt:

	> Please type something and hit return

* **GatewayDemo** uses *DemoBean* service, which will echo the response, upper-casing it.
* **ChannelAdapterDemo** will simply echo the response