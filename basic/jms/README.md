JMS Sample
==========

This example demonstrates the following aspects of the Java Message Service (JMS) support available with *Spring Integration*:

1. JMS Message-driven Channel Adapter
2. JMS Inbound Gateway
3. JMS Outbound Gateway

It also uses the following components:

1. Poller
2. Stdout Channel Adapter (from Stream support Module)
3. Stdin Channel Adapter (from Stream support Module)
4. Aggregator

It also shows an example of using Spring profiles to modify the configuration for test cases.

The Stdout and Stdin Channel Adapters will allow you to interact with JMS via the console. It uses an embedded ActiveMQ broker.

To run the sample, simply execute the **Main** class located in the the *org.springframework.integration.samples.jms*
 package either from your favorite IDE or by using Gradle. When using Gradle you can start the sample by executing:

       $ gradlew :jms:run

You will then be prompted to run one of three demos:

* **GatewayDemo**
* **ChannelAdapterDemo**
* **AggregationDemo**

The console output should look like:

	=========================================================

	    Welcome to the Spring Integration JMS Sample!

	    For more information please visit:
	    http://www.springintegration.org/

	=========================================================
	16:48:21.158 INFO  [org.springframework.integration.samples.jms.Main.main()][org.springframework.integration.samples.jms.ActiveMqTestUtils] Refreshing ActiveMQ data directory.

	    Which Demo would you like to run? <enter>:

		1. Channel Adapter Demo
		2. Gateway Demo
		3. Aggregation Demo


When running any of the demos you will see the following prompt:

	> Please type something and hit <enter>

* **GatewayDemo** uses the *DemoBean* service, which will echo the response and upper-casing it.
* **ChannelAdapterDemo** will simply echo the response
* **AggregatingDemo** uses a JMS Topic; and aggregates the responses from two inbound gateways, which
invoke a flow that upper-cases the response; the aggregation returns a list of responses.


There are also test cases that exercise each demo; utilizing Spring 3.0 profiles to route the output to a QueueChannel instead of stdout.
