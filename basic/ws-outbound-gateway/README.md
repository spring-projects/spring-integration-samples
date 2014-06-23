WS Outbound Gateway Sample
==========================

This example demonstrates the following aspects of the Web Services (WS) support available with *Spring Integration*:

1. WS Outbound Gateway
2. Content Enricher
3. Composed Message Processor

A very simple example that show you how easy it is to invoke a service based on the [Simple Object Access Protocol][] (SOAP) using *Spring Integration*.

* A *Message* is sent to a *Channel*, where it is retrieved by a *Chain* which consists of a *Header Enricher* and a *WS Outbound Gateway*. 
* The *Header Enricher* enriches the *Message* with the SOAP action header. 
* The *WS Outbound Gateway* converts the *Message* to a SOAP request and sends it to a remote service, which converts a temperature from 
Fahrenheit (90F) to Celsius (32.2C) and the result is printed to the console:

````
<?xml version="1.0" encoding="UTF-8"?><FahrenheitToCelsiusResponse xmlns="http://tempuri.org/"><FahrenheitToCelsiusResult>32.2222222222222</FahrenheitToCelsiusResult></FahrenheitToCelsiusResponse>````

## Running the Sample

To run the sample simply execute **WebServiceDemoTestApp** in package *org.springframework.integration.samples.ws*. 
You can also execute that class using the [Gradle Application Plugin](http://www.gradle.org/docs/current/userguide/application_plugin.html):

    $ gradlew :ws-outbound-gateway:run

[Simple Object Access Protocol]: http://en.wikipedia.org/wiki/SOAP
