Travel Sample
=============

This example demonstrates a simple process that could be useful when building travel related applications. It defines two services:

1. Get Weather report based on the ZIP (Postal) code
2. Get Traffic report based on latitude/longitude information

It uses real services provided by third party providers.

* Get Weather report Service is a SOAP Web Service
* Get Traffic report Service is an HTTP Service

This example demonstrates how to configure both:

* HTTP Outbound Gateway - to integrate with HTTP Service
* WS Outbound Gateway - to integrate with WS Service

The diagram below shows the message flow:

		                                                          weatherPreProcessChannel                     weatherChannel                         weatherServiceChannel
		                                                         |------------------------| -> transformer -> |--------------| -> header-enricher -> |---------------------| -> ws:outbound-gateway --\
	     |-> getWeatherByZip(zip)     routingChannel           /                                                                                                                                       |      prettifyXml
	TravelGateway                --> |--------------| -> h-v-r|                                                                                                                                        |---->|------------| -> transformer
	     |-> getTrafficByZip(zip)                             |     trafficChannel                                                                                                                    /
	                                                           \ |-----------------| -> header-enricher -> http:outbound-gateway --------------------------------------------------------------------/

Two services are exposed via the **Gateway**. The gateway enriches the headers of the message with the type of request **weather** or **traffic** and sends the Message to the **routingChannel** from which it is retrieved by the **HeaderValueRouter** (h-v-r) which routes the Message to either **weatherPreProcessChannel** or **trafficChannel** based on the value of the *REQUEST_TYPE* header hat was set by the Gateway.

The WS service requires that the message would be is a certain format. We first need to wrap it into an XML request (done by a transformer), then we need to add a SOAP Header (done by header-enricher). Once Message is properly formatted it is sent to the **ws:outbound-gateway**, which replies with XML describing real time weather conditions in a particular ZIP code.

The HTTP Service does not require any pre-processing. All we need to do is map the HTTP URI variable **{boundingBox}** (Contains latitude/longitude coordinates).
We accomplish that via the **<uri-variable>** element of the **http:outbound-gateway**.

	<uri-variable name="zipCode" expression="payload"/>

Here you see, we are using *Spring Expression Language* (SpEL) to bind the value of the payload (boundingBox) to this variable.

# Setup

## MapQuest

This sample uses the [MapQuest API][], specifically the [MapQuest Traffic Web Service][]. As such you must setup an API Key. Therefore, please create a MapQuest developer account. This can be done at: http://developer.mapquest.com/.

¡**Important**! - Please be aware that the API key you received from MapQuest is URL encoded. As such you must decode the key, so you can use it with Spring Integration. For example you can use the service on the following site to decode the API key: http://meyerweb.com/eric/tools/dencoder/

## Running the Sample

To run the sample execute **Main** in package **org.springframework.integration.samples.travel**. 
You can also execute that class using the [Gradle Application Plugin](http://www.gradle.org/docs/current/userguide/application_plugin.html):

    $ gradlew :travel:run -Dmapquest.apikey="your_mapquest_api_key_url_decoded"

You should see the following output:

	=========================================================

	    Welcome to the Spring Integration Travel App!

	    For more information please visit:
	    http://www.springintegration.org/

	=========================================================
	Please select the city, for which you would like to get traffic and weather information:
		1. Atlanta
		2. Boston
		3. San Francisco
		q. Quit the application
	Enter your choice:

# Changes

**2012-Oct-30**

* The Yahoo Traffic API has been discontinued. As an alternative, the [MapQuest Traffic Web Service] is being used now
* Added support for multiple cities (Atlanta, Boston, San Francisco)


[MapQuest API]: http://www.mapquestapi.com/
[MapQuest Traffic Web Service]: http://platform.beta.mapquest.com/traffic/






