WS Outbound Gateway Sample
==========================

This example demonstrates the following aspects of the WS support available with Spring Integration:

1. WS Outbound Gateway
2. Content Enricher
3. Composed Message Processor

A very simple example that show you how easy it is to invoke a SOAP based service using Spring Integration.

* A Message is simply sent to a channel, where it is retrieved by a *Chain* which consists of a *Header Enricher* and a *WS Outbound Gateway*. 
* The *Header Enricher* enriches the Message with the SOAP action header. 
* The *WS Outbound Gateway* converts the Message to a SOAP request and sends it to a remote service, which converts a temperature from 
Fahrenheit to Celsius and the result is printed to the console.

To run the sample simply execute **WebServiceDemoTestApp** in package *org.springframework.integration.samples.ws*. You can also execute that class using the [Exec Maven Plugin](http://mojo.codehaus.org/exec-maven-plugin/):

    $ mvn clean package exec:java

