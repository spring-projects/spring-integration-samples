Twitter Sample
==============

This example demonstrates the following aspects of the Twitter support available with Spring Integration:

1. Twitter Inbound Channel Adapter - allows you to receive HOME status updates
2. Twitter Inbound Search Channel Adapter - allows you to receive HOME status updates
3. Twitter Outbound Channel Adapter - allows send status updates

In order to run this sample you need to configure [OAuth][] and set the values in the [OAuth][] properties.

To use [OAuth][] authentication/authorization with Twitter you must create a new Application on the Twitter Developers site. 
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

NOTE: the above values are not real ;)

Now you are ready to execute the samples. Just run each sample and look for the output produced by the inbound adapters (**TwitterSearchSample.java** and **TwitterTimelineUpdateSample.java**)
The outbound adapter sample (**TwitterSendUpdatesSample.java**) will not produce any output. Instead within seconds you should see your tweet.

[OAuth]: http://en.wikipedia.org/wiki/OAuth