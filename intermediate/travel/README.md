Travel Sample
=============

This example demonstrates a simple process that could be useful when building travel related applications. It defines two services:

1. Get Weather report based on the ZIP (Postal) code
2. Get Traffic report based on the ZIP (Postal) code

It uses real services provided by third party providers.

* Get Weather report Service is a SOAP Web Service
* Get Traffic report Service is an HTTP Service

This example demonstrates how to configure both:

* HTTP Outbound Gateway - to integrate with HTTP Service
* WS Outbound Gateway - to integrate with WS Service

The diagram below shows the message flow:

		                                                          weatherPreProcessChannel                     weatherChannel                         weatherServiceChannel
		                                                         |------------------------| -> transformer -> |--------------| -> header-enricher -> |---------------------| -> ws:outbound-gateway 
	     |-> getWeatherByZip(zip)     routingChannel           /
	TravelGateway                --> |--------------| -> h-v-r|
	     |-> getTrafficByZip(zip)                             |     trafficChannel
	                                                           \ |-----------------| -> http:outbound-gateway     

Two services are exposed via the Gateway. The gateway enriches the headers of the message with the type of request **weather** or **traffic** and sends the Message to the **routingChannel** from which it is retrieved by the **HeaderValueRouter** (h-v-r) which routes the Message to either **weatherPreProcessChannel** or **trafficChannel** based on the value of the *REQUEST_TYPE* header hat was set by the Gateway.

The WS service requires that the message would be is a certain format. We first need to wrap it into an XML request (done by a transformer), then we need to add a SOAP Header (done by header-enricher). Once Message is properly formatted it is sent to the **ws:outbound-gateway**, which replies with XML describing real time weather conditions in a particular ZIP code. 

The HTTP Service does not require any pre-processing. All we need to do is map the HTTP URI variable **{zipCode}**.
We do it via the **<uri-variable>** element of the **http:outbound-gateway**.

	<uri-variable name="zipCode" expression="payload"/>

Here you see, we are using Spring Expression Language (SpEL) to bind the value of the payload (zipCode) to this variable. To run sample, execute **TravelDemo** class.







