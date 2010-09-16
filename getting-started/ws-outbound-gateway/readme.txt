This example demonstrates the following aspects of the WS support available with Spring Integration:
1. WS Outbound Gateway

as well as:
2. Content Enricher
3. Composed Message Processor

A very simple example that show you how easy it is to invoke a SOAP based service using Spring Integration

Message is simply sent to a channel where it is retrieved by a Chain which consists of Header Enricher and WS Outbound Gateway
Header Enricher enriches Message with the SOAP action header
WS Outbound Gateway converts Message to a SOAP request and sends it to a remote service which converts temperature from 
Fahrenheit to Celsius and sent back where the result is printed to a console.

To run sample simply execute WebServicesDemoTest
