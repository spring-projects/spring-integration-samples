Spring Integration - Monitoring
================================================================================

This application demonstrates managing and monitoring Spring Integration Applications.

It is based on the STS 'Spring Integration Project (war)' template project, available using

    New... | Spring Template Project

It was used in the **Managing and Monitoring Spring Integration** webinar available on the [SpringSource Developer YouTube Channel][].

If you wish to see the changes made during the webinar, please use the following git command:

    git log -p <The commit titled 'Webinar End State'>


To run the sample, in STS, use:

    Run As... | Run on Server

and then use [VisualVM][]/JConsole to explore the [MBeans][].

The twitter search results can be examined at `http://localhost:8080/monitoring`.

Alternatively, there is a simple `main` class `SpringIntegrationTest`.

## 2.2 Updates:

The application context now includes an example of &lt;int-jmx:notification-publishing-channel-adapter/&gt;, to which the tweets are published. You can navigate to the MBean - **spring.application:type=TweetPublisher,name=tweeter**, click the _Notifications_ tab and then subscribe. You will then see the notifications.

An additional class _NotificationListener_ is now included. It demonstrates the use of &lt;int-jmx:notification-listening-channel-adapter&gt; and &lt;int-jmx:attribute-polling-channel-adapter&gt;.

Also, it shows how to use an &lt;int-jmx:operation-invoking-channel-adapter&gt; to stop/start the
_dummyAdapter_ in the web application.

These adapters use a client connector to connect to the Twitter search web application to catch notifications published by it, polls the _sendCount_ attribute of the _twitterChannel_. and invokes operations on the _dummyAdapter_.

You will need to update the credentials on the _clientConnector_ in _remote-monitor-context.xml_ to match your environment. If you are using STS, you can find the credentials in _Servers | [server] | jmxremote.password_. You can then run the _NotificationListener_'s main method.

You should observe log output of the notifications as well as polling the channel's _sendCount_. Simply use the console to enter 'n' to stop and 'y' to start the adapter.

These changes show how you can create a sophisticated monitoring application using Spring Integration - it is important to understand that the application being monitored doesn't have to be a Spring or Spring Integration application - any application that exports MBeans can be monitored in this way.

## Note:

Twitter now requires an authenticated user to perform searches. By default, this project now uses a dummy adapter to avoid having
to configure the credentials. To use a real
adapter, uncomment the __spring.profiles.active__ `context-param` element in the `web.xml`.

To run the `SpringIntegrationTest` command-line application, use `-Dspring.profiles.active=twitter` command line argument in the launch configuration.

However, you will need to configure OAuth and set the values in the OAuth properties.

To use OAuth authentication/authorization with Twitter you must create a new Application on the Twitter Developer's site.
Follow the directions below to create a new application and obtain the consumer keys and the access token:

* Go to [http://dev.twitter.com/](http://dev.twitter.com/)
* Log in to your account
* Go to *My applications*.
* Click on 'Create a new application' link and fill out all required fields on the form provided;
* Submit the form.
* If everything is successful you'll be presented with the 'Consumer Key' and 'Consumer Secret'.
* Copy both values to a safe place.
* On the same page you should see 'My Access Token' button on bottom of the page.
* Click on it and you'll be presented with two more values: 'Access Token' and 'Access Token Secret'.
* Copy these values to a safe place as well.

When done, fill out **oauth.properties** file so it looks similar to this.

	twitter.oauth.consumerKey=4XzBPabcJQxyBzzzH3TrRQ
	twitter.oauth.consumerSecret=ab2piKdMfPu8bVa3ab6DAIvIWEVZyMDL0RSEN2I8
	twitter.oauth.accessToken=21691649-4XYZY5iJEOfz2A9qCFd9SjBRGb3HLmIm4HNE6AMv4
	twitter.oauth.accessTokenSecret=AbRxUAvyNCtqQtvxFK8w5ZMtMj20KFhB6oEfTA0

NOTE: the above values are samples only.



--------------------------------------------------------------------------------

For help please see the Spring Integration documentation:

http://www.springsource.org/spring-integration

[MBeans]: http://docs.oracle.com/javase/tutorial/jmx/mbeans/index.html
[SpringSource Developer YouTube Channel]: http://www.youtube.com/SpringSourceDev
[VisualVM]: http://visualvm.java.net/
