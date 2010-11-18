This example was prepared for testing with GoogleTalk and
demonstrates the following aspects of the XMPP support available with Spring Integration:
1. XMPP Inbound Channel Adapter - receive instant messages.
2. XMPP Outboud Channel Adapter - send instant messages.

In order to run this sample you need to provide correct values in xmpp.properties.
Everything there was already preset. The only 3 properties you need to provide values for are:

	- user.login
	- user.password
	- send.to.user
	
You'll also need to test it with your friend or have two Google accounts setup

	To test SendInstantMessageSample.java, first log on to the account identified via 'send.to.user' property
	and make sure that that account is in your buddy list. Then run the demo.
	
	To test ReceiveInstantMessageSample.java, first log on to the account identified via 'send.to.user' property.
	Then run the demo. When demo class is started you'll see on GoggleTalk that your buddy (ReceiveInstantMessageSample.java)
	has just signed on. Now you can send a message from GoogleTalk and see it appear ion the console.
	