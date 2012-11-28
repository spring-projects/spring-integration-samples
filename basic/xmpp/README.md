XMPP Sample
===========

This example was prepared for testing with [Google Talk][] and demonstrates the following aspects of the [Extensible Messaging and Presence Protocol][] (XMPP) support available with *Spring Integration*:

1. XMPP Inbound Channel Adapter - receive instant messages.
2. XMPP Outboud Channel Adapter - send instant messages.

In order to run this sample you need to provide correct values in **xmpp.properties**.
Everything there was already preset. The only 3 properties you need to provide values for are:

* user.login
* user.password
* send.to.user
	
You'll also need to test it with your friend or have two Google accounts setup.

To test **SendInstantMessageSample.java**, first log on to the account identified via **send.to.user** property and make sure that that account is in your buddy list. Then run the demo.
To test **ReceiveInstantMessageSample.java**, first log on to the account identified via **send.to.user** property. Then run the demo. Now any instant messages sent to your account appear in the console.

[Google Talk]: http://www.google.com/talk/
[Extensible Messaging and Presence Protocol]: http://en.wikipedia.org/wiki/Extensible_Messaging_and_Presence_Protocol