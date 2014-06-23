Splitter/Aggregator with Reaper Sample
======================================

# Overview

Demonstration of how to implement the Splitter/Aggregator [Enterprise Integration Patterns][] (EIP) using *Spring Integration*. This sample provides a demonstration of request-reply, [splitting][] a message, and then [aggregating][] the replies. Furthermore, this sample is processing the split messages concurrently and also deals with timeout conditions. Lastly, this sample provides a concrete example of a [message store reaper][] in action.

# Run the Sample

* running the "Main" class from within STS (Right-click on Main class --> Run As --> Java Application)
* or from the command line:
    
    $ gradlew :splitter-aggregator-reaper:run

You should see the following output:

	13:47:54.886 INFO  [main][org.springframework.integration.samples.splitteraggregator.Main] 
	=========================================================
                                                         
	          Welcome to Spring Integration!                 
                                                         
	    For more information please visit:                   
	    http://www.springsource.org/spring-integration       
                                                         
	=========================================================
	13:47:56.039 INFO  [main][org.springframework.integration.store.MessageGroupStoreReaper] started org.springframework.integration.store.MessageGroupStoreReaper@4b85c17
	Please enter a choice and press <enter>: 
		1. Submit 2 search queries, 2 results returned.
		2. Submit 2 search queries, 1 search query takes too long, 1 results returned.
		3. Submit 2 search queries, 2 search queries take too long, 0 results returned.
		q. Quit the application
	Enter you choice: 1
	13:48:01.036 INFO  [searchRequestExecutor-2][org.springframework.integration.samples.splitteraggregator.SearchA] This search will take 1000ms.
	13:48:01.036 INFO  [searchRequestExecutor-1][org.springframework.integration.samples.splitteraggregator.SearchB] This search will take 1000ms.
	Number of Search Results: 2

# Credits

We would like to thank Christopher Hunt ([@huntchr](http://twitter.com/huntchr)) for contributing this sample.

--------------------------------------------------------------------------------

For help please take a look at the Spring Integration documentation:

http://www.springsource.org/spring-integration

[aggregating]: http://static.springsource.org/spring-integration/reference/html/messaging-routing-chapter.html#aggregator
[Enterprise Integration Patterns]: http://www.eaipatterns.com/
[message store reaper]: http://static.springsource.org/spring-integration/reference/html/messaging-routing-chapter.html#aggregator-config
[splitting]: http://static.springsource.org/spring-integration/reference/html/messaging-routing-chapter.html#splitter
