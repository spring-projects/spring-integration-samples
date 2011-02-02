A series of test cases that show techniques to test Spring Integration applications.


Examples
========

...spelchain.SpelChainTests.java

This test case shows how to test a fragment of a larger integration flow.
In this example, we have a simple chain containing a header enricher and
a transformer. It uses Direct channels. The test case shows how to 
bridge the output channel to a queue channel so we can retrieve the
output message after the test executes and verify the appropriate
actions were applied to the input message.


...splitter.CommaDelimitedSplitterTests.java

This test case shows both unit testing a custom splitter class as well
as testing it within a Spring Integration flow fragment.


...aggregator.CommaDelimitedAggregatorTests.java

This test case shows both unit testing a custom aggregator class as well
as testing it within a Spring Integration flow fragment.


...aggregator.ExternalGatewaySubstitutionTests.java

This example is a little more complex. The application is simplified version of a 
travel demo presented at SpringOne in 2010. You do not need to fully understand
the application for the purposes of this demonstration. Suffice it to 
say that a zip code is sent to two outbound gateways (one web service, one http) and
the weather and traffic for that zip code are aggregated together. You 
can run the "real" application by executing the Main class in src/main/java
and entering a valid zip code in the console.

In many cases, when integrating with external services, we can't rely on 
those services being available while we test. Consequently, we need a 
mechanism to stub out those services so we can run the remainder of the
flow in a repeatable manner without need for connectivity to the services.
This example uses dummy service activators in place of the real outbound
gateways.

This separation of 'infrastructure' from application beans is similar to 
the way we separate DataSource, ConnectionFactory beans etc so that the
application can be tested with local versions.