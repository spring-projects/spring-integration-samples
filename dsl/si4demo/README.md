# Spring Integration 4.0/4.1 Java Config/DSL Demo

This sample is the demo used in the [Spring Integration 4.0 Webinar](https://spring.io/blog/2014/05/15/webinar-replay-spring-integration-4-0-the-new-frontier) and SpringOne/2GX 2014.

There are two demo applications from the webinar:

__org.springframework.integration.samples.javaconfig.annotations.Application__ is a Spring Boot application using 
Spring Integration 4.0 Java configuration features.

__org.springframework.integration.samples.javaconfig.dsl.Application__ is the equivalent application using the new Java 
DSL that is currently being developed in the [extensions github repository](https://github.com/spring-projects/spring-integration-extensions/tree/master/spring-integration-java-dsl)

In both cases, you can use Telnet or curl to search twitter


    $ telnet localhost 9876
    Trying 127.0.0.1...
    Connected to localhost.
    Escape character is '^]'.
    #springintegration
    [{"extraData":{},"id":461548132401438720,"text":"RT @gprussell: Spring Integration 4.0.0.RELEASE is out! ...

    $ curl http://localhost:8080/foo -H"content-type:text/plain" -d '#springintegration'
    [{"extraData":{},"id":461548132401438720,"text":"RT @gprussell: Spring Integration 4.0.0.RELEASE is out! ...

The DSL  version also accepts typing in a hashtag for the search in the console. The DSL version also adds a filter to only allow hashtags starting with `#spring`, and only returns the first tweet.

Twitter now requires authentication to perform searches; visit the [twitter developer site](http://dev.twitter.com) to set up the application and enter the keys/secrets in _application.yml_ on the classpath. An 'empty' yaml file is provided in _src/main/resources:

    twitter:
      oauth:
        consumerKey:
        consumerSecret:
        accessToken:
        accessTokenSecret:



# SpringOne/2GX 2014

Additional examples were added to this project at __SpringOne2GX 2014__ - see the __springone__ package.

This demonstrates moving from completely XML configuration, through Java Configuration, and ultimately to the DSL.

## AXML

This example is pure XML - it's a simple flow...

    gateway -> transformer -> service-activator

The transformer concatentates the payload to itself (String), the service upper cases the payload. The result is returned to the gateway. "foo" becomes "FOOFOO".

## BXMLAndPojo

This takes the same example and shows how to configure it using "classic" Spring Integration annotations, available since 2.0.

## CNoXML

This takes the same example and configures it using standard Java Configuration (available since 4.0). Many of the standard annotations are now available on __@Bean__ definitions; note that the output channel must be configured on the handler, not the annotation. __@IntegrationComponentScan__ detects __@MessagingGateway__ interfaces and creates gateways.

## DBoot

This takes the same example as __C...__ and configures it using Spring Boot. The configuration is slightly more concise.

Note: Other applications in this project use the embedded web server (for http adapters). The boot apps in this section disable the embedded server by using the __SpringApplicationBuilder__ ...

				new SpringApplicationBuilder(DBoot.class)
					.web(false)
					.run(args);

## EDSL

This takes the same example and configures it using the Spring Integration Java DSL. It requires Spring Integration 4.1.0.M1 and DSL 1.0.0.M3.

## FMail

This adds a recipient list router and sends a copy of the payload to an SMTP Email server (add credentials to __application.yml__).

## GIMAP

This is a separate application that demonstrates the DSL configuring an IMAP idle channel adapter, to receive emails sent by __FEmail__.

