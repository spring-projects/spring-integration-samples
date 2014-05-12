#Spring Integration 4.0 Java Config/DSL Demo

This sample is the demo used in the [Spring Integration 4.0 Webinar](https://spring.io/blog/2014/05/15/webinar-replay-spring-integration-4-0-the-new-frontier)

It's currently using the spring-boot 1.1.0.M1 milestone so you may have to add the __repo.spring.io/repo__ repository to your settings.xml.

There are two demo applications:

__demo.Application__ is a Spring Boot application using Spring Integration 4.0 Java configuration features.

__dsl.Application__ is the equivalent application using the new Java DSL that is currently being developed in the [extensions github repository](https://github.com/spring-projects/spring-integration-extensions/tree/master/spring-integration-java-dsl)

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


