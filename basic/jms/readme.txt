This example demonstrates the following aspects of the JMS support available with Spring Integration:
1. JMS Message-driven Channel Adapter
2. JMS Inbound Gateway
3. JMS Outbound Gateway

as well as Poller and stdout and stdin Channel Adapters (from Stream support) which will allow you to interact with JMS via the console.
It uses ActiveMQ embedded broker

To run samples simply execute GatewayDemo and ChannelAdapterDemo classes located in the org.springframework.integration.samples.jms package

When running demos you will see the following prompt:
> Please type something and hit return

GatewayDemo uses DemoBean service which will echo the response upper-casing it

ChannelAdapterDemo will simply echo the response