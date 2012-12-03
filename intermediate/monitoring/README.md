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

## 2.2 Updates:

The application context now includes an example of &lt;int-jmx:notification-publishing-channel-adapter/&gt;, to which the tweets are published. You can navigate to the MBean - **spring.application:type=TweetPublisher,name=tweeter**, click the _Notifications_ tab and then subscribe. You will then see the notifications.

An additional class _NotificationListener_ is now included. It demonstrates the use of &lt;int-jmx:notification-listening-channel-adapter&gt; and &lt;int-jmx:attribute-polling-channel-adapter&gt;.

Also, it shows how to use an &lt;int-jmx:operation-invoking-channel-adapter&gt; to stop/start the
_dummyAdapter_ in the web application.

These adapters use a client connector to connect to the Twitter search web application to catch notifications published by it, polls the _sendCount_ attribute of the _twitterChannel_. and invokes operations on the _dummyAdapter_.

You will need to update the credentials on the _clientConnector_ in _remote-monitor-context.xml_ to match your environment. If you are using STS, you can find the credentials in _Servers | [server] | jmxremote.password_. You can then run the _NotificationListener_'s main method.

You should observe log output of the notifications as well as polling the channel's _sendCount_. Simply use the console to enter 'n' to stop and 'y' to start the adapter.

These changes show how you can create a sophisticated monitoring application using Spring Integration - it is important to understand that the application being monitored doesn't have to be a Spring or Spring Integration application - any application that exports MBeans can be monitored in this way.

--------------------------------------------------------------------------------

For help please see the Spring Integration documentation:

http://www.springsource.org/spring-integration

[MBeans]: http://docs.oracle.com/javase/tutorial/jmx/mbeans/index.html
[SpringSource Developer YouTube Channel]: http://www.youtube.com/SpringSourceDev
[VisualVM]: http://visualvm.java.net/
